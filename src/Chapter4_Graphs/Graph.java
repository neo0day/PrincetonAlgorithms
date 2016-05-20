package Chapter4_Graphs;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class Graph {
	private static final String NEWLINE = System.getProperty("line.separator");

	private int V;
	private int E;
	private List<List<Integer>> adj;
	private boolean[] marked;

	public Graph(int V) {
		if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
		this.V = V;
		this.E = 0;
		adj = new ArrayList<List<Integer>>(V);
		for (int v = 0; v < V; v++) {
			List<Integer> list = new LinkedList<Integer>();
			adj.add(list);
		}
	}

	public Graph(Scanner in) {
		this(in.nextInt());
		int E = in.nextInt();
		if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
		for (int i = 0; i < E; i++) {
			int v = in.nextInt();
			int w = in.nextInt();
			addEdge(v, w);
		}
	}

	public int V() {
		return V;
	}

	public int E() {
		return E;
	}

	// throw an IndexOutOfBoundsException unless 0 <= v < V
	private void validateVertex(int v) {
		if (v < 0 || v >= V)
			throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V-1));
	}

	public void addEdge(int v, int w) {
		validateVertex(v);
		validateVertex(w);
		E++;
		adj.get(v).add(w);
		adj.get(w).add(v);
	}

	public Iterable<Integer> adj(int v) {
		validateVertex(v);
		return adj.get(v);
	}

	public int degree(int v) {
		validateVertex(v);
		return adj.get(v).size();
	}


	/**
	 * Ex 4.1.3
	 * deep copy a graph
	 */
	public Graph(Graph G) {
		this(G.V());
		this.E = G.E();
		for (int v = 0; v < G.V(); v++) {
			for (int w : G.adj.get(v))
				this.adj.get(v).add(w);
		}
	}

	/**
	 * Ex 4.1.4
	 * Add a method hasEdge() to Graph
	 */
	public boolean hasEdge(int v, int w) {
		marked = new boolean[this.V()];
		if (w == v) return false;
		return dfs(v, w);
	}

	private boolean dfs(int v, int w) {
		if (v == w) return true;
		marked[v] = true;
		for (int neib : this.adj.get(v)) {
			if (!marked[neib]) {
				if (dfs(neib, w))
					return true;
			}
		}
		return false;
	}

	/**
	 * Ex 4.1.5
	 * Modify Graph to disallow parallel edges and self-loops.
	 */
	public void validateEdge(int v, int w) throws Exception {
		if (v == w)
			throw new Exception("vertex " + v + " has self-loop");
		for (int u : this.adj.get(v))
			if (u == w)
				throw new Exception("vertex " + v + " has parallel edge");
	}

	/**
	 * Ex 4.1.7
	 * Returns a string representation of this graph.
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(V + " vertices, " + E + " edges " + NEWLINE);
		for (int v = 0; v < V; v++) {
			s.append(v + ": ");
			for (int w : adj.get(v)) {
				s.append(w + " ");
			}
			s.append(NEWLINE);
		}
		return s.toString();
	}

	/**
	 * Ex 4.1.8
	 * Develop an implementation for the Search API
	 */
	public class Search {
		private int s;
		private WeightedQuickUnionUF uf;

		public Search(Graph G, int s) {
			this.s = s;
			this.uf = new WeightedQuickUnionUF(G.V());
			for (int v = 0; v < G.V(); v++) {
				for (int w : G.adj.get(v)) {
					if (uf.connected(v, w))
						continue;
					uf.union(v, w);
				}
			}
		}
		
		boolean marked(int v) {
			return uf.connected(s, v);
		}

		int count() {
			return uf.count(s);
		}

	}

	/**
	 * Ex 4.1.10
	 * finds a vertex whose removal will not disconnect the graph
	 * 		  
	 */
	public List<Integer> non_articulation_vertex() {
		vertexClassification();
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < V; i++) {
			if (!articulation[i])
				list.add(i);
		}
		return list;
	}

	private boolean[] articulation;
	private int time;
	private int[] disc;
	private int[] low;
	public void vertexClassification() {
		articulation = new boolean[this.V()];
		disc = new int[this.V()];
		low = new int[this.V()];
		Arrays.fill(disc, -1);
		Arrays.fill(low, -1);
		time = 0;
		for (int v = 0; v < this.V(); v++) {
			if (disc[v] == -1)
				dfs_ap(v, v);
		}
	}

	private void dfs_ap(int parent, int cur_v) {
		int children = 0;
		disc[cur_v] = time++;
		low[cur_v] = disc[cur_v];
		for (int neib : adj.get(cur_v)) {
			if (disc[neib] == -1) {
				children++;
				dfs_ap(cur_v, neib);
				// update low number
				low[cur_v] = Math.min(low[cur_v], low[neib]);
				// non-root of DFS is an articulation point if low[neib] >= disc[cur_v]
				if (low[neib] >= disc[cur_v] && parent != cur_v)
					articulation[cur_v] = true;
			}
			// update low number - ignore reverse of edge leading to cur_v
			else if (neib != parent)
				low[cur_v] = Math.min(low[cur_v], disc[neib]);
		}
		// root of DFS is an articulation point if it has more than 1 child
		if (parent == cur_v && children > 1)
			articulation[cur_v] = true;
	}

	/**
	 * Ex 4.1.13
	 * Add a distTo() method
	 */
	public class BFS {
		private int[] distTo;
		public BFS(Graph G, int s) {
			marked = new boolean[G.V()];
			distTo = new int[G.V()];
			bfs(G, s);
		}

		private void bfs(Graph G, int s) {
			Queue<Integer> q = new LinkedList<Integer>();
			for (int v = 0; v < G.V(); v++)
				distTo[v] = Integer.MAX_VALUE;
			distTo[s] = 0;
			marked[s] = true;
			q.add(s);
			while(!q.isEmpty()) {
				int v = q.poll();
				for (int w : G.adj.get(v)) {
					if (!marked[w]) {
						distTo[w] = distTo[v] + 1;
						marked[w] = true;
						q.add(w);
					}
				}
			}
		}
		public int distTo(int v) {
			return distTo[v];
		}
	}

	/**
	 * Ex 4.1.15
	 * Modify the input stream constructor for Graph
	 */
	public Graph(Scanner in, String sp) {
		this(in.nextInt());
		this.E = in.nextInt();
		if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
		in.nextLine();
		List<Stack<Integer>> stacks = new ArrayList<>();
		for (int i = 0; i < V; i++)
			stacks.add(new Stack<Integer>());
		while (in.hasNextLine())
		{
			String[] arr = in.nextLine().split(sp);
			int v = Integer.parseInt(arr[0]);
			for (int i = 1; i < arr.length; i++) {
				int w = Integer.parseInt(arr[i]);
				stacks.get(v).push(w);
				stacks.get(w).push(v);
			}
		}
		for (int i = 0; i < V; i++) {
			Stack<Integer> stack = stacks.get(i);
			while (!stack.isEmpty())
				adj.get(i).add(stack.pop());
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File(args[0]));
		Graph G = new Graph(in);
		System.out.println(G);
		// test for ex 4.1.3
		Graph G2 = new Graph(G);
		System.out.println(G2);
		// test for ex 4.1.4
		System.out.println("The path " + (G.hasEdge(2,11) ? "exists" : "does not exist"));
		// test for ex 4.1.8
		Search search = G.new Search(G, 0);
		System.out.println(search.marked(10));
		System.out.println(search.marked(8));
		System.out.println(search.count());
		// test for ex 4.1.10
		in = new Scanner(new File(args[1]));
		Graph G3 = new Graph(in);
		System.out.println(G3.non_articulation_vertex());
		// test for ex 4.1.13
		BFS bfs = G.new BFS(G, 0);
		System.out.println(bfs.distTo(3));
		System.out.println(bfs.distTo(10));
		// test for ex 4.1.15
		in = new Scanner(new File(args[2]));
		Graph G4 = new Graph(in, " ");
		System.out.println(G4);
		// test for ex 4.1.16
		in = new Scanner(new File(args[3]));
		Graph G5 = new Graph(in);
		GraphProperties gp = new GraphProperties(G5);
		System.out.println("diameter: " + gp.diameter() + " radius: " + gp.radius() + " center: " + gp.center());
		// test for ex 4.1.17
		System.out.println("wiener: " + gp.wiener());
		// test for ex 4.1.18
		System.out.println("girth: " + gp.girth());
	}

}
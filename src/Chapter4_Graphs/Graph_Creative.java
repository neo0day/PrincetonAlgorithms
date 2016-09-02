package Chapter4_Graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Graph_Creative {

	private int[] disc;			// the discover time
	private int[] low;			// the earliest discover time
	private int time;
	private int[] parent;
	private int[] degree;
	private int odd;
	private int count;
	private boolean[][] bridge;
	private boolean[][] edge_visited;
	private int[] path;
	private int pos;

	/**
	 * Ex 4.1.30
	 * Eulerian and Hamiltonian cycles.
	 */
	public Iterable<Integer> euler_cycles(Graph G) {
		disc = new int[G.V()];
		low = new int[G.V()];
		time = 0;
		Arrays.fill(disc, -1);
		Arrays.fill(low, -1);
		parent = new int[G.V()];
		degree = new int[G.V()];
		odd = 0;
		count = 0;
		bridge = new boolean[G.V()][G.V()];
		edge_visited = new boolean[G.V()][G.V()];
		path = new int[G.E() + 1];
		pos = 0;
		List<Integer> cycle = new LinkedList<>();
		if (has_euler_cycles(G)) {
			System.out.print("Euler cycle is: ");
			path[pos++] = 0;
			find_euler_cycle(G, 0);
			for (int i = 0; i < path.length; i++)
				cycle.add(path[i]);
		}
		return cycle;
	}

	// Therom: A connected graph with even degree at each vertex has an Eulerian circuit.
	private boolean has_euler_cycles(Graph G) {
		dfs(G, 0);
		return odd == 0 && count == G.V();
	}

	// this also finds the bridge
	private void dfs(Graph G, int v) {
		disc[v] = time++;
		low[v] = disc[v];
		count++;
		for (int w : G.adj(v)) {
			degree[v]++;
			parent[w] = v;
			if (disc[w] == -1) {
				dfs(G, w);
				// check if the subtree rooted with w has a connection to one of the ancestors of v
				low[v] = Math.min(low[v], low[w]);
				// if the lowest vertex reachable from subtree under w is below v in DFS tree,
				// then u-v is a bridge
				if (low[w] > disc[v]) {
					System.out.println(v + " " + w);
					bridge[v][w] = true;
					bridge[w][v] = true;
				}
			}
			// Update low value of v for parent function calls.
			else if (w != parent[v])
				low[v]  = Math.min(low[v], disc[w]);
		}
		if (degree[v] % 2 != 0)
			odd++;
	}

	/**
	 *  Don't burn your bridges
	 * 	1) avoid the bridge if there are more edges unvisited
	 *  2) use the bridge if this is the only edge available
	 */
	public void find_euler_cycle(Graph G, int v) {
		for (int w : G.adj(v)) {
			if (isValidEdge(v, w)) {
				// mark edge as visited to indicate the edge get removed 
				// and update degree to reduce the available adjacent vertices
				edge_visited[v][w] = true;
				edge_visited[w][v] = true;
				degree[v]--;
				degree[w]--;
				find_euler_cycle(G, w);
			}
		}
		path[pos++] = v;
	}

	private boolean isValidEdge(int v, int w) {
		if (degree[v] == 0 || edge_visited[v][w]) return false;
		if (degree[v] == 1) return true;
		return !bridge[v][w];
	}

	/**
	 * Ex 4.1.32
	 * Parallel edge detection. 
	 */
	private boolean[] marked;
	public int count_parallel_edges(Graph G) {
		marked = new boolean[G.V()];
		count = 0;
		for (int v = 0; v < G.V(); v++) {
			if (!marked[v])
				dfs_cpe(G, v);
		}
		return count / 2;		// we counted twice
	}

	private void dfs_cpe(Graph G, int v) {
		int[] edge_count = new int[G.V()];
		marked[v] = true;
		for (int w : G.adj(v)) {
			if (edge_count[w] == 1)
				count++;
			edge_count[w]++;
			if (edge_count[w] > 1)
				count++;
			if (!marked[w])
				dfs_cpe(G, w);
		}
	}

	/**
	 * Ex 4.1.36
	 * Two-edge connectivity.
	 */
	public boolean isTwoEdgeConnected(Graph G) {
		disc = new int[G.V()];
		low = new int[G.V()];
		Arrays.fill(disc, -1);
		Arrays.fill(low, -1);
		time = 0;
		for (int v = 0; v < G.V(); v++) {
			if (disc[v] == -1)
				if (dfs_has_bridge(G, v, v))
					return false;
		}
		return true;
	}

	private boolean dfs_has_bridge(Graph G, int parent, int cur_v) {
		disc[cur_v] = time++;
		low[cur_v] = disc[cur_v];
		for (int neib : G.adj(cur_v)) {
			if (parent == neib)
				continue;
			if (disc[neib] == -1) {
				if (dfs_has_bridge(G, cur_v, neib))
					return true;
				if (low[neib] > disc[cur_v])
					return true;				// has bridge
				// update low number
				low[cur_v] = Math.min(low[cur_v], low[neib]);
			}
			else {
				// update low number
				low[cur_v] = Math.min(low[cur_v], disc[neib]);
			}
		}
		return false;
	}

	private int[] color;
	/**
	 * Ex 4.1.38
	 * Image processing.
	 */
	public void floor_fill(Graph G, int s, int new_color) {
		marked = new boolean[G.V()];
		Queue<Integer> Q = new LinkedList<>();
		int old_color = color[s];
		marked[s] = true;
		Q.add(s);
		while (!Q.isEmpty()) {
			int v = Q.poll();
			color[v] = new_color;
			for (int w : G.adj(v)) {
				if (!marked[w]) {
					marked[w] = true;
					if (color[w] == old_color)
						Q.add(w);
				}
			}
		}
	}

	/**
	 * Ex 4.2.21
	 * LCA of a DAG.
	 */
	private int[] height;
	public int DAG_LCA (Digraph G, int v, int w) {
		Topological T = new Topological(G);
		parent = new int[G.V()];
		height = new int[G.V()];
		Arrays.fill(height, Integer.MIN_VALUE);
		// Get Topological order to calculate the longest path
		if (T.hasOrder()) {
			for (int node : T.order()) {
				if (height[node] < 0) {		// no parent, create a root
					height[node] = 0;
					parent[node] = node;
				}
				for (int u : G.adj(node)) {
					if (height[node] + 1 > height[u]) {
						height[u] = height[node] + 1;
						parent[u] = node;
					}
				}
			}
		}
		return findLCA(v, w);
	}

	private int findLCA(int v, int w) {
		// always assume height[v] > height[w]
		if (height[w] > height[v])
			return findLCA(w, v);
		for (int i = height[v]; i > height[w]; i--) {
			v = parent[v];
		}
		while (v != parent[v]) {
			if (v == w) break;
			v = parent[v];
			w = parent[w];
		}
		if (v != w) return -1;
		return v;
	}

	/**
	 * Ex 4.2.22
	 * Shortest ancestral path.
	 */
	public Iterable<Iterable<Integer>> SAP(Digraph G, int v, int w) {
		List<Iterable<Integer>> result = new LinkedList<>();
		// BFS from v and w to get shortest paths to all vertices
		BreadthFirstDirectedPaths vPaths = new BreadthFirstDirectedPaths(G, v);
		BreadthFirstDirectedPaths wPaths = new BreadthFirstDirectedPaths(G, w);
		// find the shortest ancestor
		int ancestor = findSA(G, vPaths, wPaths);
		// get shortest ancestor path
		Iterable<Integer> vSAP = vPaths.pathTo(ancestor);
		Iterable<Integer> wSAP = wPaths.pathTo(ancestor);
		result.add(vSAP);
		result.add(wSAP);
		return result;
	}

	// common ancestor in shortest ancestral path
	private int findSA(Digraph G, BreadthFirstDirectedPaths vPaths,
			BreadthFirstDirectedPaths wPaths) {
		int ancestor = -1, min = -1, distance;
		for (int v = 0; v < G.V(); v++) {
			if (vPaths.hasPathTo(v) && wPaths.hasPathTo(v)) {
				distance = vPaths.distTo(v) + wPaths.distTo(v);
				if (min < 0 || distance < min) {
					min = distance;
					ancestor = v;
				}
			}
		}
		return ancestor;
	}

	/**
	 * Ex 4.2.23
	 * Strong component. 
	 */
	public Iterable<Integer> strongComponent(Digraph G, int v) {
		marked = new boolean[G.V()];
		List<Integer> list = new LinkedList<>();
		DepthFirstOrder order = new DepthFirstOrder(G.reverse());
		for (int s : order.reversePost())     // discover the SCCs one by one
			if (!marked[s]) {
				list = new LinkedList<>();
				dfs(G, s, list);
				if (marked[v])		// v has been visited
					break;
			}
		return list;
	}

	private void dfs(Digraph G, int u, List<Integer> list) {
		marked[u] = true;
		list.add(u);
		for (int w : G.adj(u)) {
			if (!marked[w])
				dfs(G, w, list);
		}
	}

	/**
	 * Ex 4.2.24 
	 * Hamiltonian path in DAGs.
	 */
	public boolean hasHamiltonianPath(Digraph G) {
		Topological T = new Topological(G);
		if (T.hasOrder()) {
			Iterator<Integer> itr = T.order().iterator();
			int prev = itr.next();
			while (itr.hasNext()) {
				int cur = itr.next();
				if (!G.hasEdge(prev, cur))
					return false;
				prev = cur;
			}
		} else
			return false;
		return true;
	}

	/**
	 * Ex 4.2.25
	 * Unique topological ordering.
	 */
	public boolean isUniqueTopological(Digraph G) {
		return hasHamiltonianPath(G);
	}

	/**
	 * Ex 4.2.26
	 * 2-satisfiability. 
	 * @throws FileNotFoundException 
	 */
	public void find2SAT(String filename) throws FileNotFoundException {
		/**
		 * Build Symbol Digraph
		 */
		HashMap<String, Integer> st = new HashMap<>();	// string -> index
		String[] keys;           // index  -> string
		List<String> literals = new LinkedList<>();

		// First pass builds the index by reading literals and their negation
		Scanner in = new Scanner(new File(filename));
		while (in.hasNext()) {
			String literal = in.next();
			String x = "";
			String negx = "";
			if (literal.charAt(0) == '-') {
				x = literal.substring(1);
				negx = literal;
			}
			else {
				x = literal;
				negx = '-' + x;
			}
			if (!st.containsKey(x)) {
				st.put(x, st.size());
				literals.add(x);
			}
			if (!st.containsKey(negx))
				st.put(negx, st.size());
		}

		// inverted index to get string keys in an aray
		keys = new String[st.size()];
		for (String name : st.keySet()) {
			keys[st.get(name)] = name;
		}

		// second pass builds the digraph by adding two directed edges for x_i + y_i
		Digraph G = new Digraph(st.size());
		in = new Scanner(new File(filename));
		while (in.hasNextLine()) {
			String x = in.next();
			String y = in.next();
			String negx = "";
			String negy = "";
			if (x.charAt(0) == '-')
				negx = x.substring(1);
			else
				negx = '-' + x;
			if (y.charAt(0) == '-')
				negy = y.substring(1);
			else
				negy = '-' + y;
			// 1. From -x_i to y_i
			int v = st.get(negx);
			int w = st.get(y);
			G.addEdge(v, w);
			// 2. From -y_i to x_i
			v = st.get(negy);
			w = st.get(x);
			G.addEdge(v, w);
		}
		in.close();

		/**
		 * Build SCCs
		 */
		KosarajuSCC scc = new KosarajuSCC(G);

		/**
		 * f is not satisfiable if both -x_i and xi are in the same SCC
		 * same applies to y_i
		 */
		for (String x : literals) {
			String negx = '-' + x;
			int v = st.get(x);
			int w = st.get(negx);
			if (scc.stronglyConnected(v, w)) {
				System.out.println("not satisfiable for assignment");
				return;
			}
		}

		/**
		 * Build Kernel DAG, vertex is scc #
		 */
		Digraph KG = new Digraph(scc.count());
		for (int v = 0; v < G.V(); v++) {
			int vid = scc.id(v);		// scc #
			for (int w = 0; w < G.V(); w++) {
				if (v == w) continue;
				int wid = scc.id(w);		// scc #
				if (vid != wid) {
					if (G.hasEdge(v, w))
						KG.addEdge(vid, wid);
				}
			}
		}

		/**
		 * find assignment with a topological sort of vertices of the kernel graph. 
		 * If -x_i is after x_i in topological sort x_i should be FALSE. 
		 * It should be TRUE otherwise.
		 */
		Topological T = new Topological(KG);
		if (T.hasOrder()) {
			System.out.println("2-SAT assignment: ");
			for (String x : literals) {
				String negx = '-' + x;
				int v = st.get(x);
				int w = st.get(negx);
				if (T.rank(scc.id(v)) < T.rank(scc.id(w)))
					System.out.println(x + " - FALSE");
				else
					System.out.println(x + " - TRUE");
			}
		}
	}

	/**
	 * Ex 4.3.26 
	 * Critical edges. 
	 */
	public Iterable<Edge> findCriticalEdges(EdgeWeightedGraph G) {
		Edge[] edges = new Edge[G.E()];
		boolean[] critical = new boolean[G.E()];
		int i = 0;
		for (Edge e : G.edges())
			edges[i++] = e;
		Arrays.sort(edges);
		UF uf = new UF(G.V());
		for (i = 0; i < G.V(); i++) {
			Edge e = edges[i];
			int v = e.either();
			int w = e.other(v);
			if (!uf.connected(v, w)) {
				uf.union(v, w);
				critical[i] = true;
			} else {
				// search same weight in MST
				double weight = e.weight();
				for (int j = i - 1; j > 0 && edges[j].weight() == weight && critical[j]; j--)
					critical[j] = false;
			}
		}
		List<Edge> result = new LinkedList<>();
		for (i = 0; i < G.E(); i++) {
			if (critical[i])
				result.add(edges[i]);
		}
		return result;
	}

	public static void main(String[] args) throws FileNotFoundException {
		Graph_Creative gc = new Graph_Creative();
		Scanner in = new Scanner(new File(args[0]));
		Graph G = new Graph(in);
		System.out.println(gc.euler_cycles(G));
		in = new Scanner(new File(args[1]));
		G = new Graph(in);
		System.out.println(gc.count_parallel_edges(G));
		in = new Scanner(new File(args[2]));
		G = new Graph(in);
		System.out.println(gc.isTwoEdgeConnected(G));
		// test for 4.2.21
		Digraph DG = new Digraph(5);
		DG.addEdge(0, 3);
		DG.addEdge(3, 4);
		DG.addEdge(1, 0);
		DG.addEdge(0, 2);
		DG.addEdge(2, 4);
		System.out.println(DG);
		System.out.println("LCA: " + gc.DAG_LCA(DG, 2, 4));
		System.out.println("SAP: " + gc.SAP(DG, 2, 4));
		DG.addEdge(2, 0);
		DG.addEdge(3, 2);
		System.out.println("Strong Component: " + gc.strongComponent(DG, 3));
		gc.find2SAT(args[3]);
	}

}

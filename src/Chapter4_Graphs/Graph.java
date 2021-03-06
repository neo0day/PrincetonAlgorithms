package Chapter4_Graphs;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Graph {
	private static final String NEWLINE = System.getProperty("line.separator");

	private int V;
	private int E;
	private List<List<Integer>> adj;

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
	 * deep copy
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
	 * Add a method hasEdge()
	 */
	public boolean hasEdge(int v, int w) {
		for (int u : adj.get(v))
			if (u == w)
				return true;
		return false;
	}

	/**
	 * Ex 4.1.5
	 * disallow parallel edges and self-loops.
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
	 * Search API
	 */
	public class Search {
		private int s;
		private WeightedQuickUnionUF uf;

		/**
		 * find vertices connected to a source vertex s
		 * The constructor can build a UF object, do a union() operation
		 * for each of the graph’s edges.
		 */
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

		/**
		 * is v connected to s?
		 * Implement marked(v) by calling connected(s, v)
		 */
		boolean marked(int v) {
			return uf.connected(s, v);
		}

		/**
		 * how many vertices are connected to s?
		 * Implementing count() requires using a weighted UF implementation
		 * and extending its API to use a count() method that returns wt[find(v)]
		 */
		int count() {
			return uf.count(s);
		}

	}

	/**
	 * Ex 4.1.10
	 * finds non-articulation vertex. 
	 */
	public List<Integer> non_articulation_vertex() {
		vertexClassification();
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < V; i++) {
			if (!articulation[i])
				list.add(i);
			System.out.println(i + " " + disc[i] + "/" + low[i]);
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
	 * Ex 4.1.15
	 * Modify the input stream constructor for Graph to also allow adjacency lists
	 */
	public Graph(Scanner in, String sp) {
		this(in.nextInt());		// calls Graph(V)
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
		BFS bfs = new BFS(G, 0);
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

/******************************************************************************
 *  Copyright 2002-2015, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/

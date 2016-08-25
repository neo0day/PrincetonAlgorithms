package Chapter4_Graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Digraph {
	private static final String NEWLINE = System.getProperty("line.separator");

	private final int V;           		// number of vertices in this digraph
	private int E;                 		// number of edges in this digraph
	private List<List<Integer>> adj;    // adj[v] = adjacency list for vertex v
	private int[] indegree;        		// indegree[v] = indegree of vertex v

	public Digraph(int V) {
		if (V < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
		this.V = V;
		this.E = 0;
		indegree = new int[V];
		adj = new ArrayList<List<Integer>>(V);
		for (int v = 0; v < V; v++) {
			List<Integer> list = new LinkedList<Integer>();
			adj.add(list);
		}
	}

	public Digraph(Scanner in) {
		this(in.nextInt());
		int E = in.nextInt();
		if (E < 0) throw new IllegalArgumentException("Number of edges in a Digraph must be nonnegative");
		for (int i = 0; i < E; i++) {
			int v = in.nextInt();
			int w = in.nextInt();
			addEdge(v, w); 
		}
	}

	/**
	 * Ex 4.2.3
	 * copy constructor for Digraph
	 */
	public Digraph(Digraph G) {
		this(G.V());
		this.E = G.E();
		for (int v = 0; v < V; v++)
			this.indegree[v] = G.indegree(v);
		for (int v = 0; v < G.V(); v++) {
			// reverse so that adjacency list is in same order as original
			Stack<Integer> reverse = new Stack<Integer>();
			for (int w : G.adj(v)) {
				reverse.push(w);
			}
			for (int w : reverse) {
				adj.get(v).add(w);
			}
		}
	}

	/**
	 * Ex 4.2.4
	 * Add a method hasEdge() to Digraph
	 */
	public boolean hasEdge(int v, int w) {
		for (int u : adj.get(v))
			if (u == w)
				return true;
		return false;
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

	/**
	 * Ex 4.2.5
	 * disallow parallel edges and self-loops.
	 * @throws Exception 
	 */
	public void validateEdge(int v, int w) throws Exception {
		if (v == w)
			throw new Exception("vertex " + v + " has self-loop");
		for (int u : this.adj.get(v))
			if (u == w)
				throw new Exception("vertex " + v + " has parallel edge");
	}

	public void addEdge(int v, int w) {
		validateVertex(v);
		validateVertex(w);
		adj.get(v).add(w);
		indegree[w]++;
		E++;
	}

	public Iterable<Integer> adj(int v) {
		validateVertex(v);
		return adj.get(v);
	}

	public int outdegree(int v) {
		validateVertex(v);
		return adj.get(v).size();
	}

	public int indegree(int v) {
		validateVertex(v);
		return indegree[v];
	}

	public Digraph reverse() {
		Digraph R = new Digraph(V);
		for (int v = 0; v < V; v++) {
			for (int w : adj(v)) {
				R.addEdge(w, v);
			}
		}
		return R;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(V + " vertices, " + E + " edges " + NEWLINE);
		for (int v = 0; v < V; v++) {
			s.append(String.format("%d: ", v));
			for (int w : adj.get(v)) {
				s.append(String.format("%d ", w));
			}
			s.append(NEWLINE);
		}
		return s.toString();
	}

	/**
	 * Ex 4.2.9
	 * checks whether or not a given permutation of a DAG’s vertices 
	 * is a topological order of that DAG.
	 */
	public boolean isToposort(Digraph G, List<Integer> order) {
		boolean[] marked = new boolean[G.V()];
		for (int v : order) {
			marked[v] = true;
			for (int w : G.adj(v)) {
				if (marked[w])
					return false;
			}
		}
		return true;
	}


	/**
	 * Ex 4.2.6
	 * Develop a test client for Digraph
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File(args[0]));
		Digraph G = new Digraph(in);
		System.out.println(G);
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

package Chapter4_Graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class EdgeWeightedGraph {
	private static final String NEWLINE = System.getProperty("line.separator");

	private final int V;
	private int E;
	private List<List<Edge>> adj;
	private double[][] adj_matrix;

	public EdgeWeightedGraph(int V) {
		if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
		this.V = V;
		this.E = 0;
		adj = new ArrayList<List<Edge>>(V);
		for (int v = 0; v < V; v++) {
			List<Edge> list = new LinkedList<>();
			adj.add(list);
		}
	}

	public EdgeWeightedGraph(int V, int E) {
		this(V);
		if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
		Random random = new Random();
		for (int i = 0; i < E; i++) {
			int v = random.nextInt(V);
			int w = random.nextInt(V);
			double weight = Math.round(100 * random.nextDouble()) / 100.0;
			Edge e = new Edge(v, w, weight);
			addEdge(e);
		}
	}

	/**
	 * Ex 4.3.9
	 * Implement the constructor for EdgeWeightedGraph
	 */
	public EdgeWeightedGraph(Scanner in) {
		this(in.nextInt());
		int E = in.nextInt();
		if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
		for (int i = 0; i < E; i++) {
			int v = in.nextInt();
			int w = in.nextInt();
			double weight = in.nextDouble();
			Edge e = new Edge(v, w, weight);
			addEdge(e);
		}
	}

	/**
	 * Ex 4.3.10
	 * Develop an EdgeWeightedGraph implementation for dense graphs
	 */
	public EdgeWeightedGraph(Scanner in, boolean matrix) {
		this.V = in.nextInt();
		this.E = in.nextInt();
		adj_matrix = new double[V][V];
		Arrays.fill(adj_matrix, Double.NEGATIVE_INFINITY);
		if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
		for (int i = 0; i < E; i++) {
			int v = in.nextInt();
			int w = in.nextInt();
			double weight = in.nextDouble();
			if (adj_matrix[v][w] == Double.NEGATIVE_INFINITY) {
				adj_matrix[v][w] = weight;
				adj_matrix[w][v] = weight;
			} else {
				System.out.println("parallel edge!");
			}
		}
	}

	public EdgeWeightedGraph(EdgeWeightedGraph G) {
		this(G.V());
		this.E = G.E();
		for (int v = 0; v < G.V(); v++) {
			// reverse so that adjacency list is in same order as original
			Deque<Edge> reverse = new ArrayDeque<Edge>();
			for (Edge e : G.adj(v)) {
				reverse.push(e);
			}
			for (Edge e : reverse) {
				adj.get(v).add(e);
			}
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

	public void addEdge(Edge e) {
		int v = e.either();
		int w = e.other(v);
		validateVertex(v);
		validateVertex(w);
		adj.get(v).add(e);
		adj.get(w).add(e);
		E++;
	}
	
	public void removeEdge(Edge e) {
		int v = e.either();
		int w = e.other(v);
		validateVertex(v);
		validateVertex(w);
		adj.get(v).remove(e);
		adj.get(w).remove(e);
		E++;
	}

	public Iterable<Edge> adj(int v) {
		validateVertex(v);
		return adj.get(v);
	}
	
	public int degree(int v) {
		validateVertex(v);
		return adj.get(v).size();
	}

	public Iterable<Edge> edges() {
		List<Edge> list = new LinkedList<Edge>();
		for (int v = 0; v < V; v++) {
			int selfLoops = 0;
			for (Edge e : adj(v)) {
				if (e.other(v) > v) {
					list.add(e);
				}
				// only add one copy of each self loop (self loops will be consecutive)
				else if (e.other(v) == v) {
					if (selfLoops % 2 == 0) list.add(e);
					selfLoops++;
				}
			}
		}
		return list;
	}

	/**
	 * Ex 4.3.17 
	 * Implement toString() for EdgeWeightedGraph.
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(V + " " + E + NEWLINE);
		for (int v = 0; v < V; v++) {
			s.append(v + ": ");
			for (Edge e : adj.get(v)) {
				s.append(e + "  ");
			}
			s.append(NEWLINE);
		}
		return s.toString();
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File(args[0]));
		EdgeWeightedGraph G = new EdgeWeightedGraph(in);
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

package Chapter4_Graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Ex 4.3.29 
 * Dense graphs. 
 */
public class PrimDenseMST {

	private Edge[] edgeTo;        // edgeTo[v] = shortest edge from tree vertex to non-tree vertex
	private double[] distTo;      // distTo[v] = weight of shortest such edge
	private boolean[] marked;     // marked[v] = true if v on tree, false otherwise

	// no priority queue, depends on distTo[]
	public PrimDenseMST(EdgeWeightedGraph G) {
		edgeTo = new Edge[G.V()];
		distTo = new double[G.V()];
		marked = new boolean[G.V()];
		for (int v = 0; v < G.V(); v++)
			distTo[v] = Double.POSITIVE_INFINITY;

		for (int v = 0; v < G.V(); v++)      // run from each vertex to find
			if (!marked[v]) {
				prim(G, v);      // minimum spanning forest
			}

	}

	// run Prim's algorithm in graph G, starting from vertex s
	private void prim(EdgeWeightedGraph G, int s) {
		distTo[s] = 0.0;
		for (int i = 0; i < G.V(); i++) {
			int v = find_min();
			scan(G, v);
		}
	}

	// O(V)
	private int find_min() {
		double min = Double.POSITIVE_INFINITY;
		int min_idx = 0;
		for (int v = 0; v < distTo.length; v++) {
			if (!marked[v] && distTo[v] < min) {
				min = distTo[v];
				min_idx = v;
			}
		}
		return min_idx;
	}

	// scan vertex v
	private void scan(EdgeWeightedGraph G, int v) {
		marked[v] = true;
		for (Edge e : G.adj(v)) {
			int w = e.other(v);
			if (marked[w]) continue;         // v-w is obsolete edge
			if (e.weight() < distTo[w]) {
				distTo[w] = e.weight();		// similar to decreasekey()
				edgeTo[w] = e;
			}
		}
	}

	public Iterable<Edge> edges() {
		Queue<Edge> mst = new LinkedList<Edge>();
		for (int v = 0; v < edgeTo.length; v++) {
			Edge e = edgeTo[v];
			if (e != null) {
				mst.add(e);
			}
		}
		return mst;
	}

	public double weight() {
		double weight = 0.0;
		for (Edge e : edges())
			weight += e.weight();
		return weight;
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File(args[0]));
		EdgeWeightedGraph G = new EdgeWeightedGraph(in);
		PrimDenseMST mst = new PrimDenseMST(G);
		for (Edge e : mst.edges()) {
			System.out.println(e);
		}
		System.out.printf("%.5f\n", mst.weight());
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

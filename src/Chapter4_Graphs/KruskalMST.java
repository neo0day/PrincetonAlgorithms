package Chapter4_Graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class KruskalMST {
	private static final double FLOATING_POINT_EPSILON = 1E-12;

	private double weight;                        		// weight of MST
	private Queue<Edge> mst = new LinkedList<Edge>();  	// edges in MST

	public KruskalMST(EdgeWeightedGraph G) {
		// more efficient to build heap by passing array of edges
		PriorityQueue<Edge> pq = new PriorityQueue<Edge>();
		for (Edge e : G.edges()) {
			pq.add(e);
		}

		// run greedy algorithm
		UF uf = new UF(G.V());
		while (!pq.isEmpty() && mst.size() < G.V() - 1) {
			Edge e = pq.remove();
			int v = e.either();
			int w = e.other(v);
			if (!uf.connected(v, w)) { // v-w does not create a cycle
				uf.union(v, w);  // merge v and w components
				mst.add(e);  // add edge e to mst
				weight += e.weight();
			}
		}

		// check optimality conditions
		assert check(G);
	}

	public Iterable<Edge> edges() {
		return mst;
	}

	public double weight() {
		return weight;
	}

	// check optimality conditions (takes time proportional to E V lg* V)
	private boolean check(EdgeWeightedGraph G) {

		// check total weight
		double total = 0.0;
		for (Edge e : edges()) {
			total += e.weight();
		}
		if (Math.abs(total - weight()) > FLOATING_POINT_EPSILON) {
			System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", total, weight());
			return false;
		}

		// check that it is acyclic
		UF uf = new UF(G.V());
		for (Edge e : edges()) {
			int v = e.either(), w = e.other(v);
			if (uf.connected(v, w)) {
				System.err.println("Not a forest");
				return false;
			}
			uf.union(v, w);
		}

		// check that it is a spanning forest
		for (Edge e : G.edges()) {
			int v = e.either(), w = e.other(v);
			if (!uf.connected(v, w)) {
				System.err.println("Not a spanning forest");
				return false;
			}
		}

		// check that it is a minimal spanning forest (cut optimality conditions)
		for (Edge e : edges()) {

			// all edges in MST except e
			uf = new UF(G.V());
			for (Edge f : mst) {
				int x = f.either(), y = f.other(x);
				if (f != e) uf.union(x, y);
			}

			// check that e is min weight edge in crossing cut
			for (Edge f : G.edges()) {
				int x = f.either(), y = f.other(x);
				if (!uf.connected(x, y)) {
					if (f.weight() < e.weight()) {
						System.err.println("Edge " + f + " violates cut optimality conditions");
						return false;
					}
				}
			}

		}

		return true;
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File(args[0]));
		EdgeWeightedGraph G = new EdgeWeightedGraph(in);
		KruskalMST mst = new KruskalMST(G);
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

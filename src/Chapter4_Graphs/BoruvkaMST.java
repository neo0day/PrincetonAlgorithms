package Chapter4_Graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class BoruvkaMST {

	private Queue<Edge> mst = new LinkedList<Edge>();    // edges in MST
	private double weight;                      // weight of MST

	/**
	 * Ex 4.3.43 
	 * Boruvka’s algorithm. 
	 */
	public BoruvkaMST(EdgeWeightedGraph G) {
		UF uf = new UF(G.V());

		// repeat at most log V times or until we have V-1 edges
		for (int t = 1; t < G.V() && mst.size() < G.V() - 1; t = t + t) {

			// foreach tree in forest, find closest edge
			// if edge weights are equal, ties are broken in favor of first edge in G.edges()
			Edge[] closest = new Edge[G.V()];
			for (Edge e : G.edges()) {
				int v = e.either(), w = e.other(v);
				int i = uf.find(v), j = uf.find(w);
				if (i == j) continue;   // same tree
				if (closest[i] == null || less(e, closest[i])) closest[i] = e;
				if (closest[j] == null || less(e, closest[j])) closest[j] = e;
			}

			// add newly discovered edges to MST
			for (int i = 0; i < G.V(); i++) {
				Edge e = closest[i];
				if (e != null) {
					int v = e.either(), w = e.other(v);
					// don't add the same edge twice
					if (!uf.connected(v, w)) {
						mst.add(e);
						weight += e.weight();
						uf.union(v, w);
					}
				}
			}
		}
	}

	// is the weight of edge e strictly less than that of edge f?
	private static boolean less(Edge e, Edge f) {
		return e.weight() < f.weight();
	}
	
	public Iterable<Edge> edges() {
        return mst;
    }
	
	public double weight() {
        return weight;
    }
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File(args[0]));
		EdgeWeightedGraph G = new EdgeWeightedGraph(in);
		BoruvkaMST mst = new BoruvkaMST(G);
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

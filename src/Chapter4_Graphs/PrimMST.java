package Chapter4_Graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class PrimMST {
	private static final double FLOATING_POINT_EPSILON = 1E-12;

	private Edge[] edgeTo;        // edgeTo[v] = shortest edge from tree vertex to non-tree vertex
	private double[] distTo;      // distTo[v] = weight of shortest such edge
	private boolean[] marked;     // marked[v] = true if v on tree, false otherwise
	private IndexMinPQ<Double> pq;

	/**
	 * Ex 4.3.22 
	 * Minimum spanning forest. 
	 */
	private int[] id;			 // id[v] = id of MST containing v
	private int count;            // number of MST
	private double v_weight;

	public PrimMST(EdgeWeightedGraph G) {
		edgeTo = new Edge[G.V()];
		distTo = new double[G.V()];
		marked = new boolean[G.V()];
		id = new int[G.V()];
		pq = new IndexMinPQ<Double>(G.V());
		count = 0;
		v_weight = 0.0;
		for (int v = 0; v < G.V(); v++)
			distTo[v] = Double.POSITIVE_INFINITY;

		for (int v = 0; v < G.V(); v++)      // run from each vertex to find
			if (!marked[v]) {
				prim(G, v);      // minimum spanning forest
				count++;
			}

		// check optimality conditions
		assert check(G);
	}

	// run Prim's algorithm in graph G, starting from vertex s
	private void prim(EdgeWeightedGraph G, int s) {
		distTo[s] = 0.0;
		pq.insert(s, distTo[s]);
		while (!pq.isEmpty()) {
			int v = pq.delMin();
			id[v] = count;
			scan(G, v);
		}
	}

	// scan vertex v
	private void scan(EdgeWeightedGraph G, int v) {
		marked[v] = true;
		for (Edge e : G.adj(v)) {
			int w = e.other(v);
			if (marked[w]) continue;         // v-w is obsolete edge
			if (e.weight() < distTo[w]) {
				double old_weight = distTo[w];
				distTo[w] = e.weight();
				edgeTo[w] = e;
				if (pq.contains(w)) {
					pq.decreaseKey(w, distTo[w]);
					v_weight -= old_weight;
					v_weight += distTo[w];
				}
				else {
					pq.insert(w, distTo[w]);
					v_weight += distTo[w];		// eager strategy for weight()
				}
			}
		}
	}

	/**
	 * Ex 4.3.32 
	 * Specified set. 
	 */
	private Queue<Edge> mst;      		// MST edges
	private PriorityQueue<Edge> minPQ;	// crossing edges
	public PrimMST(EdgeWeightedGraph G, List<Edge> S) {
		// pre-process S
		mst = new LinkedList<>();
		minPQ = new PriorityQueue<>();
		for (Edge e : S) {
			mst.add(e);
			int v = e.either();
			int w = e.other(v);
			visit(G, v);
			visit(G, w);
		}
		// prim-lazy
		while (!minPQ.isEmpty()) {
			Edge e = minPQ.remove();
			int v = e.either();
			int w = e.other(v);
			if (marked[v] && marked[w]) continue;
			mst.add(e);
			if (!marked[v])
				visit(G, v);
			if (!marked[w])
				visit(G, w);
		}
	}

	private void visit(EdgeWeightedGraph G, int v) {
		marked[v] = true;
		for (Edge e: G.adj(v))
			if (!marked[e.other(v)])
				minPQ.add(e);
	}

	/**
	 * Ex 4.3.21
	 * edges() for PrimMST
	 */
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

	/**
	 * 4.3.31 
	 * MST weights. 
	 */
	public double weight() {
		double weight = 0.0;
		for (Edge e : edges())		// lazy strategy
			weight += e.weight();
		return weight;
	}
	
	public double weight2() {
		return v_weight;
	}

	/**
	 * Ex 4.3.33 
	 * Certification. 
	 */
	// check optimality conditions (takes time proportional to E V lg* V)
	private boolean check(EdgeWeightedGraph G) {

		// check weight
		double totalWeight = 0.0;
		for (Edge e : edges()) {
			totalWeight += e.weight();
		}
		if (Math.abs(totalWeight - weight()) > FLOATING_POINT_EPSILON) {
			System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", totalWeight, weight());
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
			for (Edge f : edges()) {
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
		PrimMST mst = new PrimMST(G);
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

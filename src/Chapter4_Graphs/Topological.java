package Chapter4_Graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Topological {
	private Iterable<Integer> order;  // topological order
	private int[] rank;               // rank[v] = position of vertex v in topological order

	public Topological(Digraph G) {
		DirectedCycle finder = new DirectedCycle(G);
		if (!finder.hasCycle()) {
			DepthFirstOrder dfs = new DepthFirstOrder(G);
			order = dfs.reversePost();
			rank = new int[G.V()];
			int i = 0;
			for (int v : order)
				rank[v] = i++;
		}
	}

	public Iterable<Integer> order() {
		return order;
	}

	public boolean hasOrder() {
		return order != null;
	}

	public int rank(int v) {
		validateVertex(v);
		if (hasOrder()) return rank[v];
		else            return -1;
	}

	// throw an IndexOutOfBoundsException unless 0 <= v < V
	private void validateVertex(int v) {
		int V = rank.length;
		if (v < 0 || v >= V)
			throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V-1));
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File(args[0]));
		Digraph dg = new Digraph(in);
		Topological topological = new Topological(dg);
		for (int v : topological.order()) {
			System.out.println(v);
		}
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

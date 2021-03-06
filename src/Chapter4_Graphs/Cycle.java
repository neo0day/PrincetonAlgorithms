package Chapter4_Graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

/**
 * Ex 4.1.29
 * Modify Cycle
 */
public class Cycle {
	private boolean[] marked;
	private int[] edgeTo;
	private Stack<Integer> cycle;

	public Cycle(Graph G) {
		marked = new boolean[G.V()];
		edgeTo = new int[G.V()];
		for (int v = 0; v < G.V(); v++)
			if (!marked[v])
				dfs(G, -1, v);
	}

	public boolean hasCycle() {
		return cycle != null;
	}

	public Iterable<Integer> cycle() {
		return cycle;
	}

	private void dfs(Graph G, int u, int v) {
		marked[v] = true;
		for (int w : G.adj(v)) {

			// short circuit if cycle already found
			if (cycle != null) return;

			if (w == v || marked[w])			// self-loop || parallel edges
				continue;

			if (!marked[w]) {
				edgeTo[w] = v;
				dfs(G, v, w);
			}

			// check for cycle (but disregard reverse of edge leading to v)
			else if (w != u) {
				cycle = new Stack<Integer>();
				for (int x = v; x != w; x = edgeTo[x]) {
					cycle.push(x);
				}
				cycle.push(w);
				cycle.push(v);
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File(args[0]));
		Graph G = new Graph(in);
		Cycle finder = new Cycle(G);
		if (finder.hasCycle()) {
			for (int v : finder.cycle()) {
				System.out.print(v + " ");
			}
			System.out.println();
		}
		else {
			System.out.println("Graph is acyclic");
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

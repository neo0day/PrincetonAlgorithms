package Chapter4_Graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class BreadthFirstDirectedPaths {
	private static final int INFINITY = Integer.MAX_VALUE;
	private boolean[] marked;  // marked[v] = is there an s->v path?
	private int[] edgeTo;      // edgeTo[v] = last edge on shortest s->v path
	private int[] distTo;      // distTo[v] = length of shortest s->v path

	public BreadthFirstDirectedPaths(Digraph G, int s) {
		marked = new boolean[G.V()];
		distTo = new int[G.V()];
		edgeTo = new int[G.V()];
		for (int v = 0; v < G.V(); v++)
			distTo[v] = INFINITY;
		bfs(G, s);
	}

	// BFS from single source
	private void bfs(Digraph G, int s) {
		Queue<Integer> q = new LinkedList<Integer>();
		marked[s] = true;
		distTo[s] = 0;
		q.add(s);
		while (!q.isEmpty()) {
			int v = q.poll();
			for (int w : G.adj(v)) {
				if (!marked[w]) {
					edgeTo[w] = v;
					distTo[w] = distTo[v] + 1;
					marked[w] = true;
					q.add(w);
				}
			}
		}
	}

	public boolean hasPathTo(int v) {
		return marked[v];
	}

	public int distTo(int v) {
		return distTo[v];
	}

	public Iterable<Integer> pathTo(int v) {
		if (!hasPathTo(v)) return null;
		Deque<Integer> path = new ArrayDeque<Integer>();
		int x;
		for (x = v; distTo[x] != 0; x = edgeTo[x])
			path.push(x);
		path.push(x);
		return path;
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File(args[0]));
		Digraph G = new Digraph(in);
		// StdOut.println(G);

		int s = Integer.parseInt(args[1]);
		BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(G, s);

		for (int v = 0; v < G.V(); v++) {
			if (bfs.hasPathTo(v)) {
				System.out.printf("%d to %d (%d):  ", s, v, bfs.distTo(v));
				for (int x : bfs.pathTo(v)) {
					if (x == s) System.out.print(x);
					else        System.out.print("->" + x);
				}
				System.out.println();
			}

			else {
				System.out.printf("%d to %d (-):  not connected\n", s, v);
			}

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

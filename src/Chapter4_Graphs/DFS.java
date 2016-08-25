package Chapter4_Graphs;

import java.util.ArrayDeque;
import java.util.Deque;

public class DFS {
	private int[] distTo;       // distTo[v] = number of edges shortest s-v path
	private int[] edgeTo;       // edgeTo[v] = previous edge on shortest s-v path
	private boolean[] marked;
	public DFS(Graph G, int s) {
		marked = new boolean[G.V()];
		distTo = new int[G.V()];
		edgeTo = new int[G.V()];
		dfs(G, s);
	}

	private void dfs(Graph G, int v) {
		marked[v] = true;
		for (int w : G.adj(v)) {
			if (!marked[w]) {
				distTo[w] = distTo[v] + 1;
				edgeTo[w] = v;
				dfs(G, w);
			}
		}
	}

	public int distTo(int v) {
		return distTo[v];
	}

	public boolean hasPathTo(int v) {
		return marked[v];
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

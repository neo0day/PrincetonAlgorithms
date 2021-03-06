package Chapter4_Graphs;

import java.util.LinkedList;
import java.util.Queue;

public class CC {
	private boolean[] marked;   // marked[v] = has vertex v been marked?
	private int[] id;           // id[v] = id of connected component containing v
	private int[] size;         // size[id] = number of vertices in given component
	private int count;          // number of connected components

	public CC(Graph G) {
		marked = new boolean[G.V()];
		id = new int[G.V()];
		size = new int[G.V()];
		for (int v = 0; v < G.V(); v++) {
			if (!marked[v]) {
				bfs(G, v);
				count++;
			}
		}
	}

	private void bfs(Graph G, int s) {
		Queue<Integer> q = new LinkedList<>();
		q.add(s);
		marked[s] = true;
		while (!q.isEmpty()) {
			int v = q.poll();
			id[v] = count;
			size[count]++;
			for (int w : G.adj(v)) {
				if (!marked[w]) {
					q.add(w);
					marked[w] = true;
				}
			}
		}

	}

	public int id(int v) {
		return id[v];
	}

	public int size(int v) {
		return size[id[v]];
	}

	public int count() {
		return count;
	}

	public boolean connected(int v, int w) {
		return id(v) == id(w);
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

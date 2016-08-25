package Chapter4_Graphs;

public class KosarajuSCC {
	private boolean[] marked;          // reached vertices
	private int[] id;                  // component identifiers
	private int count;                 // number of strong components
	public KosarajuSCC(Digraph G) {
		marked = new boolean[G.V()];
		id = new int[G.V()];
		DepthFirstOrder order = new DepthFirstOrder(G.reverse());
		for (int s : order.reversePost())     // discover the SCCs one by one
			if (!marked[s]) {
				dfs(G, s);
				count++;
			}
	}
	private void dfs(Digraph G, int v) {
		marked[v] = true;
		id[v] = count;
		for (int w : G.adj(v))
			if (!marked[w])
				dfs(G, w);
	}
	public boolean stronglyConnected(int v, int w) {
		return id[v] == id[w];
	}
	public int id(int v) {
		return id[v];
	}
	public int count() {
		return count;
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

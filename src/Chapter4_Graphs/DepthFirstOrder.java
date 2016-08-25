package Chapter4_Graphs;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class DepthFirstOrder {
	private boolean[] marked;
	private Queue<Integer> pre;            // vertices in preorder
	private Queue<Integer> post;           // vertices in postorder
	private Deque<Integer> reversePost;    // vertices in reverse postorder
	public DepthFirstOrder(Digraph G) {
		pre = new LinkedList<Integer>();
		post = new LinkedList<Integer>();
		reversePost = new ArrayDeque<Integer>();
		marked = new boolean[G.V()];
		for (int v = 0; v < G.V(); v++)
			if (!marked[v])
				dfs(G, v);
	}
	private void dfs(Digraph G, int v) {
		pre.add(v);
		marked[v] = true;
		for (int w : G.adj(v))
			if (!marked[w])
				dfs(G, w);
		post.add(v);
		reversePost.push(v);
	}
	public Iterable<Integer> pre() {
		return pre;
	}
	public Iterable<Integer> post() {
		return post;
	}
	public Iterable<Integer> reversePost() {
		return reversePost;
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

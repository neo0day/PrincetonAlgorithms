package Chapter4_Graphs;

import java.util.LinkedList;
import java.util.Queue;

import edu.princeton.cs.algs4.Stack;

/**
 * Ex 4.1.13
 * Add a distTo() method
 */
public class BFS {
	private int[] distTo;       // distTo[v] = number of edges shortest s-v path
	private int[] edgeTo;       // edgeTo[v] = previous edge on shortest s-v path
	private boolean[] marked;
	public BFS(Graph G, int s) {
		marked = new boolean[G.V()];
		distTo = new int[G.V()];
		edgeTo = new int[G.V()];
		bfs(G, s);
	}

	private void bfs(Graph G, int s) {
		Queue<Integer> q = new LinkedList<Integer>();
		for (int v = 0; v < G.V(); v++)
			distTo[v] = Integer.MAX_VALUE;
		distTo[s] = 0;
		marked[s] = true;
		q.add(s);
		while(!q.isEmpty()) {
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
	
	public int distTo(int v) {
		return distTo[v];
	}

	public boolean hasPathTo(int v) {
        return marked[v];
    }

	public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<Integer>();
        int x;
        for (x = v; distTo[x] != 0; x = edgeTo[x])
            path.push(x);
        path.push(x);
        return path;
    }
}

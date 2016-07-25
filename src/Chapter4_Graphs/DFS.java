package Chapter4_Graphs;

import edu.princeton.cs.algs4.Stack;

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
        Stack<Integer> path = new Stack<Integer>();
        int x;
        for (x = v; distTo[x] != 0; x = edgeTo[x])
            path.push(x);
        path.push(x);
        return path;
    }
}

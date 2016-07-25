package Chapter4_Graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Graph_Creative {

	private int[] disc;			// the discover time
	private int[] low;			// the earliest discover time
	private int time;
	private int[] parent;
	private int[] degree;
	private int odd;
	private int count;
	private boolean[][] bridge;
	private boolean[][] edge_visited;
	private int[] path;
	private int pos;

	/**
	 * Ex 4.1.30
	 * Eulerian and Hamiltonian cycles.
	 */
	public Iterable<Integer> euler_cycles(Graph G) {
		disc = new int[G.V()];
		low = new int[G.V()];
		time = 0;
		Arrays.fill(disc, -1);
		Arrays.fill(low, -1);
		parent = new int[G.V()];
		degree = new int[G.V()];
		odd = 0;
		count = 0;
		bridge = new boolean[G.V()][G.V()];
		edge_visited = new boolean[G.V()][G.V()];
		path = new int[G.E() + 1];
		pos = 0;
		List<Integer> cycle = new LinkedList<>();
		if (has_euler_cycles(G)) {
			System.out.print("Euler cycle is: ");
			path[pos++] = 0;
			find_euler_cycle(G, 0);
			for (int i = 0; i < path.length; i++)
				cycle.add(path[i]);
		}
		return cycle;
	}

	// Therom: A connected graph with even degree at each vertex has an Eulerian circuit.
	private boolean has_euler_cycles(Graph G) {
		dfs(G, 0);
		return odd == 0 && count == G.V();
	}

	// this also finds the bridge
	private void dfs(Graph G, int v) {
		disc[v] = time++;
		low[v] = disc[v];
		count++;
		for (int w : G.adj(v)) {
			degree[v]++;
			parent[w] = v;
			if (disc[w] == -1) {
				dfs(G, w);
				// check if the subtree rooted with w has a connection to one of the ancestors of v
				low[v] = Math.min(low[v], low[w]);
				// if the lowest vertex reachable from subtree under w is below v in DFS tree,
				// then u-v is a bridge
				if (low[w] > disc[v]) {
					System.out.println(v + " " + w);
					bridge[v][w] = true;
					bridge[w][v] = true;
				}
			}
			// Update low value of v for parent function calls.
			else if (w != parent[v])
				low[v]  = Math.min(low[v], disc[w]);
		}
		if (degree[v] % 2 != 0)
			odd++;
	}

	/**
	 *  Don't burn your bridges
	 * 	1) avoid the bridge if there are more edges unvisited
	 *  2) use the bridge if this is the only edge available
	 */
	public void find_euler_cycle(Graph G, int v) {
		for (int w : G.adj(v)) {
			if (isValidEdge(v, w)) {
				// mark edge as visited to indicate the edge get removed 
				// and update degree to reduce the available adjacent vertices
				edge_visited[v][w] = true;
				edge_visited[w][v] = true;
				degree[v]--;
				degree[w]--;
				find_euler_cycle(G, w);
			}
		}
		path[pos++] = v;
	}

	private boolean isValidEdge(int v, int w) {
		if (degree[v] == 0 || edge_visited[v][w]) return false;
		if (degree[v] == 1) return true;
		return !bridge[v][w];
	}

	/**
	 * Ex 4.1.32
	 * Parallel edge detection. 
	 */
	private boolean[] marked;
	public int count_parallel_edges(Graph G) {
		marked = new boolean[G.V()];
		count = 0;
		for (int v = 0; v < G.V(); v++) {
			if (!marked[v])
				dfs_cpe(G, v);
		}
		return count / 2;		// we counted twice
	}

	private void dfs_cpe(Graph G, int v) {
		int[] edge_count = new int[G.V()];
		marked[v] = true;
		for (int w : G.adj(v)) {
			if (edge_count[w] == 1)
				count++;
			edge_count[w]++;
			if (edge_count[w] > 1)
				count++;
			if (!marked[w])
				dfs_cpe(G, w);
		}
	}

	/**
	 * Ex 4.1.36
	 * Two-edge connectivity.
	 */
	public boolean isTwoEdgeConnected(Graph G) {
		disc = new int[G.V()];
		low = new int[G.V()];
		Arrays.fill(disc, -1);
		Arrays.fill(low, -1);
		time = 0;
		for (int v = 0; v < G.V(); v++) {
			if (disc[v] == -1)
				if (dfs_has_bridge(G, v, v))
					return false;
		}
		return true;
	}

	private boolean dfs_has_bridge(Graph G, int parent, int cur_v) {
		disc[cur_v] = time++;
		low[cur_v] = disc[cur_v];
		for (int neib : G.adj(cur_v)) {
			if (parent == neib)
				continue;
			if (disc[neib] == -1) {
				if (dfs_has_bridge(G, cur_v, neib))
					return true;
				if (low[neib] > disc[cur_v])
					return true;				// has bridge
				// update low number
				low[cur_v] = Math.min(low[cur_v], low[neib]);
			}
			else {
				// update low number
				low[cur_v] = Math.min(low[cur_v], disc[neib]);
			}
		}
		return false;
	}
	
	private int[] color;
	/**
	 * Ex 4.1.38
	 * Image processing.
	 */
	public void floor_fill(Graph G, int s, int new_color) {
		marked = new boolean[G.V()];
		Queue<Integer> Q = new LinkedList<>();
		int old_color = color[s];
		marked[s] = true;
		Q.add(s);
		while (!Q.isEmpty()) {
			int v = Q.poll();
			color[v] = new_color;
			for (int w : G.adj(v)) {
				if (!marked[w]) {
					marked[w] = true;
					if (color[w] == old_color)
						Q.add(w);
				}
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		Graph_Creative gc = new Graph_Creative();
		Scanner in = new Scanner(new File(args[0]));
		Graph G = new Graph(in);
		System.out.println(gc.euler_cycles(G));
		in = new Scanner(new File(args[1]));
		G = new Graph(in);
		System.out.println(gc.count_parallel_edges(G));
		in = new Scanner(new File(args[2]));
		G = new Graph(in);
		System.out.println(gc.isTwoEdgeConnected(G));
	}

}

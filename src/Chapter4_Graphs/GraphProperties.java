package Chapter4_Graphs;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GraphProperties {
	private Graph G;
	private int[] e;
	private int diam;
	private int rad;
	private boolean[] marked;
	private int[] dist;
	private int[] parent;

	/**
	 * Ex 4.1.16
	 * The eccentricity of a vertex
	 */
	GraphProperties(Graph G) {
		this.G = G;
		e = new int[G.V()];
		diam = Integer.MIN_VALUE;
		rad = Integer.MAX_VALUE;
		marked = new boolean[G.V()];
		for (int v = 0; v < G.V(); v++) {
			Arrays.fill(marked, false);
			e[v] = BFS(G, v);
			diam = Math.max(diam, e[v]);
			rad = Math.min(rad, e[v]);
			if (v % 10000 == 0)
				System.out.print("\nprocessing vertex " + v);
			else if (v % 100 == 0)
				System.out.print(".");
				
		}
	}

	private int BFS(Graph G, int s) {
		int max = 0;
		int[] dist = new int[G.V()];
		marked[s] = true;
		Queue<Integer> q = new LinkedList<>();
		q.add(s);
		while (!q.isEmpty()) {
			int v = q.poll();
			for (int w : G.adj(v)) {
				if (!marked[w]) {
					dist[w] = dist[v] + 1;
					q.add(w);
					marked[w] = true;
				}
			}
		}
		for (int i = 0; i < dist.length; i++)
			max = Math.max(max, dist[i]);
		return max;
	}

	public int diameter() {
		return diam;
	}

	public int radius() {
		return rad;
	}

	public List<Integer> center() {
		List<Integer> list = new LinkedList<>();
		for (int v = 0; v < G.V(); v++)
			if (e[v] == radius())
				list.add(v);
		return list;
	}

	/**
	 * Ex 4.1.17
	 * The Wiener index of a graph
	 */
	public int wiener() {
		int sum = 0;
		for (int s = 0; s < G.V(); s++) {		// BFS from each vertex
			marked = new boolean[G.V()];
			dist = new int[G.V()];
			Queue<Integer> q = new LinkedList<>();
			q.add(s);
			dist[s] = 0;
			marked[s] = true;
			while (!q.isEmpty()) {
				int v = q.poll();
				for (int w : G.adj(v)) {
					if (!marked[w]) {
						dist[w] = dist[v] + 1;
						q.add(w);
						marked[w] = true;
						sum += dist[w];
					}
				}
			}
		}
		return sum / 2;		// we sum the distance twice
	}

	/**
	 * 4.1.18
	 * The girth of a graph
	 */
	public int girth() {
		int girth = Integer.MAX_VALUE;
		for (int s = 0; s < G.V(); s++) {		// BFS from each vertex
			int circle = -1;
			marked = new boolean[G.V()];
			dist = new int[G.V()];
			parent = new int[G.V()];
			Queue<Integer> q = new LinkedList<>();
			q.add(s);
			dist[s] = 0;
			parent[s] = s;
			marked[s] = true;
			while (!q.isEmpty()) {
				int v = q.poll();
				for (int w : G.adj(v)) {
					if (!marked[w]) {
						dist[w] = dist[v] + 1;
						parent[w] = v;
						q.add(w);
						marked[w] = true;
					} else if (w != parent[v]) {
						circle = dist[v] + dist[w] + 1;
						break;
					}
				}
				if (circle > 0) break;		// found a circle
			}
			girth = Math.min(girth, circle);
		}
		return girth;
	}
}
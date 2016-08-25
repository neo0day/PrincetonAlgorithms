package Chapter4_Graphs;

import java.util.LinkedList;
import java.util.List;

/**
 * Ex 4.2.20
 * Directed Eulerian cycle.
 */
public class Euler {
	boolean[][] edge_visited;
	int[] path;
	int pos;
	List<Integer> cycle;

	public Euler (Digraph G) {
		Degrees D = new Degrees(G);
		edge_visited = new boolean[G.V()][G.V()];
		path = new int[G.E() + 1];
		pos = 0;
		if (has_euler_cycles(G, D)) {
			cycle = new LinkedList<>();
			find_euler_cycle(G, 0);
			for (int i = 0; i < path.length; i++)
				cycle.add(path[i]);
		}
	}

	public Iterable<Integer> cycle() {
		return cycle;
	}

	public void print_euler_cycle() {
		if (cycle == null)
			System.out.println("No euler tour exists.");
		else {
			System.out.println("The euler cycle is: ");
			for (int v : cycle)
				System.out.print(v + " ");
			System.out.println();
		}
	}

	private static boolean has_euler_cycles(Digraph G, Degrees D) {
		// Digraph is connected ==> only 1 SCC
		KosarajuSCC scc = new KosarajuSCC(G);
		if (scc.count() != 1)
			return false;
		// each vertex has indegree == outdegree
		for (int v = 0; v < G.V(); v++) {
			if (D.indegree(v) != D.outdegree(v))
				return false;
		}
		return true;
	}

	// no duplicated edges, DFS-style on edges
	public void find_euler_cycle(Digraph G, int v) {
		for (int w : G.adj(v)) {
			if (!edge_visited[v][w]) {
				edge_visited[v][w] = true;
				find_euler_cycle(G, w);
			}
		}
		path[pos++] = v;
	}

	public static void main(String[] args) {
		Digraph G = new Digraph(5);
		G.addEdge(1, 0);
		G.addEdge(0, 2);
		G.addEdge(2, 1);
		G.addEdge(0, 3);
		G.addEdge(3, 4);
		G.addEdge(4, 0);
		System.out.println(G);
		Euler e = new Euler(G);
		e.print_euler_cycle();
	}

}

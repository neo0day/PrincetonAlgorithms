package Chapter4_Graphs;

import java.util.Random;

/**
 * Ex 4.1.39
 * Random graphs. Write a program ErdosRenyiGraph that takes integer values V and E 
 * from the command line and builds a graph by generating E random pairs of integers
 * between 0 and V - 1. Note: This generator produces self-loops and parallel edges.
 */
public class ErdosRenyiGraph {
	private Graph G;
	
	public ErdosRenyiGraph(int V, int E) {
		G = new Graph(V);
		Random random = new Random();
		for (int i = 0; i < E; i++) {
			int v = random.nextInt(V);
			int w = random.nextInt(V);
			G.addEdge(v, w);
		}
	}
	
	public String toString() {
		return G.toString();
	}

	public static void main(String[] args) {
		int V = Integer.valueOf(args[0]);
		int E = Integer.valueOf(args[1]);
		ErdosRenyiGraph G = new ErdosRenyiGraph(V, E);
		System.out.println(G);
	}
}

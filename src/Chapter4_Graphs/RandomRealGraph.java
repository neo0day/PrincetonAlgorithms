package Chapter4_Graphs;

import java.util.HashSet;
import java.util.Random;

/**
 * Ex 4.1.44
 * Real-world graphs. Find a large weighted graph on the web—perhaps a map with distances, 
 * telephone connections with costs, or an airline rate schedule. Write a program RandomRealGraph 
 * that builds a graph by choosing V vertices at random and E edges at random from the subgraph 
 * induced by those vertices.
 */
public class RandomRealGraph {

	private Graph G;
	private static final class Edge {
		private int v;
		private int w;

		private Edge(int v, int w) {
			if (v < w) {
				this.v = v;
				this.w = w;
			}
			else {
				this.v = w;
				this.w = v;
			}
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Edge))
				return false;
			if (obj == this)
				return true;
			Edge that = (Edge)obj;
			return (this.v == that.v && this.w == that.w);
		}

		@Override
		public int hashCode() {
			return v * w;
		}
	}

	public RandomRealGraph() {
		Random random = new Random();
		HashSet<Edge> set = new HashSet<Edge>();
		int V = random.nextInt(1000);
		G = new Graph(V);
		for (int v = 0; v < V; v++) {
			int subE = random.nextInt(1000);
			int cnt = 0;
			while (cnt < subE) {
				int w = random.nextInt(V);
				Edge e = new Edge(v, w);
				if (v != w && !set.contains(e)) {
					set.add(e);
					G.addEdge(v, w);
					cnt++;
				}
			}
		}
	}

	public String toString() {
		return G.toString();
	}

	public static void main(String[] args) {
		RandomRealGraph G = new RandomRealGraph();
		System.out.println(G);
	}
}

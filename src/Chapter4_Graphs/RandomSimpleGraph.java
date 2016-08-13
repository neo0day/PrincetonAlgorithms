package Chapter4_Graphs;

import java.util.HashSet;
import java.util.Random;

/**
 * Ex 4.1.40
 * Random simple graphs. Write a program RandomSimpleGraph that takes integer 
 * values V and E from the command line and produces, with equal likelihood, 
 * each of the possible simple graphs with V vertices and E edges.
 */
public class RandomSimpleGraph {
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

	public RandomSimpleGraph(int V, int E) {
		if (E > (long) V*(V-1)/2) throw new IllegalArgumentException("Too many edges");
		if (E < 0)                throw new IllegalArgumentException("Too few edges");
		G = new Graph(V);
		HashSet<Edge> set = new HashSet<Edge>();
		Random random = new Random();
		while (G.E() < E) {
			int v = random.nextInt(V);
			int w = random.nextInt(V);
			Edge e = new Edge(v, w);
			if ((v != w) && !set.contains(e)) {		// equal likelihood
				set.add(e);
				G.addEdge(v, w);
			}
		}
	}

	public String toString() {
		return G.toString();
	}

	public static void main(String[] args) {
		int V = Integer.valueOf(args[0]);
		int E = Integer.valueOf(args[1]);
		RandomSimpleGraph G = new RandomSimpleGraph(V, E);
		System.out.println(G);
	}
}

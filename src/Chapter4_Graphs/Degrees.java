package Chapter4_Graphs;

import java.util.LinkedList;
import java.util.List;

/**
 * Ex 4.2.7
 * public class Degrees
 */
public class Degrees {
	private int[] indegree;
	private int[] outdegree;
	private List<Integer> sources;
	private List<Integer> sinks;

	public Degrees(Digraph G) {				// constructor
		indegree = new int[G.V()];
		outdegree = new int[G.V()];
		sources = new LinkedList<>();
		sinks = new LinkedList<>();
		for (int v = 0; v < G.V(); v++) {
			for (int w : G.adj(v)) {
				indegree[w]++;
				outdegree[v]++;
			}
		}
		for (int v = 0; v < G.V(); v++) {
			if (indegree[v] == 0)
				sources.add(v);
			if (outdegree[v] == 0)
				sinks.add(v);
		}
	}

	public int indegree(int v) {			// indegree of v
		return indegree[v];
	}

	public int outdegree(int v) {			// outdegree of v
		return outdegree[v];
	}

	public Iterable<Integer> sources() {	// sources
		return sources;
	}

	public Iterable<Integer> sinks() {		// sinks
		return sinks;
	}

	public boolean isMap() {				// is G a map?
		for (int v = 0; v < outdegree.length; v++)
			if (outdegree[v] != 1)
				return false;
		return true;
	}
}

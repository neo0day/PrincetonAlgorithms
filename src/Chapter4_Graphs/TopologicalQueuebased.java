package Chapter4_Graphs;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Ex 4.2.30
 * Queue-based topological sort.
 */
public class TopologicalQueuebased {

	private List<Integer> order;		// topological order
	private int[] indegree;
	private Queue<Integer> Q;		// source queue, indegree = 0

	// O(V+E)
	public TopologicalQueuebased(Digraph G) {
		indegree = new int[G.V()];
		Q = new LinkedList<>();
		order = new LinkedList<>();
		for (int v = 0; v < G.V(); v++) {
			for (int w : G.adj(v)) {
				indegree[w]++;
			}
		}

		for (int v = 0; v < G.V(); v++)
			if (indegree[v] == 0)
				Q.add(v);

		while (!Q.isEmpty()) {
			int v = Q.poll();
			order.add(v);
			for (int w : G.adj(v)) {
				indegree[w]--;
				if (indegree[w] == 0)
					Q.add(w);
			}
		}
	}

	public Iterable<Integer> order() {
		return order;
	}

	public static void main(String[] args) {
		Digraph G = new Digraph(6);
		G.addEdge(0, 1);
		G.addEdge(0, 3);
		G.addEdge(1, 2);
		G.addEdge(2, 3);
		G.addEdge(2, 4);
		G.addEdge(3, 4);
		TopologicalQueuebased TQ = new TopologicalQueuebased(G);
		System.out.println(TQ.order());
	}
}

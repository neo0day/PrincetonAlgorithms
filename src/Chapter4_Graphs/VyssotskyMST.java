package Chapter4_Graphs;

import java.util.Collections;
import java.util.PriorityQueue;

/**
 * Ex 4.3.23 
 * Vyssotsky’s algorithm. 
 */
public class VyssotskyMST {
	
	private PriorityQueue<Edge> maxPQ;
	
	public VyssotskyMST(EdgeWeightedGraph G) {
		maxPQ = new PriorityQueue<Edge>(Collections.reverseOrder());
		UF uf = new UF(G.V());
		for (Edge e : G.edges()) {
			int v = e.either();
			int w = e.other(v);
			if (uf.connected(v, w)) {
				maxPQ.remove();
			} else {
				maxPQ.add(e);
				uf.union(v, w);
			}
		}
	}
	
	public Iterable<Edge> edges() {
		return maxPQ;
	}
}

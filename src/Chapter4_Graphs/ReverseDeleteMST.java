package Chapter4_Graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Ex 4.3.24 
 * Reverse-delete algorithm. 
 */
public class ReverseDeleteMST {

	private Queue<Edge> mst;
	private boolean[] marked;
	
	public ReverseDeleteMST(EdgeWeightedGraph G) {
		List<Edge> edges = new ArrayList<>();
		for (Edge e : G.edges())
			edges.add(e);
		Collections.sort(edges, Collections.reverseOrder());
		mst = new LinkedList<>();
		for (Edge e : edges) {
			int v = e.either();
			int w = e.other(v);
			G.removeEdge(e);
			if (!isConnected(G, v, w))
				mst.add(e);
		}
	}
	
	// bfs
	private boolean isConnected(EdgeWeightedGraph G, int s, int d) {
		Queue<Integer> q = new LinkedList<Integer>();
		marked[s] = true;
		q.add(s);
		while(!q.isEmpty()) {
			int v = q.poll();
			for (Edge e : G.adj(v)) {
				int w = e.other(v);
				if (w == d) return true;		// connected
				if (!marked[w]) {
					marked[w] = true;
					q.add(w);
				}
			}
		}
		return false;
	}
	
	public Iterable<Edge> edges() {
		return mst;
	}
}

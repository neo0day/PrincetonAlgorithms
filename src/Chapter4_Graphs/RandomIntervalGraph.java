package Chapter4_Graphs;

import java.util.Random;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Ex 4.1.45
 * Random interval graphs. Consider a collection of V intervals on the real line (pairs 
 * of real numbers). Such a collection defines an interval graph with one vertex corresponding
 * to each interval, with edges between vertices if the corresponding intervals intersect 
 * (have any points in common). Write a program that generates V random intervals in the unit 
 * interval, all of length d, then builds the corresponding interval graph. Hint: Use a BST.
 */
public class RandomIntervalGraph {
	private Graph G;
	private HashMap<Integer, Integer> map;		// interval start --> index
	private int[] intervals;
	private final int d = 10;

	public RandomIntervalGraph() {
		Random random = new Random();
		int V = random.nextInt(20);
		// randomize unit intervals
		random_unit_interval(V);
		// sort
		Arrays.sort(intervals);
		// build the graph
		G = new Graph(V);
		for (int i = 0; i < V; i++) {
			int v = intervals[i];
			for (int j = i + 1; j < V; j++) {
				int w = intervals[j];
				if (w <= v + d)		// edge for interval graph
					G.addEdge(map.get(v), map.get(w));
				else
					break;
			}
		}
	}

	private void random_unit_interval(int V) {
		Random random = new Random();
		map = new HashMap<>();
		intervals = new int[V];
		while (map.size() < V) {
			int v = random.nextInt(100);
			if (!map.containsKey(v)) {
				intervals[map.size()] = v;
				map.put(v, map.size());
				System.out.println("vertex " + v + ": " + map.get(v));
			}
		}
		
	}
	
	public String toString() {
		return G.toString();
	}

	public static void main(String[] args) {
		RandomIntervalGraph G = new RandomIntervalGraph();
		System.out.println(G);
	}
}

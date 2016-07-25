package Chapter4_Graphs;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * Ex 4.1.37
 * Euclidean graphs.
 */
public class EuclideanGraph {
	private ST<String, Integer> st;  // string -> index
	private String[] keys;           // index  -> string
	private Graph G;
	private Point[] points;
	private boolean[] marked;

	@SuppressWarnings("resource")
	public EuclideanGraph(String filename, String delimiter) throws FileNotFoundException {
		st = new ST<String, Integer>();

		// First pass builds the index by reading strings to associate
		// distinct strings with an index
		Scanner in = new Scanner(new File(filename));
		while (in.hasNextLine()) {
			String point = in.next();
			System.out.println(point);
			if (!st.contains(point))
				st.put(point, st.size());
				
		}
		StdOut.println("Done reading " + filename);
		
		// inverted index to get string keys in an array
		keys = new String[st.size()];
		for (String name : st.keys()) {
			keys[st.get(name)] = name;
		}
		
		// convert string to Point
		points = new Point[st.size()];
		for (int i = 0; i < st.size(); i++) {
			int loc = keys[i].indexOf(",");
			int x = Integer.parseInt(keys[i].substring(1, loc));
			int y = Integer.parseInt(keys[i].substring(loc + 1, keys[i].length() - 1));
			points[i] = new Point(x,y);
		}

		// second pass builds the graph by connecting first vertex on each
		// line to all others
		G = new Graph(st.size());
		in = new Scanner(new File(filename));
		while (in.hasNextLine()) {
			String x = in.next();
			int v = st.get(x);
			String y = in.next();
			int w = st.get(y);
			G.addEdge(v, w);
		}
	}
	
	public void show() {
		StdDraw.setScale(-1, 10);
		marked = new boolean[G.V()];
		for (int v = 0; v < G.V(); v++) {
			if (!marked[v])
				bfs(v);
		}
	}

	private void bfs(int s) {
		Queue<Integer> Q = new LinkedList<>();
		marked[s] = true;
		Q.add(s);
		while (!Q.isEmpty()) {
			int v = Q.poll();
			StdDraw.setPenRadius(0.02);
			StdDraw.point(points[v].getX(), points[v].getY());
			for (int w : G.adj(v)) {
				StdDraw.setPenRadius();
				StdDraw.line(points[v].getX(), points[v].getY(), points[w].getX(), points[w].getY());
				if (!marked[w]) {
					marked[w] = true;
					Q.add(w);
				}
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		String filename  = args[0];
		EuclideanGraph G = new EuclideanGraph(filename, " ");
		G.show();
	}
}

package Chapter4_Graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class SymbolGraph {
	private HashMap<String, Integer> st;  // string -> index
	private String[] keys;           // index  -> string
	private Graph G;

	public SymbolGraph(String filename, String delimiter) throws FileNotFoundException {
		st = new HashMap<>();

		// First pass builds the index by reading strings to associate
		// distinct strings with an index
		Scanner in = new Scanner(new File(filename));
		while (in.hasNextLine()) {
			String[] a = in.nextLine().split(delimiter);
			for (int i = 0; i < a.length; i++) {
				if (!st.containsKey(a[i]))
					st.put(a[i], st.size());
			}
		}
		System.out.println("Done reading " + filename);

		// inverted index to get string keys in an aray
		keys = new String[st.size()];
		for (String name : st.keySet()) {
			keys[st.get(name)] = name;
		}

		// second pass builds the graph by connecting first vertex on each
		// line to all others
		G = new Graph(st.size());
		in = new Scanner(new File(filename));
		while (in.hasNextLine()) {
			String[] a = in.nextLine().split(delimiter);
			int v = st.get(a[0]);
			for (int i = 1; i < a.length; i++) {
				int w = st.get(a[i]);
				G.addEdge(v, w);
			}
		}
		in.close();
	}

	public SymbolGraph(String filename, String delimiter, int year) throws FileNotFoundException {
		st = new HashMap<>();

		// First pass builds the index by reading strings to associate
		// distinct strings with an index
		Scanner in = new Scanner(new File(filename));
		while (in.hasNextLine()) {
			String[] a = in.nextLine().split(" ");
			for (int i = 0; i < a.length; i++) {
				if (!st.containsKey(a[i]))
					st.put(a[i], st.size());
			}
		}
		System.out.println("Done reading " + filename);

		// inverted index to get string keys in an aray
		keys = new String[st.size()];
		for (String name : st.keySet()) {
			keys[st.get(name)] = name;
		}

		// second pass builds the graph by connecting first vertex on each
		// line to all others
		G = new Graph(st.size());
		in = new Scanner(new File(filename));
		while (in.hasNextLine()) {
			String[] a = in.nextLine().split(" ");
			String[] yarr = a[0].split("[\\(\\)]");
			String y = yarr[yarr.length - 1];
			if (y.contains(" "))
				y = y.substring(0, y.indexOf(" "));
			int yrs = Integer.parseInt(y);
			if (yrs < 2012 - year)		// ignore movies that are more than y years old.
				continue;
			int v = st.get(a[0]);
			for (int i = 1; i < a.length; i++) {
				int w = st.get(a[i]);
				G.addEdge(v, w);
			}
		}
		in.close();
	}

	public boolean contains(String s) {
		return st.containsKey(s);
	}

	public int index(String s) {
		return st.get(s);
	}

	public String name(int v) {
		return keys[v];
	}

	public Graph G() {
		return G;
	}

	/**
	 * Ex 4.1.23
	 * Write a program BaconHistogram
	 */
	public void BaconHistogram(String source) {
		if (!this.contains(source)) {
			System.out.println(source + " not in database.");
			return;
		}

		// run breadth-first search from s
		int s = this.index(source);
		BFS bfs = new BFS(G, s);

		// compute histogram of Kevin Bacon numbers - 100 for infinity
		int MAX_BACON = 100;
		int[] hist = new int[MAX_BACON + 1];
		for (int v = 0; v < G.V(); v++) {
			int bacon = Math.min(MAX_BACON, bfs.distTo(v));
			hist[bacon]++;

			// to print actors and movies with large bacon numbers
			if (bacon/2 >= 7 && bacon < MAX_BACON)
				System.out.printf("%d %s\n", bacon/2, this.name(v));
		}

		// print out histogram - even indices are actors
		for (int i = 0; i < MAX_BACON; i += 2) {
			if (hist[i] == 0) break;
			System.out.printf("%3d %8d\n", i/2, hist[i]);
		}
		System.out.printf("Inf %8d\n", hist[MAX_BACON]);
	}

	/**
	 * Ex 4.1.24
	 * Compute the number of connected components in movies.txt
	 */
	public void count_cc() {
		CC cc = new CC(G);
		System.out.println("number of CC: " + cc.count());
		int max = 0, cnt = 0, maxV = 0;
		for (int v = 0; v < G.V(); v++) {
			if (cc.size(v) < 10)
				cnt++;
			if (cc.size(v) > max) {
				max = cc.size(v);
				maxV = v;
			}
		}
		System.out.println("size of largest CC: " + max + ", # of components of size less than 10: " + cnt);

		// build a new graph of largest component
		HashMap<Integer, Integer> maxST = new HashMap<>();
		Graph maxCC = new Graph(max);
		bfs(maxCC, maxV, maxST);
		assert maxST.size() == maxCC.V();
		GraphProperties gp = new GraphProperties(maxCC);		// This gonna take a while
		System.out.println("diameter: " + gp.diameter() + " radius: " + gp.radius() + " center: " + gp.center());

		String name = "Bacon, Kevin";
		System.out.println(name + (cc.connected(st.get(name), maxV) ? " " : " not ") + "in largest CC.");
	}

	private void bfs(Graph maxCC, int maxV, HashMap<Integer, Integer> maxST) {
		boolean[] marked = new boolean[G.V()];
		Queue<Integer> q = new LinkedList<>();
		marked[maxST.size()] = true;
		maxST.put(maxV, maxST.size());
		q.add(maxV);
		while (!q.isEmpty()) {
			int v = q.poll();
			int v2;
			if (maxST.containsKey(v))
				v2 = maxST.get(v);
			else {
				v2 = maxST.size();
				maxST.put(v, maxST.size());
			}
			for (int w : G.adj(v)) {
				int w2;
				if (maxST.containsKey(w))
					w2 = maxST.get(w);
				else {
					w2 = maxST.size();
					maxST.put(w, maxST.size());
				}
				maxCC.addEdge(v2, w2);
				if (!marked[maxST.get(w)]) {
					q.add(w);
					marked[maxST.get(w)] = true;
				}
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		String filename  = args[0];
		SymbolGraph sg = new SymbolGraph(filename, "/");
		sg.BaconHistogram("Bacon, Kevin");
		sg.count_cc();
	}
}

/******************************************************************************
 *  Copyright 2002-2015, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/

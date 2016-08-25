package Chapter4_Graphs;

import java.io.FileNotFoundException;
import edu.princeton.cs.algs4.StdIn;

public class DegreesOfSeparation {

	// this class cannot be instantiated
	private DegreesOfSeparation() { }

	/**
	 * Ex 4.1.25
	 * Modify DegreesOfSeparation
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		String filename  = args[0];
		int year		 = Integer.parseInt(args[1]);
		String delimiter = "/";
		String source    = "Bacon, Kevin";

		SymbolGraph sg = new SymbolGraph(filename, delimiter, year);
		Graph G = sg.G();
		if (!sg.contains(source)) {
			System.out.println(source + " not in database.");
			return;
		}

		int s = sg.index(source);
		BFS bfs = new BFS(G, s);

		while (!StdIn.isEmpty()) {
			String sink = StdIn.readLine();
			if (sink.equals("exit"))
				break;
			if (sg.contains(sink)) {
				int t = sg.index(sink);
				if (bfs.hasPathTo(t)) {
					for (int v : bfs.pathTo(t)) {
						System.out.println("   " + sg.name(v));
					}
				}
				else {
					System.out.println("Not connected");
				}
			}
			else {
				System.out.println("   Not in database.");
			}
		}

		/**
		 * Ex 4.1.26
		 * Write a SymbolGraph client like DegreesOfSeparation
		 */
		DFS dfs = new DFS(G, s);
		while (!StdIn.isEmpty()) {
			String sink = StdIn.readLine();
			if (sink.equals("exit"))
				break;
			if (sg.contains(sink)) {
				int t = sg.index(sink);
				if (dfs.hasPathTo(t)) {
					for (int v : dfs.pathTo(t))
						System.out.println("   " + sg.name(v));
				} else {
					System.out.println("Not connected");
				}
			} else {
				System.out.println("   Not in database.");
			}
		}
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

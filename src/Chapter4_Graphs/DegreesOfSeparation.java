package Chapter4_Graphs;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class DegreesOfSeparation {

    // this class cannot be instantiated
    private DegreesOfSeparation() { }

    /**
     * Ex 4.1.25
     * Modify DegreesOfSeparation
     */
    public static void main(String[] args) {
        String filename  = args[0];
        int year		 = Integer.parseInt(args[1]);
        String delimiter = "/";
        String source    = "Bacon, Kevin";

        SymbolGraph sg = new SymbolGraph(filename, delimiter, year);
        Graph G = sg.G();
        if (!sg.contains(source)) {
            StdOut.println(source + " not in database.");
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
                        StdOut.println("   " + sg.name(v));
                    }
                }
                else {
                    StdOut.println("Not connected");
                }
            }
            else {
                StdOut.println("   Not in database.");
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
        				StdOut.println("   " + sg.name(v));
        		} else {
        			StdOut.println("Not connected");
        		}
        	} else {
        		StdOut.println("   Not in database.");
        	}
        }
    }
}

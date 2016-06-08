package Chapter4_Graphs;

import java.util.LinkedList;
import java.util.Queue;

public class CC {
    private boolean[] marked;   // marked[v] = has vertex v been marked?
    private int[] id;           // id[v] = id of connected component containing v
    private int[] size;         // size[id] = number of vertices in given component
    private int count;          // number of connected components
    
    public CC(Graph G) {
        marked = new boolean[G.V()];
        id = new int[G.V()];
        size = new int[G.V()];
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) {
                bfs(G, v);
                count++;
            }
        }
    }
    
    private void bfs(Graph G, int s) {
    	Queue<Integer> q = new LinkedList<>();
    	q.add(s);
        marked[s] = true;
        while (!q.isEmpty()) {
        	int v = q.poll();
            id[v] = count;
            size[count]++;
        	for (int w : G.adj(v)) {
                if (!marked[w]) {
                    q.add(w);
                    marked[w] = true;
                }
            }
        }
        
    }
    
    public int id(int v) {
        return id[v];
    }
    
    public int size(int v) {
        return size[id[v]];
    }
    
    public int count() {
    	return count;
    }
    
    public boolean connected(int v, int w) {
    	return id(v) == id(w);
    }
    
}

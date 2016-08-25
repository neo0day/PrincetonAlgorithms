package Chapter4_Graphs;

/**
 * Ex 4.2.29
 * Arithmetic expressions. 
 */
public class DAGEval {

	private String[] nodes;
	private Digraph G;
	private boolean[] marked;

	public DAGEval(Digraph G, String[] array) {
		this.nodes = new String[array.length];
		this.marked = new boolean[array.length];
		for (int i = 0; i < array.length; i++) {
			nodes[i] = array[i];
		}
		this.G = G;
	}

	// dfs equivalent
	public int eval(int s) {
		int[] nums = new int[2];
		int i = 0;
		marked[s] = true;
		int value = 0;
		if (nodes[s].equals("+") || nodes[s].equals("-")
				|| nodes[s].equals("*") || nodes[s].equals("/")) {
			for (int v : G.adj(s)) {
				if (!marked[v])
					nums[i++] = eval(v);
				else		// cross-edge, sub-tree has already evaluated
					nums[i++] = Integer.parseInt(nodes[v]);
			}
			// now evaluates the equation of two variables
			if (nodes[s].equals("+"))
				value = nums[0] + nums[1];
			else if (nodes[s].equals("-"))
				value = nums[0] - nums[1];
			else if (nodes[s].equals("*"))
				value = nums[0] * nums[1];
			else if (nodes[s].equals("/"))
				value = nums[0] / nums[1];
			// save the value that has been evaluated
			nodes[s] = String.valueOf(value);
		} else {			// leaves ==> return value
			value = Integer.parseInt(nodes[s]);
		}
		return value;
	}

	public static void main(String[] args) {
		// Build a DAG
		Digraph G = new Digraph(8);
		String[] array = new String[8];
		array[0] = "+";
		array[1] = "+";
		array[2] = "/";
		array[3] = "2";
		array[4] = "*";
		array[5] = "5";
		array[6] = "3";
		array[7] = "4";
		G.addEdge(0, 1);
		G.addEdge(0, 2);
		G.addEdge(1, 3);
		G.addEdge(1, 4);
		G.addEdge(2, 4);
		G.addEdge(2, 5);
		G.addEdge(4, 6);
		G.addEdge(4, 7);
		// evaluation using DFS
		DAGEval eval = new DAGEval(G, array);
		System.out.println(eval.eval(0));
	}
}

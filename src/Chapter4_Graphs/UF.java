package Chapter4_Graphs;

import java.util.Scanner;

public class UF {

	private int[] parent;  // parent[i] = parent of i
	private byte[] rank;   // rank[i] = rank of subtree rooted at i (never more than 31)
	private int count;     // number of components

	public UF(int N) {
		if (N < 0) throw new IllegalArgumentException();
		count = N;
		parent = new int[N];
		rank = new byte[N];
		for (int i = 0; i < N; i++) {
			parent[i] = i;
			rank[i] = 0;
		}
	}

	public int find(int p) {
		validate(p);
		while (p != parent[p]) {
			parent[p] = parent[parent[p]];    // path compression by halving
			p = parent[p];
		}
		return p;
	}

	public int count() {
		return count;
	}

	public boolean connected(int p, int q) {
		return find(p) == find(q);
	}

	public void union(int p, int q) {
		int rootP = find(p);
		int rootQ = find(q);
		if (rootP == rootQ) return;

		// make root of smaller rank point to root of larger rank
		if      (rank[rootP] < rank[rootQ]) parent[rootP] = rootQ;
		else if (rank[rootP] > rank[rootQ]) parent[rootQ] = rootP;
		else {
			parent[rootQ] = rootP;
			rank[rootP]++;
		}
		count--;
	}

	// validate that p is a valid index
	private void validate(int p) {
		int N = parent.length;
		if (p < 0 || p >= N) {
			throw new IndexOutOfBoundsException("index " + p + " is not between 0 and " + (N-1));  
		}
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int N = in.nextInt();
		UF uf = new UF(N);
		while (!in.hasNext()) {
			int p = in.nextInt();
			int q = in.nextInt();
			if (uf.connected(p, q)) continue;
			uf.union(p, q);
			System.out.println(p + " " + q);
		}
		System.out.println(uf.count() + " components");
		in.close();
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

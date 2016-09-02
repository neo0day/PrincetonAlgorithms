package Chapter4_Graphs;

public class Edge implements Comparable<Edge> { 

	private final int v;
	private final int w;
	private final double weight;

	public Edge(int v, int w, double weight) {
		if (v < 0) throw new IndexOutOfBoundsException("Vertex name must be a nonnegative integer");
		if (w < 0) throw new IndexOutOfBoundsException("Vertex name must be a nonnegative integer");
		if (Double.isNaN(weight)) throw new IllegalArgumentException("Weight is NaN");
		this.v = v;
		this.w = w;
		this.weight = weight;
	}

	public double weight() {
		return weight;
	}

	public int either() {
		return v;
	}

	public int other(int vertex) {
		if      (vertex == v) return w;
		else if (vertex == w) return v;
		else throw new IllegalArgumentException("Illegal endpoint");
	}

	@Override
	public int compareTo(Edge that) {
		if      (this.weight() < that.weight()) return -1;
		else if (this.weight() > that.weight()) return +1;
		else                                    return  0;
	}

	public String toString() {
		return String.format("%d-%d %.5f", v, w, weight);
	}

	public static void main(String[] args) {
		Edge e = new Edge(12, 34, 5.67);
		System.out.println(e);
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

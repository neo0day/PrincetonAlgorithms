package Chapter4_Graphs;


import java.util.Iterator;
import java.util.NoSuchElementException;

public class IndexMinPQ<Key extends Comparable<Key>> implements Iterable<Integer> {
	private int maxN;        // maximum number of elements on PQ
	private int N;           // number of elements on PQ
	private int[] pq;        // binary heap using 1-based indexing
	private int[] qp;        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
	private Key[] keys;      // keys[i] = priority of i

	@SuppressWarnings("unchecked")
	public IndexMinPQ(int maxN) {
		if (maxN < 0) throw new IllegalArgumentException();
		this.maxN = maxN;
		keys = (Key[]) new Comparable[maxN + 1];    // make this of length maxN??
		pq   = new int[maxN + 1];
		qp   = new int[maxN + 1];                   // make this of length maxN??
		for (int i = 0; i <= maxN; i++)
			qp[i] = -1;
	}

	public boolean isEmpty() {
		return N == 0;
	}

	public boolean contains(int i) {
		if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
		return qp[i] != -1;
	}

	public int size() {
		return N;
	}

	public void insert(int i, Key key) {
		if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
		if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");
		N++;
		qp[i] = N;
		pq[N] = i;
		keys[i] = key;
		swim(N);
	}

	public int minIndex() {
		if (N == 0) throw new NoSuchElementException("Priority queue underflow");
		return pq[1];
	}

	public Key minKey() {
		if (N == 0) throw new NoSuchElementException("Priority queue underflow");
		return keys[pq[1]];
	}

	public int delMin() {
		if (N == 0) throw new NoSuchElementException("Priority queue underflow");
		int min = pq[1];
		exch(1, N--);
		sink(1);
		assert min == pq[N+1];
		qp[min] = -1;        // delete
		keys[min] = null;    // to help with garbage collection
		pq[N+1] = -1;        // not needed
		return min;
	}

	public Key keyOf(int i) {
		if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
		if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
		else return keys[i];
	}

	public void changeKey(int i, Key key) {
		if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
		if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
		keys[i] = key;
		swim(qp[i]);
		sink(qp[i]);
	}

	public void decreaseKey(int i, Key key) {
		if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
		if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
		if (keys[i].compareTo(key) <= 0)
			throw new IllegalArgumentException("Calling decreaseKey() with given argument would not strictly decrease the key");
		keys[i] = key;
		swim(qp[i]);
	}

	public void increaseKey(int i, Key key) {
		if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
		if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
		if (keys[i].compareTo(key) >= 0)
			throw new IllegalArgumentException("Calling increaseKey() with given argument would not strictly increase the key");
		keys[i] = key;
		sink(qp[i]);
	}

	public void delete(int i) {
		if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
		if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
		int index = qp[i];
		exch(index, N--);
		swim(index);
		sink(index);
		keys[i] = null;
		qp[i] = -1;
	}


	/***************************************************************************
	 * General helper functions.
	 ***************************************************************************/
	private boolean greater(int i, int j) {
		return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
	}

	private void exch(int i, int j) {
		int swap = pq[i];
		pq[i] = pq[j];
		pq[j] = swap;
		qp[pq[i]] = i;
		qp[pq[j]] = j;
	}


	/***************************************************************************
	 * Heap helper functions.
	 ***************************************************************************/
	private void swim(int k) {
		while (k > 1 && greater(k/2, k)) {
			exch(k, k/2);
			k = k/2;
		}
	}

	private void sink(int k) {
		while (2*k <= N) {
			int j = 2*k;
			if (j < N && greater(j, j+1)) j++;
			if (!greater(k, j)) break;
			exch(k, j);
			k = j;
		}
	}


	/***************************************************************************
	 * Iterators.
	 ***************************************************************************/

	public Iterator<Integer> iterator() { return new HeapIterator(); }

	private class HeapIterator implements Iterator<Integer> {
		// create a new pq
		private IndexMinPQ<Key> copy;

		// add all elements to copy of heap
		// takes linear time since already in heap order so no keys move
		public HeapIterator() {
			copy = new IndexMinPQ<Key>(pq.length - 1);
			for (int i = 1; i <= N; i++)
				copy.insert(pq[i], keys[pq[i]]);
		}

		public boolean hasNext()  { return !copy.isEmpty();                     }
		public void remove()      { throw new UnsupportedOperationException();  }

		public Integer next() {
			if (!hasNext()) throw new NoSuchElementException();
			return copy.delMin();
		}
	}

	public static void main(String[] args) {
		// insert a bunch of strings
		String[] strings = { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };

		IndexMinPQ<String> pq = new IndexMinPQ<String>(strings.length);
		for (int i = 0; i < strings.length; i++) {
			pq.insert(i, strings[i]);
		}

		// delete and print each key
		while (!pq.isEmpty()) {
			int i = pq.delMin();
			System.out.println(i + " " + strings[i]);
		}
		System.out.println();

		// reinsert the same strings
		for (int i = 0; i < strings.length; i++) {
			pq.insert(i, strings[i]);
		}

		// print each key using the iterator
		for (int i : pq) {
			System.out.println(i + " " + strings[i]);
		}
		while (!pq.isEmpty()) {
			pq.delMin();
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

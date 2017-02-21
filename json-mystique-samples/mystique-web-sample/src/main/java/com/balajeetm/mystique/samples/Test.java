package com.balajeetm.mystique.samples;

import lombok.Data;

public class Test {

	public static void main(String[] args) {
		Test test = new Test();
		Integer k = 2;
		Node root = test.getNodes(1, 2, 3, 4, 5, 6, 7);
		test.printNodes(root);

		/*Node reverse = test.reverse(root);
		test.printNodes(reverse);*/

		Node reverse = test.limitReverse(root, k);
		test.printNodes(reverse);

	}

	private void printNodes(Node root) {
		System.out.println("Printing Nodes");
		while (root != null) {
			System.out.println(root.getValue());
			root = root.getNext();
		}
	}

	private Node getNodes(Integer... integers) {
		Node root = null;
		Node prev = null;
		Node curr = null;
		for (Integer integer : integers) {
			curr = new Node();
			curr.setValue(integer);
			root = null == root ? curr : root;
			if (null != prev) {
				prev.setNext(curr);
			}
			prev = curr;
		}
		return root;
	}

	private Node limitReverse(Node root, Integer limit) {
		Integer count = 0;
		Node prev = null;
		Node curr = null;
		Node next = null;

		curr = root;
		while (count < limit && null != curr) {
			next = curr.getNext();
			curr.setNext(prev);
			prev = curr;
			curr = next;
			count++;
		}

		root.setNext(null == curr ? curr : limitReverse(curr, limit));
		return prev;
	}

	private Node reverse(Node root) {
		Node prev = null;
		Node curr = null;
		Node next = null;

		curr = root;
		while (null != curr) {
			next = curr.getNext();
			curr.setNext(prev);
			prev = curr;
			curr = next;
		}
		return prev;
	}

	@Data
	public static class Node {
		private Integer value;
		private Node next;
	}

}

package search;

import java.util.Comparator;

abstract class NodeFunction implements Comparator<Node> {
	
	abstract int apply(Node node);

	public int compare(Node a, Node b) {
		if (apply(a) < apply(b)) {
			return -1;
		}

		if (apply(a) > apply(b)) {
			return 1;
		}

		return 0;	// f(a) == f(b)
	}
}
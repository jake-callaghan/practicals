package search;

import java.util.Comparator;

public class NodeFunction implements Comparator<Node> {
	
	public int apply(Node node) {
		return 0;
	}

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
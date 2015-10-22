package search;

public interface Frontier {
	boolean isEmpty();

	void clear();

	Node remove() throws FrontierException;

	void add(Node node);
}
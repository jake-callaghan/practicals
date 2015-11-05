package search;

public interface Frontier {
	boolean isEmpty();

	void clear();

	Node remove() throws FrontierException;

	void add(Node node);

	int maxSeen();

	int seen();

	void clearMaxSeen();

	void clearSeen();
}
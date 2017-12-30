import java.util.List;

public interface GraphInterface<T> {
	
	public boolean addVertex(int v);
	
	public abstract void addEdge(T edge);
	
	public int getNumVertices();
	
	public int getNumEdges();
	
	public boolean containsVertex(int v);
	
	public List<Integer> getVertices();
	
	public List<T> getOutgoingEdges(int v);
	
	public void printGraph();
	
}

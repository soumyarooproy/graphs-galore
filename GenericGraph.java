import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public abstract class GenericGraph<T> implements GraphInterface<T> {
	
	protected Map<Integer, List<T>> vMap;
	protected int numEdges;
	
	public boolean addVertex(int v) {
		if (!vMap.containsKey(v)) {
			vMap.put(v, new ArrayList<T>());
			return true;
		}
		return false;
	}
	
	public abstract void addEdge(T edge);
	
	public int getNumVertices() {
		return vMap.size();
	}
	
	public int getNumEdges() {
		return numEdges;
	}
	
	public boolean containsVertex(int v) {
		return vMap.containsKey(v);
	}
	
	public List<Integer> getVertices() {
		return new ArrayList<>(vMap.keySet());
	}
	
	public List<T> getOutgoingEdges(int v) {
		return vMap.get(v);
	}
	
	public void printGraph() {
		System.out.println(getNumVertices() + " vertices, " + getNumEdges() + " edges");
		for (int v : vMap.keySet()) {
			String line = v + ": ";
			for (T edge : vMap.get(v)) {
				line += edge + ", ";
			}
			System.out.println(line);
		}
	}

}

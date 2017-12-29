import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public abstract class GenericDiGraph<T> implements GraphInterface<T> {
	
	protected Map<Integer, List<T>> vMapOutgoing;
	protected Map<Integer, List<T>> vMapIncoming;
	protected int numEdges;
	
	public boolean addVertex(int v) {
		if (!vMapOutgoing.containsKey(v)) {
			vMapOutgoing.put(v, new ArrayList<T>());
			vMapIncoming.put(v, new ArrayList<T>());
			return true;
		}
		return false;
	}
	
	public abstract void addEdge(T edge);
	
	public int getNumVertices() {
		return vMapOutgoing.size();
	}
	
	public int getNumEdges() {
		return numEdges;
	}
	
	public boolean containsVertex(int v) {
		return vMapOutgoing.containsKey(v);
	}
	
	public List<Integer> getVertices() {
		return new ArrayList<>(vMapOutgoing.keySet());
	}
	
	public List<T> getOutgoingEdges(int v) {
		return vMapOutgoing.get(v);
	}
	
	public List<T> getIncomingEdges(int v) {
		return vMapIncoming.get(v);
	}
	
	public void printGraph() {
		System.out.println(getNumVertices() + " vertices, " + getNumEdges() + " edges");
		for (int v :vMapOutgoing.keySet()) {
			String line = v + ": ";
			for (T edge : vMapOutgoing.get(v)) {
				line += edge + ", ";
			}
			System.out.println(line);
		}
	}

}

import java.util.HashMap;
import java.util.List;

public class WeightedDiGraph extends GenericDiGraph<WeightedEdge> {

	public WeightedDiGraph() {
		vMapOutgoing = new HashMap<Integer, List<WeightedEdge>>();
		vMapIncoming = new HashMap<Integer, List<WeightedEdge>>();
		numEdges = 0;
	}
	
	public WeightedDiGraph(WeightedDiGraph WG) {
		super();
		for (int v : vMapOutgoing.keySet()) {
			for (WeightedEdge e: vMapOutgoing.get(v)) {
				addEdge(e);
			}
		}
	}
	
	@Override
	public void addEdge(WeightedEdge e) {
		int source = e.getFromVertex();
		int destination = e.getToVertex();
		addVertex(source);
		addVertex(destination);
		vMapOutgoing.get(source).add(e);
		vMapIncoming.get(destination).add(e);
		numEdges++;
	}

}

import java.util.HashMap;
import java.util.List;

public class WeightedGraph extends GenericGraph<WeightedEdge> {
	
	public WeightedGraph() {
		vMap = new HashMap<Integer, List<WeightedEdge>>();
		numEdges = 0;
	}
	
	public WeightedGraph(WeightedGraph WG) {
		super();
		for (int v : WG.vMap.keySet()) {
			for (WeightedEdge e : WG.vMap.get(v)) {
				this.addEdge(e);
			}
		}
	}

	@Override
	public void addEdge(WeightedEdge e) {
		int source = e.getFromVertex();
		int destination = e.getToVertex();
		double weight = e.getWeight();
		addVertex(source);
		addVertex(destination);
		vMap.get(source).add(e);
		vMap.get(destination).add(new WeightedEdge(destination, source, weight));
		numEdges++;
	}
	
}

import java.util.HashMap;
import java.util.List;

public class DiGraph extends GenericDiGraph<SimpleEdge> {
	
	public DiGraph() {
		vMapOutgoing = new HashMap<Integer, List<SimpleEdge>>();
		vMapIncoming = new HashMap<Integer, List<SimpleEdge>>();
		numEdges = 0;
	}
	
	public DiGraph(DiGraph G) {
		super();
		for (int v : vMapOutgoing.keySet()) {
			for (SimpleEdge e : vMapOutgoing.get(v)) {
				this.addEdge(e);
			}
		}
	}

	@Override
	public void addEdge(SimpleEdge e) {
		int source = e.getFromVertex();
		int destination = e.getToVertex();
		addVertex(source);
		addVertex(destination);
		vMapOutgoing.get(source).add(e);
		vMapIncoming.get(destination).add(e);
		numEdges++;
	}

}

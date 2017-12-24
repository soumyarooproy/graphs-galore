import java.util.HashMap;
import java.util.List;

public class Graph extends GenericGraph<SimpleEdge> {
	
	public Graph() {
		vMap = new HashMap<Integer, List<SimpleEdge>>();
		numEdges = 0;
	}
	
	public Graph(Graph G) {
		super();
		for (int v : G.vMap.keySet()) {
			for (SimpleEdge e : G.vMap.get(v)) {
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
		vMap.get(source).add(e);
		vMap.get(destination).add(new SimpleEdge(destination, source));
		numEdges++;
	}
	
}

public class SimpleEdge {
	
	private int from;
	private int to;
	
	public SimpleEdge(int from, int to) {
		this.from = from;
		this.to = to;
	}
	
	public int getFromVertex() {
		return from;
	}
	
	public int getToVertex() {
		return to;
	}
	
	@Override
	public String toString() {
		return "(" + from + ", " + to + ")";
	}

}

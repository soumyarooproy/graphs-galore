public class WeightedEdge {

	private int from;
	private int to;
	private double weight;
	
	public WeightedEdge(int from, int to, double weight) {
		this.from = from;
		this.to = to;
		this.weight = weight;
	}
	
	public int getFromVertex() {
		return from;
	}
	
	public int getToVertex() {
		return to;
	}
	
	public double getWeight() {
		return weight;
	}
	
	@Override
	public String toString() {
		return "(" + from + ", " + to + "): " + weight;
	}
}

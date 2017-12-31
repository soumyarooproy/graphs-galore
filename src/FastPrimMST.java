import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class FastPrimMST {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("ERROR: One argument, the input file name, is required.");
			return;
		}
		String inputFileName = args[0];
		String outputFileName = "output.txt";
		WeightedGraph G;
		try {
			// read input file
			Scanner scanner = new Scanner(new File(inputFileName));
			if (scanner.hasNextLine()) {
				String graphType = scanner.nextLine();
				if (graphType.equals("directed")) {
					System.out.println("ERROR: Graph is assumed to be undirected.");
					scanner.close();
					return;
				}
				else if (graphType.equals("undirected")) {
					G = new WeightedGraph();
				}
				else {
					System.out.println("ERROR: First line of input file must state if graph is either directed or undirected.");
					System.out.println("NOTE: Prim's MST algorithm assumes graph is undirected.");
					scanner.close();
					return;
				}
			}
			else {
				System.out.println("ERROR: input file is empty.");
				scanner.close();
				return;
			}
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] split = line.split("\\s+");
				if (split.length == 1) {
					int vertex1 = Integer.parseInt(split[0]);
					G.addVertex(vertex1);
				}
				else if (split.length == 2) {
					System.out.println("ERROR: Each edge must include a weight.");
					scanner.close();
					return;
				}
				else if (split.length == 3) {
					int vertex1 = Integer.parseInt(split[0]);
					int vertex2 = Integer.parseInt(split[1]);
					int weight = Integer.parseInt(split[2]);
					G.addEdge(new WeightedEdge(vertex1, vertex2, weight));
				}
				else {
					System.out.println("ERROR: Each line is an edge that consists of two vertex integers and a double weight separated by spaces.");
					System.out.println("NOTE: This Minimum Spanning Tree algorithm assume graph given is connected, undirected, and weighted.");
					scanner.close();
					return;
				}
			}
			scanner.close();
			
			// check that given graph is connected
			if (!isConnected(G)) {
				System.out.println("ERROR: Graph is not connected.");
				return;
			}
			
			// find MST and overall cost of MST
			List<WeightedEdge> mst = getMST(G);
			double overallCost = getOverallCost(mst);
			
			// write to file
			FileWriter fw = new FileWriter(new File(outputFileName));
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(Double.toString(overallCost));
			bw.newLine();
			for (WeightedEdge e : mst) {
				bw.write(e.toString());
				bw.newLine();
			}
			bw.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: " + inputFileName + " file not found.");
		} catch (IOException e1) {
			System.out.println("ERROR: could not write file " + outputFileName + ".");
			e1.printStackTrace();
		}
	}
	
	public static boolean isConnected(GenericGraph<WeightedEdge> G) {
		Queue<Integer> queue = new PriorityQueue<Integer>();
		int s = G.getVertices().get(0); // pick arbitrary vertex
		queue.add(s);
		Set<Integer> visited = new HashSet<Integer>();
		visited.add(s);
		while (!queue.isEmpty()) {
			int curr = queue.poll();
			for (WeightedEdge edge : G.getOutgoingEdges(curr)) {
				int next = edge.getToVertex();
				if (!visited.contains(next)) {
					visited.add(next);
					queue.add(next);
				}
			}
		}
		return (visited.size() == G.getNumVertices());
	}
	
	private static class Vertex implements Comparable<Vertex> {
		private int id;
		private WeightedEdge cheapestEdge;
		public Vertex(int id) {
			this.id = id;
			this.cheapestEdge = null;
		}
		public int getId() {return id;}
		public WeightedEdge getCheapestEdge() {return cheapestEdge;}
		public double getCheapestEdgeWeight() {return (cheapestEdge == null) ? Double.MAX_VALUE : cheapestEdge.getWeight();}
		public void setCheapestEdge(WeightedEdge e) {cheapestEdge = e;}
		@Override
		public int compareTo(Vertex o) {
			double c1 = this.getCheapestEdgeWeight();
			double c2 = o.getCheapestEdgeWeight();
			if (c1 < c2) return -1;
			else if (c1 == c2) return 0;
			else return 1;
		}
	}
	
	public static List<WeightedEdge> getMST(WeightedGraph G) {
		// INITIALIZATION
		// initialize V
		Map<Integer, Vertex> mapToVertex = new HashMap<Integer, Vertex>();
		List<Vertex> V = new ArrayList<Vertex>();
		for (int vint : G.getVertices()) {
			Vertex v = new Vertex(vint);
			V.add(v);
			mapToVertex.put(vint, v);
		}
		// initialize vertices proposed so far
		Set<Vertex> X = new HashSet<Vertex>();
		int s = G.getVertices().get(0); // pick arbitrary first vertex 
		X.add(mapToVertex.get(s));
		V.remove(mapToVertex.get(s));
		// initialize minimum cost spanning tree
		List<WeightedEdge> mst = new ArrayList<WeightedEdge>();
		// initialize in V, key[v] = cheapest edge (u,v)
		List<WeightedEdge> edges_s = G.getOutgoingEdges(s);
		for (WeightedEdge e : edges_s) {
			Vertex v = mapToVertex.get(e.getToVertex());
			if (e.getWeight() < v.getCheapestEdgeWeight()) {
				v.setCheapestEdge(e);
			}
		}
		// initialize VX
		Queue<Vertex> VX = new PriorityQueue<Vertex>(V.size());
		VX.addAll(V);
		// MAIN LOOP
		while (!VX.isEmpty()) {
			Vertex curr = VX.remove();
			// add edge to the mst
			mst.add(curr.getCheapestEdge());
			// add curr to X
			X.add(curr);
			for (WeightedEdge e : G.getOutgoingEdges(curr.getId())) {
				Vertex w = mapToVertex.get(e.getToVertex());
				if (VX.contains(w)) {
					WeightedEdge c_vw = getEdge(G, curr, w);
					double cheapestEdgeWeight = w.getCheapestEdgeWeight();
					if (c_vw.getWeight() < cheapestEdgeWeight) {
						VX.remove(w);
						w.setCheapestEdge(c_vw);
						VX.add(w);
					}
				}
			}
			
		}
		return mst;
	}
	
	private static WeightedEdge getEdge(WeightedGraph G, Vertex v, Vertex w) {
		List<WeightedEdge> edgesOfV = G.getOutgoingEdges(v.getId());
		for (WeightedEdge e : edgesOfV) {
			if (e.getToVertex() == w.getId()) return e;
		}
		return null;
	}
	
	public static double getOverallCost(List<WeightedEdge> mst) {
		double costSum = 0;
		for (WeightedEdge e : mst) {
			costSum += e.getWeight();
		}
		return costSum;
	}
}

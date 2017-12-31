import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class PrimMST {

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
					double weight = Double.parseDouble(split[2]);
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
	
	public static List<WeightedEdge> getMST(WeightedGraph G) {
		// INITIALIZATION
		// initialize vertices proposed so far
		Set<Integer> X = new HashSet<Integer>();
		X.add(G.getVertices().get(0)); // assuming graph has a vertex
		// initialize minimum cost spanning tree
		List<WeightedEdge> mst = new ArrayList<WeightedEdge>();
		
		// MAIN LOOP
		List<Integer> V = G.getVertices();
		while (XequalsV(X, V) == false) {
			// get all edges crossing from X to V-X
			List<WeightedEdge> crosses = new ArrayList<WeightedEdge>();
			for (Integer u : X) {
				List<WeightedEdge> edges = G.getOutgoingEdges(u);
				for (WeightedEdge e : edges) {
					int w = e.getToVertex();
					if (!X.contains(w)) {
						crosses.add(e);
					}
				}
			}
			// pick cheapest edge e = (u, w) of G w/ u in X and w not in X
			double cost = Double.MAX_VALUE;
			WeightedEdge cheapest = null;
			for (WeightedEdge e : crosses) {
				double e_cost = e.getWeight();
				if (e_cost < cost) {
					cost = e_cost;
					cheapest = e;
				}
			}
			mst.add(cheapest);
			X.add(cheapest.getToVertex());
		}
		return mst;
	}
	
	private static boolean XequalsV(Set<Integer> X, List<Integer> V) {
		for (Integer v : V) {
			if (!X.contains(v)) return false;
		}
		return true;
	}
	
	public static double getOverallCost(List<WeightedEdge> mst) {
		double costSum = 0;
		for (WeightedEdge e : mst) {
			costSum += e.getWeight();
		}
		return costSum;
	}

}

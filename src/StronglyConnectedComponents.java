import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class StronglyConnectedComponents {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("ERROR: One argument, the input file name, is required.");
			return;
		}
		String inputFileName = args[0];
		String outputFileName = "output.txt";
		GenericDiGraph<SimpleEdge> G;
		try {
			// read input file
			Scanner scanner = new Scanner(new File(inputFileName));
			if (scanner.hasNextLine()) {
				String graphType = scanner.nextLine();
				if (graphType.equals("directed")) {
					G = new DiGraph();
				}
				else if (graphType.equals("undirected")) {
					System.out.println("ERROR: Strongly connected componenets applies only to directed graphs.");
					scanner.close();
					return;
				}
				else {
					System.out.println("ERROR: First line of input file must state if graph is either directed or undirected.");
					System.out.println("NOTE: Strongly connected componenets applies only to directed graphs.");
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
					int vertex1 = Integer.parseInt(split[0]);
					int vertex2 = Integer.parseInt(split[1]);
					G.addEdge(new SimpleEdge(vertex1, vertex2));
				}
				else {
					System.out.println("ERROR: Each line is either one vertex integer or an edge that consists of two vertex integers separated by a space");
					System.out.println("NOTE: Strongly connected components applies only to unweighted graphs.");
					scanner.close();
					return;
				}
			}
			scanner.close();
			
			// find strongly connected components
			List<List<Integer>> sccs = getSCCs(G);
			
			// write to output file
			File file = new File(outputFileName);
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			for (List<Integer> scc : sccs) {
				for (Integer vertex : scc) {
					bw.write(vertex + ", ");
				}
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
	
	public static List<List<Integer>> getSCCs(GenericDiGraph<SimpleEdge> G) {
		// part 1: run DFS, get finished times
		Stack<Integer> vertices = new Stack<Integer>();
		vertices.addAll(G.getVertices());
		Map<Integer, Integer> leaderMap = new HashMap<Integer, Integer>();
		Stack<Integer> finished = DFS_Loop(G, false, vertices, leaderMap);
		
		// part 2/3: runs DFS using finished times, get leaders
		DFS_Loop(G, true, finished, leaderMap);
		
		// part 4: post-processing - get sccs
		List<List<Integer>> sccs = getSCCsFromLeaderMap(leaderMap);
		return sccs;
	}
	

	private static Stack<Integer> DFS_Loop(GenericDiGraph<SimpleEdge> G, 
								   boolean forward,
						 		   Stack<Integer> vertices,
						           Map<Integer, Integer> leaderMap) {
		int leader;
		Set<Integer> explored = new HashSet<Integer>();
		Stack<Integer> finished = new Stack<Integer>();
		while (!vertices.isEmpty()) {
			int v = vertices.pop();
			if (!explored.contains(v)) {
				leader = v;
				DFS(G, v, leader, forward, explored, finished, leaderMap);
			}
		}
		return finished;
	}
	
	private static void DFS(GenericDiGraph<SimpleEdge> G,
	        int i,
	        int leader,
	        boolean forward,
	        Set<Integer> explored,
	        Stack<Integer> finished,
	        Map<Integer, Integer> leaderMap) {
		// mark vertex i as explored and set its leader
		explored.add(i);
		leaderMap.put(i, leader);
		// get neighbors
		List<SimpleEdge> edges;
		List<Integer> neighbors = new ArrayList<Integer>();
		if (forward) {
			edges = G.getOutgoingEdges(i);
			for (SimpleEdge e : edges) {
				neighbors.add(e.getToVertex());
			}
		}
		else {
			edges = G.getIncomingEdges(i);
			for (SimpleEdge e : edges) {
				neighbors.add(e.getFromVertex());
			}
		}
		for (int j : neighbors) {
			if (!explored.contains(j)) {
				DFS(G, j, leader, forward, explored, finished, leaderMap);
			}
		}
		finished.push(i);
	}
	
	private static List<List<Integer>> getSCCsFromLeaderMap(Map<Integer, Integer> map) {
		List<List<Integer>> sccs = new ArrayList<List<Integer>>();
		Map<Integer, List<Integer>> leadersLists = new HashMap<Integer, List<Integer>>();
		for (int vertex : map.keySet()) {
			int leader = map.get(vertex); 
			if (!leadersLists.containsKey(leader)) {
				leadersLists.put(leader, new ArrayList<Integer>());
				leadersLists.get(leader).add(vertex);
			}
			else {
				leadersLists.get(leader).add(vertex);
			}
		}
		for (List<Integer> scc : leadersLists.values()) {
			sccs.add(scc);
		}
		return sccs;
	}
}

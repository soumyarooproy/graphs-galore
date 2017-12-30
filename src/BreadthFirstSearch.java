import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class BreadthFirstSearch {

	public static void main(String[] args) {
		
		String inputFileName = args[0];
		int source = Integer.parseInt(args[1]);
		int destination = Integer.parseInt(args[2]);
		String outputFileName = "output.txt";
		try {
			// read in input file
			GraphInterface<SimpleEdge> G;
			Scanner scanner = new Scanner(new File(inputFileName));
			if (scanner.hasNextLine()) {
				String graphType = scanner.nextLine();
				if (graphType.equals("directed")) {
					G = new DiGraph();
				}
				else if (graphType.equals("undirected")) {
					G = new Graph();
				}
				else {
					System.out.println("ERROR: First line of input file must state if graph is either directed or undirected.");
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
					System.out.println("ERROR: Each line is a Graph edge that consists of two vertex integers separated by a space");
					System.out.println("NOTE: BFS is applied to unweighted graphs only.");
					scanner.close();
					return;
				}
			}
			scanner.close();
			if (!G.containsVertex(source)) {
				System.out.println("ERROR: Graph does not contain source vertex " + source + ".");
				return;
			}
			if (!G.containsVertex(destination)) {
				System.out.println("ERROR: Graph does not contain destination vertex " + destination + ".");
				return;
			}
			
			// find path from source to destination
			List<Integer> path = bfs(G, source, destination);
			if (path == null) {
				System.out.println("NOTE: Path from " + source + " to " + destination + " does not exist.");
				return;
			}
			
			// write to output file
			File file = new File(outputFileName);
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			for (Integer vertex : path) {
				bw.write(vertex + ", ");
			}
			bw.newLine();
			bw.close();
			
		} catch (FileNotFoundException e){
			System.out.println("ERROR: " + inputFileName + " file not found.");
		} catch (IOException e1) {
			System.out.println("ERROR: could not write file " + outputFileName + ".");
			e1.printStackTrace();
		}
		
		/*
		// small example 1 using undirected graph
		GraphInterface<SimpleEdge> G = new DiGraph();
		G.addEdge(new SimpleEdge(1, 2));
		G.addEdge(new SimpleEdge(2, 3));
		G.addEdge(new SimpleEdge(1, 4));
		G.addEdge(new SimpleEdge(4, 3));
		G.addEdge(new SimpleEdge(4, 5));
		G.addEdge(new SimpleEdge(3, 5));
		G.addEdge(new SimpleEdge(3, 6));
		G.addEdge(new SimpleEdge(5, 6));
		G.addVertex(7);
		G.addVertex(8);
		System.out.println("Example 1: ");
		G.printGraph();
		System.out.println("\nPath from 1 to 6: ");
		System.out.println(bfs(G, 1, 6));
		*/
	}
	
	public static List<Integer> bfs(GraphInterface<SimpleEdge> G, int s, int d) {
		// create parent map defined as vertex mapped to vertex's parent
		Map<Integer, Integer> parentMap = new HashMap<Integer, Integer>();
		if (!bfsHelper(G, s, d, parentMap)) {
			return null;
		}
		// construct path using parentMap
		List<Integer> path = constructPath(s, d, parentMap);
		return path;
	}
	
	private static boolean bfsHelper(GraphInterface<SimpleEdge> G, int s, int d, Map<Integer, Integer> parentMap) {
		boolean found = false;
		// set up queue, visited set
		Queue<Integer> queue = new PriorityQueue<Integer>();
		queue.add(s);
		Set<Integer> visited = new HashSet<Integer>();
		visited.add(s);
		while (!queue.isEmpty()) {
			int curr = queue.poll();
			if (curr == d) {
				found = true;
				break;
			}
			for (SimpleEdge edge : G.getOutgoingEdges(curr)) {
				int next = edge.getToVertex();
				if (!visited.contains(next)) {
					visited.add(next);
					queue.add(next);
					parentMap.put(next, curr);
				}
			}
		}
		return found;
	}
	
	private static List<Integer> constructPath(int s, int d, Map<Integer, Integer> parentMap) {
		List<Integer> path = new ArrayList<Integer>();
		int curr = d;
		while (curr != s) {
			path.add(0,curr);
			curr = parentMap.get(curr);
		}
		path.add(0, s);
		return path;
	}

}

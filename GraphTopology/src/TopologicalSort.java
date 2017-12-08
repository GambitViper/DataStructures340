import java.io.*;
import java.util.*;

public class TopologicalSort {
	// Adjacency list representation of a directed graph
	private class VertexNode {
		private String name;
		private VertexNode nextV;
		private EdgeNode edges;
		private int indegree;

		private VertexNode(String n, VertexNode v) {
			name = n;
			nextV = v;
			edges = null;
			indegree = 0;
		}
		
		public String toString(){
			String toString = "[ " + name;
			return toString += " deg: " + indegree + " ]";
		}
	}

	private class EdgeNode {
		@SuppressWarnings("unused")
		private VertexNode vertex1;
		private VertexNode vertex2;
		private EdgeNode nextE;

		private EdgeNode(VertexNode v1, VertexNode v2, EdgeNode e) {
			vertex1 = v1;
			vertex2 = v2;
			nextE = e;
		}
	}

	private VertexNode vertices;
	private int numVertices;

	public TopologicalSort() {
		vertices = null;
		numVertices = 0;
	}

	public void addVertex(String s) {
		// PRE: the vertex list is sorted in ascending order using the name as
		// the key
		// POST: a vertex with name s has been added to the vertex list and the
		// vertex list is sorted in ascending order using the name as the key
		if(vertices == null) {
			vertices = new VertexNode(s, null);
		} else {
			VertexNode curr = vertices;
			while(curr.nextV != null && curr.name.compareTo(s) > 0) {
				curr = curr.nextV;
			}
			curr.nextV = new VertexNode(s, curr.nextV);
			numVertices++;
		}
	}

	private VertexNode findVertex(String s) {
		VertexNode look = vertices;
		while (!(look.name.equals(s)) && look != null) {
			look = look.nextV;
		}
		return look;
	}

	private VertexNode findIndegree(int deg) {
		VertexNode curr = vertices;
		while (curr != null && curr.indegree != deg) {
			curr = curr.nextV;
		}
		return curr;
	}

	public void addEdge(String n1, String n2) {
		// PRE: the vertices n1 and n2 have already been added
		// POST: the new edge (n1, n2) has been added to the n1 edge list
		VertexNode n1Vertex = findVertex(n1);
		VertexNode n2Vertex = findVertex(n2);
		//System.out.println(n1Vertex.toString() + " -> " + n2Vertex.toString());
		if (n1Vertex.edges == null) {
			n1Vertex.edges = new EdgeNode(n1Vertex, n2Vertex, null);
			n2Vertex.indegree++;
		} else {
			n1Vertex.edges.nextE = new EdgeNode(n1Vertex, n2Vertex, n1Vertex.edges.nextE);
			n2Vertex.indegree++;
		}
	}

	public String topoSort() {
		// if the graph contains a cycle return null
		// otherwise return a string containing the names of vertices separated
		// by blanks
		// in topological order
		VertexNode curr = findIndegree(0);
		String response;
		if (curr == null) {
			response = "No Topological Order";
		} else {
			response = "";
			while ((curr = findIndegree(0)) != null) {
				curr.indegree--;
				numVertices--;
				response += curr.name + " ";
				//System.out.println(response);
				EdgeNode edge = curr.edges;
				while (edge != null) {
					edge.vertex2.indegree--;
					edge = edge.nextE;
				}
			}
			if (numVertices > 0) {
				response = "No Topological Order";
			}
		}
		return response;
	}

	public static void main(String[] args) {
		try {
			File graphText = new File(args[0]);
			Scanner scanner = new Scanner(graphText);
			TopologicalSort graph = new TopologicalSort();
			String[] nodes = scanner.nextLine().split(" ");
			for (String str : nodes) {
				graph.addVertex(str);
			}

			while (scanner.hasNext()) {
				String v1 = scanner.next();
				String v2 = scanner.next();
				graph.addEdge(v1, v2);
				//System.out.println("Edge: ( " + v1 + ", " + v2 + " )");
			}

			scanner.close();
			System.out.println(graph.topoSort());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

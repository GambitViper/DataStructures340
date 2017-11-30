import java.io.*;
import java.util.*;

public class TopologicalSort {
//Adjacency list representation of a directed graph
	private class VertexNode{
		private String name;
		private VertexNode nextV;
		private EdgeNode edges;
		private int indegree;
		
		private VertexNode(String n, VertexNode v){
			name = n;
			nextV = v;
			edges = null;
			indegree = 0;
		}
		
		private EdgeNode findLastEdge(){
			EdgeNode currE = edges;
			if(currE == null){
				return null;
			}else {
				while(currE.nextE != null){
					currE = currE.nextE;
				}
				return currE;
			}
		}
	}
	
	private class EdgeNode{
		private VertexNode vertex1;
		private VertexNode vertex2;
		private EdgeNode nextE;
		
		private EdgeNode(VertexNode v1, VertexNode v2, EdgeNode e){
			vertex1 = v1;
			vertex2 = v2;
			nextE = e;
		}
	}
	
	private VertexNode vertices;
	private int numVertices;
	
	public TopologicalSort(){
		vertices = null;
		numVertices = 0;
		Scanner scan = new Scanner(System.in);
		String vNames = scan.nextLine();
		String[] vList = vNames.split(" ");
		numVertices = vList.length;
		for(String vertex : vList){
			addVertex(vertex);
		}
		String currEdge = scan.nextLine();
		while(currEdge != null){
			addEdge(String.valueOf(currEdge.charAt(0)), String.valueOf(currEdge.charAt(1)));
			currEdge = scan.nextLine();
		}
		scan.close();
	}
	
	public void addVertex(String s){
		//PRE: the vertex list is sorted in ascending order using the name as the key
		//POST: a vertex with name s has been added to the vertex list and the
		//      vertex list is sorted in ascending order using the name as the key
		if(vertices == null){
			vertices = new VertexNode(s, null);
		}else {
			VertexNode curr = vertices;
			while(curr.nextV != null){
				curr = curr.nextV;
			}
			curr.nextV = new VertexNode(s, null);
		}
	}
	
	private VertexNode findVertex(String s){
		VertexNode look = vertices;
		while(look.name != s || look.nextV != null){
			look = look.nextV;
		}
		if(look.name == s){
			return look;
		}else {
			return null; //VertexNode not found
		}
	}
	
	public void addEdge(String n1, String n2){
		//PRE: the vertices n1 and n2 have already been added
		//POST: the new edge (n1, n2) has been added to the n1 edge list
		VertexNode n1Vertex = findVertex(n1);
		VertexNode n2Vertex = findVertex(n2);
		EdgeNode addEdgePos = n1Vertex.findLastEdge();
		addEdgePos.nextE = new EdgeNode(n1Vertex, n2Vertex, null);
	}
	
	public String topoSort(){
		//if the graph contains a cycle return null
		//otherwise return a string containing the names of vertices seperated by blanks
		//in topological order
		return null;
	}
	
	public static void main(String[] args) {
		new TopologicalSort();
	}

}

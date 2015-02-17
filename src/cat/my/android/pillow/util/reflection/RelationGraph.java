package cat.my.android.pillow.util.reflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cat.my.util.exceptions.BreakFastException;


/**
 * Creates a graph with the relations between classes.
 * 
 * Used to obtain an ordered list on how to synchonize classes (which order)
 */
public class RelationGraph {
	Graph<Class<?>> graph = new Graph<Class<?>>();
	List<Class<?>> synchOrder;
	
	public void addClass(Class<?> modelClass){
		List<Class<?>> relations = ReflectionUtil.getBelongsToClasses(modelClass);
		graph.add(modelClass);
		for(Class<?> relation: relations){
			graph.addEdge(modelClass, relation);
		}
	}
	
	public List<Class<?>> getSynchOrder(){
		if(synchOrder==null){
			synchOrder = graph.getLinearOrder();
			if(synchOrder==null){
				throw new BreakFastException("Cycle found in model classes. Create soft links.");
			}
		}
		return synchOrder;
	}

	
	/**
	 * Set public for testing
	 *  @param <T>
	 */
	public static class Graph<T>{
		private static class Node<T>{
			T value;
			List<T> incommingEdges;
			List<T> outGoingEdges;
			public Node(T value){
				this.value = value;
				incommingEdges = new ArrayList<T>();
				outGoingEdges  = new ArrayList<T>();
			}
			public Node(Node<T> node){
				this.value = node.value;
				incommingEdges = new ArrayList<T>(node.incommingEdges);
				outGoingEdges  = new ArrayList<T>(node.outGoingEdges);
			}
		}
		
		Map<T, Node<T>> nodes = new HashMap<T, Node<T>>();
		
		public Graph(){}
		
		public Graph(Graph<T> graph){
			//Clone graph
			for(Node<T> node : graph.nodes.values()){
				nodes.put(node.value, new Node<T>(node));
			}
		}
		
		public void remove(T value){
			Node<T> node = nodes.get(value);
			remove(node);
		}
		
		public void remove(Node<T> node){
			nodes.remove(node.value);
			for(T edge : node.outGoingEdges){
				nodes.get(edge).incommingEdges.remove(node.value);
			}
			for(T edge : node.incommingEdges){
				nodes.get(edge).outGoingEdges.remove(node.value);
			}
		}
		
		public void add(T value){
			nodes.put(value, new Node(value));
		}
		
		
		public void addEdge(T value, T edgeValue){
			nodes.get(value).outGoingEdges.add(edgeValue);
		}
		
		public void createIncommingEdges(){
			for(Node<T> node: nodes.values()){
				for(T edge : node.outGoingEdges){
					nodes.get(edge).incommingEdges.add(node.value);
				}
			}
		}
		
		private Node<T> popNoIncommingEdgeNode() {
			for(Node<T> node: nodes.values()){
				if(node.incommingEdges.isEmpty()){
					remove(node);
					return node;
				}
			}
			return null;
		}


		/**
		 * List of nodes in an ordered way according to edges. If cycle founds (not possible to order) it will return null.
		 */
		public List<T> getLinearOrder(){
			createIncommingEdges();
			List<T> result = new ArrayList<T>();
			Graph<T> clonedGraph = new Graph<T>(this);
			
			for(Node<T> node = clonedGraph.popNoIncommingEdgeNode(); node!=null; node = clonedGraph.popNoIncommingEdgeNode()){
				result.add(node.value);
			}
			if(!clonedGraph.nodes.isEmpty()){
				return null; //Cycle graph!
			}
			return result;
		}
	}
	
//	public boolean isCyclic(){
//	Set<Node> visited = new HashSet<RelationGraph.Node>();
//	Set<Node> recStack = new HashSet<RelationGraph.Node>();
//	for(Node node: nodes.values()){
//		if(isCyclic(node, visited, recStack)){
//			return true;
//		}
//	}
//	return false;
//}
//
//private boolean isCyclic(Node node, Set<Node> visited, Set<Node> recStack){
//	if(! visited.contains(node)){
//		visited.add(node);
//		recStack.add(node);
//	
//		for(Class<?> edge : node.getRelations()){
//			Node newNode = nodes.get(edge);
//			if(!visited.contains(newNode) && isCyclic(newNode, visited, recStack, order
//					))
//				return true;
//			if (recStack.contains(node))
//				return true;
//		}
//	}
//	recStack.remove(node);
//	return false;
//}
	
}

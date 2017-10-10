/**
 * Implementation of a PriorityQueue Data Structure using a D-Heap
 * @author Zach Baklund
 * Last Updated: 13-9-17
 *
 */
import java.util.*;

public class PriorityQueue {
	//implements a d-heap based priority queue
	//the priority is an int (low value high priority)
	//associated with each priority is an object

	private class Item{
		private int priority;
		private Object data;

		private Item(int p, Object d){
			priority = p;
			data = d;
		}
	}

	private Item queue[];
	private int order;//D value of the D heap
	private int size;

	public PriorityQueue(int ord, int s){
		queue = new Item[s];
		order = ord;
		size = 0;
	}

	public int getPriority(){
		//PRE !empty()
		//Return the highest priority value in the queue
		return queue[0].priority;
	}

	public Object getData(){
		//PRE !empty()
		//Return the data object associated with the highest priority
		return queue[0].data;
	}

	public void remove(){
		//PRE !empty()
		//Remove the item with the highest priority in the queue
		size--;
		Item x = queue[size];
		int child = 1;
		while (child <= size){
			int startChild = child;
			int endChild = (((child - 1) / order) * order + order);
			child = findSmallestChild(startChild, endChild);
			if (x.priority < queue[child].priority){
				break;
			}
			queue[(child - 1) / order] = queue[child];
			child = child * order + 1;
		}
		queue[(child - 1) / order] = x;
	}

	public int getSize(){
		//Return the number of items in the queue
		return size;
	}

	public boolean full(){
		return size == queue.length - 1;
	}

	public boolean empty(){
		return size == 0;
	}

	public void insert(int p, Object d){
		//PRE !full()
		//Insert a new item into the queue with priority p and associated data d
		Item toInsert = new Item(p, d);
		size++;
		int child = size - 1;
		int parent = (child - 1) / order;
		while (child > 0 && p < queue[parent].priority){
			queue[child] = queue[parent];
			child = parent;
			parent = (child - 1) / order;
		}
		queue[child] = toInsert;
	}

	/**
	 * Takes an array of children and returns the index of highest priority
	 * @param queue of 'D' children of the parent node
	 * @return index of smallest child in the array/heap
	 */
	private int findSmallestChild(int start, int end){
		int smallest = start;
		for (int i = start + 1; i < end && i < size; ++i){
			if(queue[i].priority < queue[smallest].priority){
				smallest = i;
			}
		}
		return smallest;
	}

	public static void main(String[] args) {
		PriorityQueue p1 = new PriorityQueue(5, 100);
		PriorityQueue p2 = new PriorityQueue(6, 100);
		PriorityQueue p3 = new PriorityQueue(7, 100);

		int p = -1; //pointless initialization to keep the compiler happy
		p1.insert(0, new Integer(0));
		System.out.println("First insert");

		for (int i = 1; i < 100; i++)
			p1.insert(i, new Integer(i));

		for (int i = 0; i < 100; i++)
			p2.insert(i, new Integer(i));

		for (int i = 0; i < 100; i++)
			p3.insert(i, new Integer(i));

		System.out.println("First insert tests");

		System.out.print(p1.getPriority()+",");
		while (!p1.empty()) {
			p = p1.getPriority();
			Object d = p1.getData();
			p1.remove();
		}
		System.out.println(p);

		System.out.print(p2.getPriority()+",");

		while (!p2.empty()) {
			p = p2.getPriority();
			Object d = p2.getData();
			p2.remove();
		}
		System.out.println(p);

		System.out.print(p3.getPriority()+",");

		while (!p3.empty()) {
			p = p3.getPriority();
			Object d = p3.getData();
			p3.remove();
		}
		System.out.println(p);
		System.out.println("First Remove Test");

		for (int i = 100; i > 0 ; i--)
			p1.insert(i, new Integer(i));

		for (int i = 100; i > 0 ; i--)
			p2.insert(i, new Integer(i));

		for (int i = 100; i > 0 ; i--)
			p3.insert(i, new Integer(i));

		System.out.println("Second insert tests");

		System.out.print(p1.getPriority()+",");
		while (!p1.empty()) {
			p = p1.getPriority();
			Object d = p1.getData();
			p1.remove();
		}
		System.out.println(p);

		System.out.print(p2.getPriority()+",");

		while (!p2.empty()) {
			p = p2.getPriority();
			Object d = p2.getData();
			p2.remove();
		}
		System.out.println(p);

		System.out.print(p3.getPriority()+",");

		while (!p3.empty()) {
			p = p3.getPriority();
			Object d = p3.getData();
			p3.remove();
		}
		System.out.println(p);
		System.out.println("Second Remove Test");

		Random r1 = new Random(1000);
		while (!p3.full()) {
			p = r1.nextInt(200);
			System.out.print(p+",");
			p3.insert(p, new Integer(p));
		}
		System.out.println();

		while (!p3.empty()) {
			System.out.print(p3.getPriority()+",");
			Object d = p3.getData();
			p3.remove();
		}
		System.out.println();
		System.out.println("Third Remove Test");
	}

	/**
	 * Testing method used to evaluate accuracy of the Priority Queue Implementation
	 */
	private void viewQueue(){
		for(int i = 0; i < size; ++i){
			System.out.printf("%2d | ", queue[i].priority);
		}
		System.out.println();
		for(int i = 0; i < size; ++i){
			System.out.printf("%2d | ", i);
		}
	}

}

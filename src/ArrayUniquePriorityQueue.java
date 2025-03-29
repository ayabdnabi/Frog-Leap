/**
 * This class is uses 2 arrays (queue of type T) in order to store data items and (priority) of type double to store
 * the priority that corresponds to the data items.
 * 
 * @author Aya Abdul Nabi 
 * @param <T> type of elements that are stored in the priority queue
 */


public class ArrayUniquePriorityQueue<T> implements UniquePriorityQueueADT<T> {
	private T[] queue;  // Stores data items
	private double[] priority;  // Stores priorities for the corresponding data item
	private int count;  // Number of elements in queue
	
	/**
	 * The constructor initialises both arrays with a capacity of 10. 
	 */
	public ArrayUniquePriorityQueue() {
		queue = (T[]) new Object[10];
		priority = new double[10];
		count = 0;
		
	}
	/**
	 * This method adds the given data in the queue and its corresponding priority in "priority"
	 * and is ordered from lowest to highest
	 * 
	 * @param data, data item to be added to the queue
	 * @param prio, priority of the data item
	 * 
	 */
	@Override
	public void add(T data, double prio) {
		for (int i = 0; i < count; i++) {
			// If the data item is already in queue, ends the method immediately
			if (queue[i].equals(data)) {
				return;
			}
		}
		// If both arrays are full, expand the capacity by 5
		if (count == queue.length) {
			int newCapacity = queue.length + 5;
			T[] updatedQueue = (T[]) new Object[newCapacity];
			double[] updatedPriority = new double[newCapacity];
			
			// Copies existing elements into the array
			for (int i = 0; i < count; i++) {
				updatedQueue[i] = queue[i];
				updatedPriority[i] = priority[i];
			}
			queue = updatedQueue;
			priority = updatedPriority;
		}
		// Find the correct index in order to maintain proper ordering 
		int index = 0;
		while (index < count && prio >= priority[index]) {
			index++;
		}
		
		// Shift elements to make space for the new added item
		for (int i = count - 1; i >= index; i--) {
			priority[i+1] = priority[i];
			queue[i+1] = queue[i];
		}
		
		// Insert data item with its corresponding priority 
		queue[index] = data;
		priority[index] = prio;
		
		count++;
    }
	
	/**
	 * Checks if the data item is in the queue. If it is, return true, else, return false
	 */
	@Override
	public boolean contains(T data) {
        for (int i = 0; i < count; i++) {
        	if (queue[i].equals(data)) {
        		return true;
        	}
        }
        return false;
    }
	
	/**
	 * Returns the data item with the smallest priority without removing it
	 */
	@Override
	public T peek() throws CollectionException {
		// If the queue is empty, throw an exception 
		if (isEmpty()) {
			throw new CollectionException("PQ is empty");
		}
		return queue[0];
	}
	
	/**
	 * This method removes the item with the smallest priority
	 */
	@Override
	public T removeMin() throws CollectionException {
		// If the queue is empty, throw an exception
		if (isEmpty()) {
			throw new CollectionException("PQ is empty");
		}
		/*
		 * If not, remove the item with the smallest priority and shift the values from both arrays
		 * to the left 
		 */
		T result = queue[0];
		count--;
		for (int i = 0; i < count; i++) {
			queue[i] = queue[i+1];
			priority[i] = priority[i+1];
		}
		queue[count] = null;
		return result;
		
		
	}
	
	/*
	 * This method updates the priority of an existing data item in the queue
	 */
	@Override
	public void updatePriority(T data, double newPrio) throws CollectionException {
		// If the data item is not in queue, throw an exception
		if (!contains(data)) {
			throw new CollectionException("Item not found in PQ");
		}
		
		// Find the given data item you want to update and save its index
		int updateIndex = 0;
		for (int i = 0; i < count; i ++) {
			if (queue[i].equals(data)) {
				updateIndex = i;
			}
		}
		
		/*
		 * Updates the priority of the given data and makes sure that the data is still in
		 * increasing order even after the data and priority was updated
		 */
		T dataUpdate = queue[updateIndex];
		double prioUpdate = priority[updateIndex];
		for (int i = updateIndex; i < count - 1; i++) {
				priority[i] = priority[i + 1];	
				queue[i] = queue[i + 1];
		}
		count--;
		add(data, newPrio);
	}
	/*
	 * This method checks if the queue is empty or not
	 */
	@Override
	public boolean isEmpty() {
		if (count == 0) {
			return true;	
		}
		return false;
	}
	/*
	 * This method returns the number of items stored in the priority queue
	 */
	@Override
	public int size() {
		return count;
	}
	/*
	 * This method returns the capacity of the arrays
	 */
	public int getLength() {
		return priority.length;
	}
	
	/*
	 * This method creates a string that contains each data item in the queue followed by its\
	 * corresponding priority
	 */
	public String toString() {
		// If the queue is empty, return the following message
		if (isEmpty()) {
			return "The PQ is empty";
		}
		/*
		 * If it's not, return a string containing the data item along with its corresponding priority
		 * between brackets.
		 */
		String result = new String();
		for (int i = 0; i < count; i++) {
			result = result + queue[i] + " [" + priority[i] + "]";
			if (i < count - 1) {
				result = result + ", ";
			}
		}
		return result;
	}

}

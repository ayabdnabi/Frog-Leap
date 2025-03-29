/**
 * This class is used to determine the path that Kermit should follow to go from the starting lilypad to the
 * end to reach Denise
 * 
 * @author Aya Abdul Nabi
 * 
 */


public class FrogPath {
    private Pond pond;  // The pond the frog moves along

    
    /**
     * Constructor, initialises the pond based on the provided file
     * @param filename name of the file which has the map
     */
    public FrogPath(String filename) {
        try {
            pond = new Pond(filename);
        } catch (Exception e) {
            System.out.println("File not found");
        }
    }
    
    /**
     * This method finds the best path for Kermit to take
     * @param currCell current position of Kermit
     * @return The best cell for Kermit to take
     */
    public Hexagon findBest(Hexagon currCell) {
    	ArrayUniquePriorityQueue<Hexagon> priorityQueue = new ArrayUniquePriorityQueue<>();  // Used to store unmarked cells Kermit can reach from currCell
    	// Scan all the cells surrounding him
    	for (int i = 0; i < 6; i++) {
    		Hexagon next = currCell.getNeighbour(i); // next cells are the cells around him
    		if (next != null && !next.isMarked() && !next.isAlligator() && !next.isMudCell()) {  // Restricted cells that Kermit cannot go on
    			// Prevents him from going to cells even adjacent to alligators
    			if (!alligatorNeighbour(next)) {
    				double prio = getPriority(currCell, next);  // Get the priority of the adjacent cells and add them to the priority queue
    				priorityQueue.add(next, prio);
    			}
    		}
    	}
    	// If the current cell he's on is a lilypad, check the adjacent cells and the cells 2 far away from him
    	if (currCell.isLilyPadCell()) {
    		for (int i = 0; i < 6; i++) {
    			Hexagon next = currCell.getNeighbour(i);
    			if (next != null) {
    				for (int j = 0; j < 6; j++) {
    					Hexagon twoNext = next.getNeighbour(j);
    					if (twoNext != null && !twoNext.isMarked() && !twoNext.isAlligator() && !twoNext.isMudCell() && !priorityQueue.contains(twoNext)) {  // Restricted cells he still can't go on 
    						// Prevents him on going on cells that are adjacent to alligators even 2 far from him
    						if (!alligatorNeighbour(twoNext)) {
    							double prio = getPriority(currCell, twoNext);  // Get the priority of the cells
    							if (((i % 2 == 0) && (j % 2 == 0)) || ((i % 2 != 0) && (j % 2 != 0))) {  // If they are in a straight line and 2 far away from him, increase priority by 0.5
    								prio += 0.5;
    							}
    							else if (((i % 2 == 0) && (j % 2 != 0)) || ((i % 2 != 0) && (j % 2 == 0))) {  // If they AREN'T in a straight line and 2 far away from him, increase priority by 1.0
    								prio += 1.0;
    							}
    							priorityQueue.add(twoNext, prio);  // Add to priority queue		
    						}
    					}
    				}
    			}
    		}
    	}
    	// If queue isn't empty, return the first element without removing, else, return null
    	if (!priorityQueue.isEmpty()) {
    		return priorityQueue.peek();
    	}
    	return null;
    }
    
    /**
     * Method used to check if there is an alligator adjacent to the given hexagon
     * @param hexagon the cell used to check for alligator neighbours
     * @return  True if there is an alligator near the hexagon, false otherwise
     */
    private boolean alligatorNeighbour(Hexagon hexagon) {
        for (int i = 0; i < 6; i++) {
            Hexagon neighbour = hexagon.getNeighbour(i);
            if (neighbour != null && neighbour.isAlligator()) {
                return true;
            }
        }
        return false;
    }
   
   /**
    *  Method used to get the priority of the current cell and neighbour cell
    * @param current  the current cell he's on
    * @param neighbour  neighbouring cell
    * @return  the priority for the next cell 
    */
    private double getPriority(Hexagon current, Hexagon neighbour) {
        double prio = 0.0;
        
        if (neighbour.isEnd()) {
            prio = 3.0;  // If neighbour is the end cell, prio is 3.0
        } else if (neighbour.isLilyPadCell()) {
            prio = 4.0;  // If neighbour is a lilypad, prio is 4.0
        } else if (neighbour.isReedsCell()) {
            prio = 5.0;  // If neighbour is a reed cell, prio is 5.0
        } else if (neighbour.isWaterCell()) {
            prio = 6.0;  // If neighbour is a water cell, prio is 6.0
        } else if (neighbour.isAlligator() && current.isReedsCell()) {
            prio = 10.0;  // Reeds near an alligator has a priority of 10.0
        } else if (neighbour instanceof FoodHexagon) {
            int numFlies = ((FoodHexagon) neighbour).getNumFlies();
            if (numFlies == 3) {
                prio = 0.0;  // If there are 3 flies, prio is 0.0
            } else if (numFlies == 2) {
                prio = 1.0;  // If there are 2 flies, prio is 1.0
            } else if (numFlies == 1) {
                prio = 2.0;  // If there is 1 flies, prio is 2.0
            }
        }
        return prio;
    }


    /**
     * Method containing the ID of every cell that Kermit visits and the amount of flies he ate
     * @return  Path that Kermit followed
     */
    public String findPath() {
    	ArrayStack<Hexagon> stack = new ArrayStack<>();  // Stack to keep track of visited cells and marks them as visited
    	Hexagon startingCell = pond.getStart();  // Starting ecll of the pond
    	stack.push(startingCell);
    	startingCell.markInStack();  // Push the start in the stack and mark as visited
    	
    	int fliesEaten = 0;
    	
    	String path = "";
    	while (!stack.isEmpty()) {
    		Hexagon curr = stack.peek();  
    		path += curr.getID() + " ";  // While the stack is empty, get the first element in the stack, get the ID of it, and add it to the string
    		if (curr.isEnd()) {
    			break;  // If Kermit reaches the end, break the loop
    		}
    		
    		// Keep track of the number of flies Kermit eats along his way
    		if (curr instanceof FoodHexagon) {
    			int numFiles = ((FoodHexagon) curr).getNumFlies();
    			fliesEaten += numFiles;
    			((FoodHexagon) curr).removeFlies();  
    		}
    		
    		Hexagon next = findBest(curr);  // Find the next best cell to move to
    		// If there isn't a next valid trail, start backtracking 
    		if (next == null) {
    			stack.pop();
    			curr.markOutStack();
    		} else {
    			stack.push(next);
    			next.markInStack();  // Otherwise, push the next cell into stack and mark as visited
    		}
    	}
    	if (stack.isEmpty()) {
    		path = "No solution";  // If the stack is empty, there is no solution
    	} else {
    		path += "ate " + fliesEaten + " flies";  // Otherwise, add how many flies he ate along the way into path
    	}
    	return path;
    }
}
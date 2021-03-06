/**
    A class of bags whose entries are stored in a chain of linked nodes.
    The bag is never full.
    INCOMPLETE DEFINITION; includes definitions for the methods add,
    toArray, isEmpty, and getCurrentSize.
    @author Frank M. Carrano, Timothy M. Henry
    @version 5.0
*/
public final class LinkedBag<T> implements BagInterface<T>
{
	private Node firstNode;       // Reference to first node
	private int numberOfEntries;

	public LinkedBag()
	{
		firstNode = null;
      numberOfEntries = 0;
	} // end default constructor

	/** Adds a new entry to this bag.
	    @param newEntry  The object to be added as a new entry.
	    @return  True. */
	public boolean add(T newEntry) // OutOfMemoryError possible
	{
      // Add to beginning of chain:
		Node newNode = new Node(newEntry);
		newNode.next = firstNode;  // Make new node reference rest of chain
                                 // (firstNode is null if chain is empty)
      firstNode = newNode;       // New node is at beginning of chain
		numberOfEntries++;
      
		return true;
	} // end add

	/** Retrieves all entries that are in this bag.
       @return  A newly allocated array of all the entries in this bag. */
	public T[] toArray()
	{
      // The cast is safe because the new array contains null entries.
      @SuppressWarnings("unchecked")
      T[] result = (T[])new Object[numberOfEntries]; // Unchecked cast
      
      int index = 0;
      Node currentNode = firstNode;
      while ((index < numberOfEntries) && (currentNode != null))
      {
         result[index] = currentNode.data;
         index++;
         currentNode = currentNode.next;
      } // end while
      
      return result;
      // Note: The body of this method could consist of one return statement,
      // if you call Arrays.copyOf
	} // end toArray
   
	/** Sees whether this bag is empty.
       @return  True if the bag is empty, or false if not. */
	public boolean isEmpty()
	{
		return numberOfEntries == 0;
	} // end isEmpty
   
	/** Gets the number of entries currently in this bag.
       @return  The integer number of entries currently in the bag. */
	public int getCurrentSize()
	{
		return numberOfEntries;
	} // end getCurrentSize
   
	/** Removes one unspecified entry from this bag, if possible.
       @return  Either the removed entry, if the removal
                was successful, or null. */
	public T remove()
   {
	   if(firstNode!=null){
		    T data=firstNode.data;
      		firstNode=firstNode.next;
			numberOfEntries--;
	  		return data;
		}
		return null;
   } // end remove
   
	/** Removes one occurrence of a given entry from this bag.
       @param anEntry  The entry to be removed.
       @return  True if the removal was successful, or false otherwise. */
   public boolean remove(T anEntry)
   {
	   Node current=firstNode;
	   if(current==null)
	   		return false;
	   do{
		   if(current.data.equals(anEntry)){
				Node temp=current;
				current=firstNode;
				firstNode=temp;
				firstNode=null;
				numberOfEntries--;
			    return true;
		   }
		   current=current.next;
	   }while(current.next!=null);
      return false;
   } // end remove
	
	/** Removes all entries from this bag. */
	public void clear()
   {
	   firstNode=null;
	   numberOfEntries=0;
   } // end clear
	
	/** Counts the number of times a given entry appears in this bag.
		 @param anEntry  The entry to be counted.
		 @return  The number of times anEntry appears in the bag. */
	public int getFrequencyOf(T anEntry)
   {   
	   int count = 0;
	   if(firstNode!=null){
		   Node current=firstNode;
		   do{
				if(anEntry.equals(current.data))
					count++;
				current=current.next;
		   } while(current.next!=null);
	   }
      return count;
   } // end getFrequencyOf
	
	/** Tests whether this bag contains a given entry.
		 @param anEntry  The entry to locate.
		 @return  True if the bag contains anEntry, or false otherwise. */
	public boolean contains(T anEntry)
   {
	   if(firstNode!=null){
		   Node current=firstNode;
		   do{
				if(anEntry.equals(current.data))
					return true;
				current=current.next;
		   }while(current.next!=null);
	   }
      return false; // STUB
   } // end contains

    /** Returns a new bag that contains both elements of the bag, including duplicates.
     *  Does not affect the contents of the bags used.
    	@param bag1 The bag you want to union with.
    	@return  The union of both bags as a new bag. Order does not matter and duplicates are allowed. */
	public Object union(Object bag1) {
		@SuppressWarnings("unchecked")
		LinkedBag<T> otherBag = (LinkedBag<T>)bag1;
		if (firstNode==null || otherBag.isEmpty())
			return new LinkedBag<T>();
		LinkedBag<T> unifiedBag = new LinkedBag<T>();
		T[] items = this.toArray();
		T[] items2 = otherBag.toArray();
		for (int i = 0; i < items.length; i++) {
			unifiedBag.add(items[i]);
			unifiedBag.add(items2[i]);
		}
		return unifiedBag;
	}

	/** Returns a new bag that contains elements that only appear in both bags. Duplicate items are counted
      * if both bags contain that duplicate item.
      * Does not affect the contents of the bags used.
        @param bag1  The bag you want to intersect with
        @return  The intersection of both bags as a new bag. */
	public Object intersection(Object bag1) {
		@SuppressWarnings("unchecked")
		LinkedBag<T> otherBag = (LinkedBag<T>)bag1;
		if(firstNode==null || otherBag.isEmpty())
			return new LinkedBag<T>();
		LinkedBag<T> intersectionBag = new LinkedBag<T>();
		T[] items = this.toArray();
		boolean[] appears = new boolean[items.length];
		for (int i = 0; i < items.length; i++) {
			appears[i]=otherBag.contains(items[i]);
		}
		for (int i = 0; i < appears.length; i++) {
			if (appears[i]){
				int frequencyThis = this.getFrequencyOf(items[i]);
				int frequencyOther = otherBag.getFrequencyOf(items[i]);
				if(frequencyThis<=frequencyOther){
					for(int j = 0; j < frequencyThis; j++){
						intersectionBag.add(items[i]);
					}
				}
				else{
					for(int j = 0; j < frequencyOther; j++){
						intersectionBag.add(items[i]);
					}
				}
			}
		}
		return intersectionBag;
	}

	/** Returns a new bag that contains elements in one bag after removing the elements that are found
      * in another bag. Does not affect the contents of the bags used.
        @param bag1 The bag that elements you don't want in the first bag.
        @return  The difference of both bags as a new bag. */
	public Object difference(Object bag1) {
		@SuppressWarnings("unchecked")
		LinkedBag<T> otherBag = (LinkedBag<T>) bag1;
		if (firstNode == null || otherBag.isEmpty()) {
			return new LinkedBag<T>();
		}
		LinkedBag<T> differenceBag = new LinkedBag<T>();
		T[] thisItems = this.toArray();
		T[] otherItems = otherBag.toArray();
		for (int i = 0; i < thisItems.length; i++) {
			for (int j = 0; j < otherItems.length; j++) {
				if (thisItems[i].equals(otherItems[j])) {
					otherItems[j] = null;
					break;
				} else if (j == otherItems.length -1) {
					differenceBag.add(thisItems[i]);
				}
				
			}
		}
		return differenceBag;
	}

	private class Node
	{
	  private T    data; // Entry in bag
	  private Node next; // Link to next node

		private Node(T dataPortion)
		{
			this(dataPortion, null);	
		} // end constructor
		
		private Node(T dataPortion, Node nextNode)
		{
			data = dataPortion;
			next = nextNode;	
		} // end constructor
	} // end Node
} // end LinkedBag
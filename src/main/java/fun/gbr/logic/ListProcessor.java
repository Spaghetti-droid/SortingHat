package fun.gbr.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import fun.gbr.entity.Clump;
import fun.gbr.entity.ListInfo;
import fun.gbr.parameters.Constants;
import fun.gbr.parameters.Options;

/**
 * For list analysis and manipulation
 *
 */
public class ListProcessor {
	
	/** Checks that it is possible to find an ordering of the list elements without having sequences of identical elements that are larger than MAX_REPEATS allows
	 * @param list
	 * @return true if it's possible to sort this list without violating constraints, false otherwise.
	 */
	public static boolean validateList(List<String> list) {
		
		if(Options.MAX_REPEATS == Constants.INT_NONE_VALUE) { // No constraint
			return true; 
		}
		ListInfo info = analyseList(list);		
		return info.getMinNumberOfClumps()<=info.getNumberOfOtherElements()+1;
	}
	
	/** Finds information about the list which is needed to validate it
	 * @param list
	 * @return
	 */
	private static ListInfo analyseList(List<String> list) {
		
		Map<String, Integer> elementToInstanceNumber = countElementInstances(list);
		int highestNumber = 0;
		String mostPresentElement = null;
		for(Entry<String, Integer> entry: elementToInstanceNumber.entrySet()) {
			Integer instances = entry.getValue();
			if (instances > highestNumber) {
				mostPresentElement = entry.getKey();
				highestNumber = instances.intValue();
			}
		}
		
		int numberOfOtherElements = list.size() - highestNumber;
		int minNumberOfClumps = highestNumber/Options.MAX_REPEATS;
		
		return new ListInfo(highestNumber, numberOfOtherElements, mostPresentElement, minNumberOfClumps);
		
	}
	
	/** Counts the number of times an element appears in a list
	 * @param list
	 * @return A map of list elements to number of occurrences
	 */
	private static Map<String, Integer> countElementInstances(List<String> list){
		
		Map<String, Integer> elementToInstanceNumber = new HashMap<>();
		
		for(String element: list) {
			Integer instances = elementToInstanceNumber.get(element);
			if(instances == null) {
				instances = 1;
			} else {
				instances++;
			}
			
			elementToInstanceNumber.put(element, instances);			
		}
		
		return elementToInstanceNumber;		
	}
	
	/** Shuffles the list. Will only output lists that respect the constraints set in Options.
	 * @param list
	 */
	public static void shuffle(List<String> list){
		
		// Shuffle list
		
		Collections.shuffle(list);
		
		if(Constants.INT_NONE_VALUE == Options.MAX_REPEATS) {
			return;
		}
		
		// Find clumps
		
		List<Clump> clumps = getListClumps(list);
		
		// Revert order to deal with later clumps first
		
		Collections.reverse(clumps);
		
		// Remove elements from clumps that are too big
		
		List<String> illegalElements = new ArrayList<>();
		
		for(Clump clump: clumps) {			
			if(clump.isIllegal()) {
				int extraElements = clump.getSize()-Options.MAX_REPEATS;
				for(int i=0; i<extraElements; i++) {
					illegalElements.add(list.remove(clump.getLocation()));
				}
			}			
		}
		
		// Randomly put elements in locations that are available
		
		Random random = new Random();
		for(String element: illegalElements) {
			randomlyAddInLegalLocation(random, list, element);
		}
		
	}
	
	/** Adds element into a random location in list. This location has to obey the constaints set by options. If no such location is found, an IllegalArgumentException is thrown.
	 * @param random
	 * @param list
	 * @param element
	 */
	public static void randomlyAddInLegalLocation(Random random, List<String> list, String element) {
		
		if(random == null) {
			random = new Random();
		}
		
		List<Integer> allowedLocations = getLegalLocations(list, element);
		if(allowedLocations == null || allowedLocations.isEmpty()) {
			throw new IllegalArgumentException("It is impossible to add the new element to the input list without violating option constraints!");
		}
		int randomLocation = allowedLocations.get(random.nextInt(allowedLocations.size()));
		
		list.add(randomLocation, element);
	}
	
	/** Returns a list of locations in the input list where element can be inserted while respecting Options constraints
	 * @param list
	 * @param element
	 * @return A list of locations
	 */
	public static List<Integer> getLegalLocations(List<String> list, String element){
		
		List<Clump> clumps = getListClumps(list);
		List<Integer> allowedLocations = IntStream.rangeClosed(0, list.size())
			    .boxed().collect(Collectors.toList());	
		
		// Remove locations adjacent or in clumps at their max size
		
		for(Clump clump: clumps) {
			if(clump.isBorderlineOrWorse() && clump.getElement().equals(element)) {
				allowedLocations.removeAll(IntStream.rangeClosed(clump.getLocation(), clump.getLocation()+clump.getSize())
					    .boxed().collect(Collectors.toList()));	
				if(allowedLocations.isEmpty()) {
					return allowedLocations;
				}
			}
		}
		
		return allowedLocations;	
	}
	
	/** Divides the list into 'Clumps' containing only equal elements.
	 * @param list
	 * @return Info on location, size, and the element present within these clumps
	 */
	public static List<Clump> getListClumps(List<String> list){
		
		List<Clump> clumps = new ArrayList<>();
		
		if(list == null || list.isEmpty()) {
			return clumps;
		}		
		
		String lastElement = list.get(0);
		int clumpSize = 0;
		int clumpLocation = 0;
		for(int i=0; i<list.size(); i++) {
			
			String element = list.get(i);
			if(!element.equals(lastElement)) {
				
				clumps.add(new Clump(lastElement, clumpSize, clumpLocation));
				
				// reset
				
				lastElement = element;
				clumpSize = 1;
				clumpLocation = i;
				
			} else {
				
				clumpSize++;				
			}			
		}
		
		clumps.add(new Clump(lastElement, clumpSize, clumpLocation));		
		return clumps;		
	}
}

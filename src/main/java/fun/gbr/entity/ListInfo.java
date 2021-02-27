package fun.gbr.entity;

/**
 * Contains useful info about the list for ListProcessor
 *
 */
public class ListInfo {
	
	private int numberOfMostCommonElement;
	private int numberOfOtherElements;
	private String mostCommonElement;
	private int minNumberOfClumps;
	
	public ListInfo(int numberOfMostCommonElement, int numberOfOtherElements, String mostCommonElement,
			int minNumberOfClumps) {
		super();
		this.numberOfMostCommonElement = numberOfMostCommonElement;
		this.numberOfOtherElements = numberOfOtherElements;
		this.mostCommonElement = mostCommonElement;
		this.minNumberOfClumps = minNumberOfClumps;
	}

	public int getNumberOfMostCommonElement() {
		return numberOfMostCommonElement;
	}

	public int getNumberOfOtherElements() {
		return numberOfOtherElements;
	}

	public String getMostCommonElement() {
		return mostCommonElement;
	}

	public int getMinNumberOfClumps() {
		return minNumberOfClumps;
	}	

}

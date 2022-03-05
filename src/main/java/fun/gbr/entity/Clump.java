package fun.gbr.entity;

import fun.gbr.parameters.Constants;
import fun.gbr.parameters.Options;

/**
 * Describes a part of a list which is entirely made up of a sequence of identical elements
 *
 */
public class Clump {
	
	String element;
	int size;
	int location;
	
	public Clump(String element, int size, int location) {
		if(Options.MAX_REPEATS == Constants.INT_NONE_VALUE) {
			throw new IllegalStateException("Clumps don't need to be initialised when no constraints on repeats exist");
		}
		this.element = element;
		this.size = size;
		this.location = location;
	}

	public String getElement() {
		return element;
	}

	public int getSize() {
		return size;
	}

	public int getLocation() {
		return location;
	}
	
	/**
	 * @return true if clump has size above max
	 */
	public boolean isIllegal() {
		return this.size > Options.MAX_REPEATS;
	}
	
	/**
	 * @return true if clump has size superior or equal to max, false otherwise
	 */
	public boolean isBorderlineOrWorse() {
		return this.size >= Options.MAX_REPEATS;
	}
}

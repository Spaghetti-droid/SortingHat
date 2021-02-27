package fun.gbr.entity;

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
		super();
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
	
	public boolean isIllegal() {
		return this.size > Options.MAX_REPEATS;
	}
	
	public boolean isBorderlineOrWorse() {
		return this.size >= Options.MAX_REPEATS;
	}
}

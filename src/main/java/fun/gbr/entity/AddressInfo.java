package fun.gbr.entity;

public class AddressInfo {
	
	private boolean isDirAddress;
	private int validFileNumberAtAddress;
	private String address;
	private String directory;
	
	
	public boolean isDirAddress() {
		return isDirAddress;
	}
	public void setDirAddress(boolean isDirAddress) {
		this.isDirAddress = isDirAddress;
	}
	public int getValidFileNumberAtAddress() {
		return validFileNumberAtAddress;
	}
	public void setValidFileNumberAtAddress(int validFileNumberAtAddress) {
		this.validFileNumberAtAddress = validFileNumberAtAddress;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDirectory() {
		return directory;
	}
	public void setDirectory(String directory) {
		this.directory = directory;
	}
}

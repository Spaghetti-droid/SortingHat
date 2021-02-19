package fun.gbr.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import fun.gbr.entity.AddressInfo;
import fun.gbr.io.CSVFetcher;
import fun.gbr.io.CSVListWriter;
import fun.gbr.io.FileHandler;
import fun.gbr.io.ListFetcher;
import fun.gbr.io.ListWriter;
import fun.gbr.io.TerminalFetcher;
import fun.gbr.io.TerminalWriter;
import fun.gbr.parameters.FilePurpose;
import fun.gbr.parameters.Options;
import fun.gbr.ui.UserQuit;

public class ListService {
	
	private FileHandler readStorage;
	private FileHandler writeStorage;
	
	private ListFetcher fetcher;
	private ListWriter writer;

	public AddressInfo setFileHandler(String address, FilePurpose purpose) {
		
		boolean isRead = FilePurpose.READ.equals(purpose);
		
		if(address == null) {
			address = (isRead)? Options.DEFAULT_INPUT_LOCATION : Options.DEFAULT_OUTPUT_LOCATION;
		}
		
		FileHandler handler = new FileHandler(address);
		
		if(isRead) {
			this.readStorage = handler;
		} else {
			this.writeStorage = handler;
		}
		int validFiles = handler.findAndSetFile();
		
		AddressInfo info = new AddressInfo();
		info.setAddress(address);
		info.setDirAddress(handler.isDirGiven());
		info.setDirectory(handler.getDirectory().getPath());
		info.setValidFileNumberAtAddress(validFiles);
		
		return info;		
	}
	
	public boolean createOutputFile() {
		
		return writeStorage.createFile();		
	}
	
	public boolean createOutputFileInDirectory(String fileName) {
		return writeStorage.createFileInDirectory(fileName);
	}
	
	public void clearHandler(FilePurpose purpose) {
		if(FilePurpose.READ.equals(purpose)) {
			readStorage = null;
		} else {
			writeStorage = null;
		}
	}
	
	public void initWriterAndFetcher(Scanner scanner) throws FileNotFoundException {
		
		if (readStorage == null) {
			this.fetcher = new TerminalFetcher(scanner);
		} else {
			this.fetcher = new CSVFetcher(readStorage);
		}
		if (writeStorage == null) {
			this.writer = new TerminalWriter();
		} else {
			this.writer = new CSVListWriter(writeStorage);
		}
		
	}
	
	public void fetchShuffleAndWrite() throws FileNotFoundException, IOException, UserQuit {
		
		List<String> elements = fetcher.getList();
		Collections.shuffle(elements);
		writer.writeList(elements);
	}
	
	public List<String> fetchList() throws UserQuit{
		return this.fetcher.getList();
	}
	
	public void writeList(List<String> list) throws FileNotFoundException, IOException {
		this.writer.writeList(list);
	}
	
}

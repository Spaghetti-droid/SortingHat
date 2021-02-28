package fun.gbr.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.opencsv.exceptions.CsvException;

import fun.gbr.entity.AddressInfo;
import fun.gbr.io.CSVListFetcher;
import fun.gbr.io.CSVListWriter;
import fun.gbr.io.FileHandler;
import fun.gbr.io.ListFetcher;
import fun.gbr.io.ListWriter;
import fun.gbr.io.TerminalFetcher;
import fun.gbr.io.TerminalWriter;
import fun.gbr.logic.ListProcessor;
import fun.gbr.parameters.FilePurpose;
import fun.gbr.parameters.Options;
import fun.gbr.ui.UserQuit;

/**
 * Service for treating manipulations of lists, including reading and storing. Is also used for file handling commands, so is a bit mis-named.
 *
 */
public class ListService {
	
	private FileHandler readHandler;
	private FileHandler writeHandler;
	
	private ListFetcher fetcher;
	private ListWriter writer;

	/** Initialises a file handler with the address given
	 * @param address File or dir address
	 * @param purpose Read or Write
	 * @return Info about the address chosen
	 */
	public AddressInfo setFileHandler(String address, FilePurpose purpose) {
		
		boolean isRead = FilePurpose.READ.equals(purpose);
		
		if(address == null) {
			address = (isRead)? Options.DEFAULT_INPUT_LOCATION : Options.DEFAULT_OUTPUT_LOCATION;
		}
		
		FileHandler handler = new FileHandler(address);
		
		if(isRead) {
			this.readHandler = handler;
		} else {
			this.writeHandler = handler;
		}
		int validFiles = handler.findAndSetFile();
		
		AddressInfo info = new AddressInfo();
		info.setAddress(address);
		info.setDirAddress(handler.isDirGiven());
		info.setDirectory(handler.getDirectory().getPath());
		info.setValidFileNumberAtAddress(validFiles);
		info.setDesiredFileIsGuessed(handler.isDesiredFileGuessed());
		
		return info;		
	}
	
	/** Creates a file for csv output.
	 * @return true if file created
	 */
	public boolean createOutputFile() {
		
		return writeHandler.createFile();		
	}
	
	/** Creates file in a directory for csv output
	 * @param fileName
	 * @return true if successful
	 */
	public boolean createOutputFileInDirectory(String fileName) {
		return writeHandler.createFileInDirectory(fileName);
	}
	
	/** Removes a handler
	 * @param purpose Read or Write
	 */
	public void clearHandler(FilePurpose purpose) {
		if(FilePurpose.READ.equals(purpose)) {
			readHandler = null;
		} else {
			writeHandler = null;
		}
	}
	
	/** Chooses input and output methods based on handler presence
	 * @param scanner
	 * @throws CsvException 
	 * @throws IOException 
	 */
	public void initWriterAndFetcher(Scanner scanner) throws IOException, CsvException {
		
		if (readHandler == null) {
			this.fetcher = new TerminalFetcher(scanner);
		} else {
			this.fetcher = new CSVListFetcher(scanner, readHandler);
		}
		if (writeHandler == null) {
			this.writer = new TerminalWriter();
		} else {
			this.writer = new CSVListWriter(writeHandler);
		}
		
	}
	
	/** Fetches, shuffles, and outputs a list
	 * @return true if success, false otherwise
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws UserQuit
	 */
	public boolean fetchShuffleAndWrite() throws FileNotFoundException, IOException, UserQuit {
		
		List<String> elements = fetchList();
		if(elements.isEmpty()) {
			return false;
		}
		ListProcessor.shuffle(elements);
		writer.writeList(elements);
		return true;
	}
	
	/** Gets list from input source and validates it.
	 * @return the list. Empty if validation failed.
	 * @throws UserQuit
	 */
	public List<String> fetchList() throws UserQuit{
		
		List<String> elements = new ArrayList<>();
		boolean done = false;
		boolean valid = false;
		while(!done) {
			elements = fetcher.getList();
			valid=ListProcessor.validateList(elements);
			if(!valid) {
				System.out.println("It is impossible to shuffle the list provided while avoiding sequences of more than " + Options.MAX_REPEATS + " characters.");
				done = !fetcher.onInvalidList();
			} else {
				done = true;
			}
		}
		
		if(valid) {
			return elements;
		}
		return new ArrayList<>(0);		
	}
	
	/** Returns a list of files that are considered valid at the specified location
	 * @param purpose
	 * @return A list of csv/text files
	 */
	public List<File> getvalidFileList(FilePurpose purpose){
		
		FileHandler handler = chooseHandler(purpose);
		
		if(handler != null) {
			return handler.getValidDirectoryChildren();
		}
		
		throw new IllegalStateException(purpose + " Handler not initialised!");		
	}
	
	/** Retrieves a file from a handler
	 * @param purpose
	 * @return stored file
	 */
	public File getFile(FilePurpose purpose) {
		FileHandler handler = chooseHandler(purpose);
		return handler.getFile();
	}
	
	/** Stores a file in a handler, replacing any that was there before
	 * @param file
	 * @param purpose
	 */
	public void setFile(File file, FilePurpose purpose) {
		
		FileHandler handler = chooseHandler(purpose);
		handler.setFile(file);
	}
	
	/** Chooses the handler based on purpose
	 * @param purpose
	 * @return read or write handler
	 */
	private FileHandler chooseHandler(FilePurpose purpose) {
		if(FilePurpose.READ.equals(purpose)) {
			return readHandler;
		}
		return writeHandler;
	}
	
}

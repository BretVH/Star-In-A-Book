import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * UserInterface.java
 * 
 * version 1.0 completed 5/27/2017
 */
public class UserInterface {
	private Pattern regExPattern = Pattern.compile("^\\b[a-zA-z0-9\\s]");
	private Scanner in;
	private String nameToReplace = "";

	/**
	 * Default constructor
	 * 
	 */
	public UserInterface() {}

	/*
	 * Public Methods
	 */
	
	/**
	 * Method: instantiateScannerObject instantiates a scanner using system.in
	 */
	public void instantiateScanner() {
		in = new Scanner(System.in);
	}
	
	/**
	 * Method: closeScanner Closes scanner.
	 */
	public void closeScanner() {
		in.close();
	}
	
	/**
	 * Method: commandLineUsage give info on command line usage 
	 */
	public void commandLineUsage(){
		String prompt = "Command Line usage: [filename] [name to replace] [replacement name] optional: [-L] for large font.";
		System.out.println(prompt);
	}
	
	/**
	 * Method: promptForFileName prompts for a file name 
	 */
	public void promptForFileName(){
		String prompt = "Please enter a valid file name for the book you want to star in: ";
		System.out.println(prompt);
	}
	
	/**
	 * Method: promptForNameToReplace prompts for name to replace
	 */
	public void promptForNameToReplace() {
		String prompt = "Please enter the name to replace: ";
		System.out.println(prompt);
	}
	
	/**
	 * Method: promptForReplacementName prompts for replacement name
	 */
	public void promptForReplacementName() {
		String prompt = "Please enter the replacement name: ";
		System.out.println(prompt);
	}
	
	/**
	 * Method: promptForFontChoice prompts for font choice
	 */
	public void promptForFontChoice() {
		String prompt = "If you want to use a large font press 'Y'";
		System.out.println(prompt);
	}	
	
	/**
	 * Method: getFileName gets a valid file name from user input 
	 * 
	 * @return String fileName
	 */
	public String getFileName() {
		String inputFileName = in.nextLine();
		while(!validateFileName(inputFileName)){
			promptForFileNameError();
			inputFileName = in.nextLine();
		}
		return inputFileName;
	}
	
	/**
	 * Method: getNameToReplace gets a valid name from user input 
	 * 
	 * @return String nameToReplace
	 */
	public String getNameToReplace() {
		nameToReplace = in.nextLine();
		while(!validateNameToReplace(nameToReplace)){
			promptForNameToReplaceError();
			promptForNameToReplace();
			nameToReplace = in.nextLine();
		}
		return nameToReplace;
	}
	
	/**
	 * Method: getReplacementName gets a valid name from user input 
	 * 
	 * @return String
	 */
	public String getReplacementName() {
		String name = in.nextLine();
		while(!validateReplacementName(name)){
			promptForReplacementNameError();
			promptForReplacementName();
			name = in.nextLine();
		}
		return name;
	}

	/**
	 * Method: getFontChoice sets a large font if desired
	 *
	 * @return boolean
	 */
	public boolean getFontChoice() {
		if (in.nextLine().equalsIgnoreCase("Y")) {
			return true;
		}
		return false;
	}	
	
	/**
	 * Method: getFileName returns a valid fileName
	 * 
	 * @param fileName
	 * @return String fileName
	 */
	public String getFileName(String fileName) {
		while(!validateFileName(fileName)) {
			promptForFileNameError();
			fileName = getFileName();
		}
		return fileName;
	}
	
	/**
	 * Method: getNameToReplace returns a valid name
	 * 
	 * @param name
	 * @return String nameToReplace
	 */
	public String getNameToReplace(String name) {
		nameToReplace = name;
		while(!validateNameToReplace(nameToReplace)){
			promptForNameToReplaceError();
			promptForNameToReplace();
			nameToReplace = getNameToReplace();
		}
		return nameToReplace;
	}
	
	/**
	 * Method: getReplacementName returns a valid name
	 * 
	 * @param name
	 * @return String
	 */

	public String getReplacementName(String name) {
		while(!validateReplacementName(name)){
			promptForReplacementNameError();
			promptForReplacementName();
			name = getReplacementName();
		}
		return name;
	}
	
	/**
	 * Method: getFontChoice chooses a large or small font based
	 * on String argument 
	 * 
	 * @param arg
	 * @return boolean
	 */

	public boolean getFontChoice(String arg){
		if (arg.equalsIgnoreCase("-L")) {
			System.out.println("Using Large font");
			return true;
		} else {
			System.out.println(
					"I did not recognize that option. " + "Using default font size. For large font please use -L");
			return false;
		}
	}
	
	/*
	 * Private methods
	 */
	
	/**
	 * Method: promptForFileNameError prompts user that file was not valid
	 */
	private void promptForFileNameError() {
		String prompt = "Either the file is not a .txt file or I could not find that file.  Please try again" +
			" use a valid path. Only use a text file ending in .txt and containing only ascii characters";
	    System.out.println(prompt);
	    promptForFileName();
	}
	
	/**
	 * Method: promptForReplacementNameError prompts user that name was not valid
	 */
	private void promptForReplacementNameError() {
		promptForNameToReplaceError();
		String prompt = "The replacement name cannot equal the name to replace";
		System.out.println(prompt);
	}
	
	/**
	 * Method: promptForNameToReplaceError prompts user that name was not valid
	 */
	private void promptForNameToReplaceError() {
		String prompt = "The name must contain at least one character and only use alphabetic or numerical characters" +
			". Please try again";
		System.out.println(prompt);
	}
	
	/**
	 * Method: validateFileName checks if a string is a valid file 
	 * 
	 * @param inputFileName 
	 * @return boolean
	 */
	private boolean validateFileName(String inputFileName) {
		if((inputFileName.length() <= 5) || !inputFileName.substring(inputFileName.length() - 4).equals(".txt")){
			return false;
		}
		File f = new File(inputFileName);
		return (f.exists() == true && !f.isDirectory());
	}
	
	/**
	 * Method: validateReplacementName  
	 * 
	 * @param name 
	 * @return boolean
	 */
	private boolean validateReplacementName(String name) {
		if(!validateNameToReplace(name)){
			return false;
		}
		if(name.equals(nameToReplace))
			return false;
		return true;
		
	}
	
	/**
	 * Method: validateNameToReplace  
	 * 
	 * @param name 
	 * @return boolean
	 */
	private boolean validateNameToReplace(String name) {
		Matcher matcher = regExPattern.matcher(name);
		if (matcher.find())
			return true;
		return false;
	}
}

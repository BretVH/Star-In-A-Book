/*
 * BookMaker.java
 * 
 * version 1.0 completed 4/13/2011
 * version 1.1 completed 5/25/2016
 */

/**
 * BookMaker
 *
 * class containing main, uses command line arguments, and a WordReplacer to
 * create a customized HTML representation of a book
 *
 * @author Bret A. Van Hof
 * @version 1.1
 *
 *          Compiler: Java SE 1.8 OS: Windows 10 Hardware: PC
 *
 *          April 13 2012 BVH completed v 1.0
 *          May 26 2016 BVH completed v 1.1
 */
public class BookMaker {
	
	private static final int NUMBER_OF_ARGUMENTS_EXPECTED = 3;
	private static final int EXTRA_COMMAND_LINE_OPTION = 4;
	private static UserInterface userInterface = new UserInterface();
	private static String inputFileName = null;
	private static boolean useLargeFont = false;
	private static String nameToReplace = null;
	private static String replacementName = null;
	
	/**
	 * Method: main takes command line arguments or prompts for information to
	 * pass to a WordReplacer object, and calls appropriate methods from
	 * WordReplacer object to create a customized book in html
	 * 
	 * @param args String array of input File, name to replace, replacement name
	 *        and flag for large or small type
	 */
	public static void main(String[] args) {
		userInterface.instantiateScanner();
		if (args.length < NUMBER_OF_ARGUMENTS_EXPECTED){
			userInterface.commandLineUsage();
			userInterface.promptForFileName();			
			inputFileName = userInterface.getFileName();
			userInterface.promptForNameToReplace();
			nameToReplace = userInterface.getNameToReplace();
			userInterface.promptForReplacementName();
			replacementName = userInterface.getReplacementName();
			userInterface.promptForFontChoice();
			useLargeFont = userInterface.getFontChoice();
		}
		else{		
			inputFileName = userInterface.getFileName(args[0]);
			nameToReplace = userInterface.getNameToReplace(args[1]);
			replacementName = userInterface.getReplacementName(args[2]);
	
			if(args.length >= EXTRA_COMMAND_LINE_OPTION) {
				useLargeFont = userInterface.getFontChoice(args[3]);
			}
			else {
				userInterface.promptForFontChoice();
				useLargeFont = userInterface.getFontChoice();
			}
		}
		userInterface.closeScanner();
		makeBook(inputFileName, nameToReplace, replacementName, useLargeFont);
	}

	/**
	 * Method: makeBook Creates a book with all instances of nameToReplace
	 * replaced with replacementName
	 * 
	 * @param inputFileName
	 * @param nameToReplace
	 * @param replacementName 
	 * @param useLargeFont
	 */
	private static void makeBook(String inputFileName, String nameToReplace, String replacementName,
			boolean useLargeFont) {
		WordReplacer book = new WordReplacer(inputFileName, nameToReplace, replacementName);
		book.makeArray();
		book.format();
		book.replaceName();
		book.createFile(useLargeFont);
		book.printInfo();
	}

}
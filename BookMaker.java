
/*
 * BookMaker.java
 * 
 * version 1.0 completed 4/13/2011
 * version 1.1 completed 5/25/2016
 */

import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	private static final int EXTRA_COMMAND_LINE_OPTION = 4;
	private static final int NUMBER_OF_ARGUMENTS_EXPECTED = 3;
	private static boolean found = false;

	/**
	 * Method: main takes command line arguments or prompts for information to
	 * pass to a WordReplacer object, and calls appropriate methods from
	 * WordReplacer object to create a customized book in html
	 * 
	 * @param args
	 *            String array of input File, name to replace, replacement name
	 *            and flag for large or small type
	 */
	public static void main(String[] args) {
		String inputFileName = null;
		boolean useLargeFont = false;
		String nameToReplace = null;
		String replacementName = null;
		Pattern regExPattern = Pattern.compile("^\\b[a-zA-z0-9\\s]");

		if (args.length < NUMBER_OF_ARGUMENTS_EXPECTED) {
			inputFileName = getInputFileName();
			nameToReplace = getName(regExPattern, false);
			replacementName = getName(regExPattern, true);
			replacementName = checkNames(nameToReplace, replacementName);
			useLargeFont = getFontChoice();
		} else {
			inputFileName = args[0];
			inputFileName = checkInput(inputFileName);
			nameToReplace = args[1];
			nameToReplace = checkName(nameToReplace, regExPattern, "Please enter name to replace: ");
			replacementName = args[2];
			replacementName = checkName(replacementName, regExPattern, "Please enter the replacement name: ");
			replacementName = checkNames(nameToReplace, replacementName);
			if (args.length == EXTRA_COMMAND_LINE_OPTION) {
				useLargeFont = checkFont(args[3]);
			} else
				useLargeFont = getFontChoice();
		}
		makeBook(inputFileName, nameToReplace, replacementName, useLargeFont);
	}

	/**
	 * Method: makeBook Creates a book with all instances of nameToReplace
	 * replaced with replacementName
	 * 
	 * @param String
	 *            inputFileName, String nameToReplace, String replacementName,
	 *            boolean useLargeFont
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

	/**
	 * Method: checkFont determines if a large font is desired by user
	 * 
	 * @param String
	 *            choice
	 * @return boolean
	 */
	private static boolean checkFont(String choice) {
		Scanner in = new Scanner(System.in);
		if (choice.equalsIgnoreCase("-L")) {
			System.out.println("Using Large font");
			return true;
		} else {
			System.out.println(
					"I did not recognize that option. " + "Using default font size.  For large font please use -L");
			return false;
		}
	}

	/**
	 * Method: getFontChoice Prompts user to determine if a large font is
	 * desired
	 *
	 * @return boolean
	 */
	private static boolean getFontChoice() {
		Scanner in = new Scanner(System.in);
		System.out.println("If you want to use a large font press 'Y'");
		if (in.nextLine().equalsIgnoreCase("Y")) {
			return true;
		}
		return false;
	}

	/**
	 * Method: checkNames double checks replacement name is not equal to the
	 * name to be replaced
	 * 
	 * @param String
	 *            nameToReplace, String replacementName
	 * @return String replacementName
	 */
	private static String checkNames(String nameToReplace, String replacementName) {
		Scanner in = new Scanner(System.in);
		while (nameToReplace.equals(replacementName)) {
			System.out.println();
			replacementName = in.nextLine();
		}
		return replacementName;
	}

	/**
	 * Method: getName prompts for a valid name and returns a valid name
	 * 
	 * @param Pattern
	 *            regExPattern, boolean replacement
	 * @return String
	 */
	private static String getName(Pattern regExPattern, boolean replacement) {
		Scanner in = new Scanner(System.in);
		String prompt = replacement ? "Please enter the replacement name: " : "Please enter the name to replace: ";
		System.out.println(prompt);
		String name = in.nextLine();
		return checkName(name, regExPattern, prompt);
	}

	/**
	 * Method: checkName double checks a string is a valid name and returns a
	 * valid name
	 * 
	 * @param String
	 *            name, Pattern regExPattern, String prompt
	 * @return String name
	 */
	private static String checkName(String name, Pattern regExPattern, String prompt) {
		Scanner in = new Scanner(System.in);
		Matcher matcher = regExPattern.matcher(name);
		if (matcher.find())
			found = true;
		while (!found || name.length() < 1) {
			System.out.println(prompt);
			name = in.nextLine();
			matcher = regExPattern.matcher(name);
			if (matcher.find()) {
				found = true;
			}
		}
		return name;
	}

	/**
	 * Method: getInputFileName gets a file name from user input and double
	 * checks file is valid
	 * 
	 * @return String fileName
	 */
	private static String getInputFileName() {
		Scanner in = new Scanner(System.in);
		System.out.println(
				"Input file, name to replace or replacement" + " name not found!\nPlease enter Input file name: ");
		String inputFileName = in.nextLine();
		inputFileName = checkInput(inputFileName);
		return inputFileName;
	}

	/**
	 * Method: checkInput double checks file is valid
	 * 
	 * @param String
	 *            fileName
	 * @return String fileName
	 */
	public static String checkInput(String fileName) {
		File f = new File(fileName);
		Scanner in = new Scanner(System.in);
		while (f.exists() == false) {
			System.out.print("You did not enter a valid filename!" + " please try again: ");
			fileName = in.nextLine();
			f = new File(fileName);
		}
		return fileName;
	}
}

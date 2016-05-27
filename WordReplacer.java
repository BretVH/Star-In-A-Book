/*
 * WordReplacer.java
 * 
 * version 1.0 completed 4/13/2011
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * WordReplacer
 *
 * Utility class to replace a word in a .txt file, format the .txt file and
 * create an HTML file of the re-formated .txt file
 *
 * @version 1.1
 *
 *          Compiler: Java SE 1.8 OS: Windows 10 Hardware: PC
 *
 *          April 13 2012 BVH completed v 1.0
 *          May 26 2016 BVH completed v 1.1
 */
public class WordReplacer {
	private String fileName;
	private String nameToReplace;
	private String replacementName;
	private File f;
	private String[] book;
	private int totalLines;
	private int totalParagraphs;
	private int totalSubstitutions;
	private int paragraphsWithSubstitutions;
	private static final int OFFSET = 4;
	private final String pElement = "<p>";
	//carriage feed to beautify doc
	private final String closeP = "</p>\r\n";
	private final String preElement = "<pre>";
	private final String closePre = "</pre>";
	private final String brElement = "<br />";
	private final String newLine = "\r\n";
	private boolean firstLine = true;

	/**
	 * Default constructor
	 * 
	 * @param fileName
	 *            String representation of the .txt file
	 * @param replaceable
	 *            String representation of the name to be replaced
	 * @param replacement
	 *            String representation of the replacement name
	 */
	public WordReplacer(String fileName, String replaceable, String replacement) {
		this.fileName = fileName;
		nameToReplace = replaceable;
		replacementName = replacement;
		f = new File(fileName);
	}

	/**
	 * Method: makeArray() creates an arrayList from a .txt file and transfers
	 * the arrayList to a String array
	 */
	public void makeArray() {
		ArrayList<String> theBook = scanTheBook();
		book = new String[theBook.size()];
		for (int i = 0; i < book.length; i++) {
			book[i] = theBook.get(i);
		}
		totalLines = book.length;
	}

	private ArrayList<String> scanTheBook() {
		ArrayList<String> theBook = new ArrayList<String>();
		try {
			Scanner bookScanner = new Scanner(f);
			while (bookScanner.hasNextLine()) {
				theBook.add(bookScanner.nextLine());
			}
			bookScanner.close();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(WordReplacer.class.getName()).log(Level.SEVERE, null, ex);
		}
		return theBook;
	}

	/**
	 * Method: replaceName() Uses regular expressions to replace the name given
	 * to be replaced with the name given for the replacement for each String in
	 * the aBook array
	 */
	public void replaceName() {
		Pattern pattern = Pattern.compile("(?i)\\b" + nameToReplace + "(?i)");		
		paragraphsWithSubstitutions = 0;
		totalSubstitutions = 0;
		scanBook(pattern);
	}

	private void scanBook(Pattern pattern) {
		for (int i = 0; i < book.length; i++) {
			String bookLine = book[i];
			Matcher matcher = pattern.matcher(bookLine);
			if(matcher.find()){
				paragraphsWithSubstitutions++;
				matcher.reset();
				countMatches(matcher);
				book[i] = matcher.replaceAll(replacementName);
			}
		}		
	}

	private void countMatches(Matcher matcher) {
		while (matcher.find()) {
			totalSubstitutions++;
		}
	}

	/**
	 * Method: format() uses regular expressions to replace each occurrence of "
	 * with &quot; in each String in the aBook array, then uses regular
	 * expressions to search for each occurrence of a blank String in the aBook
	 * array putting the Strings that are separated by blank lines into one
	 * String prefixed with
	 * <p>
	 * &nbsp; and postfixed with
	 * </p>
	 * , and adding that String to an arrayList it also uses regular expressions
	 * to find any String starting with 2 whitespace characters prefixing each
	 * String found with
	 * 
	 * <pre>
	 *  and postfixing it with
	 * </pre>
	 * 
	 * The arrayList is copied to a String array and the ArrayList is set to
	 * null
	 */
	public void format() {
		//note this was a requirement of the assignment and likely unnecessary
		encodeQuoteChars();
		doEncode();
		totalParagraphs = book.length;
	}

	private void doEncode() {
		ArrayList<String> bookInHTML = new ArrayList<String>();
		Pattern whitespaceLine = Pattern.compile("^\\s*$");
		Pattern twoWhitespaceChars = Pattern.compile("^\\s\\s");
		Matcher wSLMatcher;

		boolean specialFormat = false;
		String temp = "";
		for (int i = 0; i < book.length; i++) {
			boolean found = false;
			specialFormat = false;
			found = false;
			wSLMatcher = whitespaceLine.matcher(book[i]);
			Matcher twoWSCMatcher = twoWhitespaceChars.matcher(book[i]);
			if(twoWSCMatcher.find()) {
				specialFormat = true;
			}
			if(wSLMatcher.find()) {
				found = true;
			}			
			if (!found && !specialFormat){
				temp = addPLine(temp, firstLine, i);
			}
				
			else if (specialFormat && !found){
				temp = addPreLine(temp, i);
			}
			else if (found && !specialFormat) {
				//no open p element so just add a br
				if(!temp.contains(pElement)){
					bookInHTML = addBreak(temp, bookInHTML);
					if(temp.contains(preElement)){
						temp = "";
					}
				}
				//close the p tag
				else{
					bookInHTML = closeTags(temp, bookInHTML);
					temp = "";
				}
			}
			else if(found && specialFormat){
				//close pre tag
				if(temp.contains(preElement)){
					bookInHTML = closePreTag(temp, bookInHTML);
					temp = "";
				}
				//close p tag
				else if(temp.contains(pElement) && !temp.contains(closeP)){
					bookInHTML = closePTag(temp, bookInHTML);
					temp = "";
				}
			}
			if (i == book.length - 1) {
				if (temp.contains(pElement))
					bookInHTML.add(temp + closeP);
				if (temp.contains(preElement))
					bookInHTML.add(temp + closePre);
			}
		}
		book = new String[bookInHTML.size()];
		for (int j = 0; j < bookInHTML.size(); j++) {
			book[j] = bookInHTML.get(j);
		}
		bookInHTML = null;
	}

	private ArrayList<String> closePTag(String temp, ArrayList<String> bookInHTML) {
		//beautify with newLine
		temp += closeP + newLine;
		bookInHTML.add(temp);
		return bookInHTML;
	}

	private ArrayList<String> closePreTag(String temp, ArrayList<String> bookInHTML) {
		//beautify with newLine
		temp += closePre + newLine;
		bookInHTML.add(temp);
		//beautify with newLine
		bookInHTML.add(brElement + newLine);
		return bookInHTML;
	}

	private ArrayList<String> closeTags(String temp, ArrayList<String> bookInHTML) {
		//check if we need to close pre tag
		//close pre tag
		if(temp.contains(preElement)){
			//beautify with newLine
			temp += closePre;
		}
		temp += closeP;
		firstLine = false;
		bookInHTML.add(temp);
		return bookInHTML;
	}

	private ArrayList<String> addBreak(String temp, ArrayList<String> bookInHTML) {
			//check if we need to close pre tag
			//close pre tag
			if(temp.contains(preElement)){
				//beautify with newLine
				temp += closePre + newLine;
				bookInHTML.add(temp);
			}
			//add new line to make doc pretty
			bookInHTML.add(brElement + newLine);
			firstLine = !firstLine;
			return bookInHTML;			
	}

	private String addPreLine(String temp, int i) {
		//we could also use a br tag but I think assignment specified using 
		//carriage return
		if(!temp.contains(preElement))
			temp += preElement + newLine + book[i] + newLine;
		else
			temp += book[i] + newLine;
		return temp;
	}

	private String addPLine(String temp, boolean firstLine, int i) {
		if(!temp.contains(pElement)){
			temp += pElement + book[i] + " ";
			firstLine = true;
		}
		else{
			temp += book[i] + " ";
		}
		return temp;
	}

	private void encodeQuoteChars() {
		Pattern pattern = Pattern.compile("\"");
		Matcher matcher;
		String replacement = "&quot;";
		for (int i = 0; i < book.length; i++) {
			String temp = book[i];
			matcher = pattern.matcher(temp);
			book[i] = matcher.replaceAll(replacement);
		}		
	}

	/**
	 * Method: createFile() Makes an HTML file of the .txt file, taking the
	 * original filename removing the .txt extension and appending
	 * _4_aReplacementName.html
	 * 
	 * @param isLarge
	 *            boolean signifying whether to use size 18 or 14 font
	 */
	public void createFile(boolean isLarge) {
		fileName = fileName.substring(0, fileName.length() - OFFSET);
		fileName += "_4_" + replacementName + ".html";
		File modifiedBook = new File(fileName);
		try {
			PrintWriter fileOutput = new PrintWriter(new FileOutputStream(modifiedBook));
			fileOutput.append(
					"<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01" + " Transitional//EN\">\r\n<html>\r\n<head>\r\n"
							+ "<meta http-equiv=" + "\"Content-Type\" content=\"text/html; "
							+ "charset=us-ascii\">\r\n<title>the input file name + \" "
							+ "for the replacement name</title>\r\n<style type = "
							+ "\"text/css\">\t body {font-family: \"Times New Roman, " + "serif\"; font-size: ");
			if (isLarge)
				fileOutput.append("18;");
			else
				fileOutput.append("14;");
			fileOutput.append("text-align: justify;};\tp { margin-left: 1%; "
					+ "margin-right: 1%; }</style>\r\n</head>\r\n\r\n" + "<body>\r\n");
			for (String paragraph : book) {
				fileOutput.append(paragraph);
			}
			fileOutput.append("</body></html>");
			fileOutput.close();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(WordReplacer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Method: printInfo prints information about .txt and .html file
	 */
	public void printInfo() {
		System.out.println("Star in Your Own Book! Ver. 1.0 By Bret A " + "Van Hof");
		System.out.println("replace " + nameToReplace + " with " + replacementName);
		System.out.println("file " + fileName + " created...");
		System.out.println("original lines: " + totalLines);
		System.out.println("total paragraphs: " + totalParagraphs);
		System.out.println(totalSubstitutions + " replacements in " + paragraphsWithSubstitutions + " paragraphs");
	}
}

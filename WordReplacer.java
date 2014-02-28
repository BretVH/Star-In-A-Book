/*
 * WordReplacer.java
 * 
 * version 1.0 completed 4/13/2011
 */
package Star_In_A_Book;

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
 * Utility class to replace a word in a .txt file, format the .txt file 
 * and create an HTML file of the re-formated .txt file
 *
 * @author Bret A. Van Hof
 * @version 1.0
 *
 * Compiler: Java 1.71 
 * OS: Windows 7 
 * Hardware: PC 
 *
 * April 13 2012
 * BVH completed v 1.0
 */ 
public class WordReplacer 
{
    private String aFileName;
    private String aReplaceable;
    private String aReplacement;
    private File f;
    private String[] aBook;
    private int totalLines;
    private int totalParagraphs;
    private int totalSubstitutions;
    private int paragraphsWithSubs;
    private static final int OFFSET = 4;
    
   /**
    * Default constructor
    * @param fileName String representation of the .txt file
    * @param replaceable String representation of the name to be replaced
    * @param replacement String representation of the replacement name
    */
    public WordReplacer
            (String fileName, String replaceable, String replacement)
    {
        aFileName = fileName;
        aReplaceable = replaceable;
        aReplacement = replacement;
        f = new File(aFileName);
    }
    /**
     * Method: makeArray()
     * creates an arrayList from a .txt file and transfers the arrayList to a
     * String array
     */
    public void makeArray()
    {
        ArrayList<String> theBook = new ArrayList();
        try 
        {
            Scanner bookScanner = new Scanner(f);
            while(bookScanner.hasNextLine())
            {
                theBook.add(bookScanner.nextLine());
            }
            bookScanner.close();
        }
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(WordReplacer.class.getName()).log
                    (Level.SEVERE, null, ex);
        }
        aBook = new String[theBook.size()];
        for(int i = 0; i < aBook.length; i++)
        {
            aBook[i] = theBook.get(i);
        }
        totalLines = aBook.length;
    }
    /**
     * Method: replaceName()
     * Uses regular expressions to replace the name given to be replaced with 
     * the name given for the replacement for each String in the aBook array
     */
    public void replaceName()
    {
         Pattern pattern = Pattern.compile("(?i)\\b" + aReplaceable + "(?i)");
         Matcher matcher;
         int diff = aReplaceable.length() - aReplacement.length(); 
         paragraphsWithSubs = 0;
         totalSubstitutions = 0;
         for(int i = 0; i < aBook.length; i++)
         {
            boolean aNewParagraph = true;
            String temp = aBook[i];
            matcher = pattern.matcher(temp);
            String temp2;
            int count = 0;
            boolean useUppercase = false;
            String anotherReplacement = aReplacement.toUpperCase();
            while (matcher.find()) 
	    {
                totalSubstitutions++;
                if(aNewParagraph)
                {
                    paragraphsWithSubs++;
                    aNewParagraph = false;
                }
                if(matcher.group().equals(aReplaceable.toUpperCase()))
                    useUppercase = true;
                if(matcher.start() == 0)
                {
                    if(useUppercase)
                        aBook[i] = anotherReplacement;
                    else
                        aBook[i] = aReplacement;
                    temp2 = temp.substring(matcher.end());
                    aBook[i] = aBook[i].concat(temp2);
                    count++;
                }
                else
                {
                    temp2 = aBook[i].substring(0, matcher.start() - 
                            (diff * count));
                    aBook[i] = temp2;
                    if(useUppercase)
                        aBook[i] = aBook[i].concat(anotherReplacement);
                    else
                        aBook[i] = aBook[i].concat(aReplacement);
                    aBook[i] = aBook[i].concat(temp.substring(matcher.end()));
                    count++;
                }
                useUppercase = false;
            }
         }   
    }
    /**
     * Method: format()
     * uses regular expressions to replace each occurrence of " with &quot; in 
     * each String in the aBook array, then uses regular expressions to 
     * search for each occurrence of a blank String in the aBook array putting 
     * the Strings that are separated by blank lines into one String prefixed 
     * with <p>&nbsp; and postfixed with </p>, and adding that String to an 
     * arrayList it also uses regular expressions
     * to find any String starting with 2 whitespace characters prefixing each 
     * String found with <pre> and postfixing it with </pre> The arrayList is 
     * copied to a String array and the ArrayList is set to null
     */
    public void format()
    {
        Pattern pattern = Pattern.compile("\"");
        Matcher matcher;
        for(int i = 0; i < aBook.length; i++)
        {
            String temp = aBook[i];
            String temp2;
            String replacement = "&quot;";
            int count = 0;
            int diff = 5;
            matcher = pattern.matcher(temp);
            while(matcher.find())
            {
                if(matcher.start() == 0)
                {
                    aBook[i] = replacement;
                    temp2 = temp.substring(matcher.end());
                    aBook[i] = aBook[i].concat(temp2);
                    count++;
                }
                else
                {     
                    aBook[i] = aBook[i].substring(0, matcher.start() + 
                            (diff * count));
                    aBook[i] = aBook[i].concat("&quot;");
                    aBook[i] = aBook[i].concat(temp.substring
                            (matcher.end()));
                    count++;
              }
            }
        }
        ArrayList<String> newBook = new ArrayList();
        pattern = Pattern.compile("^\\s*$");
        Pattern anotherPattern = Pattern.compile("^\\s\\s");
        Matcher anotherMatcher;
        String added = "<p>&nbsp; ";
        String special = "<pre>\r\n";
        boolean secondLook = false;
        boolean specialFormat = false;
        boolean firstLine = true;
        for(int i = 0; i < aBook.length; i++)
        {
            boolean found = false;
            matcher = pattern.matcher(aBook[i]);
            secondLook = false;
            anotherMatcher = anotherPattern.matcher(aBook[i]);
            while(anotherMatcher.find())
            {
                secondLook = true;
                specialFormat = true;
            }
            while(matcher.find())
            {
                found = true;
            }
            if(!found && !secondLook)
            {   
                if(specialFormat)
                {
                    if(!special.equals("<pre>\r\n"))
                    {
                        newBook.add(special + "\r\n</pre>\r\n");
                        special = "<pre>\r\n";
                    }
                    else
                        newBook.add("<br />\r\n");
                }
                if(aBook[i].startsWith("-"))
                {
                    added += "</p>\r\n";
                    newBook.add(added);
                    added = "<p>&nbsp; ";
                }
                added += aBook[i] + " ";
                specialFormat = false;
            }
            if(!found)
                firstLine = true;
            if(secondLook)
                special += aBook[i] + "\r\n";
            if(found && !specialFormat)
            {
                if(firstLine)
                {
                    if(!added.equals("<p>&nbsp; "))
                    {
                        newBook.add(added + "</p>\r\n");
                        added = "<p>&nbsp; ";
                    }
                    else
                        newBook.add("<br />\r\n");
                    firstLine = false; 
                }
                else
                {
                    newBook.add("<br />\r\n");
                    firstLine = true;
                }
            }
            if(found && specialFormat)
            {
                if(!(added.equals("<p>&nbsp; ")) && added.startsWith("<p>") && 
                        !(added.substring
                            (added.length() - 2*OFFSET, added.length()).equals
                                ("</p>\r\n")))
                {
                    newBook.add(added + "</p>\r\n");
                    added = "<p>&nbsp; ";
                }     
                else if(!special.equals("<pre>\r\n"))
                {
                    newBook.add(special + "</pre>\r\n");
                    newBook.add("<br />\r\n");
                    specialFormat = false;
                }
                special = "<pre>\r\n";
            }
            if(i == aBook.length - 1)
            {
                if(!added.equals("<p>&nbsp; "))
                    newBook.add(added + "</p>\r\n");
                if(!special.equals("<pre>\r\n"))
                    newBook.add(special + "</pre>\r\n");
            }
        }
        aBook = new String[newBook.size()];
        for(int j = 0; j < newBook.size(); j++)
        {
            aBook[j] = newBook.get(j);
        }
        newBook = null;
        totalParagraphs = aBook.length;
    }
    /**
     * Method: createFile()
     * Makes an HTML file of the .txt file, taking the original filename 
     * removing the .txt extension and appending _4_aReplacementName.html
     * @param isLarge boolean signifying whether to use size 18 or 14 font
     */
    public void createFile(boolean isLarge)
    {
         aFileName = aFileName.substring(0, aFileName.length() - OFFSET); 
         aFileName += "_4_"+ aReplacement + ".html";
         File finalBook = new File(aFileName);
        try 
        {
            PrintWriter fileOutput = new PrintWriter(new FileOutputStream
                (finalBook));
            fileOutput.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01"
                    + " Transitional//EN\">\r\n<html>\r\n<head>\r\n"
                    + "<meta http-equiv="
                    + "\"Content-Type\" content=\"text/html; "
                    + "charset=us-ascii\">\r\n<title>the input file name + \" "
                    + "for the replacement name</title>\r\n<style type = "
                    + "\"text/css\">\t body {font-family: \"Times New Roman, "
                    + "serif\"; font-size: ");
                if(isLarge)
                    fileOutput.append("18;");
                else
                    fileOutput.append("14;");
                fileOutput.append("text-align: justify;};\tp { margin-left: 1%; "
                        + "margin-right: 1%; }</style>\r\n</head>\r\n\r\n"
                        + "<body>\r\n");
            for(String paragraph: aBook)
            {
                fileOutput.append(paragraph);
            }
            fileOutput.append("</body>");
            fileOutput.close();
        } 
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(WordReplacer.class.getName()).log
                    (Level.SEVERE, null, ex);
        }
    }
    /**
     * Method: printInfo
     * prints information about .txt and .html file
     */
    public void printInfo()
    {
        System.out.println("Star in Your Own Book! Ver. 1.0 By Bret A "
                + "Van Hof");
        System.out.println("replace " + aReplaceable + " with " + aReplacement);
        System.out.println("file " + aFileName + " created...");
        System.out.println("original lines: " + totalLines);
        System.out.println("total paragraphs: " + totalParagraphs);
        System.out.println(totalSubstitutions + " replacements in " + 
                paragraphsWithSubs + " paragraphs");
    }
}

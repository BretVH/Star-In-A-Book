/*
 * FileOpener.java
 * 
 * version 1.0 completed 4/13/2011
 */
package Star_In_A_Book;

import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * WordReplacer
 *
 * class containing main, uses command line arguments, and WordReplacer object 
 * to create a customized HTML representation of a book 
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
public class FileOpener 
{
    private static final int EXTRA_COMMAND_LINE_OPTION = 4;
    
    /**
     * Method: main takes command line arguments or prompts for information to
     * pass to a WordReplacer object, calls appropriate methods from 
     * WordReplacer object to create a customized book in html 
     * @param args String array of input File, name to replace, replacement name
     * and flag for large or small type
     */
    public static void main(String[] args)
    {
        String inputFileName = null;
        boolean useLargeFont = false;
        String replaceable = null;
        String replacement = null;
        Scanner in = new Scanner(System.in);      
        Pattern pattern = Pattern.compile("^\\b[a-zA-z0-9\\s]");
        boolean found = false;
        
        if(args.length < 3)
        {
            System.out.println("Input file, name to replace or replacement"
                    + " name not found!\nPlease enter Input file name: ");
            inputFileName = in.nextLine();
            inputFileName = checkInput(inputFileName);
            System.out.println("Please enter name to replace: ");
            replaceable = in.nextLine();
            Matcher matcher = pattern.matcher(replaceable);
            if(matcher.find())
                found = true;
            while(!found || replaceable.length() < 1)
            {
                System.out.println("Please enter name to replace: ");
                replaceable = in.nextLine();
                matcher = pattern.matcher(replaceable);
                if(matcher.find())
                    found = true;
            }
            System.out.println("Please enter the replacement name: ");
            replacement = in.nextLine();
            
            while(replaceable.equals(replacement))
            {
                System.out.println("Please enter the replacement name: ");
                replacement = in.nextLine();
            }
            System.out.println("If you want to use a large font press 'Y'");
            if(in.nextLine().equalsIgnoreCase("Y"))
                useLargeFont = true;               
        }
        else
        {
            inputFileName = args[0];
            inputFileName = checkInput(inputFileName);
            replaceable = args[1];
            Matcher matcher = pattern.matcher(replaceable);
            if(matcher.find())
                found = true;
            while(!found || replaceable.length() < 1)
            {
                System.out.println("Please enter name to replace: ");
                replaceable = in.nextLine();
                matcher = pattern.matcher(replaceable);
                if(matcher.find())
                    found = true;
            }
            replacement = args[2];
            while(replaceable.equals(replacement) || replaceable.length() < 1 
                    || args[2].substring(0,1).equals("-"))
            {
                System.out.println("Please enter the replacement name: ");
                replacement = in.nextLine();
                System.out.println("Enter 'y' to use Larger fonts: ");
                if(in.nextLine().equalsIgnoreCase("y"))
                    useLargeFont = true;
            }
        }
        if(args.length == EXTRA_COMMAND_LINE_OPTION)
        {   if(args[3].equalsIgnoreCase("-L"))
                useLargeFont = true;
            else
            {
                System.out.println("I did not recognize that option"
                        + "Enter 'y' to use Larger fonts");
                if(in.nextLine().equalsIgnoreCase("y"))
                    useLargeFont = true;
            }
        }
        WordReplacer book = new WordReplacer
                (inputFileName, replaceable, replacement);
        book.makeArray();
        book.format();
        book.replaceName();
        book.createFile(useLargeFont);
        book.printInfo();
    }
    /**
   * Method: checkInput
   * double checks file is valid
   * @param fileName
   * @return fileName
   */
  public static String checkInput(String fileName)
  {
      File f = new File(fileName);
      Scanner in = new Scanner(System.in);
      while (f.exists() == false)
      {
          System.out.print("You did not enter a valid filename!"
                  + " please try again: ");
          fileName = in.nextLine();
          f = new File(fileName);
      }
      return fileName;
  }
}

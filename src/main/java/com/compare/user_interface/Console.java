package com.compare.user_interface;

import java.util.Scanner;

/**
 * The user interface for the program. Obtains user input through the recieveInput()
 * function and displays results to the user with the displayResults() function.
 * 
 * @author luke
 *
 */
public class Console {	
	//the parser used to check input
	private InputParser parser;
	//the scanner used to obtain user input
	private Scanner input;
	
	/**Constructor*/
	public Console() {
		input = new Scanner(System.in);
		parser = new InputParser();
	}
	
	/**
	 * function to close scanner on system exit
	 */
	public void closeScanner() {
		input.close();
	}
    
    /**
     * displays results of a command to the user
     * 
     * @param results the results of a command to display to the user
     */
    public void displayResults(String results) {
    	System.out.println(results);
    }
    
    /**
     * STUBBED UNTIL COMPARATOR COMPLETE
     */
    public void printProgress() {
    	System.out.println("update");
        // https://stackoverflow.com/questions/1001290/console-based-progress-in-java (Just use carriage return)
    }

    public OverwriteOption getOverwriteChoice(String filename){
    	String choicesMessage = "([y]es/[n]o/[a]ppend)";

    	System.out.print("The file: " + filename + " already exists.\n" +
				"Do you wish to overwrite it?" + choicesMessage +  ":");
		System.out.print("");

		OverwriteOption result = null;
		do {
			String choice = input.nextLine();
			if (choice.equalsIgnoreCase("y"))
				result = OverwriteOption.Yes;
			else if (choice.equalsIgnoreCase("n"))
				result = OverwriteOption.No;
			else if (choice.equalsIgnoreCase("a"))
				result = OverwriteOption.Append;
			else
				System.out.print("Invalid choice, options are " + choicesMessage + ": ");
		}while(result == null);
    	return result;
	}
}

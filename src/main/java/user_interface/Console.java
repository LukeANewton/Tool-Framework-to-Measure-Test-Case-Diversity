package user_interface;

import model.DataTransferObject;
import model.HelpDTO;
import model.HelpType;

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
	 * Gets user input and parses input into a DataTransferObject containing 
	 * the necessary information to issue a system command
	 * 
	 * @return the DataTransferObject containing information to issue a request 
	 * 				to the controller
	 */
    public DataTransferObject receiveInput() {
    	//get user input
        System.out.print("\nEnter command: ");
        String command = input.nextLine();
        
        //parse input into DataTransferObject
        DataTransferObject dto;
        try {
			dto = parser.parse(command);
		} catch (InvalidCommandException e) {
			/*if we have an invalid command, display an error message and list the 
			valid commands through issuing a HelpDTO*/
			System.out.println(e.getErrorMessage() + " Valid commands are:");
	    	HelpDTO help = new HelpDTO();
			help.setHelpType(HelpType.Command);
			dto = help;
		}
        return dto;
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

}

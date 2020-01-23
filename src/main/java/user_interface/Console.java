package user_interface;

import model.DataTransferObject;
import model.HelpDTO;
import model.HelpType;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

/**
 * The user interface for the program. Obtains user input through the recieveInput()
 * function and displays results to the user with the displayResults() function.
 * 
 * @author luke
 *
 */
public class Console implements PropertyChangeListener {
	//the parser used to check input
	private InputParser parser;
	//the scanner used to obtain user input
	private Scanner input;
	//used in displaying progress to denote the number of completed tasks
	private int completedComparisons;
	//used in displaying progress to denote the total number of tasks
	private int numberTasks;
	/*used in displaying progress for performance reasons. we keep track of every 0.1
	increment in progress so that we only update the progress bar when another 10%
	completion is made. This means we only have to print at most 10 times, instead of
	every time a task is completed, which would be very slow*/
	private double lastProgressMilestone;
	
	/**Constructor*/
	public Console() {
		input = new Scanner(System.in);
		parser = new InputParser();
		completedComparisons = 0;
		numberTasks = 0;
		lastProgressMilestone = 0;
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
        return processInstruction(command);
    }
    
    /**
     * displays results of a command to the user
     * 
     * @param results the results of a command to display to the user
     */
    public void displayResults(String results) {
    	System.out.println(results);
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

	/**
	 * takes a string containing a command as input, and packages the relevant information into a DataTransferObject
	 *
	 * in order to perform instructions passed by the command line, this has been pulled out of the receiveInput method
	 *
	 * @param command a string of the command to parse
	 * @return a DataTransferObject with the necessary information to perform a the passed command
	 */
	public DataTransferObject processInstruction(String command) {
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

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("numberTasks"))
			numberTasks = (Integer) evt.getNewValue();
		else {
			synchronized (this) {
				completedComparisons++;
				double percent = ((double) completedComparisons) / numberTasks;

				if(percent - lastProgressMilestone >= 0.1) {
					lastProgressMilestone = percent;
					if (completedComparisons == numberTasks)
						System.out.println("[==========]");
					else {
						int n = (int) (percent * 10);
						System.out.print(
								"[" +
								String.format("%0" + n + "d", 0).replace("0", "=") +
								String.format("%0" + (10 - n) + "d", 0).replace("0", " ")
								+ "]\r"
						);
					}
				}
			}
		}
	}
}

package user_interface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Scanner;

/**
 * The user interface for the program. Obtains user input through the recieveInput()
 * function and displays results to the user with the displayResults() function.
 * 
 * @author luke
 *
 */
public class Console implements PropertyChangeListener {
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

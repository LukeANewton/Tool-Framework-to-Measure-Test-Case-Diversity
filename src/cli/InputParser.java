package cli;

import model.JobDTO;

public class InputParser {

    public JobDTO parse(String command) {
        // To get commands, we receive one string from the user and
        // .split(" ") to make an array of words
        // the first 1-n elements can be assigned to task names
        // after these elements come the assortment of -a --b -c -d flags...
        // while the array still has elements,
        // if the flag is -a, shift or pop element and set variable. shift/pop again if needed
        return null;
    }
}

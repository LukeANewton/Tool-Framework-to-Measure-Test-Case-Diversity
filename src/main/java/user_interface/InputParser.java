package user_interface;

import model.JobDTO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class InputParser {

    public JobDTO parse(String fullCommand) {

        // To get commands, we receive one string from the user and
        // .split(" ") to make an array of words
        // the first 1-n elements can be assigned to task names
        // after these elements come the assortment of -a --b -c -d flags...
        // while the array still has elements,
        // if the flag is -a, shift or pop element and set variable. shift/pop again if needed

        //TODO: Rethink this
        Stack args = new Stack();
        List<String> listArgs = Arrays.asList(fullCommand.split(" "));
        Collections.reverse(listArgs);
        listArgs.stream().forEachOrdered(args::push);

        JobDTO job = new JobDTO();
        job.setCommand(args.pop().toString());

        while (args.size() > 0) {
            String arg = args.pop().toString();
            switch(arg) {
                case "-f": job.setDataLocation(args.pop().toString()); break;
                case "-c": job.setComparisonMethod(args.pop().toString()); break;
                case "-h":
                case "-help":  System.out.println("Theres no helping you."); job.setCommand("help"); break;
                default: System.err.println("Unknown parameter " + arg + " remove me in place of exception."); /*throw new UnknownFormatException to bubble up to Controller*/;
            }
        }
        return job;
    }
}

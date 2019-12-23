package user_interface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import model.CompareDTO;
import model.ConfigDTO;
import model.DataTransferObject;
import model.ExitDTO;
import model.HelpDTO;
import model.HelpType;
import model.UpdateDTO;

/**
 * The parser for commands input through the user interface. The parse() function takes
 * the command as input and returns a DataTransfer object which encapsulates all the 
 * information required to issue a request to the controller. 
 * 
 * @author luken
 *
 */
public class InputParser {
	/*constants for the keywords to issue a command*/
	private static final String EXIT_COMMAND = "exit";
	private static final String UPDATE_COMMAND = "update";
	private static final String HELP_COMMAND = "help";
	private static final String CONFIG_COMMAND = "config";
	private static final String COMPARE_COMMAND = "compare";
	
	/*constants for the flags used in commands*/
	private static final String DIVERISTY_METRIC_FLAG = "-m";
	private static final String AGGREGATION_METHOD_FLAG = "-a";
	private static final String DELIMITER_FLAG = "-d";
	private static final String NUMBER_THREADS_FLAG = "-t";
	private static final String SAVE_FLAG = "-s";
	private static final String DATA_REPRESENTATION_FLAG = "-f";
	private static final String UPDATE_LOACTION_FLAG = "-l";
	
	/**
	 * The main function provided by the InputParser object. parse() takes a
	 * string as input and attempts to extract the information for a system 
	 * command, which is encapsulated into a DataTransferObject
	 * 
	 * @param command a string representation of a system command
	 * @throws InvalidCommandException exception thrown when the command is determined invalid
	 * @return a DataTransferObject, containing the relevant information to make a 
	 * 				request to the controller
	 */
    public DataTransferObject parse(String command) throws InvalidCommandException {
    	//first, strip any whitespace preceding or following the command
    	String stripedCommand = command.strip();
    	
    	//next, break command into tokens
    	ArrayList<String> tokens = new ArrayList<>(Arrays.asList(stripedCommand.split(" ")));
    	System.out.println("tokens:" + tokens);
    	
    	//the type of command is determined by the first token
    	String commandType = tokens.remove(0);
    	if(commandType.equals(EXIT_COMMAND)) {
    		return parseExitCommand(tokens);
    	} else if (commandType.equals(UPDATE_COMMAND)) {
    		return parseUpdateCommand(tokens);
    	} else if (commandType.equals(HELP_COMMAND)) {
    		return parseHelpCommand(tokens);
    	} else if (commandType.equals(CONFIG_COMMAND)) {
    		return parseConfigCommand(tokens);
    	} else if (commandType.equals(COMPARE_COMMAND)){
    		return parseCompareCommand(tokens);
    	} else { // the command entered is not recognized
    		throw new InvalidCommandException("The command entered is not recognized.");
    	}
    }
    
    /**
     * parse instructions for an exit command
     * 
     * @param tokens a list of any specified flags/values after the command keyword
     * @throws InvalidCommandException exception thrown when the command is determined invalid
     * @return a DataTransferObject containing the necessary information to issue the command input
     */
	private DataTransferObject parseExitCommand(List<String> tokens) throws InvalidCommandException{
		//expect command to match: exit
		
		if (tokens.size() == 0) { //the exit command only contained "exit"
			return new ExitDTO();
		} else { //the command has unnecessary extra tokens
			throw new InvalidCommandException("Unexpected additional tokens.");
		}
	}

    /**
     * parse instructions for a configure command
     * 
     * @param tokens a list of any specified flags/values after the command keyword
     * @throws InvalidCommandException exception thrown when the command is determined invalid
     * @return a DataTransferObject containing the necessary information to issue the command input
     */
	private DataTransferObject parseConfigCommand(List<String> tokens) throws InvalidCommandException{
		// expect command to match: config <parameter-name> <parameter-value> [-s]
		ConfigDTO configure = new ConfigDTO();
		
		if(tokens.size() == 0) {//no configuration was specified with command
			throw new InvalidCommandException("No parameter name or value was specified.");
		} else if (tokens.size() == 1) {//the command is too short
			throw new InvalidCommandException("Command too short.");
		} else if (tokens.size() == 2) { //expect this to be the parameter to configure and associated value
			configure.setParameterName(tokens.get(0));
			configure.setParameterValue(tokens.get(1));
			return configure;
		} else if (tokens.size() == 3) {
			if(tokens.get(2).equals(SAVE_FLAG)) {//the configuration must be saved in the configuration file
				configure.setParameterName(tokens.get(0));
				configure.setParameterValue(tokens.get(1));
				configure.setConfigSaved(true);
				return configure;
			} else {//the command has unrecognized or unnecessary extra tokens
				throw new InvalidCommandException("Unrecognized or unexpected additional tokens.");
			}
		} else { //the command has unnecessary extra tokens
			throw new InvalidCommandException("Unexpected additional tokens.");
		}
	}

    /**
     * parse instructions for a compare command
     * 
     * @param tokens a list of any specified flags/values after the command keyword
     * @throws InvalidCommandException exception thrown when the command is determined invalid
     * @return a DataTransferObject containing the necessary information to issue the command input
     */
	private DataTransferObject parseCompareCommand(List<String> tokens) throws InvalidCommandException{
		//expect command to match: compare <test-file-1> [<test-file-2>] <data-representation> [-m <pairwise-metric> | -a <aggregation-method> |
		//													-d <delimiter> | -t <number-of-threads> | -s <output-file-location>]
		CompareDTO compare = new CompareDTO();
		
		//first check the boundaries for allowable command sizes
		if(tokens.size() <= 1) {//no test case files have been specified
			throw new InvalidCommandException("No test case(s) or data representation specified.");
		} else if(tokens.size() >= 14) {//if every option is specified in the command, there are 13 tokens. Any more than that are invalid
			throw new InvalidCommandException("Unexpected additional tokens.");
		}
		
		//after checking boundaries, we need to determine filenames and what flags have been set
		Iterator<String> tokenIterator = tokens.iterator();
		String token = tokenIterator.next();
		//check first position, which should always be the first filename
		if(isTokenCompareFlag(token)) {
			throw new InvalidCommandException("Filenames for test cases must be spcified before any flags.");
		} else {
			compare.setTestCaseLocationOne(token);
		}
		
		//next, we must determine if there is a second filename provided
		if(tokens.size() == 2) {//only one filename and data representation should be specified
			compare.setDataRepresentation(tokenIterator.next());
			return compare;
		} else if (tokens.size() >= 3) {//either there are two filenames and a data representation, or the command is invalid (each command needs a following value)
			if(isTokenCompareFlag(tokens.get(2))) {//the third token is a flag, so the command is invalid
				throw new InvalidCommandException("Flag has no value.");
			}else {//assume there are two filenames and a data representation
				compare.setTestCaseLocationTwo(tokenIterator.next());
				compare.setDataRepresentation(tokenIterator.next());
			}
		}
		
		//check remainder of command for flags
		while(tokenIterator.hasNext()) {
			token = tokenIterator.next();
			if(token.equals(DIVERISTY_METRIC_FLAG)) {// found a pairwise metric flag
				//if a flag is found there should be a next token, and it should not also be a flag
				if(!tokenIterator.hasNext()) {//reached the end of the tokens, so there is no value after the flag
					throw new InvalidCommandException("No metric specified after pairwise flag.");
				}
				token = tokenIterator.next();
				if(isTokenCompareFlag(token)) {// the next token is a flag, so there is no value after the flag
					throw new InvalidCommandException("No metric specified after pairwise flag.");
				} else { //the next token should be the pairwise metric name
					compare.setPairwiseMethod(token);
				}
			} else if(token.equals(AGGREGATION_METHOD_FLAG)) {//found an aggregation method flag
				if(!tokenIterator.hasNext()) {//reached the end of the tokens, so there is no value after the flag
					throw new InvalidCommandException("No method specified after aggregation flag.");
				}
				token = tokenIterator.next();
				if(isTokenCompareFlag(token)) {// the next token is a flag, so there is no value after the flag
					throw new InvalidCommandException("No method specified after aggregation flag.");
				} else { //the next token should be the aggregation method name
					compare.setAggregationMethod(token);
				}
			} else if(token.equals(DELIMITER_FLAG)) {//found a delimiter flag
				if(!tokenIterator.hasNext()) {//reached the end of the tokens, so there is no value after the flag
					throw new InvalidCommandException("No delimiter specified after flag.");
				}
				token = tokenIterator.next();
				if(isTokenCompareFlag(token)) {// the next token is a flag, so there is no value after the flag
					throw new InvalidCommandException("No delimiter specified after flag.");
				} else { //the next token should be the delimiter
					//delimiters must be at least three characters: open quote, at least one character, close quote
					if(token.length() < 3) {
						throw new InvalidCommandException("Delimiter too short. Delimters should be enclosed in quotes and contain at leat one character.");
					}
					
					//the delimiter should be specified with surrounding quotes, which should be stripped
					if(token.charAt(0) == '"' && token.charAt(token.length()-2) == '"') {
						compare.setDelimiter(token.substring(1, token.length()-2));
					} else {
						throw new InvalidCommandException("Delimiter should be enclosed in quotation marks.");
					}
				}
			} else if(token.equals(NUMBER_THREADS_FLAG)) {//found a flag to set number of threads
				if(!tokenIterator.hasNext()) {//reached the end of the tokens, so there is no value after the flag
					throw new InvalidCommandException("No value specified after flag.");
				}
				token = tokenIterator.next();
				if(isTokenCompareFlag(token)) {// the next token is a flag, so there is no value after the flag
					throw new InvalidCommandException("No value specified after flag.");
				} else { //the next token should be the number of threads to use
					//must also check if the value is a valid integer
					try {
						Integer numThreads = Integer.parseInt(token);
						compare.setNumberOfThreads(numThreads);
					} catch (NumberFormatException e) {
						throw new InvalidCommandException("Value specified after number of threads flag is not a number.");
					}
				}
			} else if(token.equals(SAVE_FLAG)) {//found a flag to specify if saving results to a file
				if(!tokenIterator.hasNext()) {//reached the end of the tokens, so there is no value after the flag
					throw new InvalidCommandException("No filename specified after save flag.");
				}
				token = tokenIterator.next();
				if(isTokenCompareFlag(token)) {// the next token is a flag, so there is no value after the flag
					throw new InvalidCommandException("No filename specified after save flag.");
				} else { //the next token should be the filename to save output to
					compare.setOutputFilename(token);
				}
			} else {//the token is not a flag, and should not be in the command
				throw new InvalidCommandException("Unrecognized token '" + token + "'.");
			}
		} 
		return compare;
	}
	
	/**
	 * helper function to determine if a token is one of the possible flags that can be set for a compare command
	 * 
	 * @param token the token to check for valid flags
	 * @return true if the token input is a flag that is valid in a compare command, otherwise returns false
	 */
	private boolean isTokenCompareFlag(String token) {
		return token.equals(DIVERISTY_METRIC_FLAG) | token.equals(AGGREGATION_METHOD_FLAG) | token.equals(DELIMITER_FLAG) |
				token.equals(NUMBER_THREADS_FLAG) | token.equals(SAVE_FLAG);
	}

    /**
     * parse instructions for a help command
     * 
     * @param tokens a list of any specified flags/values after the command keyword
     * @throws InvalidCommandException exception thrown when the command is determined invalid
     * @return a DataTransferObject containing the necessary information to issue the command input
     */
	private DataTransferObject parseHelpCommand(List<String> tokens) throws InvalidCommandException{
		//expect command to match: help [-d | -a | -f]
		HelpDTO help = new HelpDTO();
		
		if (tokens.size() == 0) { //the help command contained only "help"
			help.setHelpType(HelpType.Command);
		} else if (tokens.size() == 1) {
			if(tokens.get(0).equals(DIVERISTY_METRIC_FLAG)) { //need to provide list of diversity metrics
				help.setHelpType(HelpType.PairwiseMetric);
			} else if (tokens.get(0).equals(AGGREGATION_METHOD_FLAG)) {//need to provide list of aggregation methods
				help.setHelpType(HelpType.AggregationMethod);
			} else if (tokens.get(0).equals(DATA_REPRESENTATION_FLAG)) {//need to proved list of data representations
				help.setHelpType(HelpType.DataRepresentation);
			} else { //the type of help requested is not recognized
				System.out.println("Help type not valid.");
				help.setHelpType(HelpType.Command);
			}
		} else {//the command has unnecessary extra tokens
			System.out.println("Unexpected additional tokens.");
			help.setHelpType( HelpType.Command);
		}
		return help;
	}

    /**
     * parse instructions for an update command
     * 
     * @param tokens a list of any specified flags/values after the command keyword
     * @throws InvalidCommandException exception thrown when the command is determined invalid
     * @return a DataTransferObject containing the necessary information to issue the command input
     */
	private DataTransferObject parseUpdateCommand(List<String> tokens) throws InvalidCommandException{
		//expect command to match: update [-l <class-name>]
		UpdateDTO update = new UpdateDTO();
		
		if(tokens.size() == 0) {//need to restart without checking for new class on reboot
			return update;
		} else if (tokens.size() == 1) {//the command is either too short, or too long
			throw new InvalidCommandException("Command is incorrect length.");
		} else if (tokens.size() == 2) {
			if (tokens.get(0).equals(UPDATE_LOACTION_FLAG)) {//need to check on restart whether the specified class can be found
				update.setLocation(tokens.get(1));
				return update;
			} else { // the flags specified are not recognized
				throw new InvalidCommandException("Options specified in command are not recognized.");
			}
		} else {//the command has unnecessary extra tokens
			throw new InvalidCommandException("Unexpected additional tokens.");
		}
	}
}

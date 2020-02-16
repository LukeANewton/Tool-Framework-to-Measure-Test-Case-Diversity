package user_interface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import model.CompareDTO;
import model.ConfigDTO;
import model.DataTransferObject;
import model.HelpDTO;
import model.HelpType;

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
		String stripedCommand = command.trim();

		//next, break command into tokens
		ArrayList<String> tokens = new ArrayList<>(Arrays.asList(stripedCommand.split(" ")));

		//the type of command is determined by the first token
		String commandType = tokens.remove(0);
		if (commandType.equals(HELP_COMMAND)) {
			return parseHelpCommand(tokens);
		} else if (commandType.equals(CONFIG_COMMAND)) {
			return parseConfigCommand(tokens);
		} else if (commandType.equals(COMPARE_COMMAND)){
			return parseCompareCommand(tokens);
		} else { // the command entered is not recognized
			throw new InvalidCommandException("The keyword '" + commandType + "' is not recognized. ");
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
			throw new InvalidCommandException("No value specified for parameter: " + tokens.get(0));
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
				throw new InvalidCommandException("Unrecognized or unexpected additional tokens: " + tokens.subList(2, tokens.size()).toString());
			}
		} else { //the command has unnecessary extra tokens
			throw new InvalidCommandException("Unexpected additional tokens: " + tokens.subList(3, tokens.size()).toString());
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
			throw new InvalidCommandException("Unexpected additional tokens: " + tokens.subList(13, tokens.size()).toString());
		}

		//after checking boundaries, we need to determine filenames and what flags have been set
		//first, get the filenames and data representation, this should be everything before flags
		ArrayList<String> tokensBeforeFlags = new ArrayList<>();
		int i = 0;
		for(; i < tokens.size(); i++){
			if(isTokenCompareFlag(tokens.get(i))) {//is the token is a flag, we have found all the filenames and data representation
				break;
			} else {
				tokensBeforeFlags.add(tokens.get(i));
			}
		}

		//now that we have all filenames and data representation, check the remainder for flags
		for(; i < tokens.size(); i++){
			boolean isAtLastElement = i == tokens.size() - 1;
			if(tokens.get(i).equals(DIVERISTY_METRIC_FLAG)) {// found a pairwise metric flag
				//if a flag is found there should be a next token, and it should not also be a flag
				if(isAtLastElement) {//reached the end of the tokens, so there is no value after the flag
					throw new InvalidCommandException("No metric specified after pairwise flag.");
				}
				i++;
				if(isTokenCompareFlag(tokens.get(i))) {// the next token is a flag, so there is no value after the flag
					throw new InvalidCommandException("No metric specified after pairwise flag.");
				} else { //the next token should be the pairwise metric name
					compare.setPairwiseMethod(tokens.get(i));
				}
			} else if(tokens.get(i).equals(AGGREGATION_METHOD_FLAG)) {//found an aggregation method flag
				if(isAtLastElement) {//reached the end of the tokens, so there is no value after the flag
					throw new InvalidCommandException("No method specified after aggregation flag.");
				}
				i++;
				if(isTokenCompareFlag(tokens.get(i))) {// the next token is a flag, so there is no value after the flag
					throw new InvalidCommandException("No method specified after aggregation flag.");
				} else { //the next token should be the aggregation method name
					compare.setAggregationMethod(tokens.get(i));
				}
			} else if(tokens.get(i).equals(DELIMITER_FLAG)) {//found a delimiter flag
				if(isAtLastElement) {//reached the end of the tokens, so there is no value after the flag
					throw new InvalidCommandException("No delimiter specified after flag.");
				}
				i++;
				if(isTokenCompareFlag(tokens.get(i))) {// the next token is a flag, so there is no value after the flag
					throw new InvalidCommandException("No delimiter specified after flag.");
				} else { //the next token should be the delimiter
					compare.setDelimiter(tokens.get(i));
				}
			} else if(tokens.get(i).equals(NUMBER_THREADS_FLAG)) {//found a flag to set number of threads
				if(isAtLastElement) {//reached the end of the tokens, so there is no value after the flag
					compare.setUseThreadPool(true);
					continue;
				}
				i++;
				if(isTokenCompareFlag(tokens.get(i))) {// the next token is a flag, so there is no value after the flag
					compare.setUseThreadPool(true);
					i--;//go back so we dont skip over a flag
				} else { //the next token should be the number of threads to use
					//must also check if the value is a valid integer
					try {
						Integer numThreads = Integer.parseInt(tokens.get(i));
						compare.setUseThreadPool(true);
						compare.setNumberOfThreads(numThreads);
					} catch (NumberFormatException e) {
						throw new InvalidCommandException("Value specified after number of threads flag is not a number or flag.");
					}
				}
			} else if(tokens.get(i).equals(SAVE_FLAG)) {//found a flag to specify if saving results to a file
				compare.setSave(true);
				if(!isAtLastElement && !isTokenCompareFlag(tokens.get(i+1))) {// the next token is a not flag
					i++;
					compare.setOutputFilename(tokens.get(i));
				}
			} else {//the token is not a flag, and should not be in the command
				throw new InvalidCommandException("Unrecognized token '" + tokens.get(i) + "'.");
			}
		}
		
		/*done iterating over tokens, all that is left is to check the filenames and 
		 * data representation we found. There should be either two filenames and
		 * one data representation, or one filename and one data representation.
		 */
		if (tokensBeforeFlags.size() == 2) {//one filename and one data representation
			compare.setTestCaseLocationOne(tokensBeforeFlags.get(0));
			compare.setDataRepresentation(tokensBeforeFlags.get(1));
		} else if (tokensBeforeFlags.size() == 3) {//two filenames and one data representation
			compare.setTestCaseLocationOne(tokensBeforeFlags.get(0));
			compare.setTestCaseLocationTwo(tokensBeforeFlags.get(1));
			compare.setDataRepresentation(tokensBeforeFlags.get(2));
		} else {// the command is invalid
			throw new InvalidCommandException("Wrong number of filenames or data representation set.");
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
				throw new InvalidCommandException("Help type not valid: " + tokens.get(0) + " ");
			}
		} else {//the command has unnecessary extra tokens
			throw new InvalidCommandException("Unexpected additional tokens: " + tokens.subList(1, tokens.size()).toString() + " ");
		}
		return help;
	}
}

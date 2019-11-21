package capstone;

/**
 * an implementation of a DataRepresentation for comma separated values
 * 
 * @author luke
 *
 */
public class CSV implements DataRepresentation {
	//the test case read from a file to use in comparison
	private String testcase;
	//the end position of the most recently used element in the test case
	private int nextPosition;

	/**Constructor
	 * 
	 * @param s the test case string to iterate over
	 * @throws InvalidFormatException thrown when the supplied string does not match the specified format
	 */
	public CSV(String s) throws InvalidFormatException {
		this.testcase = this.parse(s);
		this.nextPosition = 0;
	}
	
	/**Constructor*/
	public CSV() {
		this.testcase = null;
		this.nextPosition = 0;
	}

	@Override
	public boolean hasNext() {
		return this.testcase.length() > this.nextPosition;
	}

	@Override
	public String next() {
		StringBuffer s = new StringBuffer();
		while(this.hasNext() && this.testcase.charAt(this.nextPosition) != ',') {
			s.append(this.testcase.charAt(this.nextPosition));
			this.nextPosition++;
		}
		this.nextPosition++;
		return s.toString();
	}

	@Override
	public String parse(String s) throws InvalidFormatException {	
		if(this.checkFormat(s)) 
			return s;
		else 
			throw new InvalidFormatException();
	}

	@Override
	public String toString() {
		return this.testcase;
	}

	@Override
	public boolean checkFormat(String s){
		String regex = "(.+?)(?:,\\s*|$)";
		return s.matches(regex);
	}
}

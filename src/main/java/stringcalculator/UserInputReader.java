package stringcalculator;

public class UserInputReader
{
	private String consoleLine;

	public String read()
	{
		final int indexOfFirstQuote = consoleLine.indexOf("'");
		final int indexOfLastQuote = consoleLine.lastIndexOf("'");

		return consoleLine.substring(indexOfFirstQuote+1, indexOfLastQuote);
	}
}

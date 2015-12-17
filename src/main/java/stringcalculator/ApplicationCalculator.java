package stringcalculator;

public class ApplicationCalculator
{

	private final StringCalculator stringCalculator;
	private UserInputReader userInputReader = new UserInputReader();

	public ApplicationCalculator(final StringCalculator stringCalculator)
	{
		this.stringCalculator = stringCalculator;
	}

	public void main()
	{
		for (String userInput = userInputReader.read(); userInput != null; userInput = userInputReader.read()){
			stringCalculator.add(userInput);
		}
	}

	public void setUserInputReader(final UserInputReader userInputReader)
	{
		this.userInputReader = userInputReader;
	}
}

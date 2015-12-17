package stringcalculator;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import stringcalculator.logger.ILogger;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ApplicationCalculatorTest
{
	@Test
	public void givenAValidConsoleLineContainingAListOfNumbersAsString_whenCallingAdd_theItShouldDisplayTheirSumInConsole()
	{
		final StringCalculator stringCalculator = new StringCalculator();
		final ILogger logger = TestUtils.<ILogger>givenStubI(ILogger.class);

		stringCalculator.setLogger(logger);

		final UserInputReader userInputReader = Mockito.mock(UserInputReader.class);
		when(userInputReader.read()).thenReturn("1,2,3", null);

		final ApplicationCalculator applicationCalculator = new ApplicationCalculator(stringCalculator);
		applicationCalculator.setUserInputReader(userInputReader);

		applicationCalculator.main();

		verify(logger, times(1)).write("Sum is 6");
	}

	@Test
	public void givenAValidConsoleLineContainingAListOfNumbersAsString_whenCallingAdd_theItShouldDisplayTheirSumInConsoleAndWaitForAnotherUserInputUntilTheInputIsEmpty()
	{
		final ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);

		final StringCalculator stringCalculator = new StringCalculator();
		final ILogger logger = TestUtils.<ILogger>givenStubI(ILogger.class);

		stringCalculator.setLogger(logger);

		final UserInputReader userInputReader = Mockito.mock(UserInputReader.class);
		when(userInputReader.read()).thenReturn("1,2,3", "1,2,3,4", null);

		final ApplicationCalculator applicationCalculator = new ApplicationCalculator(stringCalculator);
		applicationCalculator.setUserInputReader(userInputReader);

		applicationCalculator.main();

		verify(logger, atLeast(1)).write(argument.capture());
		final List<String> values = argument.getAllValues();

		assertEquals("Sum is 6", values.get(0));
		assertEquals("Sum is 10", values.get(1));

	}
}

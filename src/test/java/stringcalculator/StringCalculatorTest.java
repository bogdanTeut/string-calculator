package stringcalculator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import stringcalculator.exception.LoggingException;
import stringcalculator.logger.ILogger;
import stringcalculator.webservice.IWebServervice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

public class StringCalculatorTest
{

	private StringCalculator stringCalculator;

	@Before
	public void setUp()
	{
		stringCalculator = new StringCalculator();
	}

	@Test
	public void givenAnEmptyString_thenItShouldReturn0()
	{
		assertEquals(0, stringCalculator.add(""));
	}

	@Test
	public void givenAValidNumberAsAString_thenItShouldReturnTheNumber()
	{
		assertEquals(1, stringCalculator.add("1"));
		assertEquals(2, stringCalculator.add("2"));
	}

	@Test
	public void givenAListOfNumbersAsAStringSeparatedByCommas_thenItShouldReturnTheSum()
	{
		assertEquals(3, stringCalculator.add("1,2"));
		assertEquals(10, stringCalculator.add("1,2,3,4"));
	}

	@Test
	public void givenAListOfNumbersAsAStringSeparatedByCommasAndNewLines_thenItShouldReturnTheSum()
	{
		assertEquals(3, stringCalculator.add("1\n2"));
		assertEquals(6, stringCalculator.add("1\n2,3"));
	}

	@Test
	public void givenAListOfNumbersAsAString_whenTheFirstLineContainsTheSeparator_thenItShouldReturnTheSum()
	{
		assertEquals(6, stringCalculator.add("//[***]\n1***2***3"));
	}

	@Test
	public void givenANegativeNumber_thenItShouldThrowAnException()
	{
		try
		{
			stringCalculator.add("-1");
			fail();
		}catch (IllegalArgumentException exception)
		{
			assertEquals("negatives not allowed: [-1]", exception.getMessage());
		}
	}

	@Test
	public void givenAListOfNumbersAsAString_whenOneOfThemISBiggerThan1000_thenItShouldReturnTheSum()
	{
		assertEquals(6, stringCalculator.add("1,2,1001,3"));
	}

	@Test
	public void givenAListOfNumbersAsAString_whenTheFirstLineContainsMultipleSeparators_thenItShouldReturnTheSum()
	{
		assertEquals(6, stringCalculator.add("//[*][%]\n1*2%3"));
	}

	@Test
	public void givenAListOfNumbersAsString_whenCalculatingTheirSum_thenTheResultShouldBeLogged()
	{
		final ILogger logger = TestUtils.<ILogger>givenStubI(ILogger.class);
		stringCalculator.setLogger(logger);

		assertEquals(10, stringCalculator.add("1,2,3,4"));
		Mockito.verify(logger, times(1)).write("Sum is 10");
	}

	@Test
	public void givenAListOfNumbersAsString_whenCalculatingTheirSumAndLoggingServiceThrowsExceptions_thenWebServiceShouldBeNoticed()
	{
		final ILogger logger = TestUtils.<ILogger>givenStubI(ILogger.class);
		stringCalculator.setLogger(logger);

		final IWebServervice webServervice = TestUtils.<IWebServervice>givenStubI(IWebServervice.class);
		stringCalculator.setIWebServervice(webServervice);

		doThrow(new LoggingException("Logging failed")).when(logger).write(anyString());

		stringCalculator.add("1,2,3,4");
		Mockito.verify(webServervice, times(1)).nortifyException("Logging failed");
	}

}

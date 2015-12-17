package stringcalculator;

import org.mockito.Mockito;

public class TestUtils
{
	public static <T>T givenStubI(final Class<T> type)
	{
		final T logger = Mockito.mock(type);
		return logger;
	}

}

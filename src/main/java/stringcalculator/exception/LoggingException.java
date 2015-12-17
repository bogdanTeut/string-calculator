package stringcalculator.exception;

public class LoggingException extends RuntimeException
{
	private final String message;

	public LoggingException(final String message)
	{
		this.message = message;
	}

	@Override
	public String getMessage()
	{
		return message;
	}
}

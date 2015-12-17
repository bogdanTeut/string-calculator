package stringcalculator;

import stringcalculator.logger.ILogger;
import stringcalculator.logger.impl.Logger;
import stringcalculator.webservice.IWebServervice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringCalculator
{
	private static final Pattern PATTERN = Pattern.compile("\\[([^\\]]*)\\]");
	private ILogger logger = new Logger();
	private IWebServervice iWebServervice;

	public int add(final String numbers)
	{
		if (numbers.trim().length() == 0)
		{
			return 0;
		}

		final List<String> numbersArrayAfterFirstSplit = splitAfterRegex(numbers, "\n");

		final List<String> regexArray = getSplittingRegex(numbersArrayAfterFirstSplit);

		final List<String> numbersArray = splitAfterRegexAndFlattenize(numbersArrayAfterFirstSplit, regexArray);

		checkForNullValue(numbersArray);

		final int sum = calculateSum(numbersArray);
		try
		{
			logger.write(String.format("Sum is %s", sum));
		}catch (final Exception ex)
		{
			iWebServervice.nortifyException(ex.getMessage());
		}

		return calculateSum(numbersArray);
	}

	private void checkForNullValue(final List<String> numbersArray)
	{
		numbersArray
				.stream()
				.forEach(number -> {
					if (Integer.valueOf(number) < 0)
						throw new IllegalArgumentException(String.format("negatives not allowed: [%s]", number));
				});
	}

	private List<String> getSplittingRegex(final List<String> numbersArrayAfterFirstSplit)
	{
		final List<String> regexArray = new ArrayList<>();

		final Matcher matcher = PATTERN.matcher(numbersArrayAfterFirstSplit.get(0));

		while (matcher.find())
		{
			regexArray.add(matcher.group(1));
		}

		if (regexArray.size() == 0)
		{
			regexArray.add(",");
		}
		return regexArray;
	}

	private int calculateSum(final List<String> numbersArray)
	{
		return numbersArray
					.stream()
					.mapToInt(number -> Integer.valueOf(number))
					.filter(number -> number < 1001)
					.sum();
	}

	private List<String> splitAfterRegex(final String numbers, final String regex)
	{
		return Arrays
			.stream(numbers.split(regex))
			.collect(Collectors.toList());
	}

	private List<String> splitAfterRegexAndFlattenize(final List<String> numbersArrayAfterFirstSplit, final List<String> regexArray)
	{

		return numbersArrayAfterFirstSplit
			.stream()
			.filter(number -> !number.contains("//"))
			.flatMap(number -> flattenizeStream(regexArray, number))
			.collect(Collectors.toList());
	}

	private Stream<String> flattenizeStream(final List<String> regexArray, final String number)
	{
		final Reference<List<String>> resultsRef = new Reference<>();
		final List<String> results = new ArrayList<>();
		results.add(number);
		resultsRef.setReference(results);

		regexArray
			.stream()
			.forEach(regex ->
			{
				final List<String> eachRegexSplit = resultsRef.getReference()
						.stream()
						.flatMap(s -> Arrays.stream(s.split( Pattern.quote(regex))))
						.collect(Collectors.toList());

				resultsRef.setReference(eachRegexSplit);
			});

			return resultsRef.getReference().stream();

	}

	public void setLogger(final ILogger logger)
	{
		this.logger = logger;
	}

	public void setIWebServervice(final IWebServervice iWebServervice)
	{
		this.iWebServervice = iWebServervice;
	}

	static class Reference<T>
	{
		private T reference;

		public T getReference()
		{
			return reference;
		}

		public void setReference(final T reference)
		{
			this.reference = reference;
		}
	}
}

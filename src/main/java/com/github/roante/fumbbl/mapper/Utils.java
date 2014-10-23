package com.github.roante.fumbbl.mapper;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Utils {
	private static final Logger logger = LoggerFactory.getLogger(Utils.class);

	private Utils() {
	}

	/**
	 * The input should be in the format <code>-coachId=1234</code>.
	 * 
	 * @param coachIdParam
	 *            the input in the format above; cannot be <code>null</code>
	 * @return the parsed <i>coachId</i> as a <code>long</code>
	 */
	public static long getCoachId(final String coachIdParam) {
		Validate.notNull(coachIdParam, "coachId cannot be null");

		if (!coachIdParam.startsWith("-coachId=")) {
			throw new IllegalArgumentException(
					"Invalid coachId parameter, the format should be -coachId=1234");
		}

		final String coachId = coachIdParam.substring(coachIdParam
				.indexOf("-coachId=") + 9);

		logger.info("Parsed coachId is {}", coachId);

		try {
			return Integer.parseInt(coachId);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Invalid coachId parameter, the format should be -coachId=1234. Parsed coachId was "
							+ coachId);
		}
	}

	public static String parseRunTime(final long millis) {
		return String.format(
				"%d min, %d sec",
				TimeUnit.MILLISECONDS.toMinutes(millis),
				TimeUnit.MILLISECONDS.toSeconds(millis)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(millis)));
	}
}

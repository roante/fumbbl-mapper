package com.github.roante.fumbbl.mapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountryNameToCodeMatcher {
	private static final Logger logger = LoggerFactory
			.getLogger(CountryNameToCodeMatcher.class);
	private final Map<String, String> countries = new HashMap<>();

	public CountryNameToCodeMatcher() {
		Arrays.stream(Locale.getISOCountries()).forEach(
				iso -> countries.put(new Locale("", iso).getDisplayCountry(),
						iso));
	}

	public String getCountryCodeForCountryName(final String countryName)
			throws NoCountryCodeFoundException {
		if (StringUtils.isEmpty(countryName)) {
			throw new NoCountryCodeFoundException();
		}

		final String ret = countries.get(countryName);

		if (null == ret) {
			throw new NoCountryCodeFoundException();
		}

		logger.info("Mapping county {} to code {}", countryName, ret);
		return ret;
	}
}

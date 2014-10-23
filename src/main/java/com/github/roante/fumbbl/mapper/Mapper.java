package com.github.roante.fumbbl.mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Mapper {
	private static final Logger logger = LoggerFactory.getLogger(Mapper.class);

	public Set<String> getOpponentIds(final long coachId) {
		final HashSet<String> ret = new HashSet<String>();
		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			final DocumentBuilder builder = factory.newDocumentBuilder();

			final XPathFactory xPathfactory = XPathFactory.newInstance();
			final XPath xpath = xPathfactory.newXPath();
			final XPathExpression expr = xpath.compile("//coach/@id");

			for (int matchesPage = 0;; ++matchesPage) {
				final int startIndex = 1 + matchesPage * 26;
				logger.info("Parsing matches page {}, matches {}-{}...",
						matchesPage, startIndex, startIndex + 25);
				final Document doc = builder
						.parse("http://fumbbl.com/xml:matches?c=" + coachId
								+ "&p=" + matchesPage);
				final NodeList attributes = (NodeList) expr.evaluate(doc,
						XPathConstants.NODESET);

				if (0 == attributes.getLength()) {
					break;
				}

				for (int j = 0; j < attributes.getLength(); ++j) {
					ret.add(attributes.item(j).getNodeValue());
				}
			}
		} catch (XPathExpressionException | ParserConfigurationException
				| SAXException | IOException e) {
			e.printStackTrace();
		}

		logger.info("Found opponent ids {}", ret);
		return ret;
	}

	public Set<String> getCountryNames(final Set<String> opponentIds) {
		final ConcurrentSkipListSet<String> ret = new ConcurrentSkipListSet<String>();
		final Pattern pattern = Pattern.compile("<b>Location:</b>(.+?)<br />");
		opponentIds
				.parallelStream()
				.forEach(
						coachId -> {
							try (BufferedReader in = new BufferedReader(
									new InputStreamReader(new URL(
											"https://fumbbl.com/FUMBBL.php?page=coachinfo&coach="
													+ coachId).openStream()))) {
								String line = null;
								while ((line = in.readLine()) != null) {
									final Matcher matcher = pattern
											.matcher(line);
									if (matcher.find()) {
										final String location = matcher
												.group(1);
										final String country = tryFiguringOutCountry(location);
										logger.info(
												"Coach {} has location {}, identified country {}",
												coachId, location, country);

										if (country != null) {
											ret.add(country);
										}
									}
								}
							} catch (final IOException e) {
								e.printStackTrace();
							}
						});
		logger.info("Found countries {}", ret);
		return ret;
	}

	protected String tryFiguringOutCountry(final String location) {
		if (StringUtils.isEmpty(location)) {
			return null;
		}

		if (location.contains(",")) {
			return location.substring(location.lastIndexOf(',') + 1,
					location.length()).trim();
		}

		if (location.contains("(")) {
			return location.substring(location.lastIndexOf('(') + 1,
					location.lastIndexOf(')'));
		}

		return location.trim();
	}

	public Set<String> getCountryCodes(final Set<String> countryNames) {
		final CountryNameToCodeMatcher matcher = new CountryNameToCodeMatcher();

		final HashSet<String> ret = new HashSet<String>();

		for (final String countryName : countryNames) {
			try {
				ret.add(matcher.getCountryCodeForCountryName(countryName));
			} catch (final Exception e) {
				logger.error("Cannot determine country code for country {}",
						countryName);
			}
		}

		logger.info("Found country codes " + ret);
		return ret;
	}

	public String joinCountryNames(final Set<String> countryNames) {
		Validate.notNull(countryNames, "countryNames cannot be null");
		if (countryNames.isEmpty()) {
			return "";
		}

		final StringJoiner sj = new StringJoiner("','");
		countryNames.stream().forEach(code -> sj.add(code));
		return '\'' + sj.toString() + '\'';
	}

	public URI createOutputMapWithCountries(final long coachId,
			final String countryCodes) {
		final OutputWriter writer = new OutputWriter();
		return writer.write(coachId, countryCodes);
	}
}

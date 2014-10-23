package com.github.roante.fumbbl.mapper;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	private static final String COACH_ID_ROANTE = "110917";

	public static void main(final String[] args) {
		printHeader();

		if (args.length != 1) {
			logger.error("Invalid input, please specify the coachId parameter");
			usage();
			return;
		}

		final String coachIdParam = args[0];

		try {
			long time = -System.currentTimeMillis();
			final long coachId = Utils.getCoachId(coachIdParam);
			logger.info("Parsing games for coach " + coachId);

			final Mapper mapper = new Mapper();
			final Set<String> opponentIds = mapper.getOpponentIds(coachId);
			final Set<String> countryNames = mapper
					.getCountryNames(opponentIds);
			final Set<String> countryCodes = mapper
					.getCountryCodes(countryNames);
			final String countryCodeList = mapper
					.joinCountryNames(countryCodes);
			final URI mapFileURI = mapper.createOutputMapWithCountries(coachId,
					countryCodeList);

			time += System.currentTimeMillis();
			printFooter(time);
			openBrowser(mapFileURI);
		} catch (final IllegalArgumentException e) {
			logger.error("Cannot parse coachId from input {}", coachIdParam);
		}
	}

	private static void printHeader() {
		logger.info("        _,--',   _._.--._____");
		logger.info(" .--.--';_'-.', \";_      _.,-'");
		logger.info(".'--'.  _.'    {`'-;_ .-.>.'");
		logger.info("      '-:_      )  / `' '=.");
		logger.info("        ) >     {_/,     /~)");
		logger.info("        |/               `^ .'");
		logger.info("");
		logger.info("FUMBBL Mapper - by roante");
		logger.info("Check out some other cool useless stuff here!");
		logger.info("                    https://github.com/roante");
		logger.info("---------------------------------------------");
	}

	private static void usage() {
		logger.info("Usage: java -jar xxx.jar -coachId {}", COACH_ID_ROANTE);
		logger.info("");
		logger.info("How to find your coachId?");
		logger.info("-------------------------");
		logger.info("Go to https://fumbbl.com/xml:teams?coach=roante and check the first line: <teams coach=\"YOUR_COACHID_HERE\">");
	}

	private static void printFooter(final long time) {
		logger.info("=================================");
		logger.info("   Done! Run took " + Utils.parseRunTime(time) + "");
		logger.info("=================================");
	}

	private static void openBrowser(final URI mapFileURI) {
		if (Desktop.isDesktopSupported()) {
			try {
				logger.info(
						"Opening generated map {} in your default browser...",
						mapFileURI);
				Desktop.getDesktop().browse(mapFileURI);
			} catch (final IOException e) {
				logger.error(
						"Cannot open browser, sry. You should find the generated map here: "
								+ mapFileURI, e);
			}
		}
	}
}

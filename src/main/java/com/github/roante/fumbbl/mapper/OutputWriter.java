package com.github.roante.fumbbl.mapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Locale;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class OutputWriter {
	private final Configuration cfg = new Configuration(
			Configuration.VERSION_2_3_21);

	public OutputWriter() {
		cfg.setClassForTemplateLoading(OutputWriter.class, "/");
		cfg.setDefaultEncoding("UTF-8");
		cfg.setLocale(Locale.US);
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
	}

	public URI write(final long coachId, final String countryCodesSeparatedList) {
		final HashMap<String, String> root = new HashMap<>();
		root.put("countryCodesSeparatedList", countryCodesSeparatedList);

		final File file = new File("map-for-coach-" + coachId + ".html");
		try (FileWriter writer = new FileWriter(file)) {
			final Template template = cfg.getTemplate("map.ftl");
			template.process(root, writer);
		} catch (final IOException | TemplateException e) {
			e.printStackTrace();
		}

		return file.toURI();
	}
}

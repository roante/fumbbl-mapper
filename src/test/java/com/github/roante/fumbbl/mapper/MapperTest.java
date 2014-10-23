package com.github.roante.fumbbl.mapper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MapperTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	private final Mapper mapper = new Mapper();

	@Test
	public void tryFiguringOutCountry_nullInput() throws Exception {
		final String res = mapper.tryFiguringOutCountry(null);
		assertThat(res, is(nullValue()));
	}

	@Test
	public void tryFiguringOutCountry_emptyInput() throws Exception {
		final String res = mapper.tryFiguringOutCountry("");
		assertThat(res, is(nullValue()));
	}

	@Test
	public void tryFiguringOutCountry_simpleInput() throws Exception {
		final String res = mapper.tryFiguringOutCountry("France");
		assertThat(res, equalTo("France"));
	}

	@Test
	public void tryFiguringOutCountry_commaSeparatedCountry() throws Exception {
		final String res = mapper.tryFiguringOutCountry("Budapest, Hungary");
		assertThat(res, equalTo("Hungary"));
	}

	@Test
	public void tryFiguringOutCountry_countryInParentheses() throws Exception {
		final String res = mapper
				.tryFiguringOutCountry("Deathwater (Middenheim)");
		assertThat(res, equalTo("Middenheim"));
	}

	@Test
	public void getCountryCodes_emptyInput() throws Exception {
		final Set<String> res = mapper.getCountryCodes(Collections.emptySet());
		assertThat(res, equalTo(Collections.emptySet()));
	}

	@Test
	public void getCountryCodes_simpleInput() throws Exception {
		final Set<String> res = mapper.getCountryCodes(setOf("France",
				"Hungary"));
		assertThat(res, equalTo(setOf("FR", "HU")));
	}

	@Test
	public void joinCountryNames_nullInput() throws Exception {
		exception.expect(NullPointerException.class);
		exception.expectMessage(equalTo("countryNames cannot be null"));

		mapper.joinCountryNames(null);
	}

	@Test
	public void joinCountryNames_emptyInput() throws Exception {
		final String res = mapper.joinCountryNames(Collections.emptySet());
		assertThat(res, equalTo(""));
	}

	@Test
	public void joinCountryNames_singleInput() throws Exception {
		String ret = mapper.joinCountryNames(setOf("HU"));
		assertThat(ret, equalTo("'HU'"));
	}

	@Test
	public void joinCountryNames_sampleInput() throws Exception {
		Set<String> sortedSet = new TreeSet<String>(setOf("HU", "FR", "XY"));
		String ret = mapper.joinCountryNames(sortedSet);
		assertThat(ret, equalTo("'FR','HU','XY'"));
	}

	private Set<String> setOf(final String... arr) {
		return Stream.of(arr).collect(Collectors.toSet());
	}
}

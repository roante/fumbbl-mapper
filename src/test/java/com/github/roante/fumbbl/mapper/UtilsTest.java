package com.github.roante.fumbbl.mapper;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class UtilsTest {
	private static final int SECOND = 1000;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void getCoachId_nullParam() throws Exception {
		exception.expect(NullPointerException.class);
		exception.expectMessage(equalTo("coachId cannot be null"));
		Utils.getCoachId(null);
	}

	@Test
	public void getCoachId_invalidParamWithoutEquals() throws Exception {
		exception.expect(IllegalArgumentException.class);
		exception
				.expectMessage(equalTo("Invalid coachId parameter, the format should be -coachId=1234"));
		Utils.getCoachId("some_stuff:123");
	}

	@Test
	public void getCoachId_notIdInput() throws Exception {
		exception.expect(IllegalArgumentException.class);
		exception
				.expectMessage(equalTo("Invalid coachId parameter, the format should be -coachId=1234. Parsed coachId was william"));
		Utils.getCoachId("-coachId=william");

	}

	@Test
	public void getCoachId_okInput() throws Exception {
		final long coachId = Utils.getCoachId("-coachId=1234");
		assertThat(coachId, equalTo(1234L));
	}

	@Test
	public void parseRunTime_0() throws Exception {
		String res = Utils.parseRunTime(0);
		assertThat(res, equalTo("0 min, 0 sec"));
	}

	@Test
	public void parseRunTime_1_30() throws Exception {
		String res = Utils.parseRunTime(90 * SECOND);
		assertThat(res, equalTo("1 min, 30 sec"));
	}

	@Test
	public void parseRunTime_100_mins() throws Exception {
		String res = Utils.parseRunTime(100 * 60 * 1000);
		assertThat(res, equalTo("100 min, 0 sec"));
	}
}

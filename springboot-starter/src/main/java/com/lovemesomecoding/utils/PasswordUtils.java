package com.lovemesomecoding.utils;

import javax.management.RuntimeErrorException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author fkaveinga
 *
 */
public final class PasswordUtils {

	private static final int TEMP_PASSWORD_LENGTH = 10;
	final static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	private PasswordUtils() {
	}

	/*
	 * Password for development only
	 */
	public static String hashPassword(final String password) {
		if (password == null || password.length() < 1) {
			throw new RuntimeErrorException(null, "Password must not be valid");
		}
		return passwordEncoder.encode(password);
	}

	public static boolean verify(String password, String hashPassword) {
		return passwordEncoder.matches(password, hashPassword);
	}

	public static String getRandomTempPassword() {
		return RandomGeneratorUtils.getAlphaNumeric(TEMP_PASSWORD_LENGTH);
	}

}

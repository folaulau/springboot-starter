package com.lovemesomecoding.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlUtils {

	private static Logger log = LoggerFactory.getLogger(SqlUtils.class);

	/**
	 * NestedRuntimeException e -> error = e.getMostSpecificCause().getMessage()
	 * @param error
	 * @return
	 */
	
	public static String extractSqlError(String error) {
		log.info("error: {}", error);
		final String DUPLICATE_ENTRY = "Duplicate entry";
		final String MULTIPLE_REPRESENTATIONS = "Multiple representations";
		StringBuilder result = new StringBuilder();
		if (error.contains(DUPLICATE_ENTRY)) {
			log.info("{} issue.", DUPLICATE_ENTRY);
			int firstQuote = error.indexOf("'");
			int secondQuote = error.indexOf("'", DUPLICATE_ENTRY.length() + 2);
			log.info("{}, {}", firstQuote, secondQuote);
			result.append(error.substring(firstQuote + 1, secondQuote) + " is taken already");
		} else if (error.contains(MULTIPLE_REPRESENTATIONS)) {
			log.info("{} issue.", MULTIPLE_REPRESENTATIONS);
			// int firstQuote = error.indexOf("'");
			// int secondQuote = error.indexOf("'", DUPLICATE_ENTRY.length()+2);
			// log.info("{}, {}",firstQuote, secondQuote);
			// result.append(error.substring(firstQuote+1, secondQuote)+" taken already");
			result.append(MULTIPLE_REPRESENTATIONS);
		}

		return result.toString();
	}
}

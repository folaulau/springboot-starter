package com.lovemesomecoding.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtils {

	// Millisecond Based
	public static final long MILLISECOND = 1000L;
	public static final long SECOND = MILLISECOND;
	public static final long MINUTE = SECOND * 60;
	public static final long HOUR = MINUTE * 60;
	public static final long DAY = HOUR * 24;
	public static final long WEEK = DAY * 7;
	
	public static final int NUMBER__OF_DAYS_TIL_START_COVERAGE = 13;
	
	public static final String UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	
	public static final String MONTH_DATE_YEAR = "MMMM dd, yyyy";
	
	public static final String DAYOFWEEK_MONTH_DATE_YEAR = "E, MMMM dd, yyyy";
	
	/**
	 * Based on Milliseconds
	 * @param hr
	 * @return hours in milliseconds
	 */
	public static long getHoursInMilliseconds(long hr) {
		return HOUR * hr;
	}
	
	public static String getTimeStamp() {
		DateFormat formmatter = new SimpleDateFormat("M-dd-yyyy h:mm:ss a");
		return formmatter.format(new Date());
	}
	
	public static String getFormattedDate(Date date, String format) {
		DateFormat formmatter = new SimpleDateFormat(format);
		return formmatter.format(date);
	}
	
	public static int getDiffMonths(Date start, Date end) {
		LocalDate startDate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate endDate = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		Period period = Period.between(startDate,endDate);
		
		int numOfYears = period.getYears();
		
		int numOfMonths = period.getMonths();
		
		if(numOfYears>=1) {
			numOfMonths += (numOfYears*12);
		}
		return numOfMonths;
	}

	public static int getDiffHours(Date start, Date end) {
		long different = end.getTime() - start.getTime();
		return (int) (different/HOUR);
	}

	public static int getDiffDays(Date start, Date end) {
		long different = end.getTime() - start.getTime();
		return (int) (different/DAY);
	}
	
	public static String getFormattedDateTime(Date date) {
		DateFormat formmatter = new SimpleDateFormat("M-dd-yyyy h:mm:ss.SSS a");
		return formmatter.format(date);
	}
	
	public static String getFormattedCurrentDateTime() {
		DateFormat formmatter = new SimpleDateFormat("M-dd-yyyy h:mm:ss.SSS a");
		return formmatter.format(new Date());
	}
	
	public static String getUTCFormattedDateTime(Date date) {
		DateFormat formmatter = new SimpleDateFormat(UTC_FORMAT);
		return formmatter.format(date);
	}
	
	public static String getDOBAsText(Date dob) {
		DateFormat formmatter = new SimpleDateFormat("yyyy-MM-dd");
		return formmatter.format(dob);
	}
	
	public static Date getLastSecondOfDayDateTime(Date date) {
		Calendar startDateCalendar = Calendar.getInstance();
		startDateCalendar.setTime(date);
		
		startDateCalendar.set(Calendar.HOUR_OF_DAY, startDateCalendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		startDateCalendar.set(Calendar.MINUTE, startDateCalendar.getActualMaximum(Calendar.MINUTE));
		startDateCalendar.set(Calendar.SECOND, startDateCalendar.getActualMaximum(Calendar.SECOND));
		startDateCalendar.set(Calendar.MILLISECOND, startDateCalendar.getActualMaximum(Calendar.MILLISECOND));

		return startDateCalendar.getTime();
	}
	
	public static Date getFirstSecondOfDayDateTime(Date date) {
		Calendar startDateCalendar = Calendar.getInstance();
		startDateCalendar.setTime(date);
		
		startDateCalendar.set(Calendar.HOUR_OF_DAY, startDateCalendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		startDateCalendar.set(Calendar.MINUTE, startDateCalendar.getActualMinimum(Calendar.MINUTE));
		startDateCalendar.set(Calendar.SECOND, startDateCalendar.getActualMinimum(Calendar.SECOND));
		startDateCalendar.set(Calendar.MILLISECOND, startDateCalendar.getActualMinimum(Calendar.MILLISECOND));

		return startDateCalendar.getTime();
	}
	
	public static boolean isValidStartDate(Date startDate) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		
		Date now = new Date();
		
		int numOfDays = DateTimeUtils.getDiffDays(now, startDate);
		
		return (numOfDays>=NUMBER__OF_DAYS_TIL_START_COVERAGE) ? true : false;
	}
	
	public static Date getBeginingOfDate(Date date) {
		Calendar startDateCalendar = Calendar.getInstance();
		startDateCalendar.setTime(date);

		startDateCalendar.set(Calendar.HOUR_OF_DAY, startDateCalendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		startDateCalendar.set(Calendar.MINUTE, startDateCalendar.getActualMinimum(Calendar.MINUTE));
		startDateCalendar.set(Calendar.SECOND, startDateCalendar.getActualMinimum(Calendar.SECOND));
		startDateCalendar.set(Calendar.MILLISECOND, startDateCalendar.getActualMinimum(Calendar.MILLISECOND));

		return startDateCalendar.getTime();
	}
	
	public static Date getEndingOfDate(Date date) {
		Calendar endDateCalendar = Calendar.getInstance();
		endDateCalendar.setTime(date);

		endDateCalendar.set(Calendar.HOUR_OF_DAY, endDateCalendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		endDateCalendar.set(Calendar.MINUTE, endDateCalendar.getActualMaximum(Calendar.MINUTE));
		endDateCalendar.set(Calendar.SECOND, endDateCalendar.getActualMaximum(Calendar.SECOND));
		endDateCalendar.set(Calendar.MILLISECOND, endDateCalendar.getActualMaximum(Calendar.MILLISECOND));

		return endDateCalendar.getTime();
	}
	
	public static int calculateAge(Date birthDate) {
		if (birthDate == null) {
			return 0;
		}
		Calendar birth = Calendar.getInstance();
		birth.setTime(birthDate);
		Calendar today = Calendar.getInstance();

		int yearDifference = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR);

		if (today.get(Calendar.MONTH) < birth.get(Calendar.MONTH)) {
			yearDifference--;
		} else {
			if (today.get(Calendar.MONTH) == birth.get(Calendar.MONTH)
					&& today.get(Calendar.DAY_OF_MONTH) < birth.get(Calendar.DAY_OF_MONTH)) {
				yearDifference--;
			}
		}
		return yearDifference < 0 ? 0 : yearDifference;
	}
}

package es.ewic.backend.modelutil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

	public static SimpleDateFormat sdfLong = new SimpleDateFormat("HH:mm dd/MM/yyy");

	public static Calendar setHourMinuteZero(Calendar date) {
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
		return date;
	}

	/**
	 * 
	 * @param date1
	 * @param date2
	 * @return the value 0 if the time represented by the date1 is equal to date2; a
	 *         value less than 0 if the time of date1 is before the time represented
	 *         by date2; and a value greater than 0 if the time of date1 is after
	 *         the time of date2.
	 */
	public static int compareDays(Calendar date1, Calendar date2) {
		Calendar date1Aux = new GregorianCalendar();
		date1Aux.setTime(date1.getTime());
		date1Aux = setHourMinuteZero(date1Aux);
		Calendar date2Aux = new GregorianCalendar();
		date2Aux.setTime(date2.getTime());
		date2Aux = setHourMinuteZero(date2Aux);
		return date1Aux.compareTo(date2Aux);
	}

	/**
	 * Fast method to compare if two dates are equals using their Get methods
	 * without use Calendars.
	 * 
	 * @param date1
	 * @param date2
	 * 
	 * @return the value true if the time represented by the date1 is equal to
	 *         date2; in other case return the value false. Not compare if the time
	 *         between dates is before or after; for that, use compareDays method.
	 */
	public static boolean compareDaysByGet(Calendar date1, Calendar date2) {
		boolean isEqual = date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)
				&& date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH)
				&& date1.get(Calendar.DATE) == date2.get(Calendar.DATE);
		return isEqual;
	}

	/**
	 * Method to calculate minutes of difference between two dates
	 * 
	 * @param dateFrom
	 * @param dateTo
	 * 
	 * @return the difference in minutes
	 */
	public static long getMinutesDifference(Calendar dateFrom, Calendar dateTo) {
		Date initDate = dateFrom.getTime();
		Date endDate = dateTo.getTime();
		return (endDate.getTime() - initDate.getTime()) / 1000 / 60;
	}

}

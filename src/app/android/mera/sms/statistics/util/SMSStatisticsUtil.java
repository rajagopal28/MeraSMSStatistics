package app.android.mera.sms.statistics.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import app.android.mera.sms.statistics.constants.SMSStatsisticsContants;

public class SMSStatisticsUtil {
	
	public static String getCurrentDateAsString() {
		Calendar checkCalendar = new GregorianCalendar();
		SimpleDateFormat formatter = new SimpleDateFormat(SMSStatsisticsContants.SMS_STATISTICS_DATE_FORMAT);
		return formatter.format(checkCalendar.getTime());
	}
	
	public static List<String> getCurrentWeekDatesList() {
		List<String> datesList = new ArrayList<String>();
		Calendar checkCalendar = new GregorianCalendar();
		SimpleDateFormat formatter = new SimpleDateFormat(SMSStatsisticsContants.SMS_STATISTICS_DATE_FORMAT);
		checkCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);	
		datesList.add(formatter.format(checkCalendar.getTime()));
		for(int i =1; i<7 ; i++) {
			checkCalendar.add(Calendar.DAY_OF_WEEK, 1);
			datesList.add(formatter.format(checkCalendar.getTime()));
		}
		return datesList;
	}	
	public static List<String> getCurrentMonthDatesList() {
		List<String> datesList = new ArrayList<String>();
		Calendar checkCalendar = new GregorianCalendar();
		SimpleDateFormat formatter = new SimpleDateFormat(SMSStatsisticsContants.SMS_STATISTICS_DATE_FORMAT);
		int dayOfMonth = checkCalendar.get(Calendar.DAY_OF_MONTH);
		checkCalendar.set(Calendar.DATE, 1);	
		datesList.add(formatter.format(checkCalendar.getTime()));
		for(int i =1; i<dayOfMonth ; i++) {
			checkCalendar.add(Calendar.DAY_OF_MONTH, 1);
			datesList.add(formatter.format(checkCalendar.getTime()));
		}
		return datesList;
	}
	
}

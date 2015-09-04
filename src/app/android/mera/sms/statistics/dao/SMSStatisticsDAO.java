package app.android.mera.sms.statistics.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;
import app.android.mera.sms.statistics.bean.SMSStatisticsBean;
import app.android.mera.sms.statistics.constants.SMSStatsisticsContants;
import app.android.mera.sms.statistics.exception.SMSStatisticHandledException;
import app.android.mera.sms.statistics.util.OpenHelper;
import app.android.mera.sms.statistics.util.SMSStatisticsUtil;


public class SMSStatisticsDAO {
	 private static final String DATABASE_NAME = "statistics.db";
	   private static final int DATABASE_VERSION = 1;
	   private static final String TABLE_NAME = "smsstatistics";
	   private Context context;
	   private SQLiteDatabase db;
	   private SQLiteStatement insertStmt;
	   private static final String INSERT = "insert into " + TABLE_NAME + "(smscount,date,type,lastsentid) values (1,?,?,?)";
	   public SMSStatisticsDAO(Context context) {
	      this.context = context;
	      OpenHelper openHelper = new OpenHelper(this.context, DATABASE_NAME, DATABASE_VERSION, TABLE_NAME);
	      try
	      {
	    	  this.db = openHelper.getWritableDatabase();
	          this.insertStmt = this.db.compileStatement(INSERT);
	      }
	      catch(Exception ex)
	      {
	    	  Toast.makeText(this.context,"Exception in constructor "+ex.toString(),Toast.LENGTH_LONG).show();
	    	  ex.printStackTrace();
	      }
	     
	   }
	   
	/**
	 * Inserts or updates the SMS count of given date
	 * 
	 * @param dateToCheck
	 * @return isSuccess
	 * @throws SMSStatisticHandledException
	 */
	public boolean insertOrUpdateSMSCountOfDate(String dateToCheck, String smsType, int lastSentId) throws SMSStatisticHandledException {
		   boolean isSuccess = false;
		   int obtainedCount = 0;
		   List<Integer> result = getStatisticOnDates(Arrays.asList(dateToCheck), smsType);
		   if(!result.isEmpty()) {
			   obtainedCount = result.get(0);
		   }
		   if( obtainedCount != 0 ) {
			  // means there exists an entry in the database for this date 
			  // so update
			   Log.i("SMSStatistisDAO", "Inside Update block. Subsequent time msg of a day");
			 isSuccess = updateCountOnDate(dateToCheck, smsType, obtainedCount, lastSentId);
		   } else {
			   // there is no record for the given date so insert new column
			   try {				   
				   Log.i("SMSStatistisDAO", "Inside Insertion block. First time msg");
				   this.insertStmt.bindString(1, dateToCheck);
				   this.insertStmt.bindString(2, smsType);
				   this.insertStmt.bindLong(3, lastSentId);
				   long newRowId = this.insertStmt.executeInsert();
				   isSuccess = newRowId != -1;	// -1 indicates insertion failed		  
			   } catch(Exception ex) {
			    	 Log.w("Insertion", ex.fillInStackTrace());
			    	 throw new SMSStatisticHandledException(ex);
			   }
		   }
		   return isSuccess;
	   }
	   
	/**
	 * Returns the SMS count on the given date
	 * 
	 * @param dateToCheck
	 * @return msgCount
	 * @throws SMSStatisticHandledException
	 */
	public List<Integer> getStatisticOnDates(List<String> dateToCheck, String smsType) throws SMSStatisticHandledException {
		List<Integer> msgCountList = new ArrayList<Integer>();
		try {
			String selection = "type LIKE ? and date in (";
			for(int i=0; i< dateToCheck.size(); i++) { 
				if(i != 0 ) {
					selection+=", ";
				}
				selection += "?";
			}
			selection += ")";
	      String[] selectionBindings = new String[dateToCheck.size()+1];
	      selectionBindings[0] = smsType;
	      for(int index = 0 ; index< dateToCheck.size(); index++) {
	    	  selectionBindings[index+1] = dateToCheck.get(index);
	      }
		Cursor cursor = this.db.query(TABLE_NAME, new String[] { "smscount" },
	        selection, selectionBindings , null, null, "id asc");
	      if (cursor.moveToFirst()) {
	         do {
	        	 msgCountList.add(cursor.getInt(0));// parameterIndex starts at 0
	         } while (cursor.moveToNext());
	      }
	      if (cursor != null && !cursor.isClosed()) {
	         cursor.close();
	      }
	     } catch(Exception ex) {
	    	 Log.w("Selection", ex.fillInStackTrace());
	    	 throw new SMSStatisticHandledException(ex);
	     }
		return msgCountList;
	}
	/**
	 * This method updates the count and returns whether any tows affected due to this change
	 * 
	 * @param dateToCheck
	 * @param previousCount
	 * @return isSuccess
	 * @throws SMSStatisticHandledException
	 */
	public boolean updateCountOnDate(String dateToCheck, String smsType, int previousCount, int lastSentId) throws SMSStatisticHandledException {
		int rowsAffected = 0;
		try {
			   ContentValues myValues=new ContentValues();
			   myValues.put("smscount", previousCount+1);
			   myValues.put("lastsentid", lastSentId);
			   rowsAffected = this.db.update(TABLE_NAME, myValues, "date LIKE ? and type = ? ", new String[]{dateToCheck, smsType});
		   } catch(Exception ex) {
			   Log.w("Updation", ex.fillInStackTrace());
			   throw new SMSStatisticHandledException(ex);
		   }
		return rowsAffected > 0;
	}
	
	/**
	 * Returns the stored last sent id in the data base
	 * 
	 * @param dateToCheck
	 * @return msgCount
	 * @throws SMSStatisticHandledException
	 */
	public int getLastSentIdOnDate(String dateToCheck, String smsType) throws SMSStatisticHandledException {
		int msgCount = -1;
		try {
	      Cursor cursor = this.db.query(TABLE_NAME, new String[] { "lastsentid" },
	        "date LIKE ? and type = ?", new String[]{dateToCheck, smsType}, null, null, "id asc");
	      if (cursor.moveToFirst()) {
	         do {
	        	 msgCount = cursor.getInt(0);// parameterIndex starts at 0
	         } while (cursor.moveToNext());
	      }
	      if (cursor != null && !cursor.isClosed()) {
	         cursor.close();
	      }
	     } catch(Exception ex) {
	    	 Log.w("Selection", ex.fillInStackTrace());
	    	 throw new SMSStatisticHandledException(ex);
	     }
		return msgCount;
	}
	
	public SMSStatisticsBean getDailyStatisticsOfDate(String dateToCheck) throws SMSStatisticHandledException {
		SMSStatisticsBean statisticsBean = new SMSStatisticsBean();
		List<Integer> result = this.getStatisticOnDates(Arrays.asList(dateToCheck), SMSStatsisticsContants.SMS_TYPE_SENT);
		   int obtainedCount = 0;
		   if(!result.isEmpty()) {
			   obtainedCount = result.get(0);
		   }
		statisticsBean.setSentCount(obtainedCount);
		result = this.getStatisticOnDates(Arrays.asList(dateToCheck), SMSStatsisticsContants.SMS_TYPE_RECIEVED);
		 if(!result.isEmpty()) {
			   obtainedCount = result.get(0);
		   }
		statisticsBean.setRecievedCount(obtainedCount);
		statisticsBean.setTotalCount(statisticsBean.getSentCount()+statisticsBean.getRecievedCount());// sum up count
		return statisticsBean;
	}
	
	public SMSStatisticsBean getCurrentWeekSMSStatistics() throws SMSStatisticHandledException {
		SMSStatisticsBean statisticsBean = new SMSStatisticsBean();
		int sentCount =0;
		int recievedCount = 0;
		List<String> currentWeekDates = SMSStatisticsUtil.getCurrentWeekDatesList();
		List<Integer> sentCountList = this.getStatisticOnDates(currentWeekDates, SMSStatsisticsContants.SMS_TYPE_SENT);
		List<Integer> recievedCountList = this.getStatisticOnDates(currentWeekDates, SMSStatsisticsContants.SMS_TYPE_RECIEVED);
		for(int currentCount : sentCountList) {
			sentCount += currentCount;
		}
		for(int currentCount : recievedCountList) {
			recievedCount += currentCount;
		}
		statisticsBean.setRecievedCount(recievedCount);
		statisticsBean.setSentCount(sentCount);
		statisticsBean.setTotalCount(sentCount+recievedCount);
		return statisticsBean;
	}
	
	public SMSStatisticsBean getCurrentMonthSMSStatistics() throws SMSStatisticHandledException {
		SMSStatisticsBean statisticsBean = new SMSStatisticsBean();
		int sentCount =0;
		int recievedCount = 0;
		List<String> currentMonthDates = SMSStatisticsUtil.getCurrentMonthDatesList();
		List<Integer> sentCountList = this.getStatisticOnDates(currentMonthDates, SMSStatsisticsContants.SMS_TYPE_SENT);
		List<Integer> recievedCountList = this.getStatisticOnDates(currentMonthDates, SMSStatsisticsContants.SMS_TYPE_RECIEVED);
		for(int currentCount : sentCountList) {
			sentCount += currentCount;
		}
		for(int currentCount : recievedCountList) {
			recievedCount += currentCount;
		}
		statisticsBean.setRecievedCount(recievedCount);
		statisticsBean.setSentCount(sentCount);
		statisticsBean.setTotalCount(sentCount+recievedCount);
		return statisticsBean;
	}
}

package app.android.mera.sms.statistics.observer;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import app.android.mera.sms.statistics.constants.SMSStatsisticsContants;
import app.android.mera.sms.statistics.dao.SMSStatisticsDAO;
import app.android.mera.sms.statistics.exception.SMSStatisticHandledException;
import app.android.mera.sms.statistics.util.SMSStatisticsUtil;

public class SMSStatisticsSMSTransferObserver extends ContentObserver {
	private Context context;
	private SMSStatisticsDAO smsStatisticsDAO = null;
	public SMSStatisticsSMSTransferObserver(Handler handler) {
		super(handler);
	}

	@Override
	public boolean deliverSelfNotifications() {
		Log.i("SMSStatisticsSMSTransferObserver", " inside deliverSelfNotifications");
		return super.deliverSelfNotifications();
	}

	@Override
	public void onChange(boolean selfChange, Uri uri) {
		super.onChange(selfChange, uri);
		Log.i("SMSStatisticsSMSTransferObserver", " inside onChange");
		if(smsStatisticsDAO == null) {
			smsStatisticsDAO = new SMSStatisticsDAO(this.context);
		}
		Cursor cur = context.getContentResolver().query(uri, SMSStatsisticsContants.PROJECTION, null, null, null);
		 // this will make it point to the first record, which is the last SMS sent
		cur.moveToFirst();
		if(cur.moveToFirst()) {
			int smsType = cur.getInt(cur.getColumnIndex("type"));
			int smsId = cur.getInt(cur.getColumnIndex("_id"));
			String dateToCheck = SMSStatisticsUtil.getCurrentDateAsString();
			Log.i("SMSStatisticsSMSTransferObserver", " type of message ="+smsType+" _id="+smsId);
			try {
				switch(smsType) {
					case SMSStatsisticsContants.MESSAGE_TYPE_INBOX : {						
						boolean result = smsStatisticsDAO.insertOrUpdateSMSCountOfDate(dateToCheck, SMSStatsisticsContants.SMS_TYPE_RECIEVED, smsId);
						Log.i("DAO operation", "(Recieved) Value returned from DAO ="+result);
						break;
					}
					case SMSStatsisticsContants.MESSAGE_TYPE_SENT : {
						boolean result = smsStatisticsDAO.insertOrUpdateSMSCountOfDate(dateToCheck, SMSStatsisticsContants.SMS_TYPE_SENT, smsId);
						Log.i("DAO operation", " (Sent) Value returned from DAO ="+result);
						break;
					}
					default : 
						// ignore other cases
				}
			 } catch (SMSStatisticHandledException e) {
				Log.w("SMSStatisticsSMSTransferObserver", e);
			}
			cur.moveToNext();
		}
		
	}
	public void setContext(Context callingContext) {
		context = callingContext;
	}
	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		Log.i("SMSStatisticsSMSTransferObserver", " inside onChange without Uri");
	}
	
}

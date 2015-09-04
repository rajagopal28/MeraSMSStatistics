package app.android.mera.sms.statistics.constants;

import android.net.Uri;

public class SMSStatsisticsContants {

	public static final int SMS_RECIEVED_LAST_ID = -1;
	public static final String SMS_TYPE_RECIEVED = "recieved";
	public static final String SMS_TYPE_SENT = "sent";
	public static final Uri SMS_CONTENT_URI = Uri.parse("content://sms/");
	public static final Uri SMS_SENT_CONTENT_URI =  Uri.parse("content://sms/sent");
	public static final String[] PROJECTION = new String[] { "_id",
			"thread_id", "address", "person", "date", "body", "type" };
	public static final String SMS_STATISTICS_DATE_FORMAT = "dd/MM/yyyy";
	public static final int MESSAGE_TYPE_ALL    = 0;
	public static final int MESSAGE_TYPE_INBOX  = 1;
	public static final int MESSAGE_TYPE_SENT   = 2;
	public static final int MESSAGE_TYPE_DRAFT  = 3;
	public static final int MESSAGE_TYPE_OUTBOX = 4;
	public static final int MESSAGE_TYPE_FAILED = 5; // for failed outgoing messages
	public static final int MESSAGE_TYPE_QUEUED = 6; // for messages to send later
	
	
}

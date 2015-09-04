package app.android.mera.sms.statistics;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import app.android.mera.sms.statistics.bean.SMSStatisticsBean;
import app.android.mera.sms.statistics.chart.view.MyGraphview;
import app.android.mera.sms.statistics.dao.SMSStatisticsDAO;
import app.android.mera.sms.statistics.exception.SMSStatisticHandledException;
import app.android.mera.sms.statistics.util.SMSStatisticsUtil;

public class MeraSMSStatistics extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mera_smsstatistics);

		Button goButton = (Button) findViewById(R.id.goButton);
		goButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				float values[] = new float[2];
				LinearLayout linear = (LinearLayout) findViewById(R.id.piChartLayout);
				Spinner statTypeSpinner = (Spinner) findViewById(R.id.statTypeSpinner);
				String selectedStatType = (String) statTypeSpinner
						.getSelectedItem();
				Log.i("MeraSMSStatistics", "Selected Stat ::"+selectedStatType);
				try {
					SMSStatisticsDAO statisticsDAO = new SMSStatisticsDAO(
							getApplicationContext());
					if ("Today Stat".equalsIgnoreCase(selectedStatType)) {
						SMSStatisticsBean todaysStat = statisticsDAO
								.getDailyStatisticsOfDate(SMSStatisticsUtil
										.getCurrentDateAsString());
						values[0] = todaysStat.getSentCount();
						values[1] = todaysStat.getRecievedCount();
					} else if ("This Week Stat"
							.equalsIgnoreCase(selectedStatType)) {
						SMSStatisticsBean thisWeekStat = statisticsDAO
								.getCurrentWeekSMSStatistics();
						values[0] = thisWeekStat.getSentCount();
						values[1] = thisWeekStat.getRecievedCount();
					} else if ("This Month Stat"
							.equalsIgnoreCase(selectedStatType)) {
						SMSStatisticsBean thisMonthStat = statisticsDAO
								.getCurrentMonthSMSStatistics();
						values[0] = thisMonthStat.getSentCount();
						values[1] = thisMonthStat.getRecievedCount();
					}
				} catch (SMSStatisticHandledException e) {
					Log.w("SMSStatisticHandledException", e);
				}
				if (values[0] != 0 || values[1] != 0) {
					values = calculateData(values);
					linear.removeAllViewsInLayout();
					linear.addView(new MyGraphview(getApplicationContext(),
							values));
				}

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mera_smsstatistics, menu);
		return true;
	}

	private float[] calculateData(float[] data) {
		float total = 0;
		for (int i = 0; i < data.length; i++) {
			total += data[i];
		}
		for (int i = 0; i < data.length; i++) {
			data[i] = (float) Math.ceil(360 * (data[i] / total));
			Log.i("calculateData", " calculated degree=" + data[i]);
		}
		return data;

	}
}

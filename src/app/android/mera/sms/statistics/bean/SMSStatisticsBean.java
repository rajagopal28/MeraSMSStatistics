package app.android.mera.sms.statistics.bean;

import java.io.Serializable;

public class SMSStatisticsBean implements Serializable {

	private static final long serialVersionUID = 3498120642248338096L;
	private int totalCount;
	private int sentCount;
	private int recievedCount;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getSentCount() {
		return sentCount;
	}

	public void setSentCount(int sentCount) {
		this.sentCount = sentCount;
	}

	public int getRecievedCount() {
		return recievedCount;
	}

	public void setRecievedCount(int recievedCount) {
		this.recievedCount = recievedCount;
	}

	@Override
	public String toString() {
		return "SMSStatisticsBean [totalCount=" + totalCount + ", sentCount="
				+ sentCount + ", recievedCount=" + recievedCount + "]";
	}

}

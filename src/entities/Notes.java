package entities;

public class Notes {
	public String srcHistory = "";
	public String destHistory = "";
	MyDate date;

	public String getSrcHistory() {
		return srcHistory;
	}

	public void setSrcHistory(String srcHistory) {
		this.srcHistory = srcHistory;
	}

	public String getDestHistory() {
		return destHistory;
	}

	public void setDestHistory(String destHistory) {
		this.destHistory = destHistory;
	}

	public MyDate getDate() {
		return date;
	}

	public void setDate(MyDate date) {
		this.date = date;
	}

}

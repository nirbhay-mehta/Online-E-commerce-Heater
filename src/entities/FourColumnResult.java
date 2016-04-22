package entities;

public class FourColumnResult {
	public String originCityName;
	public int cancelledFlights;
	public int totalFlights;
	public int percentage;
	public String getOriginCityName() {
		return originCityName;
	}
	public void setOriginCityName(String i) {
		this.originCityName = i;
	}
	public int getCancelledFlights() {
		return cancelledFlights;
	}
	public void setCancelledFlights(int cancelledFlights) {
		this.cancelledFlights = cancelledFlights;
	}
	public int getTotalFlights() {
		return totalFlights;
	}
	public void setTotalFlights(int totalFlights) {
		this.totalFlights = totalFlights;
	}
	public int getPercentage() {
		return percentage;
	}
	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}
	
	

}

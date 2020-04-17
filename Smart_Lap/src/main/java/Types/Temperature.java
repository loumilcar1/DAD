package Types;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Temperature {

	private static final AtomicInteger COUNTER = new AtomicInteger();
	public int id;
	
	private float value;
	private long timetmp;
	private String location;
	private int accuracy;
	
	
	@JsonCreator
	public Temperature(
			@JsonProperty("value") float value, 
			@JsonProperty("timestamp") long timetmp, 
			@JsonProperty("location") String location, 
			@JsonProperty("accuracy") int accuracy) {
		super();
		this.id = COUNTER.getAndIncrement();
		this.value = value;
		this.timetmp = timetmp;
		this.location = location;
		this.accuracy = accuracy;
	}
	public Temperature() {
		super();
		this.id = COUNTER.getAndIncrement();
		this.value = 0;
		this.timetmp =Calendar.getInstance().getTimeInMillis();
		/*Calendar cal =Calendar.getInstance();
		cal.set(Calendar.MONTH,1);
		cal.set(Calendar.DAY_OF_MONTH,25);
		cal.set(Calendar.HOUR,22);
		cal.set(Calendar.MINUTE,15);
		this.timetmp= cal.getTimeInMillis();*/
		this.location ="";
		this.accuracy = 0;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	public long getTimetmp() {
		return timetmp;
	}
	public void setTimetmp(long timetmp) {
		this.timetmp = timetmp;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}
	public int getId() {
		return id;
	}

}

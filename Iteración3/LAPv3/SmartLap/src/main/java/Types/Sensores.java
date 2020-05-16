package Types;

public class Sensores {
	private int idSensor;
	private int idDispositivo;
	private float temperatura;
	private float humedad;
	private int calidad_aire;
	private long timestamp;
	private int location;
	
	public Sensores(int idSensor, int idDispositivo, float temperatura, float humedad, int calidad_aire, long timestamp,
			int location) {
		super();
		this.idSensor = idSensor;
		this.idDispositivo = idDispositivo;
		this.temperatura = temperatura;
		this.humedad = humedad;
		this.calidad_aire = calidad_aire;
		this.timestamp = timestamp;
		this.location = location;
	}

	public Sensores() {
		super();
	}

	public int getIdSensor() {
		return idSensor;
	}

	public void setIdSensor(int idSensor) {
		this.idSensor = idSensor;
	}

	public int getIdDispositivo() {
		return idDispositivo;
	}

	public void setIdDispositivo(int idDispositivo) {
		this.idDispositivo = idDispositivo;
	}

	public float getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(float temperatura) {
		this.temperatura = temperatura;
	}

	public float getHumedad() {
		return humedad;
	}

	public void setHumedad(float humedad) {
		this.humedad = humedad;
	}

	public int getCalidad_aire() {
		return calidad_aire;
	}

	public void setCalidad_aire(int calidad_aire) {
		this.calidad_aire = calidad_aire;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + calidad_aire;
		result = prime * result + Float.floatToIntBits(humedad);
		result = prime * result + idDispositivo;
		result = prime * result + idSensor;
		result = prime * result + location;
		result = prime * result + Float.floatToIntBits(temperatura);
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sensores other = (Sensores) obj;
		if (calidad_aire != other.calidad_aire)
			return false;
		if (Float.floatToIntBits(humedad) != Float.floatToIntBits(other.humedad))
			return false;
		if (idDispositivo != other.idDispositivo)
			return false;
		if (idSensor != other.idSensor)
			return false;
		if (location != other.location)
			return false;
		if (Float.floatToIntBits(temperatura) != Float.floatToIntBits(other.temperatura))
			return false;
		if (timestamp != other.timestamp)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Sensores [idSensor=" + idSensor + ", idDispositivo=" + idDispositivo + ", temperatura=" + temperatura
				+ ", humedad=" + humedad + ", calidad_aire=" + calidad_aire + ", timestamp=" + timestamp + ", location="
				+ location + "]";
	}
	
}

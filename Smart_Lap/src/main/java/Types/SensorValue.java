package Types;

public class SensorValue {
		private int idsensor_value;
		private float valor;
		private int idsensor;
		private float accuracy;
		private long timestamp;
		private int piso;
		
		@Override
		public String toString() {
			return "SensorValue [idsensor_value=" + idsensor_value + ", valor=" + valor + ", idsensor=" + idsensor
					+ ", accuracy=" + accuracy + ", timestamp=" + timestamp + ", piso=" + piso + "]";
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Float.floatToIntBits(accuracy);
			result = prime * result + idsensor;
			result = prime * result + idsensor_value;
			result = prime * result + piso;
			result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
			result = prime * result + Float.floatToIntBits(valor);
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
			SensorValue other = (SensorValue) obj;
			if (Float.floatToIntBits(accuracy) != Float.floatToIntBits(other.accuracy))
				return false;
			if (idsensor != other.idsensor)
				return false;
			if (idsensor_value != other.idsensor_value)
				return false;
			if (piso != other.piso)
				return false;
			if (timestamp != other.timestamp)
				return false;
			if (Float.floatToIntBits(valor) != Float.floatToIntBits(other.valor))
				return false;
			return true;
		}
		public SensorValue() {
			super();
		}
		public SensorValue(int idsensor_value, float valor, int idsensor, float accuracy, long timestamp, int piso) {
			super();
			this.idsensor_value = idsensor_value;
			this.valor = valor;
			this.idsensor = idsensor;
			this.accuracy = accuracy;
			this.timestamp = timestamp;
			this.piso = piso;
		}
		public int getIdsensor_value() {
			return idsensor_value;
		}
		public void setIdsensor_value(int idsensor_value) {
			this.idsensor_value = idsensor_value;
		}
		public float getValor() {
			return valor;
		}
		public void setValor(float valor) {
			this.valor = valor;
		}
		public int getIdsensor() {
			return idsensor;
		}
		public void setIdsensor(int idsensor) {
			this.idsensor = idsensor;
		}
		public float getAccuracy() {
			return accuracy;
		}
		public void setAccuracy(float accuracy) {
			this.accuracy = accuracy;
		}
		public long getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}
		public int getPiso() {
			return piso;
		}
		public void setPiso(int piso) {
			this.piso = piso;
		}
		
		
}
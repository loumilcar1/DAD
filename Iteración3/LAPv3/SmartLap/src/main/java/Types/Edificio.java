package Types;

public class Edificio {
	private int idEdificio;
	private String direccion;
	private String dni;
	private String nombre;
	private String apellidos;
	private int telefono;
	private int numPlantas;
	
	public Edificio(int idEdificio, String direccion, String dni, String nombre, String apellidos, int telefono,
			int numPlantas) {
		super();
		this.idEdificio = idEdificio;
		this.direccion = direccion;
		this.dni = dni;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.telefono = telefono;
		this.numPlantas = numPlantas;
	}
	
	public Edificio() {
		super();
	}

	public int getIdEdificio() {
		return idEdificio;
	}

	public void setIdEdificio(int idEdificio) {
		this.idEdificio = idEdificio;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public int getTelefono() {
		return telefono;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}

	public int getNumPlantas() {
		return numPlantas;
	}

	public void setNumPlantas(int numPlantas) {
		this.numPlantas = numPlantas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apellidos == null) ? 0 : apellidos.hashCode());
		result = prime * result + ((direccion == null) ? 0 : direccion.hashCode());
		result = prime * result + ((dni == null) ? 0 : dni.hashCode());
		result = prime * result + idEdificio;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + numPlantas;
		result = prime * result + telefono;
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
		Edificio other = (Edificio) obj;
		if (apellidos == null) {
			if (other.apellidos != null)
				return false;
		} else if (!apellidos.equals(other.apellidos))
			return false;
		if (direccion == null) {
			if (other.direccion != null)
				return false;
		} else if (!direccion.equals(other.direccion))
			return false;
		if (dni == null) {
			if (other.dni != null)
				return false;
		} else if (!dni.equals(other.dni))
			return false;
		if (idEdificio != other.idEdificio)
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (numPlantas != other.numPlantas)
			return false;
		if (telefono != other.telefono)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Edificcio [idEdificio=" + idEdificio + ", direccion=" + direccion + ", dni=" + dni + ", nombre="
				+ nombre + ", apellidos=" + apellidos + ", telefono=" + telefono + ", numPlantas=" + numPlantas + "]";
	}
	
	
	
	
}

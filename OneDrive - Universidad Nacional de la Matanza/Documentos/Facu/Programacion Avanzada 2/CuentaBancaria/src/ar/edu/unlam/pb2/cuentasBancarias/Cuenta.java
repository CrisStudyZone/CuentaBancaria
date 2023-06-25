package ar.edu.unlam.pb2.cuentasBancarias;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class Cuenta implements CBUGenerador, TransferenciaEntreCuentas{

	protected static HashSet<String> cbuGenerados = new HashSet<String>();
	protected static HashMap<Integer, Operaciones> registroOperaciones;
	protected final String CBU;
	protected static final int CBU_LENGTH = 22;
	protected Integer contadorDeOperaciones = 1;
	protected Double saldo;

	public Cuenta() {
		this.CBU = generarCBU();
		registroOperaciones = new HashMap<Integer, Operaciones>();
	}

	public void agregarDinero(Double cantidadIngresada) {
		this.saldo += cantidadIngresada;
		registrarOperacion(Motivo.DEPOSITO , consultarCBU(), consultarCBU(), cantidadIngresada);
	}

	public Double consultarSaldo() {
		return this.saldo;
	}
	
	public void retirarDinero(Double candidadExtraida) throws SaldoInsuficienteException, UsoDescubiertoException {
		this.saldo -= candidadExtraida;
	}

	public String generarCBU() {
		Random random = new Random();
		StringBuilder cbuBuilder = new StringBuilder();
		// clase en Java que se utiliza para construir y manipular cadenas de caracteres
		// de manera eficiente. Proporciona métodos para agregar, insertar y modificar
		// el contenido de la cadena sin necesidad de crear objetos adicionales, lo que
		// resulta en un mejor rendimiento y consumo de memoria

		for (int i = 0; i < Cuenta.CBU_LENGTH; i++) {
			int digit = random.nextInt(10); // Generar dígito aleatorio entre 0 y 9
			cbuBuilder.append(digit);
		}

		return cbuBuilder.toString();
	}

	public String consultarCBU() {
		return CBU;
	}
	
	protected void registrarOperacion(Motivo motivo, String CBUOrigen, String CBUDestino, Double Monto) {
		Operaciones nueva = new Operaciones(motivo, CBUOrigen, CBUDestino, Monto);
		registroOperaciones.put(this.contadorDeOperaciones, nueva);
		contadorDeOperaciones++;
	}
	
	protected Integer cantidadDeRegistrosDeOperaciones() {
		return this.contadorDeOperaciones;
	}
	protected  Set<Entry<Integer, Operaciones>> RegistrosDeOperaciones() {
		return registroOperaciones.entrySet();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(CBU);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cuenta other = (Cuenta) obj;
		return Objects.equals(CBU, other.CBU);
	}

	@Override
	public void enviarDinero(String CBUqueEnvia, String CBUqueRecibe, Double monto) throws CuentaInexistenteException {
		if(CBUqueEnvia.equals(CBU) && cbuGenerados.contains(CBUqueRecibe)) {
			try {
				retirarDinero(monto);
				registrarOperacion(Motivo.TRANSFERENCIA , CBUqueEnvia, CBUqueRecibe, monto);
			} catch (SaldoInsuficienteException | UsoDescubiertoException e) {
				e.printStackTrace();
			}
		}throw new CuentaInexistenteException("Verifique los datos ingresados");
	}

	@Override
	public void recibirDinero(String CBUqueEnvia, String CBUqueRecibe, Double monto) throws CuentaInexistenteException {
		if(CBUqueEnvia.equals(CBU) && cbuGenerados.contains(CBUqueRecibe)) {
			agregarDinero(monto);
			registrarOperacion(Motivo.TRANSFERENCIA , CBUqueEnvia, CBUqueRecibe, monto);
		}throw new CuentaInexistenteException("Verifique los datos ingresados");
	}

	
}

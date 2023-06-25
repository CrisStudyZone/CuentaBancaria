package ar.edu.unlam.pb2.cuentasBancarias;

public class CuentaSueldo extends Cuenta implements CBUGenerador, TransferenciaEntreCuentas {

	private final String CBU;

	public CuentaSueldo() {
		super();
		this.saldo = 0.0;
		this.CBU = generarCBU();
	}

	@Override
//	public void retirarDinero(Double candidadExtraida){
//		try{
//			if(candidadExtraida>consultarSaldo()) {
//			throw new SaldoInsuficienteException();
//		}
//			this.saldo -= candidadExtraida;
//			System.out.println("Extraccion exitosa");
//		}catch (SaldoInsuficienteException e){
//			System.out.println(e.getMessage());
//		}
//	}
	public void retirarDinero(Double candidadExtraida) throws SaldoInsuficienteException {
		if (candidadExtraida > consultarSaldo()) {
			throw new SaldoInsuficienteException("Saldo insuficiente en la cuenta");
			// No se coloca un tryCatch para poder propagar la excepcion fuera del metodo y
			// con el tryCatch la excepcion seria captada dentro del metodo solo dando como
			// resultado un texto por consola
		}
		this.saldo -= candidadExtraida;
		registrarOperacion(Motivo.EXTRACCION , consultarCBU(), consultarCBU(), candidadExtraida);
		System.out.println("Extraccion exitosa");
	}

	@Override
	public Double consultarSaldo() {
		return this.saldo;
	}

	@Override
	public String consultarCBU() {
		return this.CBU;
	}
	
	
}

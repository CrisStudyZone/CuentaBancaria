package ar.edu.unlam.pb2.cuentasBancarias;

public class CajaDeAhorro extends Cuenta implements CBUGenerador, TransferenciaEntreCuentas {

	private String CBU;
	private Integer contadorExtracciones = 0;
	private Double costoAdicionalPorExtraccion = 6.0;

	public CajaDeAhorro() {
		super();
		this.saldo = 0.0;
		this.CBU = generarCBU();
	}

	@Override
	public String consultarCBU() {
		return CBU;
	}

	@Override
	public Double consultarSaldo() {
		return this.saldo;
	}

	@Override
	public void retirarDinero(Double candidadAExtraer) throws SaldoInsuficienteException {
		Double candidadExtraida;
		if (contadorExtracciones >= 5) {
			candidadExtraida = candidadAExtraer + costoAdicionalPorExtraccion;
			if (candidadExtraida > consultarSaldo()) {
				throw new SaldoInsuficienteException("Saldo insuficiente");
			}
			this.saldo -= candidadExtraida;
			registrarOperacion(Motivo.EXTRACCION, consultarCBU(), consultarCBU(), candidadExtraida);
			contadorExtracciones++;
			System.out.println("Usted a extraido: " + candidadExtraida + " Su saldo actual es " + consultarSaldo());
		} else {
			candidadExtraida = candidadAExtraer;
			if (candidadExtraida > consultarSaldo()) {
				throw new SaldoInsuficienteException("Saldo insuficiente");
			}
			this.saldo -= candidadExtraida;
			contadorExtracciones++;
			System.out.println("Extraccion exitosa " + consultarSaldo());
		}
	}
}

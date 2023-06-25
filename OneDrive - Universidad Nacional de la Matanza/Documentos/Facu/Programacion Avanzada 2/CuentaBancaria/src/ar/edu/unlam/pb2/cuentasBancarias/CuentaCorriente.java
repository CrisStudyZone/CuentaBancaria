package ar.edu.unlam.pb2.cuentasBancarias;

public class CuentaCorriente extends Cuenta implements CBUGenerador, TransferenciaEntreCuentas {

	private String CBU;
	private final Double SALDO_MAXIMO_DESCUBIERTO = 150.0;
	private Double saldoDisponible = SALDO_MAXIMO_DESCUBIERTO;
	private final Double PORCENTAJE_DE_RECARGO = 0.05;

	public CuentaCorriente() {
		super();
		this.CBU = generarCBU();
		this.saldo = 0.0;
	}

	@Override
	public String consultarCBU() {
		return this.CBU;
	}

	
	@Override
	public Double consultarSaldo() {
		return this.saldo;
	}
	public Double consultarSaldoMasDisponible() {
		Double saldoMasDescubierto = saldo + saldoDisponible;
		return saldoMasDescubierto;
	}

	@Override
	public void retirarDinero(Double candidadExtraida) throws SaldoInsuficienteException, UsoDescubiertoException{
			if (consultarSaldo() >= candidadExtraida) {
			this.saldo -= candidadExtraida;
			registrarOperacion(Motivo.EXTRACCION , consultarCBU(), consultarCBU(), candidadExtraida);
			System.out.println("Extraccion exitosa " + consultarSaldo());
		} else {
			if (candidadExtraida > consultarSaldoMasDisponible()) {
				throw new SaldoInsuficienteException("Saldo insuficiente, usted a agotado su descubierto");
			}else {
				Double Recargo = (candidadExtraida-consultarSaldo()) * PORCENTAJE_DE_RECARGO;
				this.saldoDisponible = Recargo + (candidadExtraida-consultarSaldo());
				this.saldo = 0.0;
				throw new UsoDescubiertoException("Usted a consumido " + consultarDisponible() + " de su descubierto");
			}
		}	
	}

	public Double consultarDisponible() {
		return saldoDisponible;
	}
}

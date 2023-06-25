package ar.edu.unlam.pb2.cuentasBancarias;

public interface TransferenciaEntreCuentas {

	public void enviarDinero(String CBUqueEnvia, String CBUqueRecibe, Double monto) throws CuentaInexistenteException;

	public void recibirDinero(String CBUqueEnvia, String CBUqueRecibe, Double monto) throws CuentaInexistenteException;

}

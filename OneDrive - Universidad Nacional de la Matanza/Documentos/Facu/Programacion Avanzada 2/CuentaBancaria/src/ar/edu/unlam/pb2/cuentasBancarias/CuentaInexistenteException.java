package ar.edu.unlam.pb2.cuentasBancarias;

public class CuentaInexistenteException extends Exception {
	
	public CuentaInexistenteException(String mensaje) {
        super(mensaje);
    }

}

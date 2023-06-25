package ar.edu.unlam.pb2.cuentasBancarias;

public class SaldoInsuficienteException extends Exception {
	
	public SaldoInsuficienteException(String mensaje) {
        super(mensaje);
    }

}

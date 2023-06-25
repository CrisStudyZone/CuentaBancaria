package ar.edu.unlam.pb2.cuentasBancarias;

import static org.junit.Assert.*;

import java.util.Map.Entry;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestCuentasBancarias {

	@Test
	public void queLaCuentaPuedaAgregarDinero() {
		Cuenta cuenta = new CuentaSueldo();
		cuenta.agregarDinero(8000.0);
		Double valorObtenido = cuenta.consultarSaldo();
		Double valorEsperado = 8000.0;
		assertEquals(valorEsperado, valorObtenido);
	}

	@Test
	public void queLaCuentaPuedaSacarDinero() throws SaldoInsuficienteException, UsoDescubiertoException {
		Cuenta cuenta = new CuentaSueldo();
		cuenta.agregarDinero(8000.0);
		cuenta.retirarDinero(4000.0);
		Double valorObtenido = cuenta.consultarSaldo();
		Double valorEsperado = 4000.0;
		assertEquals(valorEsperado, valorObtenido);
	}

	@Test
	public void queSePuedaCrearUnaCuentaDeCadaTipo() {
		Cuenta cuentaS = new CuentaSueldo();
		Cuenta cajaAh = new CajaDeAhorro();
		Cuenta cuentaCt = new CuentaCorriente();
		assertNotNull(cuentaS.consultarCBU());
		assertNotNull(cajaAh.consultarCBU());
		assertNotNull(cuentaCt.consultarCBU());
	}

	@Test
	public void queNoSePuedaExtraerMasPlataDeLaQueHayEnLaCuentaSueldo() {
		Cuenta cuenta = new CuentaSueldo();
		cuenta.agregarDinero(8000.0);
		assertThrows(SaldoInsuficienteException.class, () -> {
			cuenta.retirarDinero(8001.0);
		});
	}

	@Test(expected = SaldoInsuficienteException.class)
	public void testRetirarSaldoInsuficiente() throws SaldoInsuficienteException, UsoDescubiertoException {
		Cuenta cuenta = new CuentaSueldo();
		cuenta.agregarDinero(100.0);
		cuenta.retirarDinero(200.0);
	}

	@Rule
	// La anotación `@Rule` permite definir una regla que se aplicará a todos los
	// tests de una clase. Se usa la regla `ExpectedException` para indicar que
	// se espera una excepción en este test en particular.
	public ExpectedException exceptionRule = ExpectedException.none();

	@Test
	public void testRetirarSaldoInsuficienteConRule() throws SaldoInsuficienteException, UsoDescubiertoException {
		exceptionRule.expect(SaldoInsuficienteException.class);
		exceptionRule.expectMessage("Saldo insuficiente en la cuenta");

		Cuenta cuenta = new CuentaSueldo();
		cuenta.agregarDinero(100.0);
		cuenta.retirarDinero(200.0);
	}
	
	@Test
	public void testRetirarSaldoDeCajaDeAhorroMasCostoAdicional() throws SaldoInsuficienteException, UsoDescubiertoException {
		Cuenta cuenta = new CajaDeAhorro();
		cuenta.agregarDinero(100.0);
		cuenta.retirarDinero(2.0);
		cuenta.retirarDinero(2.0);
		cuenta.retirarDinero(2.0);
		cuenta.retirarDinero(2.0);
		cuenta.retirarDinero(2.0);
		cuenta.retirarDinero(2.0);
		Double valorObtenido = cuenta.consultarSaldo();
		Double valorEsperado = 82.0;
		assertEquals(valorEsperado, valorObtenido);
	}
	@Test(expected = UsoDescubiertoException.class)
	public void testRetirarSaldoDeCuentaCorrienteConDescubierto() throws SaldoInsuficienteException, UsoDescubiertoException {
		Cuenta cuenta = new CuentaCorriente();
		cuenta.agregarDinero(100.0);
		cuenta.retirarDinero(50.0);
		cuenta.retirarDinero(52.0);
		Double valorObtenido = ((CuentaCorriente) cuenta).consultarDisponible();
		Double valorEsperado = (150-2.1);
		assertEquals(valorEsperado, valorObtenido);
	}
	@Test(expected = CuentaInexistenteException.class)
	public void testRealizartransferencia() throws SaldoInsuficienteException, UsoDescubiertoException, CuentaInexistenteException {
		Cuenta cuenta = new CuentaCorriente();
		Cuenta cuenta2 = new CuentaSueldo();
		cuenta.agregarDinero(100.0);
		cuenta.enviarDinero(cuenta.consultarCBU(), cuenta2.consultarCBU(),100.0);
		Double valorObtenido = ((CuentaCorriente) cuenta).consultarDisponible();
		Double valorEsperado = 0.0;
		assertEquals(valorEsperado, valorObtenido);
	}

	@Test(expected = CuentaInexistenteException.class)
	public void testAlRealizarUnaTransferenciaTambienSeRecibaElDineroEnLaOtraCuenta() throws SaldoInsuficienteException, UsoDescubiertoException, CuentaInexistenteException {
		Cuenta cuenta = new CuentaCorriente();
		Cuenta cuenta2 = new CuentaSueldo();
		cuenta.agregarDinero(100.0);
		Double montoATransferir = 100.0;
		cuenta.enviarDinero(cuenta.consultarCBU(), cuenta2.consultarCBU(),montoATransferir);
		cuenta2.recibirDinero(cuenta.consultarCBU(), cuenta2.consultarCBU(), montoATransferir);
		Double valorObtenido = ((CuentaCorriente) cuenta).consultarDisponible();
		Double valorEsperado = 0.0;
		assertEquals(valorEsperado, valorObtenido);
		Double valorObtenido2 = cuenta2.consultarSaldo();
		Double valorEsperado2 = 100.0;
		assertEquals(valorEsperado2, valorObtenido2);
	}
	
	@Test(expected = CuentaInexistenteException.class)
	public void testRealizarTransferenciaAUnCBUNoRegistrado() throws SaldoInsuficienteException, UsoDescubiertoException, CuentaInexistenteException {
		Cuenta cuenta = new CuentaCorriente();
		cuenta.agregarDinero(100.0);
		cuenta.enviarDinero(cuenta.consultarCBU(), "25205152388562659856",100.0);
	}
	
	@Test(expected = CuentaInexistenteException.class)
	public void testPruebaQueSeGuardenRegistrosDeTransferencias() throws SaldoInsuficienteException, UsoDescubiertoException, CuentaInexistenteException {
		Cuenta cuenta = new CuentaCorriente();
		Cuenta cuenta2 = new CuentaSueldo();
		cuenta.agregarDinero(100.0);
		Double montoATransferir = 100.0;
		cuenta.enviarDinero(cuenta.consultarCBU(), cuenta2.consultarCBU(),montoATransferir);
		cuenta2.recibirDinero(cuenta.consultarCBU(), cuenta2.consultarCBU(), montoATransferir);
		Integer valorObtenido = cuenta.cantidadDeRegistrosDeOperaciones();
		Integer valorEsperado = 2;
		assertEquals(valorObtenido, valorEsperado);
	}
	
	@Test(expected = CuentaInexistenteException.class)
	public void testMuestraQueRegistrosDeTransferenciasSeGuardaron() throws SaldoInsuficienteException, UsoDescubiertoException, CuentaInexistenteException {
		Cuenta cuenta = new CuentaCorriente();
		Cuenta cuenta2 = new CuentaSueldo();
		cuenta.agregarDinero(100.0);
		Double montoATransferir = 100.0;
		cuenta.enviarDinero(cuenta.consultarCBU(), cuenta2.consultarCBU(),montoATransferir);
		cuenta2.recibirDinero(cuenta.consultarCBU(), cuenta2.consultarCBU(), montoATransferir);
		Set<Entry<Integer, Operaciones>> valorObtenido = cuenta.RegistrosDeOperaciones();
		assertNotNull(valorObtenido);
	}
}

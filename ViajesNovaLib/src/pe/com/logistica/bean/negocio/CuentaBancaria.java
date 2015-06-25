package pe.com.logistica.bean.negocio;

import pe.com.logistica.bean.base.Base;
import pe.com.logistica.bean.base.BaseVO;

/**
 * @author Edwin
 * @version 1.0
 * @created 14-dic-2013 01:14:34 p.m.
 */
public class CuentaBancaria extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8231091082712340586L;
	private BaseVO banco;
	private String numeroCuenta;
	private int prioridad;

	public CuentaBancaria() {

	}

	public void finalize() throws Throwable {

	}

	/**
	 * @return the banco
	 */
	public BaseVO getBanco() {
		return banco;
	}

	/**
	 * @param banco
	 *            the banco to set
	 */
	public void setBanco(BaseVO banco) {
		this.banco = banco;
	}

	/**
	 * @return the numeroCuenta
	 */
	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	/**
	 * @param numeroCuenta
	 *            the numeroCuenta to set
	 */
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	/**
	 * @return the prioridad
	 */
	public int getPrioridad() {
		return prioridad;
	}

	/**
	 * @param prioridad
	 *            the prioridad to set
	 */
	public void setPrioridad(int prioridad) {
		this.prioridad = prioridad;
	}

}
/**
 * 
 */
package pe.com.logistica.bean.base;

import java.io.Serializable;

/**
 * @author Edwin
 * 
 */
public class Base implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String codigoCadena;
	private Integer codigoEntero;
	private BaseVO estado;

	/**
	 * 
	 */
	public Base() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the codigoCadena
	 */
	public String getCodigoCadena() {
		return codigoCadena;
	}

	/**
	 * @param codigoCadena
	 *            the codigoCadena to set
	 */
	public void setCodigoCadena(String codigoCadena) {
		this.codigoCadena = codigoCadena;
	}

	/**
	 * @return the codigoEntero
	 */
	public Integer getCodigoEntero() {
		return codigoEntero;
	}

	/**
	 * @param codigoEntero
	 *            the codigoEntero to set
	 */
	public void setCodigoEntero(Integer codigoEntero) {
		this.codigoEntero = codigoEntero;
	}

	/**
	 * @return the estado
	 */
	public BaseVO getEstado() {
		if (estado == null) {
			estado = new BaseVO();
		}
		return estado;
	}

	/**
	 * @param estado
	 *            the estado to set
	 */
	public void setEstado(BaseVO estado) {
		this.estado = estado;
	}

}

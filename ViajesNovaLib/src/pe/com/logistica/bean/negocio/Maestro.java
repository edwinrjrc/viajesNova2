/**
 * 
 */
package pe.com.logistica.bean.negocio;

import pe.com.logistica.bean.base.BaseVO;

/**
 * @author Edwin
 * 
 */
public class Maestro extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String descripcion;
	private String abreviatura;
	private int orden;
	private Integer codigoMaestro;

	/**
	 * 
	 */
	public Maestro() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion
	 *            the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the orden
	 */
	public int getOrden() {
		return orden;
	}

	/**
	 * @param orden
	 *            the orden to set
	 */
	public void setOrden(int orden) {
		this.orden = orden;
	}

	/**
	 * @return the codigoMaestro
	 */
	public Integer getCodigoMaestro() {
		return codigoMaestro;
	}

	/**
	 * @param codigoMaestro
	 *            the codigoMaestro to set
	 */
	public void setCodigoMaestro(Integer codigoMaestro) {
		this.codigoMaestro = codigoMaestro;
	}

	/**
	 * @return the abreviatura
	 */
	public String getAbreviatura() {
		return abreviatura;
	}

	/**
	 * @param abreviatura the abreviatura to set
	 */
	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

}

/**
 * 
 */
package pe.com.logistica.bean.negocio;

import pe.com.logistica.bean.base.BaseNegocio;
import pe.com.logistica.bean.base.BaseVO;

/**
 * @author EDWREB
 *
 */
public class TarjetaCredito extends BaseNegocio {
	/**
	 * 
	 */
	private static final long serialVersionUID = 784568375438203063L;
	
	private String nombreTitular;
	private String numeroTarjeta;
	private String fechaVencimiento;
	private BaseVO proveedoTarjeta;
	
	
	/**
	 * @return the nombreTitular
	 */
	public String getNombreTitular() {
		return nombreTitular;
	}
	/**
	 * @param nombreTitular the nombreTitular to set
	 */
	public void setNombreTitular(String nombreTitular) {
		this.nombreTitular = nombreTitular;
	}
	/**
	 * @return the numeroTarjeta
	 */
	public String getNumeroTarjeta() {
		return numeroTarjeta;
	}
	/**
	 * @param numeroTarjeta the numeroTarjeta to set
	 */
	public void setNumeroTarjeta(String numeroTarjeta) {
		this.numeroTarjeta = numeroTarjeta;
	}
	/**
	 * @return the fechaVencimiento
	 */
	public String getFechaVencimiento() {
		return fechaVencimiento;
	}
	/**
	 * @param fechaVencimiento the fechaVencimiento to set
	 */
	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	/**
	 * @return the proveedoTarjeta
	 */
	public BaseVO getProveedoTarjeta() {
		return proveedoTarjeta;
	}
	/**
	 * @param proveedoTarjeta the proveedoTarjeta to set
	 */
	public void setProveedoTarjeta(BaseVO proveedoTarjeta) {
		this.proveedoTarjeta = proveedoTarjeta;
	}
	
	
	
}

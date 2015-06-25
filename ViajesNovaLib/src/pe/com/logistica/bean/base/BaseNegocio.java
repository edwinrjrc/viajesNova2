/**
 * 
 */
package pe.com.logistica.bean.base;

import java.util.Date;

/**
 * @author Edwin
 * 
 */
public class BaseNegocio extends Base {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7052956093882261257L;
	
	
	private String usuarioCreacion;
	private Date fechaCreacion;
	private String ipCreacion;
	private String usuarioModificacion;
	private Date fechaModificacion;
	private String ipModificacion;

	/**
	 * 
	 */
	public BaseNegocio() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the usuarioCreacion
	 */
	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}

	/**
	 * @param usuarioCreacion
	 *            the usuarioCreacion to set
	 */
	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}

	/**
	 * @return the fechaCreacion
	 */
	public Date getFechaCreacion() {
		if (fechaCreacion == null){
			fechaCreacion = new Date();
		}
		return fechaCreacion;
	}

	/**
	 * @param fechaCreacion
	 *            the fechaCreacion to set
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * @return the ipCreacion
	 */
	public String getIpCreacion() {
		return ipCreacion;
	}

	/**
	 * @param ipCreacion
	 *            the ipCreacion to set
	 */
	public void setIpCreacion(String ipCreacion) {
		this.ipCreacion = ipCreacion;
	}

	/**
	 * @return the usuarioModificacion
	 */
	public String getUsuarioModificacion() {
		return usuarioModificacion;
	}

	/**
	 * @param usuarioModificacion
	 *            the usuarioModificacion to set
	 */
	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}

	/**
	 * @return the fechaModificacion
	 */
	public Date getFechaModificacion() {
		if (fechaModificacion == null){
			fechaModificacion = new Date();
		}
		return fechaModificacion;
	}

	/**
	 * @param fechaModificacion
	 *            the fechaModificacion to set
	 */
	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	/**
	 * @return the ipModificacion
	 */
	public String getIpModificacion() {
		return ipModificacion;
	}

	/**
	 * @param ipModificacion
	 *            the ipModificacion to set
	 */
	public void setIpModificacion(String ipModificacion) {
		this.ipModificacion = ipModificacion;
	}

}

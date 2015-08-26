/**
 * 
 */
package pe.com.logistica.web.faces;

import java.sql.SQLException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import pe.com.logistica.bean.negocio.CuentaBancaria;
import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;
import pe.com.logistica.web.servicio.NegocioServicio;
import pe.com.logistica.web.servicio.impl.NegocioServicioImpl;

/**
 * @author EDWREB
 *
 */
@ManagedBean(name="cuentaBancariaMBean")
@SessionScoped()
public class CuentaBancariaMBean extends BaseMBean {
	
	private final static Logger logger = Logger.getLogger(CuentaBancariaMBean.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -286142489189063441L;

	private List<CuentaBancaria> listaCuentasBancarias;
	private CuentaBancaria cuentaBancaria;
	
	private boolean nuevaCuentaBancaria;
	private boolean editarCuentaBancaria;
	
	private NegocioServicio negocioServicio;
	
	public CuentaBancariaMBean() {
		try {
			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			negocioServicio = new NegocioServicioImpl(servletContext);
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void nuevaCuenta(){
		this.setNuevaCuentaBancaria(true);
		this.setEditarCuentaBancaria(false);
		this.setNombreFormulario("Nueva Cuenta Bancaria");
	}
	
	public void editarCuenta(){
		this.setNuevaCuentaBancaria(false);
		this.setEditarCuentaBancaria(true);
		this.setNombreFormulario("Edita Cuenta Bancaria");
	}

	
	public void ejecutarMetodo(){
		try {
			if (validarCuentaBancaria()){
				
				if (this.isNuevaCuentaBancaria()){
					
					HttpSession session = obtenerSession(false);
					Usuario usuario = (Usuario) session
							.getAttribute("usuarioSession");
					getCuentaBancaria().setUsuarioCreacion(
							usuario.getUsuario());
					getCuentaBancaria().setIpCreacion(
							obtenerRequest().getRemoteAddr());
					
					this.negocioServicio.registrarCuentaBancaria(getCuentaBancaria());
					
					this.mostrarMensajeExito("Cuenta Bancaria registrada satisfactoriamente");
				}
			}
		} catch (ErrorRegistroDataException e) {
			logger.error(e.getMessage(), e);
			this.mostrarMensajeError(e.getMessage());
		}
	}
	private boolean validarCuentaBancaria() {
		boolean resultado = true; 
		
		return resultado;
	}
	
	public void consultarCuenta(Integer idCuenta){
		
	}

	/**
	 * @return the listaCuentasBancarias
	 */
	public List<CuentaBancaria> getListaCuentasBancarias() {
		
		try {
			listaCuentasBancarias = this.negocioServicio.listarCuentasBancarias();
			
			
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
		
		return listaCuentasBancarias;
	}

	/**
	 * @param listaCuentasBancarias the listaCuentasBancarias to set
	 */
	public void setListaCuentasBancarias(List<CuentaBancaria> listaCuentasBancarias) {
		this.listaCuentasBancarias = listaCuentasBancarias;
	}

	/**
	 * @return the cuentaBancaria
	 */
	public CuentaBancaria getCuentaBancaria() {
		if (cuentaBancaria == null){
			cuentaBancaria = new CuentaBancaria();
		}
		return cuentaBancaria;
	}

	/**
	 * @param cuentaBancaria the cuentaBancaria to set
	 */
	public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
		this.cuentaBancaria = cuentaBancaria;
	}

	/**
	 * @return the nuevaCuentaBancaria
	 */
	public boolean isNuevaCuentaBancaria() {
		return nuevaCuentaBancaria;
	}

	/**
	 * @param nuevaCuentaBancaria the nuevaCuentaBancaria to set
	 */
	public void setNuevaCuentaBancaria(boolean nuevaCuentaBancaria) {
		this.nuevaCuentaBancaria = nuevaCuentaBancaria;
	}

	/**
	 * @return the editarCuentaBancaria
	 */
	public boolean isEditarCuentaBancaria() {
		return editarCuentaBancaria;
	}

	/**
	 * @param editarCuentaBancaria the editarCuentaBancaria to set
	 */
	public void setEditarCuentaBancaria(boolean editarCuentaBancaria) {
		this.editarCuentaBancaria = editarCuentaBancaria;
	}
	
}

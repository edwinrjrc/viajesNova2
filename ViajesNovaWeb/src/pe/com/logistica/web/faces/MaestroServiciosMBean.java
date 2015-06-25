/**
 * 
 */
package pe.com.logistica.web.faces;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.MaestroServicio;
import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;
import pe.com.logistica.web.servicio.NegocioServicio;
import pe.com.logistica.web.servicio.impl.NegocioServicioImpl;
import pe.com.logistica.web.util.UtilWeb;

/**
 * @author edwreb
 *
 */
@ManagedBean(name = "maeServiciosMBean")
@SessionScoped()
public class MaestroServiciosMBean extends BaseMBean {
	
	private final static Logger logger = Logger.getLogger(MaestroServiciosMBean.class);

	private MaestroServicio maestroServicio;
	
	private boolean nuevoMaestroServicio;
	private boolean editarMaestroServicio;
	
	private List<MaestroServicio> listaMaeServicio;
	private List<BaseVO> listaMaeServicioImpto;
	
	private NegocioServicio negocioServicio;

	/**
	 * 
	 */
	private static final long serialVersionUID = -748394881867935320L;

	/**
	 * 
	 */
	public MaestroServiciosMBean() {
		try {
			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			// soporteServicio = new SoporteServicioImpl(servletContext);
			negocioServicio = new NegocioServicioImpl(servletContext);
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void nuevoMaestro(){
		this.setNombreFormulario("Nuevo Servicio");
		this.setNuevoMaestroServicio(true);
		this.setEditarMaestroServicio(false);
		this.setMaestroServicio(null);
	}
	
	public void ejecutarMetodo(){
		try {
			if (validarMaestroDestino()){
				HttpSession session = obtenerSession(false);
				Usuario usuario = (Usuario)session.getAttribute(USUARIO_SESSION);
				getMaestroServicio().setUsuarioCreacion(usuario.getUsuario());
				getMaestroServicio().setIpCreacion(obtenerRequest().getRemoteAddr());
				getMaestroServicio().setUsuarioModificacion(usuario.getUsuario());
				getMaestroServicio().setIpModificacion(obtenerRequest().getRemoteAddr());
				if (this.isNuevoMaestroServicio()){
					this.negocioServicio.ingresarMaestroServicio(getMaestroServicio());
					
					this.setShowModal(true);
					this.setMensajeModal("Servicio registrado satisfactoriamente");
					this.setTipoModal(TIPO_MODAL_EXITO);
				}
				else{
					getMaestroServicio().setListaServicioDepende(getListaMaeServicioImpto());
					this.negocioServicio.actualizarMaestroServicio(getMaestroServicio());
					this.setShowModal(true);
					this.setMensajeModal("Servicio actualizado satisfactoriamente");
					this.setTipoModal(TIPO_MODAL_EXITO);
				}
			}
		} catch (ErrorRegistroDataException e) {
			this.setShowModal(true);
			this.setMensajeModal(e.getMessage());
			this.setTipoModal(TIPO_MODAL_ERROR);
		} catch (SQLException e) {
			this.setShowModal(true);
			this.setMensajeModal(e.getMessage());
			this.setTipoModal(TIPO_MODAL_ERROR);
		} catch (Exception e) {
			this.setShowModal(true);
			this.setMensajeModal(e.getMessage());
			this.setTipoModal(TIPO_MODAL_ERROR);
		}
		
	}
	
	private boolean validarMaestroDestino() {
		boolean resultado = true;
		String idFormulario = "idFrMaeServicio";
		if (StringUtils.isBlank(this.getMaestroServicio().getNombre())) {
			this.agregarMensaje(idFormulario + ":idNomServicio",
					"Ingrese el nombre del servicio", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (StringUtils.isNotBlank(this.getMaestroServicio().getDescripcion())){
			if (UtilWeb.obtenerLongitud(this.getMaestroServicio().getDescripcion()) > 300){
				this.agregarMensaje(idFormulario + ":idDescServicio",
						"El tamaño de la descripcion supera los 300 caracteres", "",
						FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
		}
		
		return resultado;
	}

	public void consultarMaestroServicio(int idServicio){
		try {
			this.setNombreFormulario("Editar Servicio");
			this.setNuevoMaestroServicio(false);
			this.setEditarMaestroServicio(true);
			
			this.setMaestroServicio(this.negocioServicio.consultarMaestroServicio(idServicio));
			
			this.setListaMaeServicioImpto(this.getMaestroServicio().getListaServicioDepende());
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void agregarServicioImpto(){
		this.getListaMaeServicioImpto().add(new BaseVO());
	}
	
	public void eliminarServicioImpto(BaseVO idServicio){
		this.getListaMaeServicioImpto().remove(idServicio);
	}

	/**
	 * @return the maestroServicio
	 */
	public MaestroServicio getMaestroServicio() {
		if (maestroServicio == null){
			maestroServicio = new MaestroServicio();
		}
		return maestroServicio;
	}

	/**
	 * @param maestroServicio the maestroServicio to set
	 */
	public void setMaestroServicio(MaestroServicio maestroServicio) {
		this.maestroServicio = maestroServicio;
	}

	/**
	 * @return the nuevoMaestroServicio
	 */
	public boolean isNuevoMaestroServicio() {
		return nuevoMaestroServicio;
	}

	/**
	 * @param nuevoMaestroServicio the nuevoMaestroServicio to set
	 */
	public void setNuevoMaestroServicio(boolean nuevoMaestroServicio) {
		this.nuevoMaestroServicio = nuevoMaestroServicio;
	}

	/**
	 * @return the editarMaestroServicio
	 */
	public boolean isEditarMaestroServicio() {
		return editarMaestroServicio;
	}

	/**
	 * @param editarMaestroServicio the editarMaestroServicio to set
	 */
	public void setEditarMaestroServicio(boolean editarMaestroServicio) {
		this.editarMaestroServicio = editarMaestroServicio;
	}

	/**
	 * @return the listaMaeServicio
	 */
	public List<MaestroServicio> getListaMaeServicio() {
		try {
			
			listaMaeServicio = negocioServicio.listarMaestroServicioAdm();
			this.setShowModal(false);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listaMaeServicio;
	}

	/**
	 * @param listaMaeServicio the listaMaeServicio to set
	 */
	public void setListaMaeServicio(List<MaestroServicio> listaMaeServicio) {
		this.listaMaeServicio = listaMaeServicio;
	}

	/**
	 * @return the listaMaeServicioImpto
	 */
	public List<BaseVO> getListaMaeServicioImpto() {
		if (listaMaeServicioImpto == null){
			listaMaeServicioImpto = new ArrayList<BaseVO>();
		}
		return listaMaeServicioImpto;
	}

	/**
	 * @param listaMaeServicioImpto the listaMaeServicioImpto to set
	 */
	public void setListaMaeServicioImpto(List<BaseVO> listaMaeServicioImpto) {
		this.listaMaeServicioImpto = listaMaeServicioImpto;
	}

}

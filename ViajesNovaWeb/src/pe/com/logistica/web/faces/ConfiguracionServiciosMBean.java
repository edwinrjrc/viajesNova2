/**
 * 
 */
package pe.com.logistica.web.faces;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.ConfiguracionTipoServicio;
import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.negocio.exception.ValidacionException;
import pe.com.logistica.web.servicio.SoporteServicio;
import pe.com.logistica.web.servicio.impl.SoporteServicioImpl;

/**
 * @author Edwin
 *
 */
@ManagedBean(name = "configuracionServiciosMBean")
@SessionScoped()
public class ConfiguracionServiciosMBean extends BaseMBean {

	private final static Logger logger = Logger
			.getLogger(ConfiguracionServiciosMBean.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -3184484901298305785L;

	private List<SelectItem> listaServicios;
	private ConfiguracionTipoServicio configuracionTipoServicio;
	private List<ConfiguracionTipoServicio> listaConfigServicios;

	private SoporteServicio soporteServicio;

	/**
	 * 
	 */
	public ConfiguracionServiciosMBean() {
		try {
			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			soporteServicio = new SoporteServicioImpl(servletContext);
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void ejecutarMetodo() {
		try {
			if (validarConfiguracion()) {
				HttpSession session = obtenerSession(false);
				Usuario usuario = (Usuario) session
						.getAttribute("usuarioSession");

				for (ConfiguracionTipoServicio config : this.listaConfigServicios) {
					config.setUsuarioCreacion(usuario.getUsuario());
					config.setIpCreacion(obtenerRequest().getRemoteAddr());
					config.setUsuarioCreacion(usuario.getUsuario());
					config.setIpCreacion(obtenerRequest().getRemoteAddr());
				}

				this.soporteServicio
						.guardarConfiguracionServicio(this.listaConfigServicios);
			}

			this.setShowModal(true);
			this.setTipoModal("1");
			this.setMensajeModal("Configuracion guardada satisfactoriamente");
		} catch (ValidacionException ex) {
			this.setShowModal(true);
			this.setTipoModal("2");
			this.setMensajeModal(ex.getMessage());
			logger.error(ex.getMessage(), ex);
		} catch (Exception ex) {
			this.setShowModal(true);
			this.setTipoModal("2");
			this.setMensajeModal(ex.getMessage());
			logger.error(ex.getMessage(), ex);
		}
	}

	private boolean validarConfiguracion() throws ValidacionException {
		int cantidad = 0;
		for (ConfiguracionTipoServicio configuracion : this.listaConfigServicios) {
			cantidad = 0;
			for (ConfiguracionTipoServicio configuracion2 : this.listaConfigServicios) {
				if (configuracion.getCodigoEntero().intValue() == configuracion2
						.getCodigoEntero().intValue()) {
					cantidad++;
				}
			}
			if (cantidad > 1) {
				throw new ValidacionException("Tipo de Servicio Repetido");
			}
		}
		return true;
	}

	public void agregarTipoServicio() {
		getListaConfigServicios().add(new ConfiguracionTipoServicio());
	}

	public void eliminar(ConfiguracionTipoServicio configuracion) {
		getListaConfigServicios().remove(configuracion);
	}

	/**
	 * @return the listaServicios
	 */
	public List<SelectItem> getListaServicios() {
		try {
			List<BaseVO> listaTipoServicios = this.soporteServicio
					.listarTiposServicios();
			listaServicios = null;
			listaServicios = new ArrayList<SelectItem>();
			SelectItem si = null;
			for (BaseVO baseVO : listaTipoServicios) {
				si = new SelectItem();
				si.setValue(baseVO.getCodigoEntero());
				si.setLabel(baseVO.getNombre());
				listaServicios.add(si);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listaServicios;
	}

	/**
	 * @param listaServicios
	 *            the listaServicios to set
	 */
	public void setListaServicios(List<SelectItem> listaServicios) {
		this.listaServicios = listaServicios;
	}

	/**
	 * @return the configuracionTipoServicio
	 */
	public ConfiguracionTipoServicio getConfiguracionTipoServicio() {
		return configuracionTipoServicio;
	}

	/**
	 * @param configuracionTipoServicio
	 *            the configuracionTipoServicio to set
	 */
	public void setConfiguracionTipoServicio(
			ConfiguracionTipoServicio configuracionTipoServicio) {
		this.configuracionTipoServicio = configuracionTipoServicio;
	}

	/**
	 * @return the listaConfigServicios
	 */
	public List<ConfiguracionTipoServicio> getListaConfigServicios() {
		try {
			if (listaConfigServicios == null) {
				listaConfigServicios = this.soporteServicio
						.listarConfiguracionServicios();
			}

			this.setShowModal(false);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listaConfigServicios;
	}

	/**
	 * @param listaConfigServicios
	 *            the listaConfigServicios to set
	 */
	public void setListaConfigServicios(
			List<ConfiguracionTipoServicio> listaConfigServicios) {
		this.listaConfigServicios = listaConfigServicios;
	}

}

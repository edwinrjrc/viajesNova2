/**
 * 
 */
package pe.com.logistica.web.faces;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import pe.com.logistica.bean.negocio.Comprobante;
import pe.com.logistica.bean.negocio.ComprobanteBusqueda;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.negocio.exception.ErrorConsultaDataException;
import pe.com.logistica.web.servicio.ConsultaNegocioServicio;
import pe.com.logistica.web.servicio.NegocioServicio;
import pe.com.logistica.web.servicio.impl.ConsultaNegocioServicioImpl;
import pe.com.logistica.web.servicio.impl.NegocioServicioImpl;

/**
 * @author EDWREB
 *
 */
@ManagedBean(name = "comprobanteMBean")
@SessionScoped()
public class ComprobanteMBean extends BaseMBean {

	private final static Logger logger = Logger
			.getLogger(ComprobanteMBean.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 3796481899238208609L;

	private ComprobanteBusqueda comprobanteBusqueda;
	private Comprobante comprobanteDetalle;

	private Proveedor proveedor;

	private List<Comprobante> listaComprobantes;
	private List<Proveedor> listadoProveedores;

	private NegocioServicio negocioServicio;
	private ConsultaNegocioServicio consultaNegocioServicio;

	/**
	 * 
	 */
	public ComprobanteMBean() {
		try {
			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			negocioServicio = new NegocioServicioImpl(servletContext);
			consultaNegocioServicio = new ConsultaNegocioServicioImpl(
					servletContext);
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void buscar() {
		try {
			this.setListaComprobantes(this.consultaNegocioServicio
					.consultarComprobantesGenerados(getComprobanteBusqueda()));
		} catch (ErrorConsultaDataException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void buscarProveedor() {

	}

	public void seleccionarProveedor() {
		for (Proveedor proveedor : this.listadoProveedores) {
			if (proveedor.getCodigoEntero().equals(
					proveedor.getCodigoSeleccionado())) {
				this.getComprobanteBusqueda().setProveedor(proveedor);
				break;
			}
		}
	}

	public void consultarComprobante(Integer idComprobante) {
		try {
			this.setComprobanteDetalle(null);
			this.setComprobanteDetalle(this.consultaNegocioServicio
					.consultarComprobanteGenerado(idComprobante));
		} catch (ErrorConsultaDataException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ========================================================================
	 * ===============================================================
	 */

	/**
	 * @return the comprobanteBusqueda
	 */
	public ComprobanteBusqueda getComprobanteBusqueda() {
		if (comprobanteBusqueda == null) {
			comprobanteBusqueda = new ComprobanteBusqueda();

			Calendar cal = Calendar.getInstance();
			comprobanteBusqueda.setFechaHasta(cal.getTime());
			cal.add(Calendar.MONTH, -1);
			comprobanteBusqueda.setFechaDesde(cal.getTime());
		}
		return comprobanteBusqueda;
	}

	/**
	 * @param comprobanteBusqueda
	 *            the comprobanteBusqueda to set
	 */
	public void setComprobanteBusqueda(ComprobanteBusqueda comprobanteBusqueda) {
		this.comprobanteBusqueda = comprobanteBusqueda;
	}

	/**
	 * @return the listaComprobantes
	 */
	public List<Comprobante> getListaComprobantes() {
		if (listaComprobantes == null) {
			listaComprobantes = new ArrayList<Comprobante>();
		}
		return listaComprobantes;
	}

	/**
	 * @param listaComprobantes
	 *            the listaComprobantes to set
	 */
	public void setListaComprobantes(List<Comprobante> listaComprobantes) {
		this.listaComprobantes = listaComprobantes;
	}

	/**
	 * @return the proveedor
	 */
	public Proveedor getProveedor() {
		if (proveedor == null) {
			proveedor = new Proveedor();
		}
		return proveedor;
	}

	/**
	 * @param proveedor
	 *            the proveedor to set
	 */
	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	/**
	 * @return the listadoProveedores
	 */
	public List<Proveedor> getListadoProveedores() {
		if (listadoProveedores == null) {
			listadoProveedores = new ArrayList<Proveedor>();
		}
		return listadoProveedores;
	}

	/**
	 * @param listadoProveedores
	 *            the listadoProveedores to set
	 */
	public void setListadoProveedores(List<Proveedor> listadoProveedores) {
		this.listadoProveedores = listadoProveedores;
	}

	/**
	 * @return the comprobanteDetalle
	 */
	public Comprobante getComprobanteDetalle() {
		if (comprobanteDetalle == null) {
			comprobanteDetalle = new Comprobante();
		}
		return comprobanteDetalle;
	}

	/**
	 * @param comprobanteDetalle
	 *            the comprobanteDetalle to set
	 */
	public void setComprobanteDetalle(Comprobante comprobanteDetalle) {
		this.comprobanteDetalle = comprobanteDetalle;
	}

}

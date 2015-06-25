/**
 * 
 */
package pe.com.logistica.web.faces;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Consolidador;
import pe.com.logistica.bean.negocio.Destino;
import pe.com.logistica.bean.negocio.MaestroServicio;
import pe.com.logistica.bean.negocio.Parametro;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.negocio.exception.ConnectionException;
import pe.com.logistica.web.servicio.NegocioServicio;
import pe.com.logistica.web.servicio.ParametroServicio;
import pe.com.logistica.web.servicio.SeguridadServicio;
import pe.com.logistica.web.servicio.SoporteServicio;
import pe.com.logistica.web.servicio.impl.NegocioServicioImpl;
import pe.com.logistica.web.servicio.impl.ParametroServicioImpl;
import pe.com.logistica.web.servicio.impl.SeguridadServicioImpl;
import pe.com.logistica.web.servicio.impl.SoporteServicioImpl;
import pe.com.logistica.web.util.UtilWeb;

/**
 * @author Edwin
 * 
 */
@ManagedBean(name = "catalogoMBean")
@SessionScoped()
public class CatalogoMBean implements Serializable{
	
	private final static Logger logger = Logger.getLogger(CatalogoMBean.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -571289965929551249L;
	
	
	private List<SelectItem> catalogoRoles;
	private List<SelectItem> catalogoTipoDocumento;
	private List<SelectItem> catalogoRubro;
	private List<SelectItem> catalogoArea;
	private List<SelectItem> catalogoVias;
	private List<SelectItem> catalogoDepartamento;
	private List<SelectItem> catalogoOperadoraMovil;
	private List<SelectItem> catalogoEstadoCivil;
	private List<SelectItem> catalogoContinente;
	private List<SelectItem> catalogoTipoDestino;
	private List<SelectItem> catalogoDestino;
	private List<SelectItem> catalogoTipoServicio;
	private List<SelectItem> catalogoTipoServicioIgv;
	private List<SelectItem> catalogoTipoServicioFee;
	private List<SelectItem> catalogoTipoServicioImpto;
	private List<SelectItem> catalogoFormaPago;
	private List<SelectItem> catalogoVendedores;
	private List<SelectItem> catalogoConsolidadores;
	private List<SelectItem> catalogoTipoProveedor;
	private List<SelectItem> catalogoParametros;
	private List<SelectItem> catalogoProveedores;
	private List<SelectItem> catalogoAerolineas;
	private List<SelectItem> catalogoHoteles;
	private List<SelectItem> catalogoOperador;
	private List<SelectItem> catalogoTipoComprobante;
	private List<SelectItem> catalogoDocumentosAdicionales;

	private SeguridadServicio seguridadServicio;
	private SoporteServicio soporteServicio;
	private NegocioServicio negocioServicio;
	private ParametroServicio parametroServicio;

	public CatalogoMBean() {
		try {
			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			seguridadServicio = new SeguridadServicioImpl(servletContext);
			soporteServicio = new SoporteServicioImpl(servletContext);
			negocioServicio = new NegocioServicioImpl(servletContext);
			parametroServicio = new ParametroServicioImpl(servletContext);
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * @return the catalogoRoles
	 */
	public List<SelectItem> getCatalogoRoles() {
		List<BaseVO> lista;
		try {
			lista = seguridadServicio.listarRoles();
			catalogoRoles = UtilWeb.convertirSelectItem(lista);
		} catch (ConnectionException e) {
			logger.error(e.getMessage(), e);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
		
		return catalogoRoles;
	}

	/**
	 * @param catalogoRoles
	 *            the catalogoRoles to set
	 */
	public void setCatalogoRoles(List<SelectItem> catalogoRoles) {
		this.catalogoRoles = catalogoRoles;
	}

	/**
	 * @return the catalogoTipoDocumento
	 */
	public List<SelectItem> getCatalogoTipoDocumento() {
		int idmaestro = UtilWeb.obtenerEnteroPropertieMaestro(
				"maestroTipoDocumento", "aplicacionDatos");

		try {
			List<BaseVO> lista = soporteServicio
					.listarCatalogoMaestro(idmaestro);
			catalogoTipoDocumento = UtilWeb.convertirSelectItem(lista);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return catalogoTipoDocumento;
	}

	/**
	 * @param catalogoTipoDocumento
	 *            the catalogoTipoDocumento to set
	 */
	public void setCatalogoTipoDocumento(List<SelectItem> catalogoTipoDocumento) {
		this.catalogoTipoDocumento = catalogoTipoDocumento;
	}

	/**
	 * @return the catalogoRubro
	 */
	public List<SelectItem> getCatalogoRubro() {
		int idmaestro = UtilWeb.obtenerEnteroPropertieMaestro("maestroRubo",
				"aplicacionDatos");
		try {
			List<BaseVO> lista = soporteServicio
					.listarCatalogoMaestro(idmaestro);
			catalogoRubro = UtilWeb.convertirSelectItem(lista);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return catalogoRubro;
	}

	/**
	 * @param catalogoRubro
	 *            the catalogoRubro to set
	 */
	public void setCatalogoRubro(List<SelectItem> catalogoRubro) {
		this.catalogoRubro = catalogoRubro;
	}

	/**
	 * @return the catalogoDepartamento
	 */
	public List<SelectItem> getCatalogoDepartamento() {
		try {
			List<BaseVO> lista = soporteServicio.listarCatalogoDepartamento();
			catalogoDepartamento = UtilWeb.convertirSelectItem(lista);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (ConnectionException e) {
			logger.error(e.getMessage(), e);
		}
		return catalogoDepartamento;
	}

	/**
	 * @param catalogoDepartamento
	 *            the catalogoDepartamento to set
	 */
	public void setCatalogoDepartamento(List<SelectItem> catalogoDepartamento) {
		this.catalogoDepartamento = catalogoDepartamento;
	}

	/**
	 * @return the catalogoVias
	 */
	public List<SelectItem> getCatalogoVias() {
		try {
			int idmaestro = UtilWeb.obtenerEnteroPropertieMaestro(
					"maestroVias", "aplicacionDatos");
			List<BaseVO> lista = soporteServicio
					.listarCatalogoMaestro(idmaestro);
			catalogoVias = UtilWeb.convertirSelectItem(lista);
		} catch (SQLException e) {
			catalogoVias = new ArrayList<SelectItem>();
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			catalogoVias = new ArrayList<SelectItem>();
			logger.error(e.getMessage(), e);
		}
		return catalogoVias;
	}

	/**
	 * @param catalogoVias
	 *            the catalogoVias to set
	 */
	public void setCatalogoVias(List<SelectItem> catalogoVias) {
		this.catalogoVias = catalogoVias;
	}

	/**
	 * @return the catalogoArea
	 */
	public List<SelectItem> getCatalogoArea() {
		try {
			int idmaestro = UtilWeb.obtenerEnteroPropertieMaestro(
					"maestroAreas", "aplicacionDatos");
			List<BaseVO> lista = soporteServicio
					.listarCatalogoMaestro(idmaestro);
			catalogoArea = UtilWeb.convertirSelectItem(lista);
		} catch (SQLException e) {
			catalogoArea = new ArrayList<SelectItem>();
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			catalogoArea = new ArrayList<SelectItem>();
			logger.error(e.getMessage(), e);
		}
		return catalogoArea;
	}

	/**
	 * @param catalogoArea
	 *            the catalogoArea to set
	 */
	public void setCatalogoArea(List<SelectItem> catalogoArea) {
		this.catalogoArea = catalogoArea;
	}

	/**
	 * @return the catalogoOperadoraMovil
	 */
	public List<SelectItem> getCatalogoOperadoraMovil() {
		try {
			int idmaestro = UtilWeb.obtenerEnteroPropertieMaestro(
					"maestroOperadoraMovil", "aplicacionDatos");
			List<BaseVO> lista = soporteServicio
					.listarCatalogoMaestro(idmaestro);
			catalogoOperadoraMovil = UtilWeb.convertirSelectItem(lista);
		} catch (SQLException e) {
			catalogoOperadoraMovil = new ArrayList<SelectItem>();
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			catalogoOperadoraMovil = new ArrayList<SelectItem>();
			logger.error(e.getMessage(), e);
		}
		return catalogoOperadoraMovil;
	}

	/**
	 * @param catalogoOperadoraMovil
	 *            the catalogoOperadoraMovil to set
	 */
	public void setCatalogoOperadoraMovil(
			List<SelectItem> catalogoOperadoraMovil) {
		this.catalogoOperadoraMovil = catalogoOperadoraMovil;
	}

	/**
	 * @return the catalogoEstadoCivil
	 */
	public List<SelectItem> getCatalogoEstadoCivil() {
		try {
			int idmaestro = UtilWeb.obtenerEnteroPropertieMaestro(
					"maestroEstadoCivil", "aplicacionDatos");
			List<BaseVO> lista = soporteServicio
					.listarCatalogoMaestro(idmaestro);
			catalogoEstadoCivil = UtilWeb.convertirSelectItem(lista);
		} catch (SQLException e) {
			catalogoEstadoCivil = new ArrayList<SelectItem>();
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			catalogoEstadoCivil = new ArrayList<SelectItem>();
			logger.error(e.getMessage(), e);
		}
		return catalogoEstadoCivil;
	}

	/**
	 * @param catalogoEstadoCivil
	 *            the catalogoEstadoCivil to set
	 */
	public void setCatalogoEstadoCivil(List<SelectItem> catalogoEstadoCivil) {
		this.catalogoEstadoCivil = catalogoEstadoCivil;
	}

	/**
	 * @return the catalogoContinente
	 */
	public List<SelectItem> getCatalogoContinente() {
		int idmaestro = UtilWeb.obtenerEnteroPropertieMaestro(
				"maestroContinente", "aplicacionDatos");

		try {
			List<BaseVO> lista = soporteServicio
					.listarCatalogoMaestro(idmaestro);
			catalogoContinente = UtilWeb.convertirSelectItem(lista);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return catalogoContinente;
	}

	/**
	 * @param catalogoContinente
	 *            the catalogoContinente to set
	 */
	public void setCatalogoContinente(List<SelectItem> catalogoContinente) {
		this.catalogoContinente = catalogoContinente;
	}

	/**
	 * @return the catalogoTipoDestino
	 */
	public List<SelectItem> getCatalogoTipoDestino() {
		int idmaestro = UtilWeb.obtenerEnteroPropertieMaestro(
				"maestroTipoDestino", "aplicacionDatos");

		try {
			List<BaseVO> lista = soporteServicio
					.listarCatalogoMaestro(idmaestro);
			catalogoTipoDestino = UtilWeb.convertirSelectItem(lista);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return catalogoTipoDestino;
	}

	/**
	 * @param catalogoTipoDestino
	 *            the catalogoTipoDestino to set
	 */
	public void setCatalogoTipoDestino(List<SelectItem> catalogoTipoDestino) {
		this.catalogoTipoDestino = catalogoTipoDestino;
	}

	/**
	 * @return the catalogoDestino
	 */
	public List<SelectItem> getCatalogoDestino() {
		try {
			catalogoDestino = new ArrayList<SelectItem>();
			List<Destino> listaDestino = this.soporteServicio.listarDestinos();
			SelectItem si = null;
			for (Destino destino : listaDestino) {
				si = new SelectItem();
				si.setValue(destino.getCodigoEntero());
				String descripcionCompleto = destino.getDescripcion()+"("+destino.getCodigoIATA()+")"; 
				si.setLabel(descripcionCompleto);
				catalogoDestino.add(si);
			}

		} catch (SQLException e) {
			catalogoDestino = new ArrayList<SelectItem>();
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			catalogoDestino = new ArrayList<SelectItem>();
			logger.error(e.getMessage(), e);
		}
		return catalogoDestino;
	}

	/**
	 * @param catalogoDestino the catalogoDestino to set
	 */
	public void setCatalogoDestino(List<SelectItem> catalogoDestino) {
		this.catalogoDestino = catalogoDestino;
	}

	/**
	 * @return the catalogoTipoServicio
	 */
	public List<SelectItem> getCatalogoTipoServicio() {
		try {
			List<MaestroServicio> lista = negocioServicio.listarMaestroServicio();
			SelectItem si = null;
			catalogoTipoServicio = new ArrayList<SelectItem>();
			for (MaestroServicio maestroServicio : lista) {
				si = new SelectItem();
				si.setLabel(maestroServicio.getNombre());
				si.setValue(maestroServicio.getCodigoEntero());
				catalogoTipoServicio.add(si);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		/*int idmaestro = UtilWeb.obtenerEnteroPropertieMaestro(
				"maestroTipoServicios", "aplicacionDatos");

		try {
			List<BaseVO> lista = soporteServicio
					.listarCatalogoMaestro(idmaestro);
			catalogoTipoServicio = UtilWeb.convertirSelectItem(lista);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}*/
		return catalogoTipoServicio;
	}

	/**
	 * @param catalogoTipoServicio the catalogoTipoServicio to set
	 */
	public void setCatalogoTipoServicio(List<SelectItem> catalogoTipoServicio) {
		this.catalogoTipoServicio = catalogoTipoServicio;
	}

	/**
	 * @return the catalogoFormaPago
	 */
	public List<SelectItem> getCatalogoFormaPago() {
		int idmaestro = UtilWeb.obtenerEnteroPropertieMaestro(
				"maestroFormaPago", "aplicacionDatos");

		try {
			List<BaseVO> lista = soporteServicio
					.listarCatalogoMaestro(idmaestro);
			catalogoFormaPago = UtilWeb.convertirSelectItem(lista);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return catalogoFormaPago;
	}

	/**
	 * @param catalogoFormaPago the catalogoFormaPago to set
	 */
	public void setCatalogoFormaPago(List<SelectItem> catalogoFormaPago) {
		this.catalogoFormaPago = catalogoFormaPago;
	}

	/**
	 * @return the catalogoVendedores
	 */
	public List<SelectItem> getCatalogoVendedores() {
		try {
			catalogoVendedores = new ArrayList<SelectItem>();
			List<Usuario> listaVendedores = this.seguridadServicio.listarVendedores();
			SelectItem si = null;
			for (Usuario usuario : listaVendedores) {
				si = new SelectItem();
				si.setValue(usuario.getCodigoEntero());
				String descripcionCompleto = usuario.getNombreCompleto();
				si.setLabel(descripcionCompleto);
				catalogoVendedores.add(si);
			}

		} catch (SQLException e) {
			catalogoVendedores = new ArrayList<SelectItem>();
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			catalogoVendedores = new ArrayList<SelectItem>();
			logger.error(e.getMessage(), e);
		}
		return catalogoVendedores;
	}

	/**
	 * @param catalogoVendedores the catalogoVendedores to set
	 */
	public void setCatalogoVendedores(List<SelectItem> catalogoVendedores) {
		this.catalogoVendedores = catalogoVendedores;
	}

	/**
	 * @return the catalogoTipoServicioFee
	 */
	public List<SelectItem> getCatalogoTipoServicioFee() {
		try {
			List<MaestroServicio> lista = negocioServicio.listarMaestroServicioFee();
			SelectItem si = null;
			catalogoTipoServicioFee = new ArrayList<SelectItem>();
			for (MaestroServicio maestroServicio : lista) {
				si = new SelectItem();
				si.setLabel(maestroServicio.getNombre());
				si.setValue(maestroServicio.getCodigoEntero());
				catalogoTipoServicioFee.add(si);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return catalogoTipoServicioFee;
	}

	/**
	 * @param catalogoTipoServicioFee the catalogoTipoServicioFee to set
	 */
	public void setCatalogoTipoServicioFee(List<SelectItem> catalogoTipoServicioFee) {
		this.catalogoTipoServicioFee = catalogoTipoServicioFee;
	}

	/**
	 * @return the catalogoTipoServicioImpto
	 */
	public List<SelectItem> getCatalogoTipoServicioImpto() {
		try {
			List<MaestroServicio> lista = negocioServicio.listarMaestroServicioImpto();
			SelectItem si = null;
			catalogoTipoServicioImpto = new ArrayList<SelectItem>();
			for (MaestroServicio maestroServicio : lista) {
				si = new SelectItem();
				si.setLabel(maestroServicio.getNombre());
				si.setValue(maestroServicio.getCodigoEntero());
				catalogoTipoServicioImpto.add(si);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return catalogoTipoServicioImpto;
	}

	/**
	 * @param catalogoTipoServicioImpto the catalogoTipoServicioImpto to set
	 */
	public void setCatalogoTipoServicioImpto(
			List<SelectItem> catalogoTipoServicioImpto) {
		this.catalogoTipoServicioImpto = catalogoTipoServicioImpto;
	}

	/**
	 * @return the catalogoTipoServicioIgv
	 */
	public List<SelectItem> getCatalogoTipoServicioIgv() {
		try {
			List<MaestroServicio> lista = negocioServicio.listarMaestroServicioIgv();
			SelectItem si = null;
			catalogoTipoServicioIgv = new ArrayList<SelectItem>();
			for (MaestroServicio maestroServicio : lista) {
				si = new SelectItem();
				si.setLabel(maestroServicio.getNombre());
				si.setValue(maestroServicio.getCodigoEntero());
				catalogoTipoServicioIgv.add(si);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return catalogoTipoServicioIgv;
	}

	/**
	 * @param catalogoTipoServicioIgv the catalogoTipoServicioIgv to set
	 */
	public void setCatalogoTipoServicioIgv(List<SelectItem> catalogoTipoServicioIgv) {
		this.catalogoTipoServicioIgv = catalogoTipoServicioIgv;
	}

	/**
	 * @return the catalogoParametros
	 */
	public List<SelectItem> getCatalogoParametros() {
		try {
			List<Parametro> lista = parametroServicio.listarParametros();
			SelectItem si = null;
			catalogoParametros = new ArrayList<SelectItem>();
			for (Parametro parametro : lista) {
				si = new SelectItem();
				si.setLabel(parametro.getNombre());
				si.setValue(parametro.getCodigoEntero());
				catalogoParametros.add(si);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return catalogoParametros;
	}

	/**
	 * @param catalogoParametros the catalogoParametros to set
	 */
	public void setCatalogoParametros(List<SelectItem> catalogoParametros) {
		this.catalogoParametros = catalogoParametros;
	}

	/**
	 * @return the catalogoConsolidadores
	 */
	public List<SelectItem> getCatalogoConsolidadores() {
		try {
			List<Consolidador> lista = this.negocioServicio.listarConsolidador();
			SelectItem si = null;
			catalogoConsolidadores = new ArrayList<SelectItem>();
			for (Consolidador consolidador : lista) {
				si = new SelectItem();
				si.setLabel(consolidador.getNombre());
				si.setValue(consolidador.getCodigoEntero());
				catalogoConsolidadores.add(si);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return catalogoConsolidadores;
	}

	/**
	 * @param catalogoConsolidadores the catalogoConsolidadores to set
	 */
	public void setCatalogoConsolidadores(List<SelectItem> catalogoConsolidadores) {
		this.catalogoConsolidadores = catalogoConsolidadores;
	}

	/**
	 * @return the catalogoTipoProveedor
	 */
	public List<SelectItem> getCatalogoTipoProveedor() {
		int idmaestro = UtilWeb.obtenerEnteroPropertieMaestro(
				"maestroTipoProveedor", "aplicacionDatos");

		try {
			List<BaseVO> lista = soporteServicio
					.listarCatalogoMaestro(idmaestro);
			catalogoTipoProveedor = UtilWeb.convertirSelectItem(lista);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return catalogoTipoProveedor;
	}

	/**
	 * @param catalogoTipoProveedor the catalogoTipoProveedor to set
	 */
	public void setCatalogoTipoProveedor(List<SelectItem> catalogoTipoProveedor) {
		this.catalogoTipoProveedor = catalogoTipoProveedor;
	}

	/**
	 * @return the catalogoProveedores
	 */
	public List<SelectItem> getCatalogoProveedores() {
		try {
			List<Proveedor> lista = this.negocioServicio.listarProveedor(new Proveedor());
			SelectItem si = null;
			catalogoProveedores = new ArrayList<SelectItem>();
			for (Proveedor proveedor : lista) {
				si = new SelectItem();
				si.setLabel(proveedor.getNombreCompleto());
				si.setValue(proveedor.getCodigoEntero());
				catalogoProveedores.add(si);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return catalogoProveedores;
	}

	/**
	 * @param catalogoProveedores the catalogoProveedores to set
	 */
	public void setCatalogoProveedores(List<SelectItem> catalogoProveedores) {
		this.catalogoProveedores = catalogoProveedores;
	}

	/**
	 * @return the catalogoAerolineas
	 */
	public List<SelectItem> getCatalogoAerolineas() {
		try {
			BaseVO tipoProveedor = new BaseVO();
			tipoProveedor.setCodigoEntero(1);
			List<Proveedor> lista = this.soporteServicio.listarComboProveedorTipo(tipoProveedor);
			SelectItem si = null;
			catalogoAerolineas = new ArrayList<SelectItem>();
			for (Proveedor proveedor : lista) {
				si = new SelectItem();
				si.setLabel(proveedor.getNombreCompleto());
				si.setValue(proveedor.getCodigoEntero());
				catalogoAerolineas.add(si);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return catalogoAerolineas;
	}

	/**
	 * @param catalogoAerolineas the catalogoAerolineas to set
	 */
	public void setCatalogoAerolineas(List<SelectItem> catalogoAerolineas) {
		this.catalogoAerolineas = catalogoAerolineas;
	}

	/**
	 * @return the catalogoHoteles
	 */
	public List<SelectItem> getCatalogoHoteles() {
		try {
			BaseVO tipoProveedor = new BaseVO();
			tipoProveedor.setCodigoEntero(6);
			List<Proveedor> lista = this.soporteServicio.listarComboProveedorTipo(tipoProveedor);
			SelectItem si = null;
			catalogoHoteles = new ArrayList<SelectItem>();
			for (Proveedor proveedor : lista) {
				si = new SelectItem();
				si.setLabel(proveedor.getNombreCompleto());
				si.setValue(proveedor.getCodigoEntero());
				catalogoHoteles.add(si);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return catalogoHoteles;
	}

	/**
	 * @param catalogoHoteles the catalogoHoteles to set
	 */
	public void setCatalogoHoteles(List<SelectItem> catalogoHoteles) {
		this.catalogoHoteles = catalogoHoteles;
	}

	/**
	 * @return the catalogoOperador
	 */
	public List<SelectItem> getCatalogoOperador() {
		try {
			BaseVO tipoProveedor = new BaseVO();
			tipoProveedor.setCodigoEntero(3);
			List<Proveedor> lista = this.soporteServicio.listarComboProveedorTipo(tipoProveedor);
			SelectItem si = null;
			catalogoOperador = new ArrayList<SelectItem>();
			for (Proveedor proveedor : lista) {
				si = new SelectItem();
				si.setLabel(proveedor.getNombreCompleto());
				si.setValue(proveedor.getCodigoEntero());
				catalogoOperador.add(si);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return catalogoOperador;
	}

	/**
	 * @param catalogoOperador the catalogoOperador to set
	 */
	public void setCatalogoOperador(List<SelectItem> catalogoOperador) {
		this.catalogoOperador = catalogoOperador;
	}

	/**
	 * @return the catalogoTipoComprobante
	 */
	public List<SelectItem> getCatalogoTipoComprobante() {
		int idmaestro = UtilWeb.obtenerEnteroPropertieMaestro(
				"maestroTipoComprobante", "aplicacionDatos");

		try {
			List<BaseVO> lista = soporteServicio
					.listarCatalogoMaestro(idmaestro);
			catalogoTipoComprobante = UtilWeb.convertirSelectItem(lista);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return catalogoTipoComprobante;
	}

	/**
	 * @param catalogoTipoComprobante the catalogoTipoComprobante to set
	 */
	public void setCatalogoTipoComprobante(List<SelectItem> catalogoTipoComprobante) {
		this.catalogoTipoComprobante = catalogoTipoComprobante;
	}

	/**
	 * @return the catalogoDocumentosAdicionales
	 */
	public List<SelectItem> getCatalogoDocumentosAdicionales() {
		int idmaestro = UtilWeb.obtenerEnteroPropertieMaestro(
				"maestroDocumentos", "aplicacionDatos");

		try {
			List<BaseVO> lista = soporteServicio
					.listarCatalogoMaestro(idmaestro);
			catalogoDocumentosAdicionales = UtilWeb.convertirSelectItem(lista);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return catalogoDocumentosAdicionales;
	}

	/**
	 * @param catalogoDocumentosAdicionales the catalogoDocumentosAdicionales to set
	 */
	public void setCatalogoDocumentosAdicionales(
			List<SelectItem> catalogoDocumentosAdicionales) {
		this.catalogoDocumentosAdicionales = catalogoDocumentosAdicionales;
	}

}

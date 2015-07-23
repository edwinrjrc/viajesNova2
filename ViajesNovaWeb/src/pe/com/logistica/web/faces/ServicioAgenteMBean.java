/**
 * 
 */
package pe.com.logistica.web.faces;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import pe.com.logistica.bean.Util.UtilParse;
import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Cliente;
import pe.com.logistica.bean.negocio.Comprobante;
import pe.com.logistica.bean.negocio.ConfiguracionTipoServicio;
import pe.com.logistica.bean.negocio.Destino;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.DocumentoAdicional;
import pe.com.logistica.bean.negocio.EventoObsAnu;
import pe.com.logistica.bean.negocio.MaestroServicio;
import pe.com.logistica.bean.negocio.PagoServicio;
import pe.com.logistica.bean.negocio.Parametro;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.bean.negocio.ServicioAgenciaBusqueda;
import pe.com.logistica.bean.negocio.ServicioProveedor;
import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;
import pe.com.logistica.negocio.exception.ValidacionException;
import pe.com.logistica.web.servicio.NegocioServicio;
import pe.com.logistica.web.servicio.ParametroServicio;
import pe.com.logistica.web.servicio.SoporteServicio;
import pe.com.logistica.web.servicio.UtilNegocioServicio;
import pe.com.logistica.web.servicio.impl.NegocioServicioImpl;
import pe.com.logistica.web.servicio.impl.ParametroServicioImpl;
import pe.com.logistica.web.servicio.impl.SoporteServicioImpl;
import pe.com.logistica.web.servicio.impl.UtilNegocioServicioImpl;
import pe.com.logistica.web.util.UtilWeb;

/**
 * @author Edwin
 * 
 */
@ManagedBean(name = "servicioAgenteMBean")
@SessionScoped()
public class ServicioAgenteMBean extends BaseMBean {

	private final static Logger logger = Logger
			.getLogger(ServicioAgenteMBean.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 3451688997471435575L;

	private ServicioAgencia servicioAgencia;
	private ServicioAgenciaBusqueda servicioAgenciaBusqueda;
	private DetalleServicioAgencia detalleServicio;
	private DetalleServicioAgencia detalleServicio2;
	private Cliente clienteBusqueda;
	private Destino destinoBusqueda;
	private Destino origenBusqueda;
	private PagoServicio pagoServicio;
	private PagoServicio pagoServicio2;
	private EventoObsAnu eventoObsAnu;
	private Comprobante comprobante;
	private Comprobante comprobanteBusqueda;
	private BaseVO tipoServicio;
	private Proveedor proveedorBusqueda;
	private DocumentoAdicional documentoAdicional;

	private BigDecimal saldoServicio;

	private List<ServicioAgencia> listadoServicioAgencia;
	private List<DetalleServicioAgencia> listadoDetalleServicio;
	private List<DetalleServicioAgencia> listadoDetalleServicioAgrupado;
	private List<Cliente> listadoClientes;
	private List<SelectItem> listadoEmpresas;
	private List<ServicioProveedor> listaProveedores;
	private List<Destino> listaDestinosBusqueda;
	private List<Destino> listaOrigenesBusqueda;
	private List<PagoServicio> listaPagosServicios;
	private List<PagoServicio> listaPagosComprobante;
	private List<Comprobante> listaComprobantes;
	private List<Proveedor> listadoProveedores;
	private List<DocumentoAdicional> listaDocumentosAdicionales;
	private List<Comprobante> listaComprobantesAdicionales;
	private List<SelectItem> listadoServiciosPadre;

	private boolean nuevaVenta;
	private boolean editarVenta;
	private boolean servicioFee;
	private boolean agregoServicioPadre;
	private boolean busquedaRealizada;
	private boolean editarComision;
	private boolean vendedor;
	private boolean calculadorIGV;
	private boolean guardoComprobantes;
	private boolean guardoRelacionComprobantes;
	private boolean consultoProveedor;
	private boolean editaServicioAgregado;
	private boolean cargoConfiguracionTipoServicio;

	private ParametroServicio parametroServicio;
	private NegocioServicio negocioServicio;
	private UtilNegocioServicio utilNegocioServicio;
	private SoporteServicio soporteServicio;

	private String pregunta;
	private String nombreCampoTexto;
	private String nombreTitulo;
	private Integer tipoEvento;
	private Integer columnasComprobantes;
	private String idModales;

	/**
	 * 
	 */
	public ServicioAgenteMBean() {
		try {
			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			parametroServicio = new ParametroServicioImpl(servletContext);
			negocioServicio = new NegocioServicioImpl(servletContext);
			soporteServicio = new SoporteServicioImpl(servletContext);
			utilNegocioServicio = new UtilNegocioServicioImpl(servletContext);
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}

		consultarTasaPredeterminada();
	}

	public void consultarClientes() {
		try {
			this.setClienteBusqueda(null);

			this.setListadoClientes(this.negocioServicio.listarCliente());
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void consultarDestinos() {
		try {
			this.setListaDestinosBusqueda(null);
			this.setDestinoBusqueda(null);

			List<Destino> listaDestinos = this.soporteServicio.listarDestinos();

			this.setListaDestinosBusqueda(listaDestinos);
			this.setListaOrigenesBusqueda(listaDestinos);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void buscarCliente() {
		try {
			this.setListadoClientes(this.negocioServicio
					.buscarCliente(getClienteBusqueda()));
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}

	}

	public void seleccionarCliente() {
		this.getServicioAgencia().setCliente(obtenerClienteListado());
	}

	public void seleccionarDestino() {
		try {
			this.getDetalleServicio().setDestino(obtenerDestinoListado());

			this.getDetalleServicio()
					.getServicioProveedor()
					.setPorcentajeComision(
							this.negocioServicio.calculaPorcentajeComision(this
									.getDetalleServicio()));
		} catch (SQLException e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		}
	}

	private Destino obtenerDestinoListado() {
		try {
			for (Destino destino : this.listaDestinosBusqueda) {
				if (destino.getCodigoEntero().intValue() == destino
						.getCodigoSeleccionado().intValue()) {
					return destino;
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	private Cliente obtenerClienteListado() {
		try {
			for (Cliente clienteLocal : this.getListadoClientes()) {
				if (clienteLocal.getCodigoEntero().equals(
						clienteLocal.getCodigoSeleccionado())) {
					return clienteLocal;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	public void buscarServicioRegistrado() {
		try {
			HttpSession session = obtenerSession(false);
			Usuario usuario = (Usuario) session.getAttribute(USUARIO_SESSION);
			if (Integer.valueOf(2).equals(usuario.getRol().getCodigoEntero())
					|| Integer.valueOf(4).equals(
							usuario.getRol().getCodigoEntero())) {
				getServicioAgenciaBusqueda().getVendedor().setCodigoEntero(
						usuario.getCodigoEntero());
			}

			listadoServicioAgencia = this.negocioServicio
					.listarVentaServicio(getServicioAgenciaBusqueda());

			this.setBusquedaRealizada(true);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void consultarServicioRegistrado(int idServicio) {
		try {
			this.setGuardoComprobantes(false);
			this.setGuardoRelacionComprobantes(false);
			this.setServicioAgencia(this.negocioServicio
					.consultarVentaServicio(idServicio));

			this.setNombreFormulario("Editar Registro Venta");
			this.setNuevaVenta(false);
			this.setEditarVenta(true);
			this.setListadoDetalleServicio(this.getServicioAgencia()
					.getListaDetalleServicio());

			HttpSession session = obtenerSession(false);
			Usuario usuario = (Usuario) session.getAttribute("usuarioSession");

			this.setColumnasComprobantes(9);

			if (usuario.getRol().getCodigoEntero().intValue() == 3) {
				if (this.getServicioAgencia().isGuardoRelacionComprobantes()) {
					this.setGuardoComprobantes(true);
					this.setGuardoRelacionComprobantes(true);
					this.getServicioAgencia().setListaDetalleServicio(
							this.negocioServicio
									.consultarDetServComprobanteObligacion(this
											.getServicioAgencia()
											.getCodigoEntero()));
					this.setColumnasComprobantes(10);
				} else if (this.getServicioAgencia().isGuardoComprobante()) {
					this.setGuardoComprobantes(true);
					this.getServicioAgencia().setListaDetalleServicio(
							this.negocioServicio
									.consultarDetalleComprobantes(this
											.getServicioAgencia()
											.getCodigoEntero()));
					this.setColumnasComprobantes(10);
				}

				this.setListadoDetalleServicioAgrupado(this.utilNegocioServicio
						.agruparServicios(this.getServicioAgencia()
								.getListaDetalleServicio()));
			}

			this.setListaDocumentosAdicionales(this.negocioServicio
					.listarDocumentosAdicionales(idServicio));

			this.setDetalleServicio(null);
			borrarInvisibles();
			calcularTotales();
			this.setTransaccionExito(false);

		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void borrarInvisibles() {
		for (DetalleServicioAgencia detalle : this.getListadoDetalleServicio()) {
			if (!detalle.getTipoServicio().isVisible()) {
				this.getListadoDetalleServicio().remove(detalle);
			}
		}
	}

	public void registrarNuevaVenta() {
		this.setNombreFormulario("Nuevo Registro Venta");
		this.setNuevaVenta(true);
		this.setEditarVenta(false);
		this.setServicioAgencia(null);
		this.setDetalleServicio(null);
		this.setTransaccionExito(false);
		this.setEditaServicioAgregado(false);

		consultarTasaPredeterminada();
		this.setListadoEmpresas(null);
		this.setListadoDetalleServicio(null);
		this.setListaDocumentosAdicionales(null);
		this.setListadoServiciosPadre(null);

		this.setVendedor(false);
		HttpSession session = obtenerSession(false);
		Usuario usuario = (Usuario) session.getAttribute(USUARIO_SESSION);
		if (Integer.valueOf(2).equals(usuario.getRol().getCodigoEntero())) {
			this.getServicioAgencia().getVendedor()
					.setCodigoEntero(usuario.getCodigoEntero());
			this.getServicioAgencia().getVendedor()
					.setNombre(usuario.getNombreCompleto());
			this.setVendedor(true);
		}

		this.getServicioAgencia().setFechaServicio(new Date());
		this.setListaDocumentosAdicionales(null);
	}

	public void agregarServicio() {
		try {
			if (validarServicioVenta()) {
				getDetalleServicio().getServicioProveedor().setEditoComision(
						this.isEditarComision());
				
				seleccionarOrigenDestino();

				this.setListadoDetalleServicio(this.utilNegocioServicio
						.agregarServicioVenta(this.getListadoDetalleServicio(),
								getDetalleServicio()));

				this.setDetalleServicio(null);

				calcularTotales();
				agregarServiciosPadre();

				this.setServicioFee(false);
				this.setListadoEmpresas(null);
				this.setCargoConfiguracionTipoServicio(false);
			}
		} catch (ErrorRegistroDataException e) {
			logger.error(e.getMessage(), e);
			this.mostrarMensajeError(e.getMessage());
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			this.mostrarMensajeError(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.mostrarMensajeError(e.getMessage());
		}

	}
	
	private void seleccionarOrigenDestino() {
		String origen = "";
		String destino = "";
		
		try {
			origen = StringUtils.trim(this.getDetalleServicio().getOrigen().getCodigoCadena());
			origen = StringUtils.substring(origen, StringUtils.indexOf(origen,"(")+1, StringUtils.indexOf(origen,")"));
			
			this.getDetalleServicio().setOrigen(this.soporteServicio.consultaDestinoIATA(origen));
			/*this.soporteServicio.consultarDestino(descripcion)
			
			for(Destino destinoBean : this.getListaOrigenesBusqueda()){
				if (StringUtils.equals(destinoBean.getCodigoIATA(), origen)){
					this.getDetalleServicio().setOrigen(destinoBean);
					break;
				}
			}*/
			
			destino = StringUtils.trim(this.getDetalleServicio().getDestino().getCodigoCadena());
			destino = StringUtils.substring(destino, StringUtils.indexOf(destino,"(")+1, StringUtils.indexOf(destino,")"));
			
			/*for(Destino destinoBean : this.getListaOrigenesBusqueda()){
				if (StringUtils.equals(destinoBean.getCodigoIATA(), destino)){
					this.getDetalleServicio().setDestino(destinoBean);
					break;
				}
			}*/
			this.getDetalleServicio().setDestino(this.soporteServicio.consultaDestinoIATA(destino));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void actualizarServicio(){
		try {
			if (validarServicioVenta()) {
				getDetalleServicio().getServicioProveedor().setEditoComision(
						this.isEditarComision());

				this.setListadoDetalleServicio(this.utilNegocioServicio
						.actualizarServicioVenta(this.getListadoDetalleServicio(),
								getDetalleServicio()));

				this.setDetalleServicio(null);

				calcularTotales();
				agregarServiciosPadre();

				this.setServicioFee(false);
				this.setListadoEmpresas(null);
				this.setEditaServicioAgregado(false);
				this.setCargoConfiguracionTipoServicio(false);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.mostrarMensajeError(e.getMessage());
		}
	}
	
	public void cancelarEdicionServicio(){
		this.setServicioFee(false);
		this.setListadoEmpresas(null);
		this.setEditaServicioAgregado(false);
		this.setCargoConfiguracionTipoServicio(false);
		this.setDetalleServicio(null);
	}

	private void agregarServiciosPadre() {
		SelectItem si = null;
		this.setListadoServiciosPadre(null);
		for (DetalleServicioAgencia detalle : this.getListadoDetalleServicio()) {
			if (detalle.getTipoServicio().isServicioPadre()) {
				si = new SelectItem();
				si.setValue(detalle.getCodigoEntero());

				String etiqueta = "" + detalle.getTipoServicio().getNombre();

				if (StringUtils.isNotBlank(detalle.getOrigen().getCodigoIATA())
						&& StringUtils.isNotBlank(detalle.getDestino()
								.getCodigoIATA())) {
					etiqueta = etiqueta + " "
							+ detalle.getOrigen().getCodigoIATA() + " --> "
							+ detalle.getDestino().getCodigoIATA();
				}
				else {
					if (StringUtils.length(detalle.getDescripcionServicio()) >= 15){
						etiqueta = etiqueta + detalle.getDescripcionServicio().substring(0, 15);
					}
					
				}

				si.setLabel(etiqueta);
				this.getListadoServiciosPadre().add(si);
				this.setAgregoServicioPadre(true);
			}
		}
	}

	private boolean validarRegistroServicioVenta() throws Exception {
		boolean resultado = true;
		String idFormulario = "idFormVentaServi";
		if (this.getServicioAgencia().getCliente() == null
				|| this.getServicioAgencia().getCliente().getCodigoEntero() == null
				|| this.getServicioAgencia().getCliente().getCodigoEntero()
						.intValue() == 0) {
			this.agregarMensaje(idFormulario + ":idFrCliente",
					"Seleccione el cliente del servicio", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}

		if (this.getServicioAgencia().getFormaPago().getCodigoEntero() == null
				|| this.getServicioAgencia().getFormaPago().getCodigoEntero()
						.intValue() == 0) {
			this.agregarMensaje(idFormulario + ":idSelForPago",
					"Seleccione el forma de Pago", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		} else {
			if (this.getServicioAgencia().getFormaPago().getCodigoEntero()
					.intValue() == 2) {
				if (this.getServicioAgencia().getTea() == null
						|| this.getServicioAgencia().getTea()
								.compareTo(BigDecimal.ZERO) == 0) {
					this.agregarMensaje(idFormulario + ":idTea",
							"Ingrese la tasa de interes", "",
							FacesMessage.SEVERITY_ERROR);
					resultado = false;
				}
				if (this.getServicioAgencia().getNroCuotas() == 0) {
					this.agregarMensaje(idFormulario + ":idNroCuotas",
							"Ingrese el nÃºmero de cuotas", "",
							FacesMessage.SEVERITY_ERROR);
					resultado = false;
				}
				if (this.getServicioAgencia().getFechaPrimerCuota() == null) {
					this.agregarMensaje(idFormulario + ":idFecPriVcto",
							"Ingrese la fecha de primer vencimiento", "",
							FacesMessage.SEVERITY_ERROR);
					resultado = false;
				}
			}
		}
		if (this.getServicioAgencia().getVendedor().getCodigoEntero() == null
				|| this.getServicioAgencia().getVendedor().getCodigoEntero()
						.intValue() == 0) {
			this.agregarMensaje(idFormulario + ":idSelVende",
					"Seleccione el Agente de Viajes", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getServicioAgencia().getFechaServicio() == null) {
			this.agregarMensaje(idFormulario + ":idSelFecSer",
					"Ingrese la Fecha de Servicio", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (resultado) {
			if (this.getListadoDetalleServicio().isEmpty()) {
				throw new ErrorRegistroDataException(
						"No se agregaron servicios a la venta");
			} else {

				validarServicios();

				validarFee();
			}
		}

		return resultado;
	}

	private void validarServicios() throws ErrorRegistroDataException,
			SQLException, Exception {

		for (DetalleServicioAgencia detalle : this.getListadoDetalleServicio()) {

			List<BaseVO> listaDependientes = this.negocioServicio
					.consultaServiciosDependientes(detalle.getTipoServicio()
							.getCodigoEntero());

			for (BaseVO baseVO : listaDependientes) {
				if (!estaEnListaServicios(baseVO) && 13 != baseVO.getCodigoEntero().intValue()) {
					throw new ErrorRegistroDataException("No se agrego "
							+ baseVO.getNombre());
				}
			}
		}
	}

	private boolean estaEnListaServicios(BaseVO baseVO) {
		boolean resultado = false;

		for (DetalleServicioAgencia detalle : this.getListadoDetalleServicio()) {
			for (DetalleServicioAgencia detalle2 : detalle.getServiciosHijos()) {
				if (detalle2.getTipoServicio().getCodigoEntero().intValue() == baseVO
						.getCodigoEntero().intValue()) {
					resultado = true;
					break;
				}
			}
			if (detalle.getTipoServicio().getCodigoEntero().intValue() == baseVO
					.getCodigoEntero().intValue()) {
				resultado = true;
				break;
			}
		}

		return resultado;
	}

	private void validarFee() throws ErrorRegistroDataException {
		int fees = 0;

		for (DetalleServicioAgencia detalle : this.getListadoDetalleServicio()) {
			if (detalle.getTipoServicio().isEsFee()) {
				fees++;
			}
		}

		if (0 == fees) {
			throw new ErrorRegistroDataException("Debe agregar El Fee de Venta");
		}
	}

	private boolean validarServicioVenta() throws ValidacionException {
		boolean resultado = true;
		String idFormulario = "idFormVentaServi";
		if (this.getDetalleServicio().getTipoServicio().getCodigoEntero() == null
				|| this.getDetalleServicio().getTipoServicio()
						.getCodigoEntero().intValue() == 0) {
			this.agregarMensaje(idFormulario + ":idSelTipoServicio",
					"Seleccione el tipo de servicio", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		} else {
			ConfiguracionTipoServicio configuracionTipoServicio = this
					.getDetalleServicio().getConfiguracionTipoServicio();

			if (configuracionTipoServicio.isMuestraDestino() && StringUtils.isBlank(this.getDetalleServicio().getOrigen().getCodigoCadena())){
				this.agregarMensaje(idFormulario + ":idTxtOrigen",
						"Seleccione el origen", "",
						FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
			if (configuracionTipoServicio.isMuestraDestino() && StringUtils.isBlank(this.getDetalleServicio().getOrigen().getCodigoCadena())){
				this.agregarMensaje(idFormulario + ":idTxtDestino",
						"Seleccione el destino", "",
						FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
			if (false && configuracionTipoServicio.isMuestraDescServicio()
					&& StringUtils.isBlank(this.getDetalleServicio()
							.getDescripcionServicio())) {
				this.agregarMensaje(idFormulario + ":idDescServicio",
						"Ingrese la descripcion del servicio", "",
						FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
			if (configuracionTipoServicio.isMuestraCantidad()
					&& this.getDetalleServicio().getCantidad() == 0) {
				this.agregarMensaje(idFormulario + ":idCantidad",
						"Ingrese la cantidad", "", FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
			if (configuracionTipoServicio.isMuestraPrecioBase()
					&& (this.getDetalleServicio().getPrecioUnitario() == null || this
							.getDetalleServicio().getPrecioUnitario()
							.doubleValue() == 0.0)) {
				this.agregarMensaje(idFormulario + ":idPrecUnitario",
						"Ingrese el precio base del servicio", "",
						FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
			if (configuracionTipoServicio.isMuestraFechaServicio()
					&& this.getDetalleServicio().getFechaIda() == null) {
				this.agregarMensaje(idFormulario + ":idFecServicio",
						"Ingrese la fecha del servicio", "",
						FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
			if (configuracionTipoServicio.isMuestraFechaServicio()
					&& (this.getDetalleServicio().getFechaIda() != null && UtilWeb
							.fecha1EsMayorIgualFecha2(this.getDetalleServicio()
									.getFechaIda(), new Date()))) {
				this.agregarMensaje(
						idFormulario + ":idFecServicio",
						"La fecha del servicio no puede ser menor que la fecha de actual",
						"", FacesMessage.SEVERITY_ERROR);
				resultado = false;
			} else if (configuracionTipoServicio.isMuestraFechaServicio()
					&& (this.getDetalleServicio().getFechaRegreso() != null && this
							.getDetalleServicio().getFechaIda()
							.after(this.getDetalleServicio().getFechaRegreso()))) {
				this.agregarMensaje(
						idFormulario + ":idFecServicio",
						"La fecha del servicio no puede ser mayor que la fecha de regreso",
						"", FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
			if (configuracionTipoServicio.isMuestraProveedor()
					&& (this.getDetalleServicio().getServicioProveedor()
							.getProveedor().getCodigoEntero() == null || this
							.getDetalleServicio().getServicioProveedor()
							.getProveedor().getCodigoEntero().intValue() == 0)) {
				this.agregarMensaje(idFormulario + ":idSelEmpServicio",
						"Seleccione el proveedor del servicio", "",
						FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
			if (resultado
					&& !this.getDetalleServicio().getTipoServicio()
							.isServicioPadre() && !this.isAgregoServicioPadre()) {
				this.setDetalleServicio(null);
				throw new ValidacionException(
						"No puede agregar este servicio hasta que no haya agregado un servicio padre o principal");
			}
		}

		return resultado;
	}

	private void calcularTotales() {
		BigDecimal montoTotal = BigDecimal.ZERO;
		BigDecimal montoComision = BigDecimal.ZERO;
		BigDecimal montoFee = BigDecimal.ZERO;
		BigDecimal montoIgv = BigDecimal.ZERO;
		try {
			Parametro param = this.parametroServicio.consultarParametro(UtilWeb
					.obtenerEnteroPropertieMaestro("codigoParametroIGV",
							"aplicacionDatos"));

			for (DetalleServicioAgencia ds : this.getListadoDetalleServicio()) {
				montoTotal = montoTotal.add(ds.getTotalServicio());
				montoComision = montoComision.add(ds.getMontoComision());
				if (ds.getTipoServicio().getCodigoEntero().toString()
						.equals(param.getValor())) {
					montoIgv = montoIgv.add(ds.getPrecioUnitario());
				}

				if (ds.getTipoServicio().getCodigoEntero() != null
						&& ds.getTipoServicio().isEsFee()) {
					montoFee = montoFee.add(ds.getTotalServicio());
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			montoComision = BigDecimal.ZERO;
			montoFee = BigDecimal.ZERO;
			montoIgv = BigDecimal.ZERO;
			montoTotal = BigDecimal.ZERO;
		}

		this.getServicioAgencia().setMontoTotalServicios(montoTotal);
		this.getServicioAgencia().setMontoTotalComision(montoComision);
		this.getServicioAgencia().setMontoTotalFee(montoFee);
		this.getServicioAgencia().setMontoTotalIGV(montoIgv);
	}

	public void ejecutarMetodo() {
		try {
			if (validarRegistroServicioVenta()) {
				if (this.isNuevaVenta()) {
					HttpSession session = obtenerSession(false);
					Usuario usuario = (Usuario) session
							.getAttribute("usuarioSession");
					getServicioAgencia().setUsuarioCreacion(
							usuario.getUsuario());
					getServicioAgencia().setIpCreacion(
							obtenerRequest().getRemoteAddr());

					for (DetalleServicioAgencia detalleServicio : getListadoDetalleServicio()) {
						detalleServicio
								.setUsuarioCreacion(usuario.getUsuario());
						detalleServicio.setUsuarioModificacion(usuario
								.getUsuario());
						detalleServicio.setIpCreacion(obtenerRequest()
								.getRemoteAddr());
						detalleServicio.setIpModificacion(obtenerRequest()
								.getRemoteAddr());
					}
					this.getServicioAgencia().setListaDetalleServicio(
							getListadoDetalleServicio());

					Integer idServicio = this.negocioServicio
							.registrarVentaServicio(getServicioAgencia());

					if (idServicio != null && idServicio.intValue() != 0) {
						this.getServicioAgencia().setCodigoEntero(idServicio);
						this.getServicioAgencia()
								.setCronogramaPago(
										this.negocioServicio
												.consultarCronogramaPago(getServicioAgencia()));
						this.setTransaccionExito(true);
					}

					for (DocumentoAdicional documento : getListaDocumentosAdicionales()) {
						documento.setIdServicio(idServicio);
						documento.setUsuarioCreacion(usuario.getUsuario());
						documento.setIpCreacion(obtenerRequest()
								.getRemoteAddr());
						documento.setUsuarioModificacion(usuario.getUsuario());
						documento.setIpModificacion(obtenerRequest()
								.getRemoteAddr());
					}
					if (!getListaDocumentosAdicionales().isEmpty()) {
						this.negocioServicio
								.grabarDocumentosAdicionales(getListaDocumentosAdicionales());
					}

					this.setListaDocumentosAdicionales(this.negocioServicio
							.listarDocumentosAdicionales(idServicio));

					this.mostrarMensajeExito("Servicio Venta registrado satisfactoriamente");
				} else if (this.isEditarVenta()) {
					HttpSession session = obtenerSession(false);
					Usuario usuario = (Usuario) session
							.getAttribute("usuarioSession");
					getServicioAgencia().setUsuarioCreacion(
							usuario.getUsuario());
					getServicioAgencia().setIpCreacion(
							obtenerRequest().getRemoteAddr());
					getServicioAgencia().setUsuarioModificacion(
							usuario.getUsuario());
					getServicioAgencia().setIpModificacion(
							obtenerRequest().getRemoteAddr());

					for (DetalleServicioAgencia detalle : getListadoDetalleServicio()) {
						detalle.setUsuarioCreacion(usuario.getUsuario());
						detalle.setIpCreacion(obtenerRequest().getRemoteAddr());
						detalle.setUsuarioModificacion(usuario.getUsuario());
						detalle.setIpModificacion(obtenerRequest()
								.getRemoteAddr());
						if (detalle.getServiciosHijos() != null) {
							for (DetalleServicioAgencia detalle2 : detalle
									.getServiciosHijos()) {
								detalle2.setUsuarioCreacion(usuario
										.getUsuario());
								detalle2.setIpCreacion(obtenerRequest()
										.getRemoteAddr());
								detalle2.setUsuarioModificacion(usuario
										.getUsuario());
								detalle2.setIpModificacion(obtenerRequest()
										.getRemoteAddr());
							}
						}
					}

					this.getServicioAgencia().setListaDetalleServicio(
							getListadoDetalleServicio());

					Integer idServicio = this.negocioServicio
							.actualizarVentaServicio(getServicioAgencia());

					if (idServicio != null && idServicio.intValue() != 0) {
						this.getServicioAgencia().setCodigoEntero(idServicio);
						this.getServicioAgencia()
								.setCronogramaPago(
										this.negocioServicio
												.consultarCronogramaPago(getServicioAgencia()));
						this.setTransaccionExito(true);
					}
					this.mostrarMensajeExito("Servicio Venta actualizado satisfactoriamente");
				}
			}
		} catch (ErrorRegistroDataException e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		} catch (SQLException e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		}
	}

	public void cerrarVenta() {
		try {

			HttpSession session = obtenerSession(false);
			Usuario usuario = (Usuario) session.getAttribute("usuarioSession");
			getServicioAgencia().setUsuarioCreacion(usuario.getUsuario());
			getServicioAgencia()
					.setIpCreacion(obtenerRequest().getRemoteAddr());
			getServicioAgencia().setUsuarioModificacion(usuario.getUsuario());
			getServicioAgencia().setIpModificacion(
					obtenerRequest().getRemoteAddr());

			this.negocioServicio.cerrarVenta(getServicioAgencia());

			this.setTransaccionExito(true);
			this.setShowModal(true);
			this.setMensajeModal("Servicio Venta se cerro satisfactoriamente");
			this.setTipoModal(TIPO_MODAL_EXITO);

		} catch (SQLException e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		}
	}

	public void anularVenta() {
		try {

			HttpSession session = obtenerSession(false);
			Usuario usuario = (Usuario) session.getAttribute("usuarioSession");
			getServicioAgencia().setUsuarioCreacion(usuario.getUsuario());
			getServicioAgencia()
					.setIpCreacion(obtenerRequest().getRemoteAddr());
			getServicioAgencia().setUsuarioModificacion(usuario.getUsuario());
			getServicioAgencia().setIpModificacion(
					obtenerRequest().getRemoteAddr());

			this.negocioServicio.anularVenta(getServicioAgencia());

			this.setTransaccionExito(true);
			this.mostrarMensajeExito("Servicio Venta se cerro satisfactoriamente");
		} catch (SQLException e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		}
	}

	public void preCerrarVenta() {
		this.setPregunta("¿Esta seguro de cerrar el servicio de venta?");
	}

	public void preAnularVenta() {
		this.setNombreTitulo("Anular Venta");
		this.setNombreCampoTexto("Comentario Anulación");
		this.setTipoEvento(EventoObsAnu.EVENTO_ANU);
	}

	public void preObservarVenta() {
		this.setNombreTitulo("Observar Venta");
		this.setNombreCampoTexto("Comentario Observación");
		this.setTipoEvento(EventoObsAnu.EVENTO_OBS);
	}

	public void preEvento2() {
		this.setIdModales("idModalventaservicio,idModalObsAnu");

		if (EventoObsAnu.EVENTO_OBS.equals(this.getTipoEvento())) {
			this.setPregunta("¿Esta seguro de observar la venta?");
		} else {
			this.setPregunta("¿Esta seguro de anular la venta?");
		}
	}

	public void registrarEvento() {
		try {
			this.getEventoObsAnu().setIdServicio(
					this.getServicioAgencia().getCodigoEntero());
			HttpSession session = obtenerSession(false);
			Usuario usuario = (Usuario) session.getAttribute("usuarioSession");
			this.getEventoObsAnu().setUsuarioCreacion(usuario.getUsuario());
			this.getEventoObsAnu().setIpCreacion(
					obtenerRequest().getRemoteAddr());
			this.getEventoObsAnu().setUsuarioModificacion(usuario.getUsuario());
			this.getEventoObsAnu().setIpModificacion(
					obtenerRequest().getRemoteAddr());

			if (EventoObsAnu.EVENTO_OBS.equals(this.getTipoEvento())) {
				this.negocioServicio.registrarEventoObservacion(this
						.getEventoObsAnu());

				this.mostrarMensajeExito("Observación registrada satisfactoriamente");
			} else {
				this.negocioServicio.registrarEventoAnulacion(this
						.getEventoObsAnu());
				this.mostrarMensajeExito("Servicio Venta anulada satisfactoriamente");
			}
		} catch (SQLException e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		}

	}

	public void calcularCuota() {
		BigDecimal valorCuota = BigDecimal.ZERO;
		try {
			valorCuota = this.negocioServicio.calcularValorCuota(this
					.getServicioAgencia());
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		this.getServicioAgencia().setValorCuota(valorCuota);
	}

	public void consultarTasaPredeterminada() {
		try {
			int idtasatea = 3;
			BigDecimal ttea = UtilParse
					.parseStringABigDecimal(parametroServicio
							.consultarParametro(idtasatea).getValor());
			this.getServicioAgencia().setTea(ttea);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void cambiarAerolinea(ValueChangeEvent e) {
		Object oe = e.getNewValue();
		try {
			if (oe != null) {
				String valor = oe.toString();

				List<Destino> listaDestino = this.soporteServicio
						.listarDestinos();

				for (Destino destino : listaDestino) {
					if (destino.getCodigoEntero()
							.equals(Integer.valueOf(valor))) {
						this.getServicioAgencia().getDestino()
								.setDescripcion(destino.getDescripcion());
						break;
					}
				}
			}
		} catch (SQLException ex) {
			logger.error(ex.getMessage(), ex);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	public void cargarDatosValores(ValueChangeEvent e) {
		Object oe = e.getNewValue();
		this.setCalculadorIGV(false);
		this.setEditaServicioAgregado(false);
		try {
			setListadoEmpresas(null);
			this.getDetalleServicio().getServicioProveedor().setProveedor(null);
			
			this.getDetalleServicio().setConfiguracionTipoServicio(null);
			this.setCargoConfiguracionTipoServicio(false);
			if (oe != null) {
				String valor = oe.toString();

				Parametro param = this.parametroServicio
						.consultarParametro(UtilWeb
								.obtenerEnteroPropertieMaestro(
										"codigoParametroIGV", "aplicacionDatos"));
				this.setServicioFee(valor.equals(param.getValor()));

				MaestroServicio maestroServicio = this.negocioServicio
						.consultarMaestroServicio(UtilWeb
								.convertirCadenaEntero(valor));
				
				this.getDetalleServicio().setTipoServicio(maestroServicio);
				this.getDetalleServicio().setConfiguracionTipoServicio(
						this.soporteServicio
								.consultarConfiguracionServicio(UtilWeb
										.convertirCadenaEntero(valor)));
				
				this.setCargoConfiguracionTipoServicio(StringUtils.equals(this.getDetalleServicio().getConfiguracionTipoServicio().getCodigoCadena(),"A"));

				List<BaseVO> listaServicios = this.negocioServicio
						.consultaServiciosDependientes(UtilWeb
								.convertirCadenaEntero(valor));

				this.setCalculadorIGV(false);
				for (BaseVO baseVO : listaServicios) {
					if (baseVO.getCodigoEntero().intValue() == UtilWeb
							.convertirCadenaEntero(param.getValor())) {
						this.setCalculadorIGV(true);
						break;
					}
				}

				this.setServicioFee(maestroServicio.isEsFee()
						|| maestroServicio.isEsImpuesto());

				if (!this.isServicioFee()) {
					cargarEmpresas(UtilWeb
							.convertirCadenaEntero(valor));
				}
				
				this.consultarDestinos();
			}
			
		} catch (SQLException ex) {
			logger.error(ex.getMessage(), ex);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}
	
	private void cargarEmpresas(Integer valor) throws SQLException, Exception{
		listaProveedores = this.negocioServicio
				.proveedoresXServicio(valor);
		setListadoEmpresas(null);

		SelectItem si = null;
		for (ServicioProveedor servicioProveedor : listaProveedores) {
			si = new SelectItem();
			si.setValue(servicioProveedor.getCodigoEntero());
			si.setLabel(servicioProveedor.getNombreProveedor());
			getListadoEmpresas().add(si);
		}
	}
	
	public void editarServicioAgregado(DetalleServicioAgencia detalleServicio){
		try {
			
			Destino destino = detalleServicio.getDestino();
			destino.setCodigoCadena(destino.getDescripcion()+"("+destino.getCodigoIATA()+")");
			Destino origen = detalleServicio.getOrigen();
			origen.setCodigoCadena(origen.getDescripcion()+"("+origen.getCodigoIATA()+")");
			
			detalleServicio.setOrigen(origen);
			detalleServicio.setDestino(destino);
			this.setDetalleServicio(detalleServicio);
			
			this.cargarEmpresas(detalleServicio.getTipoServicio().getCodigoEntero());
			
			MaestroServicio maestroServicio = this.negocioServicio
					.consultarMaestroServicio(detalleServicio.getTipoServicio().getCodigoEntero());

			this.getDetalleServicio().setTipoServicio(maestroServicio);
			this.getDetalleServicio().setConfiguracionTipoServicio(
					this.soporteServicio
							.consultarConfiguracionServicio(detalleServicio.getTipoServicio().getCodigoEntero()));
			
			this.setEditaServicioAgregado(true);
			this.setCargoConfiguracionTipoServicio(this.getDetalleServicio().getConfiguracionTipoServicio() != null);
		} catch (SQLException e) {
			this.setEditaServicioAgregado(false);
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			this.setEditaServicioAgregado(false);
			logger.error(e.getMessage(), e);
		}
	}

	public void seleccionarEmpresa(ValueChangeEvent e) {
		Object oe = e.getNewValue();
		try {
			if (oe != null) {
				String valor = oe.toString();

				this.getDetalleServicio().getServicioProveedor().getProveedor()
						.setCodigoEntero(UtilWeb.convertirCadenaEntero(valor));

				this.getDetalleServicio()
						.getServicioProveedor()
						.setPorcentajeComision(
								this.negocioServicio
										.calculaPorcentajeComision(this
												.getDetalleServicio()));

			}
		} catch (Exception ex) {
			this.getDetalleServicio().getServicioProveedor()
					.setPorcentajeComision(BigDecimal.ZERO);
			logger.error(ex.getMessage(), ex);
		}
	}

	public void seleccionarAerolinea(ValueChangeEvent e) {
		Object oe = e.getNewValue();
		try {
			if (oe != null) {
				String valor = oe.toString();

				this.getDetalleServicio().getAerolinea()
						.setCodigoEntero(UtilWeb.convertirCadenaEntero(valor));
				
				seleccionarOrigenDestino();

				this.getDetalleServicio()
						.getServicioProveedor()
						.setPorcentajeComision(
								this.negocioServicio
										.calculaPorcentajeComision(this
												.getDetalleServicio()));

			}
		} catch (Exception ex) {
			this.getDetalleServicio().getServicioProveedor()
					.setPorcentajeComision(BigDecimal.ZERO);
			logger.error(ex.getMessage(), ex);
		}
	}
	
	public void calcularComision(){
		try {
			seleccionarOrigenDestino();

			this.getDetalleServicio()
					.getServicioProveedor()
					.setPorcentajeComision(
							this.negocioServicio
									.calculaPorcentajeComision(this
											.getDetalleServicio()));

		} catch (Exception ex) {
			this.getDetalleServicio().getServicioProveedor()
					.setPorcentajeComision(BigDecimal.ZERO);
			logger.error(ex.getMessage(), ex);
		}
	}

	public void eliminarServicio(DetalleServicioAgencia detalleServicio) {
		if (listadoDetalleServicio != null) {
			for (int i = 0; i < listadoDetalleServicio.size(); i++) {
				DetalleServicioAgencia detalle = listadoDetalleServicio.get(i);
				if (detalleServicio.getCodigoEntero().equals(
						detalle.getCodigoEntero())) {
					this.listadoDetalleServicio.remove(i);
				}
			}
		}

		calcularTotales();
	}

	public void buscarDestino() {
		try {
			this.setListaDestinosBusqueda(this.soporteServicio
					.consultarDestino(this.getDestinoBusqueda()
							.getDescripcion()));
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void buscarOrigen() {
		try {
			this.setListaOrigenesBusqueda(this.soporteServicio
					.consultarOrigen(this.getOrigenBusqueda().getDescripcion()));
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void seleccionarOrigen() {
		try {
			this.getDetalleServicio().setOrigen(obtenerOrigenListado());

		} catch (Exception e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		}
	}

	private Destino obtenerOrigenListado() {
		try {
			for (Destino destino : this.getListaOrigenesBusqueda()) {
				if (destino.getCodigoEntero().intValue() == destino
						.getCodigoSeleccionado().intValue()) {
					return destino;
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public void seleccionarHotel(ValueChangeEvent e) {
		Object oe = e.getNewValue();
		try {
			if (oe != null) {
				String valor = oe.toString();

				this.getDetalleServicio().getHotel()
						.setCodigoEntero(UtilWeb.convertirCadenaEntero(valor));

				this.getDetalleServicio()
						.getServicioProveedor()
						.setPorcentajeComision(
								this.negocioServicio
										.calculaPorcentajeComision(this
												.getDetalleServicio()));

			}
		} catch (Exception ex) {
			this.getDetalleServicio().getServicioProveedor()
					.setPorcentajeComision(BigDecimal.ZERO);
			logger.error(ex.getMessage(), ex);
		}
	}

	public void seleccionarOperadora() {

	}

	public void registrarNuevoPago() {
		this.setPagoServicio(null);
	}

	public void registrarPago() {
		try {
			if (validarRegistroPago()) {
				this.getPagoServicio()
						.getServicio()
						.setCodigoEntero(
								this.getServicioAgencia().getCodigoEntero());

				HttpSession session = obtenerSession(false);
				Usuario usuario = (Usuario) session
						.getAttribute("usuarioSession");
				this.getPagoServicio().setUsuarioCreacion(usuario.getUsuario());
				this.getPagoServicio().setIpCreacion(
						obtenerRequest().getRemoteAddr());
				this.getPagoServicio().setUsuarioModificacion(
						usuario.getUsuario());
				this.getPagoServicio().setIpModificacion(
						obtenerRequest().getRemoteAddr());

				this.negocioServicio.registrarPago(getPagoServicio());
				this.mostrarMensajeExito("Pago Registrado Satisfactoriamente");
			}

		} catch (SQLException e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		}
	}

	private boolean validarRegistroPago() {
		boolean resultado = true;
		String idFormulario = "idFormRegisPago";
		if (this.getPagoServicio().getMontoPago() == null
				|| BigDecimal.ZERO
						.equals(this.getPagoServicio().getMontoPago())) {
			this.agregarMensaje(idFormulario + ":idMontoPago",
					"Ingrese el monto a pagar", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getPagoServicio().getFechaPago() == null) {
			this.agregarMensaje(idFormulario + ":idSelFecSer",
					"Ingrese la fecha de pago", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (StringUtils.length(this.getPagoServicio().getComentario()) > 300) {
			this.agregarMensaje(idFormulario + ":idTxtComentario",
					"El comentario no debe ser mayor a 300 caracteres", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}

		return resultado;
	}

	public void listener(FileUploadEvent event) throws Exception {
		UploadedFile item = event.getUploadedFile();

		String nombre = item.getName();
		StringTokenizer stk = new StringTokenizer(nombre, ".");
		String archivoNombre = stk.nextToken();
		if (stk.hasMoreTokens()) {
			archivoNombre = stk.nextToken();
		}
		byte[] arregloDatos = IOUtils.toByteArray(item.getInputStream());
		this.getPagoServicio().setNombreArchivo(nombre);
		this.getPagoServicio().setExtensionArchivo(archivoNombre);
		this.getPagoServicio().setSustentoPagoByte(arregloDatos);
		this.getPagoServicio().setTipoContenido(item.getContentType());
	}

	public void preGuardarRelacion() {
		this.setPregunta("Â¿Esta seguro de guardar la relaciÃ³n de comprobantes?");
	}

	public void agregarComprobanteAdicional() {
		this.getListaComprobantesAdicionales().add(new Comprobante());
	}

	/**
	 * @return the servicioAgencia
	 */
	public ServicioAgencia getServicioAgencia() {
		if (servicioAgencia == null) {
			servicioAgencia = new ServicioAgencia();
		}
		return servicioAgencia;
	}

	/**
	 * @param servicioAgencia
	 *            the servicioAgencia to set
	 */
	public void setServicioAgencia(ServicioAgencia servicioAgencia) {
		this.servicioAgencia = servicioAgencia;
	}

	/**
	 * @return the listadoServicioAgencia
	 */
	public List<ServicioAgencia> getListadoServicioAgencia() {
		try {
			if (!busquedaRealizada) {

				HttpSession session = obtenerSession(false);
				Usuario usuario = (Usuario) session
						.getAttribute(USUARIO_SESSION);
				if (Integer.valueOf(2).equals(
						usuario.getRol().getCodigoEntero())
						|| Integer.valueOf(4).equals(
								usuario.getRol().getCodigoEntero())) {
					getServicioAgenciaBusqueda().getVendedor().setCodigoEntero(
							usuario.getCodigoEntero());
				}

				listadoServicioAgencia = this.negocioServicio
						.listarVentaServicio(getServicioAgenciaBusqueda());
			}

			this.setShowModal(false);
		} catch (SQLException ex) {
			logger.error(ex.getMessage(), ex);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return listadoServicioAgencia;
	}

	public void verArchivo(Integer codigoPago) {
		for (PagoServicio pago : this.listaPagosServicios) {
			if (pago.getCodigoEntero().intValue() == codigoPago.intValue()) {
				this.setPagoServicio2(pago);
				break;
			}
		}
	}

	public void exportarArchivo() {
		try {
			HttpServletResponse response = obtenerResponse();
			response.setContentType(pagoServicio2.getTipoContenido());
			response.setHeader("Content-disposition", "attachment;filename="
					+ this.getPagoServicio2().getNombreArchivo());
			response.setHeader("Content-Transfer-Encoding", "binary");

			FacesContext facesContext = obtenerContexto();

			ServletOutputStream respuesta = response.getOutputStream();
			if (this.getPagoServicio2() != null
					&& this.getPagoServicio2().getSustentoPagoByte() != null) {
				respuesta.write(this.getPagoServicio2().getSustentoPagoByte());
			}

			respuesta.close();
			respuesta.flush();

			facesContext.responseComplete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void preRegistrarComponente() {
		this.setPregunta("¿Esta seguro de registrar los comprobantes?");
	}

	public void registrarComprobante() {
		try {
			HttpSession session = obtenerSession(false);
			Usuario usuario = (Usuario) session.getAttribute("usuarioSession");
			getServicioAgencia().setUsuarioCreacion(usuario.getUsuario());
			getServicioAgencia()
					.setIpCreacion(obtenerRequest().getRemoteAddr());
			getServicioAgencia().setUsuarioModificacion(usuario.getUsuario());
			getServicioAgencia().setIpModificacion(
					obtenerRequest().getRemoteAddr());

			this.getServicioAgencia().setListaDetalleServicioAgrupado(
					getListadoDetalleServicioAgrupado());

			if (this.negocioServicio.registrarComprobantes(this
					.getServicioAgencia())) {
				this.getServicioAgencia().setListaDetalleServicio(
						this.negocioServicio.consultarDetalleComprobantes(this
								.getServicioAgencia().getCodigoEntero()));
				this.setListadoDetalleServicioAgrupado(this.utilNegocioServicio
						.agruparServicios(this.getServicioAgencia()
								.getListaDetalleServicio()));
				this.setGuardoComprobantes(true);
				this.getServicioAgencia().setGuardoComprobante(true);
				this.setColumnasComprobantes(10);
			}
			this.mostrarMensajeExito("Comprobante Registrado Satisfactoriamente");
		} catch (ValidacionException e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		} catch (SQLException e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		}
	}

	public void consultarPagosComprobantes(DetalleServicioAgencia detalle) {
		MaestroServicio maestro = detalle.getTipoServicio();
		this.getTipoServicio().setNombre(maestro.getDescripcion());
		this.getComprobante().getTipoComprobante()
				.setNombre(detalle.getTipoComprobante().getNombre());
		this.getComprobante().setNumeroComprobante(detalle.getNroComprobante());
	}

	public void buscarProveedor() {
		try {
			this.setListadoProveedores(this.negocioServicio
					.buscarProveedor(getProveedorBusqueda()));

			this.setConsultoProveedor(true);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
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

	public void buscarComprobante() {
		try {
			this.setListaComprobantes(this.negocioServicio
					.listarObligacionXPagar(getComprobanteBusqueda()));

		} catch (SQLException e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		}
	}

	public void enviaDetalle(DetalleServicioAgencia detalle) {
		this.setListaComprobantes(null);
		this.setDetalleServicio2(detalle);
	}

	public void seleccionarComprobante() {
		Comprobante comprobante1 = null;
		for (Comprobante comprobante : this.listaComprobantes) {
			if (comprobante.getCodigoEntero().equals(
					comprobante.getCodigoSeleccionado())) {
				comprobante1 = comprobante;
				break;
			}
		}

		for (DetalleServicioAgencia deta : this
				.getListadoDetalleServicioAgrupado()) {
			for (DetalleServicioAgencia detaHijo : deta.getServiciosHijos()) {
				if (detaHijo.equals(this.getDetalleServicio2())) {
					detaHijo.setComprobanteAsociado(comprobante1);
					break;
				}
			}
		}
	}

	public void guardarRelacionComprobanteObligacion() {
		try {
			this.setGuardoRelacionComprobantes(false);

			HttpSession session = obtenerSession(false);
			Usuario usuario = (Usuario) session.getAttribute("usuarioSession");

			this.getServicioAgencia().setUsuarioCreacion(usuario.getUsuario());
			this.getServicioAgencia().setIpCreacion(
					obtenerRequest().getRemoteAddr());
			this.getServicioAgencia().setUsuarioModificacion(
					usuario.getUsuario());
			this.getServicioAgencia().setIpModificacion(
					obtenerRequest().getRemoteAddr());

			this.getServicioAgencia().setListaDetalleServicioAgrupado(
					getListadoDetalleServicioAgrupado());

			for (DetalleServicioAgencia detalle : this.getServicioAgencia()
					.getListaDetalleServicioAgrupado()) {
				detalle.setUsuarioCreacion(usuario.getUsuario());
				detalle.setIpCreacion(obtenerRequest().getRemoteAddr());
				detalle.setUsuarioModificacion(usuario.getUsuario());
				detalle.setIpModificacion(obtenerRequest().getRemoteAddr());
				for (DetalleServicioAgencia detalleHijo : detalle
						.getServiciosHijos()) {
					detalleHijo.setUsuarioCreacion(usuario.getUsuario());
					detalleHijo.setIpCreacion(obtenerRequest().getRemoteAddr());
					detalleHijo.setUsuarioModificacion(usuario.getUsuario());
					detalleHijo.setIpModificacion(obtenerRequest()
							.getRemoteAddr());
				}
			}
			this.negocioServicio.registrarComprobanteObligacion(this
					.getServicioAgencia());
			this.setGuardoRelacionComprobantes(true);
			this.getServicioAgencia().setGuardoRelacionComprobantes(true);
			this.getServicioAgencia().setListaDetalleServicio(
					this.negocioServicio
							.consultarDetServComprobanteObligacion(this
									.getServicioAgencia().getCodigoEntero()));
			this.setListadoDetalleServicioAgrupado(this.utilNegocioServicio
					.agruparServicios(this.getServicioAgencia()
							.getListaDetalleServicio()));
			this.mostrarMensajeExito("Se guardo la relacion entre comprobantes satisfactoriamente");
		} catch (SQLException e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		}
	}

	public void agregarDocumentoAdicional() {
		this.getListaDocumentosAdicionales().add(new DocumentoAdicional());
	}

	public void listenerAdicional(FileUploadEvent event) {
		UploadedFile item = event.getUploadedFile();

		String nombre = item.getName();
		StringTokenizer stk = new StringTokenizer(nombre, ".");
		String archivoNombre = stk.nextToken();
		if (stk.hasMoreTokens()) {
			archivoNombre = stk.nextToken();
		}
		DocumentoAdicional documento = new DocumentoAdicional();

		documento.getArchivo().setNombreArchivo(nombre);
		documento.getArchivo().setExtensionArchivo(archivoNombre);
		documento.getArchivo().setDatos(item.getData());
		documento.getArchivo().setTipoContenido(item.getContentType());
		documento.getArchivo().setContent(item.getContentType());
		documento.setEditarDocumento(true);

		this.getListaDocumentosAdicionales().add(documento);
	}

	public void limpiarArchivos() {
		for (int i = 0; i < this.getListaDocumentosAdicionales().size(); i++) {
			DocumentoAdicional documento = this.getListaDocumentosAdicionales()
					.get(i);
			if (documento.isEditarDocumento()) {
				this.listaDocumentosAdicionales.remove(i);
			}
		}
	}

	public void grabarDocumentos() {
		try {
			HttpSession session = obtenerSession(false);
			Usuario usuario = (Usuario) session.getAttribute("usuarioSession");

			for (DocumentoAdicional documento : getListaDocumentosAdicionales()) {
				documento.setIdServicio(getServicioAgencia().getCodigoEntero());
				documento.setUsuarioCreacion(usuario.getUsuario());
				documento.setIpCreacion(obtenerRequest().getRemoteAddr());
				documento.setUsuarioModificacion(usuario.getUsuario());
				documento.setIpModificacion(obtenerRequest().getRemoteAddr());
			}

			this.negocioServicio
					.grabarDocumentosAdicionales(getListaDocumentosAdicionales());

			this.setListaDocumentosAdicionales(this.negocioServicio
					.listarDocumentosAdicionales(getServicioAgencia()
							.getCodigoEntero()));
			this.mostrarMensajeExito("Se guardaron los documentos satisfactoriamente");
		} catch (ErrorRegistroDataException e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		} catch (SQLException e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		}

	}

	public void seleccionarDocumentoAdicional(Integer idDoc) {

		for (DocumentoAdicional documento : this
				.getListaDocumentosAdicionales()) {
			if (documento.getCodigoEntero().equals(idDoc)) {
				this.setDocumentoAdicional(documento);
				break;
			}
		}
	}

	public void exportarArchivoDocumento() {
		try {
			HttpServletResponse response = obtenerResponse();
			response.setContentType(getDocumentoAdicional().getArchivo()
					.getContent());
			response.setHeader("Content-disposition", "attachment;filename="
					+ getDocumentoAdicional().getArchivo().getNombreArchivo());
			response.setHeader("Content-Transfer-Encoding", "binary");

			FacesContext facesContext = obtenerContexto();

			ServletOutputStream respuesta = response.getOutputStream();
			if (getDocumentoAdicional().getArchivo().getDatos() != null) {
				respuesta
						.write(getDocumentoAdicional().getArchivo().getDatos());
			}

			respuesta.close();
			respuesta.flush();

			facesContext.responseComplete();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void grabarComprobantesAdicionales() {
		try {
			if (validarComprobantesAdicionales()) {
				HttpSession session = obtenerSession(false);
				Usuario usuario = (Usuario) session
						.getAttribute("usuarioSession");

				for (Comprobante comprobante : getListaComprobantesAdicionales()) {
					comprobante.setTitular(getServicioAgencia().getCliente());
					comprobante.setIdServicio(getServicioAgencia()
							.getCodigoEntero());
					comprobante.setUsuarioCreacion(usuario.getUsuario());
					comprobante.setIpCreacion(obtenerRequest().getRemoteAddr());
					comprobante.setUsuarioModificacion(usuario.getUsuario());
					comprobante.setIpModificacion(obtenerRequest()
							.getRemoteAddr());
				}
				this.negocioServicio
						.registrarComprobantesAdicionales(getListaComprobantesAdicionales());

				this.setShowModal(true);
				this.setMensajeModal("Se guardaron los comprobantes adicionales satisfactoriamente");
				this.setTipoModal(TIPO_MODAL_EXITO);
			}

		} catch (ErrorRegistroDataException e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		} catch (SQLException e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			this.mostrarMensajeError(e.getMessage());
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * ========================================================================
	 * ===========================
	 * 
	 * @throws ValidacionException
	 */

	private boolean validarComprobantesAdicionales() throws ValidacionException {

		for (Comprobante comprobante : listaComprobantesAdicionales) {
			if (StringUtils.isBlank(comprobante.getNumeroComprobante())) {
				throw new ValidacionException(
						"Numero de comprobante no ingresado");
			}
			if (comprobante.getTipoComprobante().getCodigoEntero() == null
					|| comprobante.getTipoComprobante().getCodigoEntero()
							.intValue() == 0) {
				throw new ValidacionException(
						"Tipo de comprobante no seleccionado");
			}
			if (comprobante.getFechaComprobante() == null) {
				throw new ValidacionException(
						"Numero de comprobante no ingresado");
			}
			if (comprobante.getTotalComprobante() == null) {
				throw new ValidacionException(
						"Total de comprobante no ingresado");
			}
			if (StringUtils.isBlank(comprobante.getDetalleTextoComprobante())) {
				throw new ValidacionException(
						"Detalle de comprobante no ingresado");
			}
		}

		return true;
	}

	/**
	 * @param listadoServicioAgencia
	 *            the listadoServicioAgencia to set
	 */
	public void setListadoServicioAgencia(
			List<ServicioAgencia> listadoServicioAgencia) {
		this.listadoServicioAgencia = listadoServicioAgencia;
	}

	/**
	 * @return the nuevaVenta
	 */
	public boolean isNuevaVenta() {
		return nuevaVenta;
	}

	/**
	 * @param nuevaVenta
	 *            the nuevaVenta to set
	 */
	public void setNuevaVenta(boolean nuevaVenta) {
		this.nuevaVenta = nuevaVenta;
	}

	/**
	 * @return the editarVenta
	 */
	public boolean isEditarVenta() {
		return editarVenta;
	}

	/**
	 * @param editarVenta
	 *            the editarVenta to set
	 */
	public void setEditarVenta(boolean editarVenta) {
		this.editarVenta = editarVenta;
	}

	/**
	 * @return the detalleServicio
	 */
	public DetalleServicioAgencia getDetalleServicio() {
		if (detalleServicio == null) {
			detalleServicio = new DetalleServicioAgencia();
		}
		return detalleServicio;
	}

	/**
	 * @param detalleServicio
	 *            the detalleServicio to set
	 */
	public void setDetalleServicio(DetalleServicioAgencia detalleServicio) {
		this.detalleServicio = detalleServicio;
	}

	/**
	 * @return the listadoDetalleServicio
	 */
	public List<DetalleServicioAgencia> getListadoDetalleServicio() {
		if (listadoDetalleServicio == null) {
			listadoDetalleServicio = new ArrayList<DetalleServicioAgencia>();
		}
		return listadoDetalleServicio;
	}

	/**
	 * @param listadoDetalleServicio
	 *            the listadoDetalleServicio to set
	 */
	public void setListadoDetalleServicio(
			List<DetalleServicioAgencia> listadoDetalleServicio) {
		this.listadoDetalleServicio = listadoDetalleServicio;
	}

	/**
	 * @return the clienteBusqueda
	 */
	public Cliente getClienteBusqueda() {
		if (clienteBusqueda == null) {
			clienteBusqueda = new Cliente();
		}
		return clienteBusqueda;
	}

	/**
	 * @param clienteBusqueda
	 *            the clienteBusqueda to set
	 */
	public void setClienteBusqueda(Cliente clienteBusqueda) {
		this.clienteBusqueda = clienteBusqueda;
	}

	/**
	 * @return the listadoClientes
	 */
	public List<Cliente> getListadoClientes() {
		return listadoClientes;
	}

	/**
	 * @param listadoClientes
	 *            the listadoClientes to set
	 */
	public void setListadoClientes(List<Cliente> listadoClientes) {
		this.listadoClientes = listadoClientes;
	}

	/**
	 * @return the servicioAgenciaBusqueda
	 */
	public ServicioAgenciaBusqueda getServicioAgenciaBusqueda() {
		if (servicioAgenciaBusqueda == null) {
			servicioAgenciaBusqueda = new ServicioAgenciaBusqueda();

			Calendar cal = Calendar.getInstance();
			servicioAgenciaBusqueda.setFechaHasta(cal.getTime());
			cal.add(Calendar.DATE, -7);
			servicioAgenciaBusqueda.setFechaDesde(cal.getTime());
		}

		return servicioAgenciaBusqueda;
	}

	/**
	 * @param servicioAgenciaBusqueda
	 *            the servicioAgenciaBusqueda to set
	 */
	public void setServicioAgenciaBusqueda(
			ServicioAgenciaBusqueda servicioAgenciaBusqueda) {
		this.servicioAgenciaBusqueda = servicioAgenciaBusqueda;
	}

	/**
	 * @return the listadoEmpresas
	 */
	public List<SelectItem> getListadoEmpresas() {
		if (listadoEmpresas == null) {
			listadoEmpresas = new ArrayList<SelectItem>();
		}
		return listadoEmpresas;
	}

	/**
	 * @param listadoEmpresas
	 *            the listadoEmpresas to set
	 */
	public void setListadoEmpresas(List<SelectItem> listadoEmpresas) {
		this.listadoEmpresas = listadoEmpresas;
	}

	/**
	 * @return the listaProveedores
	 */
	public List<ServicioProveedor> getListaProveedores() {
		return listaProveedores;
	}

	/**
	 * @param listaProveedores
	 *            the listaProveedores to set
	 */
	public void setListaProveedores(List<ServicioProveedor> listaProveedores) {
		this.listaProveedores = listaProveedores;
	}

	/**
	 * @return the servicioFee
	 */
	public boolean isServicioFee() {
		return servicioFee;
	}

	/**
	 * @param servicioFee
	 *            the servicioFee to set
	 */
	public void setServicioFee(boolean servicioFee) {
		this.servicioFee = servicioFee;
	}

	/**
	 * @return the busquedaRealizada
	 */
	public boolean isBusquedaRealizada() {
		return busquedaRealizada;
	}

	/**
	 * @param busquedaRealizada
	 *            the busquedaRealizada to set
	 */
	public void setBusquedaRealizada(boolean busquedaRealizada) {
		this.busquedaRealizada = busquedaRealizada;
	}

	/**
	 * @return the destinoBusqueda
	 */
	public Destino getDestinoBusqueda() {
		if (destinoBusqueda == null) {
			destinoBusqueda = new Destino();
		}
		return destinoBusqueda;
	}

	/**
	 * @param destinoBusqueda
	 *            the destinoBusqueda to set
	 */
	public void setDestinoBusqueda(Destino destinoBusqueda) {
		this.destinoBusqueda = destinoBusqueda;
	}

	/**
	 * @return the listaDestinosBusqueda
	 */
	public List<Destino> getListaDestinosBusqueda() {
		return listaDestinosBusqueda;
	}

	/**
	 * @param listaDestinosBusqueda
	 *            the listaDestinosBusqueda to set
	 */
	public void setListaDestinosBusqueda(List<Destino> listaDestinosBusqueda) {
		this.listaDestinosBusqueda = listaDestinosBusqueda;
	}

	/**
	 * @return the editarComision
	 */
	public boolean isEditarComision() {
		return editarComision;
	}

	/**
	 * @param editarComision
	 *            the editarComision to set
	 */
	public void setEditarComision(boolean editarComision) {
		this.editarComision = editarComision;
	}

	/**
	 * @return the origenBusqueda
	 */
	public Destino getOrigenBusqueda() {
		if (origenBusqueda == null) {
			origenBusqueda = new Destino();
		}
		return origenBusqueda;
	}

	/**
	 * @param origenBusqueda
	 *            the origenBusqueda to set
	 */
	public void setOrigenBusqueda(Destino origenBusqueda) {
		this.origenBusqueda = origenBusqueda;
	}

	/**
	 * @return the listaOrigenesBusqueda
	 */
	public List<Destino> getListaOrigenesBusqueda() {
		return listaOrigenesBusqueda;
	}

	/**
	 * @param listaOrigenesBusqueda
	 *            the listaOrigenesBusqueda to set
	 */
	public void setListaOrigenesBusqueda(List<Destino> listaOrigenesBusqueda) {
		this.listaOrigenesBusqueda = listaOrigenesBusqueda;
	}

	/**
	 * @return the pagoServicio
	 */
	public PagoServicio getPagoServicio() {
		if (pagoServicio == null) {
			pagoServicio = new PagoServicio();
		}
		return pagoServicio;
	}

	/**
	 * @param pagoServicio
	 *            the pagoServicio to set
	 */
	public void setPagoServicio(PagoServicio pagoServicio) {
		this.pagoServicio = pagoServicio;
	}

	/**
	 * @return the listaPagosServicios
	 */
	public List<PagoServicio> getListaPagosServicios() {
		try {
			listaPagosServicios = this.negocioServicio.listarPagosServicio(this
					.getServicioAgencia().getCodigoEntero());
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return listaPagosServicios;
	}

	/**
	 * @param listaPagosServicios
	 *            the listaPagosServicios to set
	 */
	public void setListaPagosServicios(List<PagoServicio> listaPagosServicios) {
		this.listaPagosServicios = listaPagosServicios;
	}

	/**
	 * @return the saldoServicio
	 */
	public BigDecimal getSaldoServicio() {

		try {
			saldoServicio = this.negocioServicio.consultarSaldoServicio(this
					.getServicioAgencia().getCodigoEntero());
		} catch (SQLException e) {
			saldoServicio = BigDecimal.ZERO;
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			saldoServicio = BigDecimal.ZERO;
			logger.error(e.getMessage(), e);
		}

		return saldoServicio;
	}

	/**
	 * @param saldoServicio
	 *            the saldoServicio to set
	 */
	public void setSaldoServicio(BigDecimal saldoServicio) {
		this.saldoServicio = saldoServicio;
	}

	/**
	 * @return the vendedor
	 */
	public boolean isVendedor() {
		return vendedor;
	}

	/**
	 * @param vendedor
	 *            the vendedor to set
	 */
	public void setVendedor(boolean vendedor) {
		this.vendedor = vendedor;
	}

	/**
	 * @return the pregunta
	 */
	public String getPregunta() {
		return pregunta;
	}

	/**
	 * @param pregunta
	 *            the pregunta to set
	 */
	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}

	/**
	 * @return the nombreCampoTexto
	 */
	public String getNombreCampoTexto() {
		return nombreCampoTexto;
	}

	/**
	 * @param nombreCampoTexto
	 *            the nombreCampoTexto to set
	 */
	public void setNombreCampoTexto(String nombreCampoTexto) {
		this.nombreCampoTexto = nombreCampoTexto;
	}

	/**
	 * @return the nombreTitulo
	 */
	public String getNombreTitulo() {
		return nombreTitulo;
	}

	/**
	 * @param nombreTitulo
	 *            the nombreTitulo to set
	 */
	public void setNombreTitulo(String nombreTitulo) {
		this.nombreTitulo = nombreTitulo;
	}

	/**
	 * @return the eventoObsAnu
	 */
	public EventoObsAnu getEventoObsAnu() {
		if (eventoObsAnu == null) {
			eventoObsAnu = new EventoObsAnu();
		}
		return eventoObsAnu;
	}

	/**
	 * @param eventoObsAnu
	 *            the eventoObsAnu to set
	 */
	public void setEventoObsAnu(EventoObsAnu eventoObsAnu) {
		this.eventoObsAnu = eventoObsAnu;
	}

	/**
	 * @return the tipoEvento
	 */
	public Integer getTipoEvento() {
		return tipoEvento;
	}

	/**
	 * @param tipoEvento
	 *            the tipoEvento to set
	 */
	public void setTipoEvento(Integer tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	/**
	 * @return the idModales
	 */
	public String getIdModales() {
		return idModales;
	}

	/**
	 * @param idModales
	 *            the idModales to set
	 */
	public void setIdModales(String idModales) {
		this.idModales = idModales;
	}

	/**
	 * @return the calculadorIGV
	 */
	public boolean isCalculadorIGV() {
		return calculadorIGV;
	}

	/**
	 * @param calculadorIGV
	 *            the calculadorIGV to set
	 */
	public void setCalculadorIGV(boolean calculadorIGV) {
		this.calculadorIGV = calculadorIGV;
	}

	/**
	 * @return the pagoServicio2
	 */
	public PagoServicio getPagoServicio2() {
		if (pagoServicio2 == null) {
			pagoServicio2 = new PagoServicio();
		}
		return pagoServicio2;
	}

	/**
	 * @param pagoServicio2
	 *            the pagoServicio2 to set
	 */
	public void setPagoServicio2(PagoServicio pagoServicio2) {
		this.pagoServicio2 = pagoServicio2;
	}

	/**
	 * @return the guardoComprobantes
	 */
	public boolean isGuardoComprobantes() {
		return guardoComprobantes;
	}

	/**
	 * @param guardoComprobantes
	 *            the guardoComprobantes to set
	 */
	public void setGuardoComprobantes(boolean guardoComprobantes) {
		this.guardoComprobantes = guardoComprobantes;
	}

	/**
	 * @return the comprobante
	 */
	public Comprobante getComprobante() {
		if (comprobante == null) {
			comprobante = new Comprobante();
		}
		return comprobante;
	}

	/**
	 * @param comprobante
	 *            the comprobante to set
	 */
	public void setComprobante(Comprobante comprobante) {
		this.comprobante = comprobante;
	}

	/**
	 * @return the tipoServicio
	 */
	public BaseVO getTipoServicio() {
		if (tipoServicio == null) {
			tipoServicio = new BaseVO();
		}
		return tipoServicio;
	}

	/**
	 * @param tipoServicio
	 *            the tipoServicio to set
	 */
	public void setTipoServicio(BaseVO tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	/**
	 * @return the listaPagosComprobante
	 */
	public List<PagoServicio> getListaPagosComprobante() {
		return listaPagosComprobante;
	}

	/**
	 * @param listaPagosComprobante
	 *            the listaPagosComprobante to set
	 */
	public void setListaPagosComprobante(
			List<PagoServicio> listaPagosComprobante) {
		this.listaPagosComprobante = listaPagosComprobante;
	}

	/**
	 * @return the comprobanteBusqueda
	 */
	public Comprobante getComprobanteBusqueda() {
		if (comprobanteBusqueda == null) {
			comprobanteBusqueda = new Comprobante();
		}
		return comprobanteBusqueda;
	}

	/**
	 * @param comprobanteBusqueda
	 *            the comprobanteBusqueda to set
	 */
	public void setComprobanteBusqueda(Comprobante comprobanteBusqueda) {
		this.comprobanteBusqueda = comprobanteBusqueda;
	}

	/**
	 * @return the listaComprobantes
	 */
	public List<Comprobante> getListaComprobantes() {
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
	 * @return the proveedorBusqueda
	 */
	public Proveedor getProveedorBusqueda() {
		if (proveedorBusqueda == null) {
			proveedorBusqueda = new Proveedor();
		}
		return proveedorBusqueda;
	}

	/**
	 * @param proveedorBusqueda
	 *            the proveedorBusqueda to set
	 */
	public void setProveedorBusqueda(Proveedor proveedorBusqueda) {
		this.proveedorBusqueda = proveedorBusqueda;
	}

	/**
	 * @return the listadoProveedores
	 */
	public List<Proveedor> getListadoProveedores() {
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
	 * @return the consultoProveedor
	 */
	public boolean isConsultoProveedor() {
		return consultoProveedor;
	}

	/**
	 * @param consultoProveedor
	 *            the consultoProveedor to set
	 */
	public void setConsultoProveedor(boolean consultoProveedor) {
		this.consultoProveedor = consultoProveedor;
	}

	/**
	 * @return the detalleServicio2
	 */
	public DetalleServicioAgencia getDetalleServicio2() {
		return detalleServicio2;
	}

	/**
	 * @param detalleServicio2
	 *            the detalleServicio2 to set
	 */
	public void setDetalleServicio2(DetalleServicioAgencia detalleServicio2) {
		this.detalleServicio2 = detalleServicio2;
	}

	/**
	 * @return the guardoRelacionComprobantes
	 */
	public boolean isGuardoRelacionComprobantes() {
		return guardoRelacionComprobantes;
	}

	/**
	 * @param guardoRelacionComprobantes
	 *            the guardoRelacionComprobantes to set
	 */
	public void setGuardoRelacionComprobantes(boolean guardoRelacionComprobantes) {
		this.guardoRelacionComprobantes = guardoRelacionComprobantes;
	}

	/**
	 * @return the listaDocumentosAdicionales
	 */
	public List<DocumentoAdicional> getListaDocumentosAdicionales() {
		if (listaDocumentosAdicionales == null) {
			listaDocumentosAdicionales = new ArrayList<DocumentoAdicional>();
		}
		return listaDocumentosAdicionales;
	}

	/**
	 * @param listaDocumentosAdicionales
	 *            the listaDocumentosAdicionales to set
	 */
	public void setListaDocumentosAdicionales(
			List<DocumentoAdicional> listaDocumentosAdicionales) {
		this.listaDocumentosAdicionales = listaDocumentosAdicionales;
	}

	/**
	 * @return the documentoAdicional
	 */
	public DocumentoAdicional getDocumentoAdicional() {
		return documentoAdicional;
	}

	/**
	 * @param documentoAdicional
	 *            the documentoAdicional to set
	 */
	public void setDocumentoAdicional(DocumentoAdicional documentoAdicional) {
		this.documentoAdicional = documentoAdicional;
	}

	/**
	 * @return the listaComprobantesAdicionales
	 */
	public List<Comprobante> getListaComprobantesAdicionales() {
		if (listaComprobantesAdicionales == null) {
			listaComprobantesAdicionales = new ArrayList<Comprobante>();
		}
		return listaComprobantesAdicionales;
	}

	/**
	 * @param listaComprobantesAdicionales
	 *            the listaComprobantesAdicionales to set
	 */
	public void setListaComprobantesAdicionales(
			List<Comprobante> listaComprobantesAdicionales) {
		this.listaComprobantesAdicionales = listaComprobantesAdicionales;
	}

	/**
	 * @return the listadoServiciosPadre
	 */
	public List<SelectItem> getListadoServiciosPadre() {
		if (listadoServiciosPadre == null) {
			listadoServiciosPadre = new ArrayList<SelectItem>();
		}
		return listadoServiciosPadre;
	}

	/**
	 * @param listadoServiciosPadre
	 *            the listadoServiciosPadre to set
	 */
	public void setListadoServiciosPadre(List<SelectItem> listadoServiciosPadre) {
		this.listadoServiciosPadre = listadoServiciosPadre;
	}

	/**
	 * @return the agregoServicioPadre
	 */
	public boolean isAgregoServicioPadre() {
		return agregoServicioPadre;
	}

	/**
	 * @param agregoServicioPadre
	 *            the agregoServicioPadre to set
	 */
	public void setAgregoServicioPadre(boolean agregoServicioPadre) {
		this.agregoServicioPadre = agregoServicioPadre;
	}

	/**
	 * @return the columnasComprobantes
	 */
	public Integer getColumnasComprobantes() {
		return columnasComprobantes;
	}

	/**
	 * @param columnasComprobantes
	 *            the columnasComprobantes to set
	 */
	public void setColumnasComprobantes(Integer columnasComprobantes) {
		this.columnasComprobantes = columnasComprobantes;
	}

	/**
	 * @return the listadoDetalleServicioAgrupado
	 */
	public List<DetalleServicioAgencia> getListadoDetalleServicioAgrupado() {
		return listadoDetalleServicioAgrupado;
	}

	/**
	 * @param listadoDetalleServicioAgrupado
	 *            the listadoDetalleServicioAgrupado to set
	 */
	public void setListadoDetalleServicioAgrupado(
			List<DetalleServicioAgencia> listadoDetalleServicioAgrupado) {
		this.listadoDetalleServicioAgrupado = listadoDetalleServicioAgrupado;
	}

	/**
	 * @return the editaServicioAgregado
	 */
	public boolean isEditaServicioAgregado() {
		return editaServicioAgregado;
	}

	/**
	 * @param editaServicioAgregado the editaServicioAgregado to set
	 */
	public void setEditaServicioAgregado(boolean editaServicioAgregado) {
		this.editaServicioAgregado = editaServicioAgregado;
	}

	/**
	 * @return the cargoConfiguracionTipoServicio
	 */
	public boolean isCargoConfiguracionTipoServicio() {
		return cargoConfiguracionTipoServicio;
	}

	/**
	 * @param cargoConfiguracionTipoServicio the cargoConfiguracionTipoServicio to set
	 */
	public void setCargoConfiguracionTipoServicio(
			boolean cargoConfiguracionTipoServicio) {
		this.cargoConfiguracionTipoServicio = cargoConfiguracionTipoServicio;
	}
}

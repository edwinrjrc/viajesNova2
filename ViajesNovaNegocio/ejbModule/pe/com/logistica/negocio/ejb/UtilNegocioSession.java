package pe.com.logistica.negocio.ejb;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.Util.UtilParse;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.MaestroServicio;
import pe.com.logistica.bean.negocio.Parametro;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.negocio.dao.ComunDao;
import pe.com.logistica.negocio.dao.DestinoDao;
import pe.com.logistica.negocio.dao.MaestroServicioDao;
import pe.com.logistica.negocio.dao.ParametroDao;
import pe.com.logistica.negocio.dao.ProveedorDao;
import pe.com.logistica.negocio.dao.impl.ComunDaoImpl;
import pe.com.logistica.negocio.dao.impl.DestinoDaoImpl;
import pe.com.logistica.negocio.dao.impl.MaestroServicioDaoImpl;
import pe.com.logistica.negocio.dao.impl.ParametroDaoImpl;
import pe.com.logistica.negocio.dao.impl.ProveedorDaoImpl;
import pe.com.logistica.negocio.exception.ErrorConsultaDataException;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilEjb;

/**
 * Session Bean implementation class UtilNegocioSession
 */
@Stateless(name = "UtilNegocioSession")
public class UtilNegocioSession implements UtilNegocioSessionRemote, UtilNegocioSessionLocal {
	
	@EJB
	NegocioSessionLocal negocioSessionLocal;

	@Override
	public List<DetalleServicioAgencia> agruparServiciosHijos (List<DetalleServicioAgencia> listaServicios){
		List<DetalleServicioAgencia> listaServiciosAgrupados = new ArrayList<DetalleServicioAgencia>();
		
		try {
			for (DetalleServicioAgencia detalleServicioAgencia : listaServicios) {
				detalleServicioAgencia.setServiciosHijos(agruparHijos(detalleServicioAgencia.getServiciosHijos()));
				listaServiciosAgrupados.add(detalleServicioAgencia);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return listaServiciosAgrupados;
	}
	
	private List<DetalleServicioAgencia> agruparHijos (List<DetalleServicioAgencia> listaServicios){
		List<DetalleServicioAgencia> listaServiciosAgrupados = new ArrayList<DetalleServicioAgencia>();
		
		try {
			MaestroServicioDao maestroServicioDao = new MaestroServicioDaoImpl();
			List<MaestroServicio> listaTiposServicio = null;
			listaTiposServicio = maestroServicioDao.listarMaestroServiciosAdm();
			int agrupados = 0;
			BigDecimal montoAgrupado = null;
			DetalleServicioAgencia detalle = null;
			for (MaestroServicio maestroServicio : listaTiposServicio) {
				detalle = null;
				detalle = new DetalleServicioAgencia();
				agrupados = 0;
				montoAgrupado = BigDecimal.ZERO;
				for (DetalleServicioAgencia detalleHijo : listaServicios){
					if (detalleHijo.getTipoServicio().getCodigoEntero().intValue() == maestroServicio.getCodigoEntero().intValue()){
						montoAgrupado = montoAgrupado.add(detalleHijo.getTotalServicio());
						agrupados++;
						detalle.setCodigoEntero(detalleHijo.getCodigoEntero());
						detalle.setTipoServicio(maestroServicio);
						detalle.setFechaIda(detalleHijo.getFechaIda());
						detalle.setFechaServicio(detalleHijo.getFechaServicio());
						detalle.setFechaRegreso(detalleHijo.getFechaRegreso());
						detalle.setCantidad(detalleHijo.getCantidad());
						detalle.setPrecioUnitario(detalleHijo.getPrecioUnitario());
						detalle.setOrigen(detalleHijo.getOrigen());
						detalle.setDestino(detalleHijo.getDestino());
						detalle.setDescripcionServicio(detalleHijo.getDescripcionServicio());
						detalle.setServicioProveedor(detalleHijo.getServicioProveedor());
						detalle.setAerolinea(detalleHijo.getAerolinea());
						detalle.setHotel(detalleHijo.getHotel());
						detalle.setTieneDetraccion(detalleHijo.isTieneDetraccion());
						detalle.setTieneRetencion(detalleHijo.isTieneRetencion());
						detalle.setTarifaNegociada(detalleHijo.isTarifaNegociada());
						detalle.setConfiguracionTipoServicio(detalleHijo.getConfiguracionTipoServicio());
						detalle.setMontoComision(detalleHijo.getMontoComision());
						detalle.getCodigoEnteroAgrupados().add(detalleHijo.getCodigoEntero());
						detalle.setComprobanteAsociado(detalleHijo.getComprobanteAsociado());
						detalle.setTipoComprobante(detalleHijo.getTipoComprobante());
						detalle.setNroComprobante(detalleHijo.getNroComprobante());
						detalle.setIdComprobanteGenerado(detalleHijo.getIdComprobanteGenerado());
						detalle.setServicioPadre(detalleHijo.getServicioPadre());
					}
				}
				if (agrupados > 1){
					detalle.setDescripcionServicio("Servicios agrupados :: "+agrupados);
					detalle.setAgrupado(true);
					detalle.setCantidadAgrupados(agrupados);
					detalle.setPrecioUnitario(montoAgrupado);
					listaServiciosAgrupados.add(detalle);
				}
				else if (agrupados > 0) {
					listaServiciosAgrupados.add(detalle);
				}
			}
		} catch (SQLException e) {	
			e.printStackTrace();
		}
		return listaServiciosAgrupados;
	}
	
	@Override
	public ServicioAgencia colocarTipoNumeroComprobante(ServicioAgencia servicioAgencia){
		List<DetalleServicioAgencia> listaFinal = new ArrayList<DetalleServicioAgencia>();
		for (int a=0; a<servicioAgencia.getListaDetalleServicio().size(); a++){
			DetalleServicioAgencia detallePadre = servicioAgencia.getListaDetalleServicio().get(a);
			System.out.println("detallePadre::"+detallePadre);
			for (int b=0; b<detallePadre.getServiciosHijos().size(); b++){
				DetalleServicioAgencia detalle = detallePadre.getServiciosHijos().get(b);
				System.out.println("detalle::"+detalle);
				for (int x=0; x<servicioAgencia.getListaDetalleServicioAgrupado().size(); x++){
					DetalleServicioAgencia detallePadreAgrupado = servicioAgencia.getListaDetalleServicioAgrupado().get(x);
					System.out.println("detallePadreAgrupado::"+detallePadreAgrupado);
					for (int y=0; y<detallePadreAgrupado.getServiciosHijos().size(); y++){
						DetalleServicioAgencia detalleAgrupado = detallePadreAgrupado.getServiciosHijos().get(y);
						System.out.println("detalleAgrupado::"+detalleAgrupado);
						System.out.println("detalleAgrupado.isAgrupado()::"+detalleAgrupado.isAgrupado());
						if (detalleAgrupado.isAgrupado()){
							for (Integer id : detalleAgrupado.getCodigoEnteroAgrupados()){
								if (detalle.getCodigoEntero().intValue() == id.intValue() ){
									detalle.setTipoComprobante(detalleAgrupado.getTipoComprobante());
									detalle.setNroComprobante(detalleAgrupado.getNroComprobante());
								}
							}
						}
						else {
							if (detalle.getCodigoEntero().intValue() == detalleAgrupado.getCodigoEntero().intValue()){
								detalle.setTipoComprobante(detalleAgrupado.getTipoComprobante());
								detalle.setNroComprobante(detalleAgrupado.getNroComprobante());
							}
						}
					}
					detallePadre.getServiciosHijos().add(detalle);
				}
			}
			listaFinal.add(detallePadre);
		}
		
		servicioAgencia.setListaDetalleServicio(listaFinal);
		
		return servicioAgencia;
	}
	
	@Override
	public List<DetalleServicioAgencia> agregarServicioVenta(
			List<DetalleServicioAgencia> listaServiciosVenta,
			DetalleServicioAgencia detalleServicio)
			throws ErrorRegistroDataException, SQLException, Exception {

		Connection conn = null;

		try {
			conn = UtilConexion.obtenerConexion();

			MaestroServicioDao maestroServicioDao = new MaestroServicioDaoImpl();
			DestinoDao destinoDao = new DestinoDaoImpl();
			ProveedorDao proveedorDao = new ProveedorDaoImpl();
			ComunDao comunDao = new ComunDaoImpl();

			detalleServicio.setTipoServicio(maestroServicioDao
					.consultarMaestroServicio(detalleServicio.getTipoServicio()
							.getCodigoEntero(), conn));

			// obtener nombre empresa proveedor
			if (detalleServicio.getServicioProveedor().getProveedor()
					.getCodigoEntero() != null
					&& detalleServicio.getServicioProveedor().getProveedor()
							.getCodigoEntero().intValue() != 0) {
				detalleServicio.getServicioProveedor().setProveedor(
						proveedorDao.consultarProveedor(detalleServicio
								.getServicioProveedor().getProveedor()
								.getCodigoEntero(), conn));
			}

			// obtener nombre aerolinea
			if (detalleServicio.getAerolinea().getCodigoEntero() != null
					&& detalleServicio.getAerolinea().getCodigoEntero()
							.intValue() != 0) {
				detalleServicio.getAerolinea().setNombre(
						proveedorDao.consultarProveedor(
								detalleServicio.getAerolinea()
										.getCodigoEntero(), conn)
								.getNombreCompleto());
			}

			// obtener nombre empresa transporte
			if (detalleServicio.getEmpresaTransporte().getCodigoEntero() != null
					&& detalleServicio.getEmpresaTransporte().getCodigoEntero()
							.intValue() != 0) {
				detalleServicio.getEmpresaTransporte().setNombre(
						proveedorDao.consultarProveedor(
								detalleServicio.getEmpresaTransporte()
										.getCodigoEntero(), conn)
								.getNombreCompleto());
			}

			// obtener nombre operador
			if (detalleServicio.getOperadora().getCodigoEntero() != null
					&& detalleServicio.getOperadora().getCodigoEntero()
							.intValue() != 0) {
				detalleServicio.getOperadora().setNombre(
						proveedorDao.consultarProveedor(
								detalleServicio.getOperadora()
										.getCodigoEntero(), conn)
								.getNombreCompleto());
			}

			// obtener nombre hotel
			if (detalleServicio.getHotel().getCodigoEntero() != null
					&& detalleServicio.getHotel().getCodigoEntero().intValue() != 0) {
				detalleServicio.getHotel().setNombre(
						proveedorDao.consultarProveedor(
								detalleServicio.getHotel().getCodigoEntero(),
								conn).getNombreCompleto());
			}

			BigDecimal comision = BigDecimal.ZERO;
			BigDecimal totalVenta = BigDecimal.ZERO;

			if (StringUtils.isBlank(detalleServicio.getDescripcionServicio())) {
				detalleServicio.setDescripcionServicio(StringUtils
						.upperCase(detalleServicio.getTipoServicio()
								.getNombre()));
			}

			detalleServicio.setDescripcionServicio(StringUtils
					.upperCase(detalleServicio.getDescripcionServicio()));

			if (detalleServicio.getCantidad() == 0) {
				detalleServicio.setCantidad(1);
			}

			if (detalleServicio.getOrigen().getCodigoEntero() != null) {
				detalleServicio.setOrigen(destinoDao.consultarDestino(
						detalleServicio.getOrigen().getCodigoEntero(), conn));
				detalleServicio.setDestino(destinoDao.consultarDestino(
						detalleServicio.getDestino().getCodigoEntero(), conn));
			}

			boolean calcularIGV = false;
			calcularIGV = ("PE".equalsIgnoreCase(detalleServicio.getOrigen()
					.getPais().getAbreviado()) || "PE"
					.equalsIgnoreCase(detalleServicio.getDestino().getPais()
							.getAbreviado()));

			if (detalleServicio.getPrecioUnitario() != null) {
				BigDecimal total = detalleServicio.getPrecioUnitario()
						.multiply(
								UtilParse.parseIntABigDecimal(detalleServicio
										.getCantidad()));
				if (calcularIGV) {
					if (detalleServicio.isConIGV()) {
						ParametroDao parametroDao = new ParametroDaoImpl();
						BigDecimal valorParametro = BigDecimal.ZERO;
						Parametro param = parametroDao
								.consultarParametro(UtilEjb
										.obtenerEnteroPropertieMaestro(
												"codigoParametroIGV",
												"aplicacionDatosEjb"));
						List<MaestroServicio> lista = maestroServicioDao
								.consultarServiciosInvisibles(detalleServicio
										.getTipoServicio().getCodigoEntero());
						for (MaestroServicio maestroServicio : lista) {
							if (maestroServicio.getCodigoEntero().intValue() == UtilEjb
									.convertirCadenaEntero(param.getValor())) {
								valorParametro = UtilEjb
										.convertirCadenaDecimal(maestroServicio
												.getValorParametro());
								break;
							}
						}
						valorParametro = valorParametro.add(BigDecimal.ONE);
						BigDecimal precioBase = detalleServicio
								.getPrecioUnitario().divide(valorParametro, 4,
										RoundingMode.HALF_DOWN);
						total = precioBase.multiply(UtilParse
								.parseIntABigDecimal(detalleServicio
										.getCantidad()));
						detalleServicio.setPrecioUnitario(precioBase);
					}
				}

				totalVenta = totalVenta.add(total);
			}

			if (detalleServicio.getServicioProveedor().getPorcentajeComision() != null) {
				comision = detalleServicio.getServicioProveedor()
						.getPorcentajeComision().multiply(totalVenta);
				comision = comision.divide(BigDecimal.valueOf(100.0));

				ParametroDao parametroDao = new ParametroDaoImpl();
				Parametro paramIGV = parametroDao
						.consultarParametro(UtilEjb
								.obtenerEnteroPropertieMaestro(
										"codigoParametroImptoIGV",
										"aplicacionDatosEjb"));
				BigDecimal valorParametroIGV = BigDecimal.ZERO;
				valorParametroIGV = UtilEjb.convertirCadenaDecimal(paramIGV
						.getValor());
				valorParametroIGV = valorParametroIGV.add(BigDecimal.ONE);

				comision = comision.multiply(valorParametroIGV);
			}

			if (detalleServicio.getServicioProveedor().getProveedor()
					.getCodigoEntero() != null) {
				Proveedor proveedor = negocioSessionLocal.consultarProveedor(detalleServicio
						.getServicioProveedor().getProveedor()
						.getCodigoEntero().intValue());
				detalleServicio.getServicioProveedor().setProveedor(proveedor);
			}

			detalleServicio.setMontoComision(comision);

			int idDetServicio = comunDao.obtenerSiguienteSecuencia(conn);
			detalleServicio.setCodigoEntero(idDetServicio);
			listaServiciosVenta.add(detalleServicio);

			List<DetalleServicioAgencia> listaInvisibles = null;
			if (detalleServicio.getTipoServicio().isServicioPadre()) {
				listaInvisibles = agregarServicioVentaInvisible(idDetServicio,
						detalleServicio, calcularIGV);
			} else {
				listaInvisibles = agregarServicioVentaInvisible(detalleServicio
						.getServicioPadre().getCodigoEntero(), detalleServicio,
						calcularIGV);
			}

			if (listaInvisibles != null) {
				listaServiciosVenta.addAll(listaInvisibles);
			}

			listaServiciosVenta = UtilEjb
					.ordenarServiciosVenta(listaServiciosVenta);

			return listaServiciosVenta;
		} catch (Exception e) {
			throw new ErrorRegistroDataException(
					"No se pudo agregar el servicio al listado", e);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	@Override
	public List<DetalleServicioAgencia> actualizarServicioVenta(
			List<DetalleServicioAgencia> listaServiciosVenta,
			DetalleServicioAgencia detalleServicio)
			throws ErrorRegistroDataException, SQLException, Exception {

		Connection conn = null;

		try {
			DetalleServicioAgencia detalleServicioAgencia = null;
			int indice = 0;
			for (int i=0; i<listaServiciosVenta.size(); i++){
				detalleServicioAgencia = listaServiciosVenta.get(i);
				if (detalleServicioAgencia.getCodigoEntero().intValue() == detalleServicio.getCodigoEntero().intValue()){
					indice = i;
					break;
				}
			}
			
			conn = UtilConexion.obtenerConexion();

			MaestroServicioDao maestroServicioDao = new MaestroServicioDaoImpl();
			DestinoDao destinoDao = new DestinoDaoImpl();
			ProveedorDao proveedorDao = new ProveedorDaoImpl();

			detalleServicioAgencia.setTipoServicio(maestroServicioDao
					.consultarMaestroServicio(detalleServicio.getTipoServicio()
							.getCodigoEntero(), conn));

			// obtener nombre empresa proveedor
			if (detalleServicio.getServicioProveedor().getProveedor()
					.getCodigoEntero() != null
					&& detalleServicio.getServicioProveedor().getProveedor()
							.getCodigoEntero().intValue() != 0) {
				detalleServicioAgencia.getServicioProveedor().setProveedor(
						proveedorDao.consultarProveedor(detalleServicio
								.getServicioProveedor().getProveedor()
								.getCodigoEntero(), conn));
			}

			// obtener nombre aerolinea
			if (detalleServicio.getAerolinea().getCodigoEntero() != null
					&& detalleServicio.getAerolinea().getCodigoEntero()
							.intValue() != 0) {
				detalleServicioAgencia.getAerolinea().setNombre(
						proveedorDao.consultarProveedor(
								detalleServicio.getAerolinea()
										.getCodigoEntero(), conn)
								.getNombreCompleto());
			}

			// obtener nombre empresa transporte
			if (detalleServicio.getEmpresaTransporte().getCodigoEntero() != null
					&& detalleServicio.getEmpresaTransporte().getCodigoEntero()
							.intValue() != 0) {
				detalleServicioAgencia.getEmpresaTransporte().setNombre(
						proveedorDao.consultarProveedor(
								detalleServicio.getEmpresaTransporte()
										.getCodigoEntero(), conn)
								.getNombreCompleto());
			}

			// obtener nombre operador
			if (detalleServicio.getOperadora().getCodigoEntero() != null
					&& detalleServicio.getOperadora().getCodigoEntero()
							.intValue() != 0) {
				detalleServicioAgencia.getOperadora().setNombre(
						proveedorDao.consultarProveedor(
								detalleServicio.getOperadora()
										.getCodigoEntero(), conn)
								.getNombreCompleto());
			}

			// obtener nombre hotel
			if (detalleServicio.getHotel().getCodigoEntero() != null
					&& detalleServicio.getHotel().getCodigoEntero().intValue() != 0) {
				detalleServicioAgencia.getHotel().setNombre(
						proveedorDao.consultarProveedor(
								detalleServicio.getHotel().getCodigoEntero(),
								conn).getNombreCompleto());
			}

			BigDecimal comision = BigDecimal.ZERO;
			BigDecimal totalVenta = BigDecimal.ZERO;

			if (StringUtils.isBlank(detalleServicio.getDescripcionServicio())) {
				detalleServicioAgencia.setDescripcionServicio(StringUtils
						.upperCase(detalleServicio.getTipoServicio()
								.getNombre()));
			}

			detalleServicioAgencia.setDescripcionServicio(StringUtils
					.upperCase(detalleServicio.getDescripcionServicio()));

			detalleServicioAgencia.setCantidad(detalleServicio.getCantidad());
			if (detalleServicio.getCantidad() == 0) {
				detalleServicioAgencia.setCantidad(1);
			}

			if (detalleServicio.getOrigen().getCodigoEntero() != null) {
				detalleServicioAgencia.setOrigen(destinoDao.consultarDestino(
						detalleServicio.getOrigen().getCodigoEntero(), conn));
				detalleServicioAgencia.setDestino(destinoDao.consultarDestino(
						detalleServicio.getDestino().getCodigoEntero(), conn));
			}

			boolean calcularIGV = false;
			calcularIGV = ("PE".equalsIgnoreCase(detalleServicio.getOrigen()
					.getPais().getAbreviado()) || "PE"
					.equalsIgnoreCase(detalleServicio.getDestino().getPais()
							.getAbreviado()));

			if (detalleServicio.getPrecioUnitario() != null) {
				BigDecimal total = detalleServicio.getPrecioUnitario()
						.multiply(
								UtilParse.parseIntABigDecimal(detalleServicio
										.getCantidad()));
				if (calcularIGV) {
					if (detalleServicio.isConIGV()) {
						ParametroDao parametroDao = new ParametroDaoImpl();
						BigDecimal valorParametro = BigDecimal.ZERO;
						Parametro param = parametroDao
								.consultarParametro(UtilEjb
										.obtenerEnteroPropertieMaestro(
												"codigoParametroIGV",
												"aplicacionDatosEjb"));
						List<MaestroServicio> lista = maestroServicioDao
								.consultarServiciosInvisibles(detalleServicio
										.getTipoServicio().getCodigoEntero());
						for (MaestroServicio maestroServicio : lista) {
							if (maestroServicio.getCodigoEntero().intValue() == UtilEjb
									.convertirCadenaEntero(param.getValor())) {
								valorParametro = UtilEjb
										.convertirCadenaDecimal(maestroServicio
												.getValorParametro());
								break;
							}
						}
						valorParametro = valorParametro.add(BigDecimal.ONE);
						BigDecimal precioBase = detalleServicio
								.getPrecioUnitario().divide(valorParametro, 4,
										RoundingMode.HALF_DOWN);
						total = precioBase.multiply(UtilParse
								.parseIntABigDecimal(detalleServicio
										.getCantidad()));
						detalleServicioAgencia.setPrecioUnitario(precioBase);
					}
				}

				totalVenta = totalVenta.add(total);
			}

			if (detalleServicio.getServicioProveedor().getPorcentajeComision() != null) {
				comision = detalleServicio.getServicioProveedor()
						.getPorcentajeComision().multiply(totalVenta);
				comision = comision.divide(BigDecimal.valueOf(100.0));

				ParametroDao parametroDao = new ParametroDaoImpl();
				Parametro paramIGV = parametroDao
						.consultarParametro(UtilEjb
								.obtenerEnteroPropertieMaestro(
										"codigoParametroImptoIGV",
										"aplicacionDatosEjb"));
				BigDecimal valorParametroIGV = BigDecimal.ZERO;
				valorParametroIGV = UtilEjb.convertirCadenaDecimal(paramIGV
						.getValor());
				valorParametroIGV = valorParametroIGV.add(BigDecimal.ONE);

				comision = comision.multiply(valorParametroIGV);
			}
			detalleServicioAgencia.setMontoComision(comision);

			List<DetalleServicioAgencia> listaInvisibles = null;
			if (detalleServicio.getTipoServicio().isServicioPadre()) {
				listaInvisibles = agregarServicioVentaInvisible(detalleServicioAgencia.getCodigoEntero(),
						detalleServicio, calcularIGV);
			} else {
				listaInvisibles = agregarServicioVentaInvisible(detalleServicioAgencia
						.getServicioPadre().getCodigoEntero(), detalleServicio,
						calcularIGV);
			}
			
			for (DetalleServicioAgencia detalleServicioAgencia2 : listaServiciosVenta) {
				if (detalleServicioAgencia2.getServicioPadre().getCodigoEntero().intValue() == detalleServicioAgencia.getCodigoEntero().intValue()){
					listaServiciosVenta.remove(detalleServicioAgencia2);
				}
			}
			
			listaServiciosVenta.remove(indice);
			listaServiciosVenta.add(detalleServicioAgencia);
			if (listaInvisibles != null) {
				listaServiciosVenta.addAll(listaInvisibles);
			}

			listaServiciosVenta = UtilEjb
					.ordenarServiciosVenta(listaServiciosVenta);

			return listaServiciosVenta;
			
		} catch (Exception e) {
			throw new ErrorRegistroDataException(
					"No se pudo agregar el servicio al listado", e);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	private List<DetalleServicioAgencia> agregarServicioVentaInvisible(
			Integer idServicioPadre, DetalleServicioAgencia detalleServicio2,
			boolean calcularIGV) throws ErrorConsultaDataException, Exception {

		List<DetalleServicioAgencia> listaServicios = new ArrayList<DetalleServicioAgencia>();
		try {
			if (calcularIGV) {
				ComunDao comunDao = new ComunDaoImpl();
				MaestroServicioDao maestroServicioDao = new MaestroServicioDaoImpl();
				List<MaestroServicio> lista = maestroServicioDao
						.consultarServiciosInvisibles(detalleServicio2
								.getTipoServicio().getCodigoEntero());

				DetalleServicioAgencia detalle = null;
				for (MaestroServicio maestroServicio : lista) {
					detalle = new DetalleServicioAgencia();
					detalle.setCodigoEntero(comunDao
							.obtenerSiguienteSecuencia());
					detalle.setDescripcionServicio(maestroServicio.getNombre());
					detalle.setCantidad(1);
					detalle.getTipoServicio().setCodigoEntero(
							maestroServicio.getCodigoEntero());
					detalle.getTipoServicio().setNombre(
							maestroServicio.getNombre());
					detalle.setFechaIda(new Date());
					detalle.getServicioPadre().setCodigoEntero(idServicioPadre);

					try {
						BigDecimal cantidad = BigDecimal.valueOf(Double
								.valueOf(String.valueOf(detalleServicio2
										.getCantidad())));
						BigDecimal precioBase = detalleServicio2
								.getPrecioUnitario();
						BigDecimal porcenIGV = BigDecimal.valueOf(Double
								.valueOf(maestroServicio.getValorParametro()));
						BigDecimal totalServicioPrecede = precioBase
								.multiply(cantidad);

						BigDecimal igvServicio = totalServicioPrecede
								.multiply(porcenIGV);

						detalle.setMontoIGV(igvServicio);
						detalle.setPrecioUnitario(igvServicio);
						listaServicios.add(detalle);
					} catch (Exception e) {
						detalle.setMontoIGV(BigDecimal.ZERO);
						detalle.setPrecioUnitario(BigDecimal.ZERO);
						listaServicios.add(detalle);
						e.printStackTrace();
					}
				}
			}

			return listaServicios;
		} catch (SQLException e) {
			throw new ErrorConsultaDataException(
					"Error en Consulta de Servicios Ocultos", e);
		} catch (Exception e) {
			throw new ErrorConsultaDataException(
					"Error en Consulta de Servicios Ocultos", e);
		}

	}
}

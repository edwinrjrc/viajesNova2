package pe.com.logistica.negocio.ejb;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.cargaexcel.ReporteArchivoBusqueda;
import pe.com.logistica.bean.negocio.Cliente;
import pe.com.logistica.bean.negocio.Comprobante;
import pe.com.logistica.bean.negocio.ComprobanteBusqueda;
import pe.com.logistica.bean.negocio.Consolidador;
import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.bean.negocio.CorreoClienteMasivo;
import pe.com.logistica.bean.negocio.CuentaBancaria;
import pe.com.logistica.bean.negocio.CuotaPago;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.Direccion;
import pe.com.logistica.bean.negocio.DocumentoAdicional;
import pe.com.logistica.bean.negocio.Maestro;
import pe.com.logistica.bean.negocio.MaestroServicio;
import pe.com.logistica.bean.negocio.MovimientoCuenta;
import pe.com.logistica.bean.negocio.PagoServicio;
import pe.com.logistica.bean.negocio.ProgramaNovios;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.bean.negocio.ServicioAgenciaBusqueda;
import pe.com.logistica.bean.negocio.ServicioProveedor;
import pe.com.logistica.bean.negocio.Telefono;
import pe.com.logistica.bean.negocio.TipoCambio;
import pe.com.logistica.negocio.dao.ArchivoReporteDao;
import pe.com.logistica.negocio.dao.ClienteDao;
import pe.com.logistica.negocio.dao.ComprobanteNovaViajesDao;
import pe.com.logistica.negocio.dao.ConsolidadorDao;
import pe.com.logistica.negocio.dao.ContactoDao;
import pe.com.logistica.negocio.dao.CorreoMasivoDao;
import pe.com.logistica.negocio.dao.CuentaBancariaDao;
import pe.com.logistica.negocio.dao.DireccionDao;
import pe.com.logistica.negocio.dao.MaestroDao;
import pe.com.logistica.negocio.dao.MaestroServicioDao;
import pe.com.logistica.negocio.dao.ProveedorDao;
import pe.com.logistica.negocio.dao.ServicioNegocioDao;
import pe.com.logistica.negocio.dao.ServicioNovaViajesDao;
import pe.com.logistica.negocio.dao.ServicioNoviosDao;
import pe.com.logistica.negocio.dao.TelefonoDao;
import pe.com.logistica.negocio.dao.TipoCambioDao;
import pe.com.logistica.negocio.dao.impl.ArchivoReporteDaoImpl;
import pe.com.logistica.negocio.dao.impl.ClienteDaoImpl;
import pe.com.logistica.negocio.dao.impl.ComprobanteNovaViajesDaoImpl;
import pe.com.logistica.negocio.dao.impl.ConsolidadorDaoImpl;
import pe.com.logistica.negocio.dao.impl.ContactoDaoImpl;
import pe.com.logistica.negocio.dao.impl.CorreoMasivoDaoImpl;
import pe.com.logistica.negocio.dao.impl.CuentaBancariaDaoImpl;
import pe.com.logistica.negocio.dao.impl.DireccionDaoImpl;
import pe.com.logistica.negocio.dao.impl.MaestroDaoImpl;
import pe.com.logistica.negocio.dao.impl.MaestroServicioDaoImpl;
import pe.com.logistica.negocio.dao.impl.ProveedorDaoImpl;
import pe.com.logistica.negocio.dao.impl.ServicioNegocioDaoImpl;
import pe.com.logistica.negocio.dao.impl.ServicioNovaViajesDaoImpl;
import pe.com.logistica.negocio.dao.impl.ServicioNoviosDaoImpl;
import pe.com.logistica.negocio.dao.impl.TelefonoDaoImpl;
import pe.com.logistica.negocio.dao.impl.TipoCambioDaoImpl;
import pe.com.logistica.negocio.exception.ErrorConsultaDataException;
import pe.com.logistica.negocio.exception.ValidacionException;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilDatos;

/**
 * Session Bean implementation class ConsultaNegocioSession
 */
@Stateless(name = "ConsultaNegocioSession")
public class ConsultaNegocioSession implements ConsultaNegocioSessionRemote,
		ConsultaNegocioSessionLocal {

	@EJB
	UtilNegocioSessionLocal utilNegocioSessionLocal;

	/**
	 * Default constructor.
	 */
	public ConsultaNegocioSession() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Proveedor> listarProveedor(Proveedor proveedor)
			throws SQLException {
		ProveedorDao proveedorDao = new ProveedorDaoImpl();
		List<Proveedor> listaProveedores = proveedorDao
				.listarProveedor(proveedor);

		MaestroDao maestroDao = new MaestroDaoImpl();

		for (Proveedor proveedor2 : listaProveedores) {
			Maestro hijoMaestro = new Maestro();
			hijoMaestro.setCodigoMaestro(2);
			hijoMaestro.setCodigoEntero(proveedor2.getDireccion().getVia()
					.getCodigoEntero());
			hijoMaestro = maestroDao.consultarHijoMaestro(hijoMaestro);
			proveedor2.getDireccion().setDireccion(
					UtilDatos.obtenerDireccionCompleta(
							proveedor2.getDireccion(), hijoMaestro));

		}

		return listaProveedores;
	}

	@Override
	public Proveedor consultarProveedor(int codigoProveedor)
			throws SQLException, Exception {
		DireccionDao direccionDao = new DireccionDaoImpl();
		ProveedorDao proveedorDao = new ProveedorDaoImpl();
		ContactoDao contactoDao = new ContactoDaoImpl();

		Proveedor proveedor = proveedorDao.consultarProveedor(codigoProveedor);
		List<Direccion> listaDirecciones = direccionDao
				.consultarDireccionProveedor(codigoProveedor);

		for (Direccion direccion : listaDirecciones) {
			direccion.setDireccion(utilNegocioSessionLocal
					.obtenerDireccionCompleta(direccion));
		}
		proveedor.setListaDirecciones(listaDirecciones);
		proveedor.setListaContactos(contactoDao
				.consultarContactoProveedor(codigoProveedor));
		proveedor.setListaServicioProveedor(proveedorDao
				.consultarServicioProveedor(codigoProveedor));

		return proveedor;
	}

	@Override
	public List<Proveedor> buscarProveedor(Proveedor proveedor)
			throws SQLException {
		ProveedorDao proveedorDao = new ProveedorDaoImpl();
		List<Proveedor> listaProveedores = proveedorDao
				.buscarProveedor(proveedor);

		MaestroDao maestroDao = new MaestroDaoImpl();

		for (Proveedor proveedor2 : listaProveedores) {
			Maestro hijoMaestro = new Maestro();
			hijoMaestro.setCodigoMaestro(2);
			hijoMaestro.setCodigoEntero(proveedor2.getDireccion().getVia()
					.getCodigoEntero());
			hijoMaestro = maestroDao.consultarHijoMaestro(hijoMaestro);
			proveedor2.getDireccion().setDireccion(
					UtilDatos.obtenerDireccionCompleta(
							proveedor2.getDireccion(), hijoMaestro));

		}

		return listaProveedores;
	}

	@Override
	public List<Cliente> listarCliente() throws SQLException {
		ClienteDao clienteDao = new ClienteDaoImpl();
		List<Cliente> listaClientes = clienteDao.consultarPersona(null);

		MaestroDao maestroDao = new MaestroDaoImpl();

		for (Cliente cliente2 : listaClientes) {
			Maestro hijoMaestro = new Maestro();
			hijoMaestro.setCodigoMaestro(2);
			hijoMaestro.setCodigoEntero(cliente2.getDireccion().getVia()
					.getCodigoEntero());
			hijoMaestro = maestroDao.consultarHijoMaestro(hijoMaestro);
			cliente2.getDireccion().setDireccion(
					UtilDatos.obtenerDireccionCompleta(cliente2.getDireccion(),
							hijoMaestro));

		}

		return listaClientes;
	}

	@Override
	public List<Cliente> buscarCliente(Cliente cliente) throws SQLException {
		ClienteDao clienteDao = new ClienteDaoImpl();
		List<Cliente> listaClientes = clienteDao.buscarPersona(cliente);

		MaestroDao maestroDao = new MaestroDaoImpl();

		for (Cliente cliente2 : listaClientes) {
			Maestro hijoMaestro = new Maestro();
			hijoMaestro.setCodigoMaestro(2);
			hijoMaestro.setCodigoEntero(cliente2.getDireccion().getVia()
					.getCodigoEntero());
			hijoMaestro = maestroDao.consultarHijoMaestro(hijoMaestro);
			cliente2.getDireccion().setDireccion(
					UtilDatos.obtenerDireccionCompleta(cliente2.getDireccion(),
							hijoMaestro));

		}

		return listaClientes;
	}

	@Override
	public Cliente consultarCliente(int idcliente) throws SQLException,
			Exception {
		DireccionDao direccionDao = new DireccionDaoImpl();
		ClienteDao clienteDao = new ClienteDaoImpl();
		ContactoDao contactoDao = new ContactoDaoImpl();

		Cliente cliente = clienteDao.consultarCliente(idcliente);
		List<Direccion> listaDirecciones = direccionDao
				.consultarDireccionProveedor(idcliente);

		for (Direccion direccion : listaDirecciones) {
			direccion.setDireccion(utilNegocioSessionLocal
					.obtenerDireccionCompleta(direccion));
		}
		cliente.setListaDirecciones(listaDirecciones);
		cliente.setListaContactos(contactoDao
				.consultarContactoProveedor(idcliente));

		return cliente;
	}

	@Override
	public List<Cliente> listarClientesNovios(String genero)
			throws SQLException, Exception {
		ClienteDao clienteDao = new ClienteDaoImpl();
		return clienteDao.listarClientesNovios(genero);
	}

	@Override
	public List<Cliente> consultarClientesNovios(Cliente cliente)
			throws SQLException, Exception {
		ClienteDao clienteDao = new ClienteDaoImpl();
		return clienteDao.consultarClientesNovios(cliente);
	}

	@Override
	public List<ProgramaNovios> consultarNovios(ProgramaNovios programaNovios)
			throws SQLException, Exception {
		ServicioNoviosDao servicioNovios = new ServicioNoviosDaoImpl();
		return servicioNovios.consultarNovios(programaNovios);
	}

	@Override
	public List<CuotaPago> consultarCronograma(ServicioAgencia servicioAgencia)
			throws SQLException, Exception {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();
		return servicioNovaViajesDao.consultarCronogramaPago(servicioAgencia);
	}

	@Override
	public ServicioAgencia consultarServicioVenta(int idServicio)
			throws SQLException, Exception {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();

		Connection conn = null;

		ServicioAgencia servicioAgencia;
		try {
			conn = UtilConexion.obtenerConexion();

			servicioAgencia = servicioNovaViajesDao.consultarServiciosVenta2(
					idServicio, conn);

			List<DetalleServicioAgencia> listaServiciosPadre = servicioNovaViajesDao
					.consultaServicioDetallePadre(
							servicioAgencia.getCodigoEntero(), conn);

			List<DetalleServicioAgencia> listaHijos = null;
			DetalleServicioAgencia detalleServicioAgencia = null;
			List<DetalleServicioAgencia> listaServiciosPadreNueva = new ArrayList<DetalleServicioAgencia>();
			for (int i = 0; i < listaServiciosPadre.size(); i++) {
				detalleServicioAgencia = (DetalleServicioAgencia) listaServiciosPadre
						.get(i);
				detalleServicioAgencia
						.setDescripcionServicio(detalleServicioAgencia
								.getDescripcionServicio());
				listaHijos = new ArrayList<DetalleServicioAgencia>();
				listaHijos.add(detalleServicioAgencia);
				listaHijos
						.addAll(servicioNovaViajesDao
								.consultaServicioDetalleHijo(servicioAgencia
										.getCodigoEntero(),
										detalleServicioAgencia
												.getCodigoEntero(), conn));
				detalleServicioAgencia.setServiciosHijos(listaHijos);
				listaHijos = null;
				listaServiciosPadreNueva.add(detalleServicioAgencia);
			}
			servicioAgencia.setListaDetalleServicio(listaServiciosPadreNueva);

			if (servicioAgencia.getFormaPago().getCodigoEntero() != null
					&& servicioAgencia.getFormaPago().getCodigoEntero()
							.intValue() == 2) {
				servicioAgencia.setCronogramaPago(servicioNovaViajesDao
						.consultarCronogramaPago(servicioAgencia));
			}

		} catch (SQLException e) {
			throw new SQLException(e);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return servicioAgencia;
	}

	@Override
	public List<ServicioAgencia> listarServicioVenta(
			ServicioAgenciaBusqueda servicioAgencia) throws SQLException,
			Exception {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();
		return servicioNovaViajesDao.consultarServiciosVenta(servicioAgencia);
	}

	@Override
	public List<Cliente> consultarCliente2(Cliente cliente)
			throws SQLException, Exception {
		ClienteDao clienteDao = new ClienteDaoImpl();
		DireccionDao direccionDao = new DireccionDaoImpl();
		TelefonoDao telefonoDao = new TelefonoDaoImpl();
		ContactoDao contactoDao = new ContactoDaoImpl();

		Connection conn = null;

		List<Cliente> listarCliente;
		try {
			conn = UtilConexion.obtenerConexion();
			cliente.setTipoPersona(1);
			listarCliente = clienteDao.listarClientes(cliente, conn);
			int info = 0;
			for (Cliente cliente2 : listarCliente) {
				info = 1;
				cliente2.setInfoCliente(info);
				List<Direccion> listaDireccion = direccionDao
						.consultarDireccionPersona(cliente2.getCodigoEntero(),
								conn);
				if (listaDireccion != null && listaDireccion.size() > 0) {
					info++;
					cliente2.setInfoCliente(info);
					cliente2.setDireccion(listaDireccion.get(0));
					cliente2.getDireccion().setDireccion(
							utilNegocioSessionLocal
									.obtenerDireccionCompleta(cliente2
											.getDireccion()));
					for (Direccion direccion : listaDireccion) {
						List<Telefono> listaTelefono = telefonoDao
								.consultarTelefonosDireccion(
										direccion.getCodigoEntero(), conn);
						if (listaTelefono != null && listaTelefono.size() > 0) {
							info++;
							cliente2.setInfoCliente(info);
						}
					}
				}

				List<Contacto> listaContacto = contactoDao
						.listarContactosXPersona(cliente2.getCodigoEntero(),
								conn);
				if (listaContacto != null && listaContacto.size() > 0) {
					info++;
					cliente2.setInfoCliente(info);
					for (Contacto contacto : listaContacto) {
						List<Telefono> listaTelefono = telefonoDao
								.consultarTelefonosXPersona(
										contacto.getCodigoEntero(), conn);
						if (listaTelefono != null && listaTelefono.size() > 0) {
							info++;
							cliente2.setInfoCliente(info);
							cliente2.setTelefonoMovil(listaTelefono.get(0));
						}
					}
				}
			}

		} catch (SQLException e) {
			throw new SQLException(e);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return listarCliente;
	}

	@Override
	public List<ServicioProveedor> proveedoresXServicio(BaseVO servicio)
			throws SQLException, Exception {
		ServicioNegocioDao servicioNegocioDao = new ServicioNegocioDaoImpl();

		return servicioNegocioDao.proveedoresXServicio(servicio);
	}

	@Override
	public ProgramaNovios consultarProgramaNovios(int idProgramaNovios)
			throws ValidacionException, SQLException, Exception {
		ServicioNoviosDao servicioNoviosDao = new ServicioNoviosDaoImpl();

		ProgramaNovios programa = new ProgramaNovios();
		programa.setCodigoEntero(idProgramaNovios);

		Connection conn = null;

		try {
			conn = UtilConexion.obtenerConexion();

			List<ProgramaNovios> listaProgramaNovios = servicioNoviosDao
					.consultarNovios(programa, conn);
			if (listaProgramaNovios != null && !listaProgramaNovios.isEmpty()) {
				if (listaProgramaNovios.size() > 1) {
					throw new ValidacionException(
							"Se encontro mas de un Programa de novios");
				}
			}
			programa = listaProgramaNovios.get(0);

			programa.setListaInvitados(servicioNoviosDao
					.consultarInvitasosNovios(idProgramaNovios, conn));

		} catch (SQLException e) {
			throw new SQLException(e);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return programa;
	}

	@Override
	public List<MaestroServicio> listarMaestroServicio() throws SQLException,
			Exception {
		MaestroServicioDao maestroServicioDao = new MaestroServicioDaoImpl();

		return maestroServicioDao.listarMaestroServicios();
	}

	@Override
	public List<MaestroServicio> listarMaestroServicioAdm()
			throws SQLException, Exception {
		MaestroServicioDao maestroServicioDao = new MaestroServicioDaoImpl();

		return maestroServicioDao.listarMaestroServiciosAdm();
	}

	@Override
	public List<MaestroServicio> listarMaestroServicioFee()
			throws SQLException, Exception {
		MaestroServicioDao maestroServicioDao = new MaestroServicioDaoImpl();

		return maestroServicioDao.listarMaestroServiciosFee();
	}

	@Override
	public List<MaestroServicio> listarMaestroServicioIgv()
			throws SQLException, Exception {
		MaestroServicioDao maestroServicioDao = new MaestroServicioDaoImpl();

		return maestroServicioDao.listarMaestroServiciosIgv();
	}

	@Override
	public List<MaestroServicio> listarMaestroServicioImpto()
			throws SQLException, Exception {
		MaestroServicioDao maestroServicioDao = new MaestroServicioDaoImpl();

		return maestroServicioDao.listarMaestroServiciosImpto();
	}

	@Override
	public MaestroServicio consultarMaestroServicio(int idMaestroServicio)
			throws SQLException, Exception {
		MaestroServicioDao maestroServicioDao = new MaestroServicioDaoImpl();

		MaestroServicio maestroServicio = maestroServicioDao
				.consultarMaestroServicio(idMaestroServicio);

		List<BaseVO> listaDependientes = maestroServicioDao
				.consultarServicioDependientes(maestroServicio
						.getCodigoEntero());

		maestroServicio.setListaServicioDepende(listaDependientes);

		return maestroServicio;
	}

	@Override
	public List<CorreoClienteMasivo> listarClientesCorreo()
			throws SQLException, Exception {
		CorreoMasivoDao correoMasivoDao = new CorreoMasivoDaoImpl();

		return correoMasivoDao.listarClientesCorreo();
	}

	@Override
	public List<Cliente> listarClientesCumples() throws SQLException, Exception {
		ClienteDao clienteDao = new ClienteDaoImpl();
		return clienteDao.listarClienteCumpleanieros();
	}

	@Override
	public List<BaseVO> consultaServiciosDependientes(Integer idServicio)
			throws SQLException, Exception {
		MaestroServicioDao maestroServicioDao = new MaestroServicioDaoImpl();

		return maestroServicioDao.consultarServicioDependientes(idServicio);
	}

	@Override
	public List<Consolidador> listarConsolidador() throws SQLException,
			Exception {
		ConsolidadorDao consolidadorDao = new ConsolidadorDaoImpl();

		return consolidadorDao.listarConsolidador();
	}

	@Override
	public Consolidador consultarConsolidador(Consolidador consolidador)
			throws SQLException, Exception {
		ConsolidadorDao consolidadorDao = new ConsolidadorDaoImpl();

		return consolidadorDao.consultarConsolidador(consolidador);
	}

	@Override
	public List<PagoServicio> listarPagosServicio(Integer idServicio)
			throws SQLException, Exception {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();

		return servicioNovaViajesDao.listarPagosServicio(idServicio);
	}

	@Override
	public List<PagoServicio> listarPagosObligacion(Integer idObligacion)
			throws SQLException, Exception {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();

		return servicioNovaViajesDao.listarPagosObligacion(idObligacion);
	}

	@Override
	public BigDecimal consultarSaldoServicio(Integer idServicio)
			throws SQLException, Exception {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();

		return servicioNovaViajesDao.consultarSaldoServicio(idServicio);
	}

	@Override
	public List<DetalleServicioAgencia> consultarDetalleServicioComprobante(
			Integer idServicio) throws SQLException, Exception {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();

		List<DetalleServicioAgencia> lista = servicioNovaViajesDao
				.consultaServicioDetalleComprobante(idServicio);

		for (DetalleServicioAgencia detalleServicioAgencia : lista) {
			detalleServicioAgencia.getServiciosHijos().add(
					detalleServicioAgencia);
			List<DetalleServicioAgencia> lista2 = servicioNovaViajesDao
					.consultaServicioDetalleComprobanteHijo(idServicio,
							detalleServicioAgencia.getCodigoEntero());
			detalleServicioAgencia.getServiciosHijos().addAll(lista2);
		}

		return lista;
	}

	@Override
	public List<DetalleServicioAgencia> consultarDetServComprobanteObligacion(
			Integer idServicio) throws ErrorConsultaDataException,
			SQLException, Exception {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();
		List<DetalleServicioAgencia> lista = null;
		Connection conn = null;

		try {
			conn = UtilConexion.obtenerConexion();
			lista = servicioNovaViajesDao.consultaServDetComprobanteObligacion(
					idServicio, conn);
			for (DetalleServicioAgencia detalleServicioAgencia : lista) {
				detalleServicioAgencia.getServiciosHijos().add(
						detalleServicioAgencia);
				detalleServicioAgencia.getServiciosHijos().addAll(
						servicioNovaViajesDao
								.consultaServDetComprobanteObligacionHijo(
										idServicio, detalleServicioAgencia
												.getCodigoEntero(), conn));
			}

			return lista;
		} catch (SQLException e) {
			throw new ErrorConsultaDataException(e);
		} catch (Exception e) {
			throw new ErrorConsultaDataException(e);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	@Override
	public List<Comprobante> listarObligacionXPagar(Comprobante comprobante)
			throws SQLException, Exception {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();
		return servicioNovaViajesDao.consultaObligacionXPagar(comprobante);
	}

	@Override
	public List<DocumentoAdicional> listarDocumentosAdicionales(
			Integer idServicio) throws SQLException {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();
		return servicioNovaViajesDao.listarDocumentosAdicionales(idServicio);
	}

	@Override
	public List<Comprobante> consultarComprobantesGenerados(
			ComprobanteBusqueda comprobanteBusqueda)
			throws ErrorConsultaDataException {
		try {
			ComprobanteNovaViajesDao comprobanteNovaViajesDao = new ComprobanteNovaViajesDaoImpl();
			return comprobanteNovaViajesDao
					.consultarComprobantes(comprobanteBusqueda);
		} catch (SQLException e) {
			throw new ErrorConsultaDataException(e);
		} catch (Exception e) {
			throw new ErrorConsultaDataException(e);
		}
	}

	@Override
	public Comprobante consultarComprobante(Integer idComprobante)
			throws ErrorConsultaDataException {
		try {
			ComprobanteNovaViajesDao comprobanteNovaViajesDao = new ComprobanteNovaViajesDaoImpl();

			ComprobanteBusqueda comprobanteBusqueda = new ComprobanteBusqueda();
			comprobanteBusqueda.setCodigoEntero(idComprobante);

			List<Comprobante> comprobantes = comprobanteNovaViajesDao
					.consultarComprobantes(comprobanteBusqueda);

			Comprobante comprobante = comprobantes.get(0);
			comprobante.setDetalleComprobante(comprobanteNovaViajesDao
					.consultarDetalleComprobante(idComprobante));

			return comprobante;
		} catch (SQLException e) {
			throw new ErrorConsultaDataException(e);
		} catch (Exception e) {
			throw new ErrorConsultaDataException(e);
		}
	}

	@Override
	public List<ReporteArchivoBusqueda> consultarArchivosCargados(
			ReporteArchivoBusqueda reporteArchivoBusqueda)
			throws ErrorConsultaDataException {
		try {
			ArchivoReporteDao archivoReporteDao = new ArchivoReporteDaoImpl();
			return archivoReporteDao
					.consultarArchivosCargados(reporteArchivoBusqueda);
		} catch (SQLException e) {
			throw new ErrorConsultaDataException(e);
		} catch (Exception e) {
			throw new ErrorConsultaDataException(e);
		}
	}

	@Override
	public DetalleServicioAgencia consultaDetalleServicioDetalle(
			int idServicio, int idDetServicio) throws SQLException {

		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();

		DetalleServicioAgencia detalle = servicioNovaViajesDao
				.consultaDetalleServicioDetalle(idServicio, idDetServicio);

		switch (detalle.getTipoServicio().getCodigoEntero().intValue()) {
		case 11:// BOLETO DE VIAJE
			detalle.getRuta().setTramos(
					servicioNovaViajesDao.consultarTramos(detalle.getRuta()
							.getCodigoEntero()));
			break;
		}
		;

		return detalle;
	}

	@Override
	public List<CuentaBancaria> listarCuentasBancarias() throws SQLException {
		CuentaBancariaDao cuentaBancariaDao = new CuentaBancariaDaoImpl();

		return cuentaBancariaDao.listarCuentasBancarias();
	}

	@Override
	public CuentaBancaria consultaCuentaBancaria(Integer idCuenta)
			throws SQLException {
		CuentaBancariaDao cuentaBancariaDao = new CuentaBancariaDaoImpl();

		return cuentaBancariaDao.consultaCuentaBancaria(idCuenta);
	}

	@Override
	public List<CuentaBancaria> listarCuentasBancariasCombo()
			throws SQLException {
		CuentaBancariaDao cuentaBancariaDao = new CuentaBancariaDaoImpl();

		return cuentaBancariaDao.listarCuentasBancariasCombo();
	}

	@Override
	public Comprobante consultarComprobanteObligacion(Integer idObligacion)
			throws SQLException {
		ComprobanteNovaViajesDao comprobanteDao = new ComprobanteNovaViajesDaoImpl();

		return comprobanteDao.consultarObligacion(idObligacion);
	}

	@Override
	public List<CuentaBancaria> listarCuentasBancariasProveedor(
			Integer idProveedor) throws SQLException {
		ProveedorDao proveedorDao = new ProveedorDaoImpl();

		return proveedorDao.listarCuentasBancarias(idProveedor);
	}

	@Override
	public List<MovimientoCuenta> listarMovimientosXCuenta(Integer idCuenta)
			throws SQLException {
		CuentaBancariaDao cuentaBancariaDao = new CuentaBancariaDaoImpl();

		return cuentaBancariaDao.listarMovimientoCuentaBancaria(idCuenta);
	}

	@Override
	public List<TipoCambio> listarTipoCambio(Date fecha) throws SQLException {
		TipoCambioDao tipoCambioDao = new TipoCambioDaoImpl();

		return tipoCambioDao.listarTipoCambio(fecha);
	}
}

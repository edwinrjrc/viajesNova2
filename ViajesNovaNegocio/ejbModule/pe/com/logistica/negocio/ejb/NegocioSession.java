package pe.com.logistica.negocio.ejb;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.internet.AddressException;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.Util.UtilParse;
import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.cargaexcel.ColumnasExcel;
import pe.com.logistica.bean.cargaexcel.ReporteArchivo;
import pe.com.logistica.bean.cargaexcel.ReporteArchivoBusqueda;
import pe.com.logistica.bean.negocio.Cliente;
import pe.com.logistica.bean.negocio.Comprobante;
import pe.com.logistica.bean.negocio.ComprobanteBusqueda;
import pe.com.logistica.bean.negocio.Consolidador;
import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.bean.negocio.CorreoClienteMasivo;
import pe.com.logistica.bean.negocio.CorreoMasivo;
import pe.com.logistica.bean.negocio.CuentaBancaria;
import pe.com.logistica.bean.negocio.CuotaPago;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.Direccion;
import pe.com.logistica.bean.negocio.DocumentoAdicional;
import pe.com.logistica.bean.negocio.EventoObsAnu;
import pe.com.logistica.bean.negocio.Maestro;
import pe.com.logistica.bean.negocio.MaestroServicio;
import pe.com.logistica.bean.negocio.PagoServicio;
import pe.com.logistica.bean.negocio.ProgramaNovios;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.bean.negocio.ServicioAgenciaBusqueda;
import pe.com.logistica.bean.negocio.ServicioNovios;
import pe.com.logistica.bean.negocio.ServicioProveedor;
import pe.com.logistica.bean.negocio.Telefono;
import pe.com.logistica.bean.negocio.Tramo;
import pe.com.logistica.bean.negocio.Ubigeo;
import pe.com.logistica.negocio.dao.ArchivoReporteDao;
import pe.com.logistica.negocio.dao.ClienteDao;
import pe.com.logistica.negocio.dao.ComprobanteNovaViajesDao;
import pe.com.logistica.negocio.dao.ConsolidadorDao;
import pe.com.logistica.negocio.dao.ContactoDao;
import pe.com.logistica.negocio.dao.CorreoMasivoDao;
import pe.com.logistica.negocio.dao.CuentaBancariaDao;
import pe.com.logistica.negocio.dao.DestinoDao;
import pe.com.logistica.negocio.dao.DireccionDao;
import pe.com.logistica.negocio.dao.MaestroDao;
import pe.com.logistica.negocio.dao.MaestroServicioDao;
import pe.com.logistica.negocio.dao.PersonaDao;
import pe.com.logistica.negocio.dao.ProveedorDao;
import pe.com.logistica.negocio.dao.ServicioNegocioDao;
import pe.com.logistica.negocio.dao.ServicioNovaViajesDao;
import pe.com.logistica.negocio.dao.ServicioNoviosDao;
import pe.com.logistica.negocio.dao.TelefonoDao;
import pe.com.logistica.negocio.dao.UbigeoDao;
import pe.com.logistica.negocio.dao.impl.ArchivoReporteDaoImpl;
import pe.com.logistica.negocio.dao.impl.ClienteDaoImpl;
import pe.com.logistica.negocio.dao.impl.ComprobanteNovaViajesDaoImpl;
import pe.com.logistica.negocio.dao.impl.ConsolidadorDaoImpl;
import pe.com.logistica.negocio.dao.impl.ContactoDaoImpl;
import pe.com.logistica.negocio.dao.impl.CorreoMasivoDaoImpl;
import pe.com.logistica.negocio.dao.impl.CuentaBancariaDaoImpl;
import pe.com.logistica.negocio.dao.impl.DestinoDaoImpl;
import pe.com.logistica.negocio.dao.impl.DireccionDaoImpl;
import pe.com.logistica.negocio.dao.impl.MaestroDaoImpl;
import pe.com.logistica.negocio.dao.impl.MaestroServicioDaoImpl;
import pe.com.logistica.negocio.dao.impl.PersonaDaoImpl;
import pe.com.logistica.negocio.dao.impl.ProveedorDaoImpl;
import pe.com.logistica.negocio.dao.impl.ServicioNegocioDaoImpl;
import pe.com.logistica.negocio.dao.impl.ServicioNovaViajesDaoImpl;
import pe.com.logistica.negocio.dao.impl.ServicioNoviosDaoImpl;
import pe.com.logistica.negocio.dao.impl.TelefonoDaoImpl;
import pe.com.logistica.negocio.dao.impl.UbigeoDaoImpl;
import pe.com.logistica.negocio.exception.EnvioCorreoException;
import pe.com.logistica.negocio.exception.ErrorConsultaDataException;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;
import pe.com.logistica.negocio.exception.ResultadoCeroDaoException;
import pe.com.logistica.negocio.exception.ValidacionException;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilCorreo;
import pe.com.logistica.negocio.util.UtilDatos;
import pe.com.logistica.negocio.util.UtilEjb;
import pe.com.logistica.negocio.util.UtilJdbc;

/**
 * Session Bean implementation class NegocioSession
 */
@Stateless(name = "NegocioSession")
public class NegocioSession implements NegocioSessionRemote,
		NegocioSessionLocal {

	@Override
	public Direccion agregarDireccion(Direccion direccion) throws SQLException,
			Exception {
		String iddepartamento = direccion.getUbigeo().getDepartamento()
				.getCodigoCadena();
		String idprovincia = direccion.getUbigeo().getProvincia()
				.getCodigoCadena();
		String iddistrito = direccion.getUbigeo().getDistrito()
				.getCodigoCadena();
		UbigeoDao ubigeoDao = new UbigeoDaoImpl();
		Ubigeo resultado = ubigeoDao.consultarUbigeo(iddepartamento + "0000");
		direccion.getUbigeo().setDepartamento(resultado.getDepartamento());
		direccion.getUbigeo().getDepartamento()
				.setNombre(resultado.getNombre());

		resultado = ubigeoDao.consultarUbigeo(iddepartamento + idprovincia
				+ "00");
		direccion.getUbigeo().setProvincia(resultado.getProvincia());
		direccion.getUbigeo().getProvincia().setNombre(resultado.getNombre());

		resultado = ubigeoDao.consultarUbigeo(iddepartamento + idprovincia
				+ iddistrito);
		direccion.getUbigeo().setDistrito(resultado.getDistrito());
		direccion.getUbigeo().getDistrito().setNombre(resultado.getNombre());

		direccion.getUbigeo().setCodigoCadena(
				iddepartamento + idprovincia + iddistrito);

		direccion.setDireccion(obtenerDireccionCompleta(direccion));

		return direccion;
	}

	@Override
	public Contacto agregarContacto(Contacto contacto) throws SQLException,
			Exception {
		MaestroDao maestroDao = new MaestroDaoImpl();

		try {
			Maestro hijoMaestro = new Maestro();
			hijoMaestro.setCodigoEntero(contacto.getArea().getCodigoEntero());
			hijoMaestro.setCodigoMaestro(4);
			hijoMaestro = maestroDao.consultarHijoMaestro(hijoMaestro);
			contacto.getArea().setNombre(hijoMaestro.getNombre());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return contacto;
	}

	@Override
	public boolean registrarProveedor(Proveedor proveedor)
			throws ResultadoCeroDaoException, SQLException, Exception {
		PersonaDao personaDao = new PersonaDaoImpl();
		DireccionDao direccionDao = new DireccionDaoImpl();
		TelefonoDao telefonoDao = new TelefonoDaoImpl();
		ProveedorDao proveedorDao = new ProveedorDaoImpl();
		ContactoDao contactoDao = new ContactoDaoImpl();

		Connection conexion = null;
		try {
			conexion = UtilConexion.obtenerConexion();

			proveedor.setTipoPersona(2);
			int idPersona = personaDao.registrarPersona(proveedor, conexion);
			if (idPersona == 0) {
				throw new ResultadoCeroDaoException(
						"No se pudo completar el registro de la persona");
			}
			proveedor.setCodigoEntero(idPersona);
			if (proveedor.getListaDirecciones() != null) {
				int idDireccion = 0;
				for (Direccion direccion : proveedor.getListaDirecciones()) {
					idDireccion = direccionDao.registrarDireccion(direccion,
							conexion);
					if (idDireccion == 0) {
						throw new ResultadoCeroDaoException(
								"No se pudo completar el registro de la direccion");
					}
					direccion.setCodigoEntero(idDireccion);
					int idTelefono = 0;
					if (!direccion.getTelefonos().isEmpty()) {
						for (Telefono telefono : direccion.getTelefonos()) {
							telefono.getEmpresaOperadora().setCodigoEntero(0);
							idTelefono = telefonoDao.registrarTelefono(
									telefono, conexion);
							if (idTelefono == 0) {
								throw new ResultadoCeroDaoException(
										"No se pudo completar el registro del telefono");
							}
							telefonoDao.registrarTelefonoDireccion(idTelefono,
									idDireccion, conexion);
						}
					}

					direccionDao.registrarPersonaDireccion(
							proveedor.getCodigoEntero(),
							proveedor.getTipoPersona(), idDireccion, conexion);
				}
			}

			if (proveedor.getListaContactos() != null) {
				int idContacto = 0;
				for (Contacto contacto : proveedor.getListaContactos()) {
					contacto.setTipoPersona(3);
					idContacto = personaDao
							.registrarPersona(contacto, conexion);
					contacto.setCodigoEntero(idContacto);
					if (!contacto.getListaTelefonos().isEmpty()) {
						for (Telefono telefono : contacto.getListaTelefonos()) {
							int idTelefono = telefonoDao.registrarTelefono(
									telefono, conexion);
							if (idTelefono == 0) {
								throw new ResultadoCeroDaoException(
										"No se pudo completar el registro del telefono");
							}
							telefonoDao.registrarTelefonoPersona(idTelefono,
									idContacto, conexion);
						}
					}
					contactoDao.registrarContactoProveedor(idPersona, contacto,
							conexion);

					contactoDao.ingresarCorreoElectronico(contacto, conexion);
				}
			}
			if (proveedor.getListaServicioProveedor() != null) {
				for (ServicioProveedor servicio : proveedor
						.getListaServicioProveedor()) {
					boolean resultado = proveedorDao.ingresarServicioProveedor(
							idPersona, servicio, conexion);
					if (!resultado) {
						throw new ResultadoCeroDaoException(
								"No se pudo completar el registro del servicios");
					}
				}
			}

			proveedorDao.registroProveedor(proveedor, conexion);

			proveedorDao.registroProveedorTipo(proveedor, conexion);

			return true;
		} catch (ResultadoCeroDaoException e) {
			throw new ResultadoCeroDaoException(e.getMensajeError(), e);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			if (conexion != null) {
				conexion.close();
			}
		}
	}

	@Override
	public boolean actualizarProveedor(Proveedor proveedor)
			throws SQLException, ResultadoCeroDaoException, Exception {
		PersonaDao personaDao = new PersonaDaoImpl();
		DireccionDao direccionDao = new DireccionDaoImpl();
		TelefonoDao telefonoDao = new TelefonoDaoImpl();
		ProveedorDao proveedorDao = new ProveedorDaoImpl();
		ContactoDao contactoDao = new ContactoDaoImpl();

		Connection conexion = null;
		try {
			conexion = UtilConexion.obtenerConexion();

			proveedor.setTipoPersona(2);

			Integer idPersona = personaDao.actualizarPersona(proveedor,
					conexion);
			if (idPersona == 0) {
				throw new ResultadoCeroDaoException(
						"No se pudo completar la actualización de la persona");
			}
			direccionDao.eliminarDireccionPersona(proveedor, conexion);
			if (proveedor.getListaDirecciones() != null) {
				int idDireccion = 0;
				for (Direccion direccion : proveedor.getListaDirecciones()) {
					idDireccion = direccionDao.actualizarDireccion(direccion,
							conexion);
					int idTelefono = 0;
					if (idDireccion == 0) {
						throw new ResultadoCeroDaoException(
								"No se pudo completar la actualización de la dirección");
					} else {
						direccionDao.eliminarTelefonoDireccion(direccion,
								conexion);
						List<Telefono> listTelefonos = direccion.getTelefonos();
						if (!listTelefonos.isEmpty()) {
							for (Telefono telefono : listTelefonos) {
								telefono.getEmpresaOperadora().setCodigoEntero(
										0);
								idTelefono = telefonoDao.registrarTelefono(
										telefono, conexion);
								if (idTelefono == 0) {
									throw new ResultadoCeroDaoException(
											"No se pudo completar el registro del teléfono de direccion");
								}
								telefonoDao.registrarTelefonoDireccion(
										idTelefono, idDireccion, conexion);
							}
						}
					}

					direccionDao.registrarPersonaDireccion(
							proveedor.getCodigoEntero(),
							proveedor.getTipoPersona(), idDireccion, conexion);
				}
			}

			contactoDao.eliminarContactoProveedor(proveedor, conexion);
			if (proveedor.getListaContactos() != null) {
				int idContacto = 0;
				for (Contacto contacto : proveedor.getListaContactos()) {
					contacto.setTipoPersona(3);

					idContacto = personaDao
							.registrarPersona(contacto, conexion);
					contacto.setCodigoEntero(idContacto);

					if (!contacto.getListaTelefonos().isEmpty()) {
						for (Telefono telefono : contacto.getListaTelefonos()) {
							int idTelefono = telefonoDao.registrarTelefono(
									telefono, conexion);
							if (idTelefono == 0) {
								throw new ResultadoCeroDaoException(
										"No se pudo completar el registro del teléfono de contacto");
							}
							telefonoDao.registrarTelefonoPersona(idTelefono,
									idContacto, conexion);
						}
					}
					contactoDao.registrarContactoProveedor(idPersona, contacto,
							conexion);

					contactoDao.eliminarCorreosContacto(contacto, conexion);

					contactoDao.ingresarCorreoElectronico(contacto, conexion);
				}
			}
			if (proveedor.getListaServicioProveedor() != null) {
				for (ServicioProveedor servicio : proveedor
						.getListaServicioProveedor()) {
					boolean resultado = proveedorDao
							.actualizarServicioProveedor(idPersona, servicio,
									conexion);
					if (!resultado) {
						throw new ResultadoCeroDaoException(
								"No se pudo completar la actualizacion del servicio");
					}
				}
			}
			proveedorDao.actualizarProveedor(proveedor, conexion);

			proveedorDao.actualizarProveedorTipo(proveedor, conexion);

			return true;
		} catch (ResultadoCeroDaoException e) {
			throw new ResultadoCeroDaoException(e.getMensajeError(), e);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			if (conexion != null) {
				conexion.close();
			}
		}
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
			direccion.setDireccion(obtenerDireccionCompleta(direccion));
		}
		proveedor.setListaDirecciones(listaDirecciones);
		proveedor.setListaContactos(contactoDao
				.consultarContactoProveedor(codigoProveedor));
		proveedor.setListaServicioProveedor(proveedorDao
				.consultarServicioProveedor(codigoProveedor));

		return proveedor;
	}

	private String obtenerDireccionCompleta(Direccion direccion)
			throws SQLException, Exception {
		MaestroDao maestroDao = new MaestroDaoImpl();
		Maestro hijoMaestro = new Maestro();
		hijoMaestro.setCodigoMaestro(2);
		hijoMaestro.setCodigoEntero(direccion.getVia().getCodigoEntero());
		hijoMaestro = maestroDao.consultarHijoMaestro(hijoMaestro);
		String direccionCompleta = "" + hijoMaestro.getAbreviatura() + " "
				+ direccion.getNombreVia();
		if (StringUtils.isNotBlank(direccion.getNumero())) {
			direccionCompleta = direccionCompleta + " Nro "
					+ direccion.getNumero();
		} else {
			direccionCompleta = direccionCompleta + " Mz. "
					+ direccion.getManzana();
			direccionCompleta = direccionCompleta + " Lt. "
					+ direccion.getLote();
		}
		if (StringUtils.isNotBlank(direccion.getInterior())) {
			direccionCompleta = direccionCompleta + " Int "
					+ direccion.getInterior();
		}

		return UtilJdbc.convertirMayuscula(direccionCompleta);
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
	public boolean registrarCliente(Cliente cliente)
			throws ResultadoCeroDaoException, SQLException, Exception {
		PersonaDao personaDao = new PersonaDaoImpl();
		DireccionDao direccionDao = new DireccionDaoImpl();
		TelefonoDao telefonoDao = new TelefonoDaoImpl();
		ContactoDao contactoDao = new ContactoDaoImpl();

		Connection conexion = null;
		try {
			conexion = UtilConexion.obtenerConexion();

			cliente.setTipoPersona(1);
			int idPersona = personaDao.registrarPersona(cliente, conexion);
			if (idPersona == 0) {
				throw new ResultadoCeroDaoException(
						"No se pudo completar el registro de la persona");
			}
			cliente.setCodigoEntero(idPersona);
			if (cliente.getListaDirecciones() != null) {
				int idDireccion = 0;
				for (Direccion direccion : cliente.getListaDirecciones()) {
					idDireccion = direccionDao.registrarDireccion(direccion,
							conexion);
					if (idDireccion == 0) {
						throw new ResultadoCeroDaoException(
								"No se pudo completar el registro de la direccion");
					}
					direccion.setCodigoEntero(idDireccion);
					int idTelefono = 0;
					if (!direccion.getTelefonos().isEmpty()) {
						for (Telefono telefono : direccion.getTelefonos()) {
							telefono.getEmpresaOperadora().setCodigoEntero(0);
							idTelefono = telefonoDao.registrarTelefono(
									telefono, conexion);
							if (idTelefono == 0) {
								throw new ResultadoCeroDaoException(
										"No se pudo completar el registro del telefono");
							}
							telefonoDao.registrarTelefonoDireccion(idTelefono,
									idDireccion, conexion);
						}
					}

					direccionDao.registrarPersonaDireccion(
							cliente.getCodigoEntero(),
							cliente.getTipoPersona(), idDireccion, conexion);
				}
			}

			if (cliente.getListaContactos() != null) {
				int idContacto = 0;
				for (Contacto contacto : cliente.getListaContactos()) {
					contacto.setTipoPersona(3);
					idContacto = personaDao
							.registrarPersona(contacto, conexion);
					contacto.setCodigoEntero(idContacto);
					if (!contacto.getListaTelefonos().isEmpty()) {
						for (Telefono telefono : contacto.getListaTelefonos()) {
							int idTelefono = telefonoDao.registrarTelefono(
									telefono, conexion);
							if (idTelefono == 0) {
								throw new ResultadoCeroDaoException(
										"No se pudo completar el registro del telefono");
							}
							telefonoDao.registrarTelefonoPersona(idTelefono,
									idContacto, conexion);
						}
					}
					contactoDao.registrarContactoProveedor(idPersona, contacto,
							conexion);

					contactoDao.ingresarCorreoElectronico(contacto, conexion);
				}
			}
			ClienteDao clienteDao = new ClienteDaoImpl();
			clienteDao.registroCliente(cliente, conexion);

			return true;
		} catch (ResultadoCeroDaoException e) {
			throw new ResultadoCeroDaoException(e.getMensajeError(), e);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			if (conexion != null) {
				conexion.close();
			}
		}
	}

	@Override
	public boolean actualizarCliente(Cliente cliente) throws SQLException,
			ResultadoCeroDaoException, Exception {
		PersonaDao personaDao = new PersonaDaoImpl();
		DireccionDao direccionDao = new DireccionDaoImpl();
		TelefonoDao telefonoDao = new TelefonoDaoImpl();
		ContactoDao contactoDao = new ContactoDaoImpl();

		Connection conexion = null;
		try {
			conexion = UtilConexion.obtenerConexion();

			cliente.setTipoPersona(1);

			Integer idPersona = personaDao.actualizarPersona(cliente, conexion);
			if (idPersona == 0) {
				throw new ResultadoCeroDaoException(
						"No se pudo completar la actualización de la persona");
			}
			direccionDao.eliminarDireccionPersona(cliente, conexion);
			if (cliente.getListaDirecciones() != null) {
				int idDireccion = 0;
				for (Direccion direccion : cliente.getListaDirecciones()) {
					idDireccion = direccionDao.actualizarDireccion(direccion,
							conexion);
					int idTelefono = 0;
					if (idDireccion == 0) {
						throw new ResultadoCeroDaoException(
								"No se pudo completar la actualización de la dirección");
					} else {
						direccionDao.eliminarTelefonoDireccion(direccion,
								conexion);
						List<Telefono> listTelefonos = direccion.getTelefonos();
						if (!listTelefonos.isEmpty()) {
							for (Telefono telefono : listTelefonos) {
								telefono.getEmpresaOperadora().setCodigoEntero(
										0);
								idTelefono = telefonoDao.registrarTelefono(
										telefono, conexion);
								if (idTelefono == 0) {
									throw new ResultadoCeroDaoException(
											"No se pudo completar el registro del teléfono de direccion");
								}
								telefonoDao.registrarTelefonoDireccion(
										idTelefono, idDireccion, conexion);
							}
						}
					}

					direccionDao.registrarPersonaDireccion(
							cliente.getCodigoEntero(),
							cliente.getTipoPersona(), idDireccion, conexion);
				}
			}

			contactoDao.eliminarContactoProveedor(cliente, conexion);
			if (cliente.getListaContactos() != null) {
				int idContacto = 0;
				for (Contacto contacto : cliente.getListaContactos()) {
					contacto.setTipoPersona(3);

					idContacto = personaDao
							.registrarPersona(contacto, conexion);
					contacto.setCodigoEntero(idContacto);

					if (!contacto.getListaTelefonos().isEmpty()) {
						for (Telefono telefono : contacto.getListaTelefonos()) {
							int idTelefono = telefonoDao.registrarTelefono(
									telefono, conexion);
							if (idTelefono == 0) {
								throw new ResultadoCeroDaoException(
										"No se pudo completar el registro del teléfono de contacto");
							}
							telefonoDao.registrarTelefonoPersona(idTelefono,
									idContacto, conexion);
						}
					}
					contactoDao.registrarContactoProveedor(idPersona, contacto,
							conexion);

					contactoDao.eliminarCorreosContacto(contacto, conexion);

					contactoDao.ingresarCorreoElectronico(contacto, conexion);
				}
			}
			ClienteDao clienteDao = new ClienteDaoImpl();
			clienteDao.actualizarPersonaAdicional(cliente, conexion);

			return true;
		} catch (ResultadoCeroDaoException e) {
			throw new ResultadoCeroDaoException(e.getMensajeError(), e);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			if (conexion != null) {
				conexion.close();
			}
		}
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
			direccion.setDireccion(obtenerDireccionCompleta(direccion));
		}
		cliente.setListaDirecciones(listaDirecciones);
		cliente.setListaContactos(contactoDao
				.consultarContactoProveedor(idcliente));

		return cliente;
	}

	@Override
	public Integer registrarNovios(ProgramaNovios programaNovios)
			throws ErrorRegistroDataException, SQLException, Exception {
		ServicioNoviosDao servicioNoviosDao = new ServicioNoviosDaoImpl();
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();
		DestinoDao destinoDao = new DestinoDaoImpl();

		Connection conexion = null;
		try {
			conexion = UtilConexion.obtenerConexion();

			int idServicio = 0;

			ServicioAgencia servicioAgencia = new ServicioAgencia();
			servicioAgencia.setCliente(programaNovios.getNovia());
			servicioAgencia.setCliente2(programaNovios.getNovio());
			servicioAgencia.setFechaServicio(new Date());
			servicioAgencia.setCantidadServicios(0);
			if (programaNovios.getListaServicios() != null
					&& !programaNovios.getListaServicios().isEmpty()) {
				servicioAgencia.setCantidadServicios(programaNovios
						.getListaServicios().size());
			}

			servicioAgencia.setDestino(destinoDao.consultarDestino(
					programaNovios.getDestino().getCodigoEntero(), conexion));
			servicioAgencia.getFormaPago().setCodigoEntero(1);
			servicioAgencia.getEstadoPago().setCodigoEntero(1);
			servicioAgencia.setVendedor(programaNovios.getVendedor());
			servicioAgencia.setMontoTotalComision(programaNovios
					.getMontoTotalComision());
			servicioAgencia.setMontoTotalServicios(programaNovios
					.getMontoTotalServiciosPrograma());
			servicioAgencia.setMontoTotalFee(programaNovios.getMontoTotalFee());
			servicioAgencia.setMontoTotalIGV(programaNovios.getMontoTotalIGV());
			servicioAgencia.setUsuarioCreacion(programaNovios
					.getUsuarioCreacion());
			servicioAgencia.setIpCreacion(programaNovios.getIpCreacion());
			servicioAgencia.setUsuarioModificacion(programaNovios
					.getUsuarioModificacion());
			servicioAgencia.setIpModificacion(programaNovios
					.getIpModificacion());
			servicioAgencia.getEstadoServicio().setCodigoEntero(1);

			idServicio = servicioNovaViajesDao.ingresarCabeceraServicio(
					servicioAgencia, conexion);
			if (idServicio == 0) {
				throw new ErrorRegistroDataException(
						"No se pudo registrar los servicios de los novios");
			}
			if (programaNovios.getListaServicios() != null
					&& !programaNovios.getListaServicios().isEmpty()) {
				for (DetalleServicioAgencia detalleServicio : programaNovios
						.getListaServicios()) {
					Integer resultado = servicioNovaViajesDao
							.ingresarDetalleServicio(detalleServicio,
									idServicio, conexion);
					if (resultado == null || resultado.intValue() == 0) {
						throw new ErrorRegistroDataException(
								"No se pudo registrar los servicios v");
					} else {
						for (DetalleServicioAgencia detalleServicio2 : detalleServicio
								.getServiciosHijos()) {
							detalleServicio2.getServicioPadre()
									.setCodigoEntero(resultado);
							resultado = servicioNovaViajesDao
									.ingresarDetalleServicio(detalleServicio2,
											idServicio, conexion);
							if (resultado == null || resultado.intValue() == 0) {
								throw new ErrorRegistroDataException(
										"No se pudo registrar los servicios de los novios");
							}
						}
					}
				}
			} else {
				throw new ErrorRegistroDataException(
						"No se enviaron los servicios de los novios");
			}

			programaNovios.setIdServicio(idServicio);

			Integer idnovios = servicioNoviosDao.registrarNovios(
					programaNovios, conexion);

			if (programaNovios.getListaInvitados() != null
					&& !programaNovios.getListaInvitados().isEmpty()) {
				for (Cliente invitado : programaNovios.getListaInvitados()) {
					boolean exitoRegistro = servicioNoviosDao
							.registrarInvitado(invitado, idnovios, conexion);
					if (!exitoRegistro) {
						throw new ErrorRegistroDataException(
								"No se pudo registrar los invitados de los novios");
					}
				}
			}

			return idnovios;
		} catch (ErrorRegistroDataException e) {
			throw new ErrorRegistroDataException(e.getMensajeError(), e);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			if (conexion != null) {
				conexion.close();
			}
		}
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
	public ServicioNovios agregarServicio(ServicioNovios detalleServicio)
			throws SQLException, Exception {

		Connection conn = null;

		try {
			conn = UtilConexion.obtenerConexion();

			MaestroServicioDao maestroServicioDao = new MaestroServicioDaoImpl();

			ProveedorDao proveedorDao = new ProveedorDaoImpl();

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

			if (detalleServicio.getPrecioUnitario() != null) {
				BigDecimal total = detalleServicio.getPrecioUnitario()
						.multiply(
								UtilParse.parseIntABigDecimal(detalleServicio
										.getCantidad()));
				totalVenta = totalVenta.add(total);
			}

			if (detalleServicio.getServicioProveedor().getPorcentajeComision() != null) {
				comision = detalleServicio.getServicioProveedor()
						.getPorcentajeComision().multiply(totalVenta);
				comision = comision.divide(BigDecimal.valueOf(100.0));
			}

			if (detalleServicio.getServicioProveedor().getProveedor()
					.getCodigoEntero() != null) {
				Proveedor proveedor = consultarProveedor(detalleServicio
						.getServicioProveedor().getProveedor()
						.getCodigoEntero().intValue());
				detalleServicio.getServicioProveedor().setProveedor(proveedor);
			}

			detalleServicio.setMontoComision(comision);

			detalleServicio.setCodigoCadena(String.valueOf(System
					.currentTimeMillis()));

			return detalleServicio;
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
	public BigDecimal calcularValorCuota(ServicioAgencia servicioAgencia)
			throws SQLException, Exception {
		BigDecimal valorCuota = BigDecimal.ZERO;

		ServicioNegocioDao servicioNegocioDao = new ServicioNegocioDaoImpl();

		valorCuota = servicioNegocioDao.calcularCuota(servicioAgencia);

		return valorCuota;
	}

	@Override
	public Integer registrarVentaServicio(ServicioAgencia servicioAgencia)
			throws ErrorRegistroDataException, SQLException, Exception {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();

		Connection conexion = null;
		Integer idServicio = 0;

		try {
			conexion = UtilConexion.obtenerConexion();

			if (servicioAgencia.getFechaServicio() == null) {
				Date fechaSer = servicioAgencia.getListaDetalleServicio()
						.get(0).getFechaIda();
				servicioAgencia.setFechaServicio(fechaSer);
			}

			if (servicioAgencia.getValorCuota() == null
					&& servicioAgencia.getFormaPago().getCodigoEntero()
							.intValue() == 2) {
				ServicioNegocioDao servicioNegocioDao = new ServicioNegocioDaoImpl();
				servicioAgencia.setValorCuota(servicioNegocioDao
						.calcularCuota(servicioAgencia));
			}

			if (!servicioAgencia.getListaDetalleServicio().isEmpty()) {
				servicioAgencia.setCantidadServicios(servicioAgencia
						.getListaDetalleServicio().size());
			}

			servicioAgencia.getEstadoServicio().setCodigoEntero(
					ServicioAgencia.ESTADO_CERRADO);
			idServicio = servicioNovaViajesDao.ingresarCabeceraServicio(
					servicioAgencia, conexion);

			servicioAgencia.setCodigoEntero(idServicio);

			if (idServicio == 0) {
				throw new ErrorRegistroDataException(
						"No se pudo registrar los servicios de los novios");
			}
			if (servicioAgencia.getListaDetalleServicio() != null
					&& !servicioAgencia.getListaDetalleServicio().isEmpty()) {
				
				for (DetalleServicioAgencia detalleServicio : servicioAgencia
						.getListaDetalleServicio()) {
					if (detalleServicio.getTipoServicio().isServicioPadre()) {
						Integer idRuta = null;
						for (Tramo tramo : detalleServicio.getRuta().getTramos()){
							tramo = servicioNovaViajesDao.registrarTramo(tramo, conexion);
							if (tramo.getCodigoEntero()== null || tramo.getCodigoEntero().intValue()==0){
								throw new ErrorRegistroDataException(
										"No se pudo registrar los servicios de la venta");
							}
							detalleServicio.getRuta().setTramo(tramo);
							if (idRuta == null){
								idRuta = servicioNovaViajesDao.obtenerSiguienteRuta(conexion);
							}
							detalleServicio.getRuta().setCodigoEntero(idRuta);
							if (!servicioNovaViajesDao.registrarRuta(detalleServicio.getRuta(), conexion)){
								throw new ErrorRegistroDataException(
										"No se pudo registrar los servicios de la venta");
							}
						}
						
						Integer idSerDetaPadre = servicioNovaViajesDao
								.ingresarDetalleServicio(detalleServicio,
										idServicio, conexion);
						if (idSerDetaPadre == null
								|| idSerDetaPadre.intValue() == 0) {
							throw new ErrorRegistroDataException(
									"No se pudo registrar los servicios de la venta");
						} else {
							for (DetalleServicioAgencia detalleServicio2 : servicioAgencia
									.getListaDetalleServicio()) {
								if (!detalleServicio2.getTipoServicio()
										.isServicioPadre()) {
									if (detalleServicio2.getServicioPadre()
											.getCodigoEntero().intValue() == detalleServicio
											.getCodigoEntero().intValue()) {
										detalleServicio2
												.getServicioPadre()
												.setCodigoEntero(idSerDetaPadre);
										Integer resultado = servicioNovaViajesDao
												.ingresarDetalleServicio(
														detalleServicio2,
														idServicio, conexion);
										if (resultado == null
												|| resultado.intValue() == 0) {
											throw new ErrorRegistroDataException(
													"No se pudo registrar los servicios de la venta");
										}
									}
								}
							}
						}
					}
				}
			} else {
				throw new ErrorRegistroDataException(
						"No se agregaron servicios a la venta");
			}

			if (servicioAgencia.getFormaPago().getCodigoEntero().intValue() == 2) {
				boolean resultado = servicioNovaViajesDao
						.generarCronogramaPago(servicioAgencia, conexion);
				if (!resultado) {
					throw new ErrorRegistroDataException(
							"No se pudo generar el cronograma de pagos");
				}
			}

			servicioNovaViajesDao.registrarSaldosServicio(servicioAgencia,
					conexion);

			return idServicio;
		} catch (ErrorRegistroDataException e) {
			throw new ErrorRegistroDataException(e.getMensajeError(), e);
		} finally {
			if (conexion != null) {
				conexion.close();
			}
		}
	}

	@Override
	public Integer actualizarVentaServicio(ServicioAgencia servicioAgencia)
			throws ErrorRegistroDataException, SQLException, Exception {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();

		Connection conexion = null;
		Integer idServicio = 0;

		try {
			conexion = UtilConexion.obtenerConexion();

			if (servicioAgencia.getFechaServicio() == null) {
				Date fechaSer = servicioAgencia.getListaDetalleServicio()
						.get(0).getFechaIda();
				servicioAgencia.setFechaServicio(fechaSer);
			}

			if (servicioAgencia.getValorCuota() == null
					&& servicioAgencia.getFormaPago().getCodigoEntero()
							.intValue() == 2) {
				ServicioNegocioDao servicioNegocioDao = new ServicioNegocioDaoImpl();
				servicioAgencia.setValorCuota(servicioNegocioDao
						.calcularCuota(servicioAgencia));
			}

			if (!servicioAgencia.getListaDetalleServicio().isEmpty()) {
				servicioAgencia.setCantidadServicios(servicioAgencia
						.getListaDetalleServicio().size());
			}

			servicioAgencia.getEstadoServicio().setCodigoEntero(2);
			idServicio = servicioNovaViajesDao.actualizarCabeceraServicio(
					servicioAgencia, conexion);

			servicioAgencia.setCodigoEntero(idServicio);

			if (idServicio == 0) {
				throw new ErrorRegistroDataException(
						"No se pudo registrar los servicios de los novios");
			}
			if (servicioAgencia.getListaDetalleServicio() != null
					&& !servicioAgencia.getListaDetalleServicio().isEmpty()) {

				servicioNovaViajesDao.eliminarDetalleServicio(servicioAgencia,
						conexion);

				for (DetalleServicioAgencia detalleServicio : servicioAgencia
						.getListaDetalleServicio()) {
					Integer resultado = servicioNovaViajesDao
							.ingresarDetalleServicio(detalleServicio,
									idServicio, conexion);
					if (resultado == null || resultado.intValue() == 0) {
						throw new ErrorRegistroDataException(
								"No se pudo registrar los servicios de la venta");
					} else {
						for (DetalleServicioAgencia detalleServicio2 : detalleServicio
								.getServiciosHijos()) {
							detalleServicio2.getServicioPadre()
									.setCodigoEntero(resultado);
							resultado = servicioNovaViajesDao
									.ingresarDetalleServicio(detalleServicio2,
											idServicio, conexion);
							if (resultado == null || resultado.intValue() == 0) {
								throw new ErrorRegistroDataException(
										"No se pudo registrar los servicios de la venta");
							}
						}
					}
				}
			}

			if (servicioAgencia.getFormaPago().getCodigoEntero().intValue() == 2) {
				servicioNovaViajesDao.eliminarCronogramaServicio(
						servicioAgencia, conexion);
				boolean resultado = servicioNovaViajesDao
						.generarCronogramaPago(servicioAgencia, conexion);
				if (!resultado) {
					throw new ErrorRegistroDataException(
							"No se pudo generar el cronograma de pagos");
				}
			}

			return idServicio;
		} catch (ErrorRegistroDataException e) {
			throw new ErrorRegistroDataException(e.getMensajeError(), e);
		} finally {
			if (conexion != null) {
				conexion.close();
			}
		}
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

			if (servicioAgencia.getFormaPago().getCodigoEntero() != null && servicioAgencia.getFormaPago().getCodigoEntero().intValue() == 2) {
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

	public int enviarCorreoMasivo(CorreoMasivo correoMasivo)
			throws EnvioCorreoException, Exception {
		int respuesta = 0;
		try {
			UtilCorreo utilCorreo = new UtilCorreo();
			for (CorreoClienteMasivo envio : correoMasivo
					.getListaCorreoMasivo()) {
				try {
					if (envio.isEnviarCorreo()
							&& UtilEjb.correoValido(envio
									.getCorreoElectronico().getDireccion())) {
						if (!correoMasivo.isArchivoCargado()) {
							utilCorreo.enviarCorreo(envio
									.getCorreoElectronico().getDireccion(),
									correoMasivo.getAsunto(), correoMasivo
											.getContenidoCorreo());
						} else {
							utilCorreo.enviarCorreo(envio
									.getCorreoElectronico().getDireccion(),
									correoMasivo.getAsunto(), correoMasivo
											.getContenidoCorreo(), correoMasivo
											.getArchivoAdjunto());
						}
					}
				} catch (AddressException e) {
					respuesta = 1;
					System.out.println("ERROR EN ENVIO DE CORREO ::"
							+ envio.getCorreoElectronico().getDireccion());
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					respuesta = 2;
					System.out.println("ERROR EN ENVIO DE CORREO ::"
							+ envio.getCorreoElectronico().getDireccion());
					e.printStackTrace();
				} catch (NoSuchProviderException e) {
					respuesta = 3;
					System.out.println("ERROR EN ENVIO DE CORREO ::"
							+ envio.getCorreoElectronico().getDireccion());
					e.printStackTrace();
				} catch (IOException e) {
					respuesta = 4;
					System.out.println("ERROR EN ENVIO DE CORREO ::"
							+ envio.getCorreoElectronico().getDireccion());
					e.printStackTrace();
				} catch (MessagingException e) {
					respuesta = 5;
					System.out.println("ERROR EN ENVIO DE CORREO ::"
							+ envio.getCorreoElectronico().getDireccion());
					e.printStackTrace();
				}
			}
			System.out.println("respuesta de envio de correo ::" + respuesta);

			return respuesta;
		} catch (Exception e) {
			e.printStackTrace();
			throw new EnvioCorreoException("0001",
					"Error en envio de correo masivo", e.getMessage(), e);
		}

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
							this.obtenerDireccionCompleta(cliente2
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
	public boolean ingresarMaestroServicio(MaestroServicio servicio)
			throws ErrorRegistroDataException, SQLException, Exception {
		MaestroServicioDao maestroServicioDao = new MaestroServicioDaoImpl();

		Integer idMaestroServicio = maestroServicioDao
				.ingresarMaestroServicio(servicio);

		if (idMaestroServicio == null || idMaestroServicio.intValue() == 0) {
			throw new ErrorRegistroDataException(
					"No se pudo completar el registro de servicio");
		}

		return true;
	}

	@Override
	public boolean actualizarMaestroServicio(MaestroServicio servicio)
			throws SQLException, Exception {
		MaestroServicioDao maestroServicioDao = new MaestroServicioDaoImpl();

		if (!maestroServicioDao.actualizarMaestroServicio(servicio)) {
			throw new ErrorRegistroDataException(
					"No se pudo completar la actualizacion de servicio");
		}

		if (servicio.getListaServicioDepende() != null
				&& !servicio.getListaServicioDepende().isEmpty()) {
			maestroServicioDao.ingresarServicioMaestroServicio(
					servicio.getCodigoEntero(),
					servicio.getListaServicioDepende());
		}
		return true;
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
	public Integer actualizarNovios(ProgramaNovios programaNovios)
			throws SQLException, Exception {
		ServicioNoviosDao servicioNoviosDao = new ServicioNoviosDaoImpl();
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();
		DestinoDao destinoDao = new DestinoDaoImpl();

		Connection conexion = null;
		try {
			conexion = UtilConexion.obtenerConexion();

			int idServicio = 0;

			ServicioAgencia servicioAgencia = new ServicioAgencia();
			servicioAgencia.setCodigoEntero(programaNovios.getIdServicio());
			servicioAgencia.setCliente(programaNovios.getNovia());
			servicioAgencia.setCliente2(programaNovios.getNovio());
			servicioAgencia.setFechaServicio(new Date());
			servicioAgencia.setCantidadServicios(0);
			if (programaNovios.getListaServicios() != null
					&& !programaNovios.getListaServicios().isEmpty()) {
				servicioAgencia.setCantidadServicios(programaNovios
						.getListaServicios().size());
			}

			servicioAgencia.setDestino(destinoDao.consultarDestino(
					programaNovios.getDestino().getCodigoEntero(), conexion));
			servicioAgencia.getFormaPago().setCodigoEntero(1);
			servicioAgencia.getEstadoPago().setCodigoEntero(1);
			servicioAgencia.setVendedor(programaNovios.getVendedor());
			servicioAgencia.setMontoTotalComision(programaNovios
					.getMontoTotalComision());
			servicioAgencia.setMontoTotalServicios(programaNovios
					.getMontoTotalServiciosPrograma());
			servicioAgencia.setMontoTotalFee(programaNovios.getMontoTotalFee());
			servicioAgencia.setUsuarioCreacion(programaNovios
					.getUsuarioCreacion());
			servicioAgencia.setIpCreacion(programaNovios.getIpCreacion());
			servicioAgencia.setUsuarioModificacion(programaNovios
					.getUsuarioModificacion());
			servicioAgencia.setIpModificacion(programaNovios
					.getIpModificacion());
			servicioAgencia.getEstadoServicio().setCodigoEntero(1);
			servicioAgencia.setListaDetalleServicio(programaNovios
					.getListaServicios());

			idServicio = servicioNovaViajesDao.actualizarCabeceraServicio(
					servicioAgencia, conexion);
			if (idServicio == 0) {
				throw new ErrorRegistroDataException(
						"No se pudo actualizar los servicios de los novios");
			}
			if (programaNovios.getListaServicios() != null
					&& !programaNovios.getListaServicios().isEmpty()) {
				servicioNovaViajesDao.eliminarDetalleServicio(servicioAgencia,
						conexion);
				for (DetalleServicioAgencia servicioNovios : programaNovios
						.getListaServicios()) {
					Integer exitoRegistro = servicioNovaViajesDao
							.ingresarDetalleServicio(servicioNovios,
									idServicio, conexion);
					;
					if (exitoRegistro == null || exitoRegistro.intValue() == 0) {
						throw new ErrorRegistroDataException(
								"No se pudo actualizar los servicios de los novios");
					}
				}
			} else {
				throw new ErrorRegistroDataException(
						"No se enviaron los servicios de los novios");
			}
			programaNovios.setIdServicio(idServicio);

			Integer idnovios = servicioNoviosDao.actualizarNovios(
					programaNovios, conexion);

			if (!servicioNoviosDao.eliminarInvitadosNovios(programaNovios,
					conexion)) {
				throw new ErrorRegistroDataException(
						"No se pudo eliminar los invitados de los novios");
			}

			if (programaNovios.getListaInvitados() != null
					&& !programaNovios.getListaInvitados().isEmpty()) {
				for (Cliente invitado : programaNovios.getListaInvitados()) {
					boolean exitoRegistro = servicioNoviosDao
							.registrarInvitado(invitado, idnovios, conexion);
					if (!exitoRegistro) {
						throw new ErrorRegistroDataException(
								"No se pudo registrar los invitados de los novios");
					}
				}
			}
			return idnovios;
		} catch (ErrorRegistroDataException e) {
			throw new ErrorRegistroDataException(e.getMensajeError(), e);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			if (conexion != null) {
				conexion.close();
			}
		}
	}

	@Override
	public List<CorreoClienteMasivo> listarClientesCorreo()
			throws SQLException, Exception {
		CorreoMasivoDao correoMasivoDao = new CorreoMasivoDaoImpl();

		return correoMasivoDao.listarClientesCorreo();
	}

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
	public boolean ingresarConsolidador(Consolidador consolidador)
			throws SQLException, Exception {
		ConsolidadorDao consolidadorDao = new ConsolidadorDaoImpl();

		return consolidadorDao.ingresarConsolidador(consolidador);
	}

	@Override
	public boolean actualizarConsolidador(Consolidador consolidador)
			throws SQLException, Exception {
		ConsolidadorDao consolidadorDao = new ConsolidadorDaoImpl();

		return consolidadorDao.actualizarConsolidador(consolidador);
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
	public void registrarPago(PagoServicio pago) throws SQLException, Exception {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();

		servicioNovaViajesDao.registrarPagoServicio(pago);
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
	public void cerrarVenta(ServicioAgencia servicioAgencia)
			throws SQLException, Exception {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();

		servicioAgencia.getEstadoServicio().setCodigoEntero(
				ServicioAgencia.ESTADO_CERRADO);

		servicioNovaViajesDao.actualizarServicioVenta(servicioAgencia);
	}

	@Override
	public void anularVenta(ServicioAgencia servicioAgencia)
			throws SQLException, Exception {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();

		servicioAgencia.getEstadoServicio().setCodigoEntero(
				ServicioAgencia.ESTADO_ANULADO);

		servicioNovaViajesDao.actualizarServicioVenta(servicioAgencia);
	}

	@Override
	public void registrarEventoObservacion(EventoObsAnu evento)
			throws SQLException, Exception {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();

		evento.getTipoEvento().setCodigoEntero(EventoObsAnu.EVENTO_OBS);

		servicioNovaViajesDao.registrarEventoObsAnu(evento);
	}

	@Override
	public void registrarEventoAnulacion(EventoObsAnu evento)
			throws SQLException, Exception {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();

		evento.getTipoEvento().setCodigoEntero(EventoObsAnu.EVENTO_ANU);

		servicioNovaViajesDao.registrarEventoObsAnu(evento);
	}

	@Override
	public boolean registrarComprobantes(ServicioAgencia servicioAgencia)
			throws ValidacionException, SQLException, Exception {
		try {
			List<Comprobante> listaComprobantes = UtilEjb
					.obtenerNumeroComprobante(servicioAgencia
							.getListaDetalleServicioAgrupado());
			for (Comprobante comprobante : listaComprobantes) {
				for (DetalleServicioAgencia detallePadre : servicioAgencia
						.getListaDetalleServicioAgrupado()) {
					for (DetalleServicioAgencia detalle : detallePadre
							.getServiciosHijos()) {
						if (comprobante.getTipoComprobante().getCodigoEntero()
								.intValue() != detalle.getTipoComprobante()
								.getCodigoEntero().intValue()
								&& comprobante.getNumeroComprobante().equals(
										detalle.getNroComprobante())) {
							throw new ValidacionException(
									"Tipo y numero de comprobante diferente");
						}
					}
				}
			}

			List<Comprobante> listaComprobantes2 = new ArrayList<Comprobante>();
			for (Comprobante comprobante : listaComprobantes) {
				comprobante = UtilEjb.obtenerNumeroComprobante(comprobante,
						servicioAgencia);
				listaComprobantes2.add(comprobante);
			}
			ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();
			Connection conn = null;

			try {
				conn = UtilConexion.obtenerConexion();
				for (Comprobante comprobante : listaComprobantes2) {
					Integer idComprobante = servicioNovaViajesDao
							.registrarComprobante(comprobante, conn);
					servicioNovaViajesDao.registrarDetalleComprobante(
							comprobante.getDetalleComprobante(), idComprobante,
							conn);
				}

				servicioNovaViajesDao.actualizarComprobantesServicio(true,
						servicioAgencia, conn);
			} catch (SQLException e) {
				throw new ValidacionException(e);
			} catch (Exception e) {
				throw new ValidacionException("Excepcion no controlada", e);
			} finally {
				if (conn != null) {
					conn.close();
				}
			}

			return true;
		} catch (ValidacionException e) {
			throw new ValidacionException(e.getMessage(), e);
		} catch (Exception e) {
			throw new ValidacionException("Excepcion no controlada", e);
		}
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
			Integer idServicio) throws ErrorConsultaDataException, SQLException, Exception {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();
		List<DetalleServicioAgencia> lista = null;
		Connection conn = null;
		
		try {
			conn = UtilConexion.obtenerConexion();
			lista = servicioNovaViajesDao
					.consultaServDetComprobanteObligacion(idServicio, conn);
			for (DetalleServicioAgencia detalleServicioAgencia : lista) {
				detalleServicioAgencia.getServiciosHijos().add(detalleServicioAgencia);
				detalleServicioAgencia.getServiciosHijos().addAll(servicioNovaViajesDao.consultaServDetComprobanteObligacionHijo(idServicio, detalleServicioAgencia.getCodigoEntero(), conn));
			}
			
			return lista;
		} catch (SQLException e) {
			throw new ErrorConsultaDataException(e);
		} catch (Exception e) {
			throw new ErrorConsultaDataException(e);
		} finally{
			if (conn != null){
				conn.close();
			}
		}
	}

	@Override
	public boolean registrarObligacionXPagar(Comprobante comprobante)
			throws SQLException, Exception {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();
		return servicioNovaViajesDao.registrarObligacionXPagar(comprobante);
	}

	@Override
	public List<Comprobante> listarObligacionXPagar(Comprobante comprobante)
			throws SQLException, Exception {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();
		return servicioNovaViajesDao.consultaObligacionXPagar(comprobante);
	}

	@Override
	public void registrarPagoObligacion(PagoServicio pago) throws SQLException,
			Exception {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();

		servicioNovaViajesDao.registrarPagoObligacion(pago);
	}

	@Override
	public void registrarRelacionComproObligacion(
			ServicioAgencia servicioAgencia) throws SQLException, Exception {
		Connection conn = null;
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();

		try {
			conn = UtilConexion.obtenerConexion();
			for (DetalleServicioAgencia detalleServicioAgencia : servicioAgencia
					.getListaDetalleServicioAgrupado()) {
				for(DetalleServicioAgencia detalleHijo : detalleServicioAgencia.getServiciosHijos()){
					if (detalleHijo.getIdComprobanteGenerado() != null
							&& detalleHijo.getComprobanteAsociado()
									.getCodigoEntero() != null
							&& detalleHijo.getCodigoEntero() != null
							&& detalleHijo.getServicioPadre()
									.getCodigoEntero() != null) {
						
						if (detalleHijo.isAgrupado()){
							for (Integer id : detalleHijo.getCodigoEnteroAgrupados()){
								detalleHijo.setCodigoEntero(id);
								servicioNovaViajesDao.guardarRelacionComproObligacion(
										detalleHijo, conn);
							}
						}
						else{
							servicioNovaViajesDao.guardarRelacionComproObligacion(
									detalleHijo, conn);
						}
						
					}
				}
			}
			servicioNovaViajesDao.actualizarRelacionComprobantes(true,
					servicioAgencia, conn);
		} catch (SQLException e) {
			throw new SQLException(e);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	@Override
	public boolean grabarDocumentosAdicionales(
			List<DocumentoAdicional> listaDocumentos)
			throws ErrorRegistroDataException, SQLException, Exception {

		Connection conn = null;
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();
		try {
			conn = UtilConexion.obtenerConexion();

			if (listaDocumentos != null && !listaDocumentos.isEmpty()) {
				DocumentoAdicional documento = listaDocumentos.get(0);
				if (documento.getIdServicio() != null
						&& !servicioNovaViajesDao.eliminarDocumentoAdicional(
								documento, conn)) {
					throw new ErrorRegistroDataException(
							"No se pudo eliminar los documentos adicionales");
				}

				for (DocumentoAdicional documentoAdicional : listaDocumentos) {
					if (documentoAdicional.getCodigoEntero() == null
							|| documentoAdicional.getCodigoEntero().intValue() == 0) {
						boolean resultado = servicioNovaViajesDao
								.grabarDocumentoAdicional(documentoAdicional,
										conn);
						if (!resultado) {
							throw new ErrorRegistroDataException(
									"No se pudo completar el registro de documentos adicionales");
						}
					}
				}

				return true;
			}

		} catch (ErrorRegistroDataException e) {
			throw new ErrorRegistroDataException(e);
		} catch (SQLException e) {
			throw new SQLException(e);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return false;
	}

	@Override
	public List<DocumentoAdicional> listarDocumentosAdicionales(
			Integer idServicio) throws SQLException {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();
		return servicioNovaViajesDao.listarDocumentosAdicionales(idServicio);
	}

	@Override
	public void registrarComprobantesAdicionales(
			List<Comprobante> listaComprobantesAdicionales)
			throws ErrorRegistroDataException, SQLException, Exception {

		Connection conn = null;

		try {
			conn = UtilConexion.obtenerConexion();

			ComprobanteNovaViajesDao comprobanteNovaViajesDao = new ComprobanteNovaViajesDaoImpl();

			for (Comprobante comprobante : listaComprobantesAdicionales) {
				comprobanteNovaViajesDao.registrarComprobanteAdicional(
						comprobante, conn);
			}

		} catch (SQLException e) {
			throw new ErrorRegistroDataException(e);
		} catch (Exception e) {
			throw new ErrorRegistroDataException(e);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	@Override
	public List<Comprobante> consultarComprobantesGenerados(ComprobanteBusqueda comprobanteBusqueda) throws ErrorConsultaDataException{
		try {
			ComprobanteNovaViajesDao comprobanteNovaViajesDao = new ComprobanteNovaViajesDaoImpl();
			return comprobanteNovaViajesDao.consultarComprobantes(comprobanteBusqueda);
		} catch (SQLException e) {
			throw new ErrorConsultaDataException(e);
		} catch (Exception e){
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
			
			List<Comprobante> comprobantes = comprobanteNovaViajesDao.consultarComprobantes(comprobanteBusqueda);
			
			Comprobante comprobante = comprobantes.get(0);
			comprobante.setDetalleComprobante(comprobanteNovaViajesDao.consultarDetalleComprobante(idComprobante));
			
			return comprobante;
		} catch (SQLException e) {
			throw new ErrorConsultaDataException(e);
		} catch (Exception e){
			throw new ErrorConsultaDataException(e);
		}
	}
	
	@Override
	public boolean grabarComprobantesReporte(ReporteArchivo reporteArchivo, ColumnasExcel columnasExcel, List<ColumnasExcel> dataExcel) throws ErrorRegistroDataException, SQLException{
		ArchivoReporteDao archivoReporteDao = new ArchivoReporteDaoImpl();
		Connection conn = null;
		
		try {
			conn = UtilConexion.obtenerConexion();
			int idArchivo = archivoReporteDao.registrarArchivoReporteCabecera(reporteArchivo, conn);
			columnasExcel.setIdArchivo(idArchivo);
			columnasExcel.setUsuarioCreacion(reporteArchivo.getUsuarioCreacion());
			columnasExcel.setIpCreacion(reporteArchivo.getIpCreacion());
			columnasExcel.getColumna1().setValorCadena(columnasExcel.getColumna1().getNombreColumna());
			columnasExcel.getColumna2().setValorCadena(columnasExcel.getColumna2().getNombreColumna());
			columnasExcel.getColumna3().setValorCadena(columnasExcel.getColumna3().getNombreColumna());
			columnasExcel.getColumna4().setValorCadena(columnasExcel.getColumna4().getNombreColumna());
			columnasExcel.getColumna5().setValorCadena(columnasExcel.getColumna5().getNombreColumna());
			columnasExcel.getColumna6().setValorCadena(columnasExcel.getColumna6().getNombreColumna());
			columnasExcel.getColumna7().setValorCadena(columnasExcel.getColumna7().getNombreColumna());
			columnasExcel.getColumna8().setValorCadena(columnasExcel.getColumna8().getNombreColumna());
			columnasExcel.getColumna9().setValorCadena(columnasExcel.getColumna9().getNombreColumna());
			columnasExcel.getColumna10().setValorCadena(columnasExcel.getColumna10().getNombreColumna());
			columnasExcel.getColumna11().setValorCadena(columnasExcel.getColumna11().getNombreColumna());
			columnasExcel.getColumna12().setValorCadena(columnasExcel.getColumna12().getNombreColumna());
			columnasExcel.getColumna13().setValorCadena(columnasExcel.getColumna13().getNombreColumna());
			columnasExcel.getColumna14().setValorCadena(columnasExcel.getColumna14().getNombreColumna());
			columnasExcel.getColumna15().setValorCadena(columnasExcel.getColumna15().getNombreColumna());
			columnasExcel.getColumna16().setValorCadena(columnasExcel.getColumna16().getNombreColumna());
			columnasExcel.getColumna17().setValorCadena(columnasExcel.getColumna17().getNombreColumna());
			columnasExcel.getColumna18().setValorCadena(columnasExcel.getColumna18().getNombreColumna());
			columnasExcel.getColumna19().setValorCadena(columnasExcel.getColumna19().getNombreColumna());
			columnasExcel.getColumna20().setValorCadena(columnasExcel.getColumna20().getNombreColumna());
			
			
			archivoReporteDao.registrarDetalleArchivoReporte(columnasExcel, conn);
			
			for (ColumnasExcel columnasExcel2 : dataExcel) {
				columnasExcel2.setIdArchivo(idArchivo);
				columnasExcel2.setUsuarioCreacion(reporteArchivo.getUsuarioCreacion());
				columnasExcel2.setIpCreacion(reporteArchivo.getIpCreacion());
				archivoReporteDao.registrarDetalleArchivoReporte(columnasExcel2, conn);
			}
			
			return true;
		} catch (SQLException e) {
			throw new ErrorRegistroDataException(e);
		} catch (Exception e){
			throw new ErrorRegistroDataException(e);
		} finally{
			if (conn != null){
				conn.close();
			}
		}
	}
	
	@Override
	public List<ReporteArchivoBusqueda> consultarArchivosCargados(ReporteArchivoBusqueda reporteArchivoBusqueda) throws ErrorConsultaDataException{
		try {
			ArchivoReporteDao archivoReporteDao = new ArchivoReporteDaoImpl();
			return archivoReporteDao.consultarArchivosCargados(reporteArchivoBusqueda);
		} catch (SQLException e) {
			throw new ErrorConsultaDataException(e);
		} catch (Exception e) {
			throw new ErrorConsultaDataException(e);
		}
	}
	
	@Override
	public DetalleServicioAgencia consultaDetalleServicioDetalle(int idServicio, int idDetServicio) throws SQLException{
		
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();
		
		return servicioNovaViajesDao.consultaDetalleServicioDetalle(idServicio, idDetServicio);
	}
	@Override
	public List<CuentaBancaria> listarCuentasBancarias() throws SQLException{
		CuentaBancariaDao cuentaBancariaDao = new CuentaBancariaDaoImpl();
		
		return cuentaBancariaDao.listarCuentasBancarias();
	}
	@Override
	public boolean registrarCuentaBancaria(CuentaBancaria cuentaBancaria) throws ErrorRegistroDataException{
		try {
			CuentaBancariaDao cuentaBancariaDao = new CuentaBancariaDaoImpl();
			
			return cuentaBancariaDao.registrarCuentaBancaria(cuentaBancaria);
		} catch (Exception e) {
			throw new ErrorRegistroDataException(e);
		}
	}
	@Override
	public boolean actualizarCuentaBancaria(CuentaBancaria cuentaBancaria) throws ErrorRegistroDataException{
		try {
			CuentaBancariaDao cuentaBancariaDao = new CuentaBancariaDaoImpl();
			
			return cuentaBancariaDao.actualizarCuentaBancaria(cuentaBancaria);
		} catch (SQLException e) {
			throw new ErrorRegistroDataException(e);
		}
	}
	@Override
	public CuentaBancaria consultaCuentaBancaria(Integer idCuenta) throws SQLException{
		CuentaBancariaDao cuentaBancariaDao = new CuentaBancariaDaoImpl();
		
		return cuentaBancariaDao.consultaCuentaBancaria(idCuenta);
	}
	@Override
	public List<CuentaBancaria> listarCuentasBancariasCombo() throws SQLException{
		CuentaBancariaDao cuentaBancariaDao = new CuentaBancariaDaoImpl();
		
		return cuentaBancariaDao.listarCuentasBancariasCombo();
	}
	@Override
	public Comprobante consultarComprobanteObligacion(Integer idObligacion) throws SQLException{
		ComprobanteNovaViajesDao comprobanteDao = new ComprobanteNovaViajesDaoImpl();
		
		return comprobanteDao.consultarObligacion(idObligacion);
	}
}

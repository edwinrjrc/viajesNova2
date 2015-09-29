package pe.com.logistica.negocio.ejb;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.internet.AddressException;
import javax.transaction.UserTransaction;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.cargaexcel.ColumnasExcel;
import pe.com.logistica.bean.cargaexcel.ReporteArchivo;
import pe.com.logistica.bean.negocio.Cliente;
import pe.com.logistica.bean.negocio.Comprobante;
import pe.com.logistica.bean.negocio.Consolidador;
import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.bean.negocio.CorreoClienteMasivo;
import pe.com.logistica.bean.negocio.CorreoMasivo;
import pe.com.logistica.bean.negocio.CuentaBancaria;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.Direccion;
import pe.com.logistica.bean.negocio.DocumentoAdicional;
import pe.com.logistica.bean.negocio.EventoObsAnu;
import pe.com.logistica.bean.negocio.MaestroServicio;
import pe.com.logistica.bean.negocio.PagoServicio;
import pe.com.logistica.bean.negocio.ProgramaNovios;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.bean.negocio.ServicioProveedor;
import pe.com.logistica.bean.negocio.Telefono;
import pe.com.logistica.bean.negocio.TipoCambio;
import pe.com.logistica.bean.negocio.Tramo;
import pe.com.logistica.negocio.dao.ArchivoReporteDao;
import pe.com.logistica.negocio.dao.ClienteDao;
import pe.com.logistica.negocio.dao.ComprobanteNovaViajesDao;
import pe.com.logistica.negocio.dao.ConsolidadorDao;
import pe.com.logistica.negocio.dao.ContactoDao;
import pe.com.logistica.negocio.dao.CuentaBancariaDao;
import pe.com.logistica.negocio.dao.DestinoDao;
import pe.com.logistica.negocio.dao.DireccionDao;
import pe.com.logistica.negocio.dao.MaestroServicioDao;
import pe.com.logistica.negocio.dao.PersonaDao;
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
import pe.com.logistica.negocio.dao.impl.CuentaBancariaDaoImpl;
import pe.com.logistica.negocio.dao.impl.DestinoDaoImpl;
import pe.com.logistica.negocio.dao.impl.DireccionDaoImpl;
import pe.com.logistica.negocio.dao.impl.MaestroServicioDaoImpl;
import pe.com.logistica.negocio.dao.impl.PersonaDaoImpl;
import pe.com.logistica.negocio.dao.impl.ProveedorDaoImpl;
import pe.com.logistica.negocio.dao.impl.ServicioNegocioDaoImpl;
import pe.com.logistica.negocio.dao.impl.ServicioNovaViajesDaoImpl;
import pe.com.logistica.negocio.dao.impl.ServicioNoviosDaoImpl;
import pe.com.logistica.negocio.dao.impl.TelefonoDaoImpl;
import pe.com.logistica.negocio.dao.impl.TipoCambioDaoImpl;
import pe.com.logistica.negocio.exception.EnvioCorreoException;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;
import pe.com.logistica.negocio.exception.ResultadoCeroDaoException;
import pe.com.logistica.negocio.exception.ValidacionException;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilCorreo;
import pe.com.logistica.negocio.util.UtilEjb;

/**
 * Session Bean implementation class NegocioSession
 */
@Stateless(name = "NegocioSession")
@TransactionManagement(TransactionManagementType.BEAN)
public class NegocioSession implements NegocioSessionRemote,
		NegocioSessionLocal {

	@Resource
	private UserTransaction userTransaction;

	@EJB
	UtilNegocioSessionLocal utilNegocioSessionLocal;

	@EJB
	ConsultaNegocioSessionLocal consultaNegocioSessionLocal;

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

			if (proveedor.getListaCuentas() != null) {
				for (CuentaBancaria cuenta : proveedor.getListaCuentas()) {
					if (!proveedorDao.ingresarCuentaBancaria(idPersona, cuenta,
							conexion)) {
						throw new ResultadoCeroDaoException(
								"No se pudo completar el registro del proveedor");
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
						"No se pudo completar la actualizaci�n de la persona");
			}
			proveedor.setCodigoEntero(idPersona);
			direccionDao.eliminarDireccionPersona(proveedor, conexion);
			if (proveedor.getListaDirecciones() != null) {
				int idDireccion = 0;
				for (Direccion direccion : proveedor.getListaDirecciones()) {
					idDireccion = direccionDao.actualizarDireccion(direccion,
							conexion);
					int idTelefono = 0;
					if (idDireccion == 0) {
						throw new ResultadoCeroDaoException(
								"No se pudo completar la actualizaci�n de la direcci�n");
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
											"No se pudo completar el registro del tel�fono de direccion");
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
										"No se pudo completar el registro del tel�fono de contacto");
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

			if (proveedor.getListaCuentas() != null
					&& !proveedor.getListaCuentas().isEmpty()) {
				if (StringUtils.isNotBlank(proveedor.getCuentasEliminadas())) {
					for (int i = 0; i < proveedor.getCuentasEliminadas().split(
							",").length; i++) {
						Integer idCuenta = UtilEjb
								.convertirCadenaEntero(proveedor
										.getCuentasEliminadas().split(",")[i]);
						proveedorDao.validarEliminarCuentaBancaria(idCuenta,
								idPersona, conexion);
					}
				}
				if (proveedorDao.eliminarCuentasBancarias(proveedor, conexion)) {
					for (CuentaBancaria cuentaBancaria : proveedor
							.getListaCuentas()) {
						if (!proveedorDao.ingresarCuentaBancaria(idPersona,
								cuentaBancaria, conexion)) {
							throw new ResultadoCeroDaoException(
									"No se pudo completar el registro de cuentas bancarias");
						}
					}
				} else {
					throw new ResultadoCeroDaoException(
							"No se pudo completar la actualizacion de cuentas bancarias");
				}
			}

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
		userTransaction.begin();

		try {
			conexion = UtilConexion.obtenerConexion();
			cliente.setTipoPersona(1);

			Integer idPersona = personaDao.actualizarPersona(cliente, conexion);
			if (idPersona == 0) {
				throw new ResultadoCeroDaoException(
						"No se pudo completar la actualizaci�n de la persona");
			}
			direccionDao.eliminarDireccionPersona(cliente, conexion);
			if (cliente.getListaDirecciones() != null) {
				int idDireccion = 0;
				if (!direccionDao.eliminarPersonaDirecciones(cliente, conexion)) {
					throw new ResultadoCeroDaoException(
							"No se pudo completar la actualizaci�n de direcciones de la persona");
				}
				for (Direccion direccion : cliente.getListaDirecciones()) {
					idDireccion = direccionDao.actualizarDireccion(direccion,
							conexion);
					int idTelefono = 0;
					if (idDireccion == 0) {
						throw new ResultadoCeroDaoException(
								"No se pudo completar la actualizaci�n de la direcci�n");
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
											"No se pudo completar el registro del tel�fono de direccion");
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
										"No se pudo completar el registro del tel�fono de contacto");
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
			userTransaction.commit();
			return true;
		} catch (ResultadoCeroDaoException e) {
			userTransaction.rollback();
			throw new ResultadoCeroDaoException(e.getMensajeError(), e);
		} catch (SQLException e) {
			userTransaction.rollback();
			throw new SQLException(e);
		} finally {
			if (conexion != null) {
				conexion.close();
			}
		}
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
						for (Tramo tramo : detalleServicio.getRuta()
								.getTramos()) {
							tramo = servicioNovaViajesDao.registrarTramo(tramo,
									conexion);
							if (tramo.getCodigoEntero() == null
									|| tramo.getCodigoEntero().intValue() == 0) {
								throw new ErrorRegistroDataException(
										"No se pudo registrar los servicios de la venta");
							}
							detalleServicio.getRuta().setTramo(tramo);
							if (idRuta == null) {
								idRuta = servicioNovaViajesDao
										.obtenerSiguienteRuta(conexion);
							}
							detalleServicio.getRuta().setCodigoEntero(idRuta);
							if (!servicioNovaViajesDao.registrarRuta(
									detalleServicio.getRuta(), conexion)) {
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

			servicioNovaViajesDao.registrarSaldosServicio(servicioAgencia,
					conexion);

			return idServicio;
		} catch (SQLException e) {
			throw new ErrorRegistroDataException(e.getMessage(), e);
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
	public void registrarPago(PagoServicio pago) throws SQLException, Exception {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();

		servicioNovaViajesDao.registrarPagoServicio(pago);
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
				comprobante = UtilEjb.obtenerDetalleComprobante(comprobante,
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
	public boolean registrarObligacionXPagar(Comprobante comprobante)
			throws SQLException, Exception {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();
		return servicioNovaViajesDao.registrarObligacionXPagar(comprobante);
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
				for (DetalleServicioAgencia detalleHijo : detalleServicioAgencia
						.getServiciosHijos()) {
					if (detalleHijo.getIdComprobanteGenerado() != null
							&& detalleHijo.getComprobanteAsociado()
									.getCodigoEntero() != null
							&& detalleHijo.getCodigoEntero() != null
							&& detalleHijo.getServicioPadre().getCodigoEntero() != null) {

						if (detalleHijo.isAgrupado()) {
							for (Integer id : detalleHijo
									.getCodigoEnteroAgrupados()) {
								detalleHijo.setCodigoEntero(id);
								servicioNovaViajesDao
										.guardarRelacionComproObligacion(
												detalleHijo, conn);
							}
						} else {
							servicioNovaViajesDao
									.guardarRelacionComproObligacion(
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
	public boolean grabarComprobantesReporte(ReporteArchivo reporteArchivo,
			ColumnasExcel columnasExcel, List<ColumnasExcel> dataExcel)
			throws ErrorRegistroDataException, SQLException {
		ArchivoReporteDao archivoReporteDao = new ArchivoReporteDaoImpl();
		Connection conn = null;

		try {
			conn = UtilConexion.obtenerConexion();
			int idArchivo = archivoReporteDao.registrarArchivoReporteCabecera(
					reporteArchivo, conn);
			columnasExcel.setIdArchivo(idArchivo);
			columnasExcel.setUsuarioCreacion(reporteArchivo
					.getUsuarioCreacion());
			columnasExcel.setIpCreacion(reporteArchivo.getIpCreacion());
			columnasExcel.getColumna1().setValorCadena(
					columnasExcel.getColumna1().getNombreColumna());
			columnasExcel.getColumna2().setValorCadena(
					columnasExcel.getColumna2().getNombreColumna());
			columnasExcel.getColumna3().setValorCadena(
					columnasExcel.getColumna3().getNombreColumna());
			columnasExcel.getColumna4().setValorCadena(
					columnasExcel.getColumna4().getNombreColumna());
			columnasExcel.getColumna5().setValorCadena(
					columnasExcel.getColumna5().getNombreColumna());
			columnasExcel.getColumna6().setValorCadena(
					columnasExcel.getColumna6().getNombreColumna());
			columnasExcel.getColumna7().setValorCadena(
					columnasExcel.getColumna7().getNombreColumna());
			columnasExcel.getColumna8().setValorCadena(
					columnasExcel.getColumna8().getNombreColumna());
			columnasExcel.getColumna9().setValorCadena(
					columnasExcel.getColumna9().getNombreColumna());
			columnasExcel.getColumna10().setValorCadena(
					columnasExcel.getColumna10().getNombreColumna());
			columnasExcel.getColumna11().setValorCadena(
					columnasExcel.getColumna11().getNombreColumna());
			columnasExcel.getColumna12().setValorCadena(
					columnasExcel.getColumna12().getNombreColumna());
			columnasExcel.getColumna13().setValorCadena(
					columnasExcel.getColumna13().getNombreColumna());
			columnasExcel.getColumna14().setValorCadena(
					columnasExcel.getColumna14().getNombreColumna());
			columnasExcel.getColumna15().setValorCadena(
					columnasExcel.getColumna15().getNombreColumna());
			columnasExcel.getColumna16().setValorCadena(
					columnasExcel.getColumna16().getNombreColumna());
			columnasExcel.getColumna17().setValorCadena(
					columnasExcel.getColumna17().getNombreColumna());
			columnasExcel.getColumna18().setValorCadena(
					columnasExcel.getColumna18().getNombreColumna());
			columnasExcel.getColumna19().setValorCadena(
					columnasExcel.getColumna19().getNombreColumna());
			columnasExcel.getColumna20().setValorCadena(
					columnasExcel.getColumna20().getNombreColumna());

			archivoReporteDao.registrarDetalleArchivoReporte(columnasExcel,
					conn);

			for (ColumnasExcel columnasExcel2 : dataExcel) {
				columnasExcel2.setIdArchivo(idArchivo);
				columnasExcel2.setUsuarioCreacion(reporteArchivo
						.getUsuarioCreacion());
				columnasExcel2.setIpCreacion(reporteArchivo.getIpCreacion());
				archivoReporteDao.registrarDetalleArchivoReporte(
						columnasExcel2, conn);
			}

			return true;
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
	public boolean registrarCuentaBancaria(CuentaBancaria cuentaBancaria)
			throws ErrorRegistroDataException {
		try {
			CuentaBancariaDao cuentaBancariaDao = new CuentaBancariaDaoImpl();

			return cuentaBancariaDao.registrarCuentaBancaria(cuentaBancaria);
		} catch (Exception e) {
			throw new ErrorRegistroDataException(e);
		}
	}

	@Override
	public boolean actualizarCuentaBancaria(CuentaBancaria cuentaBancaria)
			throws ErrorRegistroDataException {
		try {
			CuentaBancariaDao cuentaBancariaDao = new CuentaBancariaDaoImpl();

			return cuentaBancariaDao.actualizarCuentaBancaria(cuentaBancaria);
		} catch (SQLException e) {
			throw new ErrorRegistroDataException(e);
		}
	}

	@Override
	public boolean registrarTipoCambio(TipoCambio tipoCambio)
			throws SQLException {
		TipoCambioDao tipoCambioDao = new TipoCambioDaoImpl();

		return tipoCambioDao.registrarTipoCambio(tipoCambio);
	}
}

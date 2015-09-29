package pe.com.logistica.negocio.ejb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.ConfiguracionTipoServicio;
import pe.com.logistica.bean.negocio.Destino;
import pe.com.logistica.bean.negocio.Maestro;
import pe.com.logistica.bean.negocio.Pais;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.negocio.dao.CatalogoDao;
import pe.com.logistica.negocio.dao.ConfiguracionServicioDao;
import pe.com.logistica.negocio.dao.DestinoDao;
import pe.com.logistica.negocio.dao.MaestroDao;
import pe.com.logistica.negocio.dao.ProveedorDao;
import pe.com.logistica.negocio.dao.impl.CatalogoDaoImpl;
import pe.com.logistica.negocio.dao.impl.ConfiguracionServicioDaoImpl;
import pe.com.logistica.negocio.dao.impl.DestinoDaoImpl;
import pe.com.logistica.negocio.dao.impl.MaestroDaoImpl;
import pe.com.logistica.negocio.dao.impl.ProveedorDaoImpl;
import pe.com.logistica.negocio.exception.ConnectionException;
import pe.com.logistica.negocio.exception.ErrorConsultaDataException;
import pe.com.logistica.negocio.util.UtilConexion;

/**
 * Session Bean implementation class SoporteSession
 */
@Stateless(name = "SoporteSession")
public class SoporteSession implements SoporteRemote, SoporteLocal {

	MaestroDao maestroDao = null;
	CatalogoDao catalogoDao = null;
	DestinoDao destinoDao = null;

	/**
	 * Default constructor.
	 */
	public SoporteSession() {

	}

	@Override
	public List<Maestro> listarMaestros() throws SQLException {
		maestroDao = new MaestroDaoImpl();
		return maestroDao.listarMaestros();
	}

	@Override
	public List<Maestro> listarHijosMaestro(int idmaestro) throws SQLException {
		maestroDao = new MaestroDaoImpl();
		return maestroDao.listarHijosMaestro(idmaestro);
	}

	@Override
	public boolean ingresarMaestro(Maestro maestro) throws SQLException {
		maestroDao = new MaestroDaoImpl();
		return maestroDao.ingresarMaestro(maestro);
	}

	@Override
	public boolean ingresarHijoMaestro(Maestro maestro) throws SQLException {
		maestroDao = new MaestroDaoImpl();
		return maestroDao.ingresarHijoMaestro(maestro);
	}

	@Override
	public Maestro consultarMaestro(int id) throws SQLException {
		maestroDao = new MaestroDaoImpl();
		return maestroDao.consultarMaestro(id);
	}

	@Override
	public Maestro consultarHijoMaestro(Maestro hijo) throws SQLException {
		maestroDao = new MaestroDaoImpl();
		return maestroDao.consultarHijoMaestro(hijo);
	}

	@Override
	public boolean actualizarMaestro(Maestro maestro) throws SQLException {
		maestroDao = new MaestroDaoImpl();
		return maestroDao.actualizarMaestro(maestro);
	}

	@Override
	public List<BaseVO> listarCatalogoMaestro(int maestro) throws SQLException,
			ConnectionException {
		catalogoDao = new CatalogoDaoImpl();
		return catalogoDao.listarCatalogoMaestro(maestro);
	}

	@Override
	public List<BaseVO> listarCatalogoDepartamento() throws SQLException,
			ConnectionException {
		catalogoDao = new CatalogoDaoImpl();
		return catalogoDao.listaDepartamento();
	}

	@Override
	public List<BaseVO> listarCatalogoProvincia(String idDepartamento)
			throws SQLException, ConnectionException {
		catalogoDao = new CatalogoDaoImpl();
		return catalogoDao.listaProvincia(idDepartamento);
	}

	@Override
	public List<BaseVO> listarCatalogoDistrito(String idDepartamento,
			String idProvincia) throws SQLException, ConnectionException {
		catalogoDao = new CatalogoDaoImpl();
		return catalogoDao.listaDistrito(idDepartamento, idProvincia);
	}

	@Override
	public List<BaseVO> listarContinentes() throws SQLException {
		maestroDao = new MaestroDaoImpl();
		List<BaseVO> lista = new ArrayList<BaseVO>();
		int idmaestro = 10;
		List<Maestro> listaContinentes = maestroDao
				.listarHijosMaestro(idmaestro);
		for (Maestro maestro : listaContinentes) {
			lista.add((BaseVO) maestro);
		}

		return lista;
	}

	@Override
	public List<BaseVO> consultarPaisesContinente(int idcontinente)
			throws SQLException, Exception {
		maestroDao = new MaestroDaoImpl();
		return maestroDao.listarPaises(idcontinente);
	}

	@Override
	public boolean ingresarPais(Pais pais) throws SQLException, Exception {
		maestroDao = new MaestroDaoImpl();
		return maestroDao.ingresarPais(pais);
	}

	@Override
	public boolean ingresarDestino(Destino destino) throws SQLException,
			Exception {
		destinoDao = new DestinoDaoImpl();
		return destinoDao.ingresarDestino(destino);
	}

	@Override
	public boolean actualizarDestino(Destino destino) throws SQLException,
			Exception {
		destinoDao = new DestinoDaoImpl();
		return destinoDao.actualizarDestino(destino);
	}

	@Override
	public List<Destino> listarDestinos() throws SQLException, Exception {
		destinoDao = new DestinoDaoImpl();
		return destinoDao.listarDestinos();
	}

	@Override
	public ConfiguracionTipoServicio consultarConfiguracionServicio(
			int idTipoServicio) throws SQLException, Exception {
		ConfiguracionServicioDao configuracionServicioDao = new ConfiguracionServicioDaoImpl();

		return configuracionServicioDao
				.consultarConfiguracionServicio(idTipoServicio);
	}

	@Override
	public List<Proveedor> listarProveedorTipo(BaseVO tipoProveedor)
			throws SQLException, Exception {
		ProveedorDao proveedorDao = new ProveedorDaoImpl();

		return proveedorDao.listarComboProveedorTipo(tipoProveedor);
	}

	@Override
	public boolean esDestinoNacional(Integer destino)
			throws ErrorConsultaDataException, SQLException, Exception {
		DestinoDao destinoDao = new DestinoDaoImpl();

		try {
			Destino destinoConsultado = destinoDao.consultarDestino(destino);

			Locale localidad = Locale.getDefault();

			return localidad.getCountry().equals(
					destinoConsultado.getPais().getAbreviado());
		} catch (SQLException e) {
			throw new ErrorConsultaDataException(
					"Error al determinar la nacionalidad del destino");
		} catch (Exception e) {
			throw new ErrorConsultaDataException(
					"Error al determinar la nacionalidad del destino");
		}
	}

	@Override
	public List<ConfiguracionTipoServicio> listarConfiguracionServicios()
			throws SQLException, Exception {
		ConfiguracionServicioDao configuracionServicioDao = new ConfiguracionServicioDaoImpl();

		return configuracionServicioDao.listarConfiguracionServicios();
	}

	@Override
	public List<BaseVO> listarTipoServicios() throws SQLException, Exception {
		ConfiguracionServicioDao configuracionServicioDao = new ConfiguracionServicioDaoImpl();

		return configuracionServicioDao.listarTipoServicios();
	}

	@Override
	public boolean guardarConfiguracionServicio(
			List<ConfiguracionTipoServicio> listaConfigServicios)
			throws ErrorConsultaDataException, SQLException, Exception {
		ConfiguracionServicioDao configuracionServicioDao = new ConfiguracionServicioDaoImpl();

		Connection conn = null;

		try {
			conn = UtilConexion.obtenerConexion();

			configuracionServicioDao.eliminarConfiguracion(
					listaConfigServicios.get(0), conn);

			for (ConfiguracionTipoServicio configuracion : listaConfigServicios) {
				configuracionServicioDao.registrarConfiguracionServicio(
						configuracion, conn);
			}

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ErrorConsultaDataException(
					"Error al grabar configuracion");
		} catch (Exception e) {
			throw new ErrorConsultaDataException(
					"Error al grabar configuracion");
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

	}

	@Override
	public List<Destino> buscarDestinos(String descripcion)
			throws SQLException, Exception {

		destinoDao = new DestinoDaoImpl();

		return destinoDao.buscarDestinos(descripcion);
	}

	@Override
	public Destino consultaDestinoIATA(String codigoIATA) throws SQLException {
		destinoDao = new DestinoDaoImpl();

		return destinoDao.consultarDestinoIATA(codigoIATA);
	}
}

package pe.com.logistica.negocio.ejb;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Local;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.ConfiguracionTipoServicio;
import pe.com.logistica.bean.negocio.Destino;
import pe.com.logistica.bean.negocio.Maestro;
import pe.com.logistica.bean.negocio.Pais;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.negocio.exception.ConnectionException;
import pe.com.logistica.negocio.exception.ErrorConsultaDataException;

@Local
public interface SoporteLocal {

	public List<Maestro> listarMaestros() throws SQLException;

	public List<Maestro> listarHijosMaestro(int idmaestro) throws SQLException;

	public boolean ingresarMaestro(Maestro maestro) throws SQLException;

	public boolean ingresarHijoMaestro(Maestro maestro) throws SQLException;

	public Maestro consultarMaestro(int id) throws SQLException;

	public Maestro consultarHijoMaestro(Maestro hijo) throws SQLException;

	public boolean actualizarMaestro(Maestro maestro) throws SQLException;

	public List<BaseVO> listarCatalogoMaestro(int maestro) throws SQLException,
			ConnectionException;

	public List<BaseVO> listarCatalogoDepartamento() throws SQLException,
			ConnectionException;

	public List<BaseVO> listarCatalogoProvincia(String idDepartamento)
			throws SQLException, ConnectionException;

	public List<BaseVO> listarCatalogoDistrito(String idDepartamento,
			String idProvincia) throws SQLException, ConnectionException;

	public List<BaseVO> listarContinentes() throws SQLException,
			ConnectionException;

	public List<BaseVO> consultarPaisesContinente(int idcontinente)
			throws SQLException, Exception;

	boolean ingresarPais(Pais pais) throws SQLException, Exception;

	public boolean ingresarDestino(Destino destino) throws SQLException,
			Exception;

	public boolean actualizarDestino(Destino destino) throws SQLException,
			Exception;

	List<Destino> listarDestinos() throws SQLException, Exception;

	public ConfiguracionTipoServicio consultarConfiguracionServicio(
			int convertirCadenaEntero) throws SQLException, Exception;

	public List<Proveedor> listarProveedorTipo(BaseVO tipoProveedor)
			throws SQLException, Exception;

	public boolean esDestinoNacional(Integer destino)
			throws ErrorConsultaDataException, SQLException, Exception;

	public List<ConfiguracionTipoServicio> listarConfiguracionServicios()
			throws SQLException, Exception;

	List<BaseVO> listarTipoServicios() throws SQLException, Exception;

	public boolean guardarConfiguracionServicio(
			List<ConfiguracionTipoServicio> listaConfigServicios)
			throws SQLException, Exception;

	public List<Destino> buscarDestinos(String descripcion)
			throws SQLException, Exception;

	public Destino consultaDestinoIATA(String codigoIATA) throws SQLException;
}

/**
 * 
 */
package pe.com.logistica.web.servicio;

import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.ConfiguracionTipoServicio;
import pe.com.logistica.bean.negocio.Destino;
import pe.com.logistica.bean.negocio.Maestro;
import pe.com.logistica.bean.negocio.Pais;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.negocio.exception.ConnectionException;
import pe.com.logistica.negocio.exception.ErrorConsultaDataException;

/**
 * @author Edwin
 * 
 */
public interface SoporteServicio {

	public List<Maestro> listarMaestros() throws SQLException;

	public List<Maestro> listarHijosMaestro(int idmaestro) throws SQLException;

	public boolean ingresarMaestro(Maestro maestro) throws SQLException;

	public boolean ingresarHijoMaestro(Maestro maestro) throws SQLException;

	public Maestro consultarMaestro(int idmaestro) throws SQLException;

	public Maestro consultarHijoMaestro(Maestro hijo) throws SQLException;

	public boolean actualizarMaestro(Maestro hijo) throws SQLException;

	public List<BaseVO> listarCatalogoMaestro(int idmaestro)
			throws SQLException, ConnectionException;

	public List<BaseVO> listarCatalogoDepartamento() throws SQLException,
			ConnectionException;

	public List<BaseVO> listarCatalogoProvincia(String idProvincia)
			throws SQLException, ConnectionException;

	public List<BaseVO> listarCatalogoDistrito(String idDepartamento,
			String idProvincia) throws SQLException, ConnectionException;

	public List<BaseVO> listarContinentes() throws SQLException,
			ConnectionException;

	boolean ingresarPais(Pais pais) throws SQLException, Exception;

	List<BaseVO> consultarPaises(int idcontinente) throws SQLException,
			Exception;

	boolean ingresarDestino(Destino destino) throws SQLException, Exception;

	boolean actualizarDestino(Destino destino) throws SQLException, Exception;

	List<Destino> listarDestinos() throws SQLException, Exception;

	public ConfiguracionTipoServicio consultarConfiguracionServicio(
			int convertirCadenaEntero);

	public List<Proveedor> listarComboProveedorTipo(BaseVO proveedor)
			throws SQLException, Exception;

	public boolean esDestinoNacional(Integer destino)
			throws ErrorConsultaDataException, SQLException, Exception;

	public List<ConfiguracionTipoServicio> listarConfiguracionServicios()
			throws SQLException, Exception;

	List<BaseVO> listarTiposServicios() throws SQLException, Exception;

	public boolean guardarConfiguracionServicio(
			List<ConfiguracionTipoServicio> listaConfigServicios)
			throws SQLException, Exception;

	public List<Destino> consultarOrigen(String descripcion)
			throws SQLException, Exception;

	List<Destino> consultarDestino(String descripcion) throws SQLException,
			Exception;

	Destino consultaDestinoIATA(String codigoIATA) throws SQLException;
}

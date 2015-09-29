/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.ConfiguracionTipoServicio;

/**
 * @author Edwin
 *
 */
public interface ConfiguracionServicioDao {

	public ConfiguracionTipoServicio consultarConfiguracionServicio(
			Integer idTipoServicio) throws SQLException, Exception;

	public List<ConfiguracionTipoServicio> listarConfiguracionServicios()
			throws SQLException, Exception;

	List<BaseVO> listarTipoServicios() throws SQLException, Exception;

	boolean eliminarConfiguracion(
			ConfiguracionTipoServicio configuracionTipoServicio, Connection conn)
			throws SQLException;

	public boolean registrarConfiguracionServicio(
			ConfiguracionTipoServicio configuracion, Connection conn)
			throws SQLException;
}

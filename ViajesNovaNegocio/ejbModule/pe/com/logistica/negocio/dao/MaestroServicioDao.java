/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.MaestroServicio;

/**
 * @author edwreb
 *
 */
public interface MaestroServicioDao {
	
	public List<MaestroServicio> listarMaestroServicios() throws SQLException;
	
	public Integer ingresarMaestroServicio(MaestroServicio servicio) throws SQLException;
	
	public boolean actualizarMaestroServicio(MaestroServicio servicio) throws SQLException;
	
	public MaestroServicio consultarMaestroServicio(int idMaestroServicio) throws SQLException;

	List<MaestroServicio> listarMaestroServiciosFee() throws SQLException;

	List<MaestroServicio> listarMaestroServiciosImpto() throws SQLException;

	List<MaestroServicio> listarMaestroServiciosIgv() throws SQLException;

	void ingresarServicioMaestroServicio(Integer idServicio,
			List<BaseVO> listaMaeServicioImpto) throws SQLException, Exception;

	List<BaseVO> consultarServicioDependientes(Integer idServicio)
			throws SQLException;

	List<MaestroServicio> consultarServiciosInvisibles(Integer idServicio)
			throws SQLException, Exception;

	List<MaestroServicio> listarMaestroServiciosAdm() throws SQLException;

	MaestroServicio consultarMaestroServicio(int idMaestroServicio,
			Connection conn) throws SQLException;
	

}

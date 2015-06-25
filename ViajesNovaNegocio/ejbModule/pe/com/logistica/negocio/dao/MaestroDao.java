/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Maestro;
import pe.com.logistica.bean.negocio.Pais;

/**
 * @author Edwin
 *
 */
public interface MaestroDao {

	public List<Maestro> listarMaestros() throws SQLException;
	
	public List<Maestro> listarHijosMaestro(int idmaestro) throws SQLException;
	
	public boolean ingresarMaestro(Maestro maestro) throws SQLException;
	
	public boolean ingresarHijoMaestro(Maestro maestro) throws SQLException;

	public Maestro consultarMaestro(int id) throws SQLException;

	public Maestro consultarHijoMaestro(Maestro hijoMaestro) throws SQLException;

	public boolean actualizarMaestro(Maestro maestro) throws SQLException;
	
	public List<BaseVO> listarPaises(int idcontinente) throws SQLException;

	boolean ingresarPais(Pais pais) throws SQLException;
	
}

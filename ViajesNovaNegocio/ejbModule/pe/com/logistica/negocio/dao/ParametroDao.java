/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.negocio.Parametro;

/**
 * @author Edwin
 *
 */
public interface ParametroDao {

	public List<Parametro> listarParametros() throws SQLException;
	
	public void registrarParametro(Parametro parametro) throws SQLException;
	
	public void actualizarParametro(Parametro parametro) throws SQLException;

	Parametro consultarParametro(int id) throws SQLException;
}

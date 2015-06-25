/**
 * 
 */
package pe.com.logistica.web.servicio;

import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.negocio.Parametro;

/**
 * @author Edwin
 *
 */
public interface ParametroServicio {

	public List<Parametro> listarParametros() throws SQLException;

	public void registrarParametro(Parametro parametro) throws SQLException;

	public void actualizarParametro(Parametro parametro) throws SQLException;
	
	public Parametro consultarParametro(int id) throws SQLException;
}

/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.negocio.Consolidador;

/**
 * @author Edwin
 *
 */
public interface ConsolidadorDao {

	public boolean ingresarConsolidador(Consolidador consolidador)
			throws SQLException, Exception;

	public boolean actualizarConsolidador(Consolidador consolidador)
			throws SQLException, Exception;

	public List<Consolidador> listarConsolidador() throws SQLException,
			Exception;

	public Consolidador consultarConsolidador(Consolidador consolidador)
			throws SQLException, Exception;

}

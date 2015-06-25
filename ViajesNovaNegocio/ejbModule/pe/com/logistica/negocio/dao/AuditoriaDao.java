/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.SQLException;

import pe.com.logistica.bean.negocio.Usuario;

/**
 * @author Edwin
 *
 */
public interface AuditoriaDao {

	/**
	 * Metodo que registrar el evento de sesion del sistema
	 * @param usuario
	 * @param tipoEvento
	 * @return
	 * @throws SQLException
	 */
	public boolean registrarEventoSesion(Usuario usuario, Integer tipoEvento) throws SQLException;
}

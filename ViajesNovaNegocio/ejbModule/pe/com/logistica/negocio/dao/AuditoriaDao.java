/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.bean.recursoshumanos.UsuarioAsistencia;

/**
 * @author Edwin
 *
 */
public interface AuditoriaDao {

	/**
	 * Metodo que registrar el evento de sesion del sistema
	 * 
	 * @param usuario
	 * @param tipoEvento
	 * @return
	 * @throws SQLException
	 */
	public boolean registrarEventoSesion(Usuario usuario, Integer tipoEvento)
			throws SQLException;

	/**
	 * 
	 * @param fecha
	 * @return
	 * @throws SQLException
	 */
	public List<UsuarioAsistencia> listarHoraEntradaXDia(Date fecha)
			throws SQLException;
}

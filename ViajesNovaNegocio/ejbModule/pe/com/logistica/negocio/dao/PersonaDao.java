/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.Connection;
import java.sql.SQLException;

import pe.com.logistica.bean.base.Persona;

/**
 * @author Edwin
 * 
 */
public interface PersonaDao {

	int registrarPersona(Persona persona, Connection conexion)
			throws SQLException;

	int actualizarPersona(Persona persona, Connection conexion)
			throws SQLException;

}

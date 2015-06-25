/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.base.Persona;
import pe.com.logistica.bean.negocio.Direccion;

/**
 * @author Edwin
 *
 */
public interface DireccionDao {

	public int registrarDireccion(Direccion direccion, Connection conexion) throws SQLException;
	
	public int actualizarDireccion(Direccion direccion, Connection conexion) throws SQLException;

	void registrarPersonaDireccion(int idPersona, int idTipoPersona,
			int idDireccion, Connection conexion) throws SQLException;
	
	List<Direccion> consultarDireccionProveedor(int idProveedor) throws SQLException;

	boolean eliminarTelefonoDireccion(Direccion direccion, Connection conexion)
			throws SQLException;

	boolean eliminarDireccionPersona(Persona persona, Connection conexion)
			throws SQLException;
	
	List<Direccion> consultarDireccionPersona(int idPersona) throws SQLException;

	List<Direccion> consultarDireccionPersona(int idPersona, Connection conn)
			throws SQLException;
}

/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.base.CorreoElectronico;
import pe.com.logistica.bean.base.Persona;
import pe.com.logistica.bean.negocio.Contacto;

/**
 * @author Edwin
 *
 */
public interface ContactoDao {

	void registrarContactoProveedor(int idproveedor, Contacto contacto,
			Connection conexion) throws SQLException;

	boolean eliminarTelefonoContacto(Contacto contacto, Connection conexion)
			throws SQLException;

	boolean eliminarContacto(Contacto contacto, Connection conexion)
			throws SQLException;

	List<Contacto> consultarContactoProveedor(int idproveedor)
			throws SQLException;

	boolean ingresarCorreoElectronico(Contacto contacto, Connection conexion)
			throws SQLException;

	boolean eliminarCorreosContacto(Contacto contacto, Connection conexion)
			throws SQLException;

	List<CorreoElectronico> consultarCorreos(int idcontacto, Connection conn)
			throws SQLException;

	boolean eliminarContactoProveedor(Persona proveedor, Connection conexion)
			throws SQLException;
	
	List<Contacto> listarContactosXPersona(int idpersona)throws SQLException;

	List<Contacto> listarContactosXPersona(int idpersona, Connection conn)
			throws SQLException;
}

/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.bean.negocio.ServicioProveedor;

/**
 * @author Edwin
 *
 */
public interface ProveedorDao {

	void registroProveedor(Proveedor proveedor, Connection conexion)
			throws SQLException;
	
	List<Proveedor> listarProveedor(Proveedor proveedor) throws SQLException;
	
	List<Proveedor> listarComboProveedorTipo(BaseVO proveedor) throws SQLException;
	
	Proveedor consultarProveedor(int idProveedor) throws SQLException;
	
	Contacto consultarContacto(int idPersona) throws SQLException;

	void actualizarProveedor(Proveedor proveedor, Connection conexion) throws SQLException;

	List<Proveedor> buscarProveedor(Proveedor proveedor) throws SQLException;

	boolean ingresarServicioProveedor(Integer idproveedor,
			ServicioProveedor servicio, Connection conn) throws SQLException;

	boolean actualizarServicioProveedor(Integer idproveedor,
			ServicioProveedor servicio, Connection conn) throws SQLException;

	List<ServicioProveedor> consultarServicioProveedor(int idProveedor)
			throws SQLException;

	List<ServicioProveedor> consultarServicioProveedor(int idProveedor,
			Connection conn) throws SQLException;

	void registroProveedorTipo(Proveedor proveedor, Connection conexion)
			throws SQLException;

	void actualizarProveedorTipo(Proveedor proveedor, Connection conexion)
			throws SQLException;

	Proveedor consultarProveedor(int idProveedor, Connection conn)
			throws SQLException;
}

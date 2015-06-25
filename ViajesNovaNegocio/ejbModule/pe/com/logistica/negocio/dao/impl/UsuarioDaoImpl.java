/**
 * 
 */
package pe.com.logistica.negocio.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.negocio.dao.UsuarioDao;
import pe.com.logistica.negocio.exception.ErrorEncriptacionException;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilEncripta;
import pe.com.logistica.negocio.util.UtilJdbc;

/**
 * @author Edwin
 *
 */
public class UsuarioDaoImpl implements UsuarioDao{

	/**
	 * 
	 */
	public UsuarioDaoImpl() {
	}

	@Override
	public boolean registrarUsuario(Usuario usuario) throws SQLException, ErrorEncriptacionException {
		boolean resultado = false;
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{? = call seguridad.fn_ingresarusuario(?,?,?,?,?,?,?,?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setString(i++, usuario.getUsuario());
			cs.setString(i++, UtilEncripta.encriptaCadena(usuario.getCredencial()));
			cs.setInt(i++, usuario.getRol().getCodigoEntero());
			cs.setString(i++, UtilJdbc.convertirMayuscula(usuario.getNombres()));
			cs.setString(i++, UtilJdbc.convertirMayuscula(usuario.getApellidoPaterno()));
			cs.setString(i++, UtilJdbc.convertirMayuscula(usuario.getApellidoMaterno()));
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(usuario.getFechaNacimiento()));
			cs.setBoolean(i++, usuario.isVendedor());
			cs.execute();
			
			resultado = cs.getBoolean(1);
		} catch (SQLException e) {
			resultado = false;
			throw new SQLException(e);
		} finally{
			try {
				if (cs != null){
					cs.close();
				}
				if (conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null){
						conn.close();
					}
					throw new SQLException(e);
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}
		
		
		return resultado;
	}
	
	@Override
	public boolean actualizarUsuario(Usuario usuario) throws SQLException {
		boolean resultado = false;
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{? = call seguridad.fn_actualizarusuario(?,?,?,?,?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, usuario.getCodigoEntero());
			cs.setInt(i++, usuario.getRol().getCodigoEntero());
			cs.setString(i++, UtilJdbc.convertirMayuscula(usuario.getNombres()));
			cs.setString(i++, UtilJdbc.convertirMayuscula(usuario.getApellidoPaterno()));
			cs.setString(i++, UtilJdbc.convertirMayuscula(usuario.getApellidoMaterno()));
			
			cs.execute();
			
			resultado = cs.getBoolean(1);
		} catch (SQLException e) {
			resultado = false;
			throw new SQLException(e);
		} finally{
			try {	
				if (cs != null){
					cs.close();
				}
				if (conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null){
						conn.close();
					}
					throw new SQLException(e);
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}
		
		
		return resultado;
	}

	@Override
	public List<Usuario> listarUsuarios() throws SQLException {
		List<Usuario> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select * from seguridad.vw_listarusuarios";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			rs = cs.executeQuery();
			
			resultado = new ArrayList<Usuario>();
			Usuario usuario = null;
			while(rs.next()){
				usuario = new Usuario();
				usuario.setCodigoEntero(rs.getInt("id"));
				usuario.setUsuario(UtilJdbc.obtenerCadena(rs, "usuario"));
				usuario.getRol().setCodigoEntero(rs.getInt("id_rol"));
				usuario.getRol().setNombre(UtilJdbc.obtenerCadena(rs,"nombre"));
				usuario.setNombres(UtilJdbc.obtenerCadena(rs,"nombres"));
				usuario.setApellidoPaterno(UtilJdbc.obtenerCadena(rs,"apepaterno"));
				usuario.setApellidoMaterno(UtilJdbc.obtenerCadena(rs,"apematerno"));
				usuario.setVendedor(UtilJdbc.obtenerBoolean(rs, "vendedor"));
				usuario.setFechaNacimiento(UtilJdbc.obtenerFecha(rs, "fecnacimiento"));
				resultado.add(usuario);
			}
		} catch (SQLException e) {
			resultado = null;
			throw new SQLException(e);
		} finally{
			try {
				if (rs != null){
					rs.close();
				}
				if (cs != null){
					cs.close();
				}
				if (conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null){
						conn.close();
					}
					throw new SQLException(e);
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}
		
		return resultado;
	}
	
	@Override
	public Usuario consultarUsuario(int id) throws SQLException {
		Usuario resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select id, usuario, credencial, id_rol, nombre, nombres, apepaterno, " +
				"apematerno, fecnacimiento, vendedor" +
				" from seguridad.vw_listarusuarios where id = ?";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			cs.setInt(1, id);
			rs = cs.executeQuery();
			
			resultado = new Usuario();
			if (rs.next()){
				resultado.setCodigoEntero(rs.getInt("id"));
				resultado.setUsuario(UtilJdbc.obtenerCadena(rs, "usuario"));
				resultado.setCredencial(UtilJdbc.obtenerCadena(rs, "credencial"));
				resultado.getRol().setCodigoEntero(rs.getInt("id_rol"));
				resultado.getRol().setNombre(UtilJdbc.obtenerCadena(rs,"nombre"));
				resultado.setNombres(UtilJdbc.obtenerCadena(rs,"nombres"));
				resultado.setApellidoPaterno(UtilJdbc.obtenerCadena(rs,"apepaterno"));
				resultado.setApellidoMaterno(UtilJdbc.obtenerCadena(rs,"apematerno"));
				resultado.setFechaNacimiento(UtilJdbc.obtenerFecha(rs, "fecnacimiento"));
				resultado.setVendedor(UtilJdbc.obtenerBoolean(rs, "vendedor"));
			}
		} catch (SQLException e) {
			resultado = null;
			throw new SQLException(e);
		} finally{
			try {
				if (rs != null){
					rs.close();
				}
				if (cs != null){
					cs.close();
				}
				if (conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null){
						conn.close();
					}
					throw new SQLException(e);
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}
		
		return resultado;
	}

	@Override
	public boolean inicioSesion(Usuario usuario) throws SQLException, ErrorEncriptacionException {
		boolean resultado = false;
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{? = call seguridad.fn_iniciosesion(?,?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setString(i++, UtilJdbc.convertirMayuscula(usuario.getUsuario()));
			cs.setString(i++, UtilEncripta.encriptaCadena(usuario.getCredencial()));
			
			cs.execute();
			
			resultado = cs.getBoolean(1);
		} catch (SQLException e) {
			resultado = false;
			throw new SQLException(e);
		} finally{
			try {
				if (cs != null){
					cs.close();
				}
				if (conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null){
						conn.close();
					}
					throw new SQLException(e);
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}
		
		return resultado;
	}
	
	@Override
	public Usuario inicioSesion2(Usuario usuario) throws SQLException, ErrorEncriptacionException {
		Usuario resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select id, usuario, credencial, id_rol, nombre, nombres, apepaterno, apematerno, cambiarclave, feccaducacredencial" +
				" from seguridad.vw_listarusuarios where upper(usuario) = ?";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			cs.setString(1, UtilJdbc.convertirMayuscula(usuario.getUsuario()));
			//cs.setString(2, UtilEncripta.encriptaCadena(usuario.getCredencial()));
			rs = cs.executeQuery();
			
			resultado = new Usuario();
			if (rs.next()){
				resultado.setCodigoEntero(rs.getInt("id"));
				resultado.setUsuario(UtilJdbc.obtenerCadena(rs, "usuario"));
				resultado.getRol().setCodigoEntero(rs.getInt("id_rol"));
				resultado.getRol().setNombre(UtilJdbc.obtenerCadena(rs,"nombre"));
				resultado.setNombres(UtilJdbc.obtenerCadena(rs,"nombres"));
				resultado.setApellidoPaterno(UtilJdbc.obtenerCadena(rs,"apepaterno"));
				resultado.setApellidoMaterno(UtilJdbc.obtenerCadena(rs,"apematerno"));
				String credencial = UtilJdbc.obtenerCadena(rs,"credencial");
				resultado.setEncontrado(UtilEncripta.desencriptaCadena(credencial).equals(usuario.getCredencial()));
				boolean caducaCredencial = UtilJdbc.obtenerBoolean(rs, "cambiarclave");
				Date fechaVctoCredencial = UtilJdbc.obtenerFecha(rs, "feccaducacredencial");
				
				resultado.setCredencialVencida(caducaCredencial);
				if (!caducaCredencial){
					if (fechaVctoCredencial != null){
						resultado.setCredencialVencida((new Date()).after(fechaVctoCredencial));
					}
					else {
						resultado.setCredencialVencida(true);
					}
				}
			}
		} catch (SQLException e) {
			resultado = null;
			throw new SQLException(e);
		} finally{
			try {
				if (rs != null){
					rs.close();
				}
				if (cs != null){
					cs.close();
				}
				if (conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null){
						conn.close();
					}
					throw new SQLException(e);
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}
		
		return resultado;
	}
	
	@Override
	public boolean cambiarClaveUsuario(Usuario usuario) throws SQLException, ErrorEncriptacionException {
		boolean resultado = false;
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{? = call seguridad.fn_cambiarclaveusuario2(?,?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, usuario.getCodigoEntero());
			cs.setString(i++, UtilEncripta.encriptaCadena(usuario.getCredencialNueva()));
			cs.execute();
			
			resultado = cs.getBoolean(1);
		} catch (SQLException e) {
			resultado = false;
			throw new SQLException(e);
		} finally{
			try {
				if (cs != null){
					cs.close();
				}
				if (conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null){
						conn.close();
					}
					throw new SQLException(e);
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}
		
		
		return resultado;
	}
	
	@Override
	public boolean actualizarClaveUsuario(Usuario usuario) throws SQLException, ErrorEncriptacionException {
		boolean resultado = false;
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{? = call seguridad.fn_actualizarclaveusuario(?,?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, usuario.getCodigoEntero());
			cs.setString(i++, UtilEncripta.encriptaCadena(usuario.getCredencialNueva()));
			cs.execute();
			
			resultado = cs.getBoolean(1);
		} catch (SQLException e) {
			resultado = false;
			throw new SQLException(e);
		} finally{
			try {
				if (cs != null){
					cs.close();
				}
				if (conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null){
						conn.close();
					}
					throw new SQLException(e);
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}
		
		
		return resultado;
	}
	
	@Override
	public List<Usuario> listarVendedores() throws SQLException {
		List<Usuario> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{? = call seguridad.fn_listarvendedores()}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.execute();
			
			rs = (ResultSet)cs.getObject(1);
			Usuario usuario = null;
			resultado = new ArrayList<Usuario>();
			while(rs.next()){
				usuario = new Usuario();
				usuario.setCodigoEntero(rs.getInt("id"));
				usuario.setUsuario(UtilJdbc.obtenerCadena(rs, "usuario"));
				usuario.setNombres(UtilJdbc.obtenerCadena(rs,"nombres"));
				usuario.setApellidoPaterno(UtilJdbc.obtenerCadena(rs,"apepaterno"));
				usuario.setApellidoMaterno(UtilJdbc.obtenerCadena(rs,"apematerno"));
				usuario.setVendedor(UtilJdbc.obtenerBoolean(rs, "vendedor"));
				usuario.setFechaNacimiento(UtilJdbc.obtenerFecha(rs, "fecnacimiento"));
				resultado.add(usuario);
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally{
			try {
				if (rs != null){
					rs.close();
				}
				if (cs != null){
					cs.close();
				}
				if (conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null){
						conn.close();
					}
					throw new SQLException(e);
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}
		
		return resultado;
	}
	
	@Override
	public boolean actualizarCredencialVencida(Usuario usuario) throws SQLException, ErrorEncriptacionException {
		boolean resultado = false;
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{? = call seguridad.fn_actualizarcredencialvencida(?,?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, usuario.getCodigoEntero());
			cs.setString(i++, UtilEncripta.encriptaCadena(usuario.getCredencialNueva()));
			cs.execute();
			
			resultado = cs.getBoolean(1);
		} catch (SQLException e) {
			resultado = false;
			throw new SQLException(e);
		} finally{
			try {
				if (cs != null){
					cs.close();
				}
				if (conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null){
						conn.close();
					}
					throw new SQLException(e);
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}
		
		
		return resultado;
	}
}

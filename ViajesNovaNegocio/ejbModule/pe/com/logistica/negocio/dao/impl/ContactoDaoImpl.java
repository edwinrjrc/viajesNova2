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
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.base.CorreoElectronico;
import pe.com.logistica.bean.base.Persona;
import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.negocio.dao.ContactoDao;
import pe.com.logistica.negocio.dao.TelefonoDao;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilJdbc;

/**
 * @author Edwin
 *
 */
public class ContactoDaoImpl implements ContactoDao {

	/**
	 * 
	 */
	public ContactoDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.ContactoDao#registrarContactoProveedor(pe.com.logistica.bean.negocio.Contacto)
	 */
	@Override
	public void registrarContactoProveedor(int idproveedor, Contacto contacto, Connection conexion)
			throws SQLException {
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresarcontactoproveedor(?,?,?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			if (idproveedor != 0){
				cs.setInt(i++, idproveedor);
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (contacto.getCodigoEntero() != null){
				cs.setInt(i++, contacto.getCodigoEntero());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (contacto.getArea().getCodigoEntero() != null){
				cs.setInt(i++, contacto.getArea().getCodigoEntero());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (StringUtils.isNotBlank(contacto.getAnexo())){
				cs.setString(i++, UtilJdbc.convertirMayuscula(contacto.getAnexo()));
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			
			cs.execute();
			
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally{
			try {
				if (cs != null){
					cs.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
	}

	@Override
	public List<Contacto> consultarContactoProveedor(int idproveedor) throws SQLException {
		List<Contacto> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select *" +
				" from negocio.vw_contactoproveedor where idproveedor = ?";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			cs.setInt(1, idproveedor);
			
			rs = cs.executeQuery();
			
			resultado = new ArrayList<Contacto>();
			Contacto contacto = null;
			TelefonoDao telefonoDao = new TelefonoDaoImpl();
			while (rs.next()){
				contacto = new Contacto();
				contacto.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				contacto.setNombres(UtilJdbc.obtenerCadena(rs, "nombres"));
				contacto.setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno"));
				contacto.setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno"));
				contacto.getGenero().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idgenero"));
				contacto.getEstadoCivil().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idestadocivil"));
				contacto.getDocumentoIdentidad().getTipoDocumento().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtipodocumento"));
				contacto.getDocumentoIdentidad().setNumeroDocumento(UtilJdbc.obtenerCadena(rs, "numerodocumento"));
				contacto.setUsuarioCreacion(UtilJdbc.obtenerCadena(rs, "usuariocreacion"));
				contacto.setFechaCreacion(UtilJdbc.obtenerFecha(rs, "fechacreacion"));
				contacto.setIpCreacion(UtilJdbc.obtenerCadena(rs, "ipcreacion"));
				contacto.getArea().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idarea"));
				contacto.getArea().setNombre(UtilJdbc.obtenerCadena(rs, "nombre"));
				contacto.setAnexo(UtilJdbc.obtenerCadena(rs, "anexo"));
				contacto.setListaTelefonos(telefonoDao.consultarTelefonoContacto(UtilJdbc.obtenerNumero(contacto.getCodigoEntero()), conn));
				contacto.setListaCorreos(consultarCorreos(UtilJdbc.obtenerNumero(contacto.getCodigoEntero()), conn));
				resultado.add(contacto);
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
	public boolean eliminarTelefonoContacto(Contacto contacto, Connection conexion)
			throws SQLException {
		boolean resultado = false;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_eliminartelefonoscontacto(?,?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, contacto.getCodigoEntero().intValue());
			cs.setString(i++, UtilJdbc.convertirMayuscula(contacto.getUsuarioModificacion()));
			cs.setString(i++, contacto.getIpModificacion());
			
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
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		return resultado;
	}
	
	@Override
	public boolean eliminarContacto(Contacto contacto, Connection conexion)
			throws SQLException {
		boolean resultado = false;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_eliminarpersona(?,?,?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, contacto.getCodigoEntero().intValue());
			cs.setInt(i++, contacto.getTipoPersona());
			cs.setString(i++, UtilJdbc.convertirMayuscula(contacto.getUsuarioModificacion()));
			cs.setString(i++, contacto.getIpModificacion());
			
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
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		return resultado;
	}
	
	@Override
	public boolean eliminarContactoProveedor(Persona proveedor, Connection conexion)
			throws SQLException {
		boolean resultado = false;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_eliminarcontactoproveedor(?,?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, proveedor.getCodigoEntero().intValue());
			cs.setString(i++, UtilJdbc.convertirMayuscula(proveedor.getUsuarioModificacion()));
			cs.setString(i++, proveedor.getIpModificacion());
			
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
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		return resultado;
	}
	
	@Override
	public boolean ingresarCorreoElectronico(Contacto contacto, Connection conexion)
			throws SQLException {
		boolean resultado = false;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresarcorreoelectronico(?,?,?,?,?) }";
		
		try {
			
			for(CorreoElectronico correo : contacto.getListaCorreos()){
				cs = conexion.prepareCall(sql);
				int i=1;
				cs.registerOutParameter(i++, Types.INTEGER);
				cs.setString(i++, UtilJdbc.convertirMayuscula(correo.getDireccion()));
				cs.setInt(i++, contacto.getCodigoEntero().intValue());
				cs.setBoolean(i++, correo.isRecibirPromociones());
				cs.setString(i++, UtilJdbc.convertirMayuscula(contacto.getUsuarioCreacion()));
				cs.setString(i++, contacto.getIpCreacion());
				
				cs.execute();
				cs.close();
			}
			
		} catch (SQLException e) {
			resultado = false;
			throw new SQLException(e);
		} finally{
			try {
				if (cs != null){
					cs.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		return resultado;
	}
	
	@Override
	public boolean eliminarCorreosContacto(Contacto contacto, Connection conexion)
			throws SQLException {
		boolean resultado = false;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_eliminarcorreoscontacto(?,?,?) }";
		
		try {
			
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.INTEGER);
			cs.setInt(i++, contacto.getCodigoEntero().intValue());
			cs.setString(i++, UtilJdbc.convertirMayuscula(contacto.getUsuarioModificacion()));
			cs.setString(i++, contacto.getIpModificacion());
			
			cs.execute();
			
		} catch (SQLException e) {
			resultado = false;
			throw new SQLException(e);
		} finally{
			try {
				if (cs != null){
					cs.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		return resultado;
	}
	
	@Override
	public List<CorreoElectronico> consultarCorreos(int idcontacto,
			Connection conn) throws SQLException {
		List<CorreoElectronico> resultado = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select * " +
				" from negocio.vw_consultacorreocontacto where idpersona = ?";

		try {
			cs = conn.prepareCall(sql);
			cs.setInt(1, idcontacto);
			rs = cs.executeQuery();
			
			resultado = new ArrayList<CorreoElectronico>();
			CorreoElectronico correoElectronico = null;
			while (rs.next()) {
				correoElectronico = new CorreoElectronico();
				correoElectronico.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				correoElectronico.setDireccion(UtilJdbc.obtenerCadena(rs, "correo"));
				correoElectronico.setRecibirPromociones(UtilJdbc.obtenerBoolean(rs, "recibirPromociones"));
				resultado.add(correoElectronico);
			}
			
		} catch (SQLException e) {
			resultado = null;
			throw new SQLException(e);
		} finally {
			try {
				if (rs != null){
					rs.close();
				}
				if (cs != null){
					cs.close();
				}
			} catch (SQLException e) {
				try {
					if (cs != null){
						cs.close();
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
	public List<Contacto> listarContactosXPersona(int idpersona)
			throws SQLException {
		List<Contacto> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_consultarcontactoxpersona(?) }";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			cs.registerOutParameter(1, Types.OTHER);
			cs.setInt(2, idpersona);
			cs.execute();
			
			rs = (ResultSet)cs.getObject(1);
			resultado = new ArrayList<Contacto>();
			Contacto contacto = null;
			while (rs.next()){
				contacto = new Contacto();
				contacto.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				contacto.setNombres(UtilJdbc.obtenerCadena(rs, "nombres"));
				contacto.setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno"));
				contacto.setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno"));
				contacto.getGenero().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idgenero"));
				contacto.getEstadoCivil().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idestadocivil"));
				contacto.getDocumentoIdentidad().getTipoDocumento().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtipodocumento"));
				contacto.getDocumentoIdentidad().setNumeroDocumento(UtilJdbc.obtenerCadena(rs, "numerodocumento"));
				resultado.add(contacto);
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
	public List<Contacto> listarContactosXPersona(int idpersona, Connection conn)
			throws SQLException {
		List<Contacto> resultado = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_consultarcontactoxpersona(?) }";

		try {
			cs = conn.prepareCall(sql);
			cs.registerOutParameter(1, Types.OTHER);
			cs.setInt(2, idpersona);
			cs.execute();
			
			rs = (ResultSet)cs.getObject(1);
			resultado = new ArrayList<Contacto>();
			Contacto contacto = null;
			while (rs.next()){
				contacto = new Contacto();
				contacto.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				contacto.setNombres(UtilJdbc.obtenerCadena(rs, "nombres"));
				contacto.setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno"));
				contacto.setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno"));
				contacto.getGenero().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idgenero"));
				contacto.getEstadoCivil().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idestadocivil"));
				contacto.getDocumentoIdentidad().getTipoDocumento().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtipodocumento"));
				contacto.getDocumentoIdentidad().setNumeroDocumento(UtilJdbc.obtenerCadena(rs, "numerodocumento"));
				resultado.add(contacto);
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
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		
		return resultado;
	}
}

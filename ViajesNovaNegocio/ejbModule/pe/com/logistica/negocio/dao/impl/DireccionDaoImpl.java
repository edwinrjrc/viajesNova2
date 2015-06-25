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

import pe.com.logistica.bean.base.Persona;
import pe.com.logistica.bean.negocio.Direccion;
import pe.com.logistica.negocio.dao.DireccionDao;
import pe.com.logistica.negocio.dao.TelefonoDao;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilJdbc;

/**
 * @author Edwin
 *
 */
public class DireccionDaoImpl implements DireccionDao {

	@Override
	public int registrarDireccion(Direccion direccion, Connection conexion) throws SQLException {
		int resultado = 0;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresardireccion(?,?,?,?,?,?,?,?,?,?,?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.INTEGER);
			cs.setInt(i++, direccion.getVia().getCodigoEntero());
			if (StringUtils.isNotBlank(direccion.getNombreVia())){
				cs.setString(i++, UtilJdbc.convertirMayuscula(direccion.getNombreVia()));
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(direccion.getNumero())){
				cs.setString(i++, direccion.getNumero());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(direccion.getInterior())){
				cs.setString(i++, direccion.getInterior());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(direccion.getManzana())){
				cs.setString(i++, UtilJdbc.convertirMayuscula(direccion.getManzana()));
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(direccion.getLote())){
				cs.setString(i++, UtilJdbc.convertirMayuscula(direccion.getLote()));
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			cs.setString(i++, direccion.isPrincipal()?"S":"N");
			if (StringUtils.isNotBlank(direccion.getUbigeo().getCodigoCadena())){
				cs.setString(i++, direccion.getUbigeo().getCodigoCadena());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(direccion.getUsuarioCreacion())){
				cs.setString(i++, UtilJdbc.convertirMayuscula(direccion.getUsuarioCreacion()));
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(direccion.getIpCreacion())){
				cs.setString(i++, direccion.getIpCreacion());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(direccion.getObservaciones())){
				cs.setString(i++, UtilJdbc.convertirMayuscula(direccion.getObservaciones()));
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(direccion.getReferencia())){
				cs.setString(i++, UtilJdbc.convertirMayuscula(direccion.getReferencia()));
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			
			cs.execute();
			resultado = cs.getInt(1);
		} catch (SQLException e) {
			resultado = 0;
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
	public void registrarPersonaDireccion(int idPersona, int idTipoPersona, int idDireccion, Connection conexion) throws SQLException {
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresarpersonadireccion(?,?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, idPersona);
			cs.setInt(i++, idTipoPersona);
			cs.setInt(i++, idDireccion);
			
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
	public List<Direccion> consultarDireccionProveedor(int idProveedor)
			throws SQLException {
		List<Direccion> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select * " +
				" from negocio.vw_consultadireccionproveedor where idpersona = ?";
		
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			cs.setInt(1, idProveedor);
			rs = cs.executeQuery();
			
			resultado = new ArrayList<Direccion>();
			Direccion direccion = null;
			TelefonoDao telefonoDao = new TelefonoDaoImpl();
			while (rs.next()) {
				direccion = new Direccion();
				direccion.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				direccion.getVia().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idvia"));
				direccion.setNombreVia(UtilJdbc.obtenerCadena(rs, "nombrevia"));
				direccion.setNumero(UtilJdbc.obtenerCadena(rs, "numero"));
				direccion.setInterior(UtilJdbc.obtenerCadena(rs, "interior"));
				direccion.setManzana(UtilJdbc.obtenerCadena(rs, "manzana"));
				direccion.setLote(UtilJdbc.obtenerCadena(rs, "lote"));
				direccion.setPrincipal(UtilJdbc.convertirBooleanSiNo(rs, "principal"));
				direccion.getUbigeo().setCodigoCadena(UtilJdbc.obtenerCadena(rs, "idubigeo"));
				direccion.getUbigeo().getDepartamento().setCodigoCadena(UtilJdbc.obtenerCadena(rs, "iddepartamento"));
				direccion.getUbigeo().getDepartamento().setNombre(UtilJdbc.obtenerCadena(rs, "departamento"));
				direccion.getUbigeo().getProvincia().setCodigoCadena(UtilJdbc.obtenerCadena(rs, "idprovincia"));
				direccion.getUbigeo().getProvincia().setNombre(UtilJdbc.obtenerCadena(rs, "provincia"));
				direccion.getUbigeo().getDistrito().setCodigoCadena(UtilJdbc.obtenerCadena(rs, "iddistrito"));
				direccion.getUbigeo().getDistrito().setNombre(UtilJdbc.obtenerCadena(rs, "distrito"));
				direccion.setUsuarioCreacion(UtilJdbc.obtenerCadena(rs, "usuariocreacion"));
				direccion.setFechaCreacion(UtilJdbc.obtenerFecha(rs, "fechacreacion"));
				direccion.setIpCreacion(UtilJdbc.obtenerCadena(rs, "ipcreacion"));
				direccion.setObservaciones(UtilJdbc.obtenerCadena(rs, "observacion"));
				direccion.setReferencia(UtilJdbc.obtenerCadena(rs, "referencia"));
				direccion.setTelefonos(telefonoDao.consultarTelefonoDireccion(direccion.getCodigoEntero().intValue(), conn));
				resultado.add(direccion);
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
	public int actualizarDireccion(Direccion direccion, Connection conexion)
			throws SQLException {
		int resultado = 0;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_actualizardireccion(?,?,?,?,?,?,?,?,?,?,?,?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.INTEGER);
			cs.setInt(i++, UtilJdbc.obtenerNumero(direccion.getCodigoEntero()));
			cs.setInt(i++, direccion.getVia().getCodigoEntero());
			if (StringUtils.isNotBlank(direccion.getNombreVia())){
				cs.setString(i++, UtilJdbc.convertirMayuscula(direccion.getNombreVia()));
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(direccion.getNumero())){
				cs.setString(i++, direccion.getNumero());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(direccion.getInterior())){
				cs.setString(i++, direccion.getInterior());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(direccion.getManzana())){
				cs.setString(i++, UtilJdbc.convertirMayuscula(direccion.getManzana()));
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(direccion.getLote())){
				cs.setString(i++, direccion.getLote());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			cs.setString(i++, direccion.isPrincipal()?"S":"N");
			if (StringUtils.isNotBlank(direccion.getUbigeo().getCodigoCadena())){
				cs.setString(i++, direccion.getUbigeo().getCodigoCadena());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(direccion.getUsuarioModificacion())){
				cs.setString(i++, UtilJdbc.convertirMayuscula(direccion.getUsuarioModificacion()));
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(direccion.getIpModificacion())){
				cs.setString(i++, direccion.getIpModificacion());	
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(direccion.getObservaciones())){
				cs.setString(i++, UtilJdbc.convertirMayuscula(direccion.getObservaciones()));
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(direccion.getReferencia())){
				cs.setString(i++, UtilJdbc.convertirMayuscula(direccion.getReferencia()));
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			
			cs.execute();
			resultado = cs.getInt(1);
		} catch (SQLException e) {
			resultado = 0;
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
	public boolean eliminarTelefonoDireccion(Direccion direccion, Connection conexion)
			throws SQLException {
		boolean resultado = false;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_eliminartelefonosdireccion(?,?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, UtilJdbc.obtenerNumero(direccion.getCodigoEntero()));
			cs.setString(i++, UtilJdbc.convertirMayuscula(direccion.getUsuarioModificacion()));
			cs.setString(i++, direccion.getIpModificacion());
			
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
	public boolean eliminarDireccionPersona(Persona persona, Connection conexion)
			throws SQLException {
		boolean resultado = false;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_eliminardirecciones(?,?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, persona.getCodigoEntero().intValue());
			cs.setString(i++, UtilJdbc.convertirMayuscula(persona.getUsuarioModificacion()));
			cs.setString(i++, persona.getIpModificacion());
			
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
	public List<Direccion> consultarDireccionPersona(int idPersona)
			throws SQLException {
		List<Direccion> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_direccionesxpersona(?) }";
		
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			cs.registerOutParameter(1, Types.OTHER);
			cs.setInt(2, idPersona);
			cs.execute();
			
			rs = (ResultSet)cs.getObject(1);
			resultado = new ArrayList<Direccion>();
			Direccion direccion = null;
			TelefonoDao telefonoDao = new TelefonoDaoImpl();
			while (rs.next()) {
				direccion = new Direccion();
				direccion.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				direccion.getVia().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idvia"));
				direccion.setNombreVia(UtilJdbc.obtenerCadena(rs, "nombrevia"));
				direccion.setNumero(UtilJdbc.obtenerCadena(rs, "numero"));
				direccion.setInterior(UtilJdbc.obtenerCadena(rs, "interior"));
				direccion.setManzana(UtilJdbc.obtenerCadena(rs, "manzana"));
				direccion.setLote(UtilJdbc.obtenerCadena(rs, "lote"));
				direccion.setPrincipal(UtilJdbc.convertirBooleanSiNo(rs, "principal"));
				direccion.getUbigeo().setCodigoCadena(UtilJdbc.obtenerCadena(rs, "idubigeo"));
				direccion.getUbigeo().getDepartamento().setCodigoCadena(UtilJdbc.obtenerCadena(rs, "iddepartamento"));
				direccion.getUbigeo().getDepartamento().setNombre(UtilJdbc.obtenerCadena(rs, "departamento"));
				direccion.getUbigeo().getProvincia().setCodigoCadena(UtilJdbc.obtenerCadena(rs, "idprovincia"));
				direccion.getUbigeo().getProvincia().setNombre(UtilJdbc.obtenerCadena(rs, "provincia"));
				direccion.getUbigeo().getDistrito().setCodigoCadena(UtilJdbc.obtenerCadena(rs, "iddistrito"));
				direccion.getUbigeo().getDistrito().setNombre(UtilJdbc.obtenerCadena(rs, "distrito"));
				direccion.setUsuarioCreacion(UtilJdbc.obtenerCadena(rs, "usuariocreacion"));
				direccion.setFechaCreacion(UtilJdbc.obtenerFecha(rs, "fechacreacion"));
				direccion.setIpCreacion(UtilJdbc.obtenerCadena(rs, "ipcreacion"));
				direccion.setObservaciones(UtilJdbc.obtenerCadena(rs, "observacion"));
				direccion.setReferencia(UtilJdbc.obtenerCadena(rs, "referencia"));
				direccion.setTelefonos(telefonoDao.consultarTelefonoDireccion(direccion.getCodigoEntero().intValue(), conn));
				resultado.add(direccion);
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
	public List<Direccion> consultarDireccionPersona(int idPersona, Connection conn)
			throws SQLException {
		List<Direccion> resultado = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_direccionesxpersona(?) }";
		
		
		try {
			cs = conn.prepareCall(sql);
			cs.registerOutParameter(1, Types.OTHER);
			cs.setInt(2, idPersona);
			cs.execute();
			
			rs = (ResultSet)cs.getObject(1);
			resultado = new ArrayList<Direccion>();
			Direccion direccion = null;
			TelefonoDao telefonoDao = new TelefonoDaoImpl();
			while (rs.next()) {
				direccion = new Direccion();
				direccion.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				direccion.getVia().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idvia"));
				direccion.setNombreVia(UtilJdbc.obtenerCadena(rs, "nombrevia"));
				direccion.setNumero(UtilJdbc.obtenerCadena(rs, "numero"));
				direccion.setInterior(UtilJdbc.obtenerCadena(rs, "interior"));
				direccion.setManzana(UtilJdbc.obtenerCadena(rs, "manzana"));
				direccion.setLote(UtilJdbc.obtenerCadena(rs, "lote"));
				direccion.setPrincipal(UtilJdbc.convertirBooleanSiNo(rs, "principal"));
				direccion.getUbigeo().setCodigoCadena(UtilJdbc.obtenerCadena(rs, "idubigeo"));
				direccion.getUbigeo().getDepartamento().setCodigoCadena(UtilJdbc.obtenerCadena(rs, "iddepartamento"));
				direccion.getUbigeo().getDepartamento().setNombre(UtilJdbc.obtenerCadena(rs, "departamento"));
				direccion.getUbigeo().getProvincia().setCodigoCadena(UtilJdbc.obtenerCadena(rs, "idprovincia"));
				direccion.getUbigeo().getProvincia().setNombre(UtilJdbc.obtenerCadena(rs, "provincia"));
				direccion.getUbigeo().getDistrito().setCodigoCadena(UtilJdbc.obtenerCadena(rs, "iddistrito"));
				direccion.getUbigeo().getDistrito().setNombre(UtilJdbc.obtenerCadena(rs, "distrito"));
				direccion.setUsuarioCreacion(UtilJdbc.obtenerCadena(rs, "usuariocreacion"));
				direccion.setFechaCreacion(UtilJdbc.obtenerFecha(rs, "fechacreacion"));
				direccion.setIpCreacion(UtilJdbc.obtenerCadena(rs, "ipcreacion"));
				direccion.setObservaciones(UtilJdbc.obtenerCadena(rs, "observacion"));
				direccion.setReferencia(UtilJdbc.obtenerCadena(rs, "referencia"));
				direccion.setTelefonos(telefonoDao.consultarTelefonoDireccion(direccion.getCodigoEntero().intValue(), conn));
				resultado.add(direccion);
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
				throw new SQLException(e);

			}
		}
		
		return resultado;
	}
}

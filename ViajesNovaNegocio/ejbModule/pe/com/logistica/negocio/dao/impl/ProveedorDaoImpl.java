/**
 * 
 */
package pe.com.logistica.negocio.dao.impl;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.bean.negocio.CuentaBancaria;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.bean.negocio.ServicioProveedor;
import pe.com.logistica.bean.negocio.Telefono;
import pe.com.logistica.negocio.dao.ProveedorDao;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilEjb;
import pe.com.logistica.negocio.util.UtilJdbc;

/**
 * @author Edwin
 *
 */
public class ProveedorDaoImpl implements ProveedorDao {

	private final static Logger logger = Logger.getLogger(ProveedorDaoImpl.class);
	/**
	 * 
	 */
	public ProveedorDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.ProveedorDao#registroProveedor(pe.com.logistica.bean.negocio.Proveedor)
	 */
	@Override
	public void registroProveedor(Proveedor proveedor, Connection conexion) throws SQLException {
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresarpersonaproveedor(?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, proveedor.getCodigoEntero());
			cs.setInt(i++, proveedor.getRubro().getCodigoEntero());
			
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
	public List<Proveedor> listarProveedor(Proveedor proveedor)
			throws SQLException {
		List<Proveedor> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select * " +
				" from negocio.vw_proveedor";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			rs = cs.executeQuery();
			
			resultado = new ArrayList<Proveedor>();
			Proveedor proveedor2 = null;
			while (rs.next()){
				proveedor2 = new Proveedor();
				proveedor2.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idproveedor"));
				proveedor2.getDocumentoIdentidad().getTipoDocumento().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtipodocumento"));
				proveedor2.getDocumentoIdentidad().getTipoDocumento().setNombre(UtilJdbc.obtenerCadena(rs, "nombretipodocumento"));
				proveedor2.getDocumentoIdentidad().setNumeroDocumento(UtilJdbc.obtenerCadena(rs, "numerodocumento"));
				proveedor2.setNombres(UtilJdbc.obtenerCadena(rs, "nombres"));
				proveedor2.setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno"));
				proveedor2.setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno"));
				proveedor2.getRubro().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idrubro"));
				proveedor2.getRubro().setNombre(UtilJdbc.obtenerCadena(rs, "nombrerubro"));
				proveedor2.getDireccion().getVia().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idvia"));
				proveedor2.getDireccion().getVia().setNombre(UtilJdbc.obtenerCadena(rs, "nombretipovia"));
				proveedor2.getDireccion().setNombreVia(UtilJdbc.obtenerCadena(rs, "nombrevia"));
				proveedor2.getDireccion().setNumero(UtilJdbc.obtenerCadena(rs, "numero"));
				proveedor2.getDireccion().setInterior(UtilJdbc.obtenerCadena(rs, "interior"));
				proveedor2.getDireccion().setManzana(UtilJdbc.obtenerCadena(rs, "manzana"));
				proveedor2.getDireccion().setLote(UtilJdbc.obtenerCadena(rs, "lote"));
				Telefono teldireccion = new Telefono();
				teldireccion.setNumeroTelefono(UtilJdbc.obtenerCadena(rs, "teledireccion"));
				proveedor2.getDireccion().getTelefonos().add(teldireccion);
				resultado.add(proveedor2);
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
	public List<Proveedor> buscarProveedor(Proveedor proveedor)
			throws SQLException {
		List<Proveedor> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select * " +
				" from negocio.vw_proveedor";
		
		if (proveedor.getDocumentoIdentidad().getTipoDocumento().getCodigoEntero()!=null && StringUtils.isNotBlank(proveedor.getDocumentoIdentidad().getNumeroDocumento())){
			sql = sql + " where idtipodocumento = "+proveedor.getDocumentoIdentidad().getTipoDocumento().getCodigoEntero().intValue();
			sql = sql + " and numerodocumento = '"+proveedor.getDocumentoIdentidad().getNumeroDocumento()+"'";
			if (StringUtils.isNotBlank(proveedor.getNombres())){
				sql = sql + " and upper(nombres) like '%"+UtilJdbc.convertirMayuscula(proveedor.getNombres())+"%'";
			}
		}
		else if (StringUtils.isNotBlank(proveedor.getNombres())){
			sql = sql + " where upper(nombres) like '%"+UtilJdbc.convertirMayuscula(proveedor.getNombres())+"%'";
		}
		
		try {
			conn = UtilConexion.obtenerConexion();
			System.out.println("SQL::"+sql);
			cs = conn.prepareCall(sql);
			rs = cs.executeQuery();
			
			resultado = new ArrayList<Proveedor>();
			Proveedor proveedor2 = null;
			while (rs.next()){
				proveedor2 = new Proveedor();
				proveedor2.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idproveedor"));
				proveedor2.getDocumentoIdentidad().getTipoDocumento().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtipodocumento"));
				proveedor2.getDocumentoIdentidad().getTipoDocumento().setNombre(UtilJdbc.obtenerCadena(rs, "nombretipodocumento"));
				proveedor2.getDocumentoIdentidad().setNumeroDocumento(UtilJdbc.obtenerCadena(rs, "numerodocumento"));
				proveedor2.setNombres(UtilJdbc.obtenerCadena(rs, "nombres"));
				proveedor2.setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno"));
				proveedor2.setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno"));
				proveedor2.getRubro().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idrubro"));
				proveedor2.getRubro().setNombre(UtilJdbc.obtenerCadena(rs, "nombrerubro"));
				proveedor2.getDireccion().getVia().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idvia"));
				proveedor2.getDireccion().getVia().setNombre(UtilJdbc.obtenerCadena(rs, "nombretipovia"));
				proveedor2.getDireccion().setNombreVia(UtilJdbc.obtenerCadena(rs, "nombrevia"));
				proveedor2.getDireccion().setNumero(UtilJdbc.obtenerCadena(rs, "numero"));
				proveedor2.getDireccion().setInterior(UtilJdbc.obtenerCadena(rs, "interior"));
				proveedor2.getDireccion().setManzana(UtilJdbc.obtenerCadena(rs, "manzana"));
				proveedor2.getDireccion().setLote(UtilJdbc.obtenerCadena(rs, "lote"));
				Telefono teldireccion = new Telefono();
				teldireccion.setNumeroTelefono(UtilJdbc.obtenerCadena(rs, "teledireccion"));
				proveedor2.getDireccion().getTelefonos().add(teldireccion);
				resultado.add(proveedor2);
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
	public Proveedor consultarProveedor(int idProveedor) throws SQLException {
		Proveedor resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select * " +
				" from negocio.vw_consultaproveedor where id = ?";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			cs.setInt(1, idProveedor);
			rs = cs.executeQuery();
			
			if (rs.next()){
				resultado = new Proveedor();
				resultado.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				resultado.setNombres(UtilJdbc.obtenerCadena(rs, "nombres"));
				resultado.setRazonSocial(resultado.getNombres());
				resultado.setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno"));
				resultado.setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno"));
				resultado.getGenero().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idgenero"));
				resultado.getEstadoCivil().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idestadocivil"));
				resultado.getDocumentoIdentidad().getTipoDocumento().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtipodocumento"));
				resultado.getDocumentoIdentidad().setNumeroDocumento(UtilJdbc.obtenerCadena(rs, "numerodocumento"));
				resultado.getRubro().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idrubro"));
				resultado.setUsuarioCreacion(UtilJdbc.obtenerCadena(rs, "usuariocreacion"));
				resultado.setFechaCreacion(UtilJdbc.obtenerFecha(rs, "fechacreacion"));
				resultado.setIpCreacion(UtilJdbc.obtenerCadena(rs, "ipcreacion"));
				resultado.getTipoProveedor().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtipoproveedor"));
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
	public Proveedor consultarProveedor(int idProveedor, Connection conn) throws SQLException {
		Proveedor resultado = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select * " +
				" from negocio.vw_consultaproveedor where id = ?";

		try {
			cs = conn.prepareCall(sql);
			cs.setInt(1, idProveedor);
			rs = cs.executeQuery();
			
			if (rs.next()){
				resultado = new Proveedor();
				resultado.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				resultado.setNombres(UtilJdbc.obtenerCadena(rs, "nombres"));
				resultado.setRazonSocial(resultado.getNombres());
				resultado.setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno"));
				resultado.setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno"));
				resultado.getGenero().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idgenero"));
				resultado.getEstadoCivil().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idestadocivil"));
				resultado.getDocumentoIdentidad().getTipoDocumento().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtipodocumento"));
				resultado.getDocumentoIdentidad().setNumeroDocumento(UtilJdbc.obtenerCadena(rs, "numerodocumento"));
				resultado.getRubro().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idrubro"));
				resultado.setUsuarioCreacion(UtilJdbc.obtenerCadena(rs, "usuariocreacion"));
				resultado.setFechaCreacion(UtilJdbc.obtenerFecha(rs, "fechacreacion"));
				resultado.setIpCreacion(UtilJdbc.obtenerCadena(rs, "ipcreacion"));
				resultado.getTipoProveedor().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtipoproveedor"));
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
	public Contacto consultarContacto(int idPersona) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actualizarProveedor(Proveedor proveedor, Connection conexion) throws SQLException {
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_actualizarpersonaproveedor(?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, proveedor.getCodigoEntero());
			cs.setInt(i++, proveedor.getRubro().getCodigoEntero());
			
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
	public boolean ingresarServicioProveedor(Integer idproveedor, ServicioProveedor servicio, Connection conn) throws SQLException {
		boolean resultado = false;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresarservicioproveedor(?,?,?,?,?,?,?) }";
		
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, idproveedor);
			cs.setInt(i++, servicio.getTipoServicio().getCodigoEntero().intValue());
			if (servicio.getProveedorServicio().getCodigoEntero()!=null && servicio.getProveedorServicio().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicio.getProveedorServicio().getCodigoEntero().intValue());
			}
			else {
				cs.setNull(i++, Types.INTEGER);
			}
			cs.setBigDecimal(i++, servicio.getPorcentajeComision());
			if (servicio.getPorcenComInternacional() !=null || servicio.getPorcenComInternacional().compareTo(BigDecimal.ZERO)==1){
				cs.setBigDecimal(i++, servicio.getPorcenComInternacional());
			}
			else {
				cs.setNull(i++, Types.DECIMAL);
			}
			cs.setString(i++, servicio.getUsuarioCreacion());
			cs.setString(i++, servicio.getIpCreacion());
			cs.execute();
			
			resultado = cs.getBoolean(1);
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
		
		return resultado;
	}
	
	@Override
	public boolean actualizarServicioProveedor(Integer idproveedor, ServicioProveedor servicio, Connection conn) throws SQLException {
		boolean resultado = false;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_actualizarproveedorservicio(?,?,?,?,?,?,?) }";
		
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, idproveedor);
			cs.setInt(i++, servicio.getTipoServicio().getCodigoEntero().intValue());
			cs.setInt(i++, servicio.getProveedorServicio().getCodigoEntero().intValue());
			cs.setBigDecimal(i++, servicio.getPorcentajeComision());
			if (servicio.getPorcenComInternacional() !=null || servicio.getPorcenComInternacional().compareTo(BigDecimal.ZERO)==1){
				cs.setBigDecimal(i++, servicio.getPorcenComInternacional());
			}
			else {
				cs.setNull(i++, Types.DECIMAL);
			}
			cs.setString(i++, servicio.getUsuarioModificacion());
			cs.setString(i++, servicio.getIpModificacion());
			cs.execute();
			
			resultado = cs.getBoolean(1);
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
		
		return resultado;
	}
	
	@Override
	public List<ServicioProveedor> consultarServicioProveedor(int idProveedor) throws SQLException {
		List<ServicioProveedor> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_consultarproveedorservicio(?) }";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			cs.registerOutParameter(1, Types.OTHER);
			cs.setInt(2, idProveedor);
			cs.execute();
			
			rs = (ResultSet)cs.getObject(1);
			resultado = new ArrayList<ServicioProveedor>();
			ServicioProveedor servicioProveedor = null;
			while (rs.next()){
				servicioProveedor = new ServicioProveedor();
				servicioProveedor.getProveedor().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idproveedor"));
				servicioProveedor.getTipoServicio().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtiposervicio"));
				servicioProveedor.getProveedorServicio().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idproveedorservicio"));
				servicioProveedor.setPorcentajeComision(UtilJdbc.obtenerBigDecimal(rs, "porcencomnacional"));
				servicioProveedor.setPorcenComInternacional(UtilJdbc.obtenerBigDecimal(rs, "porcencominternacional"));
				resultado.add(servicioProveedor);
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
	public List<ServicioProveedor> consultarServicioProveedor(int idProveedor, Connection conn) throws SQLException {
		List<ServicioProveedor> resultado = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_consultarproveedorservicio(?) }";

		try {
			cs = conn.prepareCall(sql);
			cs.setInt(1, idProveedor);
			rs = cs.executeQuery();
			
			resultado = new ArrayList<ServicioProveedor>();
			ServicioProveedor servicioProveedor = null;
			while (rs.next()){
				servicioProveedor = new ServicioProveedor();
				servicioProveedor.getProveedor().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idproveedor"));
				servicioProveedor.getTipoServicio().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idservicio"));
				servicioProveedor.setPorcentajeComision(UtilJdbc.obtenerBigDecimal(rs, "porcencomision"));
				resultado.add(servicioProveedor);
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
	
	@Override
	public void registroProveedorTipo(Proveedor proveedor, Connection conexion) throws SQLException {
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresarproveedortipo(?,?,?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, proveedor.getCodigoEntero());
			cs.setInt(i++, proveedor.getTipoProveedor().getCodigoEntero().intValue());
			cs.setString(i++, proveedor.getUsuarioCreacion());
			cs.setString(i++, proveedor.getIpCreacion());
			
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
	public void actualizarProveedorTipo(Proveedor proveedor, Connection conexion) throws SQLException {
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_actualizarproveedortipo(?,?,?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, proveedor.getCodigoEntero());
			cs.setInt(i++, proveedor.getTipoProveedor().getCodigoEntero().intValue());
			cs.setString(i++, proveedor.getUsuarioModificacion());
			cs.setString(i++, proveedor.getIpModificacion());
			
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
	public List<Proveedor> listarComboProveedorTipo(BaseVO tipoProveedor)
			throws SQLException {
		List<Proveedor> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_comboproveedorestipo(?) }";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			cs.registerOutParameter(1, Types.OTHER);
			cs.setInt(2, tipoProveedor.getCodigoEntero().intValue());
			cs.execute();
			rs = (ResultSet)cs.getObject(1);
			
			resultado = new ArrayList<Proveedor>();
			Proveedor proveedor = null;
			while (rs.next()){
				proveedor = new Proveedor();
				proveedor.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				proveedor.setNombres(UtilJdbc.obtenerCadena(rs, "nombres"));
				proveedor.setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno"));
				proveedor.setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno"));
				resultado.add(proveedor);
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
				throw new SQLException(e);
			}
		}
		
		return resultado;
	}
	
	@Override
	public boolean ingresarCuentaBancaria(Integer idProveedor, CuentaBancaria cuenta, Connection conexion)throws SQLException {
		CallableStatement cs = null;
		String sql = UtilEjb.generaSentenciaFuncion("negocio.fn_ingresarcuentabancariaproveedor", 8);
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setString(i++, cuenta.getNombreCuenta());
			cs.setString(i++, cuenta.getNumeroCuenta());
			cs.setInt(i++, cuenta.getTipoCuenta().getCodigoEntero().intValue());
			cs.setInt(i++, cuenta.getBanco().getCodigoEntero().intValue());
			cs.setInt(i++, cuenta.getMoneda().getCodigoEntero().intValue());
			cs.setInt(i++, idProveedor.intValue());
			cs.setString(i++, cuenta.getUsuarioCreacion());
			cs.setString(i++, cuenta.getIpCreacion());
			
			cs.execute();
			
			return true;
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
	public boolean validarEliminarCuentaBancaria(Integer idCuenta, Integer idProveedor, Connection conn) throws SQLException{
		CallableStatement cs = null;
		String sql = "";
		
		try {
			sql = "{ ? = call negocio.fn_validareliminarcuentasproveedor(?,?) }";
			int i=1;
			cs = conn.prepareCall(sql);
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, idCuenta.intValue());
			cs.setInt(i++, idProveedor.intValue());
			cs.execute();
			
			return cs.getBoolean(1);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new SQLException(e);
		} finally{
			if (cs != null){
				cs.close();
			}
		}
	}
	
	@Override
	public boolean eliminarCuentasBancarias(Proveedor proveedor, Connection conn) throws SQLException{
		CallableStatement cs = null;
		String sql = "";
		
		try {
			sql = "{ ? = call negocio.fn_eliminarcuentasproveedor(?,?,?) }";
			int i=1;
			cs = conn.prepareCall(sql);
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, proveedor.getCodigoEntero().intValue());
			cs.setString(i++, proveedor.getUsuarioCreacion());
			cs.setString(i++, proveedor.getIpCreacion());
			cs.execute();
			
			return cs.getBoolean(1);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new SQLException(e);
		} finally{
			if (cs != null){
				cs.close();
			}
		}
	}
	
	@Override
	public boolean actualizarCuentaBancaria(Integer idProveedor, CuentaBancaria cuenta, Connection conn) throws SQLException{
		CallableStatement cs = null;
		String sql = "";
		
		try {
			sql = "{ ? = call negocio.fn_actualizarproveedorcuentabancaria(?,?,?,?,?,?,?,?) }";
			int i=1;
			cs = conn.prepareCall(sql);
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, cuenta.getCodigoEntero().intValue());
			cs.setInt(i++, idProveedor.intValue());
			cs.setString(i++, cuenta.getNombreCuenta());
			cs.setString(i++, cuenta.getNumeroCuenta());
			cs.setInt(i++, cuenta.getTipoCuenta().getCodigoEntero().intValue());
			cs.setInt(i++, cuenta.getBanco().getCodigoEntero().intValue());
			cs.setString(i++, cuenta.getUsuarioCreacion());
			cs.setString(i++, cuenta.getIpCreacion());
			cs.execute();
			
			return cs.getBoolean(1);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new SQLException(e);
		} finally{
			if (cs != null){
				cs.close();
			}
		}
	}

	@Override
	public List<CuentaBancaria> listarCuentasBancarias(Integer idProveedor)
			throws SQLException {
		List<CuentaBancaria> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_listarcuentasbancariasproveedor(?) }";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			cs.registerOutParameter(1, Types.OTHER);
			cs.setInt(2, idProveedor.intValue());
			cs.execute();
			rs = (ResultSet)cs.getObject(1);
			
			resultado = new ArrayList<CuentaBancaria>();
			CuentaBancaria cuentaBancaria = null;
			while (rs.next()){
				cuentaBancaria = new CuentaBancaria();
				cuentaBancaria.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				cuentaBancaria.setNombreCuenta(UtilJdbc.obtenerCadena(rs, "nombrecuenta"));
				cuentaBancaria.setNumeroCuenta(UtilJdbc.obtenerCadena(rs, "numerocuenta"));
				cuentaBancaria.getTipoCuenta().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtipocuenta"));
				cuentaBancaria.getBanco().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idbanco"));
				cuentaBancaria.getMoneda().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idmoneda"));
				resultado.add(cuentaBancaria);
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
				throw new SQLException(e);
			}
		}
		
		return resultado;
	}
}

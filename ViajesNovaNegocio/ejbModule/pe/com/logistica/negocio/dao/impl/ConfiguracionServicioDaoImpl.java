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

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.ConfiguracionTipoServicio;
import pe.com.logistica.negocio.dao.ConfiguracionServicioDao;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilJdbc;

/**
 * @author Edwin
 *
 */
public class ConfiguracionServicioDaoImpl implements ConfiguracionServicioDao {

	/**
	 * 
	 */
	public ConfiguracionServicioDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.ConfiguracionServicioDao#consultarConfiguracionServicio()
	 */
	@Override
	public ConfiguracionTipoServicio consultarConfiguracionServicio(Integer idTipoServicio)
			throws SQLException, Exception {
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call soporte.fn_consultarconfiguracionservicio(?) }";
		ResultSet rs = null;
		ConfiguracionTipoServicio resultado = null;

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.setInt(i++, idTipoServicio.intValue());
			cs.execute();

			rs = (ResultSet) cs.getObject(1);
			
			if (rs.next()) {
				resultado = new ConfiguracionTipoServicio();
				resultado.setMuestraAerolinea(UtilJdbc.obtenerBoolean(rs, "muestraaerolinea"));
				resultado.setMuestraEmpresaTransporte(UtilJdbc.obtenerBoolean(rs, "muestraempresatransporte"));
				resultado.setMuestraHotel(UtilJdbc.obtenerBoolean(rs, "muestrahotel"));
				resultado.setMuestraProveedor(UtilJdbc.obtenerBoolean(rs, "muestraproveedor"));	
				resultado.setMuestraDescServicio(UtilJdbc.obtenerBoolean(rs, "muestradescservicio"));
				resultado.setMuestraFechaServicio(UtilJdbc.obtenerBoolean(rs, "muestrafechaservicio"));
				resultado.setMuestraFechaRegreso(UtilJdbc.obtenerBoolean(rs, "muestrafecharegreso"));
				resultado.setMuestraCantidad(UtilJdbc.obtenerBoolean(rs, "muestracantidad"));
				resultado.setMuestraPrecioBase(UtilJdbc.obtenerBoolean(rs, "muestraprecio"));
				resultado.setMuestraDestino(UtilJdbc.obtenerBoolean(rs, "muestradestino"));
				resultado.setMuestraComision(UtilJdbc.obtenerBoolean(rs, "muestracomision"));
				resultado.setMuestraOperadora(UtilJdbc.obtenerBoolean(rs, "muestraoperador"));
				resultado.setMuestraTarifaNegociada(UtilJdbc.obtenerBoolean(rs, "muestratarifanegociada"));
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null) {
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
	public List<ConfiguracionTipoServicio> listarConfiguracionServicios()
			throws SQLException, Exception {
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call soporte.fn_listarconfiguracionservicio() }";
		ResultSet rs = null;
		List<ConfiguracionTipoServicio> resultado = null;

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.execute();

			rs = (ResultSet) cs.getObject(1);
			resultado = new ArrayList<ConfiguracionTipoServicio>();
			ConfiguracionTipoServicio bean = null;
			while (rs.next()) {
				bean = new ConfiguracionTipoServicio();
				bean.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtiposervicio"));
				bean.setMuestraAerolinea(UtilJdbc.obtenerBoolean(rs, "muestraaerolinea"));
				bean.setMuestraEmpresaTransporte(UtilJdbc.obtenerBoolean(rs, "muestraempresatransporte"));
				bean.setMuestraHotel(UtilJdbc.obtenerBoolean(rs, "muestrahotel"));
				bean.setMuestraProveedor(UtilJdbc.obtenerBoolean(rs, "muestraproveedor"));	
				bean.setMuestraDescServicio(UtilJdbc.obtenerBoolean(rs, "muestradescservicio"));
				bean.setMuestraFechaServicio(UtilJdbc.obtenerBoolean(rs, "muestrafechaservicio"));
				bean.setMuestraFechaRegreso(UtilJdbc.obtenerBoolean(rs, "muestrafecharegreso"));
				bean.setMuestraCantidad(UtilJdbc.obtenerBoolean(rs, "muestracantidad"));
				bean.setMuestraPrecioBase(UtilJdbc.obtenerBoolean(rs, "muestraprecio"));
				bean.setMuestraDestino(UtilJdbc.obtenerBoolean(rs, "muestradestino"));
				bean.setMuestraComision(UtilJdbc.obtenerBoolean(rs, "muestracomision"));
				bean.setMuestraOperadora(UtilJdbc.obtenerBoolean(rs, "muestraoperador"));
				bean.setMuestraTarifaNegociada(UtilJdbc.obtenerBoolean(rs, "muestratarifanegociada"));
				resultado.add(bean);
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null) {
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
	public List<BaseVO> listarTipoServicios()
			throws SQLException, Exception {
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call soporte.fn_listartiposservicio() }";
		ResultSet rs = null;
		List<BaseVO> resultado = null;

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.execute();

			rs = (ResultSet) cs.getObject(1);
			resultado = new ArrayList<BaseVO>();
			BaseVO bean = null;
			while (rs.next()) {
				bean = new BaseVO();
				bean.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				bean.setNombre(UtilJdbc.obtenerCadena(rs, "nombre"));
				resultado.add(bean);
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null) {
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
	public boolean eliminarConfiguracion(
			ConfiguracionTipoServicio configuracionTipoServicio, Connection conn) throws SQLException {
		CallableStatement cs = null;
		String sql = "{ ? = call soporte.fn_eliminarconfiguracion(?,?) }";
		boolean resultado = false;
		
		try {
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setString(i++, configuracionTipoServicio.getUsuarioModificacion());
			cs.setString(i++, configuracionTipoServicio.getIpModificacion());
			cs.execute();
			
			resultado = cs.getBoolean(1);
			
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				
			} catch (SQLException e) {
				try {
					if (conn != null) {
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
	public boolean registrarConfiguracionServicio(
			ConfiguracionTipoServicio configuracion, Connection conn)
			throws SQLException {
		CallableStatement cs = null;
		String sql = "{? = call soporte.fn_registrarconfiguracionservicio(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		boolean resultado = false;
		
		try {
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, configuracion.getCodigoEntero().intValue());
			cs.setBoolean(i++, configuracion.isMuestraAerolinea());
			cs.setBoolean(i++, configuracion.isMuestraEmpresaTransporte());
			cs.setBoolean(i++, configuracion.isMuestraHotel());
			cs.setBoolean(i++, configuracion.isMuestraProveedor());
			cs.setBoolean(i++, configuracion.isMuestraDescServicio());
			cs.setBoolean(i++, configuracion.isMuestraFechaServicio());
			cs.setBoolean(i++, configuracion.isMuestraFechaRegreso());
			cs.setBoolean(i++, configuracion.isMuestraCantidad());
			cs.setBoolean(i++, configuracion.isMuestraPrecioBase());
			cs.setBoolean(i++, configuracion.isMuestraDestino());
			cs.setBoolean(i++, configuracion.isMuestraComision());
			cs.setBoolean(i++, configuracion.isMuestraOperadora());
			cs.setBoolean(i++, configuracion.isMuestraTarifaNegociada());
			cs.setString(i++, configuracion.getUsuarioCreacion());
			cs.setString(i++, configuracion.getIpCreacion());
			
			cs.execute();
			
			resultado = cs.getBoolean(1);
			
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				
			} catch (SQLException e) {
				try {
					if (conn != null) {
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

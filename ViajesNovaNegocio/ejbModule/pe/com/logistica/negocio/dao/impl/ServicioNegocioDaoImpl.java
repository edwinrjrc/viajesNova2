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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.jasper.DetalleServicio;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.bean.negocio.ServicioProveedor;
import pe.com.logistica.bean.util.UtilParse;
import pe.com.logistica.negocio.dao.ServicioNegocioDao;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilJdbc;

/**
 * @author edwreb
 *
 */
public class ServicioNegocioDaoImpl implements ServicioNegocioDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pe.com.logistica.negocio.dao.ServicioNegocioDao#calcularCuota(pe.com.
	 * logistica.bean.negocio.ServicioAgencia)
	 */
	@Override
	public BigDecimal calcularCuota(ServicioAgencia servicioAgencia)
			throws SQLException {
		BigDecimal resultado = BigDecimal.ZERO;
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_calcularcuota(?,?,?) }";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.DECIMAL);
			cs.setBigDecimal(i++, servicioAgencia.getMontoTotalServicios());
			cs.setBigDecimal(i++, UtilParse.parseIntABigDecimal(servicioAgencia
					.getNroCuotas()));
			cs.setBigDecimal(i++, servicioAgencia.getTea());
			cs.execute();

			resultado = cs.getBigDecimal(1);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		return resultado;
	}

	@Override
	public List<ServicioProveedor> proveedoresXServicio(BaseVO servicio)
			throws SQLException {
		List<ServicioProveedor> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_proveedorxservicio(?) }";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.setInt(i++, servicio.getCodigoEntero().intValue());
			cs.execute();

			rs = (ResultSet) cs.getObject(1);
			resultado = new ArrayList<ServicioProveedor>();
			ServicioProveedor servicio2 = null;
			while (rs.next()) {
				servicio2 = new ServicioProveedor();
				servicio2.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				servicio2.setNombreProveedor(UtilJdbc.obtenerCadena(rs,
						"nombres"));
				servicio2.setPorcentajeComision(UtilJdbc.obtenerBigDecimal(rs,
						"porcencomnacional"));
				servicio2.setPorcenComInternacional(UtilJdbc.obtenerBigDecimal(
						rs, "porcencominternacional"));
				resultado.add(servicio2);
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
				throw new SQLException(e);
			}
		}
		return resultado;
	}

	@Override
	public List<DetalleServicio> consultarServicioVentaJR(Integer idServicio)
			throws SQLException {
		List<DetalleServicio> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "";
		try {
			sql = "{ ? = call negocio.fn_consultarservicioventajr(?) }";
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			cs.registerOutParameter(1, Types.OTHER);
			cs.setInt(2, idServicio);
			cs.execute();

			rs = (ResultSet) cs.getObject(1);
			DetalleServicio detalle = null;
			resultado = new ArrayList<DetalleServicio>();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			DecimalFormatSymbols decimalSymbols = null;
			DecimalFormat df = null;
			Date fechaIda = null;
			BigDecimal precio = null;
			BigDecimal total = null;
			while (rs.next()) {
				detalle = new DetalleServicio();
				detalle.setCantidad(UtilJdbc.obtenerCadena(rs, "cantidad"));
				detalle.setDescripcionServicio(UtilJdbc.obtenerCadena(rs,
						"descripcionservicio"));
				fechaIda = UtilJdbc.obtenerFecha(rs, "fechaida");
				detalle.setFechaIda(sdf.format(fechaIda));

				decimalSymbols = new DecimalFormatSymbols(Locale.US);
				String pattern = "###,##0.00";
				String simboloMoneda = UtilJdbc
						.obtenerCadena(rs, "abreviatura");
				pattern = simboloMoneda + " " + pattern;
				df = new DecimalFormat(pattern, decimalSymbols);
				precio = UtilJdbc.obtenerBigDecimal(rs, "preciobase");
				detalle.setPrecioUnitario(df.format(precio.doubleValue()));
				total = UtilJdbc.obtenerBigDecimal(rs, "montototal");
				detalle.setTotal(df.format(total.doubleValue()));
				resultado.add(detalle);
			}

			return resultado;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(e);
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (cs != null) {
				cs.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
}

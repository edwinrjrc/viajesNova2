/**
 * 
 */
package pe.com.logistica.negocio.dao.impl;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.Util.UtilParse;
import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Comprobante;
import pe.com.logistica.bean.negocio.CuotaPago;
import pe.com.logistica.bean.negocio.DetalleComprobante;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.DocumentoAdicional;
import pe.com.logistica.bean.negocio.EventoObsAnu;
import pe.com.logistica.bean.negocio.PagoServicio;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.bean.negocio.ServicioAgenciaBusqueda;
import pe.com.logistica.negocio.dao.ServicioNovaViajesDao;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilJdbc;

/**
 * @author edwreb
 *
 */
public class ServicioNovaViajesDaoImpl implements ServicioNovaViajesDao {

	@Override
	public Integer ingresarCabeceraServicio(ServicioAgencia servicioAgencia)
			throws SQLException {
		Integer idservicio = 0;
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresarserviciocabecera(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.INTEGER);
			cs.setInt(i++, servicioAgencia.getCliente().getCodigoEntero().intValue());
			if (servicioAgencia.getCliente2().getCodigoEntero()!= null && servicioAgencia.getCliente2().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicioAgencia.getCliente2().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaServicio()));
			if (servicioAgencia.getMontoTotalIGV()!= null && !servicioAgencia.getMontoTotalIGV().equals(BigDecimal.ZERO)){
				cs.setBigDecimal(i++, servicioAgencia.getMontoTotalIGV());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			if (servicioAgencia.getMontoTotal() != null && !servicioAgencia.getMontoTotal().equals(BigDecimal.ZERO)){
				cs.setBigDecimal(i++, servicioAgencia.getMontoTotal());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			if (servicioAgencia.getMontoTotalFee() != null && !servicioAgencia.getMontoTotalFee().equals(BigDecimal.ZERO)){
				cs.setBigDecimal(i++, servicioAgencia.getMontoTotalFee());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			if (servicioAgencia.getMontoTotalComision() != null && !servicioAgencia.getMontoTotalComision().equals(BigDecimal.ZERO)){
				cs.setBigDecimal(i++, servicioAgencia.getMontoTotalComision());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			if (servicioAgencia.getDestino().getCodigoEntero()!=null && servicioAgencia.getDestino().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicioAgencia.getDestino().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (StringUtils.isNotBlank(servicioAgencia.getDestino().getDescripcion())){
				cs.setString(i++, servicioAgencia.getDestino().getDescripcion());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (servicioAgencia.getFormaPago().getCodigoEntero()!=null && servicioAgencia.getFormaPago().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicioAgencia.getFormaPago().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			cs.setInt(i++, 1);
			cs.setInt(i++, servicioAgencia.getEstadoServicio().getCodigoEntero());
			if (servicioAgencia.getNroCuotas()!=0){
				cs.setInt(i++, servicioAgencia.getNroCuotas());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (servicioAgencia.getTea()!=null && !servicioAgencia.getTea().equals(BigDecimal.ZERO)){
				cs.setBigDecimal(i++, servicioAgencia.getTea());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			if (servicioAgencia.getValorCuota()!=null && !servicioAgencia.getValorCuota().equals(BigDecimal.ZERO)){
				cs.setBigDecimal(i++, servicioAgencia.getValorCuota());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			if (servicioAgencia.getFechaPrimerCuota()!=null){
				cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaPrimerCuota()));
			}
			else{
				cs.setNull(i++, Types.DATE);
			}
			if (servicioAgencia.getFechaUltimaCuota()!=null){
				cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaUltimaCuota()));
			}
			else{
				cs.setNull(i++, Types.DATE);
			}
			cs.setInt(i++, servicioAgencia.getVendedor().getCodigoEntero());
			if (StringUtils.isNotBlank(servicioAgencia.getObservaciones())){
				cs.setString(i++, servicioAgencia.getObservaciones());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			cs.setString(i++, servicioAgencia.getUsuarioCreacion());
			cs.setString(i++, servicioAgencia.getIpCreacion());
			cs.execute();
			
			idservicio = cs.getInt(1);
		} catch (SQLException e) {
			idservicio = 0;
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (conn != null){
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

		return idservicio;
	}
	
	
	@Override
	public Integer ingresarCabeceraServicio(ServicioAgencia servicioAgencia, Connection conn)
			throws SQLException {
		Integer idservicio = 0;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresarserviciocabecera(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.INTEGER);
			cs.setInt(i++, servicioAgencia.getCliente().getCodigoEntero().intValue());
			if (servicioAgencia.getCliente2().getCodigoEntero()!= null && servicioAgencia.getCliente2().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicioAgencia.getCliente2().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaServicio()));
			cs.setBigDecimal(i++, servicioAgencia.getMontoTotalIGV());
			cs.setBigDecimal(i++, servicioAgencia.getMontoTotal());
			cs.setBigDecimal(i++, servicioAgencia.getMontoTotalFee());
			cs.setBigDecimal(i++, servicioAgencia.getMontoTotalComision());
			if (servicioAgencia.getFormaPago().getCodigoEntero()!=null && servicioAgencia.getFormaPago().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicioAgencia.getFormaPago().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			cs.setInt(i++, 1);
			cs.setInt(i++, servicioAgencia.getEstadoServicio().getCodigoEntero());
			if (servicioAgencia.getNroCuotas()!=0){
				cs.setInt(i++, servicioAgencia.getNroCuotas());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (servicioAgencia.getTea()!=null && !servicioAgencia.getTea().equals(BigDecimal.ZERO)){
				cs.setBigDecimal(i++, servicioAgencia.getTea());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			if (servicioAgencia.getValorCuota()!=null && !servicioAgencia.getValorCuota().equals(BigDecimal.ZERO)){
				cs.setBigDecimal(i++, servicioAgencia.getValorCuota());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			if (servicioAgencia.getFechaPrimerCuota()!=null){
				cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaPrimerCuota()));
			}
			else{
				cs.setNull(i++, Types.DATE);
			}
			if (servicioAgencia.getFechaUltimaCuota()!=null){
				cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaUltimaCuota()));
			}
			else{
				cs.setNull(i++, Types.DATE);
			}
			cs.setInt(i++, servicioAgencia.getVendedor().getCodigoEntero());
			if (StringUtils.isNotBlank(servicioAgencia.getObservaciones())){
				cs.setString(i++, servicioAgencia.getObservaciones());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			cs.setString(i++, servicioAgencia.getUsuarioCreacion());
			cs.setString(i++, servicioAgencia.getIpCreacion());
			cs.execute();
			
			idservicio = cs.getInt(1);
		} catch (SQLException e) {
			idservicio = 0;
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}

		return idservicio;
	}
	
	@Override
	public Integer ingresarDetalleServicio(DetalleServicioAgencia detalleServicio, int idServicio, Connection conn)
			throws SQLException {
		Integer resultado = 0;
		CallableStatement cs = null;

		String sql = "{ ? = call negocio.fn_ingresarserviciodetalle(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.INTEGER);
			cs.setInt(i++, detalleServicio.getTipoServicio().getCodigoEntero().intValue());
			if (StringUtils.isNotBlank(detalleServicio.getDescripcionServicio())){
				cs.setString(i++, detalleServicio.getDescripcionServicio());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			cs.setInt(i++, idServicio);
			cs.setTimestamp(i++, UtilJdbc.convertirUtilDateTimeStamp(detalleServicio.getFechaIda()));
			if (detalleServicio.getFechaRegreso() != null){
				cs.setTimestamp(i++, UtilJdbc.convertirUtilDateTimeStamp(detalleServicio.getFechaRegreso()));
			}
			else{
				cs.setNull(i++, Types.DATE);
			}
			cs.setInt(i++, detalleServicio.getCantidad());
			if (detalleServicio.getServicioProveedor().getProveedor().getCodigoEntero()!=null && detalleServicio.getServicioProveedor().getProveedor().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, detalleServicio.getServicioProveedor().getProveedor().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (StringUtils.isNotBlank(detalleServicio.getServicioProveedor().getProveedor().getNombreCompleto())){
				cs.setString(i++, detalleServicio.getServicioProveedor().getProveedor().getNombreCompleto());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (detalleServicio.getOperadora().getCodigoEntero()!=null && detalleServicio.getOperadora().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, detalleServicio.getOperadora().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (StringUtils.isNotBlank(detalleServicio.getOperadora().getNombre())){
				cs.setString(i++, detalleServicio.getOperadora().getNombre());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (detalleServicio.getAerolinea().getCodigoEntero()!=null && detalleServicio.getAerolinea().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, detalleServicio.getAerolinea().getCodigoEntero().intValue());
			}
			else if (detalleServicio.getEmpresaTransporte().getCodigoEntero()!=null && detalleServicio.getEmpresaTransporte().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, detalleServicio.getEmpresaTransporte().getCodigoEntero().intValue());
			}
			else {
				cs.setNull(i++, Types.INTEGER);
			}
			if (StringUtils.isNotBlank(detalleServicio.getAerolinea().getNombre())){
				cs.setString(i++, detalleServicio.getAerolinea().getNombre());
			}
			else if (StringUtils.isNotBlank(detalleServicio.getEmpresaTransporte().getNombre())){
				cs.setString(i++, detalleServicio.getEmpresaTransporte().getNombre());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			
			if (detalleServicio.getHotel().getCodigoEntero()!=null && detalleServicio.getHotel().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, detalleServicio.getHotel().getCodigoEntero().intValue());
			}
			else {
				cs.setNull(i++, Types.INTEGER);
			}
			if (StringUtils.isNotBlank(detalleServicio.getHotel().getNombre())){
				cs.setString(i++, detalleServicio.getHotel().getNombre());
			}
			else {
				cs.setNull(i++, Types.VARCHAR);
			}
			if (detalleServicio.getOrigen().getCodigoEntero() != null && detalleServicio.getOrigen().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, detalleServicio.getOrigen().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (StringUtils.isNotBlank(detalleServicio.getOrigen().getDescripcion())){
				cs.setString(i++, detalleServicio.getOrigen().getDescripcion());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (detalleServicio.getDestino().getCodigoEntero() != null && detalleServicio.getDestino().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, detalleServicio.getDestino().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (StringUtils.isNotBlank(detalleServicio.getDestino().getDescripcion())){
				cs.setString(i++, detalleServicio.getDestino().getDescripcion());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			cs.setBigDecimal(i++, detalleServicio.getPrecioUnitario());
			cs.setBoolean(i++, detalleServicio.getServicioProveedor().isEditoComision());
			cs.setBoolean(i++, detalleServicio.isTarifaNegociada());
			if (detalleServicio.getServicioProveedor().getPorcentajeComision()!=null && !detalleServicio.getServicioProveedor().getPorcentajeComision().equals(BigDecimal.ZERO)){
				cs.setBigDecimal(i++, detalleServicio.getServicioProveedor().getPorcentajeComision());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			cs.setBigDecimal(i++, detalleServicio.getMontoComision());
			if (StringUtils.isNotBlank(detalleServicio.getCodigoReserva())){
				cs.setString(i++, detalleServicio.getCodigoReserva());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(detalleServicio.getNumeroBoleto())){
				cs.setString(i++, detalleServicio.getNumeroBoleto());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			cs.setBigDecimal(i++, detalleServicio.getTotalServicio());
			if (detalleServicio.getServicioPadre().getCodigoEntero()!=null && detalleServicio.getServicioPadre().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, detalleServicio.getServicioPadre().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			cs.setString(i++, detalleServicio.getUsuarioCreacion());
			cs.setString(i++, detalleServicio.getIpCreacion());
			cs.execute();
			
			resultado = cs.getInt(1);
		} catch (SQLException e) {
			resultado = 0;
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}

		return resultado;
	}

	
	@Override
	public boolean generarCronogramaPago(ServicioAgencia servicioAgencia, Connection conn)
			throws SQLException {
		boolean resultado = false;
		CallableStatement cs = null;

		String sql = "{ ? = call negocio.fn_generarcronogramapago(?,?,?,?,?,?,?)}";
		
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, servicioAgencia.getCodigoEntero().intValue());
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaPrimerCuota()));
			cs.setBigDecimal(i++, servicioAgencia.getMontoTotalServicios());
			cs.setBigDecimal(i++, servicioAgencia.getTea());
			cs.setBigDecimal(i++, UtilParse.parseIntABigDecimal(servicioAgencia.getNroCuotas()));

			cs.setString(i++, servicioAgencia.getUsuarioCreacion());
			cs.setString(i++, servicioAgencia.getIpCreacion());
			cs.execute();
			
			resultado = cs.getBoolean(1);
		} catch (SQLException e) {
			resultado = false;
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}

		return resultado;
	}
	
	@Override
	public List<CuotaPago> consultarCronogramaPago(ServicioAgencia servicioAgencia)
			throws SQLException {
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_consultarcronogramapago(?)}";
		List<CuotaPago> cronograma = null;
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.setInt(i++, servicioAgencia.getCodigoEntero().intValue());
			cs.execute();
			
			rs = (ResultSet)cs.getObject(1);
			CuotaPago cuota = null;
			cronograma = new ArrayList<CuotaPago>();
			while (rs.next()){
				cuota = new CuotaPago();
				cuota.setNroCuota(UtilJdbc.obtenerNumero(rs, "nrocuota"));
				cuota.setFechaVencimiento(UtilJdbc.obtenerFecha(rs, "fechavencimiento"));
				cuota.setCapital(UtilJdbc.obtenerBigDecimal(rs, "capital"));
				cuota.setInteres(UtilJdbc.obtenerBigDecimal(rs, "interes"));
				cuota.setTotalCuota(UtilJdbc.obtenerBigDecimal(rs, "totalcuota"));
				cuota.getEstadoCuota().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idestadocuota"));
				if (cuota.getEstadoCuota().getCodigoEntero().intValue() == 1){
					cuota.getEstadoCuota().setNombre("PENDIENTE");
				}
				cronograma.add(cuota);
			}
		} catch (SQLException e) {
			cronograma = null;
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

		return cronograma;
	}
	
	@Override
	public ServicioAgencia consultarServiciosVenta2(int idServicio)
			throws SQLException {
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_consultarservicioventa(?)}";
		ServicioAgencia servicioAgencia2 = new ServicioAgencia();
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.setInt(i++, idServicio);
			
			cs.execute();
			
			rs = (ResultSet)cs.getObject(1);
			if (rs.next()){
				servicioAgencia2.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				servicioAgencia2.getCliente().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idcliente1"));
				servicioAgencia2.getCliente().setNombres(UtilJdbc.obtenerCadena(rs, "nombres1"));
				servicioAgencia2.getCliente().setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno1"));
				servicioAgencia2.getCliente().setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno1"));
				servicioAgencia2.getCliente2().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idcliente2"));
				servicioAgencia2.getCliente2().setNombres(UtilJdbc.obtenerCadena(rs, "nombres2"));
				servicioAgencia2.getCliente2().setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno2"));
				servicioAgencia2.getCliente2().setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno2"));
				servicioAgencia2.setFechaServicio(UtilJdbc.obtenerFecha(rs, "fechaservicio"));
				servicioAgencia2.setMontoTotalServicios(UtilJdbc.obtenerBigDecimal(rs, "montototal"));
				servicioAgencia2.setCantidadServicios(UtilJdbc.obtenerNumero(rs, "cantidadservicios"));
				servicioAgencia2.getDestino().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "iddestino"));
				servicioAgencia2.getDestino().setDescripcion(UtilJdbc.obtenerCadena(rs, "descdestino"));
				servicioAgencia2.getFormaPago().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idformapago"));
				servicioAgencia2.getFormaPago().setNombre(UtilJdbc.obtenerCadena(rs, "nommediopago"));
				servicioAgencia2.getEstadoPago().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idestadopago"));
				servicioAgencia2.getEstadoPago().setNombre(UtilJdbc.obtenerCadena(rs, "nomestpago"));
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

		return servicioAgencia2;
	}
	
	@Override
	public ServicioAgencia consultarServiciosVenta2(int idServicio, Connection conn)
			throws SQLException {
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_consultarservicioventa(?)}";
		ServicioAgencia servicioAgencia2 = new ServicioAgencia();
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.setInt(i++, idServicio);
			
			cs.execute();
			
			rs = (ResultSet)cs.getObject(1);
			if (rs.next()){
				servicioAgencia2.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				servicioAgencia2.getCliente().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idcliente1"));
				servicioAgencia2.getCliente().setNombres(UtilJdbc.obtenerCadena(rs, "nombres1"));
				servicioAgencia2.getCliente().setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno1"));
				servicioAgencia2.getCliente().setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno1"));
				servicioAgencia2.getCliente2().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idcliente2"));
				servicioAgencia2.getCliente2().setNombres(UtilJdbc.obtenerCadena(rs, "nombres2"));
				servicioAgencia2.getCliente2().setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno2"));
				servicioAgencia2.getCliente2().setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno2"));
				servicioAgencia2.setFechaServicio(UtilJdbc.obtenerFecha(rs, "fechacompra"));
				servicioAgencia2.setMontoTotalServicios(UtilJdbc.obtenerBigDecimal(rs, "montototal"));
				servicioAgencia2.setMontoTotalComision(UtilJdbc.obtenerBigDecimal(rs, "montocomisiontotal"));
				servicioAgencia2.setMontoTotalIGV(UtilJdbc.obtenerBigDecimal(rs, "montototaligv"));
				servicioAgencia2.setMontoTotalFee(UtilJdbc.obtenerBigDecimal(rs, "montototalfee"));
				servicioAgencia2.getFormaPago().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idformapago"));
				servicioAgencia2.getFormaPago().setNombre(UtilJdbc.obtenerCadena(rs, "nommediopago"));
				servicioAgencia2.getEstadoPago().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idestadopago"));
				servicioAgencia2.getEstadoPago().setNombre(UtilJdbc.obtenerCadena(rs, "nomestpago"));
				servicioAgencia2.setServicioPagado(UtilJdbc.comparaNumero(servicioAgencia2.getEstadoPago().getCodigoEntero().intValue(),2));
				servicioAgencia2.setNroCuotas(UtilJdbc.obtenerNumero(rs, "nrocuotas"));
				servicioAgencia2.setTea(UtilJdbc.obtenerBigDecimal(rs, "tea"));
				servicioAgencia2.setValorCuota(UtilJdbc.obtenerBigDecimal(rs, "valorcuota"));
				servicioAgencia2.setFechaPrimerCuota(UtilJdbc.obtenerFecha(rs, "fechaprimercuota"));
				servicioAgencia2.setFechaUltimaCuota(UtilJdbc.obtenerFecha(rs, "fechaultcuota"));
				servicioAgencia2.getEstadoServicio().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idestadoservicio"));
				servicioAgencia2.setTienePagos(UtilJdbc.obtenerNumero(rs, "tienepagos"));
				servicioAgencia2.getVendedor().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idusuario"));
				String nombreVendedor = UtilJdbc.obtenerCadena(rs, "nombresvendedor")+" "+UtilJdbc.obtenerCadena(rs, "apepaterno")+" "+UtilJdbc.obtenerCadena(rs, "apematerno");
				nombreVendedor = StringUtils.normalizeSpace(nombreVendedor);
				servicioAgencia2.getVendedor().setNombre(nombreVendedor);
				servicioAgencia2.setGuardoComprobante(UtilJdbc.obtenerBoolean(rs, "generocomprobantes"));
				servicioAgencia2.setGuardoRelacionComprobantes(UtilJdbc.obtenerBoolean(rs, "guardorelacioncomprobantes"));
				servicioAgencia2.setObservaciones(UtilJdbc.obtenerCadena(rs, "observaciones"));
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
				
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}

		return servicioAgencia2;
	}

	@Override
	public List<DetalleServicioAgencia> consultaServicioDetalle(int idServicio)
			throws SQLException {
		List<DetalleServicioAgencia> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_consultarservicioventadetalle(?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.setInt(i++, idServicio);
			cs.execute();
			
			rs = (ResultSet)cs.getObject(1);
			DetalleServicioAgencia detalleServicio = null;
			resultado = new ArrayList<DetalleServicioAgencia>();
			while (rs.next()){
				detalleServicio = new DetalleServicioAgencia();

				detalleServicio.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				detalleServicio.getTipoServicio().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtiposervicio"));
				detalleServicio.getTipoServicio().setNombre(UtilJdbc.obtenerCadena(rs, "nomtipservicio"));
				detalleServicio.getTipoServicio().setDescripcion(UtilJdbc.obtenerCadena(rs, "descservicio"));
				detalleServicio.getTipoServicio().setRequiereFee(UtilJdbc.obtenerBoolean(rs, "requierefee"));
				detalleServicio.getTipoServicio().setPagaImpto(UtilJdbc.obtenerBoolean(rs, "pagaimpto"));
				detalleServicio.getTipoServicio().setCargaComision(UtilJdbc.obtenerBoolean(rs, "cargacomision"));
				detalleServicio.getTipoServicio().setEsImpuesto(UtilJdbc.obtenerBoolean(rs, "esimpuesto"));
				detalleServicio.getTipoServicio().setEsFee(UtilJdbc.obtenerBoolean(rs, "esfee"));
				detalleServicio.setDescripcionServicio(UtilJdbc.obtenerCadena(rs, "descripcionservicio"));
				detalleServicio.getDestino().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "iddestino"));
				detalleServicio.getDestino().setDescripcion(UtilJdbc.obtenerCadena(rs, "descripciondestino"));
				detalleServicio.setDias(UtilJdbc.obtenerNumero(rs, "dias"));
				detalleServicio.setNoches(UtilJdbc.obtenerNumero(rs, "noches"));
				detalleServicio.setFechaIda(UtilJdbc.obtenerFecha(rs, "fechaida"));
				detalleServicio.setFechaRegreso(UtilJdbc.obtenerFecha(rs, "fecharegreso"));
				detalleServicio.setCantidad(UtilJdbc.obtenerNumero(rs, "cantidad"));
				detalleServicio.setPrecioUnitario(UtilJdbc.obtenerBigDecimal(rs, "preciobase"));
				detalleServicio.getServicioProveedor().setPorcentajeComision(UtilJdbc.obtenerBigDecimal(rs, "porcencomision"));
				detalleServicio.setMontoComision(UtilJdbc.obtenerBigDecimal(rs, "montocomision"));
				detalleServicio.getServicioProveedor().getProveedor().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idproveedor"));
				detalleServicio.getServicioProveedor().getProveedor().setNombres(UtilJdbc.obtenerCadena(rs, "nombres"));
				detalleServicio.getServicioProveedor().getProveedor().setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno"));
				detalleServicio.getServicioProveedor().getProveedor().setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno"));
				detalleServicio.setCodigoReserva(UtilJdbc.obtenerCadena(rs, "codigoreserva"));
				detalleServicio.setNumeroBoleto(UtilJdbc.obtenerCadena(rs, "numeroboleto"));
				
				resultado.add(detalleServicio);
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
	public List<DetalleServicioAgencia> consultaServicioDetalle(int idServicio, Connection conn)
			throws SQLException {
		List<DetalleServicioAgencia> resultado = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_consultarservicioventadetalle(?)}";
		
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.setInt(i++, idServicio);
			cs.execute();
			
			rs = (ResultSet)cs.getObject(1);
			DetalleServicioAgencia detalleServicio = null;
			resultado = new ArrayList<DetalleServicioAgencia>();
			while (rs.next()){
				detalleServicio = new DetalleServicioAgencia();

				detalleServicio.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idSerdetalle"));
				detalleServicio.getTipoServicio().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtiposervicio"));
				detalleServicio.getTipoServicio().setNombre(UtilJdbc.obtenerCadena(rs, "nomtipservicio"));
				detalleServicio.getTipoServicio().setDescripcion(UtilJdbc.obtenerCadena(rs, "descservicio"));
				detalleServicio.getTipoServicio().setRequiereFee(UtilJdbc.obtenerBoolean(rs, "requierefee"));
				detalleServicio.getTipoServicio().setPagaImpto(UtilJdbc.obtenerBoolean(rs, "pagaimpto"));
				detalleServicio.getTipoServicio().setCargaComision(UtilJdbc.obtenerBoolean(rs, "cargacomision"));
				detalleServicio.getTipoServicio().setEsImpuesto(UtilJdbc.obtenerBoolean(rs, "esimpuesto"));
				detalleServicio.getTipoServicio().setEsFee(UtilJdbc.obtenerBoolean(rs, "esfee"));
				detalleServicio.setDescripcionServicio(UtilJdbc.obtenerCadena(rs, "descripcionservicio"));
				detalleServicio.setFechaIda(UtilJdbc.obtenerFecha(rs, "fechaida"));
				detalleServicio.setFechaRegreso(UtilJdbc.obtenerFecha(rs, "fecharegreso"));
				detalleServicio.setCantidad(UtilJdbc.obtenerNumero(rs, "cantidad"));
				detalleServicio.setPrecioUnitario(UtilJdbc.obtenerBigDecimal(rs, "preciobase"));
				detalleServicio.getServicioProveedor().setPorcentajeComision(UtilJdbc.obtenerBigDecimal(rs, "porcencomision"));
				detalleServicio.setMontoComision(UtilJdbc.obtenerBigDecimal(rs, "montocomision"));
				detalleServicio.getServicioProveedor().getProveedor().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idempresaproveedor"));
				detalleServicio.getServicioProveedor().getProveedor().setNombres(UtilJdbc.obtenerCadena(rs, "nombres"));
				detalleServicio.getServicioProveedor().getProveedor().setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno"));
				detalleServicio.getServicioProveedor().getProveedor().setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno"));
				detalleServicio.getTipoServicio().setVisible(UtilJdbc.obtenerBoolean(rs, "visible"));
				detalleServicio.getServicioPadre().setCodigoEntero(idServicio);
				detalleServicio.setCodigoReserva(UtilJdbc.obtenerCadena(rs, "codigoreserva"));
				detalleServicio.setNumeroBoleto(UtilJdbc.obtenerCadena(rs, "numeroboleto"));
				
				resultado.add(detalleServicio);
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
				
			} catch (SQLException e) {
				throw new SQLException(e);
				
			}
		}
		
		return resultado;
	}
	
	@Override
	public List<DetalleServicioAgencia> consultaServicioDetallePadre(int idServicio, Connection conn)
			throws SQLException {
		List<DetalleServicioAgencia> resultado = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_consultarservicioventadetallepadre(?)}";
		
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.setInt(i++, idServicio);
			cs.execute();
			
			rs = (ResultSet)cs.getObject(1);
			DetalleServicioAgencia detalleServicio = null;
			resultado = new ArrayList<DetalleServicioAgencia>();
			while (rs.next()){
				detalleServicio = new DetalleServicioAgencia();

				detalleServicio.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idSerdetalle"));
				detalleServicio.getTipoServicio().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtiposervicio"));
				detalleServicio.getTipoServicio().setNombre(UtilJdbc.obtenerCadena(rs, "nomtipservicio"));
				detalleServicio.getTipoServicio().setDescripcion(UtilJdbc.obtenerCadena(rs, "descservicio"));
				detalleServicio.getTipoServicio().setRequiereFee(UtilJdbc.obtenerBoolean(rs, "requierefee"));
				detalleServicio.getTipoServicio().setPagaImpto(UtilJdbc.obtenerBoolean(rs, "pagaimpto"));
				detalleServicio.getTipoServicio().setCargaComision(UtilJdbc.obtenerBoolean(rs, "cargacomision"));
				detalleServicio.getTipoServicio().setEsImpuesto(UtilJdbc.obtenerBoolean(rs, "esimpuesto"));
				detalleServicio.getTipoServicio().setEsFee(UtilJdbc.obtenerBoolean(rs, "esfee"));
				detalleServicio.setDescripcionServicio(UtilJdbc.obtenerCadena(rs, "descripcionservicio"));
				detalleServicio.setFechaIda(UtilJdbc.obtenerFecha(rs, "fechaida"));
				detalleServicio.setFechaRegreso(UtilJdbc.obtenerFecha(rs, "fecharegreso"));
				detalleServicio.setCantidad(UtilJdbc.obtenerNumero(rs, "cantidad"));
				detalleServicio.setPrecioUnitario(UtilJdbc.obtenerBigDecimal(rs, "preciobase"));
				detalleServicio.getServicioProveedor().setPorcentajeComision(UtilJdbc.obtenerBigDecimal(rs, "porcencomision"));
				detalleServicio.setMontoComision(UtilJdbc.obtenerBigDecimal(rs, "montocomision"));
				detalleServicio.getServicioProveedor().getProveedor().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idempresaproveedor"));
				detalleServicio.getServicioProveedor().getProveedor().setNombres(UtilJdbc.obtenerCadena(rs, "nombres"));
				detalleServicio.getServicioProveedor().getProveedor().setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno"));
				detalleServicio.getServicioProveedor().getProveedor().setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno"));
				detalleServicio.getTipoServicio().setVisible(UtilJdbc.obtenerBoolean(rs, "visible"));
				detalleServicio.getServicioPadre().setCodigoEntero(idServicio);
				detalleServicio.setCodigoReserva(UtilJdbc.obtenerCadena(rs, "codigoreserva"));
				detalleServicio.setNumeroBoleto(UtilJdbc.obtenerCadena(rs, "numeroboleto"));
				
				resultado.add(detalleServicio);
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
				
			} catch (SQLException e) {
				throw new SQLException(e);
				
			}
		}
		
		return resultado;
	}
	
	@Override
	public List<DetalleServicioAgencia> consultaServicioDetalleHijo(int idServicio, int idSerDetaPadre, Connection conn)
			throws SQLException {
		List<DetalleServicioAgencia> resultado = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_consultarservicioventadetallehijo(?,?)}";
		
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.setInt(i++, idServicio);
			cs.setInt(i++, idSerDetaPadre);
			cs.execute();
			
			rs = (ResultSet)cs.getObject(1);
			DetalleServicioAgencia detalleServicio = null;
			resultado = new ArrayList<DetalleServicioAgencia>();
			while (rs.next()){
				detalleServicio = new DetalleServicioAgencia();

				detalleServicio.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idSerdetalle"));
				detalleServicio.getTipoServicio().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtiposervicio"));
				detalleServicio.getTipoServicio().setNombre(UtilJdbc.obtenerCadena(rs, "nomtipservicio"));
				detalleServicio.getTipoServicio().setDescripcion(UtilJdbc.obtenerCadena(rs, "descservicio"));
				detalleServicio.getTipoServicio().setRequiereFee(UtilJdbc.obtenerBoolean(rs, "requierefee"));
				detalleServicio.getTipoServicio().setPagaImpto(UtilJdbc.obtenerBoolean(rs, "pagaimpto"));
				detalleServicio.getTipoServicio().setCargaComision(UtilJdbc.obtenerBoolean(rs, "cargacomision"));
				detalleServicio.getTipoServicio().setEsImpuesto(UtilJdbc.obtenerBoolean(rs, "esimpuesto"));
				detalleServicio.getTipoServicio().setEsFee(UtilJdbc.obtenerBoolean(rs, "esfee"));
				detalleServicio.setDescripcionServicio(UtilJdbc.obtenerCadena(rs, "descripcionservicio"));
				detalleServicio.setFechaIda(UtilJdbc.obtenerFecha(rs, "fechaida"));
				detalleServicio.setFechaRegreso(UtilJdbc.obtenerFecha(rs, "fecharegreso"));
				detalleServicio.setCantidad(UtilJdbc.obtenerNumero(rs, "cantidad"));
				detalleServicio.setPrecioUnitario(UtilJdbc.obtenerBigDecimal(rs, "preciobase"));
				detalleServicio.getServicioProveedor().setPorcentajeComision(UtilJdbc.obtenerBigDecimal(rs, "porcencomision"));
				detalleServicio.setMontoComision(UtilJdbc.obtenerBigDecimal(rs, "montocomision"));
				detalleServicio.getServicioProveedor().getProveedor().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idempresaproveedor"));
				detalleServicio.getServicioProveedor().getProveedor().setNombres(UtilJdbc.obtenerCadena(rs, "nombres"));
				detalleServicio.getServicioProveedor().getProveedor().setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno"));
				detalleServicio.getServicioProveedor().getProveedor().setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno"));
				detalleServicio.getTipoServicio().setVisible(UtilJdbc.obtenerBoolean(rs, "visible"));
				detalleServicio.getServicioPadre().setCodigoEntero(idServicio);
				detalleServicio.setCodigoReserva(UtilJdbc.obtenerCadena(rs, "codigoreserva"));
				detalleServicio.setNumeroBoleto(UtilJdbc.obtenerCadena(rs, "numeroboleto"));
				
				resultado.add(detalleServicio);
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
				
			} catch (SQLException e) {
				throw new SQLException(e);
				
			}
		}
		
		return resultado;
	}
	
	@Override
	public List<DetalleServicioAgencia> consultaServicioDetalleHijos(int idServicio, int idSerPadre, Connection conn)
			throws SQLException {
		List<DetalleServicioAgencia> resultado = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_consultarserviciodetallehijos(?,?)}";
		
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.setInt(i++, idServicio);
			cs.setInt(i++, idSerPadre);
			cs.execute();
			
			rs = (ResultSet)cs.getObject(1);
			DetalleServicioAgencia detalleServicio = null;
			resultado = new ArrayList<DetalleServicioAgencia>();
			while (rs.next()){
				detalleServicio = new DetalleServicioAgencia();

				detalleServicio.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idSerdetalle"));
				detalleServicio.getTipoServicio().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtiposervicio"));
				detalleServicio.getTipoServicio().setNombre(UtilJdbc.obtenerCadena(rs, "nomtipservicio"));
				detalleServicio.getTipoServicio().setDescripcion(UtilJdbc.obtenerCadena(rs, "descservicio"));
				detalleServicio.getTipoServicio().setRequiereFee(UtilJdbc.obtenerBoolean(rs, "requierefee"));
				detalleServicio.getTipoServicio().setPagaImpto(UtilJdbc.obtenerBoolean(rs, "pagaimpto"));
				detalleServicio.getTipoServicio().setCargaComision(UtilJdbc.obtenerBoolean(rs, "cargacomision"));
				detalleServicio.getTipoServicio().setEsImpuesto(UtilJdbc.obtenerBoolean(rs, "esimpuesto"));
				detalleServicio.getTipoServicio().setEsFee(UtilJdbc.obtenerBoolean(rs, "esfee"));
				detalleServicio.setDescripcionServicio(UtilJdbc.obtenerCadena(rs, "descripcionservicio"));
				detalleServicio.setFechaIda(UtilJdbc.obtenerFecha(rs, "fechaida"));
				detalleServicio.setFechaRegreso(UtilJdbc.obtenerFecha(rs, "fecharegreso"));
				detalleServicio.setCantidad(UtilJdbc.obtenerNumero(rs, "cantidad"));
				detalleServicio.setPrecioUnitario(UtilJdbc.obtenerBigDecimal(rs, "preciobase"));
				detalleServicio.getServicioProveedor().setPorcentajeComision(UtilJdbc.obtenerBigDecimal(rs, "porcencomision"));
				detalleServicio.setMontoComision(UtilJdbc.obtenerBigDecimal(rs, "montocomision"));
				detalleServicio.getServicioProveedor().getProveedor().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idempresaproveedor"));
				detalleServicio.getServicioProveedor().getProveedor().setNombres(UtilJdbc.obtenerCadena(rs, "nombres"));
				detalleServicio.getServicioProveedor().getProveedor().setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno"));
				detalleServicio.getServicioProveedor().getProveedor().setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno"));
				detalleServicio.setCodigoReserva(UtilJdbc.obtenerCadena(rs, "codigoreserva"));
				detalleServicio.setNumeroBoleto(UtilJdbc.obtenerCadena(rs, "numeroboleto"));
				
				resultado.add(detalleServicio);
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
				
			} catch (SQLException e) {
				throw new SQLException(e);
				
			}
		}
		
		return resultado;
	}
	
	@Override
	public List<ServicioAgencia> consultarServiciosVenta(ServicioAgenciaBusqueda servicioAgencia)
			throws SQLException {
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_consultarservicioventa(?,?,?,?,?,?,?)}";
		List<ServicioAgencia> listaVentaServicios = null;
		try {	
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.OTHER);
			if (servicioAgencia.getCliente().getDocumentoIdentidad().getTipoDocumento().getCodigoEntero() != null && servicioAgencia.getCliente().getDocumentoIdentidad().getTipoDocumento().getCodigoEntero().intValue() != 0){
				cs.setInt(i++, servicioAgencia.getCliente().getDocumentoIdentidad().getTipoDocumento().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (StringUtils.isNotBlank(servicioAgencia.getCliente().getDocumentoIdentidad().getNumeroDocumento())){
				cs.setString(i++, servicioAgencia.getCliente().getDocumentoIdentidad().getNumeroDocumento());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(servicioAgencia.getCliente().getNombres())){
				cs.setString(i++, UtilJdbc.borrarEspacioMayusculas(servicioAgencia.getCliente().getNombres()));
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (servicioAgencia.getVendedor().getCodigoEntero()!=null && servicioAgencia.getVendedor().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicioAgencia.getVendedor().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (servicioAgencia.getCodigoEntero()!=null && servicioAgencia.getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicioAgencia.getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaDesde()));
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaHasta()));
			
			cs.execute();
			
			rs = (ResultSet)cs.getObject(1);
			ServicioAgencia servicioAgencia2 = null;
			listaVentaServicios = new ArrayList<ServicioAgencia>();
			while (rs.next()){
				servicioAgencia2 = new ServicioAgencia();
				servicioAgencia2.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				servicioAgencia2.getCliente().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idcliente1"));
				servicioAgencia2.getCliente().setNombres(UtilJdbc.obtenerCadena(rs, "nombres1"));
				servicioAgencia2.getCliente().setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno1"));
				servicioAgencia2.getCliente().setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno1"));
				servicioAgencia2.getCliente2().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idcliente2"));
				servicioAgencia2.getCliente2().setNombres(UtilJdbc.obtenerCadena(rs, "nombres2"));
				servicioAgencia2.getCliente2().setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno2"));
				servicioAgencia2.getCliente2().setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno2"));
				servicioAgencia2.setFechaServicio(UtilJdbc.obtenerFecha(rs, "fechacompra"));
				servicioAgencia2.setMontoTotalServicios(UtilJdbc.obtenerBigDecimal(rs, "montototal"));
				servicioAgencia2.getFormaPago().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idformapago"));
				servicioAgencia2.getFormaPago().setNombre(UtilJdbc.obtenerCadena(rs, "nommediopago"));
				servicioAgencia2.getEstadoPago().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idestadopago"));
				servicioAgencia2.getEstadoPago().setNombre(UtilJdbc.obtenerCadena(rs, "nomestpago"));
				servicioAgencia2.getEstadoServicio().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idestadoservicio"));
				servicioAgencia2.getEstadoServicio().setNombre(UtilJdbc.obtenerCadena(rs, "nomestservicio"));
				int novios = UtilJdbc.obtenerNumero(rs, "cantidadNovios");
				servicioAgencia2.setEsProgramaNovios((novios>0));
				int idvendedor = UtilJdbc.obtenerNumero(rs, "idvendedor");
				servicioAgencia2.setEditable(false);
				if (servicioAgencia.getVendedor().getCodigoEntero()!=null && servicioAgencia.getVendedor().getCodigoEntero().intValue()!=0){
					
					boolean editable = false; 
					editable = ((idvendedor == servicioAgencia.getVendedor().getCodigoEntero().intValue()) && servicioAgencia2.getEstadoServicio().getCodigoEntero().equals(ServicioAgencia.ESTADO_PENDIENTE_CIERRE));
					
					servicioAgencia2.setEditable(editable);
					
				}
				listaVentaServicios.add(servicioAgencia2);
			}
		} catch (SQLException e) {
			servicioAgencia = null;
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

		return listaVentaServicios;
	}
	
	@Override
	public Integer actualizarCabeceraServicio(ServicioAgencia servicioAgencia)
			throws SQLException {
		Integer idservicio = 0;
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_actualizarserviciocabecera1(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.INTEGER);
			cs.setInt(i++, servicioAgencia.getCodigoEntero().intValue());
			cs.setInt(i++, servicioAgencia.getCliente().getCodigoEntero().intValue());
			if (servicioAgencia.getCliente2().getCodigoEntero()!= null && servicioAgencia.getCliente2().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicioAgencia.getCliente2().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaServicio()));
			if (servicioAgencia.getCantidadServicios() != 0){
				cs.setInt(i++, servicioAgencia.getCantidadServicios());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (servicioAgencia.getMontoTotalServicios() != null && !servicioAgencia.getMontoTotalServicios().equals(BigDecimal.ZERO)){
				cs.setBigDecimal(i++, servicioAgencia.getMontoTotalServicios());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			if (servicioAgencia.getMontoTotalFee() != null && !servicioAgencia.getMontoTotalFee().equals(BigDecimal.ZERO)){
				cs.setBigDecimal(i++, servicioAgencia.getMontoTotalFee());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			if (servicioAgencia.getMontoTotalComision() != null && !servicioAgencia.getMontoTotalComision().equals(BigDecimal.ZERO)){
				cs.setBigDecimal(i++, servicioAgencia.getMontoTotalComision());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			if (servicioAgencia.getDestino().getCodigoEntero()!=null && servicioAgencia.getDestino().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicioAgencia.getDestino().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (StringUtils.isNotBlank(servicioAgencia.getDestino().getDescripcion())){
				cs.setString(i++, servicioAgencia.getDestino().getDescripcion());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (servicioAgencia.getFormaPago().getCodigoEntero()!=null && servicioAgencia.getFormaPago().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicioAgencia.getFormaPago().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			cs.setInt(i++, 1);
			cs.setInt(i++, servicioAgencia.getEstadoServicio().getCodigoEntero());
			if (servicioAgencia.getNroCuotas()!=0){
				cs.setInt(i++, servicioAgencia.getNroCuotas());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (servicioAgencia.getTea()!=null && !servicioAgencia.getTea().equals(BigDecimal.ZERO)){
				cs.setBigDecimal(i++, servicioAgencia.getTea());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			if (servicioAgencia.getValorCuota()!=null && !servicioAgencia.getValorCuota().equals(BigDecimal.ZERO)){
				cs.setBigDecimal(i++, servicioAgencia.getValorCuota());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			if (servicioAgencia.getFechaPrimerCuota()!=null){
				cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaPrimerCuota()));
			}
			else{
				cs.setNull(i++, Types.DATE);
			}
			if (servicioAgencia.getFechaUltimaCuota()!=null){
				cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaUltimaCuota()));
			}
			else{
				cs.setNull(i++, Types.DATE);
			}
			cs.setInt(i++, servicioAgencia.getVendedor().getCodigoEntero());
			if (StringUtils.isNotBlank(servicioAgencia.getObservaciones())){
				cs.setString(i++, servicioAgencia.getObservaciones());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			cs.setString(i++, servicioAgencia.getUsuarioModificacion());
			cs.setString(i++, servicioAgencia.getIpModificacion());
			cs.execute();
			
			idservicio = cs.getInt(1);
		} catch (SQLException e) {
			idservicio = 0;
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (conn != null){
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

		return idservicio;
	}
	
	@Override
	public Integer actualizarCabeceraServicio(ServicioAgencia servicioAgencia, Connection conn)
			throws SQLException {
		Integer idservicio = 0;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_actualizarserviciocabecera1(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.INTEGER);
			cs.setInt(i++, servicioAgencia.getCodigoEntero().intValue());
			cs.setInt(i++, servicioAgencia.getCliente().getCodigoEntero().intValue());
			if (servicioAgencia.getCliente2().getCodigoEntero()!= null && servicioAgencia.getCliente2().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicioAgencia.getCliente2().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaServicio()));
			if (servicioAgencia.getCantidadServicios() != 0){
				cs.setInt(i++, servicioAgencia.getCantidadServicios());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (servicioAgencia.getMontoTotalServicios() != null && !servicioAgencia.getMontoTotalServicios().equals(BigDecimal.ZERO)){
				cs.setBigDecimal(i++, servicioAgencia.getMontoTotalServicios());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			if (servicioAgencia.getMontoTotalFee() != null && !servicioAgencia.getMontoTotalFee().equals(BigDecimal.ZERO)){
				cs.setBigDecimal(i++, servicioAgencia.getMontoTotalFee());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			if (servicioAgencia.getMontoTotalComision() != null && !servicioAgencia.getMontoTotalComision().equals(BigDecimal.ZERO)){
				cs.setBigDecimal(i++, servicioAgencia.getMontoTotalComision());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			if (servicioAgencia.getMontoTotalIGV() != null && !servicioAgencia.getMontoTotalIGV().equals(BigDecimal.ZERO)){
				cs.setBigDecimal(i++, servicioAgencia.getMontoTotalIGV());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			if (servicioAgencia.getDestino().getCodigoEntero()!=null && servicioAgencia.getDestino().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicioAgencia.getDestino().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (StringUtils.isNotBlank(servicioAgencia.getDestino().getDescripcion())){
				cs.setString(i++, servicioAgencia.getDestino().getDescripcion());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (servicioAgencia.getFormaPago().getCodigoEntero()!=null && servicioAgencia.getFormaPago().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicioAgencia.getFormaPago().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			cs.setInt(i++, 1);
			cs.setInt(i++, servicioAgencia.getEstadoServicio().getCodigoEntero());
			if (servicioAgencia.getNroCuotas()!=0){
				cs.setInt(i++, servicioAgencia.getNroCuotas());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (servicioAgencia.getTea()!=null && !servicioAgencia.getTea().equals(BigDecimal.ZERO)){
				cs.setBigDecimal(i++, servicioAgencia.getTea());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			if (servicioAgencia.getValorCuota()!=null && !servicioAgencia.getValorCuota().equals(BigDecimal.ZERO)){
				cs.setBigDecimal(i++, servicioAgencia.getValorCuota());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			if (servicioAgencia.getFechaPrimerCuota()!=null){
				cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaPrimerCuota()));
			}
			else{
				cs.setNull(i++, Types.DATE);
			}
			if (servicioAgencia.getFechaUltimaCuota()!=null){
				cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaUltimaCuota()));
			}
			else{
				cs.setNull(i++, Types.DATE);
			}
			cs.setInt(i++, servicioAgencia.getVendedor().getCodigoEntero());
			if (StringUtils.isNotBlank(servicioAgencia.getObservaciones())){
				cs.setString(i++, UtilJdbc.convertirMayuscula( servicioAgencia.getObservaciones()));
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			cs.setString(i++, servicioAgencia.getUsuarioModificacion());
			cs.setString(i++, servicioAgencia.getIpModificacion());
			cs.execute();
			
			idservicio = cs.getInt(1);
		} catch (SQLException e) {
			idservicio = 0;
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}

		return idservicio;
	}
	
	@Override
	public Integer eliminarDetalleServicio(ServicioAgencia servicioAgencia)
			throws SQLException {
		Integer idservicio = 0;
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_eliminardetalleservicio(?,?,?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.INTEGER);
			cs.setInt(i++, servicioAgencia.getCodigoEntero().intValue());
			cs.setString(i++, servicioAgencia.getUsuarioModificacion());
			cs.setString(i++, servicioAgencia.getIpModificacion());
			cs.execute();
			
			idservicio = cs.getInt(1);
		} catch (SQLException e) {
			idservicio = 0;
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (conn != null){
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

		return idservicio;
	}
	
	@Override
	public boolean eliminarDetalleServicio(ServicioAgencia servicioAgencia, Connection conn)
			throws SQLException {
		boolean resultado = false;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_eliminardetalleservicio(?,?,?)}";
		
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, servicioAgencia.getCodigoEntero().intValue());
			cs.setString(i++, servicioAgencia.getUsuarioModificacion());
			cs.setString(i++, servicioAgencia.getIpModificacion());
			cs.execute();
			
			resultado = cs.getBoolean(1);
		} catch (SQLException e) {
			resultado = false;
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}

		return resultado;
	}
	
	@Override
	public boolean eliminarCronogramaServicio(ServicioAgencia servicioAgencia)
			throws SQLException {
		boolean resultado = false;
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_eliminarcronogramaservicio(?,?,?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, servicioAgencia.getCodigoEntero().intValue());
			cs.setString(i++, servicioAgencia.getUsuarioModificacion());
			cs.setString(i++, servicioAgencia.getIpModificacion());
			cs.execute();
			
			resultado = cs.getBoolean(1);
		} catch (SQLException e) {
			resultado = false;
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (conn != null){
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
	public boolean eliminarCronogramaServicio(ServicioAgencia servicioAgencia, Connection conn)
			throws SQLException {
		boolean resultado = false;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_eliminarcronogramaservicio(?,?,?)}";
		
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, servicioAgencia.getCodigoEntero().intValue());
			cs.setString(i++, servicioAgencia.getUsuarioModificacion());
			cs.setString(i++, servicioAgencia.getIpModificacion());
			cs.execute();
			
			resultado = cs.getBoolean(1);
		} catch (SQLException e) {
			resultado = false;
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}

		return resultado;
	}

	@Override
	public void registrarSaldosServicio(ServicioAgencia servicioAgencia,
			Connection conn) throws SQLException {
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_registrarsaldoservicio(?,?,?,?,?,?)}";
		
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.INTEGER);
			cs.setInt(i++, servicioAgencia.getCodigoEntero().intValue());
			cs.setInt(i++, 0);
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaServicio()));
			cs.setBigDecimal(i++, servicioAgencia.getMontoTotal());
			cs.setString(i++, servicioAgencia.getUsuarioCreacion());
			cs.setString(i++, servicioAgencia.getIpCreacion());
			cs.execute();
			
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
	}

	@Override
	public void registrarPagoServicio(PagoServicio pago) throws SQLException {
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_registrarpagoservicio(?,?,?,?,?,?,?,?,?,?,?,?)}";
		Connection conn = null;
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.INTEGER);
			cs.setInt(i++, pago.getServicio().getCodigoEntero());
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(pago.getFechaPago()));
			cs.setBigDecimal(i++, pago.getMontoPago());
			if (pago.getSustentoPagoByte()!=null){
				cs.setBinaryStream(i++, new ByteArrayInputStream(pago.getSustentoPagoByte()), pago.getSustentoPagoByte().length);
			}
			else{
				cs.setNull(i++, Types.VARBINARY);
			}
			if (StringUtils.isNotBlank(pago.getNombreArchivo())){
				cs.setString(i++, pago.getNombreArchivo());
			}
			else {
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(pago.getExtensionArchivo())){
				cs.setString(i++, pago.getExtensionArchivo());
			}
			else {
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(pago.getTipoContenido())){
				cs.setString(i++, pago.getTipoContenido());
			}
			else {
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(pago.getComentario())){
				cs.setString(i++, pago.getComentario());
			}
			else {
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(pago.getTipoPago().getCodigoCadena())){
				cs.setBoolean(i++, "D".equals(pago.getTipoPago().getCodigoCadena()));
			}
			else {
				cs.setNull(i++, Types.BOOLEAN);
			}
			if (StringUtils.isNotBlank(pago.getTipoPago().getCodigoCadena())){
				cs.setBoolean(i++, "R".equals(pago.getTipoPago().getCodigoCadena()));
			}
			else {
				cs.setNull(i++, Types.BOOLEAN);
			}
			cs.setString(i++, pago.getUsuarioCreacion());
			cs.setString(i++, pago.getIpCreacion());
			cs.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				cs.close();
				cs = conn.prepareCall(sql);
				int i=1;
				cs.registerOutParameter(i++, Types.INTEGER);
				cs.setInt(i++, pago.getServicio().getCodigoEntero());
				cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(pago.getFechaPago()));
				cs.setBigDecimal(i++, pago.getMontoPago());
				if (pago.getSustentoPagoByte()!=null){
					cs.setBinaryStream(i++, new ByteArrayInputStream(pago.getSustentoPagoByte()));
				}
				else{
					cs.setNull(i++, Types.VARBINARY);
				}
				if (StringUtils.isNotBlank(pago.getComentario())){
					cs.setString(i++, pago.getComentario());
				}
				else {
					cs.setNull(i++, Types.VARCHAR);
				}
				if (StringUtils.isNotBlank(pago.getTipoPago().getCodigoCadena())){
					cs.setBoolean(i++, "D".equals(pago.getTipoPago().getCodigoCadena()));
				}
				else {
					cs.setNull(i++, Types.BOOLEAN);
				}
				if (StringUtils.isNotBlank(pago.getTipoPago().getCodigoCadena())){
					cs.setBoolean(i++, "R".equals(pago.getTipoPago().getCodigoCadena()));
				}
				else {
					cs.setNull(i++, Types.BOOLEAN);
				}
				cs.setString(i++, pago.getUsuarioCreacion());
				cs.setString(i++, pago.getIpCreacion());
				cs.execute();
			} catch (SQLException e1) {
				throw new SQLException(e);
			}
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		
	}

	@Override
	public List<PagoServicio> listarPagosServicio(Integer idServicio)
			throws SQLException {
		List<PagoServicio> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_listarpagos(?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.OTHER);
			if (idServicio != null){
				cs.setInt(i++, idServicio);
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			
			cs.execute();
			rs = (ResultSet)cs.getObject(1);
			resultado = new ArrayList<PagoServicio>();
			PagoServicio pago = null;
			while (rs.next()){
				pago = new PagoServicio();
				pago.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idpago"));
				pago.getServicio().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idservicio"));
				pago.setFechaPago(UtilJdbc.obtenerFecha(rs, "fechapago"));
				pago.setMontoPago(UtilJdbc.obtenerBigDecimal(rs, "montopagado"));
				byte[] sustento = rs.getBytes("sustentopago");
				pago.setSustentoPagoByte(sustento);
				pago.setTieneSustento((sustento != null));
				pago.setNombreArchivo(UtilJdbc.obtenerCadena(rs, "nombrearchivo"));
				pago.setExtensionArchivo(UtilJdbc.obtenerCadena(rs, "extensionarchivo"));
				pago.setTipoContenido(UtilJdbc.obtenerCadena(rs, "tipocontenido"));
				boolean esDetraccion = UtilJdbc.obtenerBoolean(rs, "espagodetraccion");
				boolean esRetencion = UtilJdbc.obtenerBoolean(rs, "espagoretencion");
				
				BaseVO tipoPago = new BaseVO();
				tipoPago.setCodigoCadena((esDetraccion?"D":(esRetencion?"R":"")));
				tipoPago.setNombre((esDetraccion?"Detracción":(esRetencion?"Retención":"Normal")));
				pago.setTipoPago(tipoPago);
				resultado.add(pago);
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
	public List<PagoServicio> listarPagosObligacion(Integer idObligacion)
			throws SQLException {
		List<PagoServicio> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_listarpagosobligaciones(?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.OTHER);
			if (idObligacion != null){
				cs.setInt(i++, idObligacion);
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			
			cs.execute();
			rs = (ResultSet)cs.getObject(1);
			resultado = new ArrayList<PagoServicio>();
			PagoServicio pago = null;
			while (rs.next()){
				pago = new PagoServicio();
				pago.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idpago"));
				pago.setIdObligacion(UtilJdbc.obtenerNumero(rs, "idobligacion"));
				pago.setFechaPago(UtilJdbc.obtenerFecha(rs, "fechapago"));
				pago.setMontoPago(UtilJdbc.obtenerBigDecimal(rs, "montopagado"));
				byte[] sustento = rs.getBytes("sustentopago");
				pago.setSustentoPagoByte(sustento);
				pago.setTieneSustento((sustento != null));
				pago.setNombreArchivo(UtilJdbc.obtenerCadena(rs, "nombrearchivo"));
				pago.setExtensionArchivo(UtilJdbc.obtenerCadena(rs, "extensionarchivo"));
				pago.setTipoContenido(UtilJdbc.obtenerCadena(rs, "tipocontenido"));
				
				boolean esDetraccion = UtilJdbc.obtenerBoolean(rs, "espagodetraccion");
				boolean esRetencion = UtilJdbc.obtenerBoolean(rs, "espagoretencion");
				
				BaseVO tipoPago = new BaseVO();
				tipoPago.setCodigoCadena((esDetraccion?"D":(esRetencion?"R":"")));
				tipoPago.setNombre((esDetraccion?"Detracción":(esRetencion?"Retención":"Normal")));
				pago.setTipoPago(tipoPago);
				resultado.add(pago);
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
	public BigDecimal consultarSaldoServicio(Integer idServicio)
			throws SQLException {
		BigDecimal resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_consultarsaldosservicio(?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.OTHER);
			if (idServicio != null){
				cs.setInt(i++, idServicio);
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			
			cs.execute();
			rs = (ResultSet)cs.getObject(1);
			if (rs.next()){
				
				resultado = UtilJdbc.obtenerBigDecimal(rs, "montosaldoservicio");
				
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
	public void actualizarServicioVenta(ServicioAgencia servicioAgencia) throws SQLException, Exception{
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_actualizarestadoservicio(?,?,?,?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, servicioAgencia.getCodigoEntero().intValue());
			cs.setInt(i++, servicioAgencia.getEstadoServicio().getCodigoEntero().intValue());
			cs.setString(i++, servicioAgencia.getUsuarioModificacion());
			cs.setString(i++, servicioAgencia.getIpModificacion());
			
			cs.execute();
			
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
	}

	@Override
	public boolean registrarEventoObsAnu(EventoObsAnu evento) throws SQLException, Exception {
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_registrareventoservicio(?,?,?,?,?,?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, evento.getTipoEvento().getCodigoEntero().intValue());
			cs.setString(i++, evento.getComentario());
			cs.setInt(i++, evento.getIdServicio().intValue());
			if (evento.getTipoEvento().getCodigoEntero().equals(EventoObsAnu.EVENTO_ANU)){
				cs.setInt(i++, ServicioAgencia.ESTADO_ANULADO);
			}
			else{
				cs.setInt(i++, ServicioAgencia.ESTADO_OBSERVADO);
			}
			cs.setString(i++, evento.getUsuarioCreacion());
			cs.setString(i++, evento.getIpCreacion());
			
			cs.execute();
			
			return true;
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
	}
	
	@Override
	public Integer registrarComprobante(Comprobante comprobante, Connection conn) throws SQLException, Exception{
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresarcomprobantegenerado(?,?,?,?,?,?,?,?,?,?,?)}";
		int resultado = 0;
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.INTEGER);
			cs.setInt(i++, comprobante.getIdServicio().intValue());
			cs.setInt(i++, comprobante.getTipoComprobante().getCodigoEntero().intValue());
			cs.setString(i++, comprobante.getNumeroComprobante());
			cs.setInt(i++, comprobante.getTitular().getCodigoEntero().intValue());
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(comprobante.getFechaComprobante()));
			cs.setBigDecimal(i++, comprobante.getTotalIGV());
			cs.setBigDecimal(i++, comprobante.getTotalComprobante());
			cs.setBoolean(i++, comprobante.isTieneDetraccion());
			cs.setBoolean(i++, comprobante.isTieneRetencion());
			
			cs.setString(i++, comprobante.getUsuarioCreacion());
			cs.setString(i++, comprobante.getIpCreacion());
			
			cs.execute();
			
			resultado = cs.getInt(1);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		
		return resultado;
	}
	
	@Override
	public Integer registrarDetalleComprobante(List<DetalleComprobante> listaDetalle, Integer idComprobante, Connection conn) throws SQLException, Exception{
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresardetallecomprobantegenerado(?,?,?,?,?,?,?,?)}";
		int resultado = 0;
		try {
			
			for (DetalleComprobante detalleComprobante : listaDetalle) {
				cs = conn.prepareCall(sql);
				int i=1;
				cs.registerOutParameter(i++, Types.BOOLEAN);
				cs.setInt(i++, detalleComprobante.getIdServicioDetalle().intValue());
				cs.setInt(i++, idComprobante.intValue());
				cs.setInt(i++, detalleComprobante.getCantidad());
				cs.setString(i++, detalleComprobante.getConcepto());
				cs.setBigDecimal(i++, detalleComprobante.getPrecioUnitario());
				cs.setBigDecimal(i++, detalleComprobante.getTotalDetalle());
				cs.setString(i++, detalleComprobante.getUsuarioCreacion());
				cs.setString(i++, detalleComprobante.getIpCreacion());
				
				cs.execute();
				if (cs != null){
					cs.close();
				}
			}
			
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		
		return resultado;
	}
	
	@Override
	public void actualizarComprobantesServicio(boolean generoComprobantes, ServicioAgencia servicio, Connection conn) throws SQLException, Exception{
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_actualizarcomprobanteservicio(?,?,?,?)}";
		try {
			
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, servicio.getCodigoEntero().intValue());
			cs.setBoolean(i++, generoComprobantes);
			cs.setString(i++, servicio.getUsuarioCreacion());
			cs.setString(i++, servicio.getIpCreacion());
			
			cs.execute();
			
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		
	}
	
	@Override
	public List<DetalleServicioAgencia> consultaServicioDetalleComprobante(int idServicio)
			throws SQLException {
		Connection conn = null;
		List<DetalleServicioAgencia> resultado = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_consultarcomprobantesserviciodetalle(?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.setInt(i++, idServicio);
			cs.execute();
			
			rs = (ResultSet)cs.getObject(1);
			DetalleServicioAgencia detalleServicio = null;
			resultado = new ArrayList<DetalleServicioAgencia>();
			while (rs.next()){
				detalleServicio = new DetalleServicioAgencia();

				detalleServicio.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idSerdetalle"));
				detalleServicio.getTipoServicio().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtiposervicio"));
				detalleServicio.getTipoServicio().setNombre(UtilJdbc.obtenerCadena(rs, "nomtipservicio"));
				detalleServicio.getTipoServicio().setDescripcion(UtilJdbc.obtenerCadena(rs, "descservicio"));
				detalleServicio.getTipoServicio().setRequiereFee(UtilJdbc.obtenerBoolean(rs, "requierefee"));
				detalleServicio.getTipoServicio().setPagaImpto(UtilJdbc.obtenerBoolean(rs, "pagaimpto"));
				detalleServicio.getTipoServicio().setCargaComision(UtilJdbc.obtenerBoolean(rs, "cargacomision"));
				detalleServicio.getTipoServicio().setEsImpuesto(UtilJdbc.obtenerBoolean(rs, "esimpuesto"));
				detalleServicio.getTipoServicio().setEsFee(UtilJdbc.obtenerBoolean(rs, "esfee"));
				detalleServicio.setDescripcionServicio(detalleServicio.getTipoServicio().getNombre()+" - "+UtilJdbc.obtenerCadena(rs, "descripcionservicio"));
				detalleServicio.setFechaIda(UtilJdbc.obtenerFecha(rs, "fechaida"));
				detalleServicio.setFechaRegreso(UtilJdbc.obtenerFecha(rs, "fecharegreso"));
				detalleServicio.setCantidad(UtilJdbc.obtenerNumero(rs, "cantidad"));
				detalleServicio.setPrecioUnitario(UtilJdbc.obtenerBigDecimal(rs, "preciobase"));
				detalleServicio.getServicioProveedor().setPorcentajeComision(UtilJdbc.obtenerBigDecimal(rs, "porcencomision"));
				detalleServicio.setMontoComision(UtilJdbc.obtenerBigDecimal(rs, "montocomision"));
				detalleServicio.getServicioProveedor().getProveedor().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idempresaproveedor"));
				detalleServicio.getServicioProveedor().getProveedor().setNombres(UtilJdbc.obtenerCadena(rs, "nombres"));
				detalleServicio.getServicioProveedor().getProveedor().setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno"));
				detalleServicio.getServicioProveedor().getProveedor().setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno"));
				detalleServicio.getTipoServicio().setVisible(UtilJdbc.obtenerBoolean(rs, "visible"));
				detalleServicio.setTieneDetraccion(UtilJdbc.obtenerBoolean(rs, "tieneDetraccion"));
				detalleServicio.setTieneRetencion(UtilJdbc.obtenerBoolean(rs, "tieneRetencion"));
				detalleServicio.setIdComprobanteGenerado(UtilJdbc.obtenerNumero(rs, "idComprobante"));
				detalleServicio.getTipoComprobante().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "tipoComprobante"));
				detalleServicio.getTipoComprobante().setNombre(UtilJdbc.obtenerCadena(rs, "tipoComprobanteNombre"));
				detalleServicio.getTipoComprobante().setAbreviatura(UtilJdbc.obtenerCadena(rs, "tipoComprobanteAbrev"));
				detalleServicio.setNroComprobante(UtilJdbc.obtenerCadena(rs, "numeroComprobante"));
				detalleServicio.getServicioPadre().setCodigoEntero(idServicio);
				
				resultado.add(detalleServicio);
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
	public List<DetalleServicioAgencia> consultaServicioDetalleComprobanteHijo(int idServicio, int idDetaServicio)
			throws SQLException {
		Connection conn = null;
		List<DetalleServicioAgencia> resultado = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_consultarcompserviciodethijo(?,?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.setInt(i++, idServicio);
			cs.setInt(i++, idDetaServicio);
			cs.execute();
			
			rs = (ResultSet)cs.getObject(1);
			DetalleServicioAgencia detalleServicio = null;
			resultado = new ArrayList<DetalleServicioAgencia>();
			while (rs.next()){
				detalleServicio = new DetalleServicioAgencia();

				detalleServicio.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idSerdetalle"));
				detalleServicio.getTipoServicio().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtiposervicio"));
				detalleServicio.getTipoServicio().setNombre(UtilJdbc.obtenerCadena(rs, "nomtipservicio"));
				detalleServicio.getTipoServicio().setDescripcion(UtilJdbc.obtenerCadena(rs, "descservicio"));
				detalleServicio.getTipoServicio().setRequiereFee(UtilJdbc.obtenerBoolean(rs, "requierefee"));
				detalleServicio.getTipoServicio().setPagaImpto(UtilJdbc.obtenerBoolean(rs, "pagaimpto"));
				detalleServicio.getTipoServicio().setCargaComision(UtilJdbc.obtenerBoolean(rs, "cargacomision"));
				detalleServicio.getTipoServicio().setEsImpuesto(UtilJdbc.obtenerBoolean(rs, "esimpuesto"));
				detalleServicio.getTipoServicio().setEsFee(UtilJdbc.obtenerBoolean(rs, "esfee"));
				detalleServicio.setDescripcionServicio(UtilJdbc.obtenerCadena(rs, "descripcionservicio"));
				detalleServicio.setFechaIda(UtilJdbc.obtenerFecha(rs, "fechaida"));
				detalleServicio.setFechaRegreso(UtilJdbc.obtenerFecha(rs, "fecharegreso"));
				detalleServicio.setCantidad(UtilJdbc.obtenerNumero(rs, "cantidad"));
				detalleServicio.setPrecioUnitario(UtilJdbc.obtenerBigDecimal(rs, "preciobase"));
				detalleServicio.getServicioProveedor().setPorcentajeComision(UtilJdbc.obtenerBigDecimal(rs, "porcencomision"));
				detalleServicio.setMontoComision(UtilJdbc.obtenerBigDecimal(rs, "montocomision"));
				detalleServicio.getServicioProveedor().getProveedor().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idempresaproveedor"));
				detalleServicio.getServicioProveedor().getProveedor().setNombres(UtilJdbc.obtenerCadena(rs, "nombres"));
				detalleServicio.getServicioProveedor().getProveedor().setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno"));
				detalleServicio.getServicioProveedor().getProveedor().setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno"));
				detalleServicio.getTipoServicio().setVisible(UtilJdbc.obtenerBoolean(rs, "visible"));
				detalleServicio.setTieneDetraccion(UtilJdbc.obtenerBoolean(rs, "tieneDetraccion"));
				detalleServicio.setTieneRetencion(UtilJdbc.obtenerBoolean(rs, "tieneRetencion"));
				detalleServicio.setIdComprobanteGenerado(UtilJdbc.obtenerNumero(rs, "idComprobante"));
				detalleServicio.getTipoComprobante().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "tipoComprobante"));
				detalleServicio.getTipoComprobante().setNombre(UtilJdbc.obtenerCadena(rs, "tipoComprobanteNombre"));
				detalleServicio.getTipoComprobante().setAbreviatura(UtilJdbc.obtenerCadena(rs, "tipoComprobanteAbrev"));
				detalleServicio.setNroComprobante(UtilJdbc.obtenerCadena(rs, "numeroComprobante"));
				detalleServicio.getServicioPadre().setCodigoEntero(idDetaServicio);
				
				resultado.add(detalleServicio);
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
	public List<DetalleServicioAgencia> consultaServDetComprobanteObligacion(int idServicio, Connection conn)
			throws SQLException {
		List<DetalleServicioAgencia> resultado = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_consultarcomprobantesobligacionservdet(?)}";
		
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.setInt(i++, idServicio);
			cs.execute();
			
			rs = (ResultSet)cs.getObject(1);
			DetalleServicioAgencia detalleServicio = null;
			resultado = new ArrayList<DetalleServicioAgencia>();
			while (rs.next()){
				detalleServicio = new DetalleServicioAgencia();

				detalleServicio.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idSerdetalle"));
				detalleServicio.getTipoServicio().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtiposervicio"));
				detalleServicio.getTipoServicio().setNombre(UtilJdbc.obtenerCadena(rs, "nomtipservicio"));
				detalleServicio.getTipoServicio().setDescripcion(UtilJdbc.obtenerCadena(rs, "descservicio"));
				detalleServicio.getTipoServicio().setRequiereFee(UtilJdbc.obtenerBoolean(rs, "requierefee"));
				detalleServicio.getTipoServicio().setPagaImpto(UtilJdbc.obtenerBoolean(rs, "pagaimpto"));
				detalleServicio.getTipoServicio().setCargaComision(UtilJdbc.obtenerBoolean(rs, "cargacomision"));
				detalleServicio.getTipoServicio().setEsImpuesto(UtilJdbc.obtenerBoolean(rs, "esimpuesto"));
				detalleServicio.getTipoServicio().setEsFee(UtilJdbc.obtenerBoolean(rs, "esfee"));
				detalleServicio.setDescripcionServicio(UtilJdbc.obtenerCadena(rs, "descripcionservicio"));
				detalleServicio.setFechaIda(UtilJdbc.obtenerFecha(rs, "fechaida"));
				detalleServicio.setFechaRegreso(UtilJdbc.obtenerFecha(rs, "fecharegreso"));
				detalleServicio.setCantidad(UtilJdbc.obtenerNumero(rs, "cantidad"));
				detalleServicio.setPrecioUnitario(UtilJdbc.obtenerBigDecimal(rs, "preciobase"));
				detalleServicio.getServicioProveedor().setPorcentajeComision(UtilJdbc.obtenerBigDecimal(rs, "porcencomision"));
				detalleServicio.setMontoComision(UtilJdbc.obtenerBigDecimal(rs, "montocomision"));
				detalleServicio.getServicioProveedor().getProveedor().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idempresaproveedor"));
				detalleServicio.getServicioProveedor().getProveedor().setNombres(UtilJdbc.obtenerCadena(rs, "nombres"));
				detalleServicio.getServicioProveedor().getProveedor().setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno"));
				detalleServicio.getServicioProveedor().getProveedor().setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno"));
				detalleServicio.getTipoServicio().setVisible(UtilJdbc.obtenerBoolean(rs, "visible"));
				detalleServicio.setTieneDetraccion(UtilJdbc.obtenerBoolean(rs, "tieneDetraccion"));
				detalleServicio.setTieneRetencion(UtilJdbc.obtenerBoolean(rs, "tieneRetencion"));
				detalleServicio.setIdComprobanteGenerado(UtilJdbc.obtenerNumero(rs, "idComprobante"));
				detalleServicio.getTipoComprobante().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "tipoComprobante"));
				detalleServicio.getTipoComprobante().setNombre(UtilJdbc.obtenerCadena(rs, "tipoComprobanteNombre"));
				detalleServicio.getTipoComprobante().setAbreviatura(UtilJdbc.obtenerCadena(rs, "tipoComprobanteAbrev"));
				detalleServicio.setNroComprobante(UtilJdbc.obtenerCadena(rs, "numeroComprobante"));
				detalleServicio.getComprobanteAsociado().getTipoComprobante().setNombre(UtilJdbc.obtenerCadena(rs, "tipoObligacion"));
				detalleServicio.getComprobanteAsociado().getTipoComprobante().setAbreviatura(UtilJdbc.obtenerCadena(rs, "tipoObligacionAbrev"));
				detalleServicio.getComprobanteAsociado().setNumeroComprobante(UtilJdbc.obtenerCadena(rs, "numeroObligacion"));
				detalleServicio.getServicioPadre().setCodigoEntero(idServicio);
				
				resultado.add(detalleServicio);
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
				
			} catch (SQLException e) {
				throw new SQLException(e);
				
			}
		}
		
		return resultado;
	}
	
	@Override
	public List<DetalleServicioAgencia> consultaServDetComprobanteObligacionHijo(int idServicio, int idDetaServicio, Connection conn)
			throws SQLException {
		List<DetalleServicioAgencia> resultado = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_consultarcompbtobligcnservdethijo(?,?)}";
		
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.setInt(i++, idServicio);
			cs.setInt(i++, idDetaServicio);
			cs.execute();
			
			rs = (ResultSet)cs.getObject(1);
			DetalleServicioAgencia detalleServicio = null;
			resultado = new ArrayList<DetalleServicioAgencia>();
			while (rs.next()){
				detalleServicio = new DetalleServicioAgencia();

				detalleServicio.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idSerdetalle"));
				detalleServicio.getTipoServicio().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtiposervicio"));
				detalleServicio.getTipoServicio().setNombre(UtilJdbc.obtenerCadena(rs, "nomtipservicio"));
				detalleServicio.getTipoServicio().setDescripcion(UtilJdbc.obtenerCadena(rs, "descservicio"));
				detalleServicio.getTipoServicio().setRequiereFee(UtilJdbc.obtenerBoolean(rs, "requierefee"));
				detalleServicio.getTipoServicio().setPagaImpto(UtilJdbc.obtenerBoolean(rs, "pagaimpto"));
				detalleServicio.getTipoServicio().setCargaComision(UtilJdbc.obtenerBoolean(rs, "cargacomision"));
				detalleServicio.getTipoServicio().setEsImpuesto(UtilJdbc.obtenerBoolean(rs, "esimpuesto"));
				detalleServicio.getTipoServicio().setEsFee(UtilJdbc.obtenerBoolean(rs, "esfee"));
				detalleServicio.setDescripcionServicio(UtilJdbc.obtenerCadena(rs, "descripcionservicio"));
				detalleServicio.setFechaIda(UtilJdbc.obtenerFecha(rs, "fechaida"));
				detalleServicio.setFechaRegreso(UtilJdbc.obtenerFecha(rs, "fecharegreso"));
				detalleServicio.setCantidad(UtilJdbc.obtenerNumero(rs, "cantidad"));
				detalleServicio.setPrecioUnitario(UtilJdbc.obtenerBigDecimal(rs, "preciobase"));
				detalleServicio.getServicioProveedor().setPorcentajeComision(UtilJdbc.obtenerBigDecimal(rs, "porcencomision"));
				detalleServicio.setMontoComision(UtilJdbc.obtenerBigDecimal(rs, "montocomision"));
				detalleServicio.getServicioProveedor().getProveedor().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idempresaproveedor"));
				detalleServicio.getServicioProveedor().getProveedor().setNombres(UtilJdbc.obtenerCadena(rs, "nombres"));
				detalleServicio.getServicioProveedor().getProveedor().setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno"));
				detalleServicio.getServicioProveedor().getProveedor().setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno"));
				detalleServicio.getTipoServicio().setVisible(UtilJdbc.obtenerBoolean(rs, "visible"));
				detalleServicio.setTieneDetraccion(UtilJdbc.obtenerBoolean(rs, "tieneDetraccion"));
				detalleServicio.setTieneRetencion(UtilJdbc.obtenerBoolean(rs, "tieneRetencion"));
				detalleServicio.setIdComprobanteGenerado(UtilJdbc.obtenerNumero(rs, "idComprobante"));
				detalleServicio.getTipoComprobante().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "tipoComprobante"));
				detalleServicio.getTipoComprobante().setNombre(UtilJdbc.obtenerCadena(rs, "tipoComprobanteNombre"));
				detalleServicio.getTipoComprobante().setAbreviatura(UtilJdbc.obtenerCadena(rs, "tipoComprobanteAbrev"));
				detalleServicio.setNroComprobante(UtilJdbc.obtenerCadena(rs, "numeroComprobante"));
				detalleServicio.getComprobanteAsociado().getTipoComprobante().setNombre(UtilJdbc.obtenerCadena(rs, "tipoObligacion"));
				detalleServicio.getComprobanteAsociado().getTipoComprobante().setAbreviatura(UtilJdbc.obtenerCadena(rs, "tipoObligacionAbrev"));
				detalleServicio.getComprobanteAsociado().setNumeroComprobante(UtilJdbc.obtenerCadena(rs, "numeroObligacion"));
				detalleServicio.getServicioPadre().setCodigoEntero(idServicio);
				
				resultado.add(detalleServicio);
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
				
			} catch (SQLException e) {
				throw new SQLException(e);
				
			}
		}
		
		return resultado;
	}
	
	@Override
	public List<Comprobante> consultaObligacionXPagar(Comprobante comprobante)
			throws SQLException {
		Connection conn = null;
		List<Comprobante> resultado = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_consultarobligacionxpagar(?,?,?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.OTHER);
			if (comprobante.getTipoComprobante().getCodigoEntero() != null && comprobante.getTipoComprobante().getCodigoEntero().intValue() != 0){
				cs.setInt(i++, comprobante.getTipoComprobante().getCodigoEntero().intValue());
			}
			else {
				cs.setNull(i++, Types.INTEGER);
			}
			if (StringUtils.isNotBlank(comprobante.getNumeroComprobante())){
				cs.setString(i++, comprobante.getNumeroComprobante());
			}
			else {
				cs.setNull(i++, Types.VARCHAR);
			}
			if (comprobante.getProveedor().getCodigoEntero() != null && comprobante.getProveedor().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, comprobante.getProveedor().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			cs.execute();
			
			rs = (ResultSet)cs.getObject(1);
			Comprobante comprobante2 = null;
			resultado = new ArrayList<Comprobante>();
			while (rs.next()){
				comprobante2 = new Comprobante();
				comprobante2.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				comprobante2.getTipoComprobante().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtipocomprobante"));
				comprobante2.getTipoComprobante().setNombre(UtilJdbc.obtenerCadena(rs, "nombrecomprobante"));
				comprobante2.setNumeroComprobante(UtilJdbc.obtenerCadena(rs, "numerocomprobante"));
				comprobante2.getProveedor().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idproveedor"));
				comprobante2.getProveedor().setNombres(UtilJdbc.obtenerCadena(rs, "nombres"));
				comprobante2.setFechaComprobante(UtilJdbc.obtenerFecha(rs, "fechacomprobante"));
				comprobante2.setFechaPago(UtilJdbc.obtenerFecha(rs, "fechapago"));
				comprobante2.setDetalleTextoComprobante(UtilJdbc.obtenerCadena(rs, "detallecomprobante"));
				comprobante2.setTotalIGV(UtilJdbc.obtenerBigDecimal(rs, "totaligv"));
				comprobante2.setTotalComprobante(UtilJdbc.obtenerBigDecimal(rs, "totalcomprobante"));
				
				resultado.add(comprobante2);
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
	public boolean registrarObligacionXPagar(Comprobante comprobante)
			throws SQLException {
		boolean resultado = false;
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresarobligacionxpagar(?,?,?,?,?,?,?,?,?,?,?,?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, comprobante.getTipoComprobante().getCodigoEntero().intValue());
			cs.setString(i++, comprobante.getNumeroComprobante());
			cs.setInt(i++, comprobante.getProveedor().getCodigoEntero().intValue());
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(comprobante.getFechaComprobante()));
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(comprobante.getFechaPago()));
			cs.setString(i++, comprobante.getDetalleTextoComprobante());
			cs.setBigDecimal(i++, comprobante.getTotalIGV());
			cs.setBigDecimal(i++, comprobante.getTotalComprobante());
			cs.setBoolean(i++, comprobante.isTieneDetraccion());
			cs.setBoolean(i++, comprobante.isTieneRetencion());
			cs.setString(i++, comprobante.getUsuarioCreacion());
			cs.setString(i++, comprobante.getIpCreacion());
			cs.execute();
			
			resultado = cs.getBoolean(1);
			
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
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
	public void registrarPagoObligacion(PagoServicio pago) throws SQLException {
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_registrarpagoobligacion(?,?,?,?,?,?,?,?,?,?,?,?)}";
		Connection conn = null;
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.INTEGER);
			cs.setInt(i++, pago.getIdObligacion().intValue());
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(pago.getFechaPago()));
			cs.setBigDecimal(i++, pago.getMontoPago());
			if (pago.getSustentoPagoByte()!=null){
				cs.setBinaryStream(i++, new ByteArrayInputStream(pago.getSustentoPagoByte()), pago.getSustentoPagoByte().length);
			}
			else{
				cs.setNull(i++, Types.VARBINARY);
			}
			if (StringUtils.isNotBlank(pago.getNombreArchivo())){
				cs.setString(i++, pago.getNombreArchivo());
			}
			else {
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(pago.getExtensionArchivo())){
				cs.setString(i++, pago.getExtensionArchivo());
			}
			else {
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(pago.getTipoContenido())){
				cs.setString(i++, pago.getTipoContenido());
			}
			else {
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(pago.getComentario())){
				cs.setString(i++, pago.getComentario());
			}
			else {
				cs.setNull(i++, Types.VARCHAR);
			}
			cs.setBoolean(i++, "D".equals(pago.getTipoPago().getCodigoCadena()));
			cs.setBoolean(i++, "R".equals(pago.getTipoPago().getCodigoCadena()));
			cs.setString(i++, pago.getUsuarioCreacion());
			cs.setString(i++, pago.getIpCreacion());
			cs.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				cs.close();
				cs = conn.prepareCall(sql);
				int i=1;
				cs.registerOutParameter(i++, Types.INTEGER);
				cs.setInt(i++, pago.getIdObligacion().intValue());
				cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(pago.getFechaPago()));
				cs.setBigDecimal(i++, pago.getMontoPago());
				if (pago.getSustentoPagoByte()!=null){
					cs.setBinaryStream(i++, new ByteArrayInputStream(pago.getSustentoPagoByte()));
				}
				else{
					cs.setNull(i++, Types.VARBINARY);
				}
				if (StringUtils.isNotBlank(pago.getComentario())){
					cs.setString(i++, pago.getComentario());
				}
				else {
					cs.setNull(i++, Types.VARCHAR);
				}
				cs.setBoolean(i++, pago.getTipoPago().getCodigoCadena().equals("D"));
				cs.setBoolean(i++, pago.getTipoPago().getCodigoCadena().equals("R"));
				cs.setString(i++, pago.getUsuarioCreacion());
				cs.setString(i++, pago.getIpCreacion());
				cs.execute();
			} catch (SQLException e1) {
				throw new SQLException(e);
			}
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		
	}
	
	@Override
	public boolean guardarRelacionComproObligacion(DetalleServicioAgencia detalle, Connection conn) throws SQLException, Exception{
		CallableStatement cs = null;
		String sql = "";
		boolean resultado = false;
		try{
			sql = "{ ? = call negocio.fn_registrarcomprobanteobligacion(?,?,?,?,?,?)}";
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, detalle.getIdComprobanteGenerado().intValue());
			cs.setInt(i++, detalle.getComprobanteAsociado().getCodigoEntero().intValue());
			cs.setInt(i++, detalle.getCodigoEntero().intValue());
			cs.setInt(i++, detalle.getServicioPadre().getCodigoEntero().intValue());
			cs.setString(i++, detalle.getUsuarioCreacion());
			cs.setString(i++, detalle.getIpCreacion());
			
			cs.execute();
			
			resultado = true;
		}
		catch (SQLException e){
			throw new SQLException(e);
		}
		finally{
			if (cs != null){
				cs.close();
			}
		}
		
		return resultado;
	}
	
	@Override
	public void actualizarRelacionComprobantes(boolean relacionComprobantes, ServicioAgencia servicio, Connection conn) throws SQLException, Exception{
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_actualizarrelacioncomprobantes(?,?,?,?)}";
		try {
			
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, servicio.getCodigoEntero().intValue());
			cs.setBoolean(i++, relacionComprobantes);
			cs.setString(i++, servicio.getUsuarioCreacion());
			cs.setString(i++, servicio.getIpCreacion());
			
			cs.execute();
			
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		
	}
	
	@Override
	public boolean grabarDocumentoAdicional(DocumentoAdicional documento, Connection conn) throws SQLException{
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_registrardocumentosustentoservicio(?,?,?,?,?,?,?,?,?)}";
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, documento.getIdServicio().intValue());
			cs.setInt(i++, documento.getDocumento().getCodigoEntero().intValue());
			cs.setString(i++, documento.getDescripcionArchivo());
			cs.setBinaryStream(i++, new ByteArrayInputStream(documento.getArchivo().getDatos()), documento.getArchivo().getDatos().length);
			cs.setString(i++, documento.getArchivo().getNombreArchivo());
			cs.setString(i++, documento.getArchivo().getExtensionArchivo());
			cs.setString(i++, documento.getArchivo().getContent());
			cs.setString(i++, documento.getUsuarioCreacion());
			cs.setString(i++, documento.getIpCreacion());
			cs.execute();
			
			return cs.getBoolean(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		return false;
	}
	
	@Override
	public List<DocumentoAdicional> listarDocumentosAdicionales(Integer idServicio) throws SQLException{
		List<DocumentoAdicional> resultado = null;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_listardocumentosadicionales(?)}";
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.setInt(i++, idServicio.intValue());
			cs.execute();
			
			rs = (ResultSet) cs.getObject(1);
			resultado = new ArrayList<DocumentoAdicional>();
			DocumentoAdicional documento = null;
			while (rs.next()){
				documento = new DocumentoAdicional();
				
				documento.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				documento.setIdServicio(UtilJdbc.obtenerNumero(rs, "idservicio"));
				documento.getDocumento().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtipodocumento"));
				documento.getDocumento().setNombre(UtilJdbc.obtenerCadena(rs, "nombredocumento"));
				documento.setDescripcionArchivo(UtilJdbc.obtenerCadena(rs, "descripciondocumento"));
				byte[] sustento = rs.getBytes("archivo");
				documento.getArchivo().setDatos(sustento);
				documento.getArchivo().setNombreArchivo(UtilJdbc.obtenerCadena(rs, "nombrearchivo"));
				documento.getArchivo().setContent(UtilJdbc.obtenerCadena(rs, "tipocontenido"));
				documento.getArchivo().setExtensionArchivo(UtilJdbc.obtenerCadena(rs, "extensionarchivo"));
				documento.setEditarDocumento(false);
				
				resultado.add(documento);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null){
					rs.close();
				}
				if (cs != null) {
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
	public boolean eliminarDocumentoAdicional(DocumentoAdicional documento, Connection conn) throws SQLException{
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_eliminardocumentosustentoservicio(?,?,?)}";
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, documento.getIdServicio().intValue());
			cs.setString(i++, documento.getUsuarioModificacion());
			cs.setString(i++, documento.getIpModificacion());
			cs.execute();
			
			return cs.getBoolean(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		return false;
	}
	
}

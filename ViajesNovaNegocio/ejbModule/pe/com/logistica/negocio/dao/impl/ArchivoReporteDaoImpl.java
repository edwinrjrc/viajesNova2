/**
 * 
 */
package pe.com.logistica.negocio.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.cargaexcel.ColumnasExcel;
import pe.com.logistica.bean.cargaexcel.ReporteArchivo;
import pe.com.logistica.negocio.dao.ArchivoReporteDao;

/**
 * @author Edwin
 *
 */
public class ArchivoReporteDaoImpl implements ArchivoReporteDao {

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.ArchivoReporteDao#registrarArchivoReporteCabecera(pe.com.logistica.bean.cargaexcel.ReporteArchivo, java.sql.Connection)
	 */
	@Override
	public Integer registrarArchivoReporteCabecera(
			ReporteArchivo reporteArchivo, Connection conn) throws SQLException {
		Integer resultado = 0;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresararchivocargado(?,?,?,?,?,?,?) }";
		
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.INTEGER);
			cs.setString(i++, reporteArchivo.getNombreArchivo());
			cs.setString(i++, reporteArchivo.getNombreReporte());
			cs.setInt(i++, reporteArchivo.getProveedor().getCodigoEntero().intValue());
			cs.setInt(i++, reporteArchivo.getNumeroFilas());
			cs.setInt(i++, reporteArchivo.getNumeroColumnas());
			cs.setString(i++, reporteArchivo.getUsuarioCreacion());
			cs.setString(i++, reporteArchivo.getIpCreacion());
			cs.execute();
			
			resultado = cs.getInt(1);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally{
			if (cs != null){
				cs.close();
			}
		}
		
		return resultado;
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.ArchivoReporteDao#registrarDetalleArchivoReporte(pe.com.logistica.bean.cargaexcel.ColumnasExcel, java.sql.Connection)
	 */
	@Override
	public boolean registrarDetalleArchivoReporte(ColumnasExcel columnasExcel,
			Connection conn) throws SQLException, Exception {
		boolean resultado = false;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresardetallearchivocargado(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";
		
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, columnasExcel.getIdArchivo().intValue());
			if (StringUtils.isNotBlank(columnasExcel.getColumna1().getValorCadena())){
				cs.setString(i++, columnasExcel.getColumna1().getValorCadena());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(columnasExcel.getColumna2().getValorCadena())){
				cs.setString(i++, columnasExcel.getColumna2().getValorCadena());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(columnasExcel.getColumna3().getValorCadena())){
				cs.setString(i++, columnasExcel.getColumna3().getValorCadena());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(columnasExcel.getColumna4().getValorCadena())){
				cs.setString(i++, columnasExcel.getColumna4().getValorCadena());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(columnasExcel.getColumna5().getValorCadena())){
				cs.setString(i++, columnasExcel.getColumna5().getValorCadena());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(columnasExcel.getColumna6().getValorCadena())){
				cs.setString(i++, columnasExcel.getColumna6().getValorCadena());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(columnasExcel.getColumna7().getValorCadena())){
				cs.setString(i++, columnasExcel.getColumna7().getValorCadena());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(columnasExcel.getColumna8().getValorCadena())){
				cs.setString(i++, columnasExcel.getColumna8().getValorCadena());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(columnasExcel.getColumna9().getValorCadena())){
				cs.setString(i++, columnasExcel.getColumna9().getValorCadena());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(columnasExcel.getColumna10().getValorCadena())){
				cs.setString(i++, columnasExcel.getColumna10().getValorCadena());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(columnasExcel.getColumna11().getValorCadena())){
				cs.setString(i++, columnasExcel.getColumna11().getValorCadena());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(columnasExcel.getColumna12().getValorCadena())){
				cs.setString(i++, columnasExcel.getColumna12().getValorCadena());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(columnasExcel.getColumna13().getValorCadena())){
				cs.setString(i++, columnasExcel.getColumna13().getValorCadena());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(columnasExcel.getColumna14().getValorCadena())){
				cs.setString(i++, columnasExcel.getColumna14().getValorCadena());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(columnasExcel.getColumna15().getValorCadena())){
				cs.setString(i++, columnasExcel.getColumna15().getValorCadena());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(columnasExcel.getColumna16().getValorCadena())){
				cs.setString(i++, columnasExcel.getColumna16().getValorCadena());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(columnasExcel.getColumna17().getValorCadena())){
				cs.setString(i++, columnasExcel.getColumna17().getValorCadena());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(columnasExcel.getColumna18().getValorCadena())){
				cs.setString(i++, columnasExcel.getColumna18().getValorCadena());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(columnasExcel.getColumna19().getValorCadena())){
				cs.setString(i++, columnasExcel.getColumna19().getValorCadena());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(columnasExcel.getColumna20().getValorCadena())){
				cs.setString(i++, columnasExcel.getColumna20().getValorCadena());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			cs.setBoolean(i++, columnasExcel.isSeleccionar());
			if (columnasExcel.getTipoComprobante().getCodigoEntero()!=null && columnasExcel.getTipoComprobante().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, columnasExcel.getTipoComprobante().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (StringUtils.isNotBlank(columnasExcel.getNumeroComprobante())){
				cs.setString(i++, columnasExcel.getNumeroComprobante());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			cs.setString(i++, columnasExcel.getUsuarioCreacion());
			cs.setString(i++, columnasExcel.getIpCreacion());
			cs.execute();
			
			resultado = cs.getBoolean(1);
		} catch (SQLException e) {
			throw new SQLException(e);
		} catch (Exception e){
			throw new Exception(e);
		} finally{
			if (cs != null){
				cs.close();
			}
		}
		
		return resultado;
	}

}

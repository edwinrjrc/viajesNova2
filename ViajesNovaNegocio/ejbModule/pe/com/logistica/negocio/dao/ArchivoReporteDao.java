/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.Connection;
import java.sql.SQLException;

import pe.com.logistica.bean.cargaexcel.ColumnasExcel;
import pe.com.logistica.bean.cargaexcel.ReporteArchivo;

/**
 * @author Edwin
 *
 */
public interface ArchivoReporteDao {

	public Integer registrarArchivoReporteCabecera (ReporteArchivo reporteArchivo, Connection conn) throws SQLException;
	
	public boolean registrarDetalleArchivoReporte (ColumnasExcel columnasExcel, Connection conn) throws SQLException, Exception;
	
	
}

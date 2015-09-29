/**
 * 
 */
package pe.com.logistica.web.servicio;

import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.reportes.ReporteVentas;

/**
 * @author EDWREB
 *
 */
public interface ReportesServicio {

	public List<DetalleServicioAgencia> reporteGeneralVentas(
			ReporteVentas reporteVentas) throws SQLException;

}

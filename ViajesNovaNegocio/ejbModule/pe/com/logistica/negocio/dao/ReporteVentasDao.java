/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.reportes.ReporteVentas;

/**
 * @author Edwin
 *
 */
public interface ReporteVentasDao {

	public List<DetalleServicioAgencia> reporteGeneralVentas(
			ReporteVentas reporteVentas) throws SQLException;
}

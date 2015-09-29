package pe.com.logistica.negocio.ejb;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Stateless;

import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.reportes.ReporteVentas;
import pe.com.logistica.negocio.dao.ReporteVentasDao;
import pe.com.logistica.negocio.dao.impl.ReporteVentasDaoImpl;

/**
 * Session Bean implementation class Reportes
 */
@Stateless(name = "ReportesSession")
public class ReportesSession implements ReportesSessionRemote,
		ReportesSessionLocal {

	@Override
	public List<DetalleServicioAgencia> reporteGeneralVentas(
			ReporteVentas reporteVentas) throws SQLException {
		ReporteVentasDao reporteVentasDao = new ReporteVentasDaoImpl();

		return reporteVentasDao.reporteGeneralVentas(reporteVentas);
	}
}

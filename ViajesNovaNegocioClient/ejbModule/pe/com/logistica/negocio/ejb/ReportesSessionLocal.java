package pe.com.logistica.negocio.ejb;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Local;

import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.reportes.ReporteVentas;

@Local
public interface ReportesSessionLocal {

	public List<DetalleServicioAgencia> reporteGeneralVentas(ReporteVentas reporteVentas) throws SQLException;
}

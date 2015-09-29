package pe.com.logistica.negocio.ejb;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;

import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.reportes.ReporteVentas;

@Remote
public interface ReportesSessionRemote {

	public List<DetalleServicioAgencia> reporteGeneralVentas(
			ReporteVentas reporteVentas) throws SQLException;
}

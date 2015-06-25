package pe.com.logistica.negocio.ejb;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Local;

import pe.com.logistica.bean.cargaexcel.ReporteArchivo;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.reportes.ReporteVentas;
import pe.com.logistica.negocio.exception.ConnectionException;

@Local
public interface ReportesSessionLocal {

	public List<DetalleServicioAgencia> reporteGeneralVentas(ReporteVentas reporteVentas) throws SQLException;
}

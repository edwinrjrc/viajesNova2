/**
 * 
 */
package pe.com.logistica.web.servicio;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;

/**
 * @author Edwin
 *
 */
public interface UtilNegocioServicio {
	
	public List<DetalleServicioAgencia> agruparServicios(List<DetalleServicioAgencia> listaServicios);

	List<DetalleServicioAgencia> agregarServicioVenta(
			List<DetalleServicioAgencia> listaServiciosVenta,
			DetalleServicioAgencia detalleServicio)
			throws ErrorRegistroDataException, SQLException, Exception;

	List<DetalleServicioAgencia> actualizarServicioVenta(
			List<DetalleServicioAgencia> listaServiciosVenta,
			DetalleServicioAgencia detalleServicio)
			throws ErrorRegistroDataException, SQLException, Exception;

	BigDecimal calcularPorcentajeComision(DetalleServicioAgencia detalleServicio)
			throws SQLException, Exception;
	
}

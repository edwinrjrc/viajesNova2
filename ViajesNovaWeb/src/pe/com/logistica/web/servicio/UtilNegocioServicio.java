/**
 * 
 */
package pe.com.logistica.web.servicio;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.jasper.DetalleServicio;
import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.Direccion;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.bean.negocio.ServicioNovios;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;

/**
 * @author Edwin
 *
 */
public interface UtilNegocioServicio {

	public List<DetalleServicioAgencia> agruparServicios(
			List<DetalleServicioAgencia> listaServicios);

	BigDecimal calcularPorcentajeComision(DetalleServicioAgencia detalleServicio)
			throws SQLException, Exception;

	List<DetalleServicioAgencia> agregarServicioVenta(Integer idMonedaServicio,
			List<DetalleServicioAgencia> listaServiciosVenta,
			DetalleServicioAgencia detalleServicio)
			throws ErrorRegistroDataException, SQLException, Exception;

	List<DetalleServicioAgencia> actualizarServicioVenta(
			Integer idMonedaServicio,
			List<DetalleServicioAgencia> listaServiciosVenta,
			DetalleServicioAgencia detalleServicio)
			throws ErrorRegistroDataException, SQLException, Exception;

	List<DetalleServicio> consultarServiciosVenta(Integer idServicio)
			throws SQLException;

	public Direccion agregarDireccion(Direccion direccion) throws SQLException,
			Exception;

	public Contacto agregarContacto(Contacto contacto) throws SQLException,
			Exception;

	ServicioNovios agregarServicioNovios(ServicioNovios servicioNovios)
			throws SQLException, Exception;

	BigDecimal calcularValorCuota(ServicioAgencia servicioAgencia)
			throws SQLException, Exception;
}

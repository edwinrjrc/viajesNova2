package pe.com.logistica.negocio.ejb;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.Local;

import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;

@Local
public interface UtilNegocioSessionLocal {

	List<DetalleServicioAgencia> agruparServiciosHijos(
			List<DetalleServicioAgencia> listaServicios);

	public List<DetalleServicioAgencia> agregarServicioVenta(
			Integer idMonedaServicio,
			List<DetalleServicioAgencia> listaServiciosVenta,
			DetalleServicioAgencia detalleServicio)
			throws ErrorRegistroDataException, SQLException, Exception;

	public List<DetalleServicioAgencia> actualizarServicioVenta(
			Integer idMonedaServicio,
			List<DetalleServicioAgencia> listaServiciosVenta,
			DetalleServicioAgencia detalleServicio)
			throws ErrorRegistroDataException, SQLException, Exception;

	public BigDecimal calculaPorcentajeComision(
			DetalleServicioAgencia detalleServicio) throws SQLException,
			Exception;

	public List<DetalleServicioAgencia> consultarServiciosVentaJR(
			Integer idServicio) throws SQLException;
}

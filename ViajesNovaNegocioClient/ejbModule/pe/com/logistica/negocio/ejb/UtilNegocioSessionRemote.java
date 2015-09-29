package pe.com.logistica.negocio.ejb;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;

import pe.com.logistica.bean.jasper.DetalleServicio;
import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.Direccion;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.bean.negocio.ServicioNovios;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;

@Remote
public interface UtilNegocioSessionRemote {

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

	public List<DetalleServicio> consultarServiciosVentaJR(Integer idServicio)
			throws SQLException;

	public Direccion agregarDireccion(Direccion direccion) throws SQLException,
			Exception;

	public Contacto agregarContacto(Contacto contacto) throws SQLException,
			Exception;

	ServicioNovios agregarServicio(ServicioNovios detalleServicio)
			throws SQLException, Exception;

	BigDecimal calcularValorCuota(ServicioAgencia servicioAgencia)
			throws SQLException, Exception;
}

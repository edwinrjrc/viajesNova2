package pe.com.logistica.negocio.ejb;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;

import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;

@Remote
public interface UtilNegocioSessionRemote {

	List<DetalleServicioAgencia> agruparServiciosHijos(
			List<DetalleServicioAgencia> listaServicios);
	
	public List<DetalleServicioAgencia> agregarServicioVenta(
			List<DetalleServicioAgencia> listaServiciosVenta,
			DetalleServicioAgencia detalleServicio)
			throws ErrorRegistroDataException, SQLException, Exception;

	public List<DetalleServicioAgencia> actualizarServicioVenta(
			List<DetalleServicioAgencia> listaServiciosVenta,
			DetalleServicioAgencia detalleServicio)
			throws ErrorRegistroDataException, SQLException, Exception;
}

package pe.com.logistica.negocio.ejb;

import java.util.List;

import javax.ejb.Local;

import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.ServicioAgencia;

@Local
public interface UtilNegocioSessionLocal {

	List<DetalleServicioAgencia> agruparServiciosHijos(
			List<DetalleServicioAgencia> listaServicios);
	
	ServicioAgencia colocarTipoNumeroComprobante(ServicioAgencia servicioAgencia);
}

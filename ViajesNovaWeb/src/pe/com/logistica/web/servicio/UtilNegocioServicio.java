/**
 * 
 */
package pe.com.logistica.web.servicio;

import java.util.List;

import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.ServicioAgencia;

/**
 * @author Edwin
 *
 */
public interface UtilNegocioServicio {
	
	public List<DetalleServicioAgencia> agruparServicios(List<DetalleServicioAgencia> listaServicios);

	ServicioAgencia colocarTipoNumeroComprobante(ServicioAgencia servicioAgencia);
	
}

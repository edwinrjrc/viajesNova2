/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.jasper.DetalleServicio;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.bean.negocio.ServicioProveedor;

/**
 * @author edwreb
 *
 */
public interface ServicioNegocioDao {

	public BigDecimal calcularCuota(ServicioAgencia servicioAgencia)
			throws SQLException;

	List<ServicioProveedor> proveedoresXServicio(BaseVO servicio)
			throws SQLException;

	public List<DetalleServicio> consultarServicioVentaJR(Integer idServicio)
			throws SQLException;
}

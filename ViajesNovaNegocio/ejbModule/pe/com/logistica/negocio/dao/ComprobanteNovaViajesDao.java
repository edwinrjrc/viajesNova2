/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.negocio.Comprobante;
import pe.com.logistica.bean.negocio.ComprobanteBusqueda;
import pe.com.logistica.bean.negocio.DetalleComprobante;

/**
 * @author Edwin
 *
 */
public interface ComprobanteNovaViajesDao {
	
	public Integer registrarComprobanteAdicional(Comprobante comprobante, Connection conn) throws SQLException;
	
	public Integer listarComprobantesAdicionales(Integer idServicio) throws SQLException;
	
	public Integer eliminarComprobantesAdicionales(Integer idServicio) throws SQLException;
	
	public List<Comprobante> consultarComprobantes(ComprobanteBusqueda comprobanteBusqueda) throws SQLException;
	
	public List<DetalleComprobante> consultarDetalleComprobante (Integer idComprobante) throws SQLException;

	Comprobante consultarObligacion(Integer idObligacion) throws SQLException;
	
}

/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import pe.com.logistica.bean.negocio.TipoCambio;

/**
 * @author EDWREB
 *
 */
public interface TipoCambioDao {

	public boolean registrarTipoCambio(TipoCambio tipoCambio)
			throws SQLException;

	List<TipoCambio> listarTipoCambio(Date fecha) throws SQLException;

	TipoCambio consultarTipoCambio(Integer idMonedaOrigen,
			Integer idMonedaDestino, Connection conn) throws SQLException;
}

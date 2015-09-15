/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.negocio.TipoCambio;

/**
 * @author EDWREB
 *
 */
public interface TipoCambioDao {

	public boolean registrarTipoCambio(TipoCambio tipoCambio) throws SQLException;
	
	public List<TipoCambio> listarTipoCambio() throws SQLException;

	public TipoCambio consultarTipoCambio(Integer idMonedaOrigen, Integer idMonedaDestino) throws SQLException;
}

/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.negocio.CuentaBancaria;

/**
 * @author EDWREB
 *
 */
public interface CuentaBancariaDao {

	public List<CuentaBancaria> listarCuentasBancarias() throws SQLException;
	
	public boolean registrarCuentaBancaria(CuentaBancaria cuentaBancaria) throws SQLException;
	
	public boolean actualizarCuentaBancaria(CuentaBancaria cuentaBancaria) throws SQLException;
	
	public CuentaBancaria consultaCuentaBancaria(Integer idCuenta) throws SQLException;
}

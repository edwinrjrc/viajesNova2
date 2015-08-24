/**
 * 
 */
package pe.com.logistica.negocio.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import pe.com.logistica.bean.negocio.CuentaBancaria;
import pe.com.logistica.negocio.dao.CuentaBancariaDao;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilJdbc;

/**
 * @author EDWREB
 *
 */
public class CuentaBancariaDaoImpl implements CuentaBancariaDao {

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.CuentaBancariaDao#listarCuentasBancarias()
	 */
	@Override
	public List<CuentaBancaria> listarCuentasBancarias() throws SQLException {
		List<CuentaBancaria> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "";
		
		try{
			sql = "{ ? = call negocio.fn_listarcuentasbancarias() }";
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			cs.registerOutParameter(1, Types.OTHER);
			cs.execute();
			
			rs = (ResultSet)cs.getObject(1);
			CuentaBancaria cuentaBancaria = null;
			resultado = new ArrayList<CuentaBancaria>();
			while (rs.next()){
				cuentaBancaria = new CuentaBancaria();
				cuentaBancaria.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				cuentaBancaria.setNombreCuenta(UtilJdbc.obtenerCadena(rs, "nombrecuenta"));
				cuentaBancaria.setNumeroCuenta(UtilJdbc.obtenerCadena(rs, "numerocuenta"));
				cuentaBancaria.getBanco().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idbanco"));
				cuentaBancaria.setSaldo(UtilJdbc.obtenerBigDecimal(rs, "saldocuenta"));
				resultado.add(cuentaBancaria);
			}
		}
		catch (SQLException e){
			throw new SQLException(e);
		}
		finally{
			if (rs != null){
				rs.close();
			}
			if (cs != null){
				cs.close();
			}
			if (conn != null){
				conn.close();
			}
		}
		
		return resultado;
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.CuentaBancariaDao#registrarCuentaBancaria(pe.com.logistica.bean.negocio.CuentaBancaria)
	 */
	@Override
	public boolean registrarCuentaBancaria(CuentaBancaria cuentaBancaria)
			throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.CuentaBancariaDao#actualizarCuentaBancaria(pe.com.logistica.bean.negocio.CuentaBancaria)
	 */
	@Override
	public boolean actualizarCuentaBancaria(CuentaBancaria cuentaBancaria)
			throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.CuentaBancariaDao#consultaCuentaBancaria(pe.com.logistica.bean.negocio.CuentaBancaria)
	 */
	@Override
	public CuentaBancaria consultaCuentaBancaria(Integer idCuenta)
			throws SQLException {
		CuentaBancaria resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "";
		
		try{
			sql = "{ ? = call negocio.fn_consultacuentabancaria(?) }";
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.setInt(i++, idCuenta.intValue());
			cs.execute();
			
			rs = (ResultSet)cs.getObject(1);
			if (rs.next()){
				resultado = new CuentaBancaria();
				resultado.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				resultado.setNombreCuenta(UtilJdbc.obtenerCadena(rs, "nombrecuenta"));
				resultado.setNumeroCuenta(UtilJdbc.obtenerCadena(rs, "numerocuenta"));
				resultado.getBanco().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idbanco"));
				resultado.setSaldo(UtilJdbc.obtenerBigDecimal(rs, "saldocuenta"));
			}
		}
		catch (SQLException e){
			throw new SQLException(e);
		}
		finally{
			if (rs != null){
				rs.close();
			}
			if (cs != null){
				cs.close();
			}
			if (conn != null){
				conn.close();
			}
		}
		
		return resultado;
	}

}

/**
 * 
 */
package pe.com.logistica.negocio.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import pe.com.logistica.bean.negocio.Ubigeo;
import pe.com.logistica.negocio.dao.UbigeoDao;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilJdbc;

/**
 * @author Edwin
 *
 */
public class UbigeoDaoImpl implements UbigeoDao {

	/**
	 * 
	 */
	public UbigeoDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Ubigeo consultarUbigeo(String idUbigeo) throws SQLException {
		Ubigeo resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select * from soporte.vw_ubigeo where id = ?";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			cs.setString(1, idUbigeo);
			rs = cs.executeQuery();

			if (rs.next()) {
				resultado = new Ubigeo();
				resultado.setCodigoCadena(UtilJdbc.obtenerCadena(rs, "id"));
				resultado.getDepartamento().setCodigoCadena(
						UtilJdbc.obtenerCadena(rs, "iddepartamento"));
				resultado.getProvincia().setCodigoCadena(
						UtilJdbc.obtenerCadena(rs, "idprovincia"));
				resultado.getDistrito().setCodigoCadena(
						UtilJdbc.obtenerCadena(rs, "iddistrito"));
				resultado.setNombre(UtilJdbc.obtenerCadena(rs, "descripcion"));
			}
		} catch (SQLException e) {
			resultado = null;
			throw new SQLException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null) {
						conn.close();
					}
					throw new SQLException(e);
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}

		return resultado;
	}

}

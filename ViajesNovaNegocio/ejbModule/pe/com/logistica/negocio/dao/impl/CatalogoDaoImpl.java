/**
 * 
 */
package pe.com.logistica.negocio.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.negocio.dao.CatalogoDao;
import pe.com.logistica.negocio.exception.ConnectionException;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilJdbc;

/**
 * @author Edwin
 *
 */
public class CatalogoDaoImpl implements CatalogoDao {

	/**
	 * 
	 */
	public CatalogoDaoImpl() {
	}

	@Override
	public List<BaseVO> listarRoles() throws ConnectionException, SQLException {
		List<BaseVO> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select id, nombre from seguridad.rol";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			rs = cs.executeQuery();

			resultado = new ArrayList<BaseVO>();
			BaseVO rol = null;
			while (rs.next()) {
				rol = new BaseVO();
				rol.setCodigoEntero(rs.getInt("id"));
				rol.setNombre(UtilJdbc.obtenerCadena(rs, "nombre"));
				resultado.add(rol);
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
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}

		return resultado;
	}

	@Override
	public List<BaseVO> listarCatalogoMaestro(int maestro)
			throws ConnectionException, SQLException {
		List<BaseVO> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select * from soporte.vw_catalogomaestro where idmaestro = ?";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			cs.setInt(1, maestro);
			rs = cs.executeQuery();

			resultado = new ArrayList<BaseVO>();
			BaseVO maestroVO = null;
			while (rs.next()) {
				maestroVO = new BaseVO();
				maestroVO.setCodigoEntero(rs.getInt("id"));
				maestroVO.setNombre(UtilJdbc.obtenerCadena(rs, "nombre"));
				resultado.add(maestroVO);
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
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}

		return resultado;
	}

	@Override
	public List<BaseVO> listaDepartamento() throws ConnectionException,
			SQLException {
		List<BaseVO> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select * from soporte.vw_catalogodepartamento";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			rs = cs.executeQuery();

			resultado = new ArrayList<BaseVO>();
			BaseVO maestroVO = null;
			while (rs.next()) {
				maestroVO = new BaseVO();
				maestroVO.setCodigoCadena(rs.getString("iddepartamento"));
				maestroVO.setNombre(UtilJdbc.obtenerCadena(rs, "descripcion"));
				resultado.add(maestroVO);
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
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}

		return resultado;
	}

	@Override
	public List<BaseVO> listaProvincia(String idDepartamento)
			throws ConnectionException, SQLException {
		List<BaseVO> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select * from soporte.vw_catalogoprovincia where iddepartamento = ?";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			cs.setString(1, idDepartamento);
			rs = cs.executeQuery();

			resultado = new ArrayList<BaseVO>();
			BaseVO maestroVO = null;
			while (rs.next()) {
				maestroVO = new BaseVO();
				maestroVO.setCodigoCadena(rs.getString("idprovincia"));
				maestroVO.setNombre(UtilJdbc.obtenerCadena(rs, "descripcion"));
				resultado.add(maestroVO);
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
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}

		return resultado;
	}

	@Override
	public List<BaseVO> listaDistrito(String idDepartamento, String idProvincia)
			throws ConnectionException, SQLException {
		List<BaseVO> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select * from soporte.vw_catalogodistrito where iddepartamento = ? and idprovincia = ?";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			cs.setString(1, idDepartamento);
			cs.setString(2, idProvincia);
			rs = cs.executeQuery();

			resultado = new ArrayList<BaseVO>();
			BaseVO maestroVO = null;
			while (rs.next()) {
				maestroVO = new BaseVO();
				maestroVO.setCodigoCadena(rs.getString("iddistrito"));
				maestroVO.setNombre(UtilJdbc.obtenerCadena(rs, "descripcion"));
				resultado.add(maestroVO);
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
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}

		return resultado;
	}

}

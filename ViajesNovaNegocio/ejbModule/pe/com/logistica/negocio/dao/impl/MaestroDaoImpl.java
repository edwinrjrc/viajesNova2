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

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Maestro;
import pe.com.logistica.bean.negocio.Pais;
import pe.com.logistica.negocio.dao.MaestroDao;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilJdbc;

/**
 * @author Edwin
 * 
 */
public class MaestroDaoImpl implements MaestroDao {

	/**
	 * 
	 */
	public MaestroDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pe.com.logistica.negocio.dao.MaestroDao#listarMaestros()
	 */
	@Override
	public List<Maestro> listarMaestros() throws SQLException {
		List<Maestro> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select * from soporte.vw_listamaestros";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			rs = cs.executeQuery();

			resultado = new ArrayList<Maestro>();
			Maestro maestro = null;
			while (rs.next()) {
				maestro = new Maestro();
				maestro.setCodigoEntero(rs.getInt("id"));
				maestro.setNombre(UtilJdbc.obtenerCadena(rs, "nombre"));
				maestro.setDescripcion(UtilJdbc
						.obtenerCadena(rs, "descripcion"));
				maestro.getEstado().setCodigoCadena(
						UtilJdbc.obtenerCadena(rs, "estado"));
				maestro.getEstado().setNombre(
						UtilJdbc.obtenerCadena(rs, "descestado"));
				resultado.add(maestro);
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

	@Override
	public Maestro consultarMaestro(int id) throws SQLException {
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select * from soporte.vw_listamaestros where id = ?";
		Maestro maestro = null;
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			cs.setInt(1, id);
			rs = cs.executeQuery();

			if (rs.next()) {
				maestro = new Maestro();
				maestro.setCodigoEntero(rs.getInt("id"));
				maestro.setCodigoMaestro(rs.getInt("idmaestro"));
				maestro.setNombre(UtilJdbc.obtenerCadena(rs, "nombre"));
				maestro.setDescripcion(UtilJdbc
						.obtenerCadena(rs, "descripcion"));
				maestro.getEstado().setCodigoCadena(
						UtilJdbc.obtenerCadena(rs, "estado"));
				maestro.getEstado().setNombre(
						UtilJdbc.obtenerCadena(rs, "descestado"));
			}
		} catch (SQLException e) {
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

		return maestro;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pe.com.logistica.negocio.dao.MaestroDao#listarHijosMaestro(int)
	 */
	@Override
	public List<Maestro> listarHijosMaestro(int idmaestro) throws SQLException {
		List<Maestro> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select * from soporte.vw_listahijosmaestro where idmaestro = ?";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			cs.setInt(1, idmaestro);
			rs = cs.executeQuery();

			resultado = new ArrayList<Maestro>();
			Maestro maestro = null;
			while (rs.next()) {
				maestro = new Maestro();
				maestro.setCodigoEntero(rs.getInt("id"));
				maestro.setNombre(UtilJdbc.obtenerCadena(rs, "nombre"));
				maestro.setDescripcion(UtilJdbc
						.obtenerCadena(rs, "descripcion"));
				maestro.getEstado().setCodigoCadena(
						UtilJdbc.obtenerCadena(rs, "estado"));
				maestro.getEstado().setNombre(
						UtilJdbc.obtenerCadena(rs, "descestado"));
				resultado.add(maestro);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pe.com.logistica.negocio.dao.MaestroDao#ingresarMaestro(pe.com.logistica
	 * .bean.negocio.Maestro)
	 */
	@Override
	public boolean ingresarMaestro(Maestro maestro) throws SQLException {
		boolean resultado = false;
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call soporte.fn_ingresarmaestro(?,?) }";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setString(i++, UtilJdbc.convertirMayuscula(maestro.getNombre()));
			cs.setString(i++,
					UtilJdbc.convertirMayuscula(maestro.getDescripcion()));

			cs.execute();

			resultado = cs.getBoolean(1);

		} catch (SQLException e) {
			resultado = false;
			throw new SQLException(e);
		} finally {
			try {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pe.com.logistica.negocio.dao.MaestroDao#ingresarHijoMaestro(pe.com.logistica
	 * .bean.negocio.Maestro)
	 */
	@Override
	public boolean ingresarHijoMaestro(Maestro maestro) throws SQLException {
		boolean resultado = false;
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call soporte.fn_ingresarhijomaestro(?,?,?,?) }";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, maestro.getCodigoMaestro());
			cs.setString(i++, UtilJdbc.convertirMayuscula(maestro.getNombre()));
			cs.setString(i++,
					UtilJdbc.convertirMayuscula(maestro.getDescripcion()));
			cs.setString(i++,
					UtilJdbc.convertirMayuscula(maestro.getAbreviatura()));

			cs.execute();

			resultado = cs.getBoolean(1);

		} catch (SQLException e) {
			resultado = false;
			throw new SQLException(e);
		} finally {
			try {
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

	@Override
	public Maestro consultarHijoMaestro(Maestro hijoMaestro)
			throws SQLException {
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select * from soporte.vw_listahijosmaestro where idmaestro = ? and id = ?";
		Maestro maestro = null;
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			cs.setInt(1, hijoMaestro.getCodigoMaestro());
			cs.setInt(2, hijoMaestro.getCodigoEntero());
			rs = cs.executeQuery();

			if (rs.next()) {
				maestro = new Maestro();
				maestro.setCodigoEntero(rs.getInt("id"));
				maestro.setNombre(UtilJdbc.obtenerCadena(rs, "nombre"));
				maestro.setCodigoMaestro(rs.getInt("idmaestro"));
				maestro.setDescripcion(UtilJdbc
						.obtenerCadena(rs, "descripcion"));
				maestro.getEstado().setCodigoCadena(
						UtilJdbc.obtenerCadena(rs, "estado"));
				maestro.getEstado().setNombre(
						UtilJdbc.obtenerCadena(rs, "descestado"));
				maestro.setAbreviatura(UtilJdbc
						.obtenerCadena(rs, "abreviatura"));
			}
		} catch (SQLException e) {
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

		return maestro;
	}

	@Override
	public boolean actualizarMaestro(Maestro maestro) throws SQLException {
		boolean resultado = false;
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{? = call soporte.fn_actualizarmaestro(?,?,?,?,?,?,?)}";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, maestro.getCodigoEntero());
			cs.setInt(i++, maestro.getCodigoMaestro());
			cs.setString(i++, UtilJdbc.convertirMayuscula(maestro.getNombre()));
			if (StringUtils.isNotBlank(maestro.getDescripcion())) {
				cs.setString(i++,
						UtilJdbc.convertirMayuscula(maestro.getDescripcion()));
			} else {
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(maestro.getEstado().getCodigoCadena())) {
				cs.setString(i++, UtilJdbc.convertirMayuscula(maestro
						.getEstado().getCodigoCadena()));
			} else {
				cs.setNull(i++, Types.VARCHAR);
			}

			cs.setInt(i++, maestro.getOrden());
			if (StringUtils.isNotBlank(maestro.getAbreviatura())) {
				cs.setString(i++,
						UtilJdbc.convertirMayuscula(maestro.getAbreviatura()));
			} else {
				cs.setNull(i++, Types.VARCHAR);
			}
			cs.execute();

			resultado = cs.getBoolean(1);

		} catch (SQLException e) {
			resultado = false;
			throw new SQLException(e);
		} finally {
			try {
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

	@Override
	public List<BaseVO> listarPaises(int idcontinente) throws SQLException {
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call soporte.fn_listarpaises(?) }";
		List<BaseVO> listaPaises = null;
		try {
			conn = UtilConexion.obtenerConexion();

			cs = conn.prepareCall(sql);
			cs.registerOutParameter(1, Types.OTHER);
			cs.setInt(2, idcontinente);
			cs.execute();
			rs = (ResultSet) cs.getObject(1);

			listaPaises = new ArrayList<BaseVO>();
			BaseVO pais = null;
			while (rs.next()) {
				pais = new BaseVO();
				pais.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				pais.setNombre(UtilJdbc.obtenerCadena(rs, "descripcion"));
				listaPaises.add(pais);
			}

		} catch (SQLException e) {
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

		return listaPaises;
	}

	@Override
	public boolean ingresarPais(Pais pais) throws SQLException {
		boolean resultado = false;
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call soporte.fn_ingresarpais(?,?,?,?) }";
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setString(i++,
					UtilJdbc.convertirMayuscula(pais.getDescripcion()));
			cs.setInt(i++, pais.getContinente().getCodigoEntero().intValue());
			cs.setString(i++,
					UtilJdbc.convertirMayuscula(pais.getUsuarioCreacion()));
			cs.setString(i++, UtilJdbc.convertirMayuscula(pais.getIpCreacion()));
			cs.execute();

			resultado = cs.getBoolean(1);
		} catch (SQLException e) {
			resultado = false;
			throw new SQLException(e);
		} finally {
			try {
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

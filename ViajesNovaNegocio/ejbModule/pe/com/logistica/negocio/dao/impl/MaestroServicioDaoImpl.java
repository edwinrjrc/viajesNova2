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
import pe.com.logistica.bean.negocio.MaestroServicio;
import pe.com.logistica.negocio.dao.MaestroServicioDao;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilJdbc;

/**
 * @author edwreb
 *
 */
public class MaestroServicioDaoImpl implements MaestroServicioDao {

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.MaestroServicioDao#listarMaestroServicios()
	 */
	@Override
	public List<MaestroServicio> listarMaestroServicios() throws SQLException {
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_listarmaestroservicios() }";
		ResultSet rs = null;
		List<MaestroServicio> resultado = null;

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.execute();

			rs = (ResultSet) cs.getObject(1);
			resultado = new ArrayList<MaestroServicio>();
			MaestroServicio bean = null;
			while (rs.next()) {
				bean = new MaestroServicio();
				bean.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				bean.setNombre(UtilJdbc.obtenerCadena(rs, "nombre"));
				bean.setDescripcionCorta(UtilJdbc.obtenerCadena(rs, "desccorta"));
				bean.setDescripcion(UtilJdbc.obtenerCadena(rs, "desclarga"));
				bean.setRequiereFee(UtilJdbc.obtenerBoolean(rs, "requierefee"));
				bean.setPagaImpto(UtilJdbc.obtenerBoolean(rs, "pagaimpto"));
				bean.setCargaComision(UtilJdbc.obtenerBoolean(rs, "cargacomision"));
				bean.setServicioPadre(UtilJdbc.obtenerBoolean(rs, "esserviciopadre"));
				resultado.add(bean);
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

		return resultado;
	}
	
	@Override
	public List<MaestroServicio> listarMaestroServiciosAdm() throws SQLException {
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_listarmaestroserviciosadm() }";
		ResultSet rs = null;
		List<MaestroServicio> resultado = null;

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.execute();

			rs = (ResultSet) cs.getObject(1);
			resultado = new ArrayList<MaestroServicio>();
			MaestroServicio bean = null;
			while (rs.next()) {
				bean = new MaestroServicio();
				bean.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				bean.setNombre(UtilJdbc.obtenerCadena(rs, "nombre"));
				bean.setDescripcionCorta(UtilJdbc.obtenerCadena(rs, "desccorta"));
				bean.setDescripcion(UtilJdbc.obtenerCadena(rs, "desclarga"));
				bean.setRequiereFee(UtilJdbc.obtenerBoolean(rs, "requierefee"));
				bean.setPagaImpto(UtilJdbc.obtenerBoolean(rs, "pagaimpto"));
				bean.setCargaComision(UtilJdbc.obtenerBoolean(rs, "cargacomision"));
				resultado.add(bean);
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

		return resultado;
	}
	
	@Override
	public List<MaestroServicio> listarMaestroServiciosFee() throws SQLException {
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_listarmaestroserviciosfee() }";
		ResultSet rs = null;
		List<MaestroServicio> resultado = null;

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.execute();

			rs = (ResultSet) cs.getObject(1);
			resultado = new ArrayList<MaestroServicio>();
			MaestroServicio bean = null;
			while (rs.next()) {
				bean = new MaestroServicio();
				bean.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				bean.setNombre(UtilJdbc.obtenerCadena(rs, "nombre"));
				bean.setDescripcionCorta(UtilJdbc.obtenerCadena(rs, "desccorta"));
				bean.setDescripcion(UtilJdbc.obtenerCadena(rs, "desclarga"));
				bean.setRequiereFee(UtilJdbc.obtenerBoolean(rs, "requierefee"));
				bean.setPagaImpto(UtilJdbc.obtenerBoolean(rs, "pagaimpto"));
				bean.setCargaComision(UtilJdbc.obtenerBoolean(rs, "cargacomision"));
				resultado.add(bean);
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

		return resultado;
	}
	
	@Override
	public List<MaestroServicio> listarMaestroServiciosImpto() throws SQLException {
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_listarmaestroserviciosimpto() }";
		ResultSet rs = null;
		List<MaestroServicio> resultado = null;

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.execute();

			rs = (ResultSet) cs.getObject(1);
			resultado = new ArrayList<MaestroServicio>();
			MaestroServicio bean = null;
			while (rs.next()) {
				bean = new MaestroServicio();
				bean.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				bean.setNombre(UtilJdbc.obtenerCadena(rs, "nombre"));
				bean.setDescripcionCorta(UtilJdbc.obtenerCadena(rs, "desccorta"));
				bean.setDescripcion(UtilJdbc.obtenerCadena(rs, "desclarga"));
				bean.setRequiereFee(UtilJdbc.obtenerBoolean(rs, "requierefee"));
				bean.setPagaImpto(UtilJdbc.obtenerBoolean(rs, "pagaimpto"));
				bean.setCargaComision(UtilJdbc.obtenerBoolean(rs, "cargacomision"));
				resultado.add(bean);
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

		return resultado;
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.MaestroServicioDao#ingresarMaestroServicio(pe.com.logistica.bean.negocio.MaestroServicio)
	 */
	@Override
	public Integer ingresarMaestroServicio(MaestroServicio servicio)
			throws SQLException {
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresarservicio(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";
		Integer resultado = null;

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.INTEGER);
			cs.setString(i++, UtilJdbc.convertirMayuscula(servicio.getNombre()));
			if (StringUtils.isNotBlank(servicio.getDescripcionCorta())){
				cs.setString(i++, UtilJdbc.convertirMayuscula(servicio.getDescripcionCorta()));
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(servicio.getDescripcion())){
				cs.setString(i++, UtilJdbc.convertirMayuscula(servicio.getDescripcion()));
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			cs.setBoolean(i++, servicio.isRequiereFee());
			if (servicio.getServicioFee().getCodigoEntero()!= null && servicio.getServicioFee().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicio.getServicioFee().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			
			cs.setBoolean(i++, servicio.isPagaImpto());
			if (servicio.getServicioImpto().getCodigoEntero()!= null && servicio.getServicioImpto().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicio.getServicioImpto().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			cs.setBoolean(i++, servicio.isCargaComision());
			cs.setBoolean(i++, servicio.isCargaIgv());
			if (servicio.getValorComision() != null){
				cs.setBigDecimal(i++, servicio.getValorComision());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			cs.setString(i++, servicio.getUsuarioCreacion());
			cs.setString(i++, servicio.getIpCreacion());
			if (servicio.getParametroAsociado().getCodigoEntero()!= null && servicio.getParametroAsociado().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicio.getParametroAsociado().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			cs.setBoolean(i++, true);
			cs.setBoolean(i++, servicio.isServicioPadre());
			cs.execute();

			resultado = cs.getInt(1);
		} catch (SQLException e) {
			resultado = null;
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

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.MaestroServicioDao#actualizarMaestroServicio(pe.com.logistica.bean.negocio.MaestroServicio)
	 */
	@Override
	public boolean actualizarMaestroServicio(MaestroServicio servicio)
			throws SQLException {
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_actualizarservicio(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";
		boolean resultado = false;

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, servicio.getCodigoEntero().intValue());
			cs.setString(i++, UtilJdbc.convertirMayuscula(servicio.getNombre()));
			if (StringUtils.isNotBlank(servicio.getDescripcionCorta())){
				cs.setString(i++, UtilJdbc.convertirMayuscula(servicio.getDescripcionCorta()));
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(servicio.getDescripcion())){
				cs.setString(i++, UtilJdbc.convertirMayuscula(servicio.getDescripcion()));
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			cs.setBoolean(i++, servicio.isRequiereFee());
			if (servicio.getServicioFee().getCodigoEntero()!= null && servicio.getServicioFee().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicio.getServicioFee().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			cs.setBoolean(i++, servicio.isPagaImpto());
			if (servicio.getServicioImpto().getCodigoEntero()!= null && servicio.getServicioImpto().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicio.getServicioImpto().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			cs.setBoolean(i++, servicio.isCargaComision());
			cs.setBoolean(i++, servicio.isEsImpuesto());
			cs.setBoolean(i++, servicio.isEsFee());
			cs.setString(i++, servicio.getUsuarioModificacion());
			cs.setString(i++, servicio.getIpModificacion());
			if (servicio.getParametroAsociado().getCodigoEntero()!= null && servicio.getParametroAsociado().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicio.getParametroAsociado().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			cs.setBoolean(i++, servicio.isVisible());
			cs.setBoolean(i++, servicio.isServicioPadre());
			cs.execute();

			resultado = cs.getBoolean(1);
		} catch (SQLException e) {
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

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.MaestroServicioDao#consultarMaestroServicio(int)
	 */
	@Override
	public MaestroServicio consultarMaestroServicio(int idMaestroServicio)
			throws SQLException {
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_consultarservicio(?) }";
		ResultSet rs = null;
		MaestroServicio resultado = null;

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.setInt(i++, idMaestroServicio);
			cs.execute();

			rs = (ResultSet) cs.getObject(1);
			if (rs.next()) {
				resultado = new MaestroServicio();
				resultado.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				resultado.setNombre(UtilJdbc.obtenerCadena(rs, "nombre"));
				resultado.setDescripcionCorta(UtilJdbc.obtenerCadena(rs, "desccorta"));
				resultado.setDescripcion(UtilJdbc.obtenerCadena(rs, "desclarga"));
				resultado.setRequiereFee(UtilJdbc.obtenerBoolean(rs, "requierefee"));
				resultado.getServicioFee().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idmaeserfee"));
				resultado.setPagaImpto(UtilJdbc.obtenerBoolean(rs, "pagaimpto"));
				resultado.getServicioImpto().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idmaeserimpto"));
				resultado.setCargaComision(UtilJdbc.obtenerBoolean(rs, "cargacomision"));
				resultado.setEsImpuesto(UtilJdbc.obtenerBoolean(rs, "esimpuesto"));
				resultado.setEsFee(UtilJdbc.obtenerBoolean(rs, "esfee"));
				resultado.getParametroAsociado().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idparametroasociado"));
				resultado.setVisible(UtilJdbc.obtenerBoolean(rs, "visible"));
				resultado.setServicioPadre(UtilJdbc.obtenerBoolean(rs, "esserviciopadre"));
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
					throw new SQLException(e1);
				}
			}
		}

		return resultado;
	}
	
	@Override
	public MaestroServicio consultarMaestroServicio(int idMaestroServicio, Connection conn)
			throws SQLException {
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_consultarservicio(?) }";
		ResultSet rs = null;
		MaestroServicio resultado = null;

		try {
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.setInt(i++, idMaestroServicio);
			cs.execute();

			rs = (ResultSet) cs.getObject(1);
			if (rs.next()) {
				resultado = new MaestroServicio();
				resultado.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				resultado.setNombre(UtilJdbc.obtenerCadena(rs, "nombre"));
				resultado.setDescripcionCorta(UtilJdbc.obtenerCadena(rs, "desccorta"));
				resultado.setDescripcion(UtilJdbc.obtenerCadena(rs, "desclarga"));
				resultado.setRequiereFee(UtilJdbc.obtenerBoolean(rs, "requierefee"));
				resultado.getServicioFee().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idmaeserfee"));
				resultado.setPagaImpto(UtilJdbc.obtenerBoolean(rs, "pagaimpto"));
				resultado.getServicioImpto().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idmaeserimpto"));
				resultado.setCargaComision(UtilJdbc.obtenerBoolean(rs, "cargacomision"));
				resultado.setEsImpuesto(UtilJdbc.obtenerBoolean(rs, "esimpuesto"));
				resultado.setEsFee(UtilJdbc.obtenerBoolean(rs, "esfee"));
				resultado.getParametroAsociado().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idparametroasociado"));
				resultado.setVisible(UtilJdbc.obtenerBoolean(rs, "visible"));
				resultado.setServicioPadre(UtilJdbc.obtenerBoolean(rs, "esserviciopadre"));
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
			} catch (SQLException e) {
				try {
					if (conn != null) {
						conn.close();
					}
					throw new SQLException(e);
				} catch (SQLException e1) {
					throw new SQLException(e1);
				}
			}
		}

		return resultado;
	}
	
	@Override
	public List<MaestroServicio> listarMaestroServiciosIgv() throws SQLException {
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_listarmaestroserviciosigv() }";
		ResultSet rs = null;
		List<MaestroServicio> resultado = null;

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.execute();

			rs = (ResultSet) cs.getObject(1);
			resultado = new ArrayList<MaestroServicio>();
			MaestroServicio bean = null;
			while (rs.next()) {
				bean = new MaestroServicio();
				bean.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				bean.setNombre(UtilJdbc.obtenerCadena(rs, "nombre"));
				bean.setDescripcionCorta(UtilJdbc.obtenerCadena(rs, "desccorta"));
				bean.setDescripcion(UtilJdbc.obtenerCadena(rs, "desclarga"));
				bean.setRequiereFee(UtilJdbc.obtenerBoolean(rs, "requierefee"));
				bean.setPagaImpto(UtilJdbc.obtenerBoolean(rs, "pagaimpto"));
				bean.setCargaComision(UtilJdbc.obtenerBoolean(rs, "cargacomision"));
				resultado.add(bean);
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

		return resultado;
	}

	@Override
	public void ingresarServicioMaestroServicio(Integer idServicio, List<BaseVO> listaMaeServicioImpto) throws SQLException, Exception{
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresarserviciomaestroservicio(?,?) }";

		try {
			conn = UtilConexion.obtenerConexion();
			
			for (BaseVO idServicioDepende : listaMaeServicioImpto){
				cs = conn.prepareCall(sql);
				int i = 1;
				cs.registerOutParameter(i++, Types.BOOLEAN);
				cs.setInt(i++, idServicio);
				cs.setInt(i++, idServicioDepende.getCodigoEntero());
				
				cs.execute();
				if (cs != null){
					cs.close();
				}
			}
			

		} catch (SQLException e) {
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

	}
	
	@Override
	public List<BaseVO> consultarServicioDependientes(Integer idServicio) throws SQLException {
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_consultarserviciodependientes(?) }";
		ResultSet rs = null;
		List<BaseVO> resultado = null;

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.setInt(i++, idServicio);
			cs.execute();

			rs = (ResultSet) cs.getObject(1);
			resultado = new ArrayList<BaseVO>();
			BaseVO bean = null;
			while (rs.next()) {
				bean = new BaseVO();
				bean.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idserviciodepende"));
				bean.setNombre(UtilJdbc.obtenerCadena(rs, "nombre"));
				
				resultado.add(bean);
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

		return resultado;
	}
	
	@Override
	public List<MaestroServicio> consultarServiciosInvisibles(Integer idServicio) throws SQLException, Exception{
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_consultarserviciosinvisibles(?) }";
		ResultSet rs = null;
		List<MaestroServicio> resultado = null;

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.setInt(i++, idServicio);
			cs.execute();

			rs = (ResultSet) cs.getObject(1);
			resultado = new ArrayList<MaestroServicio>();
			MaestroServicio bean = null;
			while (rs.next()) {
				bean = new MaestroServicio();
				bean.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idserviciodepende"));
				bean.setNombre(UtilJdbc.obtenerCadena(rs, "nombre"));
				bean.setValorParametro(UtilJdbc.obtenerCadena(rs, "valor"));
				
				resultado.add(bean);
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

		return resultado;
	}
}

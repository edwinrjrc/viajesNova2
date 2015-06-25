/**
 * 
 */
package pe.com.logistica.negocio.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.negocio.dao.AuditoriaDao;
import pe.com.logistica.negocio.util.UtilConexion;

/**
 * @author Edwin
 *
 */
public class AuditoriaDaoImpl implements AuditoriaDao {

	/*
	 * (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.AuditoriaDao#registrarEventoSesion(pe.com.logistica.bean.negocio.Usuario, java.lang.Integer)
	 */
	@Override
	public boolean registrarEventoSesion(Usuario usuario, Integer tipoEvento) throws SQLException{
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "";
		
		try {
			sql = "{ ? = call auditoria.fn_registrareventosesionsistema(?,?,?) }";
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, usuario.getCodigoEntero().intValue());
			cs.setString(i++, usuario.getUsuario());
			cs.setInt(i++, tipoEvento.intValue());
			cs.execute();
			
			return cs.getBoolean(1);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally{
			if (cs != null){
				cs.close();
			}
			if (conn != null){
				conn.close();
			}
		}
	}

}

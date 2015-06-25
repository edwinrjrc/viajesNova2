/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.SQLException;

import pe.com.logistica.bean.negocio.Ubigeo;

/**
 * @author Edwin
 *
 */
public interface UbigeoDao {

	public Ubigeo consultarUbigeo(String idUbigeo) throws SQLException;
}

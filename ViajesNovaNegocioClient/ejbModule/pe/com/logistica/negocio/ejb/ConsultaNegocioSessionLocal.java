package pe.com.logistica.negocio.ejb;

import java.sql.SQLException;

import javax.ejb.Local;

import pe.com.logistica.bean.negocio.Proveedor;

@Local
public interface ConsultaNegocioSessionLocal {

	public Proveedor consultarProveedor(int codigoProveedor)
			throws SQLException, Exception;
}

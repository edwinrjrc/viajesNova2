package pe.com.logistica.negocio.ejb;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;

import pe.com.logistica.bean.negocio.Parametro;

@Remote
public interface ParametroRemote {

	List<Parametro> listarParametros() throws SQLException;

	void registrarParametro(Parametro parametro) throws SQLException;

	void actualizarParametro(Parametro parametro) throws SQLException;

	Parametro consultarParametro(int id) throws SQLException;
}

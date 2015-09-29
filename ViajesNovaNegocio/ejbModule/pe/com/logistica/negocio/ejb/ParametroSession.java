package pe.com.logistica.negocio.ejb;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Stateless;

import pe.com.logistica.bean.negocio.Parametro;
import pe.com.logistica.negocio.dao.ParametroDao;
import pe.com.logistica.negocio.dao.impl.ParametroDaoImpl;

/**
 * Session Bean implementation class Parametro
 */
@Stateless(name = "ParametroSession")
public class ParametroSession implements ParametroRemote, ParametroLocal {

	ParametroDao parametroDao = null;

	/**
	 * Default constructor.
	 */
	public ParametroSession() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<pe.com.logistica.bean.negocio.Parametro> listarParametros()
			throws SQLException {
		parametroDao = new ParametroDaoImpl();
		return parametroDao.listarParametros();
	}

	@Override
	public void registrarParametro(
			pe.com.logistica.bean.negocio.Parametro parametro)
			throws SQLException {
		parametroDao = new ParametroDaoImpl();
		parametroDao.registrarParametro(parametro);
	}

	@Override
	public void actualizarParametro(
			pe.com.logistica.bean.negocio.Parametro parametro)
			throws SQLException {
		parametroDao = new ParametroDaoImpl();
		parametroDao.actualizarParametro(parametro);
	}

	@Override
	public Parametro consultarParametro(int id) throws SQLException {
		parametroDao = new ParametroDaoImpl();
		return parametroDao.consultarParametro(id);
	}
}

package pe.com.logistica.negocio.ejb;

import java.sql.SQLException;

import javax.ejb.Stateless;

import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.negocio.dao.AuditoriaDao;
import pe.com.logistica.negocio.dao.impl.AuditoriaDaoImpl;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;

/**
 * Session Bean implementation class AuditoriaSession
 */
@Stateless(name ="AuditoriaSession", mappedName = "AuditoriaSession")
public class AuditoriaSession implements AuditoriaSessionRemote, AuditoriaSessionLocal {

	@Override
	public void registrarEventoInicioSession (Usuario usuario) throws ErrorRegistroDataException{
		try {
			AuditoriaDao auditoriaDao = new AuditoriaDaoImpl();
			auditoriaDao.registrarEventoSesion(usuario, 1);
		} catch (SQLException e) {
			throw new ErrorRegistroDataException(e);
		}
	}

}

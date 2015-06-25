package pe.com.logistica.negocio.ejb;

import javax.ejb.Local;

import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;

@Local
public interface AuditoriaSessionLocal {

	void registrarEventoInicioSession(Usuario usuario)
			throws ErrorRegistroDataException;
}

package pe.com.logistica.negocio.ejb;

import javax.ejb.Remote;

import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;

@Remote
public interface AuditoriaSessionRemote {

	void registrarEventoInicioSession(Usuario usuario)
			throws ErrorRegistroDataException;

}

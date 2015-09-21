package pe.com.logistica.negocio.ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.bean.recursoshumanos.UsuarioAsistencia;
import pe.com.logistica.negocio.exception.ErrorConsultaDataException;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;

@Local
public interface AuditoriaSessionLocal {

	void registrarEventoInicioSession(Usuario usuario)
			throws ErrorRegistroDataException;

	public List<UsuarioAsistencia> consultaHorariosEntrada(Date fecha)
			throws ErrorConsultaDataException;
}

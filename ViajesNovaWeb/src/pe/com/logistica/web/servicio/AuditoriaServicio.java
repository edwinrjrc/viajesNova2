/**
 * 
 */
package pe.com.logistica.web.servicio;

import java.util.Date;
import java.util.List;

import pe.com.logistica.bean.recursoshumanos.UsuarioAsistencia;
import pe.com.logistica.negocio.exception.ErrorConsultaDataException;

/**
 * @author EDWREB
 *
 */
public interface AuditoriaServicio {

	/**
	 * 
	 * @param fecha
	 * @return
	 * @throws ErrorConsultaDataException
	 */
	public List<UsuarioAsistencia> consultarHorarioAsistenciaXDia(Date fecha)
			throws ErrorConsultaDataException;
}

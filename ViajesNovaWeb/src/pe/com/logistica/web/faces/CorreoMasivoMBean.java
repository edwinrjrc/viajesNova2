/**
 * 
 */
package pe.com.logistica.web.faces;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import pe.com.logistica.bean.negocio.ArchivoAdjunto;
import pe.com.logistica.bean.negocio.CorreoClienteMasivo;
import pe.com.logistica.bean.negocio.CorreoMasivo;
import pe.com.logistica.negocio.exception.EnvioCorreoException;
import pe.com.logistica.web.servicio.NegocioServicio;
import pe.com.logistica.web.servicio.impl.NegocioServicioImpl;

/**
 * @author Edwin
 *
 */
@ManagedBean(name = "correoMasivoMBean" )
@SessionScoped()
public class CorreoMasivoMBean extends BaseMBean {
	
	private final static Logger logger = Logger.getLogger(CorreoMasivoMBean.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 8169173720644254644L;
	
	private CorreoMasivo correoMasivo;
	
	private List<CorreoClienteMasivo> listaClientesCorreo;
	
	private List<ArchivoAdjunto> archivos;
	
	private NegocioServicio negocioServicio;
	
	private boolean correoEnviado;
	
	private String pregunta;

	/**
	 * 
	 */
	public CorreoMasivoMBean() {
		try {
			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			negocioServicio = new NegocioServicioImpl(servletContext);
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void nuevoEnvio(){
		this.setCorreoEnviado(false);
		this.setCorreoMasivo(null);
	}
	
	public void cargarArchivo(){
		
	}
	
	public void listener(FileUploadEvent event) throws Exception {
        UploadedFile item = event.getUploadedFile();
        
        ArchivoAdjunto archivo = new ArchivoAdjunto(item.getName());
        
        archivo.setTipoContenido(item.getContentType());
        archivo.setDatos(IOUtils.toByteArray(item.getInputStream()));
        archivo.setContent(item.getContentType());
        getArchivos().add(archivo);
    }
	
	public String correoEnviadoAction(){
		this.setCorreoEnviado(true);
		
		return "";
	}
	
	public void confirmarEnvioCorreo(){
		this.setPregunta("¿ Esta seguro de enviar el correo masivo?");
	}
	
	public void enviarMasivo(){
		try {
			String mensaje = "";
			if (!getArchivos().isEmpty()){
				
				byte[] buffer = getArchivos().get(0).getDatos();
				
				if (buffer == null || buffer.length == 0){
					throw new EnvioCorreoException("No se adjunto archivo al correo");
				}
				
				getCorreoMasivo().setArchivoAdjunto(getArchivos().get(0));
				getCorreoMasivo().setArchivoCargado(buffer.length>0);
				getCorreoMasivo().setBuffer(buffer);
				
				mensaje = "<html>";
				mensaje = mensaje + "<body>";
				mensaje = mensaje + "<img src='cid:imagenCorreo'>";
				mensaje = mensaje + "</body>";
				mensaje = mensaje + "</html>";
			}
			else{
				throw new EnvioCorreoException("No se adjunto archivo al correo");
			}
				
			getCorreoMasivo().setContenidoCorreo(mensaje);
			getCorreoMasivo().setListaCorreoMasivo(listaClientesCorreo);
			int tipo = this.negocioServicio.enviarCorreoMasivo(getCorreoMasivo());
			
			String msje = "Correos Enviados Satisfactoriamente";
			if (tipo > 0){
				msje = "Correos Enviados con errores verifique el log";
			}
			
			this.setShowModal(true);
			this.setTipoModal(TIPO_MODAL_EXITO);
			this.setMensajeModal(msje);
		} catch (EnvioCorreoException e) {
			e.printStackTrace();
			this.setShowModal(true);
			this.setTipoModal(TIPO_MODAL_ERROR);
			this.setMensajeModal(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			this.setShowModal(true);
			this.setTipoModal(TIPO_MODAL_ERROR);
			this.setMensajeModal(e.getMessage());
		}
	}
	
	public void aceptarConfirmacion(){
		
	}

	/**
	 * @return the correoMasivo
	 */
	public CorreoMasivo getCorreoMasivo() {
		if (correoMasivo == null){
			correoMasivo = new CorreoMasivo();
		}
		return correoMasivo;
	}

	/**
	 * @param correoMasivo the correoMasivo to set
	 */
	public void setCorreoMasivo(CorreoMasivo correoMasivo) {
		this.correoMasivo = correoMasivo;
	}

	/**
	 * @return the listaClientesCorreo
	 */
	public List<CorreoClienteMasivo> getListaClientesCorreo() {
		try {
			listaClientesCorreo = negocioServicio.listarClientesCorreo();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listaClientesCorreo;
	}

	/**
	 * @param listaClientesCorreo the listaClientesCorreo to set
	 */
	public void setListaClientesCorreo(List<CorreoClienteMasivo> listaClientesCorreo) {
		this.listaClientesCorreo = listaClientesCorreo;
	}

	/**
	 * @return the archivos
	 */
	public List<ArchivoAdjunto> getArchivos() {
		if (archivos == null){
			archivos = new ArrayList<ArchivoAdjunto>();
		}
		return archivos;
	}

	/**
	 * @param archivos the archivos to set
	 */
	public void setArchivos(List<ArchivoAdjunto> archivos) {
		this.archivos = archivos;
	}

	/**
	 * @return the correoEnviado
	 */
	public boolean isCorreoEnviado() {
		return correoEnviado;
	}

	/**
	 * @param correoEnviado the correoEnviado to set
	 */
	public void setCorreoEnviado(boolean correoEnviado) {
		this.correoEnviado = correoEnviado;
	}

	/**
	 * @return the pregunta
	 */
	public String getPregunta() {
		return pregunta;
	}

	/**
	 * @param pregunta the pregunta to set
	 */
	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}

}

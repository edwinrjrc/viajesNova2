/**
 * 
 */
package pe.com.logistica.bean.negocio;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;

import pe.com.logistica.bean.base.BaseNegocio;
import pe.com.logistica.bean.base.BaseVO;

/**
 * @author Edwin
 *
 */
public class PagoServicio extends BaseNegocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9143805785741012964L;
	
	private BaseVO servicio;
	private Date fechaPago;
	private BigDecimal montoPago;
	private InputStream sustentoPago;
	private OutputStream sustentoPagoSalida;
	private byte[] sustentoPagoByte;
	private String nombreArchivo;
	private String extensionArchivo;
	private String tipoContenido;
	
	private boolean tieneSustento;
	private Integer idObligacion;
	private String comentario;
	
	private BaseVO tipoPago;
	/**
	 * 
	 */
	public PagoServicio() {
		// TODO Auto-generated constructor stub
	}

	public BaseVO getServicio() {
		if (servicio == null){
			servicio = new BaseVO();
		}
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(BaseVO servicio) {
		this.servicio = servicio;
	}

	/**
	 * @return the fechaPago
	 */
	public Date getFechaPago() {
		if (fechaPago == null){
			fechaPago = new Date();
		}
		return fechaPago;
	}

	/**
	 * @param fechaPago the fechaPago to set
	 */
	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	/**
	 * @return the montoPago
	 */
	public BigDecimal getMontoPago() {
		return montoPago;
	}

	/**
	 * @param montoPago the montoPago to set
	 */
	public void setMontoPago(BigDecimal montoPago) {
		this.montoPago = montoPago;
	}

	/**
	 * @return the sustentoPago
	 */
	public InputStream getSustentoPago() {
		return sustentoPago;
	}

	/**
	 * @param sustentoPago the sustentoPago to set
	 */
	public void setSustentoPago(InputStream sustentoPago) {
		this.sustentoPago = sustentoPago;
	}

	/**
	 * @return the sustentoPagoByte
	 */
	public byte[] getSustentoPagoByte() {
		return sustentoPagoByte;
	}

	/**
	 * @param sustentoPagoByte the sustentoPagoByte to set
	 */
	public void setSustentoPagoByte(byte[] sustentoPagoByte) {
		this.sustentoPagoByte = sustentoPagoByte;
	}

	/**
	 * @return the nombreArchivo
	 */
	public String getNombreArchivo() {
		return nombreArchivo;
	}

	/**
	 * @param nombreArchivo the nombreArchivo to set
	 */
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	/**
	 * @return the extensionArchivo
	 */
	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	/**
	 * @param extensionArchivo the extensionArchivo to set
	 */
	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}

	/**
	 * @return the sustentoPagoSalida
	 */
	public OutputStream getSustentoPagoSalida() {
		return sustentoPagoSalida;
	}

	/**
	 * @param sustentoPagoSalida the sustentoPagoSalida to set
	 */
	public void setSustentoPagoSalida(OutputStream sustentoPagoSalida) {
		this.sustentoPagoSalida = sustentoPagoSalida;
	}

	/**
	 * @return the tipoContenido
	 */
	public String getTipoContenido() {
		return tipoContenido;
	}

	/**
	 * @param tipoContenido the tipoContenido to set
	 */
	public void setTipoContenido(String tipoContenido) {
		this.tipoContenido = tipoContenido;
	}

	/**
	 * @return the tieneSustento
	 */
	public boolean isTieneSustento() {
		return tieneSustento;
	}

	/**
	 * @param tieneSustento the tieneSustento to set
	 */
	public void setTieneSustento(boolean tieneSustento) {
		this.tieneSustento = tieneSustento;
	}

	/**
	 * @return the idObligacion
	 */
	public Integer getIdObligacion() {
		return idObligacion;
	}

	/**
	 * @param idObligacion the idObligacion to set
	 */
	public void setIdObligacion(Integer idObligacion) {
		this.idObligacion = idObligacion;
	}

	/**
	 * @return the comentario
	 */
	public String getComentario() {
		return comentario;
	}

	/**
	 * @param comentario the comentario to set
	 */
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	/**
	 * @return the tipoPago
	 */
	public BaseVO getTipoPago() {
		if (tipoPago == null){
			tipoPago = new BaseVO();
		}
		return tipoPago;
	}

	/**
	 * @param tipoPago the tipoPago to set
	 */
	public void setTipoPago(BaseVO tipoPago) {
		this.tipoPago = tipoPago;
	}

}

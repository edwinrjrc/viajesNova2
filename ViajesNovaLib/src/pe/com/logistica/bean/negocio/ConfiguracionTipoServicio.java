package pe.com.logistica.bean.negocio;

import pe.com.logistica.bean.base.BaseNegocio;

public class ConfiguracionTipoServicio extends BaseNegocio{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7203147523525517942L;
	
	
	private boolean muestraAerolinea;
	private boolean muestraEmpresaTransporte;
	private boolean muestraHotel;
	private boolean muestraProveedor;
	private boolean muestraOperadora;
	private boolean muestraDescServicio;
	private boolean muestraFechaServicio;
	private boolean muestraFechaRegreso;
	private boolean muestraCantidad;
	private boolean muestraPrecioBase;
	private boolean muestraDestino;
	private boolean muestraComision;
	private boolean muestraTarifaNegociada;

	public ConfiguracionTipoServicio() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the muestraEmpresaTransporte
	 */
	public boolean isMuestraEmpresaTransporte() {
		return muestraEmpresaTransporte;
	}

	/**
	 * @param muestraEmpresaTransporte the muestraEmpresaTransporte to set
	 */
	public void setMuestraEmpresaTransporte(boolean muestraEmpresaTransporte) {
		this.muestraEmpresaTransporte = muestraEmpresaTransporte;
	}

	/**
	 * @return the muestraHotel
	 */
	public boolean isMuestraHotel() {
		return muestraHotel;
	}

	/**
	 * @param muestraHotel the muestraHotel to set
	 */
	public void setMuestraHotel(boolean muestraHotel) {
		this.muestraHotel = muestraHotel;
	}

	/**
	 * @return the muestraProveedor
	 */
	public boolean isMuestraProveedor() {
		return muestraProveedor;
	}

	/**
	 * @param muestraProveedor the muestraProveedor to set
	 */
	public void setMuestraProveedor(boolean muestraProveedor) {
		this.muestraProveedor = muestraProveedor;
	}

	/**
	 * @return the muestraDescServicio
	 */
	public boolean isMuestraDescServicio() {
		return muestraDescServicio;
	}

	/**
	 * @param muestraDescServicio the muestraDescServicio to set
	 */
	public void setMuestraDescServicio(boolean muestraDescServicio) {
		this.muestraDescServicio = muestraDescServicio;
	}

	/**
	 * @return the muestraFechaServicio
	 */
	public boolean isMuestraFechaServicio() {
		return muestraFechaServicio;
	}

	/**
	 * @param muestraFechaServicio the muestraFechaServicio to set
	 */
	public void setMuestraFechaServicio(boolean muestraFechaServicio) {
		this.muestraFechaServicio = muestraFechaServicio;
	}

	/**
	 * @return the muestraFechaRegreso
	 */
	public boolean isMuestraFechaRegreso() {
		return muestraFechaRegreso;
	}

	/**
	 * @param muestraFechaRegreso the muestraFechaRegreso to set
	 */
	public void setMuestraFechaRegreso(boolean muestraFechaRegreso) {
		this.muestraFechaRegreso = muestraFechaRegreso;
	}

	/**
	 * @return the muestraCantidad
	 */
	public boolean isMuestraCantidad() {
		return muestraCantidad;
	}

	/**
	 * @param muestraCantidad the muestraCantidad to set
	 */
	public void setMuestraCantidad(boolean muestraCantidad) {
		this.muestraCantidad = muestraCantidad;
	}

	/**
	 * @return the muestraPrecioBase
	 */
	public boolean isMuestraPrecioBase() {
		return muestraPrecioBase;
	}

	/**
	 * @param muestraPrecioBase the muestraPrecioBase to set
	 */
	public void setMuestraPrecioBase(boolean muestraPrecioBase) {
		this.muestraPrecioBase = muestraPrecioBase;
	}

	/**
	 * @return the muestraAerolinea
	 */
	public boolean isMuestraAerolinea() {
		return muestraAerolinea;
	}

	/**
	 * @param muestraAerolinea the muestraAerolinea to set
	 */
	public void setMuestraAerolinea(boolean muestraAerolinea) {
		this.muestraAerolinea = muestraAerolinea;
	}

	/**
	 * @return the muestraDestino
	 */
	public boolean isMuestraDestino() {
		return muestraDestino;
	}

	/**
	 * @param muestraDestino the muestraDestino to set
	 */
	public void setMuestraDestino(boolean muestraDestino) {
		this.muestraDestino = muestraDestino;
	}

	/**
	 * @return the muestraComision
	 */
	public boolean isMuestraComision() {
		return muestraComision;
	}

	/**
	 * @param muestraComision the muestraComision to set
	 */
	public void setMuestraComision(boolean muestraComision) {
		this.muestraComision = muestraComision;
	}

	/**
	 * @return the muestraOperadora
	 */
	public boolean isMuestraOperadora() {
		return muestraOperadora;
	}

	/**
	 * @param muestraOperadora the muestraOperadora to set
	 */
	public void setMuestraOperadora(boolean muestraOperadora) {
		this.muestraOperadora = muestraOperadora;
	}

	/**
	 * @return the muestraTarifaNegociada
	 */
	public boolean isMuestraTarifaNegociada() {
		return muestraTarifaNegociada;
	}

	/**
	 * @param muestraTarifaNegociada the muestraTarifaNegociada to set
	 */
	public void setMuestraTarifaNegociada(boolean muestraTarifaNegociada) {
		this.muestraTarifaNegociada = muestraTarifaNegociada;
	}

}

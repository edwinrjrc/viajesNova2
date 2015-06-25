package pe.com.logistica.bean.negocio;

import java.util.ArrayList;
import java.util.List;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.base.Persona;

/**
 * @author Edwin
 * @version 1.0
 * @created 14-dic-2013 01:14:34 p.m.
 */
public class Proveedor extends Persona {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4758923398707358761L;
	private List<Contacto> listaContactos;
	private List<CuentaBancaria> listaCuentas;
	private List<ServicioProveedor> listaServicioProveedor;
	private BaseVO tipoProveedor;
	
	private Integer codigoSeleccionado;

	public Proveedor() {

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * @return the listaContactos
	 */
	public List<Contacto> getListaContactos() {
		if (listaContactos == null){
			listaContactos = new ArrayList<Contacto>();
		}
		return listaContactos;
	}

	/**
	 * @param listaContactos
	 *            the listaContactos to set
	 */
	public void setListaContactos(List<Contacto> listaContactos) {
		this.listaContactos = listaContactos;
	}

	/**
	 * @return the listaCuentas
	 */
	public List<CuentaBancaria> getListaCuentas() {
		if (listaCuentas == null){
			listaCuentas = new ArrayList<CuentaBancaria>();
		}
		return listaCuentas;
	}

	/**
	 * @param listaCuentas
	 *            the listaCuentas to set
	 */
	public void setListaCuentas(List<CuentaBancaria> listaCuentas) {
		this.listaCuentas = listaCuentas;
	}

	/**
	 * @return the listaServicioProveedor
	 */
	public List<ServicioProveedor> getListaServicioProveedor() {
		if (listaServicioProveedor == null){
			listaServicioProveedor = new ArrayList<ServicioProveedor>();
		}
		return listaServicioProveedor;
	}

	/**
	 * @param listaServicioProveedor the listaServicioProveedor to set
	 */
	public void setListaServicioProveedor(List<ServicioProveedor> listaServicioProveedor) {
		this.listaServicioProveedor = listaServicioProveedor;
	}

	/**
	 * @return the tipoProveedor
	 */
	public BaseVO getTipoProveedor() {
		if (tipoProveedor == null){
			tipoProveedor = new BaseVO();
		}
		return tipoProveedor;
	}

	/**
	 * @param tipoProveedor the tipoProveedor to set
	 */
	public void setTipoProveedor(BaseVO tipoProveedor) {
		this.tipoProveedor = tipoProveedor;
	}

	/**
	 * @return the codigoSeleccionado
	 */
	public Integer getCodigoSeleccionado() {
		return codigoSeleccionado;
	}

	/**
	 * @param codigoSeleccionado the codigoSeleccionado to set
	 */
	public void setCodigoSeleccionado(Integer codigoSeleccionado) {
		this.codigoSeleccionado = codigoSeleccionado;
	}

}
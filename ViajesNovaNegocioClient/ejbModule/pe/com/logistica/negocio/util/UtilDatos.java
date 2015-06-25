/**
 * 
 */
package pe.com.logistica.negocio.util;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.negocio.Direccion;
import pe.com.logistica.bean.negocio.Maestro;

/**
 * @author Edwin
 *
 */
public class UtilDatos {

	public static String obtenerDireccionCompleta(Direccion direccion, Maestro hijoMaestro){
		String direccionCompleta = "";
		try {
			direccionCompleta = "" + hijoMaestro.getAbreviatura() + " "
					+ direccion.getNombreVia();
			if (StringUtils.isNotBlank(direccion.getNumero())) {
				direccionCompleta = direccionCompleta + " Nro "
						+ direccion.getNumero();
			} else {
				direccionCompleta = direccionCompleta + " Mz. "
						+ direccion.getManzana();
				direccionCompleta = direccionCompleta + " Lt. "
						+ direccion.getLote();
			}
			if (StringUtils.isNotBlank(direccion.getInterior())) {
				direccionCompleta = direccionCompleta + " Int "
						+ direccion.getInterior();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return direccionCompleta;
	}

}

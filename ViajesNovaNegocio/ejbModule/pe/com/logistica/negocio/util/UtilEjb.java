/**
 * 
 */
package pe.com.logistica.negocio.util;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Comprobante;
import pe.com.logistica.bean.negocio.DetalleComprobante;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.Parametro;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.negocio.dao.ParametroDao;
import pe.com.logistica.negocio.dao.impl.ParametroDaoImpl;

/**
 * @author Edwin
 *
 */
public class UtilEjb {

	public static String obtenerCadenaPropertieMaestro(String llave,
			String maestroPropertie) {
		try {
			ResourceBundle resourceMaestros = ResourceBundle
					.getBundle(maestroPropertie);

			return resourceMaestros.getString(llave);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static int obtenerEnteroPropertieMaestro(String llave,
			String maestroPropertie) {
		try {
			ResourceBundle resourceMaestros = ResourceBundle
					.getBundle(maestroPropertie);

			return convertirCadenaEntero(resourceMaestros.getString(llave));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String obtenerCadenaBlanco(String cadena) {
		if (StringUtils.isNotBlank(cadena)) {
			return StringUtils.trimToEmpty(cadena);
		}
		return "";
	}

	public static int convertirCadenaEntero(String cadena) {
		try {
			if (StringUtils.isNotBlank(cadena)) {
				return Integer.parseInt(cadena);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static BigDecimal convertirCadenaDecimal(String numero) {
		if (StringUtils.isNotBlank(numero)) {
			return BigDecimal.valueOf(Double.valueOf(numero));
		}
		return BigDecimal.ZERO;
	}

	public static List<Comprobante> obtenerNumeroComprobante(
			List<DetalleServicioAgencia> listaDetalle) {
		List<Comprobante> lista = new ArrayList<Comprobante>();
		Comprobante comprobante = new Comprobante();
		comprobante.setNumeroComprobante(listaDetalle.get(0)
				.getServiciosHijos().get(0).getNroComprobante());
		comprobante.setTipoComprobante(listaDetalle.get(0).getServiciosHijos()
				.get(0).getTipoComprobante());
		comprobante.setTieneDetraccion(listaDetalle.get(0).getServiciosHijos()
				.get(0).isTieneDetraccion());
		comprobante.setTieneRetencion(listaDetalle.get(0).getServiciosHijos()
				.get(0).isTieneRetencion());
		lista.add(comprobante);
		for (int s = 0; s < listaDetalle.size(); s++) {
			for (int i = 0; i < listaDetalle.get(s).getServiciosHijos().size(); i++) {
				DetalleServicioAgencia bean = listaDetalle.get(s)
						.getServiciosHijos().get(i);
				for (int r = 0; r < listaDetalle.size(); r++) {
					for (int j = 0; j < listaDetalle.get(r).getServiciosHijos()
							.size(); j++) {
						DetalleServicioAgencia bean2 = listaDetalle.get(r).getServiciosHijos().get(j);
						if (!bean.getNroComprobante().equals(
								bean2.getNroComprobante())
								&& !estaEnListado(bean2.getNroComprobante(),
										lista)) {
							comprobante = new Comprobante();
							comprobante.setNumeroComprobante(bean2
									.getNroComprobante());
							comprobante.setTipoComprobante(bean2
									.getTipoComprobante());
							comprobante.setTieneDetraccion(bean2
									.isTieneDetraccion());
							comprobante.setTieneRetencion(bean2
									.isTieneRetencion());
							lista.add(comprobante);
							comprobante = null;
						}
					}
				}
			}
		}

		return lista;
	}

	private static boolean estaEnListado(String numero, List<Comprobante> lista) {
		for (Comprobante comprobante : lista) {
			if (comprobante.getNumeroComprobante().equals(numero)) {
				return true;
			}
		}
		return false;
	}

	public static Comprobante obtenerNumeroComprobante(Comprobante comp,
			ServicioAgencia servicioAgencia) {
		try {
			BigDecimal total = BigDecimal.ZERO;
			BigDecimal totalIGV = BigDecimal.ZERO;
			ParametroDao parametroDao = new ParametroDaoImpl();
			Parametro param = parametroDao.consultarParametro(UtilEjb
					.obtenerEnteroPropertieMaestro("codigoParametroIGV",
							"aplicacionDatosEjb"));
			BaseVO tipoComprobante = null;
			DetalleComprobante detalle = null;
			DetalleServicioAgencia bean = null;
			for (int s=0;s<servicioAgencia.getListaDetalleServicio().size(); s++){
				for (int i = 0; i < servicioAgencia.getListaDetalleServicioAgrupado().get(s).getServiciosHijos().size(); i++) {
					bean = servicioAgencia
							.getListaDetalleServicioAgrupado().get(s).getServiciosHijos().get(i);
					if (bean.getNroComprobante()
							.equals(comp.getNumeroComprobante())) {
						detalle = new DetalleComprobante();
						if (bean.isAgrupado()){
							for (Integer id : bean.getCodigoEnteroAgrupados()){
								total = total.add(bean.getTotalServicio());
								tipoComprobante = bean.getTipoComprobante();
								detalle = null;
								detalle = new DetalleComprobante();
								detalle.setIdServicioDetalle(id);
								detalle.setCantidad(bean.getCantidad());
								detalle.setPrecioUnitario(bean.getPrecioUnitario());
								detalle.setTotalDetalle(bean.getTotalServicio());
								detalle.setConcepto(bean.getDescripcionServicio());
								detalle.setUsuarioCreacion(servicioAgencia
										.getUsuarioCreacion());
								detalle.setIpCreacion(servicioAgencia.getIpCreacion());
								comp.getDetalleComprobante().add(detalle);
							}
							if (bean.getTipoServicio().getCodigoEntero()
									.intValue() == UtilEjb
									.convertirCadenaEntero(param.getValor())) {
								totalIGV = totalIGV.add(bean
										.getPrecioUnitario());
							}
						}
						else{
							total = total.add(bean.getTotalServicio());
							tipoComprobante = bean.getTipoComprobante();
							detalle.setIdServicioDetalle(bean.getCodigoEntero());
							detalle.setCantidad(bean.getCantidad());
							detalle.setPrecioUnitario(bean.getPrecioUnitario());
							detalle.setTotalDetalle(bean.getTotalServicio());
							detalle.setConcepto(bean.getDescripcionServicio());
							detalle.setUsuarioCreacion(servicioAgencia
									.getUsuarioCreacion());
							detalle.setIpCreacion(servicioAgencia.getIpCreacion());
							comp.getDetalleComprobante().add(detalle);
							if (bean.getTipoServicio().getCodigoEntero()
									.intValue() == UtilEjb
									.convertirCadenaEntero(param.getValor())) {
								totalIGV = totalIGV.add(bean
										.getPrecioUnitario());
							}
						}
					}
				}
			}
			
			comp.setTitular(servicioAgencia.getCliente());
			comp.setFechaComprobante(servicioAgencia.getFechaServicio());
			comp.setTipoComprobante(tipoComprobante);
			comp.setTotalComprobante(total);
			comp.setTotalIGV(totalIGV);
			comp.setUsuarioCreacion(servicioAgencia.getUsuarioCreacion());
			comp.setIpCreacion(servicioAgencia.getIpCreacion());
			comp.setIdServicio(servicioAgencia.getCodigoEntero());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return comp;
	}

	public static boolean correoValido(String correo) {
		try {
			String patternEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			Pattern pattern = Pattern.compile(patternEmail);
			Matcher matcher = pattern.matcher(correo);

			boolean resultado = matcher.matches();

			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static List<DetalleServicioAgencia> ordenarServiciosVenta(
			List<DetalleServicioAgencia> listaServicio) throws SQLException,
			Exception {

		Collections.sort(listaServicio,
				new Comparator<DetalleServicioAgencia>() {
					@Override
					public int compare(DetalleServicioAgencia s1,
							DetalleServicioAgencia s2) {

						if (s1.getFechaIda().before(s2.getFechaIda())) {
							return -1;
						}
						if (s1.getFechaIda().after(s2.getFechaIda())) {
							return 1;
						}
						return 0;
					}
				});

		return listaServicio;
	}
}

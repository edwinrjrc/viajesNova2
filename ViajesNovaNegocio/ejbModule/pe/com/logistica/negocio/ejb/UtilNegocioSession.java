package pe.com.logistica.negocio.ejb;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.MaestroServicio;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.negocio.dao.MaestroServicioDao;
import pe.com.logistica.negocio.dao.impl.MaestroServicioDaoImpl;

/**
 * Session Bean implementation class UtilNegocioSession
 */
@Stateless(name = "UtilNegocioSession")
public class UtilNegocioSession implements UtilNegocioSessionRemote, UtilNegocioSessionLocal {

	@Override
	public List<DetalleServicioAgencia> agruparServiciosHijos (List<DetalleServicioAgencia> listaServicios){
		List<DetalleServicioAgencia> listaServiciosAgrupados = new ArrayList<DetalleServicioAgencia>();
		
		try {
			for (DetalleServicioAgencia detalleServicioAgencia : listaServicios) {
				detalleServicioAgencia.setServiciosHijos(agruparHijos(detalleServicioAgencia.getServiciosHijos()));
				listaServiciosAgrupados.add(detalleServicioAgencia);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return listaServiciosAgrupados;
	}
	
	private List<DetalleServicioAgencia> agruparHijos (List<DetalleServicioAgencia> listaServicios){
		List<DetalleServicioAgencia> listaServiciosAgrupados = new ArrayList<DetalleServicioAgencia>();
		
		try {
			MaestroServicioDao maestroServicioDao = new MaestroServicioDaoImpl();
			List<MaestroServicio> listaTiposServicio = null;
			listaTiposServicio = maestroServicioDao.listarMaestroServiciosAdm();
			int agrupados = 0;
			BigDecimal montoAgrupado = null;
			DetalleServicioAgencia detalle = null;
			for (MaestroServicio maestroServicio : listaTiposServicio) {
				detalle = null;
				detalle = new DetalleServicioAgencia();
				agrupados = 0;
				montoAgrupado = BigDecimal.ZERO;
				for (DetalleServicioAgencia detalleHijo : listaServicios){
					if (detalleHijo.getTipoServicio().getCodigoEntero().intValue() == maestroServicio.getCodigoEntero().intValue()){
						montoAgrupado = montoAgrupado.add(detalleHijo.getTotalServicio());
						agrupados++;
						detalle.setCodigoEntero(detalleHijo.getCodigoEntero());
						detalle.setTipoServicio(maestroServicio);
						detalle.setFechaIda(detalleHijo.getFechaIda());
						detalle.setFechaServicio(detalleHijo.getFechaServicio());
						detalle.setFechaRegreso(detalleHijo.getFechaRegreso());
						detalle.setCantidad(detalleHijo.getCantidad());
						detalle.setPrecioUnitario(detalleHijo.getPrecioUnitario());
						detalle.setOrigen(detalleHijo.getOrigen());
						detalle.setDestino(detalleHijo.getDestino());
						detalle.setDescripcionServicio(detalleHijo.getDescripcionServicio());
						detalle.setServicioProveedor(detalleHijo.getServicioProveedor());
						detalle.setAerolinea(detalleHijo.getAerolinea());
						detalle.setHotel(detalleHijo.getHotel());
						detalle.setTieneDetraccion(detalleHijo.isTieneDetraccion());
						detalle.setTieneRetencion(detalleHijo.isTieneRetencion());
						detalle.setTarifaNegociada(detalleHijo.isTarifaNegociada());
						detalle.setConfiguracionTipoServicio(detalleHijo.getConfiguracionTipoServicio());
						detalle.setMontoComision(detalleHijo.getMontoComision());
						detalle.getCodigoEnteroAgrupados().add(detalleHijo.getCodigoEntero());
						detalle.setComprobanteAsociado(detalleHijo.getComprobanteAsociado());
						detalle.setTipoComprobante(detalleHijo.getTipoComprobante());
						detalle.setNroComprobante(detalleHijo.getNroComprobante());
						detalle.setIdComprobanteGenerado(detalleHijo.getIdComprobanteGenerado());
						detalle.setServicioPadre(detalleHijo.getServicioPadre());
					}
				}
				if (agrupados > 1){
					detalle.setDescripcionServicio("Servicios agrupados :: "+agrupados);
					detalle.setAgrupado(true);
					detalle.setCantidadAgrupados(agrupados);
					detalle.setPrecioUnitario(montoAgrupado);
					listaServiciosAgrupados.add(detalle);
				}
				else if (agrupados > 0) {
					listaServiciosAgrupados.add(detalle);
				}
			}
		} catch (SQLException e) {	
			e.printStackTrace();
		}
		return listaServiciosAgrupados;
	}
	
	@Override
	public ServicioAgencia colocarTipoNumeroComprobante(ServicioAgencia servicioAgencia){
		List<DetalleServicioAgencia> listaFinal = new ArrayList<DetalleServicioAgencia>();
		for (int a=0; a<servicioAgencia.getListaDetalleServicio().size(); a++){
			DetalleServicioAgencia detallePadre = servicioAgencia.getListaDetalleServicio().get(a);
			System.out.println("detallePadre::"+detallePadre);
			for (int b=0; b<detallePadre.getServiciosHijos().size(); b++){
				DetalleServicioAgencia detalle = detallePadre.getServiciosHijos().get(b);
				System.out.println("detalle::"+detalle);
				for (int x=0; x<servicioAgencia.getListaDetalleServicioAgrupado().size(); x++){
					DetalleServicioAgencia detallePadreAgrupado = servicioAgencia.getListaDetalleServicioAgrupado().get(x);
					System.out.println("detallePadreAgrupado::"+detallePadreAgrupado);
					for (int y=0; y<detallePadreAgrupado.getServiciosHijos().size(); y++){
						DetalleServicioAgencia detalleAgrupado = detallePadreAgrupado.getServiciosHijos().get(y);
						System.out.println("detalleAgrupado::"+detalleAgrupado);
						System.out.println("detalleAgrupado.isAgrupado()::"+detalleAgrupado.isAgrupado());
						if (detalleAgrupado.isAgrupado()){
							for (Integer id : detalleAgrupado.getCodigoEnteroAgrupados()){
								if (detalle.getCodigoEntero().intValue() == id.intValue() ){
									detalle.setTipoComprobante(detalleAgrupado.getTipoComprobante());
									detalle.setNroComprobante(detalleAgrupado.getNroComprobante());
								}
							}
						}
						else {
							if (detalle.getCodigoEntero().intValue() == detalleAgrupado.getCodigoEntero().intValue()){
								detalle.setTipoComprobante(detalleAgrupado.getTipoComprobante());
								detalle.setNroComprobante(detalleAgrupado.getNroComprobante());
							}
						}
					}
					detallePadre.getServiciosHijos().add(detalle);
				}
			}
			listaFinal.add(detallePadre);
		}
		
		servicioAgencia.setListaDetalleServicio(listaFinal);
		
		return servicioAgencia;
	}
}

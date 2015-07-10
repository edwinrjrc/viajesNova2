/**
 * 
 */
package pe.com.logistica.web.servicio.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.negocio.ejb.UtilNegocioSessionRemote;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;
import pe.com.logistica.web.servicio.UtilNegocioServicio;

/**
 * @author Edwin
 *
 */
public class UtilNegocioServicioImpl implements UtilNegocioServicio {

	UtilNegocioSessionRemote ejbSession;
	
	final String ejbBeanName = "UtilNegocioSession";
	
	public UtilNegocioServicioImpl(ServletContext context) throws NamingException {
		Properties props = new Properties();
		/*props.setProperty("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
		props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
		props.setProperty("java.naming.provider.url", "localhost:1099"); */
		props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		
		Context ctx = new InitialContext(props);
		//String lookup = "ejb:Logistica1EAR/Logistica1Negocio/SeguridadSession!pe.com.logistica.negocio.ejb.SeguridadRemote";
		String lookup = "java:jboss/exported/Logistica1EAR/Logistica1Negocio/NegocioSession!pe.com.logistica.negocio.ejb.NegocioSessionRemote";
		
		final String ejbRemoto = UtilNegocioSessionRemote.class.getName();
		lookup = "java:jboss/exported/"+context.getInitParameter("appNegocioNameEar")+"/"+context.getInitParameter("appNegocioName")+"/"+ejbBeanName+"!"+ejbRemoto;
		
		ejbSession = (UtilNegocioSessionRemote) ctx.lookup(lookup);
	}
	

	/* (non-Javadoc)
	 * @see pe.com.logistica.web.servicio.UtilNegocioServicio#agruparServicios(java.util.List)
	 */
	@Override
	public List<DetalleServicioAgencia> agruparServicios(
			List<DetalleServicioAgencia> listaServicios) {
		
		return ejbSession.agruparServiciosHijos(listaServicios);
	}
	
	@Override
	public ServicioAgencia colocarTipoNumeroComprobante(ServicioAgencia servicioAgencia){
		return ejbSession.colocarTipoNumeroComprobante(servicioAgencia);
	}
	
	@Override
	public List<DetalleServicioAgencia> agregarServicioVenta(
			List<DetalleServicioAgencia> listaServiciosVenta,
			DetalleServicioAgencia detalleServicio)
			throws ErrorRegistroDataException, SQLException, Exception{
		return ejbSession.agregarServicioVenta(listaServiciosVenta, detalleServicio);
	}
	
	@Override
	public List<DetalleServicioAgencia> actualizarServicioVenta(
			List<DetalleServicioAgencia> listaServiciosVenta,
			DetalleServicioAgencia detalleServicio)
			throws ErrorRegistroDataException, SQLException, Exception{
		return ejbSession.actualizarServicioVenta(listaServiciosVenta, detalleServicio);
	}

}

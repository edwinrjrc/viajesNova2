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

import pe.com.logistica.bean.cargaexcel.ReporteArchivo;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.reportes.ReporteVentas;
import pe.com.logistica.negocio.ejb.NegocioSessionRemote;
import pe.com.logistica.negocio.ejb.ReportesSessionRemote;
import pe.com.logistica.negocio.exception.ConnectionException;
import pe.com.logistica.web.servicio.ReportesServicio;

/**
 * @author EDWREB
 *
 */
public class ReportesServicioImpl implements ReportesServicio {

	
	ReportesSessionRemote ejbSession;
	
	final String ejbBeanName = "ReportesSession";
	/**
	 * 
	 */
	public ReportesServicioImpl(ServletContext context) throws NamingException{
		
		Properties props = new Properties();
		/*props.setProperty("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
		props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
		props.setProperty("java.naming.provider.url", "localhost:1099"); */
		props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		
		Context ctx = new InitialContext(props);
		//String lookup = "ejb:Logistica1EAR/Logistica1Negocio/SeguridadSession!pe.com.logistica.negocio.ejb.SeguridadRemote";
		String lookup = "java:jboss/exported/Logistica1EAR/Logistica1Negocio/NegocioSession!pe.com.logistica.negocio.ejb.ReportesSessionRemote";
		
		final String ejbRemoto = ReportesSessionRemote.class.getName();
		lookup = "java:jboss/exported/"+context.getInitParameter("appNegocioNameEar")+"/"+context.getInitParameter("appNegocioName")+"/"+ejbBeanName+"!"+ejbRemoto;
		
		ejbSession = (ReportesSessionRemote) ctx.lookup(lookup);
		
	}
	/* (non-Javadoc)
	 * @see pe.com.logistica.web.servicio.ReportesServicio#reporteGeneralVentas(pe.com.logistica.bean.reportes.ReporteVentas)
	 */
	@Override
	public List<DetalleServicioAgencia> reporteGeneralVentas(
			ReporteVentas reporteVentas) throws SQLException {
		
		return ejbSession.reporteGeneralVentas(reporteVentas);
	}


}

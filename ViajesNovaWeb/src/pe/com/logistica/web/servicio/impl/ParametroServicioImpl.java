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

import pe.com.logistica.bean.negocio.Parametro;
import pe.com.logistica.negocio.ejb.ParametroRemote;
import pe.com.logistica.web.servicio.ParametroServicio;

/**
 * @author Edwin
 *
 */
public class ParametroServicioImpl implements ParametroServicio {

	ParametroRemote ejbSession;
	
	final String ejbBeanName = "ParametroSession";
	/**
	 * @throws NamingException 
	 * 
	 */
	public ParametroServicioImpl(ServletContext context) throws NamingException {
		Properties props = new Properties();
        /*props.setProperty("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
        props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
        props.setProperty("java.naming.provider.url", "eddesarrollos16:1099");*/
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        
		Context ctx = new InitialContext(props);
		//String lookup = "ejb:Logistica1EAR/Logistica1Negocio/SeguridadSession!pe.com.logistica.negocio.ejb.SeguridadRemote";
		String lookup = "java:jboss/exported/Logistica1EAR/Logistica1Negocio/ParametroSession!pe.com.logistica.negocio.ejb.ParametroRemote";
		
		final String ejbRemoto = ParametroRemote.class.getName();
		lookup = "java:jboss/exported/"+context.getInitParameter("appNegocioNameEar")+"/"+context.getInitParameter("appNegocioName")+"/"+ejbBeanName+"!"+ejbRemoto;
		
		ejbSession = (ParametroRemote) ctx.lookup(lookup);
		
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.web.servicio.ParametroServicio#listarParametros()
	 */
	@Override
	public List<Parametro> listarParametros() throws SQLException {
		return ejbSession.listarParametros();
	}

	@Override
	public void registrarParametro(Parametro parametro) throws SQLException {
		ejbSession.registrarParametro(parametro);
		
	}

	@Override
	public void actualizarParametro(Parametro parametro) throws SQLException {
		ejbSession.actualizarParametro(parametro);
		
	}

	@Override
	public Parametro consultarParametro(int id) throws SQLException {
		return ejbSession.consultarParametro(id);
	}

}

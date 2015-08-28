/**
 * 
 */
package pe.com.logistica.web.servicio.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.cargaexcel.ColumnasExcel;
import pe.com.logistica.bean.cargaexcel.ReporteArchivo;
import pe.com.logistica.bean.cargaexcel.ReporteArchivoBusqueda;
import pe.com.logistica.bean.negocio.Cliente;
import pe.com.logistica.bean.negocio.Comprobante;
import pe.com.logistica.bean.negocio.ComprobanteBusqueda;
import pe.com.logistica.bean.negocio.Consolidador;
import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.bean.negocio.CorreoClienteMasivo;
import pe.com.logistica.bean.negocio.CorreoMasivo;
import pe.com.logistica.bean.negocio.CuentaBancaria;
import pe.com.logistica.bean.negocio.CuotaPago;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.Direccion;
import pe.com.logistica.bean.negocio.DocumentoAdicional;
import pe.com.logistica.bean.negocio.EventoObsAnu;
import pe.com.logistica.bean.negocio.MaestroServicio;
import pe.com.logistica.bean.negocio.PagoServicio;
import pe.com.logistica.bean.negocio.ProgramaNovios;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.bean.negocio.ServicioAgenciaBusqueda;
import pe.com.logistica.bean.negocio.ServicioNovios;
import pe.com.logistica.bean.negocio.ServicioProveedor;
import pe.com.logistica.negocio.ejb.NegocioSessionRemote;
import pe.com.logistica.negocio.exception.EnvioCorreoException;
import pe.com.logistica.negocio.exception.ErrorConsultaDataException;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;
import pe.com.logistica.negocio.exception.ResultadoCeroDaoException;
import pe.com.logistica.web.servicio.NegocioServicio;

/**
 * @author Edwin
 *
 */
public class NegocioServicioImpl implements NegocioServicio {

	NegocioSessionRemote ejbSession;
	
	final String ejbBeanName = "NegocioSession";
	/**
	 * 
	 */
	public NegocioServicioImpl(ServletContext context) throws NamingException{
		
		Properties props = new Properties();
		/*props.setProperty("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
		props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
		props.setProperty("java.naming.provider.url", "localhost:1099"); */
		props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		
		Context ctx = new InitialContext(props);
		//String lookup = "ejb:Logistica1EAR/Logistica1Negocio/SeguridadSession!pe.com.logistica.negocio.ejb.SeguridadRemote";
		String lookup = "java:jboss/exported/Logistica1EAR/Logistica1Negocio/NegocioSession!pe.com.logistica.negocio.ejb.NegocioSessionRemote";
		
		final String ejbRemoto = NegocioSessionRemote.class.getName();
		lookup = "java:jboss/exported/"+context.getInitParameter("appNegocioNameEar")+"/"+context.getInitParameter("appNegocioName")+"/"+ejbBeanName+"!"+ejbRemoto;
		
		ejbSession = (NegocioSessionRemote) ctx.lookup(lookup);
		
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.web.servicio.NegocioServicio#agregarDireccion(pe.com.logistica.bean.base.Direccion)
	 */
	@Override
	public Direccion agregarDireccion(Direccion direccion) throws SQLException, Exception {
		return ejbSession.agregarDireccion(direccion);
	}
	
	
	@Override
	public Contacto agregarContacto (Contacto contacto) throws SQLException, Exception {
		return ejbSession.agregarContacto(contacto);
	}

	@Override
	public boolean registrarProveedor(Proveedor proveedor) throws SQLException, Exception {
		return ejbSession.registrarProveedor(proveedor);
	}
	
	@Override
	public List<Proveedor> listarProveedor(Proveedor proveedor) throws SQLException, Exception {
		return ejbSession.listarProveedor(proveedor);
	}

	@Override
	public Proveedor consultarProveedorCompleto(int codigoProveedor) throws SQLException, Exception {
		return ejbSession.consultarProveedor(codigoProveedor);
	}

	@Override
	public boolean actualizarProveedor(Proveedor proveedor)
			throws SQLException, Exception {
		return ejbSession.actualizarProveedor(proveedor);
	}

	@Override
	public List<pe.com.logistica.bean.negocio.Proveedor> buscarProveedor(
			Proveedor proveedor)
			throws SQLException, Exception {
		return ejbSession.buscarProveedor(proveedor);
	}

	@Override
	public boolean registrarCliente(Cliente cliente) throws ResultadoCeroDaoException, SQLException, Exception {
		return ejbSession.registrarCliente(cliente);
	}

	@Override
	public boolean actualizarCliente(Cliente cliente) throws ResultadoCeroDaoException, SQLException, Exception {
		return ejbSession.actualizarCliente(cliente);
	}

	@Override
	public List<Cliente> buscarCliente(Cliente cliente) throws SQLException{
		return ejbSession.buscarCliente(cliente);
	}

	@Override
	public Cliente consultarClienteCompleto(int idcliente) throws SQLException, Exception {
		return ejbSession.consultarCliente(idcliente);
	}

	@Override
	public List<Cliente> listarCliente() throws SQLException {
		return ejbSession.listarCliente();
	}

	@Override
	public Integer registrarNovios(ProgramaNovios programaNovios)
			throws SQLException, Exception {
		return ejbSession.registrarNovios(programaNovios);
	}
	
	@Override
	public List<Cliente> listarClientesNovios(String genero)
			throws SQLException, Exception {
		return ejbSession.listarClientesNovios(genero);
	}
	
	@Override
	public List<Cliente> buscarClientesNovios(Cliente cliente)
			throws SQLException, Exception {
		return ejbSession.consultarClientesNovios(cliente);
	}
	
	@Override
	public List<ProgramaNovios> consultarNovios(ProgramaNovios programaNovios)
			throws SQLException, Exception {
		return ejbSession.consultarNovios(programaNovios);
	}
	
	@Override
	public ServicioNovios agregarServicioNovios(ServicioNovios servicioNovios)
			throws SQLException, Exception {
		return ejbSession.agregarServicio(servicioNovios);
	}
	
	@Override
	public BigDecimal calcularValorCuota(ServicioAgencia servicioAgencia) throws SQLException, Exception {
		return ejbSession.calcularValorCuota(servicioAgencia);
	}

	@Override
	public List<CuotaPago> consultarCronogramaPago(ServicioAgencia servicioAgencia) throws SQLException, Exception {
		return ejbSession.consultarCronograma(servicioAgencia);
	}
	
	@Override
	public Integer registrarVentaServicio(ServicioAgencia servicioAgencia) throws ErrorRegistroDataException, SQLException, Exception {
		return ejbSession.registrarVentaServicio(servicioAgencia);
	}
	
	@Override
	public Integer actualizarVentaServicio(ServicioAgencia servicioAgencia) throws ErrorRegistroDataException, SQLException, Exception {
		return ejbSession.actualizarVentaServicio(servicioAgencia);
	}
	
	@Override
	public ServicioAgencia consultarVentaServicio(int idServicio) throws SQLException, Exception{
		return ejbSession.consultarServicioVenta(idServicio);
	}
	
	@Override
	public List<ServicioAgencia> listarVentaServicio(ServicioAgenciaBusqueda servicioAgencia) throws SQLException, Exception{
		return ejbSession.listarServicioVenta(servicioAgencia);
	}

	@Override
	public List<Cliente> consultarCliente2(Cliente cliente)
			throws SQLException, Exception {
		return ejbSession.consultarCliente2(cliente);
	}
	
	@Override
	public List<ServicioProveedor> proveedoresXServicio(int idServicio) throws SQLException, Exception{
		BaseVO servicio = new BaseVO(idServicio);
		
		return ejbSession.proveedoresXServicio(servicio);
	}

	@Override
	public ProgramaNovios consultarProgramaNovios(int idProgramaNovios)
			throws SQLException, Exception {
		return ejbSession.consultarProgramaNovios(idProgramaNovios);
	}

	@Override
	public boolean ingresarMaestroServicio(MaestroServicio servicio)
			throws ErrorRegistroDataException, SQLException, Exception {
		
		return ejbSession.ingresarMaestroServicio(servicio);
	}

	@Override
	public boolean actualizarMaestroServicio(MaestroServicio servicio)
			throws SQLException, Exception {

		return ejbSession.actualizarMaestroServicio(servicio);
	}

	@Override
	public List<MaestroServicio> listarMaestroServicio() throws SQLException,
			Exception {

		return ejbSession.listarMaestroServicio();
	}
	
	@Override
	public List<MaestroServicio> listarMaestroServicioAdm() throws SQLException,
			Exception {

		return ejbSession.listarMaestroServicioAdm();
	}
	
	@Override
	public List<MaestroServicio> listarMaestroServicioFee() throws SQLException,
			Exception {

		return ejbSession.listarMaestroServicioFee();
	}
	
	@Override
	public List<MaestroServicio> listarMaestroServicioImpto() throws SQLException,
			Exception {

		return ejbSession.listarMaestroServicioImpto();
	}

	@Override
	public MaestroServicio consultarMaestroServicio(int idMaestroServicio)
			throws SQLException, Exception {

		return ejbSession.consultarMaestroServicio(idMaestroServicio);
	}

	@Override
	public Integer actualizarNovios(ProgramaNovios programaNovios) throws SQLException, Exception {
		return ejbSession.actualizarNovios(programaNovios);
	}
	
	@Override
	public List<CorreoClienteMasivo> listarClientesCorreo() throws SQLException,
			Exception {

		return ejbSession.listarClientesCorreo();
	}

	@Override
	public int enviarCorreoMasivo(CorreoMasivo correoMasivo) throws EnvioCorreoException, Exception{
		
		return ejbSession.enviarCorreoMasivo(correoMasivo);
	}

	@Override
	public List<Cliente> listarClientesCumples() throws SQLException, Exception {
		return ejbSession.listarClientesCumples();
	}
	
	@Override
	public List<MaestroServicio> listarMaestroServicioIgv() throws SQLException,
			Exception {

		return ejbSession.listarMaestroServicioIgv();
	}
	
	@Override
	public List<BaseVO> consultaServiciosDependientes(Integer idServicio) throws SQLException, Exception{
		return ejbSession.consultaServiciosDependientes(idServicio);
	}

	@Override
	public boolean ingresarConsolidador(Consolidador consolidador)
			throws SQLException, Exception {
		return ejbSession.ingresarConsolidador(consolidador);
	}

	@Override
	public boolean actualizarConsolidador(Consolidador consolidador)
			throws SQLException, Exception {
		return ejbSession.actualizarConsolidador(consolidador);
	}

	@Override
	public List<Consolidador> listarConsolidador() throws SQLException,
			Exception {
		return ejbSession.listarConsolidador();
	}

	@Override
	public Consolidador consultarConsolidador(Consolidador consolidador)
			throws SQLException, Exception {
		return ejbSession.consultarConsolidador(consolidador);
	}

	@Override
	public void registrarPago(PagoServicio pago) throws SQLException, Exception {
		ejbSession.registrarPago(pago);
	}

	@Override
	public List<PagoServicio> listarPagosServicio(Integer idServicio)
			throws SQLException, Exception {
		
		return ejbSession.listarPagosServicio(idServicio);
	}
	
	@Override
	public List<PagoServicio> listarPagosObligacion(Integer idObligacion)
			throws SQLException, Exception {
		
		return ejbSession.listarPagosObligacion(idObligacion);
	}

	@Override
	public BigDecimal consultarSaldoServicio(Integer idServicio)
			throws SQLException, Exception {

		return ejbSession.consultarSaldoServicio(idServicio);
	}
	
	@Override
	public void cerrarVenta(ServicioAgencia servicioAgencia) throws SQLException, Exception{
		ejbSession.cerrarVenta(servicioAgencia);
	}

	@Override
	public void anularVenta(ServicioAgencia servicioAgencia) throws SQLException, Exception{
		ejbSession.anularVenta(servicioAgencia);
	}
	
	@Override
	public void registrarEventoObservacion(EventoObsAnu evento) throws SQLException, Exception{
		ejbSession.registrarEventoObservacion(evento);
	}
	
	@Override
	public void registrarEventoAnulacion(EventoObsAnu evento) throws SQLException, Exception{
		ejbSession.registrarEventoAnulacion(evento);
	}

	@Override
	public boolean registrarComprobantes(ServicioAgencia servicioAgencia)
			throws SQLException, Exception {
		return ejbSession.registrarComprobantes(servicioAgencia);
	}
	
	@Override
	public List<DetalleServicioAgencia> consultarDetalleComprobantes(Integer idServicio) throws SQLException, Exception{
		return ejbSession.consultarDetalleServicioComprobante(idServicio);
	}
	
	@Override
	public List<DetalleServicioAgencia> consultarDetServComprobanteObligacion(Integer idServicio) throws SQLException, Exception{
		return ejbSession.consultarDetServComprobanteObligacion(idServicio);
	}
	
	@Override
	public boolean registrarObligacionXPagar(Comprobante comprobante) throws SQLException, Exception{
		return ejbSession.registrarObligacionXPagar(comprobante);
	}
	
	@Override
	public List<Comprobante> listarObligacionXPagar(Comprobante comprobante) throws SQLException, Exception{
		return ejbSession.listarObligacionXPagar(comprobante);
	}
	
	@Override
	public void registrarPagoObligacion(PagoServicio pago) throws SQLException, Exception{
		ejbSession.registrarPagoObligacion(pago);
	}
	
	@Override
	public void registrarComprobanteObligacion(ServicioAgencia servicioAgencia) throws SQLException, Exception{
		ejbSession.registrarRelacionComproObligacion(servicioAgencia);
	}
	
	@Override
	public boolean grabarDocumentosAdicionales(List<DocumentoAdicional> lista) throws ErrorRegistroDataException, SQLException, Exception{
		return ejbSession.grabarDocumentosAdicionales(lista);
	}
	
	@Override
	public List<DocumentoAdicional> listarDocumentosAdicionales(Integer idServicio) throws SQLException{
		return ejbSession.listarDocumentosAdicionales(idServicio);
	}
	
	@Override
	public void registrarComprobantesAdicionales(List<Comprobante> lista) throws ErrorRegistroDataException, SQLException, Exception{
		ejbSession.registrarComprobantesAdicionales(lista);
	}

	@Override
	public List<Comprobante> consultarComprobantesGenerados(
			ComprobanteBusqueda comprobanteBusqueda)
			throws ErrorConsultaDataException {
		return ejbSession.consultarComprobantesGenerados(comprobanteBusqueda);
	}
	
	@Override
	public Comprobante consultarComprobanteGenerado(Integer idComprobante)
			throws ErrorConsultaDataException {
		return ejbSession.consultarComprobante(idComprobante);
	}

	@Override
	public boolean grabarComprobantesReporte(ReporteArchivo reporteArchivo,
			ColumnasExcel columnasExcel, List<ColumnasExcel> dataExcel)
			throws ErrorRegistroDataException, SQLException {
		return ejbSession.grabarComprobantesReporte(reporteArchivo, columnasExcel, dataExcel);
	}

	@Override
	public List<ReporteArchivoBusqueda> consultarArchivosCargados(
			ReporteArchivoBusqueda reporteArchivoBusqueda)
			throws ErrorConsultaDataException {
		return ejbSession.consultarArchivosCargados(reporteArchivoBusqueda);
	}
	
	@Override
	public DetalleServicioAgencia consultarDetalleServicioDetalle(int idServicio, int idDetServicio) throws SQLException{
		return ejbSession.consultaDetalleServicioDetalle(idServicio, idDetServicio);
	}
	
	@Override
	public List<CuentaBancaria> listarCuentasBancarias() throws SQLException{
		return ejbSession.listarCuentasBancarias();
	}
	
	@Override
	public boolean registrarCuentaBancaria(CuentaBancaria cuentaBancaria) throws ErrorRegistroDataException{
		return ejbSession.registrarCuentaBancaria(cuentaBancaria);
	}
	
	@Override
	public boolean actualizarCuentaBancaria(CuentaBancaria cuentaBancaria) throws ErrorRegistroDataException{
		return ejbSession.actualizarCuentaBancaria(cuentaBancaria);
	}
	
	@Override
	public CuentaBancaria consultarCuentaBancaria(Integer idCuenta) throws SQLException{
		return ejbSession.consultaCuentaBancaria(idCuenta);
	}
	
	@Override
	public List<CuentaBancaria> listarCuentasBancariasCombo() throws SQLException{
		return ejbSession.listarCuentasBancariasCombo();
	}
}
	
/**
 * 
 */
package pe.com.logistica.web.servicio;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

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
import pe.com.logistica.negocio.exception.EnvioCorreoException;
import pe.com.logistica.negocio.exception.ErrorConsultaDataException;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;
import pe.com.logistica.negocio.exception.ResultadoCeroDaoException;

/**
 * @author Edwin
 * 
 */
public interface NegocioServicio {

	public Direccion agregarDireccion(Direccion direccion) throws SQLException,
			Exception;

	public Contacto agregarContacto(Contacto contacto) throws SQLException,
			Exception;

	public boolean registrarProveedor(Proveedor proveedor) throws SQLException,
			Exception;

	public boolean actualizarProveedor(Proveedor proveedor)
			throws SQLException, Exception;

	List<Proveedor> listarProveedor(Proveedor proveedor) throws SQLException,
			Exception;

	public Proveedor consultarProveedorCompleto(int codigoProveedor)
			throws SQLException, Exception;

	public List<Proveedor> buscarProveedor(Proveedor proveedor)
			throws SQLException, Exception;

	public boolean registrarCliente(Cliente cliente)
			throws ResultadoCeroDaoException, SQLException, Exception;

	public boolean actualizarCliente(Cliente cliente)
			throws ResultadoCeroDaoException, SQLException, Exception;

	List<Cliente> buscarCliente(Cliente cliente) throws SQLException;

	List<Cliente> listarCliente() throws SQLException;

	public Cliente consultarClienteCompleto(int idcliente) throws SQLException,
			Exception;

	public Integer registrarNovios(ProgramaNovios programaNovios)
			throws SQLException, Exception;

	List<Cliente> listarClientesNovios(String genero) throws SQLException,
			Exception;

	List<ProgramaNovios> consultarNovios(ProgramaNovios programaNovios)
			throws SQLException, Exception;

	ServicioNovios agregarServicioNovios(ServicioNovios servicioNovios)
			throws SQLException, Exception;

	List<DetalleServicioAgencia> agregarServicioVenta(
			List<DetalleServicioAgencia> listaServiciosVenta,
			DetalleServicioAgencia detalleServicio)
			throws ErrorRegistroDataException, SQLException, Exception;

	public BigDecimal calcularValorCuota(ServicioAgencia servicioAgencia)
			throws SQLException, Exception;

	List<CuotaPago> consultarCronogramaPago(ServicioAgencia servicioAgencia)
			throws SQLException, Exception;

	Integer registrarVentaServicio(ServicioAgencia servicioAgencia)
			throws ErrorRegistroDataException, SQLException, Exception;

	List<Cliente> consultarCliente2(Cliente cliente) throws SQLException,
			Exception;

	List<ServicioProveedor> proveedoresXServicio(int idServicio)
			throws SQLException, Exception;

	List<ServicioAgencia> listarVentaServicio(
			ServicioAgenciaBusqueda servicioAgencia) throws SQLException,
			Exception;

	ServicioAgencia consultarVentaServicio(int idServicio) throws SQLException,
			Exception;

	List<Cliente> buscarClientesNovios(Cliente cliente) throws SQLException,
			Exception;

	public ProgramaNovios consultarProgramaNovios(int idProgramaNovios)
			throws SQLException, Exception;

	public boolean ingresarMaestroServicio(MaestroServicio servicio)
			throws ErrorRegistroDataException, SQLException, Exception;

	public boolean actualizarMaestroServicio(MaestroServicio servicio)
			throws SQLException, Exception;

	public List<MaestroServicio> listarMaestroServicio() throws SQLException,
			Exception;

	public MaestroServicio consultarMaestroServicio(int idMaestroServicio)
			throws SQLException, Exception;

	public Integer actualizarNovios(ProgramaNovios programaNovios)
			throws SQLException, Exception;

	Integer actualizarVentaServicio(ServicioAgencia servicioAgencia)
			throws ErrorRegistroDataException, SQLException, Exception;

	List<MaestroServicio> listarMaestroServicioFee() throws SQLException,
			Exception;

	List<MaestroServicio> listarMaestroServicioImpto() throws SQLException,
			Exception;

	List<CorreoClienteMasivo> listarClientesCorreo() throws SQLException,
			Exception;

	public int enviarCorreoMasivo(CorreoMasivo correoMasivo)
			throws EnvioCorreoException, Exception;

	public List<Cliente> listarClientesCumples() throws SQLException, Exception;

	List<MaestroServicio> listarMaestroServicioIgv() throws SQLException,
			Exception;

	List<MaestroServicio> listarMaestroServicioAdm() throws SQLException,
			Exception;

	List<BaseVO> consultaServiciosDependientes(Integer idServicio)
			throws SQLException, Exception;

	boolean ingresarConsolidador(Consolidador consolidador)
			throws SQLException, Exception;

	public boolean actualizarConsolidador(Consolidador consolidador)
			throws SQLException, Exception;

	public List<Consolidador> listarConsolidador() throws SQLException,
			Exception;

	public Consolidador consultarConsolidador(Consolidador consolidador)
			throws SQLException, Exception;

	public BigDecimal calculaPorcentajeComision(
			DetalleServicioAgencia detalleServicio) throws SQLException,
			Exception;

	public void registrarPago(PagoServicio pago) throws SQLException, Exception;

	public List<PagoServicio> listarPagosServicio(Integer idServicio)
			throws SQLException, Exception;

	public BigDecimal consultarSaldoServicio(Integer idServicio)
			throws SQLException, Exception;

	void cerrarVenta(ServicioAgencia servicioAgencia) throws SQLException,
			Exception;

	public void anularVenta(ServicioAgencia servicioAgencia)
			throws SQLException, Exception;

	void registrarEventoObservacion(EventoObsAnu evento) throws SQLException,
			Exception;

	void registrarEventoAnulacion(EventoObsAnu evento) throws SQLException,
			Exception;

	public boolean registrarComprobantes(ServicioAgencia servicioAgencia)
			throws SQLException, Exception;

	List<DetalleServicioAgencia> consultarDetalleComprobantes(Integer idServicio)
			throws SQLException, Exception;

	boolean registrarObligacionXPagar(Comprobante comprobante)
			throws SQLException, Exception;

	List<Comprobante> listarObligacionXPagar(Comprobante comprobante)
			throws SQLException, Exception;

	void registrarPagoObligacion(PagoServicio pago) throws SQLException,
			Exception;

	List<PagoServicio> listarPagosObligacion(Integer idObligacion)
			throws SQLException, Exception;

	void registrarComprobanteObligacion(ServicioAgencia servicioAgencia)
			throws SQLException, Exception;

	List<DetalleServicioAgencia> consultarDetServComprobanteObligacion(
			Integer idServicio) throws SQLException, Exception;

	boolean grabarDocumentosAdicionales(List<DocumentoAdicional> lista)
			throws ErrorRegistroDataException, SQLException, Exception;

	List<DocumentoAdicional> listarDocumentosAdicionales(Integer idServicio)
			throws SQLException;

	void registrarComprobantesAdicionales(List<Comprobante> lista)
			throws ErrorRegistroDataException, SQLException, Exception;

	public List<Comprobante> consultarComprobantesGenerados(
			ComprobanteBusqueda comprobanteBusqueda)
			throws ErrorConsultaDataException;

	Comprobante consultarComprobanteGenerado(Integer idComprobante)
			throws ErrorConsultaDataException;

	public boolean grabarComprobantesReporte(ReporteArchivo reporteArchivo,
			ColumnasExcel columnasExcel, List<ColumnasExcel> dataExcel)
			throws ErrorRegistroDataException, SQLException;

	public List<ReporteArchivoBusqueda> consultarArchivosCargados(
			ReporteArchivoBusqueda reporteArchivoBusqueda)
			throws ErrorConsultaDataException;
}

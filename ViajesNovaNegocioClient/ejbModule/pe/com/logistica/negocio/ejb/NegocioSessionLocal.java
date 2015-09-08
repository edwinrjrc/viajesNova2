package pe.com.logistica.negocio.ejb;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.Local;

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
import pe.com.logistica.negocio.exception.EnvioCorreoException;
import pe.com.logistica.negocio.exception.ErrorConsultaDataException;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;
import pe.com.logistica.negocio.exception.ResultadoCeroDaoException;
import pe.com.logistica.negocio.exception.ValidacionException;

@Local
public interface NegocioSessionLocal {

	public Direccion agregarDireccion(Direccion direccion) throws SQLException,
			Exception;

	Contacto agregarContacto(Contacto contacto) throws SQLException, Exception;

	boolean registrarProveedor(Proveedor proveedor)
			throws ResultadoCeroDaoException, SQLException, Exception;

	List<Proveedor> listarProveedor(Proveedor proveedor) throws SQLException;

	public Proveedor consultarProveedor(int codigoProveedor)
			throws SQLException, Exception;

	boolean actualizarProveedor(Proveedor proveedor) throws SQLException,
			ResultadoCeroDaoException, Exception;

	List<Proveedor> buscarProveedor(Proveedor proveedor) throws SQLException;

	public boolean registrarCliente(Cliente cliente)
			throws ResultadoCeroDaoException, SQLException, Exception;

	public boolean actualizarCliente(Cliente cliente) throws SQLException,
			ResultadoCeroDaoException, Exception;

	List<Cliente> buscarCliente(Cliente cliente) throws SQLException;

	public Cliente consultarCliente(int idcliente) throws SQLException,
			Exception;

	List<Cliente> listarCliente() throws SQLException;

	public Integer registrarNovios(ProgramaNovios programaNovios)
			throws SQLException, Exception;

	public List<Cliente> listarClientesNovios(String genero)
			throws SQLException, Exception;

	public List<Cliente> consultarClientesNovios(Cliente cliente)
			throws SQLException, Exception;

	List<ProgramaNovios> consultarNovios(ProgramaNovios programaNovios)
			throws SQLException, Exception;

	ServicioNovios agregarServicio(ServicioNovios servicioNovios)
			throws SQLException, Exception;

	public BigDecimal calcularValorCuota(ServicioAgencia servicioAgencia)
			throws SQLException, Exception;

	public Integer registrarVentaServicio(ServicioAgencia servicioAgencia)
			throws ErrorRegistroDataException, SQLException, Exception;

	public List<CuotaPago> consultarCronograma(ServicioAgencia servicioAgencia)
			throws SQLException, Exception;

	public List<ServicioAgencia> listarServicioVenta(
			ServicioAgenciaBusqueda servicioAgencia) throws SQLException,
			Exception;

	public ServicioAgencia consultarServicioVenta(int idServicio)
			throws SQLException, Exception;

	public List<Cliente> consultarCliente2(Cliente cliente)
			throws SQLException, Exception;

	List<ServicioProveedor> proveedoresXServicio(BaseVO servicio)
			throws SQLException, Exception;

	public ProgramaNovios consultarProgramaNovios(int idProgramaNovios)
			throws SQLException, Exception;

	public boolean ingresarMaestroServicio(MaestroServicio servicio)
			throws ErrorRegistroDataException, SQLException, Exception;

	public boolean actualizarMaestroServicio(MaestroServicio servicio)
			throws SQLException, Exception;

	public List<MaestroServicio> listarMaestroServicio() throws SQLException,
			Exception;

	public List<MaestroServicio> listarMaestroServicioFee()
			throws SQLException, Exception;

	public List<MaestroServicio> listarMaestroServicioImpto()
			throws SQLException, Exception;

	public MaestroServicio consultarMaestroServicio(int idMaestroServicio)
			throws SQLException, Exception;

	public Integer actualizarNovios(ProgramaNovios programaNovios)
			throws SQLException, Exception;

	public Integer actualizarVentaServicio(ServicioAgencia servicioAgencia)
			throws ErrorRegistroDataException, SQLException, Exception;

	public List<CorreoClienteMasivo> listarClientesCorreo()
			throws SQLException, Exception;

	public int enviarCorreoMasivo(CorreoMasivo correoMasivo)
			throws EnvioCorreoException, Exception;

	public List<Cliente> listarClientesCumples() throws SQLException, Exception;

	public List<MaestroServicio> listarMaestroServicioIgv()
			throws SQLException, Exception;

	List<MaestroServicio> listarMaestroServicioAdm() throws SQLException,
			Exception;

	public List<BaseVO> consultaServiciosDependientes(Integer idServicio)
			throws SQLException, Exception;

	boolean ingresarConsolidador(Consolidador consolidador)
			throws SQLException, Exception;

	public boolean actualizarConsolidador(Consolidador consolidador)
			throws SQLException, Exception;

	public List<Consolidador> listarConsolidador() throws SQLException,
			Exception;

	public Consolidador consultarConsolidador(Consolidador consolidador)
			throws SQLException, Exception;

	public void registrarPago(PagoServicio pago) throws SQLException, Exception;

	public List<PagoServicio> listarPagosServicio(Integer idServicio)
			throws SQLException, Exception;

	public BigDecimal consultarSaldoServicio(Integer idServicio)
			throws SQLException, Exception;

	void cerrarVenta(ServicioAgencia servicioAgencia) throws SQLException,
			Exception;

	public void anularVenta(ServicioAgencia servicioAgencia)
			throws SQLException, Exception;

	public void registrarEventoObservacion(EventoObsAnu evento)
			throws SQLException, Exception;

	public void registrarEventoAnulacion(EventoObsAnu evento)
			throws SQLException, Exception;

	public boolean registrarComprobantes(ServicioAgencia servicioAgencia)
			throws ValidacionException, SQLException, Exception;

	List<DetalleServicioAgencia> consultarDetalleServicioComprobante(
			Integer idServicio) throws SQLException, Exception;

	boolean registrarObligacionXPagar(Comprobante comprobante)
			throws SQLException, Exception;

	List<Comprobante> listarObligacionXPagar(Comprobante comprobante)
			throws SQLException, Exception;

	void registrarPagoObligacion(PagoServicio pago) throws SQLException,
			Exception;

	List<PagoServicio> listarPagosObligacion(Integer idObligacion)
			throws SQLException, Exception;

	void registrarRelacionComproObligacion(ServicioAgencia servicioAgencia)
			throws SQLException, Exception;

	public List<DetalleServicioAgencia> consultarDetServComprobanteObligacion(
			Integer idServicio) throws ErrorConsultaDataException, SQLException, Exception;

	boolean grabarDocumentosAdicionales(List<DocumentoAdicional> listaDocumentos)
			throws ErrorRegistroDataException, SQLException, Exception;

	List<DocumentoAdicional> listarDocumentosAdicionales(Integer idServicio)
			throws SQLException;

	public void registrarComprobantesAdicionales(
			List<Comprobante> listaComprobantesAdicionales)
			throws ErrorRegistroDataException, SQLException, Exception;
	
	public List<Comprobante> consultarComprobantesGenerados(ComprobanteBusqueda comprobanteBusqueda) throws ErrorConsultaDataException;
	
	public Comprobante consultarComprobante (Integer idComprobante) throws ErrorConsultaDataException;
	
	public boolean grabarComprobantesReporte(ReporteArchivo reporteArchivo, ColumnasExcel columnasExcel, List<ColumnasExcel> dataExcel) throws ErrorRegistroDataException, SQLException;
	
	public List<ReporteArchivoBusqueda> consultarArchivosCargados(ReporteArchivoBusqueda reporteArchivoBusqueda) throws ErrorConsultaDataException;
	
	public DetalleServicioAgencia consultaDetalleServicioDetalle(int idServicio, int idDetServicio) throws SQLException;
	
	public List<CuentaBancaria> listarCuentasBancarias() throws SQLException;
	
	public boolean registrarCuentaBancaria(CuentaBancaria cuentaBancaria) throws ErrorRegistroDataException;
	
	public boolean actualizarCuentaBancaria(CuentaBancaria cuentaBancaria) throws ErrorRegistroDataException;
	
	public CuentaBancaria consultaCuentaBancaria(Integer idCuenta) throws SQLException;
	
	public List<CuentaBancaria> listarCuentasBancariasCombo() throws SQLException;
	
	public Comprobante consultarComprobanteObligacion(Integer idObligacion) throws SQLException;
}
/**
 * 
 */
package pe.com.logistica.web.servicio;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.cargaexcel.ReporteArchivoBusqueda;
import pe.com.logistica.bean.negocio.Cliente;
import pe.com.logistica.bean.negocio.Comprobante;
import pe.com.logistica.bean.negocio.ComprobanteBusqueda;
import pe.com.logistica.bean.negocio.Consolidador;
import pe.com.logistica.bean.negocio.CorreoClienteMasivo;
import pe.com.logistica.bean.negocio.CuentaBancaria;
import pe.com.logistica.bean.negocio.CuotaPago;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.DocumentoAdicional;
import pe.com.logistica.bean.negocio.MaestroServicio;
import pe.com.logistica.bean.negocio.MovimientoCuenta;
import pe.com.logistica.bean.negocio.PagoServicio;
import pe.com.logistica.bean.negocio.ProgramaNovios;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.bean.negocio.ServicioAgenciaBusqueda;
import pe.com.logistica.bean.negocio.ServicioProveedor;
import pe.com.logistica.bean.negocio.TipoCambio;
import pe.com.logistica.negocio.exception.ErrorConsultaDataException;

/**
 * @author EDWREB
 *
 */
public interface ConsultaNegocioServicio {

	List<Proveedor> listarProveedor(Proveedor proveedor) throws SQLException;

	Proveedor consultarProveedor(int codigoProveedor) throws SQLException,
			Exception;

	List<Proveedor> buscarProveedor(Proveedor proveedor) throws SQLException;

	List<Cliente> listarCliente() throws SQLException;

	List<Cliente> buscarCliente(Cliente cliente) throws SQLException;

	Cliente consultarCliente(int idcliente) throws SQLException, Exception;

	List<Cliente> listarClientesNovios(String genero) throws SQLException,
			Exception;

	List<Cliente> buscarClientesNovios(Cliente cliente) throws SQLException,
			Exception;

	List<ProgramaNovios> consultarNovios(ProgramaNovios programaNovios)
			throws SQLException, Exception;

	List<CuotaPago> consultarCronogramaPago(ServicioAgencia servicioAgencia)
			throws SQLException, Exception;

	ServicioAgencia consultarVentaServicio(int idServicio) throws SQLException,
			Exception;

	List<ServicioAgencia> listarVentaServicio(
			ServicioAgenciaBusqueda servicioAgencia) throws SQLException,
			Exception;

	List<Cliente> consultarCliente2(Cliente cliente) throws SQLException,
			Exception;

	List<ServicioProveedor> proveedoresXServicio(int idServicio)
			throws SQLException, Exception;

	ProgramaNovios consultarProgramaNovios(int idProgramaNovios)
			throws SQLException, Exception;

	List<MaestroServicio> listarMaestroServicio() throws SQLException,
			Exception;

	List<MaestroServicio> listarMaestroServicioAdm() throws SQLException,
			Exception;

	List<MaestroServicio> listarMaestroServicioFee() throws SQLException,
			Exception;

	List<MaestroServicio> listarMaestroServicioImpto() throws SQLException,
			Exception;

	MaestroServicio consultarMaestroServicio(int idMaestroServicio)
			throws SQLException, Exception;

	List<CorreoClienteMasivo> listarClientesCorreo() throws SQLException,
			Exception;

	List<Cliente> listarClientesCumples() throws SQLException, Exception;

	List<MaestroServicio> listarMaestroServicioIgv() throws SQLException,
			Exception;

	List<BaseVO> consultaServiciosDependientes(Integer idServicio)
			throws SQLException, Exception;

	List<Consolidador> listarConsolidador() throws SQLException, Exception;

	Consolidador consultarConsolidador(Consolidador consolidador)
			throws SQLException, Exception;

	List<PagoServicio> listarPagosServicio(Integer idServicio)
			throws SQLException, Exception;

	List<PagoServicio> listarPagosObligacion(Integer idObligacion)
			throws SQLException, Exception;

	BigDecimal consultarSaldoServicio(Integer idServicio) throws SQLException,
			Exception;

	List<DetalleServicioAgencia> consultarDetalleComprobantes(Integer idServicio)
			throws SQLException, Exception;

	List<DetalleServicioAgencia> consultarDetServComprobanteObligacion(
			Integer idServicio) throws SQLException, Exception;

	List<Comprobante> listarObligacionXPagar(Comprobante comprobante)
			throws SQLException, Exception;

	List<DocumentoAdicional> listarDocumentosAdicionales(Integer idServicio)
			throws SQLException;

	List<Comprobante> consultarComprobantesGenerados(
			ComprobanteBusqueda comprobanteBusqueda)
			throws ErrorConsultaDataException;

	Comprobante consultarComprobanteGenerado(Integer idComprobante)
			throws ErrorConsultaDataException;

	List<ReporteArchivoBusqueda> consultarArchivosCargados(
			ReporteArchivoBusqueda reporteArchivoBusqueda)
			throws ErrorConsultaDataException;

	DetalleServicioAgencia consultarDetalleServicioDetalle(int idServicio,
			int idDetServicio) throws SQLException;

	List<CuentaBancaria> listarCuentasBancarias() throws SQLException;

	CuentaBancaria consultarCuentaBancaria(Integer idCuenta)
			throws SQLException;

	List<CuentaBancaria> listarCuentasBancariasCombo() throws SQLException;

	Comprobante consultarComprobanteObligacion(Integer idObligacion)
			throws SQLException;

	List<CuentaBancaria> listarCuentasBancariasProveedor(Integer idProveedor)
			throws SQLException;

	List<MovimientoCuenta> listarMovimientosXCuenta(Integer idCuenta)
			throws SQLException;

	List<TipoCambio> listarTipoCambio(Date fecha) throws SQLException;
}

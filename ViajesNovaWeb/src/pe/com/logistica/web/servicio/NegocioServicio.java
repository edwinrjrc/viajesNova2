/**
 * 
 */
package pe.com.logistica.web.servicio;

import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.cargaexcel.ColumnasExcel;
import pe.com.logistica.bean.cargaexcel.ReporteArchivo;
import pe.com.logistica.bean.negocio.Cliente;
import pe.com.logistica.bean.negocio.Comprobante;
import pe.com.logistica.bean.negocio.Consolidador;
import pe.com.logistica.bean.negocio.CorreoMasivo;
import pe.com.logistica.bean.negocio.CuentaBancaria;
import pe.com.logistica.bean.negocio.DocumentoAdicional;
import pe.com.logistica.bean.negocio.EventoObsAnu;
import pe.com.logistica.bean.negocio.MaestroServicio;
import pe.com.logistica.bean.negocio.PagoServicio;
import pe.com.logistica.bean.negocio.ProgramaNovios;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.bean.negocio.TipoCambio;
import pe.com.logistica.negocio.exception.EnvioCorreoException;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;
import pe.com.logistica.negocio.exception.ResultadoCeroDaoException;

/**
 * @author Edwin
 * 
 */
public interface NegocioServicio {

	public boolean registrarProveedor(Proveedor proveedor) throws SQLException,
			Exception;

	public boolean actualizarProveedor(Proveedor proveedor)
			throws SQLException, Exception;

	public boolean registrarCliente(Cliente cliente)
			throws ResultadoCeroDaoException, SQLException, Exception;

	public boolean actualizarCliente(Cliente cliente)
			throws ResultadoCeroDaoException, SQLException, Exception;

	public Integer registrarNovios(ProgramaNovios programaNovios)
			throws SQLException, Exception;

	Integer registrarVentaServicio(ServicioAgencia servicioAgencia)
			throws ErrorRegistroDataException, SQLException, Exception;

	public boolean ingresarMaestroServicio(MaestroServicio servicio)
			throws ErrorRegistroDataException, SQLException, Exception;

	public boolean actualizarMaestroServicio(MaestroServicio servicio)
			throws SQLException, Exception;

	public Integer actualizarNovios(ProgramaNovios programaNovios)
			throws SQLException, Exception;

	Integer actualizarVentaServicio(ServicioAgencia servicioAgencia)
			throws ErrorRegistroDataException, SQLException, Exception;

	public int enviarCorreoMasivo(CorreoMasivo correoMasivo)
			throws EnvioCorreoException, Exception;

	boolean ingresarConsolidador(Consolidador consolidador)
			throws SQLException, Exception;

	public boolean actualizarConsolidador(Consolidador consolidador)
			throws SQLException, Exception;

	public void registrarPago(PagoServicio pago) throws SQLException, Exception;

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

	boolean registrarObligacionXPagar(Comprobante comprobante)
			throws SQLException, Exception;

	void registrarPagoObligacion(PagoServicio pago) throws SQLException,
			Exception;

	void registrarComprobanteObligacion(ServicioAgencia servicioAgencia)
			throws SQLException, Exception;

	boolean grabarDocumentosAdicionales(List<DocumentoAdicional> lista)
			throws ErrorRegistroDataException, SQLException, Exception;

	void registrarComprobantesAdicionales(List<Comprobante> lista)
			throws ErrorRegistroDataException, SQLException, Exception;

	public boolean grabarComprobantesReporte(ReporteArchivo reporteArchivo,
			ColumnasExcel columnasExcel, List<ColumnasExcel> dataExcel)
			throws ErrorRegistroDataException, SQLException;

	boolean registrarCuentaBancaria(CuentaBancaria cuentaBancaria)
			throws ErrorRegistroDataException;

	boolean actualizarCuentaBancaria(CuentaBancaria cuentaBancaria)
			throws ErrorRegistroDataException;

	boolean registrarTipoCambio(TipoCambio tipoCambio) throws SQLException;
}

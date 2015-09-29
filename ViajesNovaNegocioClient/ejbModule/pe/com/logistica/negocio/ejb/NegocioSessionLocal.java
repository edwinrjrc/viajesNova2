package pe.com.logistica.negocio.ejb;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Local;

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
import pe.com.logistica.negocio.exception.ValidacionException;

@Local
public interface NegocioSessionLocal {

	boolean registrarProveedor(Proveedor proveedor)
			throws ResultadoCeroDaoException, SQLException, Exception;

	boolean actualizarProveedor(Proveedor proveedor) throws SQLException,
			ResultadoCeroDaoException, Exception;

	public boolean registrarCliente(Cliente cliente)
			throws ResultadoCeroDaoException, SQLException, Exception;

	public boolean actualizarCliente(Cliente cliente) throws SQLException,
			ResultadoCeroDaoException, Exception;

	public Integer registrarNovios(ProgramaNovios programaNovios)
			throws SQLException, Exception;

	public Integer registrarVentaServicio(ServicioAgencia servicioAgencia)
			throws ErrorRegistroDataException, SQLException, Exception;

	public boolean ingresarMaestroServicio(MaestroServicio servicio)
			throws ErrorRegistroDataException, SQLException, Exception;

	public boolean actualizarMaestroServicio(MaestroServicio servicio)
			throws SQLException, Exception;

	public Integer actualizarNovios(ProgramaNovios programaNovios)
			throws SQLException, Exception;

	public Integer actualizarVentaServicio(ServicioAgencia servicioAgencia)
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

	public void registrarEventoObservacion(EventoObsAnu evento)
			throws SQLException, Exception;

	public void registrarEventoAnulacion(EventoObsAnu evento)
			throws SQLException, Exception;

	public boolean registrarComprobantes(ServicioAgencia servicioAgencia)
			throws ValidacionException, SQLException, Exception;

	boolean registrarObligacionXPagar(Comprobante comprobante)
			throws SQLException, Exception;

	void registrarPagoObligacion(PagoServicio pago) throws SQLException,
			Exception;

	void registrarRelacionComproObligacion(ServicioAgencia servicioAgencia)
			throws SQLException, Exception;

	boolean grabarDocumentosAdicionales(List<DocumentoAdicional> listaDocumentos)
			throws ErrorRegistroDataException, SQLException, Exception;

	public void registrarComprobantesAdicionales(
			List<Comprobante> listaComprobantesAdicionales)
			throws ErrorRegistroDataException, SQLException, Exception;

	public boolean grabarComprobantesReporte(ReporteArchivo reporteArchivo,
			ColumnasExcel columnasExcel, List<ColumnasExcel> dataExcel)
			throws ErrorRegistroDataException, SQLException, Exception;

	public boolean registrarCuentaBancaria(CuentaBancaria cuentaBancaria)
			throws ErrorRegistroDataException;

	public boolean actualizarCuentaBancaria(CuentaBancaria cuentaBancaria)
			throws ErrorRegistroDataException;

	public boolean registrarTipoCambio(TipoCambio tipoCambio)
			throws SQLException;
}
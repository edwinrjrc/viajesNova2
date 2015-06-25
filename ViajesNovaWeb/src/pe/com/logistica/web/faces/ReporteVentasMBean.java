/**
 * 
 */
package pe.com.logistica.web.faces;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.reportes.ReporteVentas;
import pe.com.logistica.web.servicio.ReportesServicio;
import pe.com.logistica.web.servicio.SeguridadServicio;
import pe.com.logistica.web.servicio.impl.ReportesServicioImpl;
import pe.com.logistica.web.servicio.impl.SeguridadServicioImpl;

/**
 * @author Edwin
 *
 */
@ManagedBean(name = "reporteVentasMBean")
@SessionScoped()
public class ReporteVentasMBean extends BaseMBean {

	private final static Logger logger = Logger
			.getLogger(ReporteVentasMBean.class);

	private static final long serialVersionUID = -4496282062127472546L;

	private ReportesServicio reportesServicio;
	private SeguridadServicio seguridadServicio;

	private List<DetalleServicioAgencia> reporteGeneralVentas;

	private ReporteVentas reporteVentas;

	private String nombreVendedor;

	public ReporteVentasMBean() {
		try {
			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			reportesServicio = new ReportesServicioImpl(servletContext);
			seguridadServicio = new SeguridadServicioImpl(servletContext);
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void consultaReporteGeneralVentas() {
		try {
			this.setNombreVendedor(this.seguridadServicio.consultarUsuario(
					this.getReporteVentas().getVendedor().getCodigoEntero())
					.getNombreCompleto());
			this.setReporteGeneralVentas(this.reportesServicio
					.reporteGeneralVentas(this.getReporteVentas()));

		} catch (SQLException e) {
			this.mostrarMensajeError(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void exportarReporte(){
		
		HSSFWorkbook xls = new HSSFWorkbook();
		HSSFSheet hoja = xls.createSheet("Reporte");
		HSSFRow fila = hoja.createRow(1);
		HSSFCell celda = fila.createCell(1);
		celda.setCellValue("Hola mi nuevo excel");
		
		try {
			HttpServletResponse response = obtenerResponse();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename="
					+ "reporte.xls");
			response.setHeader("Content-Transfer-Encoding", "binary");

			FacesContext facesContext = obtenerContexto();

			ServletOutputStream respuesta = response.getOutputStream();
			//respuesta.write(xls.getBytes());
			xls.write(respuesta);

			respuesta.close();
			respuesta.flush();

			facesContext.responseComplete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ========================================================================
	 * ======================
	 */
	/**
	 * @return the reporteVentas
	 */
	public ReporteVentas getReporteVentas() {
		if (reporteVentas == null) {
			reporteVentas = new ReporteVentas();
			Calendar cal = Calendar.getInstance();
			reporteVentas.setFechaHasta(cal.getTime());
			cal.add(Calendar.MONTH, -1);
			reporteVentas.setFechaDesde(cal.getTime());
		}
		return reporteVentas;
	}

	/**
	 * @param reporteVentas
	 *            the reporteVentas to set
	 */
	public void setReporteVentas(ReporteVentas reporteVentas) {
		this.reporteVentas = reporteVentas;
	}

	/**
	 * @return the reporteGeneralVentas
	 */
	public List<DetalleServicioAgencia> getReporteGeneralVentas() {
		if (reporteGeneralVentas == null) {
			reporteGeneralVentas = new ArrayList<DetalleServicioAgencia>();
		}
		return reporteGeneralVentas;
	}

	/**
	 * @param reporteGeneralVentas
	 *            the reporteGeneralVentas to set
	 */
	public void setReporteGeneralVentas(
			List<DetalleServicioAgencia> reporteGeneralVentas) {
		this.reporteGeneralVentas = reporteGeneralVentas;
	}

	/**
	 * @return the nombreVendedor
	 */
	public String getNombreVendedor() {
		return nombreVendedor;
	}

	/**
	 * @param nombreVendedor
	 *            the nombreVendedor to set
	 */
	public void setNombreVendedor(String nombreVendedor) {
		this.nombreVendedor = nombreVendedor;
	}

}

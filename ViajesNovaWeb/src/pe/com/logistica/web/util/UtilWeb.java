/**
 * 
 */
package pe.com.logistica.web.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;

import pe.com.logistica.bean.base.BaseVO;

/**
 * @author Edwin
 *
 */
public class UtilWeb {
	
	private final static Logger logger = Logger.getLogger(UtilWeb.class);
	
	public static List<SelectItem> convertirSelectItem(List<BaseVO> lista){
		List<SelectItem> listaCombo = new ArrayList<SelectItem>();
		SelectItem si = null;
		if (lista != null){
			for (BaseVO baseVO : lista) {
				si = new SelectItem(obtenerObjetoCadena(baseVO), baseVO.getNombre());
				listaCombo.add(si);
			}
		}
		
		return listaCombo;
	}
	
	public static String obtenerObjetoCadena(BaseVO baseVO){
		if (baseVO.getCodigoEntero() != null){
			return baseVO.getCodigoEntero().toString();
		}
		else{
			return baseVO.getCodigoCadena();
		}
	}
	
	public static int convertirCadenaEntero(String cadena){
		try {
			if (StringUtils.isNotBlank(cadena)){
				return Integer.parseInt(cadena);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public static String obtenerCadenaPropertieMaestro(String llave, String maestroPropertie){
		try {
			ResourceBundle resourceMaestros = ResourceBundle.getBundle(maestroPropertie);
			
			return resourceMaestros.getString(llave);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static int obtenerEnteroPropertieMaestro(String llave, String maestroPropertie){
		try {
			ResourceBundle resourceMaestros = ResourceBundle.getBundle(maestroPropertie);
			
			return convertirCadenaEntero(resourceMaestros.getString(llave));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static String obtenerCadenaBlanco(String cadena){
		if (StringUtils.isNotBlank(cadena)){
			return StringUtils.trimToEmpty(cadena);
		}
		return "";
	}
	
	public static int obtenerLongitud(String cadena){
		if (StringUtils.isNotBlank(cadena)){
			return cadena.length();
		}
		return 0;
	}
	
	public static String diaHoy(){
		Calendar cal = Calendar.getInstance();
		
		switch (cal.get(Calendar.DAY_OF_WEEK)){
		case 2:
			return "Lunes";
		case 3:
			return "Martes";
		case 4:
			return "Miercoles";
		case 5:
			return "Jueves";
		case 6:
			return "Viernes";
		case 7:
			return "Sabado";
		case 1:
			return "Domingo";
		}
		
		return "";
	}
	
	public static String mesHoy(){
		Calendar cal = Calendar.getInstance();
		
		switch (cal.get(Calendar.MONTH)+1){
		case 1:
			return "Enero";
		case 2:
			return "Febrero";
		case 3:
			return "Marzo";
		case 4:
			return "Abril";
		case 5:
			return "Mayo";
		case 6:
			return "Junio";
		case 7:
			return "Julio";
		case 8:
			return "Agosto";
		case 9:
			return "Septiembre";
		case 10:
			return "Octubre";
		case 11:
			return "Noviembre";
		case 12:
			return "Diciembre";
		}
		
		return "";
	}
	
	public static boolean validaEnteroEsNuloOCero(Integer numero){
		try {
			return (numero == null || numero.intValue()==0);
			
		} catch (Exception e) {
			logger.error("Error validacion numero cero o nullo ::"+e.getMessage());
		}
		
		return false;
	}
	
	public static boolean validarCorreo(String email){
		try {
			String patternEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			Pattern pattern = Pattern.compile(patternEmail);
			Matcher matcher = pattern.matcher(email);
			
			boolean resultado = matcher.matches();
			
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
		}
        return false;
	}
	
	public static boolean fecha1EsMayorIgualFecha2(Date fecha1, Date fecha2){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
		if (sdf.format(fecha1).equals(sdf.format(fecha2))){
			return true;
		}
		else{
			if (fecha1.before(fecha2)){
				return true;
			}	
		}
		
		return false;
	}
	
	public static Date fechaHoy(){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String fecha = "";
			
			Calendar cal = Calendar.getInstance();
			fecha = cal.get(Calendar.DATE)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR);
			
			return sdf.parse(fecha);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String obtenerDato(HSSFCell celda){
		if (celda != null){
			/*
			
			switch (celda.getCellType()) {
			case HSSFCell.CELL_TYPE_BLANK:
				return "";
			case HSSFCell.CELL_TYPE_NUMERIC:
				return String.valueOf(celda.getNumericCellValue());
			case HSSFCell.CELL_TYPE_STRING:
				return celda.getStringCellValue();
			default:
				return "";			
			}*/
			return celda.toString();
		}
		return "";
	}
	
	public static int calculaTamanioExcel(int pixeles){
		BigDecimal tamanio = BigDecimal.ZERO;
		
		tamanio = BigDecimal.valueOf(pixeles).divide(BigDecimal.valueOf(7.0), 0, RoundingMode.HALF_UP);
		
		tamanio = tamanio.multiply(BigDecimal.valueOf(256.0)); 
		
		return tamanio.intValue();
	}
}

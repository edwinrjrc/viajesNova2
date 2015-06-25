package pe.com.logistica.negocio.ejb;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import pe.com.logistica.bean.negocio.ClienteCorreo;

/**
 * Session Bean implementation class AgenteExcel
 */
@Stateless
public class AgenteExcel implements AgenteExcelRemote, AgenteExcelLocal {

    /**
     * Default constructor. 
     */
    public AgenteExcel() {
        // TODO Auto-generated constructor stub
    }
    
    
    public void enviarCorreoMasivo(InputStream is){
    	try {
			HSSFWorkbook libro = new HSSFWorkbook(is);
			HSSFSheet hoja = libro.getSheetAt(0);
			Iterator rowIterator = hoja.rowIterator();
			rowIterator.next();
			
			List<ClienteCorreo> correosExternos = new ArrayList<ClienteCorreo>();
			ClienteCorreo clienteCorreo = null;
			while (rowIterator.hasNext())
			{
				HSSFRow fila = (HSSFRow) rowIterator.next();
				
				HSSFCell celdaNombre = fila.getCell(0);
				HSSFCell celdaApellidos = fila.getCell(1);
				HSSFCell celdaCorreo = fila.getCell(2);
				clienteCorreo = new ClienteCorreo();
				clienteCorreo.setNombres(celdaNombre.getStringCellValue());
				clienteCorreo.setApellidos(celdaApellidos.getStringCellValue());
				clienteCorreo.setCorreo(celdaCorreo.getStringCellValue());
				correosExternos.add(clienteCorreo);
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	
    }

}

-- Function: negocio.fn_consultarpersonas(integer, integer, character varying, character varying)

-- DROP FUNCTION negocio.fn_consultarpersonas(integer, integer, character varying, character varying);

CREATE OR REPLACE FUNCTION negocio.fn_consultarpersonas(p_idtipopersona integer, p_idtipodocumento integer, p_numerodocumento character varying, p_nombres character varying)
  RETURNS refcursor AS
$BODY$
declare micursor refcursor;

begin

open micursor for
SELECT pro.id AS idproveedor, tdoc.id AS idtipodocumento, 
       tdoc.nombre AS nombretipodocumento, pro.numerodocumento, pro.nombres, 
       pro.apellidopaterno, pro.apellidomaterno, 
       dir.idvia, tvia.nombre AS nombretipovia, 
       dir.nombrevia, dir.numero, dir.interior, dir.manzana, dir.lote, 
	    ( SELECT tel.numero
		FROM negocio."TelefonoDireccion" tedir, 
		     negocio."Telefono" tel
	       WHERE tedir.idestadoregistro = 1 
		 AND tel.idestadoregistro   = 1 
		 AND tedir.iddireccion      = dir.id 
		 AND tedir.idtelefono       = tel.id LIMIT 1) AS teledireccion
   FROM negocio."Persona" pro
  INNER JOIN soporte."Tablamaestra" tdoc     ON tdoc.idmaestro        = 1 AND pro.idtipodocumento = tdoc.id
   LEFT JOIN negocio."PersonaDireccion" pdir ON pdir.idestadoregistro = 1 AND pro.id              = pdir.idpersona
   LEFT JOIN negocio."Direccion" dir         ON dir.idestadoregistro  = 1 AND pdir.iddireccion    = dir.id AND dir.principal = 'S'
   LEFT JOIN soporte."Tablamaestra" tvia     ON tvia.idmaestro        = 2 AND dir.idvia           = tvia.id
  WHERE pro.idestadoregistro  = 1 
    AND pro.idtipopersona     = COALESCE(p_idtipopersona,pro.idtipopersona)
    AND tdoc.id               = COALESCE(p_idtipodocumento,tdoc.id)
    AND pro.numerodocumento   = COALESCE(p_numerodocumento,pro.numerodocumento)
    AND CONCAT(replace(pro.nombres,' ',''),trim(pro.apellidopaterno),trim(pro.apellidomaterno)) like '%'||COALESCE(p_nombres,CONCAT(replace(pro.nombres,' ',''),trim(pro.apellidopaterno),trim(pro.apellidomaterno)))||'%';

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
-- Function: negocio.fn_eliminarpersonadirecciones(integer, character varying, character varying)

-- DROP FUNCTION negocio.fn_eliminarpersonadirecciones(integer, character varying, character varying);

CREATE OR REPLACE FUNCTION negocio.fn_eliminarpersonadirecciones(p_idpersona integer)
  RETURNS boolean AS
$BODY$
declare fechahoy timestamp with time zone;

begin

select current_timestamp AT TIME ZONE 'PET' into fechahoy;

UPDATE 
  negocio."PersonaDireccion"
SET 
  idestadoregistro     = 0
WHERE idestadoregistro = 1
  AND idpersona        = p_idpersona;

return true;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

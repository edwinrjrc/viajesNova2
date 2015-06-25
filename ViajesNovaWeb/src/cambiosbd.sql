

CREATE OR REPLACE FUNCTION negocio.fn_consultarcomprobantesgenerados(p_idcomprobante integer, p_idservicio integer, p_idadquiriente integer, p_idtipocomprobante integer, 
p_numerocomprobante character varying, p_fechadesde date, p_fechahasta date)
  RETURNS refcursor AS
$BODY$
declare micursor refcursor;

begin
open micursor for
SELECT cg.id, cg.idservicio, cg.idtipocomprobante, tm.nombre, cg.numerocomprobante, cg.idtitular, p.nombres, p.apellidopaterno, p.apellidomaterno,
       cg.fechacomprobante, cg.totaligv, cg.totalcomprobante, cg.tienedetraccion, 
       cg.tieneretencion, cg.usuariocreacion, cg.fechacreacion, cg.ipcreacion, cg.usuariomodificacion, 
       cg.fechamodificacion, cg.ipmodificacion
  FROM negocio."ComprobanteGenerado" cg
 INNER JOIN soporte."Tablamaestra" tm ON cg.idtipocomprobante = tm.id AND tm.idmaestro = 17
 INNER JOIN negocio."Persona" p       ON cg.idtitular         = p.id
 WHERE cg.idestadoregistro  = 1
   AND cg.fechacomprobante  BETWEEN p_fechadesde AND p_fechahasta
   AND cg.id                = COALESCE(p_idcomprobante, cg.id)
   AND cg.idservicio        = COALESCE(p_idservicio, cg.idservicio)
   AND cg.idtitular         = COALESCE(p_idadquiriente, cg.idtitular)
   AND cg.idtipocomprobante = COALESCE(p_idtipocomprobante, cg.idtipocomprobante)
   AND cg.numerocomprobante = COALESCE(p_numerocomprobante, cg.numerocomprobante);
   

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
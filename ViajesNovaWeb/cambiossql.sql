
CREATE OR REPLACE FUNCTION auditoria.fn_consultaasistencia(p_fecha date)
  RETURNS refcursor AS
$BODY$
declare micursor refcursor;

begin
open micursor for
select u.usuario, u.nombres, u.apepaterno, u.apematerno, 
       (select max(e.fecharegistro) 
          from auditoria.eventosesionsistema e
         where e.idusuario = u.id
           and date(e.fecharegistro) = p_fecha) as horaInicio
  from seguridad.usuario u
 where u.id_rol <> 1;

return micursor;

end;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

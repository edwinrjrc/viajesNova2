-- Function: negocio.fn_consultarservicioventa(integer)

-- DROP FUNCTION negocio.fn_consultarservicioventa(integer);

CREATE OR REPLACE FUNCTION negocio.fn_consultarservicioventajr(p_idservicio integer)
  RETURNS refcursor AS
$BODY$
declare micursor refcursor;

begin

open micursor for
select cantidad, descripcionservicio, fechaida, 
       fecharegreso, preciobase, montototal, 
       codigoreserva, numeroboleto, idservicio 
  from negocio.vw_servicio_detalle 
 where idservicio = p_idservicio;


return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

-- Function: negocio.fn_consultarservicioventajr(integer)

-- DROP FUNCTION negocio.fn_consultarservicioventajr(integer);

CREATE OR REPLACE FUNCTION negocio.fn_consultarservicioventajr(p_idservicio integer)
  RETURNS refcursor AS
$BODY$
declare micursor refcursor;

begin

open micursor for
select cantidad, descripcionservicio, fechaida, 
       fecharegreso, idmoneda, abreviatura, preciobase, montototal, 
       codigoreserva, numeroboleto, idservicio 
  from negocio.vw_servicio_detalle 
 where idservicio = p_idservicio;


return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

  
-- View: negocio.vw_servicio_detalle

-- DROP VIEW negocio.vw_servicio_detalle;

CREATE OR REPLACE VIEW negocio.vw_servicio_detalle AS 
 SELECT serdet.cantidad, serdet.descripcionservicio, serdet.fechaida, 
    serdet.fecharegreso, serdet.idmoneda, tmmo.abreviatura, serdet.preciobase, serdet.montototal, 
    serdet.codigoreserva, serdet.numeroboleto, serdet.idservicio
   FROM negocio."ServicioDetalle" serdet
  INNER JOIN soporte."Tablamaestra" tmmo ON tmmo.idmaestro = 20 AND serdet.idmoneda = tmmo.id;

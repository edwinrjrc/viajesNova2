-- Function: negocio.fn_consultardetalleservicioventadetalle(integer, integer)

-- DROP FUNCTION negocio.fn_consultardetalleservicioventadetalle(integer, integer);

CREATE OR REPLACE FUNCTION negocio.fn_consultardetalleservicioventadetalle(p_idservicio integer, p_iddetalle integer)
  RETURNS refcursor AS
$BODY$
declare micursor refcursor;

begin
open micursor for
SELECT serdet.id as idSerdetalle, 
       tipser.id as idtiposervicio, tipser.nombre as nomtipservicio, tipser.desccorta as descservicio, tipser.requierefee, 
       tipser.pagaimpto, tipser.cargacomision, tipser.esimpuesto, tipser.esfee, tipser.visible,
       
       serdet.descripcionservicio, 
       serdet.idservicio, 
       serdet.fechaida, 
       serdet.fecharegreso, 
       serdet.cantidad, 
       serdet.idempresaproveedor, 
       serdet.descripcionproveedor,
       pro.nombres, pro.apellidopaterno, pro.apellidomaterno,
       serdet.idempresaoperadora, 
       serdet.descripcionoperador, 
       serdet.idempresatransporte, 
       serdet.descripcionemptransporte, 
       serdet.idhotel, 
       serdet.decripcionhotel, 
       serdet.idruta, 
       serdet.preciobase, 
       serdet.editocomision, 
       serdet.tarifanegociada, 
       serdet.porcencomision, 
       serdet.montocomision, 
       serdet.montototal, 
       serdet.codigoreserva, 
       serdet.numeroboleto
  FROM negocio."ServicioDetalle" serdet
 INNER JOIN negocio."MaestroServicios" tipser ON tipser.id = serdet.idtiposervicio AND tipser.idestadoregistro = 1
  LEFT JOIN negocio.vw_proveedoresnova pro    ON pro.id    = serdet.idempresaproveedor
 WHERE serdet.idestadoregistro = 1
   AND serdet.idservicio       = p_idservicio
   AND serdet.id               = p_iddetalle;

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;


-- Function: negocio.fn_consultardetalleservicioventadetalle(integer, integer)


CREATE OR REPLACE FUNCTION negocio.fn_consultartramosruta(p_idruta integer)
  RETURNS refcursor AS
$BODY$
declare micursor refcursor;

begin
open micursor for
SELECT tramo.descripcionorigen, tramo.fechasalida, tramo.descripciondestino, tramo.fechallegada, tramo.preciobase, per.nombres
  FROM negocio."RutaServicio" ruta
 INNER JOIN negocio."Tramo" tramo ON ruta.idtramo = tramo.id
 INNER JOIN negocio."Persona" per ON per.idestadoregistro  = 1 AND tramo.idaerolinea = per.id
 WHERE ruta.idestadoregistro = 1
   AND ruta.id               = p_idruta;

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;


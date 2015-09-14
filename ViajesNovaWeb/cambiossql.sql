-- Function: negocio.fn_consultarobligacion(integer)

-- DROP FUNCTION negocio.fn_consultarobligacion(integer);

CREATE OR REPLACE FUNCTION negocio.fn_consultarobligacion(p_idobligacion integer)
  RETURNS refcursor AS
$BODY$
declare micursor refcursor;

begin

open micursor for
SELECT oxp.id, idtipocomprobante, tmtd.nombre, numerocomprobante, idproveedor as idtitular, tit.nombres, tit.apellidopaterno, tit.apellidomaterno, fechacomprobante, 
       fechapago, detallecomprobante, totaligv, totalcomprobante, saldocomprobante, tienedetraccion, 
       tieneretencion
  FROM negocio."ObligacionesXPagar" oxp
 INNER JOIN soporte."Tablamaestra" tmtd ON tmtd.idmaestro = 17 AND tmtd.id = oxp.idtipocomprobante
 INNER JOIN negocio."Persona" tit ON tit.id = oxp.idproveedor AND tit.idestadoregistro = 1
 WHERE oxp.id = p_idobligacion;

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

-- Function: negocio.fn_registrarmovimientocuenta(integer, integer, integer, character varying, numeric, integer, integer, character varying, character varying)

-- DROP FUNCTION negocio.fn_registrarmovimientocuenta(integer, integer, integer, character varying, numeric, integer, integer, character varying, character varying);

CREATE OR REPLACE FUNCTION negocio.fn_registrarmovimientocuenta(p_idcuenta integer, p_idtipomovimiento integer, p_idtransaccion integer, p_descripcionnovimiento character varying, p_importemovimiento numeric, p_idautorizador integer, p_idmovimientopadre integer, p_usuariocreacion character varying, p_ipcreacion character varying)
  RETURNS boolean AS
$BODY$

declare maxid integer;
declare fechahoy timestamp with time zone;
declare v_saldocuenta decimal(20,6);
declare v_saldocuenta_actualiza decimal(20,6);
declare v_resultado boolean;

begin

maxid = nextval('negocio.seq_movimientocuenta');
select current_timestamp AT TIME ZONE 'PET' into fechahoy;

INSERT INTO negocio."MovimientoCuenta"(
            id, idcuenta, idtipomovimiento, idtransaccion, descripcionnovimiento, 
            importemovimiento, idautorizador, idmovimientopadre, usuariocreacion, 
            fechacreacion, ipcreacion, usuariomodificacion, fechamodificacion, 
            ipmodificacion)
    VALUES (maxid, p_idcuenta, p_idtipomovimiento, p_idtransaccion, p_descripcionnovimiento, 
            p_importemovimiento, p_idautorizador, p_idmovimientopadre, p_usuariocreacion, 
            fechahoy, p_ipcreacion, p_usuariocreacion, fechahoy, p_ipcreacion);

SELECT saldocuenta
  INTO v_saldocuenta
  FROM negocio."CuentaBancaria"
 WHERE id = p_idcuenta;

if p_idtipomovimiento = 1 then -- ingreso
    v_saldocuenta_actualiza = v_saldocuenta + p_importemovimiento;
else -- egreso
    v_saldocuenta_actualiza = v_saldocuenta - p_importemovimiento;
end if;

select negocio.fn_actualizarcuentabancariasaldo(p_idcuenta, v_saldocuenta_actualiza, p_usuariocreacion, p_ipcreacion) into v_resultado;

return v_resultado;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION negocio.fn_registrarmovimientocuenta(integer, integer, integer, character varying, numeric, integer, integer, character varying, character varying)
  OWNER TO postgres;

-- Function: negocio.fn_listarcuentasbancarias()

-- DROP FUNCTION negocio.fn_listarcuentasbancarias();

CREATE OR REPLACE FUNCTION negocio.fn_listarcuentasbancarias()
  RETURNS refcursor AS
$BODY$

declare micursor refcursor;

begin

open micursor for
SELECT cb.id, cb.nombrecuenta, cb.numerocuenta, cb.idtipocuenta, tmtc.nombre as nombretipocuenta, cb.idbanco, tmba.nombre as nombrebanco, 
       cb.idmoneda, tmmo.nombre as nombremoneda, tmmo.abreviatura, cb.saldocuenta, 
       (SELECT COUNT(1)
          FROM negocio."MovimientoCuenta" mc
         WHERE mc.idcuenta = cb.id) numeroMovimientos,
       cb.usuariocreacion, cb.fechacreacion, cb.ipcreacion, cb.usuariomodificacion, cb.fechamodificacion, cb.ipmodificacion
  FROM negocio."CuentaBancaria" cb,
       soporte."Tablamaestra" tmtc,
       soporte."Tablamaestra" tmba,
       soporte."Tablamaestra" tmmo
 WHERE idestadoregistro = 1
   AND tmtc.idmaestro   = 21
   AND cb.idtipocuenta  = tmtc.id
   AND tmba.idmaestro   = 19
   AND cb.idbanco       = tmba.id
   AND tmmo.idmaestro   = 20
   AND cb.idmoneda      = tmmo.id;

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

  
-- Function: negocio.fn_listarcuentasbancarias()

-- DROP FUNCTION negocio.fn_listarcuentasbancarias();

CREATE OR REPLACE FUNCTION negocio.fn_listarmovimientosxcuenta(p_idcuenta integer)
  RETURNS refcursor AS
$BODY$

declare micursor refcursor;

begin

open micursor for
SELECT id, 
       idcuenta, 
       idtipomovimiento, 
       (CASE idtipomovimiento WHEN 1 THEN 'Ingreso' ELSE 'Egreso' END) as desTipoMovimiento,
       idtransaccion, 
       tmtt.nombre as nombreTransaccion,
       descripcionnovimiento, 
       importemovimiento, 
       idautorizador, 
       idmovimientopadre, 
       usuariocreacion, 
       fechacreacion, 
       ipcreacion, 
       usuariomodificacion, 
       fechamodificacion, 
       ipmodificacion
  FROM negocio."MovimientoCuenta" mc
 INNER JOIN negocio."CuentaBancaria" cb ON cb.idestadoregistro = 1  AND mc.idcuenta = cb.id
 INNER JOIN soporte."Tablamaestra" tmtt ON tmtt.idmaestro      = 13 AND tmtt.id     = idtransaccion
 WHERE mc.idcuenta = p_idcuenta;

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
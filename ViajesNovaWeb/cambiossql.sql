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
  
CREATE TABLE negocio."TipoCambio"
(
  id integer NOT NULL,
  fechatipocambio date,
  idmonedaorigen integer not null,
  idmonedadestino integer not null,
  montocambio decimal (9,6) not null,
  usuariocreacion character varying(20) NOT NULL,
  fechacreacion timestamp with time zone NOT NULL,
  ipcreacion character(15) NOT NULL,
  usuariomodificacion character varying(15) NOT NULL,
  fechamodificacion timestamp with time zone NOT NULL,
  ipmodificacion character(15) NOT NULL,
  idestadoregistro integer NOT NULL DEFAULT 1,
  CONSTRAINT pk_tipocambio PRIMARY KEY (id, fechatipocambio)
)
WITH (
  OIDS=FALSE
);


-- Function: negocio.fn_listarpagos(integer)

-- DROP FUNCTION negocio.fn_listarpagos(integer);

-- Function: negocio.fn_listartipocambio(date)

-- DROP FUNCTION negocio.fn_listartipocambio(date);

CREATE OR REPLACE FUNCTION negocio.fn_listartipocambio(p_fecha date)
  RETURNS refcursor AS
$BODY$

declare micursor refcursor;

begin

open micursor for
SELECT id, fechatipocambio, idmonedaorigen, idmonedadestino, montocambio, 
       usuariocreacion, fechacreacion, ipcreacion, usuariomodificacion, 
       fechamodificacion, ipmodificacion, idestadoregistro
  FROM negocio."TipoCambio"
 WHERE fechatipocambio = COALESCE(p_fecha,fechatipocambio);

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;


CREATE SEQUENCE negocio.seq_tipocambio
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  
-- Function: negocio.fn_listarpagos(integer)

-- DROP FUNCTION negocio.fn_listarpagos(integer);

CREATE OR REPLACE FUNCTION negocio.fn_ingresartipocambio(p_fecha date, p_idmonedaorigen integer, p_idmonedadestino integer, p_montocambio decimal, p_usuariocreacion character varying, p_ipcrecion character varying)
  RETURNS boolean AS
$BODY$

declare maxid integer;
declare fechahoy timestamp with time zone;

begin

maxid = nextval('negocio.seq_salsoservicio');
select current_timestamp AT TIME ZONE 'PET' into fechahoy;

INSERT INTO negocio."TipoCambio"(
            id, fechatipocambio, idmonedaorigen, idmonedadestino, montocambio, 
            usuariocreacion, fechacreacion, ipcreacion, usuariomodificacion, 
            fechamodificacion, ipmodificacion)
    VALUES (maxid, p_fecha, p_idmonedaorigen, p_idmonedadestino, p_montocambio, 
            p_usuariocreacion, fechahoy, p_ipcrecion, p_usuariocreacion, fechahoy, p_ipcrecion);

return true;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

-- Function: negocio.fn_listarpagos(integer)

-- DROP FUNCTION negocio.fn_listarpagos(integer);

-- Function: negocio.fn_consultartipocambio(integer, integer)


-- Function: negocio.fn_consultartipocambio(integer, integer)

-- DROP FUNCTION negocio.fn_consultartipocambio(integer, integer);

-- Function: negocio.fn_consultartipocambio(integer, integer)

-- DROP FUNCTION negocio.fn_consultartipocambio(integer, integer);

CREATE OR REPLACE FUNCTION negocio.fn_consultartipocambio(p_idmonedaorigen integer, p_idmonedadestino integer)
  RETURNS refcursor AS
$BODY$

declare fechahoy date;
declare v_cantidad date;
declare v_idultimo date;
declare v_idtipocambio integer;
declare v_mensaje character varying(15);
declare micursor refcursor;

begin

select current_date into fechahoy;

select count(1)
  into v_cantidad
  from negocio."TipoCambio"
 where fechatipocambio = fechahoy
   and idmonedaorigen  = p_idmonedaorigen
   and idmonedadestino = p_idmonedadestino;

if v_cantidad = 1 then
    select id
      into v_idtipocambio
      from negocio."TipoCambio"
     where fechatipocambio = fechahoy
       and idmonedaorigen  = p_idmonedaorigen
       and idmonedadestino = p_idmonedadestino;
elsif v_cantidad > 1 then
    select max(id)
      into v_idtipocambio
      from negocio."TipoCambio"
     where fechatipocambio = fechahoy
       and idmonedaorigen  = p_idmonedaorigen
       and idmonedadestino = p_idmonedadestino;
else
    select count(1)
      into v_cantidad
      from negocio."TipoCambio"
     where idmonedaorigen  = p_idmonedaorigen
       and idmonedadestino = p_idmonedadestino;

    if v_cantidad >= 1 then
        select max(id)
          into v_idtipocambio
	  from negocio."TipoCambio"
	 where idmonedaorigen  = p_idmonedaorigen
	   and idmonedadestino = p_idmonedadestino;
    else
        v_mensaje = 'Tipo de cambio de '+(select nombre
                                            from soporte."Tablamaestra" 
                                           where idmaestro = 20
                                             and id        = p_idmonedaorigen);
        v_mensaje = v_mensaje + ' a ' + (select nombre
                                           from soporte."Tablamaestra" 
                                          where idmaestro = 20
                                            and id        = p_idmonedadestino);

        v_mensaje = v_mensaje + ' no fue registrado';
        
        RAISE USING MESSAGE = v_mensaje;
    end if;
end if;

open micursor for
SELECT id, fechatipocambio, 
       idmonedaorigen, tmmo.nombre as nombreMonOrigen, 
       idmonedadestino, tmmd.nombre as nombreMonDestino, 
       montocambio
  FROM negocio."TipoCambio" tc
 INNER JOIN soporte."Tablamaestra" tmmo ON tmmo.idmaestro = 20 AND tmmo.id = idmonedaorigen
 INNER JOIN soporte."Tablamaestra" tmmd ON tmmd.idmaestro = 20 AND tmmd.id = idmonedadestino
 WHERE id = v_idtipocambio;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

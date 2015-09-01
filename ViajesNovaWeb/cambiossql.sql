

CREATE TABLE negocio."MovimientoCuenta"
(
  id integer NOT NULL,
  idcuenta integer NOT NULL,
  idtipomovimiento integer NOT NULL,
  idtransaccion integer NOT NULL,
  descripcionnovimiento character varying(100),
  importemovimiento numeric(20,6) NOT NULL DEFAULT 0.000000,
  idautorizador integer,
  idmovimientopadre integer,
  usuariocreacion character varying(20) NOT NULL,
  fechacreacion timestamp with time zone NOT NULL,
  ipcreacion character(15) NOT NULL,
  usuariomodificacion character varying(15) NOT NULL,
  fechamodificacion timestamp with time zone NOT NULL,
  ipmodificacion character(15) NOT NULL,
  idestadoregistro integer NOT NULL DEFAULT 1
)
WITH (
  OIDS=FALSE
);

CREATE SEQUENCE negocio.seq_movimientocuenta
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  

  -- Function: negocio.fn_actualizarcuentabancaria(integer, character varying, character varying, integer, integer, character varying, character varying)

-- DROP FUNCTION negocio.fn_actualizarcuentabancaria(integer, character varying, character varying, integer, integer, character varying, character varying);

CREATE OR REPLACE FUNCTION negocio.fn_actualizarcuentabancariasaldo(p_idcuenta integer, p_saldocuenta decimal, p_usuariomodificacion character varying, p_ipmodificacion character varying)
  RETURNS boolean AS
$BODY$

declare maxid integer;
declare fechahoy timestamp with time zone;

begin

maxid = nextval('negocio.seq_cuentabancaria');
select current_timestamp AT TIME ZONE 'PET' into fechahoy;


UPDATE negocio."CuentaBancaria"
   SET saldocuenta         = p_saldocuenta, 
       usuariomodificacion = p_usuariomodificacion, 
       fechamodificacion   = fechahoy, 
       ipmodificacion      = p_ipmodificacion
 WHERE id                  = p_idcuenta;

return true;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

CREATE OR REPLACE FUNCTION negocio.fn_registrarmovimientocuenta(p_idcuenta integer, p_idtipomovimiento integer, p_idtransaccion integer, p_descripcionnovimiento character varying, 
            p_importemovimiento decimal, p_idautorizador integer, p_idmovimientopadre integer, p_usuariocreacion character varying, 
            p_ipcreacion character varying)
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
    v_saldocuenta_actualiza = v_saldocuenta + p_importemovimiento;
end if;

select negocio.fn_actualizarcuentabancariasaldo(p_idcuenta, v_saldocuenta_actualiza, p_usuariocreacion, p_ipcreacion) into v_resultado;

return v_resultado;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

  
CREATE TABLE soporte."TipoCambio"
(
  id integer NOT NULL,
  monedaorigen character varying(3),
  monedadestino character varying(3),
  tipocambiocompra decimal(9,6) not null,
  tipocambioventa decimal(9,6) not null,
  usuariocreacion character varying(20) NOT NULL,
  fechacreacion timestamp with time zone NOT NULL,
  ipcreacion character(15) NOT NULL,
  usuariomodificacion character varying(15) NOT NULL,
  fechamodificacion timestamp with time zone NOT NULL,
  ipmodificacion character(15) NOT NULL,
  idestadoregistro integer NOT NULL DEFAULT 1
)
WITH (
  OIDS=FALSE
);

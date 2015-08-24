
CREATE TABLE negocio."CuentaBancaria"
(
  id integer NOT NULL,
  nombrecuenta character varying(40) NOT NULL,
  numerocuenta character varying(20) NOT NULL,
  idbanco integer,
  saldocuenta decimal(20,6) not null default 0.000000,
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


CREATE TABLE negocio."MovimientoCuenta"
(
  id integer NOT NULL,
  idcuenta integer not null,
  idtipomovimiento integer not null,
  idtransaccion integer not null,
  descripcionnovimiento character varying(100),
  importemovimiento decimal(20,6) not null default 0.000000,
  idautorizador integer not null,
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

CREATE SEQUENCE negocio.seq_cuentabancaria
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  
  


CREATE OR REPLACE FUNCTION negocio.fn_registrarcuentabancaria(
p_nombrecuenta character varying, p_numerocuenta character varying, p_idbanco integer, p_saldocuenta decimal, p_usuariocreacion character varying, 
p_ipcreacion character varying)
  RETURNS boolean AS
$BODY$

declare maxid integer;
declare fechahoy timestamp with time zone;

begin

maxid = nextval('negocio.seq_cuentabancaria');
select current_timestamp AT TIME ZONE 'PET' into fechahoy;

INSERT INTO negocio."CuentaBancaria"(
            id, nombrecuenta, numerocuenta, idbanco, saldocuenta, usuariocreacion, 
            fechacreacion, ipcreacion, usuariomodificacion, fechamodificacion, 
            ipmodificacion)
    VALUES (maxid, p_nombrecuenta, p_numerocuenta, p_idbanco, p_saldocuenta, p_usuariocreacion, 
            fechahoy, p_ipcreacion, p_usuariocreacion, fechahoy, p_ipcreacion);

return true;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  


CREATE OR REPLACE FUNCTION negocio.fn_actualizarcuentabancaria(p_idcuenta integer,
p_nombrecuenta character varying, p_numerocuenta character varying, p_idbanco integer, p_usuariomodificacion character varying, 
p_ipmodificacion character varying)
  RETURNS boolean AS
$BODY$

declare maxid integer;
declare fechahoy timestamp with time zone;

begin

maxid = nextval('negocio.seq_cuentabancaria');
select current_timestamp AT TIME ZONE 'PET' into fechahoy;


UPDATE negocio."CuentaBancaria"
   SET nombrecuenta        = p_nombrecuenta, 
       numerocuenta        = p_numerocuenta, 
       idbanco             = p_idbanco, 
       usuariomodificacion = p_usuariomodificacion, 
       fechamodificacion   = fechahoy, 
       ipmodificacion      = p_ipmodificacion
 WHERE id                  = p_idcuenta;

return true;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  

CREATE OR REPLACE FUNCTION negocio.fn_listarcuentasbancarias()
  RETURNS refcursor AS
$BODY$

declare micursor refcursor;

begin

open micursor for
SELECT id, nombrecuenta, numerocuenta, idbanco, saldocuenta, usuariocreacion, 
       fechacreacion, ipcreacion, usuariomodificacion, fechamodificacion, 
       ipmodificacion
  FROM negocio."CuentaBancaria"
 WHERE idestadoregistro = 1;

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
CREATE OR REPLACE FUNCTION negocio.fn_consultacuentabancaria(p_idcuenta integer)
  RETURNS refcursor AS
$BODY$

declare micursor refcursor;

begin

open micursor for
SELECT id, nombrecuenta, numerocuenta, idbanco, saldocuenta, usuariocreacion, 
       fechacreacion, ipcreacion, usuariomodificacion, fechamodificacion, 
       ipmodificacion
  FROM negocio."CuentaBancaria"
 WHERE idestadoregistro = 1
   AND id               = p_idcuenta;

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
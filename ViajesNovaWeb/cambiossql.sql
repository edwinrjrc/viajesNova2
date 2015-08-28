  
CREATE TABLE negocio."PagosServicio"
(
  idpago integer NOT NULL,
  idservicio integer NOT NULL,
  idformapago integer NOT NULL,
  idcuentadestino integer,
  idtipotarjeta integer,
  nombretitular character varying(50),
  numerotarjeta character varying(16),
  fechapago date NOT NULL,
  montopagado numeric(12,3),
  sustentopago bytea,
  tipocontenido character varying(50),
  nombrearchivo character varying(20),
  extensionarchivo character varying(10),
  comentario character varying(300),
  espagodetraccion boolean,
  espagoretencion boolean,
  usuariocreacion character varying(20) NOT NULL,
  fechacreacion timestamp with time zone NOT NULL,
  ipcreacion character(15) NOT NULL,
  usuariomodificacion character varying(15) NOT NULL,
  fechamodificacion timestamp with time zone NOT NULL,
  ipmodificacion character(15) NOT NULL,
  idestadoregistro integer NOT NULL DEFAULT 1,
  CONSTRAINT pk_pagosservicio PRIMARY KEY (idpago),
  CONSTRAINT fk_servicio FOREIGN KEY (idservicio)
      REFERENCES negocio."ServicioCabecera" (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);


-- Function: negocio.fn_registrarpagoservicio(integer, date, numeric, bytea, character varying, character varying, character varying, character varying, boolean, boolean, character varying, character varying)

-- DROP FUNCTION negocio.fn_registrarpagoservicio(integer, date, numeric, bytea, character varying, character varying, character varying, character varying, boolean, boolean, character varying, character varying);

CREATE OR REPLACE FUNCTION negocio.fn_registrarpagoservicio(p_idservicio integer, p_idformapago integer, p_idcuentadestino integer, 
p_idtipotarjeta integer, p_nombretitular character varying, p_numerotarjeta character varying, 
p_fechapago date, p_montopago numeric, p_sustentopago bytea, 
p_nombrearchivo character varying, p_extensionarchivo character varying, p_tipocontenido character varying, p_comentario character varying, 
p_espagodetraccion boolean, p_espagoretencion boolean, p_usuariocreacion character varying, p_ipcreacion character varying)
  RETURNS integer AS
$BODY$

declare maxid integer;
declare maxidss integer;
declare fechahoy timestamp with time zone;
declare montosaldo decimal(12,3);
declare montosaldofinal decimal(12,3);
declare fechaservicio date;
declare montoservicio decimal(12,3);
declare estadoPago integer;

begin

select idestadopago
  into estadoPago
  from negocio."ServicioCabecera"
 where id = p_idservicio;

if estadoPago = 2 then
    raise USING MESSAGE = 'El servicio se encuentra pagado ya no acepta mas pagos';
end if;

select min(montosaldoservicio)
  into montosaldo
  from negocio."SaldosServicio" ss
 where ss.idservicio = p_idservicio;

if p_montopago > montosaldo then
   raise USING MESSAGE = 'El monto a pagar es mayor que el saldo pendiente';
end if;

maxid = nextval('negocio.seq_pago');
select current_timestamp AT TIME ZONE 'PET' into fechahoy;

select fechacompra, montototal
  into fechaservicio, montoservicio
  from negocio."ServicioCabecera"
 where id = p_idservicio;

INSERT INTO negocio."PagosServicio"(
            idpago, idservicio, idformapago, idcuentadestino, idtipotarjeta, 
            nombretitular, numerotarjeta, fechapago, montopagado, sustentopago, 
            tipocontenido, nombrearchivo, extensionarchivo, comentario, espagodetraccion, 
            espagoretencion, usuariocreacion, fechacreacion, ipcreacion)
    VALUES (maxid, p_idservicio, p_idformapago, p_idcuentadestino, p_idtipotarjeta, 
            p_nombretitular, p_numerotarjeta, p_fechapago, p_montopago, p_sustentopago, 
            p_tipocontenido, p_nombrearchivo, p_extensionarchivo, p_comentario, p_espagodetraccion, 
            p_espagoretencion, p_usuariocreacion, fechahoy, p_ipcreacion);

montosaldofinal = montosaldo - p_montopago;

maxidss = nextval('negocio.seq_salsoservicio');
INSERT INTO negocio."SaldosServicio"(
            idsaldoservicio, idservicio, idpago, fechaservicio, montototalservicio, 
            montosaldoservicio, usuariocreacion, fechacreacion, ipcreacion, 
            usuariomodificacion, fechamodificacion, ipmodificacion)
    VALUES (maxidss, p_idservicio, maxid, fechaservicio, montoservicio, 
            montosaldofinal, p_usuariocreacion, fechahoy, p_ipcreacion, 
            p_usuariocreacion, fechahoy, p_ipcreacion);

if montosaldofinal = 0 then
   update negocio."ServicioCabecera"
      set idestadopago = 2
    where id           = p_idservicio;
end if;

return maxid;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

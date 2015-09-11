-- Table: negocio."PagosObligacion"


CREATE TABLE negocio."PagosObligacion"
(
  idpago integer NOT NULL,
  idobligacion integer NOT NULL,
  idformapago integer NOT NULL,
  idcuentaorigen integer,
  idcuentadestino integer,
  idbancotarjeta integer,
  idtipotarjeta integer,
  nombretitular character varying(50),
  numerotarjeta character varying(16),
  fechapago date NOT NULL,
  numerooperacion character varying(20),
  montopagado numeric(12,3),
  sustentopago bytea,
  tipocontenido character varying(50),
  nombrearchivo character varying(20),
  extensionarchivo character varying(10),
  comentario character varying(300),
  espagodetraccion boolean,
  espagoretencion boolean,
  usuariocreacion character varying(15) NOT NULL,
  fechacreacion timestamp with time zone NOT NULL,
  ipcreacion character(15) NOT NULL,
  usuariomodificacion character varying(15) NOT NULL,
  fechamodificacion timestamp with time zone NOT NULL,
  ipmodificacion character(15) NOT NULL,
  idestadoregistro integer NOT NULL DEFAULT 1,
  CONSTRAINT pk_pagoobligacion PRIMARY KEY (idpago),
  CONSTRAINT fk_obligacionesxpagar FOREIGN KEY (idobligacion)
      REFERENCES negocio."ObligacionesXPagar" (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE negocio."PagosObligacion"
  OWNER TO postgres;



-- Function: negocio.fn_registrarpagoobligacion(integer, integer, integer, integer, integer, character varying, character varying, date, numeric, bytea, character varying, character varying, character varying, character varying, boolean, boolean, integer, character varying, character varying)

CREATE OR REPLACE FUNCTION negocio.fn_registrarpagoobligacion(p_idobligacion integer, p_idformapago integer, 
p_idcuentaorigen integer, p_idcuentadestino integer, p_idbancotarjeta integer, p_idtipotarjeta integer, p_nombretitular character varying, 
p_numerotarjeta character varying, p_fechapago date, p_numerooperacion character varying, p_montopago numeric, p_sustentopago bytea, p_nombrearchivo character varying, p_extensionarchivo character varying, p_tipocontenido character varying, p_comentario character varying, p_espagodetraccion boolean, p_espagoretencion boolean, p_usuarioautoriza integer, p_usuariocreacion character varying, p_ipcreacion character varying)
  RETURNS integer AS
$BODY$

declare maxid integer;
declare fechahoy timestamp with time zone;
declare v_montocomprobante decimal(12,3);
declare v_montosaldo decimal(12,3);
declare v_montopagado decimal(12,3);
declare v_tipomovimiento integer;
declare v_tipotransaccion integer;
declare v_desctransaccion character varying;
declare v_registramovimiento boolean;

begin

maxid = nextval('negocio.seq_pago');
select current_timestamp AT TIME ZONE 'PET' into fechahoy;

SELECT totalcomprobante
  INTO v_montocomprobante
  FROM negocio."ObligacionesXPagar"
 WHERE id = p_idobligacion;

SELECT SUM(montopagado)
  into v_montopagado
  FROM negocio."PagosObligacion"
 WHERE idobligacion = p_idobligacion;

if v_montopagado is not null then
	v_montosaldo = v_montocomprobante - v_montopagado;
	IF v_montosaldo < p_montopago THEN
		raise USING MESSAGE = 'El monto a pagar es mayor que el saldo pendiente';
	END IF;
else
    v_montosaldo = v_montocomprobante - p_montopago;
end if;

INSERT INTO negocio."PagosObligacion"(
            idpago, idobligacion, idformapago, idcuentaorigen, idcuentadestino, idbancotarjeta, 
            idtipotarjeta, nombretitular, numerotarjeta, fechapago, numerooperacion, montopagado, 
            sustentopago, tipocontenido, nombrearchivo, extensionarchivo, 
            comentario, espagodetraccion, espagoretencion, usuariocreacion, 
            fechacreacion, ipcreacion, usuariomodificacion, fechamodificacion, 
            ipmodificacion)
    VALUES (maxid, p_idobligacion, p_idformapago, p_idcuentaorigen, p_idcuentadestino, p_idbancotarjeta, 
            p_idtipotarjeta, p_nombretitular, p_numerotarjeta, p_fechapago, p_numerooperacion, p_montopago, 
            p_sustentopago, p_tipocontenido, p_nombrearchivo, p_extensionarchivo, 
            p_comentario, p_espagodetraccion, p_espagoretencion, p_usuariocreacion, 
            fechahoy, p_ipcreacion, p_usuariocreacion, fechahoy, p_ipcreacion);


UPDATE negocio."ObligacionesXPagar"
   SET saldocomprobante = v_montosaldo
 WHERE id               = p_idobligacion;


-- 1: ingreso
-- 2: egreso
v_tipomovimiento = 2;
if p_idformapago = 2 then
    v_tipotransaccion = 1;-- deposito en cuenta
    v_desctransaccion = 'Deposito en cuenta';
elsif p_idformapago = 3 then
    v_tipotransaccion = 2;-- transferencia
    v_desctransaccion = 'Transferencia de fondos a cuenta';
end if;

select negocio.fn_registrarmovimientocuenta(p_idcuentaorigen, v_tipomovimiento, v_tipotransaccion, v_desctransaccion, p_montopago, 
p_usuarioautoriza, null, p_usuariocreacion, p_ipcreacion) into v_registramovimiento;

return maxid;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

  
-- Table: negocio."PagosServicio"

CREATE TABLE negocio."PagosServicio"
(
  idpago integer NOT NULL,
  idservicio integer NOT NULL,
  idformapago integer NOT NULL,
  idcuentadestino integer,
  idbancotarjeta integer,
  idtipotarjeta integer,
  nombretitular character varying(50),
  numerotarjeta character varying(16),
  numerooperacion character varying(20),
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

  

-- Function: negocio.fn_registrarpagoservicio(integer, integer, integer, integer, integer, character varying, character varying, date, numeric, bytea, character varying, character varying, character varying, character varying, boolean, boolean, character varying, character varying)


CREATE OR REPLACE FUNCTION negocio.fn_registrarpagoservicio(p_idservicio integer, p_idformapago integer, p_idcuentadestino integer, p_idbancotarjeta integer, 
p_idtipotarjeta integer, p_nombretitular character varying, p_numerotarjeta character varying, p_fechapago date, p_numerooperacion character varying, p_montopago numeric, p_sustentopago bytea, 
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
declare v_tipotransaccion integer;
declare v_desctransaccion character varying;
declare v_registramovimiento boolean;

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
            idpago, idservicio, idformapago, idcuentadestino, idbancotarjeta, idtipotarjeta, 
            nombretitular, numerotarjeta, fechapago, numerooperacion, montopagado, sustentopago, 
            tipocontenido, nombrearchivo, extensionarchivo, comentario, espagodetraccion, 
            espagoretencion, usuariocreacion, fechacreacion, ipcreacion, usuariomodificacion, fechamodificacion, ipmodificacion)
    VALUES (maxid, p_idservicio, p_idformapago, p_idcuentadestino, p_idbancotarjeta, p_idtipotarjeta, 
            p_nombretitular, p_numerotarjeta, p_fechapago, p_numerooperacion, p_montopago, p_sustentopago, 
            p_tipocontenido, p_nombrearchivo, p_extensionarchivo, p_comentario, p_espagodetraccion, 
            p_espagoretencion, p_usuariocreacion, fechahoy, p_ipcreacion, p_usuariocreacion, fechahoy, p_ipcreacion);

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

-- 1: ingreso
-- 2: egreso
if p_idformapago = 2 then
    v_tipotransaccion = 1;-- deposito en cuenta
    v_desctransaccion = 'Deposito en cuenta';
elsif p_idformapago = 3 then
    v_tipotransaccion = 2;-- transferencia
    v_desctransaccion = 'Transferencia de fondos a cuenta';
end if;

select negocio.fn_registrarmovimientocuenta(p_idcuentadestino, 1, v_tipotransaccion, v_desctransaccion, p_montopago, null, null, p_usuariocreacion, p_ipcreacion) into v_registramovimiento;

return maxid;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

  
CREATE TABLE negocio."ProveedorCuentaBancaria"
(
  id integer not null,
  nombrecuenta character varying(40) NOT NULL,
  numerocuenta character varying(20) NOT NULL,
  idtipocuenta integer NOT NULL,
  idbanco integer not null,
  idmoneda integer NOT NULL,
  idproveedor integer NOT NULL,
  usuariocreacion character varying(15) NOT NULL,
  fechacreacion timestamp with time zone NOT NULL,
  ipcreacion character varying(15) NOT NULL,
  usuariomodificacion character varying(15) NOT NULL,
  fechamodificacion timestamp with time zone NOT NULL,
  ipmodificacion character varying(15) NOT NULL,
  idestadoregistro integer NOT NULL DEFAULT 1,
  CONSTRAINT pk_proveedorcuentabancaria PRIMARY KEY (id),
  CONSTRAINT fk_proveedorcuentabancaria FOREIGN KEY (idproveedor)
      REFERENCES negocio."Persona" (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);

CREATE SEQUENCE negocio.seq_cuentabancariaproveedor
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  

CREATE OR REPLACE FUNCTION negocio.fn_ingresarcuentabancariaproveedor(p_nombrecuenta character varying, p_numerocuenta character varying, p_idtipocuenta integer, p_idbanco integer, 
p_idmoneda integer, p_idproveedor integer, p_usuariocreacion character varying, p_ipcreacion character varying)
  RETURNS boolean AS
$BODY$

declare fechahoy timestamp with time zone;
declare maxid integer;

begin

select current_timestamp AT TIME ZONE 'PET' into fechahoy;
maxid = nextval('negocio.seq_cuentabancariaproveedor');

INSERT INTO negocio."ProveedorCuentaBancaria"(
            id, nombrecuenta, numerocuenta, idtipocuenta, idbanco, idmoneda, 
            idproveedor, usuariocreacion, fechacreacion, ipcreacion, usuariomodificacion, 
            fechamodificacion, ipmodificacion)
    VALUES (maxid, p_nombrecuenta, p_numerocuenta, p_idtipocuenta, p_idbanco, p_idmoneda, 
            p_idproveedor, p_usuariocreacion, fechahoy, p_ipcreacion, p_usuariocreacion, fechahoy, p_ipcreacion);

return true;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

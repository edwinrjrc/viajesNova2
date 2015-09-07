-- Table: negocio."ObligacionesXPagar"

CREATE TABLE negocio."ObligacionesXPagar"
(
  id integer NOT NULL,
  idtipocomprobante integer NOT NULL,
  numerocomprobante character varying(20) NOT NULL,
  idproveedor integer NOT NULL,
  fechacomprobante date NOT NULL,
  fechapago date,
  detallecomprobante character varying(300),
  totaligv numeric(12,3) NOT NULL,
  totalcomprobante numeric(12,3) NOT NULL,
  saldocomprobante decimal(12,3) NOT NULL,
  tienedetraccion boolean,
  tieneretencion boolean,
  usuariocreacion character varying(20) NOT NULL,
  fechacreacion timestamp with time zone NOT NULL,
  ipcreacion character(15) NOT NULL,
  usuariomodificacion character varying(15) NOT NULL,
  fechamodificacion timestamp with time zone NOT NULL,
  ipmodificacion character(15) NOT NULL,
  idestadoregistro integer NOT NULL DEFAULT 1,
  CONSTRAINT pk_obligacionesxpagar PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);


CREATE TABLE negocio."PagosObligacion"
(
  idpago integer NOT NULL,
  idobligacion integer NOT NULL,
  idformapago integer NOT NULL,
  idcuentaorigen integer,
  idbancotarjeta integer,
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


-- Function: negocio.fn_registrarpagoobligacion(integer, integer, integer, integer, integer, character varying, character varying, date, numeric, bytea, character varying, character varying, character varying, character varying, boolean, boolean, integer, character varying, character varying)


-- Function: negocio.fn_registrarpagoobligacion(integer, integer, integer, integer, integer, character varying, character varying, date, numeric, bytea, character varying, character varying, character varying, character varying, boolean, boolean, integer, character varying, character varying)

-- DROP FUNCTION negocio.fn_registrarpagoobligacion(integer, integer, integer, integer, integer, character varying, character varying, date, numeric, bytea, character varying, character varying, character varying, character varying, boolean, boolean, integer, character varying, character varying);

CREATE OR REPLACE FUNCTION negocio.fn_registrarpagoobligacion(p_idobligacion integer, p_idformapago integer, p_idcuentaorigen integer, p_idbancotarjeta integer, p_idtipotarjeta integer, p_nombretitular character varying, p_numerotarjeta character varying, p_fechapago date, p_montopago numeric, p_sustentopago bytea, p_nombrearchivo character varying, p_extensionarchivo character varying, p_tipocontenido character varying, p_comentario character varying, p_espagodetraccion boolean, p_espagoretencion boolean, p_usuarioautoriza integer, p_usuariocreacion character varying, p_ipcreacion character varying)
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
end if;

INSERT INTO negocio."PagosObligacion"(
            idpago, idobligacion, idformapago, idcuentaorigen, idbancotarjeta, 
            idtipotarjeta, nombretitular, numerotarjeta, fechapago, montopagado, 
            sustentopago, tipocontenido, nombrearchivo, extensionarchivo, 
            comentario, espagodetraccion, espagoretencion, usuariocreacion, 
            fechacreacion, ipcreacion, usuariomodificacion, fechamodificacion, 
            ipmodificacion)
    VALUES (maxid, p_idobligacion, p_idformapago, p_idcuentaorigen, p_idbancotarjeta, 
            p_idtipotarjeta, p_nombretitular, p_numerotarjeta, p_fechapago, p_montopago, 
            p_sustentopago, p_tipocontenido, p_nombrearchivo, p_extensionarchivo, 
            p_comentario, p_espagodetraccion, p_espagoretencion, p_usuariocreacion, 
            fechahoy, p_ipcreacion, p_usuariocreacion, fechahoy, p_ipcreacion);


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

-- Function: negocio.fn_ingresarobligacionxpagar(integer, character varying, integer, date, date, character varying, numeric, numeric, boolean, boolean, character varying, character varying)


CREATE OR REPLACE FUNCTION negocio.fn_ingresarobligacionxpagar(p_idtipocomprobante integer, p_numerocomprobante character varying, p_idproveedor integer, p_fechacomprobante date, p_fechapago date, p_detallecomprobante character varying, p_totaligv numeric, p_totalcomprobante numeric, p_tienedetraccion boolean, p_tieneretencion boolean, p_usuariocreacion character varying, p_ipcreacion character varying)
  RETURNS boolean AS
$BODY$

declare maxid integer;
declare fechahoy timestamp with time zone;

begin

maxid = nextval('negocio.seq_obligacionxpagar');

select current_timestamp AT TIME ZONE 'PET' into fechahoy;

INSERT INTO negocio."ObligacionesXPagar"(
            id, idtipocomprobante, numerocomprobante, idproveedor, fechacomprobante, 
            fechapago, detallecomprobante, totaligv, totalcomprobante, saldocomprobante, tienedetraccion, tieneretencion, usuariocreacion, 
            fechacreacion, ipcreacion, usuariomodificacion, fechamodificacion, 
            ipmodificacion)
    VALUES (maxid, p_idtipocomprobante, p_numerocomprobante, p_idproveedor, p_fechacomprobante, 
            p_fechapago, p_detallecomprobante, p_totaligv, p_totalcomprobante, p_totalcomprobante, p_tienedetraccion, p_tieneretencion, p_usuariocreacion, 
            fechahoy, p_ipcreacion, p_usuariocreacion, fechahoy, p_ipcreacion);

return true;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

  
-- Function: negocio.fn_consultarobligacionxpagar(integer, character varying, integer)

-- DROP FUNCTION negocio.fn_consultarobligacionxpagar(integer, character varying, integer);

CREATE OR REPLACE FUNCTION negocio.fn_consultarobligacionxpagar(p_idtipocomprobante integer, p_numerocomprobante character varying, p_idproveedor integer)
  RETURNS refcursor AS
$BODY$
declare micursor refcursor;

begin
open micursor for
select oxp.id, oxp.idtipocomprobante, tm.nombre as nombrecomprobante, oxp.numerocomprobante, 
       oxp.idproveedor, pro.nombres, oxp.fechacomprobante, 
       oxp.fechapago, oxp.detallecomprobante, oxp.totaligv, oxp.totalcomprobante, oxp.saldocomprobante
  from negocio."ObligacionesXPagar" oxp,
       soporte."Tablamaestra" tm,
       negocio."Persona" pro
 where oxp.idtipocomprobante = tm.id
   and tm.idmaestro          = 17
   and pro.idestadoregistro  = 1
   and pro.idtipopersona     = 2
   and pro.id                = oxp.idproveedor
   and oxp.idtipocomprobante = COALESCE(p_idtipocomprobante,oxp.idtipocomprobante)
   and oxp.numerocomprobante = COALESCE(p_numerocomprobante,oxp.numerocomprobante)
   and oxp.idproveedor       = COALESCE(p_idproveedor,oxp.idproveedor);

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

-- Function: negocio.fn_registrarpagoobligacion(integer, integer, integer, integer, integer, character varying, character varying, date, numeric, bytea, character varying, character varying, character varying, character varying, boolean, boolean, integer, character varying, character varying)

-- DROP FUNCTION negocio.fn_registrarpagoobligacion(integer, integer, integer, integer, integer, character varying, character varying, date, numeric, bytea, character varying, character varying, character varying, character varying, boolean, boolean, integer, character varying, character varying);

CREATE OR REPLACE FUNCTION negocio.fn_registrarpagoobligacion(p_idobligacion integer, p_idformapago integer, p_idcuentaorigen integer, p_idbancotarjeta integer, p_idtipotarjeta integer, p_nombretitular character varying, p_numerotarjeta character varying, p_fechapago date, p_montopago numeric, p_sustentopago bytea, p_nombrearchivo character varying, p_extensionarchivo character varying, p_tipocontenido character varying, p_comentario character varying, p_espagodetraccion boolean, p_espagoretencion boolean, p_usuarioautoriza integer, p_usuariocreacion character varying, p_ipcreacion character varying)
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
end if;

INSERT INTO negocio."PagosObligacion"(
            idpago, idobligacion, idformapago, idcuentaorigen, idbancotarjeta, 
            idtipotarjeta, nombretitular, numerotarjeta, fechapago, montopagado, 
            sustentopago, tipocontenido, nombrearchivo, extensionarchivo, 
            comentario, espagodetraccion, espagoretencion, usuariocreacion, 
            fechacreacion, ipcreacion, usuariomodificacion, fechamodificacion, 
            ipmodificacion)
    VALUES (maxid, p_idobligacion, p_idformapago, p_idcuentaorigen, p_idbancotarjeta, 
            p_idtipotarjeta, p_nombretitular, p_numerotarjeta, p_fechapago, p_montopago, 
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

-- Function: negocio.fn_listartipocambio(date)

-- DROP FUNCTION negocio.fn_listartipocambio(date);

CREATE OR REPLACE FUNCTION negocio.fn_listartipocambio(p_fecha date)
  RETURNS refcursor AS
$BODY$

declare micursor refcursor;

begin

open micursor for
SELECT tc.id, fechatipocambio, 
       idmonedaorigen, tmmo.nombre as nombreMonOrigen, 
       idmonedadestino, tmmd.nombre as nombreMonDestino, 
       montocambio
  FROM negocio."TipoCambio" tc
 INNER JOIN soporte."Tablamaestra" tmmo ON tmmo.idmaestro = 20 AND tmmo.id = idmonedaorigen
 INNER JOIN soporte."Tablamaestra" tmmd ON tmmd.idmaestro = 20 AND tmmd.id = idmonedadestino
 WHERE fechatipocambio = COALESCE(p_fecha,fechatipocambio)
 ORDER BY tc.id DESC;

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION negocio.fn_listartipocambio(date)
  OWNER TO postgres;



-- Function: negocio.fn_ingresartipocambio(date, integer, integer, numeric, character varying, character varying)

-- DROP FUNCTION negocio.fn_ingresartipocambio(date, integer, integer, numeric, character varying, character varying);

CREATE OR REPLACE FUNCTION negocio.fn_ingresartipocambio(p_fecha date, p_idmonedaorigen integer, p_idmonedadestino integer, p_montocambio numeric, p_usuariocreacion character varying, p_ipcrecion character varying)
  RETURNS boolean AS
$BODY$

declare maxid integer;
declare fechahoy timestamp with time zone;

begin

maxid = nextval('negocio.seq_tipocambio');
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
ALTER FUNCTION negocio.fn_ingresartipocambio(date, integer, integer, numeric, character varying, character varying)
  OWNER TO postgres;


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
  montopagado numeric(12,3) not null,
  idmoneda integer not null,
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
  montopagado numeric(12,3) not null,
  idmoneda integer not null,
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

CREATE TABLE negocio."TransaccionTipoCambio"
(
  id integer NOT NULL,
  idmonedainicio integer,
  montoinicio decimal(15,2) not null,
  tipocambio decimal(9,6) not null,
  idmonedafin integer,
  montofin decimal(15,2) not null,
  usuariocreacion character varying(20) NOT NULL,
  fechacreacion timestamp with time zone NOT NULL,
  ipcreacion character(15) NOT NULL,
  usuariomodificacion character varying(15) NOT NULL,
  fechamodificacion timestamp with time zone NOT NULL,
  ipmodificacion character(15) NOT NULL,
  idestadoregistro integer NOT NULL DEFAULT 1,
  CONSTRAINT pk_transacciontipocambio PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);


CREATE SEQUENCE negocio.seq_transacciontipocambio
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  
CREATE OR REPLACE FUNCTION negocio.fn_registrartransacciontipocambio(p_idmonedainicio integer, p_montoinicio decimal, p_tipocambio decimal, p_idmonedafin integer, 
p_montofin decimal, p_usuariocreacion character varying, p_ipcreacion character varying)
  RETURNS integer AS
$BODY$

declare maxid integer;
declare fechahoy timestamp with time zone;

begin

maxid = nextval('negocio.seq_transacciontipocambio');
select current_timestamp AT TIME ZONE 'PET' into fechahoy;

INSERT INTO negocio."TransaccionTipoCambio"(
            id, idmonedainicio, montoinicio, tipocambio, idmonedafin, montofin, 
            usuariocreacion, fechacreacion, ipcreacion, usuariomodificacion, 
            fechamodificacion, ipmodificacion)
    VALUES (maxid, p_idmonedainicio, p_montoinicio, p_tipocambio, p_idmonedafin, p_montofin, 
            p_usuariocreacion, fechahoy, p_ipcreacion, p_usuariocreacion, fechahoy, p_ipcreacion);

return maxid;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  

-- Function: negocio.fn_consultartipocambiomonto(integer, integer)

-- DROP FUNCTION negocio.fn_consultartipocambiomonto(integer, integer);

CREATE OR REPLACE FUNCTION negocio.fn_consultartipocambiomonto(p_idmonedaorigen integer, p_idmonedadestino integer)
  RETURNS numeric AS
$BODY$

declare fechahoy date;
declare v_cantidad integer;
declare v_idtipocambio integer;
declare v_mensaje character varying(15);
declare v_tipocambio decimal(9,6);

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

SELECT montocambio
  INTO v_tipocambio
  FROM negocio."TipoCambio"
 WHERE id = v_idtipocambio;

return v_tipocambio;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

  
-- Function: negocio.fn_registrarpagoservicio(integer, integer, integer, integer, integer, character varying, character varying, date, character varying, numeric, bytea, character varying, character varying, character varying, character varying, boolean, boolean, character varying, character varying)

--DROP FUNCTION negocio.fn_registrarpagoservicio(integer, integer, integer, integer, integer, character varying, character varying, date, character varying, numeric, bytea, character varying, character varying, character varying, character varying, boolean, boolean, character varying, character varying);

CREATE OR REPLACE FUNCTION negocio.fn_registrarpagoservicio(p_idservicio integer, p_idformapago integer, p_idcuentadestino integer, p_idbancotarjeta integer, 
p_idtipotarjeta integer, p_nombretitular character varying, p_numerotarjeta character varying, p_fechapago date, p_numerooperacion character varying, 
p_montopago numeric, p_idmoneda integer,
p_sustentopago bytea, p_nombrearchivo character varying, p_extensionarchivo character varying, 
p_tipocontenido character varying, p_comentario character varying, p_espagodetraccion boolean, p_espagoretencion boolean, 
p_usuariocreacion character varying, p_ipcreacion character varying)
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
declare v_monedaservicio integer;
declare v_tipocambio decimal(12,3);
declare v_montoaplicar decimal(12,3);

begin

v_monedaservicio = 2;

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

if p_idmoneda <> v_monedaservicio then
    select negocio.fn_consultartipocambiomonto(p_idmoneda,v_monedaservicio) into v_tipocambio;
    v_montoaplicar = p_montopago * v_tipocambio;
else
    v_montoaplicar = p_montopago;
end if;

if v_montoaplicar > montosaldo then
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
            nombretitular, numerotarjeta, fechapago, numerooperacion, montopagado, idmoneda, sustentopago, 
            tipocontenido, nombrearchivo, extensionarchivo, comentario, espagodetraccion, 
            espagoretencion, usuariocreacion, fechacreacion, ipcreacion, usuariomodificacion, fechamodificacion, ipmodificacion)
    VALUES (maxid, p_idservicio, p_idformapago, p_idcuentadestino, p_idbancotarjeta, p_idtipotarjeta, 
            p_nombretitular, p_numerotarjeta, p_fechapago, p_numerooperacion, p_montopago, p_idmoneda, p_sustentopago, 
            p_tipocontenido, p_nombrearchivo, p_extensionarchivo, p_comentario, p_espagodetraccion, 
            p_espagoretencion, p_usuariocreacion, fechahoy, p_ipcreacion, p_usuariocreacion, fechahoy, p_ipcreacion);

montosaldofinal = montosaldo - v_montoaplicar;

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

DROP TABLE negocio."SaldosServicio";

CREATE TABLE negocio."SaldosServicio"
(
  idsaldoservicio integer NOT NULL,
  idservicio integer NOT NULL,
  idpago integer,
  fechaservicio date NOT NULL,
  montototalservicio numeric(12,3) NOT NULL,
  montosaldoservicio numeric(12,3) NOT NULL,
  idtransaccionreferencia integer,
  usuariocreacion character varying(20) NOT NULL,
  fechacreacion timestamp with time zone NOT NULL,
  ipcreacion character(15) NOT NULL,
  usuariomodificacion character varying(15) NOT NULL,
  fechamodificacion timestamp with time zone NOT NULL,
  ipmodificacion character(15) NOT NULL,
  idestadoregistro integer NOT NULL DEFAULT 1,
  CONSTRAINT pk_saldosservicio PRIMARY KEY (idsaldoservicio),
  CONSTRAINT fk_servicio FOREIGN KEY (idservicio)
      REFERENCES negocio."ServicioCabecera" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

-- Function: negocio.fn_registrarsaldoservicio(integer, integer, date, numeric, character varying, character varying)

CREATE OR REPLACE FUNCTION negocio.fn_registrarsaldoservicio(p_idservicio integer, p_idpago integer, p_fechaservicio date, 
p_montototalservicio numeric, idreferencia integer, p_usuariocreacion character varying, p_ipcreacion character varying)
  RETURNS integer AS
$BODY$

declare maxid integer;
declare fechahoy timestamp with time zone;

begin

maxid = nextval('negocio.seq_salsoservicio');
select current_timestamp AT TIME ZONE 'PET' into fechahoy;


INSERT INTO negocio."SaldosServicio"(
            idsaldoservicio, idservicio, idpago, fechaservicio, montototalservicio, 
            montosaldoservicio, idtransaccionreferencia, usuariocreacion, fechacreacion, ipcreacion, 
            usuariomodificacion, fechamodificacion, ipmodificacion)
    VALUES (maxid, p_idservicio, p_idpago, p_fechaservicio, p_montototalservicio, 
            p_montototalservicio, idreferencia, p_usuariocreacion, fechahoy, p_ipcreacion, 
            p_usuariocreacion, fechahoy, p_ipcreacion);

return maxid;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

-- Function: negocio.fn_registrarpagoservicio(integer, integer, integer, integer, integer, character varying, character varying, date, character varying, numeric, integer, bytea, character varying, character varying, character varying, character varying, boolean, boolean, character varying, character varying)

-- DROP FUNCTION negocio.fn_registrarpagoservicio(integer, integer, integer, integer, integer, character varying, character varying, date, character varying, numeric, integer, bytea, character varying, character varying, character varying, character varying, boolean, boolean, character varying, character varying);

CREATE OR REPLACE FUNCTION negocio.fn_registrarpagoservicio(p_idservicio integer, p_idformapago integer, p_idcuentadestino integer, p_idbancotarjeta integer, p_idtipotarjeta integer, p_nombretitular character varying, p_numerotarjeta character varying, p_fechapago date, p_numerooperacion character varying, p_montopago numeric, p_idmoneda integer, p_sustentopago bytea, p_nombrearchivo character varying, p_extensionarchivo character varying, p_tipocontenido character varying, p_comentario character varying, p_espagodetraccion boolean, p_espagoretencion boolean, p_usuariocreacion character varying, p_ipcreacion character varying)
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
declare v_monedaservicio integer;
declare v_tipocambio decimal(12,3);
declare v_montoaplicar decimal(12,3);
declare v_registrotranstc integer;

begin

v_monedaservicio = 2;

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

if p_idmoneda <> v_monedaservicio then
    select negocio.fn_consultartipocambiomonto(p_idmoneda,v_monedaservicio) into v_tipocambio;
    v_montoaplicar = p_montopago * v_tipocambio;
else
    v_montoaplicar = p_montopago;
end if;

if v_montoaplicar > montosaldo then
   raise USING MESSAGE = 'El monto a pagar es mayor que el saldo pendiente';
end if;

maxid = nextval('negocio.seq_pago');
select current_timestamp AT TIME ZONE 'PET' into fechahoy;

select fechacompra, montototal
  into fechaservicio, montoservicio
  from negocio."ServicioCabecera"
 where id = p_idservicio;

if montosaldo is null then
    montosaldo = montoservicio;
end if;

INSERT INTO negocio."PagosServicio"(
            idpago, idservicio, idformapago, idcuentadestino, idbancotarjeta, idtipotarjeta, 
            nombretitular, numerotarjeta, fechapago, numerooperacion, montopagado, idmoneda, sustentopago, 
            tipocontenido, nombrearchivo, extensionarchivo, comentario, espagodetraccion, 
            espagoretencion, usuariocreacion, fechacreacion, ipcreacion, usuariomodificacion, fechamodificacion, ipmodificacion)
    VALUES (maxid, p_idservicio, p_idformapago, p_idcuentadestino, p_idbancotarjeta, p_idtipotarjeta, 
            p_nombretitular, p_numerotarjeta, p_fechapago, p_numerooperacion, p_montopago, p_idmoneda, p_sustentopago, 
            p_tipocontenido, p_nombrearchivo, p_extensionarchivo, p_comentario, p_espagodetraccion, 
            p_espagoretencion, p_usuariocreacion, fechahoy, p_ipcreacion, p_usuariocreacion, fechahoy, p_ipcreacion);

if p_idmoneda <> v_monedaservicio then
    select negocio.fn_registrartransacciontipocambio(p_idmoneda,p_montopago,v_tipocambio,v_monedaservicio,v_montoaplicar,p_usuariocreacion,p_ipcreacion) into v_registrotranstc;
end if;

montosaldofinal = montosaldo - v_montoaplicar;

maxidss = nextval('negocio.seq_salsoservicio');
INSERT INTO negocio."SaldosServicio"(
            idsaldoservicio, idservicio, idpago, fechaservicio, montototalservicio, 
            montosaldoservicio, idtransaccionreferencia, usuariocreacion, fechacreacion, ipcreacion, 
            usuariomodificacion, fechamodificacion, ipmodificacion)
    VALUES (maxidss, p_idservicio, maxid, fechaservicio, montoservicio, 
            montosaldofinal, v_registrotranstc, p_usuariocreacion, fechahoy, p_ipcreacion, 
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


-- Function: negocio.fn_registrarpagoobligacion(integer, integer, integer, integer, integer, integer, character varying, character varying, date, character varying, numeric, bytea, character varying, character varying, character varying, character varying, boolean, boolean, integer, character varying, character varying)

--DROP FUNCTION negocio.fn_registrarpagoobligacion(integer, integer, integer, integer, integer, integer, character varying, character varying, date, character varying, numeric, bytea, character varying, character varying, character varying, character varying, boolean, boolean, integer, character varying, character varying);

CREATE OR REPLACE FUNCTION negocio.fn_registrarpagoobligacion(p_idobligacion integer, p_idformapago integer, p_idcuentaorigen integer, p_idcuentadestino integer, 
p_idbancotarjeta integer, p_idtipotarjeta integer, p_nombretitular character varying, p_numerotarjeta character varying, p_fechapago date, 
p_numerooperacion character varying, p_montopago numeric, p_idmoneda integer,
p_sustentopago bytea, p_nombrearchivo character varying, p_extensionarchivo character varying, 
p_tipocontenido character varying, p_comentario character varying, p_espagodetraccion boolean, p_espagoretencion boolean, 
p_usuarioautoriza integer, p_usuariocreacion character varying, p_ipcreacion character varying)
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
            idtipotarjeta, nombretitular, numerotarjeta, fechapago, numerooperacion, montopagado, idmoneda,
            sustentopago, tipocontenido, nombrearchivo, extensionarchivo, 
            comentario, espagodetraccion, espagoretencion, usuariocreacion, 
            fechacreacion, ipcreacion, usuariomodificacion, fechamodificacion, 
            ipmodificacion)
    VALUES (maxid, p_idobligacion, p_idformapago, p_idcuentaorigen, p_idcuentadestino, p_idbancotarjeta, 
            p_idtipotarjeta, p_nombretitular, p_numerotarjeta, p_fechapago, p_numerooperacion, p_montopago, p_idmoneda,
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
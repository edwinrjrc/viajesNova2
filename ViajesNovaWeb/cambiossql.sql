-- Function: negocio.fn_registrarpagoservicio(integer, integer, integer, integer, integer, character varying, character varying, date, numeric, bytea, character varying, character varying, character varying, character varying, boolean, boolean, character varying, character varying)

-- DROP FUNCTION negocio.fn_registrarpagoservicio(integer, integer, integer, integer, integer, character varying, character varying, date, numeric, bytea, character varying, character varying, character varying, character varying, boolean, boolean, character varying, character varying);

CREATE OR REPLACE FUNCTION negocio.fn_registrarpagoservicio(p_idservicio integer, p_idformapago integer, p_idcuentadestino integer, p_idbancotarjeta integer, p_idtipotarjeta integer, p_nombretitular character varying, p_numerotarjeta character varying, p_fechapago date, p_montopago numeric, p_sustentopago bytea, p_nombrearchivo character varying, p_extensionarchivo character varying, p_tipocontenido character varying, p_comentario character varying, p_espagodetraccion boolean, p_espagoretencion boolean, p_usuariocreacion character varying, p_ipcreacion character varying)
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
            nombretitular, numerotarjeta, fechapago, montopagado, sustentopago, 
            tipocontenido, nombrearchivo, extensionarchivo, comentario, espagodetraccion, 
            espagoretencion, usuariocreacion, fechacreacion, ipcreacion, usuariomodificacion, fechamodificacion, ipmodificacion)
    VALUES (maxid, p_idservicio, p_idformapago, p_idcuentadestino, p_idbancotarjeta, p_idtipotarjeta, 
            p_nombretitular, p_numerotarjeta, p_fechapago, p_montopago, p_sustentopago, 
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

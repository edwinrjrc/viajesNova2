
-- Function: negocio.fn_registrarpagoobligacion(integer, integer, integer, integer, integer, character varying, character varying, date, numeric, bytea, character varying, character varying, character varying, character varying, boolean, boolean, character varying, character varying)

-- DROP FUNCTION negocio.fn_registrarpagoobligacion(integer, integer, integer, integer, integer, character varying, character varying, date, numeric, bytea, character varying, character varying, character varying, character varying, boolean, boolean, character varying, character varying);

-- Function: negocio.fn_registrarpagoobligacion(integer, integer, integer, integer, integer, character varying, character varying, date, numeric, bytea, character varying, character varying, character varying, character varying, boolean, boolean, character varying, character varying)

CREATE OR REPLACE FUNCTION negocio.fn_registrarpagoobligacion(p_idobligacion integer, p_idformapago integer, p_idcuentadestino integer, 
p_idbancotarjeta integer, p_idtipotarjeta integer, p_nombretitular character varying, p_numerotarjeta character varying, 
p_fechapago date, p_montopago numeric, p_sustentopago bytea, p_nombrearchivo character varying, p_extensionarchivo character varying, 
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
end if;

INSERT INTO negocio."PagosObligacion"(
            idpago, idobligacion, idformapago, idcuentadestino, idbancotarjeta, 
            idtipotarjeta, nombretitular, numerotarjeta, fechapago, montopagado, 
            sustentopago, tipocontenido, nombrearchivo, extensionarchivo, 
            comentario, espagodetraccion, espagoretencion, usuariocreacion, 
            fechacreacion, ipcreacion, usuariomodificacion, fechamodificacion, 
            ipmodificacion)
    VALUES (maxid, p_idobligacion, p_idformapago, p_idcuentadestino, p_idbancotarjeta, 
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

select negocio.fn_registrarmovimientocuenta(p_idcuentadestino, v_tipomovimiento, v_tipotransaccion, v_desctransaccion, p_montopago, 
p_usuarioautoriza, null, p_usuariocreacion, p_ipcreacion) into v_registramovimiento;

return maxid;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;


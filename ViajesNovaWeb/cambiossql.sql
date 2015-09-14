-- Function: negocio.fn_actualizarcuentabancaria(integer, character varying, character varying, integer, integer, character varying, character varying)

-- DROP FUNCTION negocio.fn_actualizarcuentabancaria(integer, character varying, character varying, integer, integer, character varying, character varying);

CREATE OR REPLACE FUNCTION negocio.fn_actualizarproveedorcuentabancaria(p_idcuenta integer, p_idproveedor integer,
 p_nombrecuenta character varying, p_numerocuenta character varying, p_idtipocuenta integer, p_idbanco integer, 
 p_usuariomodificacion character varying, p_ipmodificacion character varying)
  RETURNS boolean AS
$BODY$

declare maxid integer;
declare v_pagosACuenta integer;
declare fechahoy timestamp with time zone;

begin

maxid = nextval('negocio.seq_cuentabancaria');
select current_timestamp AT TIME ZONE 'PET' into fechahoy;

SELECT count(1)
  INTO v_pagosACuenta
  FROM negocio."PagosObligacion" po
 INNER JOIN negocio."ObligacionesXPagar" ON oxp.id = po.idobligacion
 WHERE oxp.idproveedor    = p_idproveedor
   AND po.idcuentadestino = p_idcuenta;

IF v_pagosACuenta = 0 THEN
	UPDATE negocio."CuentaBancaria"
	   SET nombrecuenta        = p_nombrecuenta, 
	       numerocuenta        = p_numerocuenta,
	       idtipocuenta        = p_idtipocuenta,
	       idbanco             = p_idbanco, 
	       usuariomodificacion = p_usuariomodificacion, 
	       fechamodificacion   = fechahoy, 
	       ipmodificacion      = p_ipmodificacion
	 WHERE id                  = p_idcuenta;

ELSE
	UPDATE negocio."CuentaBancaria"
	   SET nombrecuenta        = p_nombrecuenta,
	       usuariomodificacion = p_usuariomodificacion, 
	       fechamodificacion   = fechahoy, 
	       ipmodificacion      = p_ipmodificacion
	 WHERE id                  = p_idcuenta;

END IF;


return true;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

CREATE OR REPLACE FUNCTION negocio.fn_listarcuentasbancariascombo()
  RETURNS refcursor AS
$BODY$

declare micursor refcursor;

begin

open micursor for
SELECT cb.id, cb.nombrecuenta
  FROM negocio."CuentaBancaria" cb
 WHERE idestadoregistro = 1;

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
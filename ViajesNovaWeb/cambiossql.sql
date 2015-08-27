-- Function: negocio.fn_listarcuentasbancarias()

-- DROP FUNCTION negocio.fn_listarcuentasbancarias();

CREATE OR REPLACE FUNCTION negocio.fn_listarcuentasbancarias()
  RETURNS refcursor AS
$BODY$

declare micursor refcursor;

begin

open micursor for
SELECT cb.id, cb.nombrecuenta, cb.numerocuenta, cb.idtipocuenta, tmtc.nombre as nombretipocuenta, cb.idbanco, tmba.nombre as nombrebanco, 
       cb.idmoneda, tmmo.nombre as nombremoneda, tmmo.abreviatura, cb.saldocuenta, cb.usuariocreacion, 
       cb.fechacreacion, cb.ipcreacion, cb.usuariomodificacion, cb.fechamodificacion, cb.ipmodificacion
  FROM negocio."CuentaBancaria" cb,
       soporte."Tablamaestra" tmtc,
       soporte."Tablamaestra" tmba,
       soporte."Tablamaestra" tmmo
 WHERE idestadoregistro = 1
   AND tmtc.idmaestro   = 21
   AND cb.idtipocuenta  = tmtc.id
   AND tmba.idmaestro   = 19
   AND cb.idbanco       = tmtc.id
   AND tmmo.idmaestro   = 20
   AND cb.idmoneda      = tmmo.id;

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

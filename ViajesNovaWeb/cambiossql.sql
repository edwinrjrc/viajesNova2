
CREATE TABLE negocio."CuentaBancaria"
(
  id integer NOT NULL,
  nombrecuenta character varying(40) NOT NULL,
  numerocuenta character varying(20) NOT NULL,
  idtipocuenta integer not null,
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

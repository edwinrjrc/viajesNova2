CREATE TABLE negocio."RutaServicio"
(
  id integer NOT NULL,
  idtramo integer NOT NULL,
  idaerolinea integer not null,
  usuariocreacion character varying(20) NOT NULL,
  fechacreacion timestamp with time zone NOT NULL,
  ipcreacion character(15) NOT NULL,
  usuariomodificacion character varying(15) NOT NULL,
  fechamodificacion timestamp with time zone NOT NULL,
  ipmodificacion character(15) NOT NULL,
  idestadoregistro integer NOT NULL DEFAULT 1,
  CONSTRAINT pk_rutaservicio PRIMARY KEY (id, idtramo)
)
WITH (
  OIDS=FALSE
);
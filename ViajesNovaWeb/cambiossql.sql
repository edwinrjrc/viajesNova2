-- Table: negocio."Tramo"

CREATE TABLE negocio."Tramo"
(
  id integer NOT NULL,
  idorigen integer NOT NULL,
  descripcionorigen character varying(100) NOT NULL,
  fechasalida timestamp with time zone NOT NULL,
  iddestino integer NOT NULL,
  descripciondestino character varying(100) NOT NULL,
  fechallegada timestamp with time zone NOT NULL,
  preciobase numeric,
  idaerolinea integer NOT NULL,
  usuariocreacion character varying(20) NOT NULL,
  fechacreacion timestamp with time zone NOT NULL,
  ipcreacion character(15) NOT NULL,
  usuariomodificacion character varying(15) NOT NULL,
  fechamodificacion timestamp with time zone NOT NULL,
  ipmodificacion character(15) NOT NULL,
  idestadoregistro integer NOT NULL DEFAULT 1,
  CONSTRAINT pk_tramo PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);


-- Table: negocio."RutaServici

CREATE TABLE negocio."RutaServicio"
(
  id integer NOT NULL,
  idtramo integer NOT NULL,
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
ALTER TABLE negocio."RutaServicio"
  OWNER TO postgres;

  
-- Function: negocio.fn_ingresartramo(integer, character varying, timestamp with time zone, integer, character varying, timestamp with time zone, numeric, character varying, character varying)

-- DROP FUNCTION negocio.fn_ingresartramo(integer, character varying, timestamp with time zone, integer, character varying, timestamp with time zone, numeric, character varying, character varying);

CREATE OR REPLACE FUNCTION negocio.fn_ingresartramo(p_idorigen integer, p_descripcionorigen character varying, p_fechasalida timestamp with time zone, p_iddestino integer, 
p_descripciondestino character varying, p_fechallegada timestamp with time zone, p_preciobase numeric, p_idaerolinea integer,
p_usuariocreacion character varying, p_ipcreacion character varying)
  RETURNS integer AS
$BODY$

declare maxid integer;
declare fechahoy timestamp with time zone;

Begin

maxid = nextval('negocio.seq_tramo');
select current_timestamp AT TIME ZONE 'PET' into fechahoy;

INSERT INTO negocio."Tramo"(
            id, idorigen, descripcionorigen, fechasalida, iddestino, descripciondestino, 
            fechallegada, preciobase, idaerolinea, usuariocreacion, fechacreacion, ipcreacion, 
            usuariomodificacion, fechamodificacion, ipmodificacion)
    VALUES (maxid, p_idorigen, p_descripcionorigen, p_fechasalida, p_iddestino, p_descripciondestino, 
            p_fechallegada, p_preciobase, p_idaerolinea, p_usuariocreacion, fechahoy, p_ipcreacion, 
            p_usuariocreacion, fechahoy, p_ipcreacion);

return maxid;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
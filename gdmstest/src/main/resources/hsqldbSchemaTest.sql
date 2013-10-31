DROP TABLE IF EXISTS "gis_schema"."schema_test";
CREATE SCHEMA gis_schema AUTHORIZATION DBA;
CREATE TABLE "gis_schema"."schema_test" (
    "f1" integer primary key,
    "f2" float,
    "f5" varchar,
    "f7" varchar_ignorecase,
    "f8" char(8),
    "f9" longvarchar,
    "f12" date,
    "f13" time,
    "f15" timestamp,
    "f16" decimal,
    "f17" numeric,
    "f18" boolean,
    "f19" tinyint,
    "f20" smallint,
    "f21" bigint,
    "f25" real,
    "f26" binary,
    "f27" varbinary,
    "f28" longvarbinary);

CREATE OR REPLACE FUNCTION tblUpdate()
  RETURNS trigger AS $updated$
BEGIN
  execute format('CREATE TABLE IF NOT EXISTS public.%s( ser%s SERIAL NOT NULL);',new.tblidentifiers, new.tblidentifiers);
  execute format('ALTER TABLE %s ADD COLUMN %s %s NULL;',new.tblidentifiers,new.valcolumns,new.valvalues);
RETURN NULL;
end;
$updated$ LANGUAGE plpgsql;


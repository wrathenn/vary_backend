insert into public.versions(code, description, created_ts)
  values ('1.0.0', 'Initial Version', now());

insert into public.application(current_version_code, dev_version_code)
  values ('1.0.0', null);
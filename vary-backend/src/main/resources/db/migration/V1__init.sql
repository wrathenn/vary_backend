create extension if not exists pgcrypto;

create table public.versions
(
    code        text                     not null primary key,
    description text,
    created_ts  timestamp with time zone not null default now()
);

create table public.application
(
    id              int  not null primary key default 0,
    current_version text not null references public.versions,
    dev_version     text references public.versions (code),
    constraint singleton check (id = 0)
);

create table public.categories
(
    id           uuid primary key not null,
    name         text             not null,
    description  text default null,
    difficulty   int              not null,
    version_code text references public.versions (code)
);

create table public.cards
(
    id          uuid primary key not null,
    name        text             not null,
    category_id uuid references public.categories (id)
);

create table public.card_stats
(
    id           uuid primary key not null,
    time         smallint,
    penalty_type int              not null default 0,
    is_skipped   bool             not null,
    card_id      uuid references public.cards (id)
);

create table public.users
(
    nickname        text     not null primary key,
    password        text     not null,
    privilege_level smallint not null default 0
);

alter table public.application rename column current_version TO current_version_code;
alter table public.application rename column dev_version TO dev_version_code;

alter table public.cards add constraint unique_names_in_category unique(name, category_id) deferrable initially immediate;


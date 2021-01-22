ALTER TABLE user_accounts
    ADD role varchar;

create unique index user_accounts_email_uindex
	on user_accounts (email);

create unique index user_accounts_username_uindex
	on user_accounts (username);

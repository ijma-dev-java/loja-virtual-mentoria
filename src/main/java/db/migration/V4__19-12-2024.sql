create table acesso_end_point(nome character varying, qtd integer);

insert into acesso_end_point(nome, qtd) values ('END-POINT-NOME-PESSOA-JURIDICA', 0);

alter table acesso_end_point add constraint nome_end_point_unique UNIQUE (nome);
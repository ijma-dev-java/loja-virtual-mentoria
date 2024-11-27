create table acesso_end_point(nome character varying, qtd integer);

insert into acesso_end_point(nome, qtd) values ('buscarNomeCadastradoPf', 0);

alter table acesso_end_point add constraint nome_unique unique (nome);
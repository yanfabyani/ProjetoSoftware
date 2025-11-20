# Análise do Erro - Aplicação Spring Boot

## Erro Principal

```
Access denied for user 'root'@'localhost' (using password: YES)
```

## Causa Raiz

O erro ocorre porque:
1. **Senha incorreta no `application.yaml`**: A senha configurada (`12345`) não corresponde à senha real do MySQL
2. **Configuração hardcoded**: As credenciais estão fixas no arquivo, não usando variáveis de ambiente

## Análise do Log (Linhas 358-691)

### Sequência de Eventos:

1. **Linha 369-372**: Spring Boot inicia normalmente
   - Aplicação iniciada com sucesso
   - Repositórios JPA encontrados (10 interfaces)

2. **Linha 373-377**: Tomcat inicializado
   - Servidor web iniciado na porta 8080

3. **Linha 378-382**: Hibernate tenta conectar
   - Hibernate ORM versão 6.6.33.Final
   - HikariCP pool iniciando

4. **Linha 383-385**: **ERRO DE AUTENTICAÇÃO**
   - SQL Error 1045: Access denied
   - Tentativa de conexão com `root@localhost` falhou

5. **Linha 387-444**: Stack trace completo
   - Hibernate não consegue obter conexão JDBC
   - EntityManagerFactory não pode ser criado

6. **Linha 446**: Aviso sobre MySQLDialect
   - O dialeto não precisa ser especificado explicitamente

7. **Linha 447-454**: Informações do banco (parciais)
   - MySQL versão 8.0 detectada
   - Mas conexão não estabelecida

8. **Linha 456-535**: Segunda tentativa de conexão
   - Mesmo erro de autenticação
   - Aplicação falha ao iniciar

## Soluções

### Solução 1: Corrigir Senha no application.yaml
Atualizar a senha para a senha real do MySQL.

### Solução 2: Usar Variáveis de Ambiente (Recomendado)
Configurar o `application.yaml` para usar variáveis de ambiente, evitando expor credenciais no código.

### Solução 3: Verificar Permissões do Usuário
Garantir que o usuário `root` tem permissões adequadas.

## Status do Código

✅ **Correto**: A entidade `Pessoa` já está usando `@Table(name = "candidato")`
✅ **Correto**: O banco de dados foi criado com a tabela `candidato`
⚠️ **Atenção**: As credenciais precisam ser corrigidas

## Próximos Passos

1. Verificar/corrigir a senha do MySQL
2. Configurar variáveis de ambiente
3. Testar conexão manualmente
4. Executar a aplicação novamente


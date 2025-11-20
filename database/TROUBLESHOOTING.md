# Troubleshooting - Erros de Conexão MySQL

## Análise do Erro

### Erro Principal
```
Access denied for user 'root'@'localhost' (using password: YES)
```

Este erro indica que:
1. A senha do MySQL no `application.yaml` está incorreta
2. O usuário `root` não existe ou não tem permissões
3. O MySQL não está rodando

### Erro Secundário (Corrigido)
A entidade `Pessoa.java` estava referenciando a tabela `pessoa`, mas o banco de dados foi alterado para usar `candidato`. **Já foi corrigido!**

## Soluções

### 1. Verificar se o MySQL está rodando

```bash
sudo systemctl status mysql
# ou
sudo systemctl status mariadb
```

Se não estiver rodando:
```bash
sudo systemctl start mysql
# ou
sudo systemctl start mariadb
```

### 2. Verificar/Criar o usuário e senha

Conecte-se ao MySQL:
```bash
sudo mysql -u root
```

Ou se tiver senha:
```bash
mysql -u root -p
```

Depois execute:
```sql
-- Verificar usuários existentes
SELECT user, host FROM mysql.user;

-- Se necessário, criar/atualizar usuário root
ALTER USER 'root'@'localhost' IDENTIFIED BY '12345';
FLUSH PRIVILEGES;

-- Ou criar um novo usuário específico para a aplicação
CREATE USER 'app_user'@'localhost' IDENTIFIED BY '12345';
GRANT ALL PRIVILEGES ON rede_mais_social.* TO 'app_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Atualizar application.yaml (se necessário)

Se você criou um novo usuário, atualize o `application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/rede_mais_social?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: app_user  # ou root
    password: 12345     # sua senha real
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 4. Verificar se o banco de dados existe

```sql
SHOW DATABASES;
```

Se `rede_mais_social` não existir, execute o script de criação:
```bash
mysql -u root -p < database/create_database.sql
```

### 5. Testar conexão manualmente

```bash
mysql -u root -p12345 -e "USE rede_mais_social; SHOW TABLES;"
```

Se funcionar, o problema está na aplicação. Se não funcionar, o problema é de autenticação.

## Checklist de Verificação

- [ ] MySQL está rodando
- [ ] Usuário e senha estão corretos no `application.yaml`
- [ ] Banco de dados `rede_mais_social` existe
- [ ] Usuário tem permissões no banco
- [ ] Tabela `candidato` existe (não `pessoa`)
- [ ] Entidade `Pessoa.java` está usando `@Table(name = "candidato")` ✅ (já corrigido)

## Comandos Úteis

### Resetar senha do root (se esquecer)
```bash
sudo systemctl stop mysql
sudo mysqld_safe --skip-grant-tables &
mysql -u root
```

No MySQL:
```sql
USE mysql;
UPDATE user SET authentication_string=PASSWORD('12345') WHERE User='root';
FLUSH PRIVILEGES;
exit;
```

Depois reinicie o MySQL:
```bash
sudo systemctl restart mysql
```

### Verificar permissões do usuário
```sql
SHOW GRANTS FOR 'root'@'localhost';
```

### Criar banco manualmente (se necessário)
```sql
CREATE DATABASE IF NOT EXISTS rede_mais_social
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
```


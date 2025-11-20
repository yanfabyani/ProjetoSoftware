# Scripts de Banco de Dados - Rede Mais Social

Este diretório contém os scripts necessários para criar e configurar o banco de dados MySQL do projeto.

## Arquivos

- `create_database.sql` - Script SQL completo para criação do banco e todas as tabelas
- `setup_database.sh` - Script shell interativo para executar a criação do banco

## Estrutura do Banco de Dados

O banco de dados `rede_mais_social` é composto pelas seguintes tabelas:

### Tabelas Principais

- **pessoa** - Tabela base para herança (JOINED strategy)
- **ong** - Dados específicos de ONGs
- **voluntario_pf** - Dados de voluntários pessoa física
- **voluntario_pj** - Dados de voluntários pessoa jurídica

### Tabelas de Relacionamento 1:1

- **conta_acesso** - Credenciais de acesso ao sistema
- **afiliacao** - Solicitações e status de afiliação
- **termo_compromisso** - Termos de compromisso aceitos
- **perfil** - Perfis dos usuários

### Tabelas de Relacionamento 1:N

- **email_validacao** - Tokens de validação de email
- **certidao** - Certidões das organizações

### Tabelas de Catálogo

- **habilidade** - Catálogo de habilidades
- **interesse** - Catálogo de interesses

### Tabelas de Relacionamento N:N

- **perfil_habilidade** - Relacionamento entre perfis e habilidades
- **perfil_interesse** - Relacionamento entre perfis e interesses

## Como Usar

### Opção 1: Usando o Script Shell (Recomendado)

```bash
cd database
./setup_database.sh
```

O script irá:
1. Solicitar as credenciais do MySQL
2. Executar automaticamente o script SQL
3. Criar o banco de dados e todas as tabelas

### Opção 2: Executando o SQL Diretamente

```bash
mysql -u root -p < database/create_database.sql
```

Ou dentro do MySQL:

```sql
mysql -u root -p
source database/create_database.sql;
```

### Opção 3: Executando Manualmente no MySQL

1. Conecte-se ao MySQL:
   ```bash
   mysql -u root -p
   ```

2. Execute o conteúdo do arquivo `create_database.sql`

## Características do Banco

- ✅ **Normalizado** - Segue as regras de normalização (1NF, 2NF, 3NF)
- ✅ **Índices** - Índices criados em colunas frequentemente consultadas
- ✅ **Chaves Estrangeiras** - Integridade referencial garantida
- ✅ **Charset UTF-8** - Suporte completo a caracteres especiais
- ✅ **Cascade Delete** - Exclusão em cascata para manter integridade
- ✅ **Herança JOINED** - Estratégia de herança implementada corretamente

## Configuração da Aplicação

Após criar o banco de dados, certifique-se de que o arquivo `application.yaml` está configurado corretamente:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/rede_mais_social?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root
    password: sua_senha_aqui
    driver-class-name: com.mysql.cj.jdbc.Driver
```

## Verificação

Para verificar se o banco foi criado corretamente:

```sql
USE rede_mais_social;
SHOW TABLES;
DESCRIBE pessoa;
```

## Notas Importantes

⚠️ **ATENÇÃO**: O script `create_database.sql` contém um comando `DROP DATABASE IF EXISTS`, que **apagará todos os dados** se o banco já existir. Use com cuidado em ambientes de produção!

Para desenvolvimento, isso é útil pois permite recriar o banco do zero sempre que necessário.

## Troubleshooting

### Erro: "Access denied"
- Verifique se o usuário MySQL tem permissões para criar bancos de dados
- Tente executar: `GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost';`

### Erro: "Unknown database"
- Certifique-se de que o MySQL está rodando: `sudo systemctl status mysql`

### Erro: "Table already exists"
- O banco já existe. Use `DROP DATABASE IF EXISTS rede_mais_social;` antes de executar o script novamente


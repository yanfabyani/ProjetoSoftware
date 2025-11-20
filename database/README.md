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


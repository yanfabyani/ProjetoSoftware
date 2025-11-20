# Guia de Configuração - Rede Mais Social

## 📋 Resumo do Problema

O erro `Access denied for user 'root'@'localhost'` ocorre porque:
- A senha no `application.yaml` não corresponde à senha real do MySQL
- As credenciais estavam hardcoded no arquivo de configuração

## ✅ Solução Implementada

1. **Refatorado `application.yaml`** para usar variáveis de ambiente
2. **Criado script `config.sh`** interativo para configurar variáveis
3. **Atualizado `config-example.sh`** com instruções claras

## 🚀 Como Usar

### Opção 1: Script Interativo (Recomendado)

```bash
# Execute o script interativo
./config.sh

# Depois execute a aplicação
mvn spring-boot:run
# ou
./mvnw spring-boot:run
```

### Opção 2: Configuração Manual

```bash
# 1. Copie o arquivo de exemplo
cp config-example.sh config.sh

# 2. Edite config.sh com suas credenciais reais
nano config.sh  # ou use seu editor preferido

# 3. Carregue as variáveis
source config.sh

# 4. Execute a aplicação
mvn spring-boot:run
```

### Opção 3: Exportar Manualmente

```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=rede_mais_social
export DB_USERNAME=root
export DB_PASSWORD=sua_senha_real_aqui

# Depois execute a aplicação
mvn spring-boot:run
```

## 🔧 Verificação

### 1. Testar Conexão MySQL

```bash
mysql -u root -p
# Digite sua senha quando solicitado
```

### 2. Verificar se o Banco Existe

```sql
SHOW DATABASES LIKE 'rede_mais_social';
USE rede_mais_social;
SHOW TABLES;
```

### 3. Verificar Tabela Candidato

```sql
DESCRIBE candidato;
```

## 📝 Estrutura de Arquivos

```
ProjetoSoftware/
├── config.sh              # Script interativo (criar com ./config.sh)
├── config-example.sh      # Exemplo de configuração
├── src/main/resources/
│   └── application.yaml   # Agora usa variáveis de ambiente
└── database/
    ├── create_database.sql
    └── setup_database.sh
```

## ⚙️ Variáveis de Ambiente

| Variável | Padrão | Descrição |
|----------|--------|-----------|
| `DB_HOST` | `localhost` | Host do MySQL |
| `DB_PORT` | `3306` | Porta do MySQL |
| `DB_NAME` | `rede_mais_social` | Nome do banco de dados |
| `DB_USERNAME` | `root` | Usuário do MySQL |
| `DB_PASSWORD` | `12345` | **Senha do MySQL (ALTERAR!)** |
| `MAIL_USERNAME` | - | Email para envio (opcional) |
| `MAIL_PASSWORD` | - | Senha do app Gmail (opcional) |

## 🔒 Segurança

⚠️ **IMPORTANTE**: 
- Nunca commite o arquivo `config.sh` com credenciais reais
- O arquivo `config.sh` está no `.gitignore`
- Use `config-example.sh` como template

## 🐛 Troubleshooting

### Erro: "Access denied"
- Verifique se a senha está correta
- Teste a conexão manualmente: `mysql -u root -p`
- Verifique se o usuário tem permissões

### Erro: "Unknown database"
- Execute o script de criação: `./database/setup_database.sh`
- Ou manualmente: `mysql -u root -p < database/create_database.sql`

### Variáveis não carregadas
- Certifique-se de usar `source config.sh` (não apenas `./config.sh`)
- Ou adicione ao `~/.bashrc` ou `~/.profile`

## 📚 Próximos Passos

1. ✅ Configure as variáveis de ambiente
2. ✅ Teste a conexão com o MySQL
3. ✅ Execute o script de criação do banco (se necessário)
4. ✅ Execute a aplicação Spring Boot
5. ✅ Verifique os logs para confirmar conexão bem-sucedida


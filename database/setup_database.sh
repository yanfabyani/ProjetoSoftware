#!/bin/bash

# =====================================================
# Script de Configuração do Banco de Dados
# Rede Mais Social - Projeto Software
# =====================================================

# Cores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}====================================================="
echo "  Configuração do Banco de Dados MySQL"
echo "  Rede Mais Social"
echo "=====================================================${NC}"
echo ""

# Verifica se o MySQL está instalado
if ! command -v mysql &> /dev/null; then
    echo -e "${RED}Erro: MySQL não está instalado ou não está no PATH${NC}"
    exit 1
fi

# Solicita credenciais do MySQL
read -p "Usuário MySQL [root]: " DB_USER
DB_USER=${DB_USER:-root}

read -sp "Senha MySQL: " DB_PASS
echo ""

# Define o nome do banco de dados
DB_NAME="rede_mais_social"
SQL_FILE="$(dirname "$0")/create_database.sql"

# Verifica se o arquivo SQL existe
if [ ! -f "$SQL_FILE" ]; then
    echo -e "${RED}Erro: Arquivo $SQL_FILE não encontrado${NC}"
    exit 1
fi

echo ""
echo -e "${YELLOW}Criando banco de dados e tabelas...${NC}"

# Executa o script SQL
if [ -z "$DB_PASS" ]; then
    mysql -u "$DB_USER" < "$SQL_FILE"
else
    mysql -u "$DB_USER" -p"$DB_PASS" < "$SQL_FILE"
fi

# Verifica se houve erro
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Banco de dados criado com sucesso!${NC}"
    echo ""
    echo -e "${GREEN}Configurações:${NC}"
    echo "  - Banco: $DB_NAME"
    echo "  - Usuário: $DB_USER"
    echo ""
    echo -e "${YELLOW}Próximos passos:${NC}"
    echo "  1. Verifique se as credenciais no application.yaml estão corretas"
    echo "  2. Execute a aplicação Spring Boot"
    echo ""
else
    echo -e "${RED}✗ Erro ao criar banco de dados${NC}"
    exit 1
fi


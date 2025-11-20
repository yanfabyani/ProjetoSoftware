#!/bin/bash

# =====================================================
# Script - Configuração de Variáveis
# Rede Mais Social - Projeto Software
# =====================================================

# Configurações do Banco de Dados MySQL
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=rede_mais_social
export DB_USERNAME=root
export DB_PASSWORD=12345

# Configurações de E-mail (opcional)
export MAIL_USERNAME=seu_email@gmail.com
export MAIL_PASSWORD=sua_senha_app_aqui

echo "Variáveis de ambiente configuradas!"
echo "DB_HOST: $DB_HOST"
echo "DB_PORT: $DB_PORT"
echo "DB_NAME: $DB_NAME"
echo "DB_USERNAME: $DB_USERNAME"
echo "DB_PASSWORD: ${DB_PASSWORD:+***}"


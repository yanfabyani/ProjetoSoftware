#!/bin/bash

# =====================================================
# Script de Exemplo - Configuração de Variáveis
# Rede Mais Social - Projeto Software
# =====================================================
# 
# INSTRUÇÕES:
# 1. Copie este arquivo: cp config-example.sh config.sh
# 2. Edite config.sh com suas credenciais reais
# 3. Execute: source config.sh
# 4. Execute a aplicação Spring Boot
#
# OU use o script interativo: ./config.sh
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


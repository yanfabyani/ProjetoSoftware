#!/bin/bash

# =====================================================
# Script de execução conveniente
# - Carrega as variáveis de ambiente (config.sh)
# - Executa a aplicação Spring Boot com ./mvnw
# =====================================================

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_FILE="$ROOT_DIR/config.sh"
EXAMPLE_FILE="$ROOT_DIR/config-example.sh"

if [ -f "$CONFIG_FILE" ]; then
  echo "[run.sh] Carregando variáveis de $CONFIG_FILE"
  # shellcheck disable=SC1090
  source "$CONFIG_FILE"
elif [ -f "$EXAMPLE_FILE" ]; then
  echo "[run.sh] Aviso: config.sh não encontrado. Usando config-example.sh (não recomendado)."
  echo "[run.sh] Copie config-example.sh para config.sh e personalize antes de continuar."
  # shellcheck disable=SC1090
  source "$EXAMPLE_FILE"
else
  echo "[run.sh] Nenhum arquivo de configuração encontrado."
  echo "[run.sh] Crie o arquivo config.sh (você pode copiar config-example.sh) e tente novamente."
  exit 1
fi

echo "[run.sh] Iniciando aplicação..."
cd "$ROOT_DIR"
./mvnw spring-boot:run "$@"


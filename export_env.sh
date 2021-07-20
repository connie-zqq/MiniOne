#!/usr/bin/env bash
# Usage: source export_env.sh
SCRIPT_DIR=$(dirname "$0")
ENV_FILE="${SCRIPT_DIR}/src/main/resources/.env"
echo "Exporting vars from ${ENV_FILE} file"
export $(cat "${ENV_FILE}" | xargs)

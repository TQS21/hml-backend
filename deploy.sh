#!/bin/bash

MODE=""
while getopts 'dp' opt; do
    case "$opt" in

    d)
        MODE="dev"
        ;;
    p)
        MODE="prod"
        ;;
    t)
        MODE="test"
        ;;
    esac
done

echo "hml_$MODE"
if [ -z "$MODE" ]; then
    exit 1
fi

PROJECT="hml_$MODE"

compose="docker-compose.${MODE}.yml"
docker-compose -p $PROJECT -f $compose down -v && docker-compose -p $PROJECT -f $compose up -d --build 
#!/bin/bash
GREEN='\n\033[1;32;40m'
NC='\033[0m' # No Color

echo -e "${GREEN}[commitlint]${NC}"

./gradlew commitlint

RESULT=$?
[ $RESULT -ne 0 ] && exit 1

echo -e "${GREEN}[Run ktlint]${NC}"

./gradlew ktlint

RESULT=$?
[ $RESULT -ne 0 ] && exit 1

echo -e "${GREEN}[Run test]${NC}"

./gradlew test

RESULT=$?
[ $RESULT -ne 0 ] && exit 1
exit 0
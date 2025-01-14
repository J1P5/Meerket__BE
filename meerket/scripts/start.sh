#!/usr/bin/env bash

# .env 파일 로드
if [ -f /home/ubuntu/project/.env ]; then
  export $(cat /home/ubuntu/project/.env | xargs)
else
  echo ".env file not found. Ensure the file exists in /home/ubuntu/project/"
  exit 1
fi

PROJECT_ROOT="/home/ubuntu/project/meerket-application"
JAR_FILE="$PROJECT_ROOT/build/libs/meerket-application-0.0.1.jar"

APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

# jar 파일 실행
echo "$TIME_NOW > $JAR_FILE 파일 실행" >> $DEPLOY_LOG
nohup java -jar $JAR_FILE > $APP_LOG 2> $ERROR_LOG &

CURRENT_PID=$(pgrep -f $JAR_FILE)
echo "$TIME_NOW > 실행된 프로세스 아이디 $CURRENT_PID 입니다." >> $DEPLOY_LOG

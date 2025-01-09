#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/project/meerket-application"
JAR_FILE="$PROJECT_ROOT/build/libs/meerket-application-0.0.1.jar"

APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

# jar 파일 실행
echo "$TIME_NOW > $JAR_FILE 파일 실행" >> $DEPLOY_LOG
nohup java -Dloader.path=BOOT-INF/classes -Dloader.main=org.j1p5.MeerketApplication org.springframework.boot.loader.PropertiesLauncher > $APP_LOG 2> $ERROR_LOG &

CURRENT_PID=$(pgrep -f $JAR_FILE)
echo "$TIME_NOW > 실행된 프로세스 아이디 $CURRENT_PID 입니다." >> $DEPLOY_LOG

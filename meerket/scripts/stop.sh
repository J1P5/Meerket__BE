#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/project/meerket-application"
JAR_FILE="$PROJECT_ROOT/build/libs/meerket-application-0.0.1.jar"

DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

# 현재 구동 중인 애플리케이션 PID 확인
CURRENT_PID=$(pgrep -f $JAR_FILE)

# 프로세스가 켜져 있으면 종료
if [ -z "$CURRENT_PID" ]; then
  echo "$TIME_NOW > 현재 실행중인 애플리케이션이 없습니다" >> $DEPLOY_LOG
else
  echo "$TIME_NOW > 실행중인 $CURRENT_PID 애플리케이션 종료" >> $DEPLOY_LOG
  kill $CURRENT_PID
  sleep 5
  if kill -0 $CURRENT_PID 2>/dev/null; then
    echo "$TIME_NOW > 강제 종료 필요: $CURRENT_PID" >> $DEPLOY_LOG
    kill -9 $CURRENT_PID
  fi
fi

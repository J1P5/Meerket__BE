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

# Java 시스템 프로퍼티로 환경변수 설정
JAVA_OPTS="-Dhome.url=$HOME_URL \
-Dhome.username=$HOME_USERNAME \
-Dhome.password=$HOME_PASSWORD \
-Dkakao.oauth.grant.type=$KAKAO_OAUTH_GRANT_TYPE \
-Dkakao.oauth.client.id=$KAKAO_OAUTH_CLIENT_ID \
-Dkakao.oauth.redirect.uri=$KAKAO_OAUTH_REDIRECT_URI \
-Dnaver.oauth.grant.type=$NAVER_OAUTH_GRANT_TYPE \
-Dnaver.oauth.client.id=$NAVER_OAUTH_CLIENT_ID \
-Dnaver.oauth.client.secret=$NAVER_OAUTH_CLIENT_SECRET \
-Dnaver.oauth.state=$NAVER_OAUTH_STATE \
-Daws.access.key.id=$AWS_ACCESS_KEY_ID \
-Daws.access.key=$AWS_ACCESS_KEY \
-Daws.s3.bucket.name=$AWS_S3_BUCKET_NAME \
-Dmongo.database=$MONGO_DATABASE \
-Dmongo.uri=$MONGO_URI \
-Dredis.host=$REDIS_HOST \
-Dredis.port=$REDIS_PORT"

# jar 파일 실행
echo "$TIME_NOW > $JAR_FILE 파일 실행" >> $DEPLOY_LOG
nohup java $JAVA_OPTS -jar $JAR_FILE > $APP_LOG 2> $ERROR_LOG &

CURRENT_PID=$(pgrep -f $JAR_FILE)
echo "$TIME_NOW > 실행된 프로세스 아이디 $CURRENT_PID 입니다." >> $DEPLOY_LOG

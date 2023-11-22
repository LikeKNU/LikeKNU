#!/bin/bash

ROOT_PATH="/home/ec2-user/deploy"
JAR="$ROOT_PATH/application.jar"
STOP_LOG="$ROOT_PATH/stop.log"
SERVICE_PID=$(pgrep -f $JAR)

if [ -z "$SERVICE_PID" ]; then
  echo "서비스 NotFound" >> $STOP_LOG
else
  echo "서비스 종료 " >> $STOP_LOG
  kill -9 "$SERVICE_PID"
fi
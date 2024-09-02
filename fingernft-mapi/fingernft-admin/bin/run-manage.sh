#!/bin/bash

#SERVER_NAME=fingernft-admin
readonly APP_HOME=${FILE_PATH:-$(dirname $(cd `dirname $0`; pwd))}

#readonly JAVA_HOME=""
readonly MEMORY_SIZE=1024M

#readonly CONFIG_HOME="$APP_HOME/config/"
readonly LIB_HOME="$APP_HOME/lib"
readonly BOOT_HOME="$APP_HOME/boot"
readonly LOGS_HOME="$APP_HOME/logs"
readonly LOG_DATE=`date +%Y%m%d%H%M%S`
readonly GC_LOG=$LOGS_HOME/gc-$LOG_DATE.log

readonly PID_FILE="$LOGS_HOME/application.pid"
readonly APP_MAIN_CLASS="com.fingerchar.admin.AdminApplication"
readonly APPLICATION_CONF="$APP_HOME/config/application.yml"
#readonly LOG_CONFIG="$CONFIG_HOME/config/logback-spring.xml"

readonly JAVA_RUN="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$GC_LOG -Dspring.pid.file=$PID_FILE -Dspring.pid.fail-on-write-error=true"
readonly JAVA_OPTS="-server -Xms$MEMORY_SIZE -Xmx$MEMORY_SIZE -XX:+AggressiveOpts -XX:+UseBiasedLocking -XX:+DisableExplicitGC -XX:MaxTenuringThreshold=15 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -Djava.awt.headless=true -XX:+UnlockCommercialFeatures -XX:+FlightRecorder $JAVA_RUN"

readonly JAVA="java"

PID=0


if [ ! -x "$LOGS_HOME" ]
then
  mkdir $LOGS_HOME
fi
#chmod +x -R "$JAVA_HOME/bin/"

functions="/etc/functions.sh"
if test -f $functions ; then
  . $functions
else
  success()
  {
    echo " SUCCESS! $@"
  }
  failure()
  {
    echo " ERROR! $@"
  }
  warning()
  {
    echo "WARNING! $@"
  }
fi

function checkpid() {
   PID=$(ps -ef | grep $APP_MAIN_CLASS | grep -v 'grep' | awk '{print int($2)}')
    if [[ -n "$PID" ]]
    then
      return 0
    else
      return 1
    fi
}

function start() {
   checkpid
   if [[ $? -eq 0 ]]
   then
      warning "[$APP_MAIN_CLASS]: already started! (PID=$PID)"
   else
      echo -n "[$APP_MAIN_CLASS]: Starting ..."
      JAVA_CMD="nohup $JAVA $JAVA_OPTS -cp $BOOT_HOME/*:$LIB_HOME/* $APP_MAIN_CLASS --logging.file.path=$LOGS_HOME --spring.config.location=$APPLICATION_CONF > /dev/null 2>&1 &"
      # echo "Exec cmmand : $JAVA_CMD"
      sh -c "$JAVA_CMD"
      sleep 3
      checkpid
      if [[ $? -eq 0 ]]
      then
         success "(PID=$PID) "
      else
         failure " "
      fi
   fi
}

function stop() {
   checkpid
   if [[ $? -eq 0 ]];
   then
      echo -n "[$APP_MAIN_CLASS]: Shutting down ...(PID=$PID) "
      kill -9 $PID
      if [[ $? -eq 0 ]];
      then
	     echo 0 > $PID_FILE
         success " "
      else
         failure " "
      fi
   else
      warning "[$APP_MAIN_CLASS]: is not running ..."
   fi
}

function status() {
   checkpid
   if [[ $? -eq 0 ]]
   then
      success "[$APP_MAIN_CLASS]: is running! (PID=$PID)"
      return 0
   else
      failure "[$APP_MAIN_CLASS]: is not running"
      return 1
   fi
}

function info() {
   echo "System Information:"
   echo
   echo "****************************"
   echo `head -n 1 /etc/issue`
   echo `uname -a`
   echo
   echo "JAVA_HOME=$JAVA_HOME"
   echo
   echo "JAVA Environment Information:"
   echo `$JAVA -version`
   echo
   echo "APP_HOME=$APP_HOME"
   echo "APP_MAIN_CLASS=$APP_MAIN_CLASS"
   echo
   echo "****************************"
}

case "$1" in
   'start')
      start
      ;;
   'stop')
     stop
     ;;
   'restart')
     stop
     start
     ;;
   'status')
     status
     ;;
   'info')
     info
     ;;
    *)
     echo "Usage: $0 {help|start|stop|restart|status|info}"
     ;;
esac
exit 0

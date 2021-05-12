mvn package 
if [ $? -ne 0 ]; then
      echo "build  failed"
      exit 0
    else
      echo "build succeed"
  fi
java -jar serv_user/target/serv_user-v1-SNAPSHOT-war-exec.jar

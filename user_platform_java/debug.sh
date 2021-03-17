DEBUG="-Xdebug -Xrunjdwp:transport=dt_socket,address=8899,server=y,suspend=n"
mvn package -U;java ${DEBUG} -jar serv_user/target/serv_user-v1-SNAPSHOT-war-exec.jar

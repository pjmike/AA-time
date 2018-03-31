# 设置要制作的镜像基于哪个镜像，FROM指令必须是整个Dockerfile的第一个指令
FROM openjdk:8-jdk-alpine rabbitmq:management
# ADD指令用于从指定路径拷贝一个文件或目录到容器的指定路径中
VOLUME /tmp
ADD aatime-web-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
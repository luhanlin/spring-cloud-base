FROM java:8
VOLUME /tmp
ADD eureka-1.0.0.RELEASE.jar app.jar
#RUN bash -c 'touch /app.jar'
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV JAVA_OPTS="\
-server \
-Xmx2g \
-Xms2g \
-Xmn1g \
-XX:+HeapDumpOnOutOfMemoryError \
-XX:SurvivorRatio=8 \
-XX:MetaspaceSize=256m \
-XX:MaxMetaspaceSize=512m"
ENTRYPOINT java ${JAVA_OPTS}  -Djava.security.egd=file:/dev/./urandom -jar /app.jar
EXPOSE 8761

## -XX:+HeapDumpOnOutOfMemoryError 内存益处监控参数
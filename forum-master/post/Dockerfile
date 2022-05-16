# 以openjdk:latest为基准
FROM openjdk:latest

# 作者信息
MAINTAINER infinite <969557028@qq.com>

# 将jar包添加到容器中并更名为app.jar
COPY target/post-0.0.1-SNAPSHOT.jar /app.jar

#设置端口
CMD ["--server.port=8080"]
EXPOSE 8080

#设置语言与时区
ENV LANG C.UTF-8
ENV TZ Asia/Shanghai

# 运行jar包
ENTRYPOINT ["java","-jar","/app.jar"]

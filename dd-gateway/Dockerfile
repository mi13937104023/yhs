# 使用官方 OpenJDK 17 精简版作为基础镜像
FROM openjdk:17-jdk-slim

# 设置时区
ENV TZ=Asia/Shanghai

# 创建工作目录
WORKDIR /dd-gateway

# 复制项目打包好的 jar 文件到容器中
COPY ./target/dd-gateway.jar app.jar

# 暴露服务端口
EXPOSE 48080

# 可选：设置默认 JVM 参数（也可通过 docker run 时 -e JAVA_OPTS 设置）
ENV JAVA_OPTS=""

# 使用 CMD 启动程序
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
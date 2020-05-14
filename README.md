# spring-cloud-example
simple example
## Build
```bash
mvn clean package -DskipTests
```

## Docker Build
```bash
docker build -t candyleer/spring-cloud-provider:v1 -f spring-cloud-provider/Dockerfile  ./spring-cloud-provider/
docker build -t candyleer/spring-cloud-eureka-server:v1 -f spring-cloud-eureka-server/Dockerfile  ./spring-cloud-eureka-server/
docker build -t candyleer/spring-cloud-consumer:v1 -f spring-cloud-consumer/Dockerfile  ./spring-cloud-consumer/

```

## docker Push
```bash
docker push candyleer/spring-cloud-provider:v1
docker push candyleer/spring-cloud-eureka-server:v1
docker push candyleer/spring-cloud-consumer:v1
```

## Deploy
```bash
# 部署 eureka-server 一个节点
kubectl apply -f deploy/eureka-server.yaml
# 部署 provider 5个节点
kubectl apply -f deploy/provider.yaml
# 部署 consumer 一个节点
kubectl apply -f deploy/consumer.yaml

访问 http://<consumer pod ip>:8080/hello 测试是否通
```



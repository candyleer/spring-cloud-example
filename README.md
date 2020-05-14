# spring-cloud-example
simple example

## Build
```bash
docker build -t candyleer/spring-cloud-provider:v1 -f spring-cloud-provider/Dockerfile  ./spring-cloud-provider/
docker build -t candyleer/spring-cloud-eureka-server:v1 -f spring-cloud-eureka-server/Dockerfile  ./spring-cloud-eureka-server/
docker build -t candyleer/spring-cloud-consumer:v1 -f spring-cloud-consumer/Dockerfile  ./spring-cloud-consumer/

```

## Push
```bash
docker push candyleer/spring-cloud-provider:v1
docker push candyleer/spring-cloud-eureka-server:v1
docker push candyleer/spring-cloud-consumer:v1
```

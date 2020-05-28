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
## Monitor
```bash
/actuator/prometheus
```

---
## SSL 支持
1. 生成两本证书并信任
```bash
keytool -genkeypair -alias server -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore springcloudserver.p12 -validity 3800
keytool -genkeypair -alias client -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore springcloudclient.p12 -validity 3800
keytool -export -alias server -file springcloudserver.crt --keystore springcloudserver.p12
keytool -export -alias client -file springcloudclient.crt --keystore springcloudclient.p12
keytool -import -alias server -file springcloudserver.crt -keystore springcloudclient.p12
keytool -import -alias client -file springcloudclient.crt -keystore springcloudserver.p12

```

2. provider端添加 ssl 支持
```yaml
server:
  port: 8080
  ssl:
    key-store: classpath:springcloudserver.p12
    key-store-password: 123456
    key-store-type: PKCS12
    key-alias: server
    enabled: true
```

3. consumer 端 yaml 修改
```yaml
#ribbon 支持 https
ribbon:
  IsSecure: true
  
#feigh 客户端需要
ssl:
  key-store: springcloudclient.p12
  key-store-password: 123456
```
4. consumer 端 feign 客户端改造支持 ssl

```java
@Configuration
@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class OkHttpConfig {

    @Autowired
    private OkHttpInterceptor interceptor;

    @Value("${ssl.key-store}")
    String keyStoreFileName;

    @Value("${ssl.key-store-password}")
    String keyStorePassword;


    @Bean
    public OkHttpClient okHttpClient() throws Exception {

        return new OkHttpClient.Builder()
                .sslSocketFactory(createSSLSocketFactory())
                .hostnameVerifier((s, sslSession) -> true)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }


    private SSLSocketFactory createSSLSocketFactory() throws Exception {
        SSLSocketFactory ssfFactory;

        try {
            SSLContext sc = new SSLContextBuilder()
                    .loadTrustMaterial(
                            Thread.currentThread().getContextClassLoader().getResource(keyStoreFileName), keyStorePassword.toCharArray()
                    )
                    .build();
            ssfFactory = sc.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new IllegalStateException(e);
        }
        return ssfFactory;
    }


}
```

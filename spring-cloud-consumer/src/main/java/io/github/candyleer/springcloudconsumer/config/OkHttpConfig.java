package io.github.candyleer.springcloudconsumer.config;

import feign.Feign;
import io.github.candyleer.springcloudconsumer.OkHttpInterceptor;
import okhttp3.OkHttpClient;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

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

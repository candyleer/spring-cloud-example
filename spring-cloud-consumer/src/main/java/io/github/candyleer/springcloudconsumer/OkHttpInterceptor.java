package io.github.candyleer.springcloudconsumer;

import okhttp3.Interceptor;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class OkHttpInterceptor implements Interceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(OkHttpInterceptor.class);

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        LOGGER.info("OkHttpUrl : " + chain.request().url());
        return chain.proceed(chain.request());
    }
}


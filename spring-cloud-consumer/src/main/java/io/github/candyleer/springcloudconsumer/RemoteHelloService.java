package io.github.candyleer.springcloudconsumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient(name = "spring-cloud-provider")
public interface RemoteHelloService {

    @RequestMapping(value = "/hello")
    Map<String, String> hello();
}

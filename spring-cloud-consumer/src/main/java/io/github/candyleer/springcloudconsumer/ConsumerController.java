package io.github.candyleer.springcloudconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ConsumerController {

    @Autowired
    private RemoteHelloService remoteHelloService;

    @RequestMapping("/hello")
    public Map<String, String> index() {
        return remoteHelloService.hello();
    }
}

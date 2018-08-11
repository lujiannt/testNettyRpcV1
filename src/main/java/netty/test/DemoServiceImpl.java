package netty.test;

import netty.core.RpcService;
import org.springframework.stereotype.Service;

@RpcService(DemoService.class)
@Service
public class DemoServiceImpl implements  DemoService {
    @Override
    public String sayHello() {
        return "hello";
    }
}

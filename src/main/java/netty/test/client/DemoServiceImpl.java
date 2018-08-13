package netty.test.client;

import netty.core.RpcService;
import org.springframework.stereotype.Service;

@RpcService(DemoService.class)
@Service
public class DemoServiceImpl implements DemoService {
    @Override
    public String demo(String demo) {
        return demo;
    }
}

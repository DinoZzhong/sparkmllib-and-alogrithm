package dino.test.rpc;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;


/**
 * Created by Dino on 2017/1/6.
 *
 * 实现rpc server
 * 调用RPC.Builder指定实现的接口、反射的实例、对应的地址和端口
 */
public class MyBizServer {
    public static final String SERVER_ADD =ConstantRPC.RPC_ADD;
    public static int SERVER_PORT=ConstantRPC.RPC_PORT;
    public static void main (String args[]) throws IOException {
        Configuration conf = new Configuration();
        RPC.Server server = new RPC.Builder(conf).setProtocol(Mybiz.class)
                .setInstance(new MyBizImpl()).setBindAddress(SERVER_ADD)
                .setPort(SERVER_PORT).build();

        System.out.println("start server...");
        server.start();

    }
}

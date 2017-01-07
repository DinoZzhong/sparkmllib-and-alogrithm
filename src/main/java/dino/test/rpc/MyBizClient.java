package dino.test.rpc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by Dino on 2017/1/6.
 *
 * 客户端测试rpc服务，需要先启动server端
 *
 * 如果需要增加方法，需要在接口层增加方法，然后在对应的服务端实现
 *  注：识别功能本质是否符合抽象规则，是否是属于该服务的一部分，如果不是可以重新设计接口新实现server以避免大范围的修改
 */
public class MyBizClient {
    public static void main(String[] a) throws IOException {
        Mybiz proxy=
                RPC.getProxy(Mybiz.class,ConstantRPC.RPC_VERSION
                ,new InetSocketAddress(ConstantRPC.RPC_ADD,ConstantRPC.RPC_PORT)
                ,new Configuration());
        String res= proxy.hello("Dino");

        System.out.println(res);
        System.out.println(proxy.hello(" RPC"));
        System.out.println(proxy.chToPinyin("rpc不受系统、地理位置的限制。\n" +
                "\n" +
                "　　不相信的话可以在和护短打断点，一步一步发现进入了；额服务器端"));

        System.out.println(proxy.initPerson("Dino","Boy"));

        RPC.stopProxy(proxy);

    }
}

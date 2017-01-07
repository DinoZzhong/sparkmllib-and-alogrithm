package dino.test.rpc;


import org.apache.hadoop.ipc.VersionedProtocol;

/**
 * Created by Dino on 2017/1/6.
 */


// 定义一个接口,接口没有方法体
public interface Mybiz extends VersionedProtocol {
    long versionID=ConstantRPC.RPC_VERSION;
    String hello(String name);

    String chToPinyin(String x);
    Person initPerson(String name,String value); // 用于测试返回对象

}




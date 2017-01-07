package dino.test.rpc;

import java.io.Serializable;

/**
 * Created by Dino on 2017/1/6.
 */
public class ConstantRPC implements Serializable {
    public static final long RPC_VERSION=1l;
    public static final String RPC_ADD="localhost";
    public static final int RPC_PORT=4050;
}

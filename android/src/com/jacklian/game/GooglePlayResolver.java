package com.jacklian.game;

/**
 * Created by jacklian on 25/03/2016.
 */

import com.badlogic.gdx.pay.PurchaseManagerConfig;


public class GooglePlayResolver extends PlatformResolver {

    private final static String GOOGLEKEY1  = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAml0ZRMNRVkf75z+WQnGwC5v7QIS0mS9vxo0Mc0BCnPeiMowqYR17rZprKV2Srj13FdKF3L6NocntgshtmB6o5noY+eEVcRWG5e/";
    private final static String GOOGLEKEY2  = "JaDEy8LeWJQdLe1HTZRkjxccX8HEyDQlyS17wc7T5guI3mN10EtQPJkNDaUoIiR/vXNYzugRopznlIkAF0OwLY437ERzj6aXXl/WKLgN87dgx+tBkz9tdST0kuRKdye1Umcp5Fpjfokeo0l4DTdkkjiykvS4MQQTJ+2r8rBmgiw8/BAONwN3b+5d9Eb44US9Rz0pkMx4V4r/qbKx65KAcG55mmR2avKkZ/pJZZxI8QQU3v0TVaQIDAQAB";


    static final int RC_REQUEST = 10001;	// (arbitrary) request code for the purchase flow

    public GooglePlayResolver(MyGdxGame game) {
        super(game);

        PurchaseManagerConfig config = game.purchaseManagerConfig;
        config.addStoreParam(PurchaseManagerConfig.STORE_NAME_ANDROID_GOOGLE, GOOGLEKEY1 + GOOGLEKEY2);
        initializeIAP(null, game.purchaseObserver, config);
    }
}
//package com.jacklian.game;
//
///**
// * Created by jacklian on 25/03/2016.
// */
//
//import com.badlogic.gdx.backends.android.AndroidApplication;
//
//package com.mygdx.game.android;
//
//        import com.badlogic.gdx.backends.android.AndroidApplication;
//        import com.badlogic.gdx.pay.PurchaseManagerConfig;
//        import com.badlogic.gdx.pay.android.amazon.PurchaseManagerAndroidAmazon;
//
//public class AmazonFireResolver extends PlatformResolver {
//
//    public AmazonFireResolver (MyGdxGame game, AndroidApplication androidApplication) {
//        super(game);
//
//        // here i am setting up an alternate purchaseManager .... normally amazon should work with openIAB, too!
//        PurchaseManagerAndroidAmazon mgr = new PurchaseManagerAndroidAmazon(androidApplication, 1001);
//        PurchaseManagerConfig config = game.purchaseManagerConfig;
//        initializeIAP(mgr, game.purchaseObserver, config);
//    }
//}
package com.jacklian.game;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


public class AndroidLauncher extends AndroidApplication implements ActionResolver {

	private static final String AD_UNIT_ID_BANNER = "ca-app-pub-7076203060950505/8849023677";
//	private static final String AD_UNIT_ID_INTERSTITIAL = "ca-app-pub-6916351754834612/3808499421";
//	private static final String GOOGLE_PLAY_URL = "https://play.google.com/store/apps/developer?id=TheInvader360";
//	private static final String GITHUB_URL = "https://github.com/TheInvader360";
//	private static final String BLOG_URL = "http://theinvader360.blogspot.co.uk/";
	protected AdView adView;
	protected View gameView;
	private InterstitialAd mInterstitialAd;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		//InterstitialAd
		setUpIntersitialAds();

		// Create the layout
		RelativeLayout layout = new RelativeLayout(this);

		// Do the stuff that initialize() would do for you
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		// Create the libgdx View
		MyGdxGame game = new MyGdxGame(this);
		View gameView = initializeForView(game, config);

		// Create and setup the AdMob view
		AdView adView = new AdView(this); // Put in your secret key here
		adView.setAdSize(AdSize.LARGE_BANNER);
		adView.setAdUnitId(AD_UNIT_ID_BANNER);

		AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
		adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
						.addTestDevice("01C8E3F1BF586596010809D8B5D89122");

		adView.loadAd(adRequestBuilder.build());

		// Add the libgdx view
		layout.addView(gameView);

		// Add the AdMob view
		RelativeLayout.LayoutParams adParams =
				new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		layout.addView(adView, adParams);

		// Hook it all up
		setContentView(layout);
//		initialize(new MyGdxGame(), config);

		payInit(game);
	}

	public void payInit(MyGdxGame game)
	{
		game.payConfig();
		Gdx.app.log("pay", "payInit start");
		if(game.getAppStore() == game.APPSTORE_OUYA) {
//			game.setPlatformResolver(new OUYAResolver(game));
		}
		else if(game.getAppStore() == game.APPSTORE_GOOGLE) {
			Gdx.app.log("pay", "google platform resolve set done");
			game.setPlatformResolver(new GooglePlayResolver(game));
		}
		else if(game.getAppStore() == game.APPSTORE_AMAZON) {
//			game.setPlatformResolver(new AmazonFireResolver(game, this));
		}

		Gdx.app.log("pay", "payInit done");
		Gdx.app.log("pay", "installIAP start");
		game.getPlatformResolver().installIAP();
		Gdx.app.log("pay", "installIAP done");
	}

	@Override
	protected void onPause() {
		if(adView != null) {
			adView.pause();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(adView != null) {
			adView.resume();
		}
	}

	public void setUpIntersitialAds()
	{

		mInterstitialAd = new InterstitialAd(this);
		mInterstitialAd.setAdUnitId("ca-app-pub-7076203060950505/6419300873");

		mInterstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
//				Toast.makeText(getApplicationContext(), "Finished Loading Interstitial", Toast.LENGTH_SHORT).show();
//				mInterstitialAd.show();
			}

			@Override
			public void onAdClosed() {
				requestNewInterstitial();
//				beginPlayingGame();
			}
		});

		requestNewInterstitial();
	}

	private void requestNewInterstitial() {
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("01C8E3F1BF586596010809D8B5D89122")
				.build();

		mInterstitialAd.loadAd(adRequest);
	}

	public void showOrLoadInterstital() {
		runOnUiThread(new Runnable() {
			@Override public void run() {
				if (mInterstitialAd.isLoaded()) {
					mInterstitialAd.show();
				}
			}

		});
	}

}

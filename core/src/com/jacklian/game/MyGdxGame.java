package com.jacklian.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.pay.Offer;
import com.badlogic.gdx.pay.OfferType;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseObserver;
import com.badlogic.gdx.pay.Transaction;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor {
	Stage stage;
	private Table mainTable;
	private ActionResolver actionResolver;
	public AssetManager assetManager;
	Boolean isAssetLoaded = false;

	private OrthographicCamera camera;
	ScrollPane upgradesScrollPane;
	ScrollPane warriorsScrollPane;
	ScrollPane itemsScrollPane;
	ScrollPane achievementsScrollPane;

	UpgradesManager upgradesManager;
	MainCharacter mainCharacter;

	Skin uiSkin;
	com.jacklian.game.MonsterManager monsterManager;
	HeroManager heroManager;
	MoneyManager moneyManager;
	com.jacklian.game.ProjectileManager projectileManager;
	Background background;

	float prevX = 0, prevY = 0;
	Boolean isReleased = true;
	int myDamage = 1;

	//MARK: InputProcessor
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//		System.out.println("GameInputProcesser Money++");
		tapOnBackground();
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	public MyGdxGame()
	{
		this.actionResolver = null;
	}

	MyGdxGame(ActionResolver actionResolver)
	{
		this.actionResolver = actionResolver;
	}


	//PAY
	public static final int APPSTORE_UNDEFINED	= 0;
	public static final int APPSTORE_GOOGLE 	= 1;
	public static final int APPSTORE_OUYA 		= 2;
	public static final int APPSTORE_AMAZON 	= 3;
	public static final int APPSTORE_DESKTOP 	= 4;

	private int isAppStore = APPSTORE_GOOGLE;
	String productID_fullVersion = "donate_001";
	static PlatformResolver m_platformResolver;
	public PurchaseManagerConfig purchaseManagerConfig;
	public PurchaseObserver purchaseObserver = new PurchaseObserver() {
		@Override
		public void handleRestore (Transaction[] transactions) {
			for (int i = 0; i < transactions.length; i++) {
				if (checkTransaction(transactions[i].getIdentifier(), true) == true) break;
			}
		}
		@Override
		public void handleRestoreError (Throwable e) {
			throw new GdxRuntimeException(e);
		}
		@Override
		public void handleInstall () {	}

		@Override
		public void handleInstallError (Throwable e) {
			Gdx.app.log("ERROR", "PurchaseObserver: handleInstallError!: " + e.getMessage());
			throw new GdxRuntimeException(e);
		}
		@Override
		public void handlePurchase (Transaction transaction) {
			checkTransaction(transaction.getIdentifier(), false);
		}
		@Override
		public void handlePurchaseError (Throwable e) {	//--- Amazon IAP: this will be called for cancelled
			throw new GdxRuntimeException(e);
		}
		@Override
		public void handlePurchaseCanceled () {	//--- will not be called by amazonIAP
		}
	};

	protected boolean checkTransaction (String ID, boolean isRestore) {
		boolean returnbool = false;
		if (productID_fullVersion.equals(ID)) {
			Gdx.app.log("checkTransaction", "full version found!");

			//----- put your logic for full version here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

			returnbool = true;
		}
		return returnbool;
	}

	public void payConfig () {

		setAppStore(APPSTORE_GOOGLE);	// change this if you deploy to another platform

		// ---- IAP: define products ---------------------
		purchaseManagerConfig = new PurchaseManagerConfig();
		purchaseManagerConfig.addOffer(new Offer().setType(OfferType.CONSUMABLE).setIdentifier(productID_fullVersion));
	}

	public PlatformResolver getPlatformResolver() {
		return m_platformResolver;
	}
	public static void setPlatformResolver (PlatformResolver platformResolver) {
		m_platformResolver = platformResolver;
	}

	public int getAppStore () {
		return isAppStore;
	}
	public void setAppStore (int isAppStore) {
		this.isAppStore = isAppStore;
	}

	@Override
	public void create () {
		super.create();
		Gdx.app.log("pay","pay created");
		loadAssets();
	}

	public void initGame () {

		OrthographicCamera camera = new OrthographicCamera();
//		camera.rotate(90);

//		stage  = new Stage(new StretchViewport(600,600));
//		stage  = new Stage(new FitViewport(600,700,camera))
		stage  = new Stage(new ExtendViewport(600,900,camera));

		//Inputs
		InputMultiplexer multiplexer = new InputMultiplexer();
		Array<InputProcessor> array = new Array<InputProcessor>();
		array.add(stage);
		array.add(this);
		multiplexer.setProcessors(array);
		Gdx.input.setInputProcessor(multiplexer);


//		Gdx.input.setInputProcessor(this.stage);
//		stage.getViewport().getCamera().rotate(270, 0, 0, 1);

		mainTable = new Table();
		mainTable.setFillParent(true);
		mainTable.bottom();

		camera = new OrthographicCamera();
//		camera.setToOrtho(false, 800, 480);
//		camera.rotate(90);



		uiSkin = new Skin(Gdx.files.internal("uiskin.json"));

		//Add money counter
//		moneyLabel = new MoneyLabel("Money", uiSkin);
//		moneyLabel.setPosition(400, 400);
//		stage.addActor(moneyLabel);

		//Background
		background = new Background(stage);
		stage.addActor(background);




		//MainCharacter
		mainCharacter = new MainCharacter();

		//Money
		moneyManager = new MoneyManager();
		stage.addActor(moneyManager);

		//Monster
		monsterManager = new com.jacklian.game.MonsterManager(stage, moneyManager, this);
		stage.addActor(monsterManager);

		projectileManager = new com.jacklian.game.ProjectileManager(monsterManager);
		stage.addActor(projectileManager);

		//Hero
		heroManager = new com.jacklian.game.HeroManager(monsterManager, stage, projectileManager, moneyManager, this);
		stage.addActor(heroManager);

		mainTable.row();
		Stack container = new Stack();
		Gdx.app.log("game height","height = " + Gdx.graphics.getHeight());
		mainTable.add(container).padTop(500).expandX().fillX();

		initUpgradesPanel(container);
		initWarriorsPanel(container);
		initItemsPanel(container);
		initAchievementsPanel(container);
		createPurchasableMenu();

		stage.addActor(mainTable);

		//rotate
//		Matrix4 mx4Font = new Matrix4();
//		mx4Font.setToRotation(new Vector3(0, 0, 1), 90);
//		batch.setTransformMatrix(mx4Font);

//		background.addListener(new InputListener() {
//			@Override
//			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
////				super.touchUp(event, x, y, pointer, button);
//				System.out.println("Money++");
//				money++;
////				moneyLabel.updateText(money.toString());
//				monsterManager.takeDamage(mainCharacter.damage);
//			}
//		});

//		background.addListener()

		loadSavedData();

		initUI();
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(assetManager.update()) {
			if(!isAssetLoaded) {
				isAssetLoaded = true;
				initGame();
			}
			stage.act();
			stage.draw();
		}

//		Vector3 touchPos = new Vector3();
//		if(Gdx.input.justTouched()) {
//			System.out.println("Money++");
//			money++;
////				moneyLabel.updateText(money.toString());
//			monsterManager.takeDamage(mainCharacter.damage);
//		}
//		if(Gdx.input.isTouched()) {
//			isReleased = false;
////			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
////			camera.unproject(touchPos);
//////			camera.unproject(touchPos);
//////			bucket.x = touchPos.y - 64 / 2;
////			bucket.x = (touchPos.x);
////
////			prevX = touchPos.x;
////			prevY = touchPos.y;
//		} else {
//			if (!isReleased) {
//				System.out.println("Money++");
//				money++;
////				moneyLabel.updateText(money.toString());
//				monsterManager.takeDamage(myDamage);
//
////				Vector3 touchPos = new Vector3();
////				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
////				stage.getViewport().getCamera().unproject(touchPos);
////				System.out.println("x: " + touchPos.x + ", y: " + touchPos.y);
////
////				FadingLabel damage = new FadingLabel("+1", uiSkin);
////				damage.setPosition(touchPos.x, touchPos.y);
////				stage.addActor(damage);
//			}
//
//			isReleased = true;
//		}
//
//		Gdx.gl.glClearColor(1, 0, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//		camera.update();
//		batch.setProjectionMatrix(camera.combined);



//		Gdx.app.log("x: " + bucket.x + " y: " + bucket.y, "asd");



//		if(Gdx.input.isKeyPressed(Keys.LEFT)) bucket.x -= 10 * Gdx.graphics.getDeltaTime();
//		if(Gdx.input.isKeyPressed(Keys.RIGHT)) bucket.x += 10 * Gdx.graphics.getDeltaTime();

	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		if(stage != null) {
			stage.getViewport().update(width, height, true);
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		if(stage != null) {
			stage.dispose();
		}
	}

	public void createPurchasableMenu()
	{
		Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
		Table upgradesMenu = new Table();
		upgradesMenu.row().height(100);

		final TextButton button = new TextButton("Upgrades", uiSkin);
		final TextButton button2 = new TextButton("Heros", uiSkin);
		final TextButton button3 = new TextButton("Items", uiSkin);
		final TextButton button4 = new TextButton("$$$", uiSkin);

		button.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("click " + x + ", " + y);
				upgradesScrollPane.setVisible(button.isChecked());
				warriorsScrollPane.setVisible(button2.isChecked());
				itemsScrollPane.setVisible(button3.isChecked());
				achievementsScrollPane.setVisible(button4.isChecked());
			}
		});


		button2.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("click 2 " + x + ", " + y);
				upgradesScrollPane.setVisible(button.isChecked());
				warriorsScrollPane.setVisible(button2.isChecked());
				itemsScrollPane.setVisible(button3.isChecked());
				achievementsScrollPane.setVisible(button4.isChecked());
			}
		});



		button3.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("click 3 " + x + ", " + y);
				upgradesScrollPane.setVisible(button.isChecked());
				warriorsScrollPane.setVisible(button2.isChecked());
				itemsScrollPane.setVisible(button3.isChecked());
				achievementsScrollPane.setVisible(button4.isChecked());
			}
		});


		button4.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("click 4 " + x + ", " + y);
				upgradesScrollPane.setVisible(button.isChecked());
				warriorsScrollPane.setVisible(button2.isChecked());
				itemsScrollPane.setVisible(button3.isChecked());
				achievementsScrollPane.setVisible(button4.isChecked());
			}
		});

		ButtonGroup tabs = new ButtonGroup();
		tabs.setMinCheckCount(0);
		tabs.setMaxCheckCount(1);
		tabs.add(button);
		tabs.add(button2);
		tabs.add(button3);
		tabs.add(button4);

		upgradesMenu.add(button).expand().fill();
		upgradesMenu.add(button2).expand().fill();
		upgradesMenu.add(button3).expand().fill();
		upgradesMenu.add(button4).expand().fill();
		mainTable.row();
		mainTable.add(upgradesMenu).padBottom(170).fill();

	}

	public void initUpgradesPanel(Stack container)
	{
		upgradesManager = new UpgradesManager(mainCharacter, this);

		Table scrollTable = new Table();
//		scrollTable.setDebug(true);

		for (int i = 0; i < upgradesManager.numberOfRows(); i++) {
			final int damage = i;
			final int row = i;
			scrollTable.row();

			Actor view = upgradesManager.actorForRow(row);
			scrollTable.add(view).width(560).height(80).pad(20);

//			String title = upgradesManager.titleForRow(row);
//			TextButton button = new TextButton(title + i, uiSkin);
//			scrollTable.add(button).width(200).height(40).pad(20);
//			button.addListener(new ClickListener() {
//				public void clicked (InputEvent event, float x, float y) {
//					System.out.println("click " + x + ", " + y);
//					upgradesManager.didClickItemOnRow(row);
//					myDamage = damage;
//				}
//			});
		}

//		upgradesScrollPane = new ScrollPane(scrollTable, uiSkin);
//		upgradesScrollPane.setScrollingDisabled(true, false);
//
//		container.add(upgradesScrollPane);

		upgradesScrollPane = upgradesManager.scrollPaneForUpgrades();
		container.add(upgradesScrollPane);
	}

	public void initWarriorsPanel(Stack container)
	{
//		Table scrollTable = new Table();
//		scrollTable.setDebug(true);
//
//		for (int i = 0; i < heroManager.numberOfRows(); i++) {
//			final int level = i;
//			scrollTable.row();
//
//			Actor actor = heroManager.actorForRow(level);
//			scrollTable.add(actor).width(200).height(40).pad(20);


//			TextButton button = new TextButton("Warriors " + i, uiSkin);
//			scrollTable.add(button).width(200).height(40).pad(20);
//			button.addListener(new ClickListener() {
//				public void clicked (InputEvent event, float x, float y) {
//					System.out.println("click " + x + ", " + y);
//					heroManager.addHero(level+1);
//				}
//			});
//		}

//		warriorsScrollPane = new ScrollPane(scrollTable, uiSkin);
//		scrollPane.setFillParent(true);
//		warriorsScrollPane.setScrollingDisabled(true, false);
//		scrollPane.setX(0);
//		scrollPane.setY(0);
//		stage.addActor(scrollPane);

		warriorsScrollPane = heroManager.scrollPaneForHeroes();
		container.add(warriorsScrollPane);
//		mainTable.row();
//		mainTable.add(warriorsScrollPane).height(240).expandX().fillX().padTop(300);
	}

	public void initItemsPanel(Stack container)
	{
		Table scrollTable = new Table();
//		scrollTable.setDebug(true);

		for (int i = 0; i < 100; i++) {
			final int row = i;
//			scrollTable.row();
//			HeroListing heroListing = new HeroListing(i);
//			scrollTable.add(heroListing).width(560).height(80).pad(20);

			scrollTable.row();
			TextButton button = new TextButton("Items " + i, uiSkin);
			scrollTable.add(button).width(560).height(80).pad(20);
			button.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					System.out.println("click item " + x + ", " + y);
					if(row == 0) {
						moneyManager.setTotal(moneyManager.getTotal() * 10);
					} else if (row == 1) {
						clearSavedData();
					} else if (row == 2) {
						getPlatformResolver().requestPurchase(productID_fullVersion);
					}
				}
			});
		}

		itemsScrollPane = new ScrollPane(scrollTable, uiSkin);
//		scrollPane.setFillParent(true);
		itemsScrollPane.setScrollingDisabled(true, false);
//		scrollPane.setX(0);
//		scrollPane.setY(0);
//		stage.addActor(scrollPane);

		container.add(itemsScrollPane);
//		mainTable.row();
//		mainTable.add(itemsScrollPane).height(240).expandX().fillX().padTop(300);
	}

	public void initAchievementsPanel(Stack container)
	{
		Table scrollTable = new Table();
//		scrollTable.setDebug(true);

		for (int i = 0; i < 100; i++) {

			scrollTable.row();
			TextButton button = new TextButton("Achievements " + i, uiSkin);
			scrollTable.add(button).width(560).height(80).pad(20);
			button.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					System.out.println("click " + x + ", " + y);
					if(actionResolver != null) {
						actionResolver.showOrLoadInterstital();
					}
				}
			});
		}

		achievementsScrollPane = new ScrollPane(scrollTable, uiSkin);
//		scrollPane.setFillParent(true);
		achievementsScrollPane.setScrollingDisabled(true, false);
//		scrollPane.setX(0);
//		scrollPane.setY(0);
//		stage.addActor(scrollPane);

		container.add(achievementsScrollPane);
//		mainTable.row();
//		mainTable.add(achievementsScrollPane).height(240).expandX().fillX().padTop(300);
	}

	public void loadSavedData()
	{
		Preferences prefs = Gdx.app.getPreferences("My Preferences");
		moneyManager.setLevel(prefs.getInteger("level"));
		moneyManager.setTotal(prefs.getInteger("money"));
	}

	public void clearSavedData()
	{
		Preferences prefs = Gdx.app.getPreferences("My Preferences");
		prefs.putInteger("level", 1);
		prefs.putInteger("money", 0);
		prefs.flush();
		loadSavedData();
	}

	public void tapOnBackground()
	{
		monsterManager.takeDamage(mainCharacter.damage);
	}

	public void loadAssets()
	{
		assetManager = new AssetManager();
		assetManager.load("dogIdle.png", Texture.class);
		assetManager.load("catIdle.png", Texture.class);
		assetManager.load("healthbar.png", Texture.class);
		assetManager.load("spin_coin_big_upscale_strip6.png", Texture.class);

		assetManager.load("JackFirstGameFont.fnt", BitmapFont.class);
	}

	public void initUI()
	{
		Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));

		TextButton button = new TextButton("Setting", uiSkin);

		button.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("click " + x + ", " + y);
				openSettingsMenu();
			}
		});
		button.setPosition(50, 850);
		stage.addActor(button);

		TextButton button2 = new TextButton("Leaderboard", uiSkin);

		button2.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("click " + x + ", " + y);
			}
		});
		button2.setPosition(450, 850);
		stage.addActor(button2);
	}

	public void openSettingsMenu()
	{
		Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));

		Table tb = new Table();
		tb.setFillParent(true);
		tb.bottom();

		final ScrollPane sp = new ScrollPane(tb, uiSkin);

		TextButton button = new TextButton("Setting", uiSkin);

		button.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("click " + x + ", " + y);
				sp.remove();
			}
		});
//		button.setPosition(400,850);



		tb.row();
		tb.add(button).padTop(10).expandX().fillX();



//		scrollPane.setFillParent(true);
		sp.setScrollingDisabled(true, false);
		sp.setFillParent(true);
//		scrollPane.setX(0);
//		scrollPane.setY(0);
//		stage.addActor(scrollPane);

		stage.addActor(sp);
	}
}

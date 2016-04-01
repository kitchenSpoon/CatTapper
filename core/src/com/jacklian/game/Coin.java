package com.jacklian.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by jacklian on 21/02/2016.
 */
public class Coin extends FadingLabel {
    MoneyManager moneyManager;
    Integer value;
    Stage stage;
    MyGdxGame game;

    Animation coinAnimation;          // #3
    Texture coinSheet;              // #4
    TextureRegion[]                 coinFrames;             // #5
    SpriteBatch spriteBatch;            // #6
    TextureRegion                   currentFrame;

    float stateTime;

    BitmapFont font;
    float timeLeft = 10;

    public Coin(MoneyManager moneyManager, Integer value, CharSequence text, Skin skin, Stage stage, MyGdxGame game) {
        super(text,skin);
        this.moneyManager = moneyManager;
        this.value = value;
        this.stage = stage;
        this.game = game;

//        font = new BitmapFont(Gdx.files.internal("JackFirstGameFont.fnt"), Gdx.files.internal("JackFirstGameFont.png"), false);
        font = game.assetManager.get("JackFirstGameFont.fnt", BitmapFont.class);
        removeSelfOnInvisible = false;
        turnsInvisible = false;
        addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                addMoney();
                remove();
                return false;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
            }
        });

        //
//        coinSheet = new Texture(Gdx.files.internal("spin_coin_big_upscale_strip6.png")); // #9
        coinSheet = game.assetManager.get("spin_coin_big_upscale_strip6.png", Texture.class);
        TextureRegion[][] tmp = TextureRegion.split(coinSheet, coinSheet.getWidth()/6, coinSheet.getHeight());              // #10
        coinFrames = new TextureRegion[6];
        int index = 0, framesSet = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 6; j++) {
                if(framesSet >= 6) break;
                coinFrames[index++] = tmp[i][j];
                framesSet++;
            }
        }
        coinAnimation = new Animation(0.025f, coinFrames);      // #11
        spriteBatch = new SpriteBatch();                // #12
        stateTime = 0f;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setBounds(xPos,yPos,50,50);
        if(yPos <= 600) {
            yPos = 600;
            speed = new Vector2(0,0);
            gravity = new Vector2(0,0);
        }

        timeLeft -= delta;
        if (timeLeft <= 0 ) {
            addMoney();
            remove();
        }
//        label.setPosition(xPos,yPos);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        stateTime += Gdx.graphics.getDeltaTime();           // #15
        currentFrame = coinAnimation.getKeyFrame(stateTime, true);  // #16
//        batch.setColor(Color.FIREBRICK);
        batch.draw(currentFrame, xPos, yPos, 50, 50);
//        batch.draw(font,xPos, yPos, 18, 20);
//        font.draw(batch, "" + value, xPos, yPos);
    }

    public void addMoney(){
        moneyManager.setTotal(moneyManager.getTotal() + value);
        FadingNumber number = new FadingNumber("+" + value);
        number.setPosition(xPos,yPos);
        stage.addActor(number);
    }
}

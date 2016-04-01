package com.jacklian.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by jacklian on 15/02/2016.
 */
public class Monster extends Actor {
    private static final int        FRAME_COLS = 3;//6;         // #1
    private static final int        FRAME_ROWS = 4;//5;         // #2
    private static final int        TOTAL_FRAMES = 10;//5;         // #2

    MyGdxGame game;

    Animation walkAnimation;          // #3
    Texture walkSheet;              // #4
    TextureRegion[]                 walkFrames;             // #5
    SpriteBatch spriteBatch;            // #6
    TextureRegion                   currentFrame;           // #7

    Texture healthBar;

    float stateTime;                                        // #8

    MoneyLabel hitpointsLabel;

    public float getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(float hitPoints) {
        this.hitPoints = hitPoints;
    }

    int level = 1;
    float hitPoints = 10, totalHitPoints = 10;
    float x = 300, y = 600;
    float width = 100, height = 100;

    public Monster(MyGdxGame game) {
        super();
        this.game = game;
//        healthBar = new Texture(Gdx.files.internal("healthbar.png"));

        healthBar = game.assetManager.get("healthbar.png", Texture.class);

//        walkSheet = new Texture(Gdx.files.internal("catIdle.png")); // #9
        walkSheet = game.assetManager.get("catIdle.png", Texture.class); // #9
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()/FRAME_COLS, walkSheet.getHeight()/FRAME_ROWS);              // #10
        walkFrames = new TextureRegion[TOTAL_FRAMES];
        int index = 0, framesSet = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                if(framesSet >= TOTAL_FRAMES) break;
                walkFrames[index++] = tmp[i][j];
                framesSet++;
            }
        }
        walkAnimation = new Animation(0.025f, walkFrames);      // #11
        spriteBatch = new SpriteBatch();                // #12
        stateTime = 0f;

        Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        hitpointsLabel = new MoneyLabel("", uiSkin);
        hitpointsLabel.setPosition(x - 100f / 2, y - 20);
    }

    public Monster(int level, MyGdxGame game) {
        this(game);

        this.level = level;
        this.width = 100 + (level* 10);
        this.height = 100 + (level * 10);

        hitPoints = new Double(28 * new Double(Math.pow(1.075,level))).intValue();
        totalHitPoints = new Double(28 * new Double(Math.pow(1.075,level))).intValue();
        updateHitPointsLabel();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        hitpointsLabel.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        stateTime += Gdx.graphics.getDeltaTime();           // #15
        currentFrame = walkAnimation.getKeyFrame(stateTime, true);  // #16
        batch.setColor(Color.FIREBRICK);
        batch.draw(currentFrame, x - width / 2, y, width, height);             // #17

        float life = (hitPoints / totalHitPoints) * 100f;
        batch.setColor(Color.BLACK);
        batch.draw(healthBar, x - 100f / 2, y, 100f, 14f);
        batch.setColor(Color.RED);
        batch.draw(healthBar, x - 100f / 2, y, life, 10f);
        batch.setColor(Color.WHITE);

        hitpointsLabel.draw(batch, parentAlpha);
    }

    public void takeDamage(float damage){
        hitPoints -= damage;
        if (hitPoints < 0) {
            hitPoints = 0;
        }
        updateHitPointsLabel();
    }

    public void updateHitPointsLabel()
    {
        hitpointsLabel.updateText("" + hitPoints + "/" + totalHitPoints + " HP");
    }
}
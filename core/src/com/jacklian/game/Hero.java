package com.jacklian.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by jacklian on 15/02/2016.
 */
public class Hero extends Actor {
    protected Stage stage;
    MyGdxGame game;
    ProjectileManager projectileManager;
    protected float timeTillShoot = 1;
    private static final int        FRAME_COLS = 3;//6;         // #1
    private static final int        FRAME_ROWS = 4;//5;         // #2
    private static final int        TOTAL_FRAMES = 10;//5;         // #2

    Animation walkAnimation;          // #3
    Texture walkSheet;              // #4
    TextureRegion[]                 walkFrames;             // #5
    SpriteBatch spriteBatch;            // #6
    TextureRegion                   currentFrame;           // #7

    float stateTime;                                        // #8

    float hitPoints = 20, totalHitPoints = 20;
    float x = 250, y = 600;
    float width = 40, height = 40;

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    int damage = 1, level = 1;

    public Hero(MyGdxGame game) {
        super();
        this.game = game;
//        walkSheet = new Texture(Gdx.files.internal("sprite-animation4.png")); // #9
//        walkSheet = new Texture(Gdx.files.internal("dogIdle.png")); // #9
        walkSheet = game.assetManager.get("dogIdle.png", Texture.class); // #9
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
    }

    public void shoot()
    {
        Projectile newProject = new Projectile(new Vector2(x,y),new Vector2(400, 400), damage);
        projectileManager.addProjectile(newProject);
        stage.addActor(newProject);
    }

    public Hero(int level, float x, float y, Stage stage, ProjectileManager projectileManager, MyGdxGame game){
        this(game);
        this.stage = stage;
        this.projectileManager = projectileManager;
        this.level = level;
        this.damage *= level;
        width += level * 10;
        height += level * 10;
        this.x = x;
        this.y = y;

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        timeTillShoot -= delta;
        if(timeTillShoot <= 0) {
            shoot();
            timeTillShoot = 2;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        stateTime += Gdx.graphics.getDeltaTime();           // #15
        currentFrame = walkAnimation.getKeyFrame(stateTime, true);  // #16
        batch.draw(currentFrame, x, y, width, height);

    }

    public void increaseLevel()
    {
        level++;
        damage++;
    }
}
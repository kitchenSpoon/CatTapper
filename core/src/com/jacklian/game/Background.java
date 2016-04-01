package com.jacklian.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by jacklian on 15/03/2016.
 */
public class Background extends Actor {

    Texture backgroundTextureBack;
    Texture backgroundTextureForest;
    Texture backgroundTextureTrees;

    Image backgroundImageBack;
    Image backgroundImageForest;
    Image backgroundImageTrees;

    Stage stage;

    public Background(Stage stage) {
        super();
        this.stage = stage;
        Image backgroundImageBack = new Image(new TextureRegion(new Texture(Gdx.files.internal("country-platform-back.png"))));
        backgroundImageBack.setFillParent(true);
        Image backgroundImageForest = new Image(new TextureRegion(new Texture(Gdx.files.internal("country-platform-forest.png"))));
        backgroundImageForest.setFillParent(true);
        Image backgroundImageTrees = new Image(new TextureRegion(new Texture(Gdx.files.internal("country-platform-tiles-example.png"))));
        backgroundImageTrees.setFillParent(true);
        stage.addActor(backgroundImageBack);
        stage.addActor(backgroundImageForest);
        stage.addActor(backgroundImageTrees);

//        backgroundTextureBack = new Texture(Gdx.files.internal("country-platform-back.png"));
//        backgroundTextureForest = new Texture(Gdx.files.internal("country-platform-forest.png"));
//        backgroundTextureTrees = new Texture(Gdx.files.internal("country-platform-tiles-example.png"));
//        Image backgroundImageBack = new Image(new TextureRegion(backgroundTextureBack));
//        backgroundImageBack.setFillParent(true);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setBounds(0,0,1600,1900);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        setBounds(0,0,1600,1900);
//        batch.draw(backgroundTextureBack, 0, 0, 384, 224);
//        batch.draw(backgroundTextureForest, 0, 0, 160, 224);
//        batch.draw(backgroundTextureTrees, 0, 0, 384, 224);

//        batch.draw(backgroundImageBack, 0, 0, 384, 224);
//        batch.draw(backgroundTextureForest, 0, 0, 160, 224);
//        batch.draw(backgroundTextureTrees, 0, 0, 384, 224);
    }
}

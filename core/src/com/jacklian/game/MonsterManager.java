package com.jacklian.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.Random;

/**
 * Created by jacklian on 15/02/2016.
 */
public class MonsterManager extends Actor {
    Stage stage;
    MyGdxGame game;
//    int level = 1;
    com.jacklian.game.Monster currentMonster;
    MoneyManager moneyManager;

    public MonsterManager(Stage stage, MyGdxGame game) {
        super();
        this.stage = stage;
        this.game = game;
        currentMonster = new com.jacklian.game.Monster(game);
    }

    public MonsterManager(Stage stage, MoneyManager moneyManager, MyGdxGame game) {
        this(stage, game);
        this.moneyManager = moneyManager;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(currentMonster.getHitPoints() == 0) {
            spawnCoin(currentMonster.x, currentMonster.y);
            spawnCoin(currentMonster.x, currentMonster.y);
            spawnCoin(currentMonster.x, currentMonster.y);
            spawnCoin(currentMonster.x, currentMonster.y);
            spawnCoin(currentMonster.x, currentMonster.y);

//            moneyManager.setTotal(moneyManager.getTotal()+level);
            moneyManager.increaseLevel();
//            level++;
            currentMonster = new com.jacklian.game.Monster(moneyManager.getLevel(), game);
        }
        currentMonster.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        currentMonster.draw(batch, parentAlpha);
    }

    public void takeDamage(int damage){
        currentMonster.stateTime = 0;
        currentMonster.takeDamage(damage);
        Boolean isCritical = false;
        Random random = new Random();
        if (random.nextFloat() < 0.05) {
            damage *= 10;
            isCritical = true;
        }
        spawnDamage(damage, isCritical);


    }

    public void spawnCoin(float x, float y){
        Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        com.jacklian.game.Coin coin = new com.jacklian.game.Coin(moneyManager,moneyManager.getLevel(),"+"+moneyManager.getLevel(), uiSkin, stage, game);
        coin.setPosition(x, y + 100);
        stage.addActor(coin);
    }

    public void spawnDamage(Integer damage, Boolean isCritical) {
        Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
//        com.jacklian.game.FadingLabel damageLabel = new com.jacklian.game.FadingLabel( "+"+damage, uiSkin);
//        damageLabel.setPosition(currentMonster.x, currentMonster.y + 100);
//        stage.addActor(damageLabel);
        int maxX = 4, minX = -4, maxY = 4, minY = -4;
        Random random = new Random();
        int xOffset = random.nextInt(maxX - minX + 1) + minX;
        int yOffset = random.nextInt(maxY - minY + 1) + minY;
        FadingNumber damageNumber = new FadingNumber("" + damage);
        damageNumber.setPosition(currentMonster.x + xOffset, currentMonster.y + yOffset + (currentMonster.height * (float) 0.8));
        damageNumber.setScale((float)0.8,(float)0.8);
        if(isCritical){
            damageNumber.setScale((float)1.1,(float)1.1);
        }

        stage.addActor(damageNumber);
    }
}

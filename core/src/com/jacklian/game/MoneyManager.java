package com.jacklian.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by jacklian on 16/02/2016.
 */
public class MoneyManager extends Actor {

    Skin uiSkin;
    MoneyLabel moneyLabel;
    BitmapFont moneyFont;
    BitmapFont levelFont;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
        Preferences prefs = Gdx.app.getPreferences("My Preferences");
        prefs.putInteger("money", total);
        prefs.flush();
    }

    Integer total = 0;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    Integer level = 1;

    public MoneyManager() {
        super();
        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        moneyLabel = new MoneyLabel("Money", uiSkin);
        moneyLabel.setPosition(400, 400);
        moneyFont = new BitmapFont(Gdx.files.internal("JackFirstGameFont.fnt"), Gdx.files.internal("JackFirstGameFont.png"), false);
        levelFont = new BitmapFont(Gdx.files.internal("JackFirstGameFont.fnt"), Gdx.files.internal("JackFirstGameFont.png"), false);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        moneyLabel.updateText(total.toString());
        moneyLabel.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        moneyLabel.draw(batch, parentAlpha);
        moneyFont.draw(batch, "$" + total, 100, 900);
        levelFont.draw(batch, "Level " + level, 300, 900);
    }

    public void increaseLevel()
    {
        level++;
        Preferences prefs = Gdx.app.getPreferences("My Preferences");
        prefs.putInteger("level", level);
        prefs.flush();
    }
}

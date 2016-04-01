package com.jacklian.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by jacklian on 19/03/2016.
 */
public class HeroListing extends Group {

    Integer level;
    Label levelLabel;
    Integer row;
    Integer baseCost;

    public HeroListing(Integer row, final Runnable block) {
        level = 0;
        this.row = row;
        this.baseCost = (row + 1) * 10;

        Image background = new Image(new Texture(Gdx.files.internal("country-platform-tiles-example.png")));
        background.setBounds(getX(), getY(), 560, 80);
        addActor(background);

        Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));


        Image hero = new Image(new Texture(Gdx.files.internal("dogHero.png")));
        hero.setBounds(getX() + 40, getY(), 60, 60);
        addActor(hero);

        Label desLabel = new Label("A ninja who is actually a dog.", uiSkin);
        desLabel.setBounds(getX() + 120, getY(), 60, 20);
        addActor(desLabel);

        levelLabel = new Label("Please buy me! Price: " + getPrice(), uiSkin);
        levelLabel.setBounds(getX() + 120, getY() + 20, 60, 60);
        addActor(levelLabel);

        TextButton button = new TextButton("Items " + row, uiSkin);
        button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("click " + x + ", " + y);
                block.run();
            }
        });
        button.setBounds(getX() + 460, getY(), 100, 60);
        addActor(button);
    }

    public void increaseLevel()
    {
        level++;
        levelLabel.setText("Level: " + level + " Price: " + getPrice().intValue());
    }

    public Double getPrice()
    {
        Double multiplier = 1.07;
        Double price =  baseCost * Math.pow(multiplier, level.doubleValue());
        return price;
    }

}

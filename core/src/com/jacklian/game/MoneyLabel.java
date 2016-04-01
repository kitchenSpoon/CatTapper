package com.jacklian.game;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by jacklian on 16/02/2016.
 */
public class MoneyLabel extends Label {
    private String text = "";

    public MoneyLabel(CharSequence text, Skin skin) {
        super(text, skin);
        this.text = text.toString();
    }

    @Override
    public void act(float delta) {
        this.setText(text);
        super.act(delta);
    }

    public void updateText(final String text){
        this.text = text;
    }
}


package com.jacklian.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.Random;

/**
 * Created by jacklian on 20/02/2016.
 */
public class FadingLabel extends Actor {
    protected Label label;
    private CharSequence text;
    private float fadePerSecond = (float) 1;
    private float alpha = 1;
    protected float xPos, yPos;
    protected Vector2 speed;
    protected Vector2 force;
    protected Vector2 gravity;
    protected boolean removeSelfOnInvisible = true, turnsInvisible = true;

    public FadingLabel(CharSequence text, Skin skin) {
        label = new Label(text, skin);
        this.text = text;
        alpha = 1;
        fadePerSecond = (float) 1;
        int maxX = 4, minX = 1, maxY = 4, minY = 1;
        Random random = new Random();
        int spdX = random.nextInt(maxX - minX + 1) + minX;
        int spdY = random.nextInt(maxY - minY + 1) + minY;
        speed = new Vector2(spdX,spdY);
        force = new Vector2(0,0);
        gravity = new Vector2((float)0,(float)-0.5);

    }

    @Override
    public void act(float delta) {
        label.setText(text);
//        System.out.println("text: " + text);
//        System.out.println("delta: " + String.format("%f", delta));
//        System.out.println("alpha: " + String.format("%f",alpha));
        if(turnsInvisible == true) {
            this.alpha = this.alpha - (delta * fadePerSecond);
        }
        label.setColor(265, 265, 265, this.alpha);
        speed.x += force.x;
        speed.y += force.y;
        speed.x += gravity.x;
        speed.y += gravity.y;
        if(speed.x >= 15) speed.x = 15;
        if(speed.x <= -15) speed.x = -15;
        if(speed.y >= 15) speed.y = 15;
        if(speed.y <= -15) speed.y = -15;
        xPos += speed.x;
        yPos += speed.y;

        label.setPosition(xPos,yPos);
        super.act(delta);

        removeSelfOnInvisible();
    }

    public void removeSelfOnInvisible()
    {
        if(removeSelfOnInvisible == true && this.alpha <= 0) {
            System.out.println("Remove FadingLabel");
            this.remove(); //Only works if this is added to the stage
        }
    }

    public void updateText(final String text){
        this.text = text;
    }

    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        label.draw(batch, parentAlpha);
//        System.out.println("draw");
    }

    @Override
    public void setPosition(float x, float y) {
        this.xPos = x;
        this.yPos = y;
    }
}

package com.jacklian.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;

/**
 * Created by jacklian on 15/03/2016.
 */
public class FadingNumber extends Actor {
    BitmapFont font;
    String value;

    private float fadePerSecond = (float) 4;
    private float alpha = 1;
    protected float xPos, yPos;
    protected Vector2 speed;
    protected Vector2 force;
    protected Vector2 gravity;
    protected boolean removeSelfOnInvisible = true, turnsInvisible = true;

    public FadingNumber(String value)
    {
        font = new BitmapFont(Gdx.files.internal("JackFirstGameFont.fnt"), Gdx.files.internal("JackFirstGameFont.png"), false);
        this.value = value;

        alpha = 1;
        fadePerSecond = (float) 2;
        int maxX = 4, minX = 1, maxY = 4, minY = 1;
        Random random = new Random();
        int spdX = 0;//random.nextInt(maxX - minX + 1) + minX;
        int spdY = 6;//random.nextInt(maxY - minY + 1) + minY;
        speed = new Vector2(spdX,spdY);
        force = new Vector2(0,0);
        gravity = new Vector2(0,0);//Vector2((float)0,(float)-0.5);
    }

    public void removeSelfOnInvisible()
    {
        if(removeSelfOnInvisible == true && this.alpha <= 0) {
            System.out.println("Remove FadingLabel");
            this.remove(); //Only works if this is added to the stage
        }
    }


    @Override
    public void act(float delta) {
        super.act(delta);

        if(turnsInvisible == true) {
            this.alpha = this.alpha - (delta * fadePerSecond);
            font.setColor(1,1,1,this.alpha);
        }
//        label.setColor(265, 265, 265, this.alpha);
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

//        label.setPosition(xPos,yPos);

        removeSelfOnInvisible();

        setBounds(xPos,yPos,50,50);
//        if(yPos <= 600) {
//            yPos = 600;
//            speed = new Vector2(0,0);
//            gravity = new Vector2(0,0);
//        }
//        label.setPosition(xPos,yPos);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
//        stateTime += Gdx.graphics.getDeltaTime();           // #15
//        currentFrame = coinAnimation.getKeyFrame(stateTime, true);  // #16
//        batch.setColor(Color.FIREBRICK);
//        batch.draw(currentFrame, xPos, yPos, 18, 20);
//        batch.draw(font,xPos, yPos, 18, 20);
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(font, "" + value);
        font.draw(batch, glyphLayout, xPos - glyphLayout.width/2, yPos);
    }

    @Override
    public void setPosition(float x, float y) {
        this.xPos = x;
        this.yPos = y;
    }

    @Override
    public void setScale(float scaleX, float scaleY) {
        super.setScale(scaleX, scaleY);
        font.getData().setScale(scaleX,scaleY);
    }
}

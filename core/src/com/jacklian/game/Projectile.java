package com.jacklian.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by jacklian on 22/02/2016.
 */
public class Projectile extends Actor {
    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    protected float damage = 1;
    protected float upForce = (float)3.0;
    protected float secondsToHit = (float) 0.5;
    protected float updatesToHit = secondsToHit/(float)60.0;
    protected Vector2 currentPosition, speed, gravity;
    protected Label label;
    protected float totalTime = 0;

    public Projectile(Vector2 position, Vector2 target, float damage)
    {
        super();
        this.damage = damage;
        currentPosition = position;
        speed = new Vector2((float)((target.x - position.x)*updatesToHit), (float)((target.y - position.y)*updatesToHit)+upForce);
//        upForce = new Vector2(0,5);
        gravity = new Vector2((float) 0.0,(float)(-upForce*(updatesToHit*2)));

        Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        label = new Label("*", uiSkin);
        label.setPosition(position.x, position.y);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
//        speed.x += upForce.x;
//        speed.y += upForce.y;
//        System.out.println("speed x: "+speed.x+" y: "+speed.y);
//        System.out.println("gravity x: "+gravity.x+" y: "+gravity.y);
        speed = new Vector2(speed.x + gravity.x, speed.y + gravity.y);
        if(speed.x >= 15) speed.x = 15;
        if(speed.x <= -15) speed.x = -15;
        if(speed.y >= 15) speed.y = 15;
        if(speed.y <= -15) speed.y = -15;
        currentPosition = new Vector2(currentPosition.x + speed.x, currentPosition.y + speed.y);

        label.setPosition(currentPosition.x,currentPosition.y);

        totalTime += delta;
        if(totalTime >= 2) {
//            destroy();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        label.draw(batch, parentAlpha);
    }

    public void destroy(){
        this.remove();
    }
}

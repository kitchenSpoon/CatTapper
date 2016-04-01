package com.jacklian.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

/**
 * Created by jacklian on 22/02/2016.
 */
public class ProjectileManager extends Actor {
    MonsterManager monsterManager;
    Array<Projectile> projectiles;

    public ProjectileManager() {
        super();
        projectiles = new Array<Projectile>();
    }

    public ProjectileManager(MonsterManager monsterManager) {
        this();
        this.monsterManager = monsterManager;
    }

    public void addProjectile(Projectile project) {
        projectiles.add(project);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Array<Projectile> remove = new Array<Projectile>();
        for(Projectile projectile: projectiles){
            //check collision
            if(collide(projectile, monsterManager.currentMonster)){
                monsterManager.currentMonster.takeDamage(projectile.getDamage());
                remove.add(projectile);
                projectile.destroy();
            }
        }
        projectiles.removeAll(remove, true);
    }

    public boolean collide(Projectile projectile, Monster monster){
        System.out.println("proj x: "+projectile.currentPosition.x+" , y: "+projectile.currentPosition.y);
        System.out.println("monster x: "+monster.x+" , y: "+monster.y+" x+width: "+(monster.x + monster.width)+" y+height: "+monster.y + monster.height);
        if (projectile.currentPosition.x > monster.x && projectile.currentPosition.x < monster.x + monster.width) {
            if (projectile.currentPosition.y > monster.y && projectile.currentPosition.y < monster.y + monster.height) {
                System.out.println("collide");
                return true;
            }
        }
        return false;
    }
}

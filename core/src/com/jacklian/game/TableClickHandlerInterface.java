package com.jacklian.game;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by jacklian on 13/03/2016.
 */
public interface  TableClickHandlerInterface {
    public void didClickItemOnRow(Integer row);
    public Actor actorForRow(Integer row);
    public Integer numberOfRows();
}

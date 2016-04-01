package com.jacklian.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jacklian.game.ActionResolver;
import com.jacklian.game.MyGdxGame;

public class DesktopLauncher implements ActionResolver {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new MyGdxGame(), config);
	}

	public void showOrLoadInterstital() {
	}
}

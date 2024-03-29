package com.groupc.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.groupc.game.scenes.ReadWriteFile;
import com.groupc.game.scenes.SceneSettings;

import java.io.IOException;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		config.backgroundFPS = 60;
		config.foregroundFPS = 0;
		config.resizable = false;
		config.addIcon("player/player1.png", Files.FileType.Internal);
		config.title = "MonkeMan and the Banana Rebellion";
		new LwjglApplication(new SceneSettings(), config);
	}
}

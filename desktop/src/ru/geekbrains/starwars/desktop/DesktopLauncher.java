package ru.geekbrains.starwars.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ru.geekbrains.starwars.Star2DGame;
import ru.geekbrains.starwars.StarWars;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 900;
		config.height = 400;
		new LwjglApplication(new Star2DGame(), config);
	}
}

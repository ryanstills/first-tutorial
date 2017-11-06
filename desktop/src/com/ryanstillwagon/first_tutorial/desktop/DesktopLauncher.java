package com.ryanstillwagon.first_tutorial.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.ryanstillwagon.first_tutorial.WizardEscape;

public class DesktopLauncher {
	public static void main(String[] arg) {
		Pack();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new WizardEscape(), "Escape from the Wizard's Keep"
				, 640, 480);
	}
	static void Pack() {
		TexturePacker.Settings settings = new TexturePacker.Settings();
		settings.maxHeight = 2048;
		settings.maxWidth = 2048;
		settings.pot = true;
		TexturePacker.process(settings, "unpacked", "packed", "game");
	}
}

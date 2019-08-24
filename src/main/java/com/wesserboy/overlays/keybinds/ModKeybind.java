package com.wesserboy.overlays.keybinds;

import com.wesserboy.overlays.Overlays;

import net.minecraft.client.settings.KeyBinding;

public abstract class ModKeybind {
	
	protected KeyBinding keyBinding;
	
	public ModKeybind(String name, int defaultKey){
		keyBinding = new KeyBinding("key." + Overlays.MODID + "." + name, defaultKey, "key.categories." + Overlays.MODID);
	}
	
	// Called by the ModKeybindManager when the key is pressed
	public abstract void onKeyPress();
	
}

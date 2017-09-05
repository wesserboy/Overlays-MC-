package com.wesserboy.overlays.keybinds;

import com.wesserboy.overlays.Overlays;

import net.minecraft.client.settings.KeyBinding;

public class ModKeybind {
	
	protected KeyBinding keyBinding;
	
	public ModKeybind(String name, int defaultKey){
		keyBinding = new KeyBinding("key." + Overlays.MODID + "." + name, defaultKey, "key.categories." + Overlays.MODID);
	}
	
	// Called from the keyInputHandler when the key is pressed
	public void onKeyPress(){}
	
}

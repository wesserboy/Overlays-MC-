package com.wesserboy.overlays.keybinds;

import java.util.ArrayList;

import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModKeybindManager {
	
	public static ModKeybindManager INSTANCE;
	
	
	private ArrayList<ModKeybind> keybinds = new ArrayList<ModKeybind>();
	
	public ModKeybindManager(){
		ModKeybindManager.INSTANCE = this;
	}
	
	public boolean addKeybind(ModKeybind keybind){
		if(!keybinds.contains(keybind)){
			keybinds.add(keybind);
			return true;
		}
		return false;
	}
	
	public void registerKeybinds(){
		for(ModKeybind keybind : keybinds){
			ClientRegistry.registerKeyBinding(keybind.keyBinding);
		}
	}
	
	@SubscribeEvent
	public void onKeyEvent(InputEvent.KeyInputEvent event){
		for(ModKeybind keybind : keybinds){
			if(keybind.keyBinding.isPressed()){
				keybind.onKeyPress();
			}
		}
	}

}

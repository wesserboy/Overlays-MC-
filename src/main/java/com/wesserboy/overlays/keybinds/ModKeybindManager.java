package com.wesserboy.overlays.keybinds;

import java.util.ArrayList;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

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

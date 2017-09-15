package com.wesserboy.overlays;

import org.lwjgl.input.Keyboard;

import com.wesserboy.overlays.keybinds.ModKeybind;
import com.wesserboy.overlays.keybinds.ModKeybindManager;
import com.wesserboy.overlays.renderers.BowAimHelp;
import com.wesserboy.overlays.renderers.ChunkOverlay;
import com.wesserboy.overlays.renderers.LightLevelOverlay;
import com.wesserboy.overlays.renderers.SlimeChunkOverlay;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = Overlays.MODID, 
	 name = Overlays.MODNAME, 
	 version = Overlays.MODVERSION,
	 clientSideOnly = true)
public class Overlays {
	
	public static final String MODID = "wesserboysoverlays";
	public static final String MODNAME = "Wesserboy's Overlays";
	public static final String MODVERSION = "1.0.0.1";
	
	@Instance
	public static Overlays INSTANCE;
	
	@EventHandler
	public static void init(FMLInitializationEvent event){
		MinecraftForge.EVENT_BUS.register(new ModKeybindManager());
		
		/** Chunk overlay **/
		MinecraftForge.EVENT_BUS.register(new ChunkOverlay());
		
		ModKeybindManager.INSTANCE.addKeybind(new ModKeybind("chunkoverlay", Keyboard.KEY_F9){
			@Override
			public void onKeyPress() {
				ChunkOverlay.INSTANCE.cycleMode();
			}
		});
		
		/** Light level overlay **/
		MinecraftForge.EVENT_BUS.register(new LightLevelOverlay());
		ModKeybindManager.INSTANCE.addKeybind(new ModKeybind("lightleveloverlay", Keyboard.KEY_F7){
			@Override
			public void onKeyPress() {
				LightLevelOverlay.INSTANCE.toggleState();
			}
		});
		
		/** Bow aim helper **/
		MinecraftForge.EVENT_BUS.register(new BowAimHelp());
		
		/** Slime chunk overlay **/
		MinecraftForge.EVENT_BUS.register(new SlimeChunkOverlay());
		ModKeybindManager.INSTANCE.addKeybind(new ModKeybind("slimechunkoverlay", Keyboard.KEY_F8){
			@Override
			public void onKeyPress() {
				SlimeChunkOverlay.INSTANCE.toggleState();
			}
		});
		
		
		ModKeybindManager.INSTANCE.registerKeybinds();
	}

}

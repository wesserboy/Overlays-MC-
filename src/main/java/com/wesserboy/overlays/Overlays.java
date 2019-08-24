package com.wesserboy.overlays;

import org.lwjgl.glfw.GLFW;

import com.wesserboy.overlays.config.ConfigHandler;
import com.wesserboy.overlays.keybinds.ModKeybind;
import com.wesserboy.overlays.keybinds.ModKeybindManager;
import com.wesserboy.overlays.renderers.BowAimHelp;
import com.wesserboy.overlays.renderers.ChunkOverlay;
import com.wesserboy.overlays.renderers.LightLevelOverlay;
import com.wesserboy.overlays.renderers.SlimeChunkOverlay;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Overlays.MODID)
public class Overlays {
	
	public static final String MODID = "wesserboysoverlays";
	
	public Overlays() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
		ConfigHandler.init();
	}
	
	private void init(FMLClientSetupEvent event){
		ConfigHandler.load();
		
		MinecraftForge.EVENT_BUS.register(new ModKeybindManager());
		
		/** Chunk overlay **/
		MinecraftForge.EVENT_BUS.register(new ChunkOverlay());
		
		ModKeybindManager.INSTANCE.addKeybind(new ModKeybind("chunkoverlay", GLFW.GLFW_KEY_F9){
			@Override
			public void onKeyPress() {
				ChunkOverlay.INSTANCE.cycleMode();
			}
		});
		
		/** Light level overlay **/
		MinecraftForge.EVENT_BUS.register(new LightLevelOverlay());
		ModKeybindManager.INSTANCE.addKeybind(new ModKeybind("lightleveloverlay", GLFW.GLFW_KEY_F7){
			@Override
			public void onKeyPress() {
				LightLevelOverlay.INSTANCE.toggleState();
			}
		});
		
		/** Bow aim helper **/
		MinecraftForge.EVENT_BUS.register(new BowAimHelp());
		
		/** Slime chunk overlay **/
		MinecraftForge.EVENT_BUS.register(new SlimeChunkOverlay());
		ModKeybindManager.INSTANCE.addKeybind(new ModKeybind("slimechunkoverlay", GLFW.GLFW_KEY_F8){
			@Override
			public void onKeyPress() {
				SlimeChunkOverlay.INSTANCE.toggleState();
			}
		});
		
		ModKeybindManager.INSTANCE.registerKeybinds();
	}

}

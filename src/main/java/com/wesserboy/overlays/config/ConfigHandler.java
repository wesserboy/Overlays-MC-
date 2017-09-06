package com.wesserboy.overlays.config;

import com.wesserboy.overlays.Overlays;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Overlays.MODID,
		name = Overlays.MODNAME)
public class ConfigHandler {
	
	@Config.Comment("Configures the bow aim overlay:\n0: Turned off\n1: Red X only\n2: Red X + Hit preview")
	@Config.RangeInt(min = 0, max = 2)
	public static int BowAssistMode = 2;
	
	
	@Mod.EventBusSubscriber(modid = Overlays.MODID)
	private static class EventHandler{
		
		@SubscribeEvent
		public static void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event){
			if(event.getModID().equals(Overlays.MODID)){
				ConfigManager.sync(Overlays.MODID, Config.Type.INSTANCE);
			}
		}
	}
	
}

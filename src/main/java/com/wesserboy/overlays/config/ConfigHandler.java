package com.wesserboy.overlays.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

// Based off of https://github.com/Vazkii/Neat/blob/master/src/main/java/vazkii/neat/NeatConfig.java
public class ConfigHandler {
	
	private static ConfigValue<Integer> v_bowAssistMode;
	
	public static int bowAssistMode = 2;
	
	public static void init() {
		// see net.minecraftforge.common.ForgeConfig
		Pair<General, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(General::new);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, specPair.getRight());
	}
	
	public static void load() {
		bowAssistMode = v_bowAssistMode.get();
	}
	
	private static class General {
		
		public General(ForgeConfigSpec.Builder builder) {
			builder.push("general");
			
			v_bowAssistMode = builder
					.comment("Configures the bow aim overlay:", "0: Turned off", "1: Red X only", "2: Red X + Hit preview")
					.defineInRange("bowAssistMode", bowAssistMode, 0, 2);
			
			builder.pop();
		}
		
	}
	
	/** Old config gui stuff, this has not yet been implemented in forge (not sure if it will be reimplemented, keep around in case it does)
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
	**/
	
}

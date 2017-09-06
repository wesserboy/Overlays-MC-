package com.wesserboy.overlays.helpers;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class ModRenderHelper {
	
	public static void translateToWorldCoords(float partialTicks){
		Entity player = Minecraft.getMinecraft().getRenderViewEntity();
		
		double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
		
		// translate draw origin to 0,0,0. This allows the use of world coords in draw calls.
		GL11.glTranslated(-x, -y, -z);
	}

}

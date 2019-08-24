package com.wesserboy.overlays.helpers;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;

public class ModRenderHelper {
	
	public static void translateToWorldCoords(float partialTicks){
		Vec3d camPos = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
		
		// translate draw origin to 0,0,0. This allows the use of world coords in draw calls.
		GL11.glTranslated(-camPos.x, -camPos.y, -camPos.z);
	}

}

package com.wesserboy.overlays.renderers;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;
import com.wesserboy.overlays.helpers.ModRenderHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChunkOverlay {
	
	public static ChunkOverlay INSTANCE;
	
	public ChunkOverlay(){
		ChunkOverlay.INSTANCE = this;
	}
	
	/**
	 * 0: No overlay
	 * 1: Just borders
	 * 2: Borders + mesh
	 **/
	private int mode = 0;
	
	public void cycleMode(){
		mode++;
		mode %= 3;
	}
	
	@SubscribeEvent
	public void renderOverlay(RenderWorldLastEvent event){
		if(mode > 0){
			GL11.glPushMatrix();
				
				Entity player = Minecraft.getInstance().getRenderViewEntity();
				
				ModRenderHelper.translateToWorldCoords(event.getPartialTicks());
				
				//translate draw origin to the center of the chunk the player is in
				GL11.glTranslated(player.chunkCoordX * 16 + 8, 0, player.chunkCoordZ * 16 + 8);
				
				GlStateManager.disableTexture();
				GlStateManager.lineWidth(1F);
				
				GL11.glBegin(GL11.GL_LINES);
				
				
				// Yellow line in the middle
				GL11.glColor3f(1F, 1F, 0F); 

				GL11.glVertex3d(0, 0, 0);
				GL11.glVertex3d(0, 256, 0);
				
				
				// Red lines on corners
				GL11.glColor3f(1F, 0F, 0F); 

				GL11.glVertex3d(-8, 0, -8);
				GL11.glVertex3d(-8, 256, -8);

				GL11.glVertex3d(8, 0, 8);
				GL11.glVertex3d(8, 256, 8);

				GL11.glVertex3d(-8, 0, 8);
				GL11.glVertex3d(-8, 256, 8);

				GL11.glVertex3d(8, 0, -8);
				GL11.glVertex3d(8, 256, -8);
				
				
				if(mode == 2){
					// Green mesh
					GL11.glColor3f(0F, 1F, 0F);
					
					int minY = (int) (((int) player.posY) - 5);
					int maxY = (int) (((int) player.posY) + 5);
					
					// Vertical
					for(int i = minY; i <= maxY; i++){

						GL11.glVertex3d(-8, i, -8);
						GL11.glVertex3d(-8, i, 8);

						GL11.glVertex3d(-8, i, 8);
						GL11.glVertex3d(8, i, 8);

						GL11.glVertex3d(8, i, 8);
						GL11.glVertex3d(8, i, -8);

						GL11.glVertex3d(8, i, -8);
						GL11.glVertex3d(-8, i, -8);
					}
					
					// Horizontal
					for(int i = 0; i < 8; i++){

						GL11.glVertex3d(-8, minY, -i);
						GL11.glVertex3d(-8, maxY, -i);

						GL11.glVertex3d(-i, minY, -8);
						GL11.glVertex3d(-i, maxY, -8);

						GL11.glVertex3d(8, minY, i);
						GL11.glVertex3d(8, maxY, i);

						GL11.glVertex3d(i, minY, 8);
						GL11.glVertex3d(i, maxY, 8);

						GL11.glVertex3d(8, minY, -i);
						GL11.glVertex3d(8, maxY, -i);

						GL11.glVertex3d(i, minY, -8);
						GL11.glVertex3d(i, maxY, -8);

						GL11.glVertex3d(-8, minY, i);
						GL11.glVertex3d(-8, maxY, i);

						GL11.glVertex3d(-i, minY, 8);
						GL11.glVertex3d(-i, maxY, 8);
						 
					}
				}
				
				GL11.glEnd();
				
				GlStateManager.enableTexture();
			
			GL11.glPopMatrix();
		}
	}

}

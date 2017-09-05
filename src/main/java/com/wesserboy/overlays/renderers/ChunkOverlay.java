package com.wesserboy.overlays.renderers;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
				
				Entity player = Minecraft.getMinecraft().getRenderViewEntity();
				
				double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
		        double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
		        double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();
				
				// translate draw origin to 0,0,0. This allows the use of world coords in draw calls.
				GL11.glTranslated(-x, -y, -z);
				
				GL11.glPushMatrix();
				
					//translate draw origin to the center of the chunk the player is in
					GL11.glTranslated(player.chunkCoordX * 16 + 8, 0, player.chunkCoordZ * 16 + 8);
					
					GlStateManager.disableTexture2D();
					GlStateManager.glLineWidth(1F);
					
					
					// Yellow line in the middle
					GL11.glColor3f(1F, 1F, 0F); 
				
					GL11.glBegin(GL11.GL_LINE_STRIP);
					GL11.glVertex3d(0, 0, 0);
					GL11.glVertex3d(0, 256, 0);
					GL11.glEnd();
					
					
					// Red lines on corners
					GL11.glColor3f(1F, 0F, 0F); 
					
					GL11.glBegin(GL11.GL_LINE_STRIP);
					GL11.glVertex3d(-8, 0, -8);
					GL11.glVertex3d(-8, 256, -8);
					GL11.glEnd();
					
					GL11.glBegin(GL11.GL_LINE_STRIP);
					GL11.glVertex3d(8, 0, 8);
					GL11.glVertex3d(8, 256, 8);
					GL11.glEnd();
					
					GL11.glBegin(GL11.GL_LINE_STRIP);
					GL11.glVertex3d(-8, 0, 8);
					GL11.glVertex3d(-8, 256, 8);
					GL11.glEnd();
					
					GL11.glBegin(GL11.GL_LINE_STRIP);
					GL11.glVertex3d(8, 0, -8);
					GL11.glVertex3d(8, 256, -8);
					GL11.glEnd();
					
					
					if(mode == 2){
						// Green mesh
						GL11.glColor3f(0F, 1F, 0F);
						
						int minY = (int) (y - 5);
						int maxY = (int) (y + 5);
						
						// Vertical
						for(int i = minY; i <= maxY; i++){
							GL11.glBegin(GL11.GL_LINE_STRIP);
							GL11.glVertex3d(-8, i, -8);
							GL11.glVertex3d(-8, i, 8);
							GL11.glEnd();
							
							GL11.glBegin(GL11.GL_LINE_STRIP);
							GL11.glVertex3d(-8, i, 8);
							GL11.glVertex3d(8, i, 8);
							GL11.glEnd();
							
							GL11.glBegin(GL11.GL_LINE_STRIP);
							GL11.glVertex3d(8, i, 8);
							GL11.glVertex3d(8, i, -8);
							GL11.glEnd();
							
							GL11.glBegin(GL11.GL_LINE_STRIP);
							GL11.glVertex3d(8, i, -8);
							GL11.glVertex3d(-8, i, -8);
							GL11.glEnd();
						}
						
						// Horizontal
						for(int i = 0; i < 8; i++){
							
							GL11.glBegin(GL11.GL_LINE_STRIP);
							GL11.glVertex3d(-8, minY, -i);
							GL11.glVertex3d(-8, maxY, -i);
							GL11.glEnd();
							
							GL11.glBegin(GL11.GL_LINE_STRIP);
							GL11.glVertex3d(-i, minY, -8);
							GL11.glVertex3d(-i, maxY, -8);
							GL11.glEnd();
							
							GL11.glBegin(GL11.GL_LINE_STRIP);
							GL11.glVertex3d(8, minY, i);
							GL11.glVertex3d(8, maxY, i);
							GL11.glEnd();
							
							GL11.glBegin(GL11.GL_LINE_STRIP);
							GL11.glVertex3d(i, minY, 8);
							GL11.glVertex3d(i, maxY, 8);
							GL11.glEnd();
							
							GL11.glBegin(GL11.GL_LINE_STRIP);
							GL11.glVertex3d(8, minY, -i);
							GL11.glVertex3d(8, maxY, -i);
							GL11.glEnd();
							
							GL11.glBegin(GL11.GL_LINE_STRIP);
							GL11.glVertex3d(i, minY, -8);
							GL11.glVertex3d(i, maxY, -8);
							GL11.glEnd();
							
							GL11.glBegin(GL11.GL_LINE_STRIP);
							GL11.glVertex3d(-8, minY, i);
							GL11.glVertex3d(-8, maxY, i);
							GL11.glEnd();
							
							GL11.glBegin(GL11.GL_LINE_STRIP);
							GL11.glVertex3d(-i, minY, 8);
							GL11.glVertex3d(-i, maxY, 8);
							GL11.glEnd();
							 
						}
					}
					
					GlStateManager.enableTexture2D();
				
				GL11.glPopMatrix();
			
			GL11.glPopMatrix();
		}
	}

}

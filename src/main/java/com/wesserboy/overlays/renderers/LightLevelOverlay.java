package com.wesserboy.overlays.renderers;

import org.lwjgl.opengl.GL11;

import com.wesserboy.overlays.helpers.ModRenderHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LightLevelOverlay {
	
	public static LightLevelOverlay INSTANCE;
	
	public LightLevelOverlay(){
		LightLevelOverlay.INSTANCE = this;
	}
	
	public void toggleState(){
		this.state = !state;
	}
	
	private boolean state = false;
	
	@SubscribeEvent
	public void renderOverlay(RenderWorldLastEvent event){
		if(state){
			GL11.glPushMatrix();
			
			Entity player = Minecraft.getMinecraft().getRenderViewEntity();
			World world = player.world;
			
			ModRenderHelper.translateToWorldCoords(event.getPartialTicks());
			
			GlStateManager.disableTexture2D();
			GlStateManager.glLineWidth(1F);
			
			GL11.glBegin(GL11.GL_LINES);
			
			int xi = (int) player.posX;
			int yi = (int) player.posY;
			int zi = (int) player.posZ;
			
			for(int px = xi - 16; px <= xi + 16; px++){
				for(int pz = zi - 16; pz <= zi + 16; pz++){
					for(int py = yi - 16; py <= Math.min(yi + 16, world.getHeight(px, pz) + 1); py++){
						BlockPos pos = new BlockPos(px, py, pz);
						
						// Check if this block + the block above are air
						if(world.getBlockState(pos).getBlock().isAir(world.getBlockState(pos), world, pos) && world.getBlockState(pos.up()).getBlock().isAir(world.getBlockState(pos.up()), world, pos.up())){
							// Check if the block below is solid on the top
							if(world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.UP)){
								
								// Check the light level from nearby blocks
								if(world.getLightFor(EnumSkyBlock.BLOCK, pos) < 8){
									// Check sky light
									if(world.getLightFor(EnumSkyBlock.SKY, pos) < 8){
										// draw red X	
										GL11.glColor3f(1F, 0F, 0F);
									}else{
										// draw yellow X
										GL11.glColor3f(1F, 1F, 0F);
									}
									
									float ry = py + 0.001F;
									
									GL11.glVertex3f(px, ry, pz);
									GL11.glVertex3f(px + 1, ry, pz + 1);
									GL11.glVertex3f(px + 1, ry, pz);
									GL11.glVertex3f(px, ry, pz + 1);
									
								}
								
							}
						}
					}
				}
			}
			
			GL11.glEnd();
			
			GlStateManager.enableTexture2D();
			
			GL11.glPopMatrix();
		}
	}

}

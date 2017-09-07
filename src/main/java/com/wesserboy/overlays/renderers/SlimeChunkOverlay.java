package com.wesserboy.overlays.renderers;

import org.lwjgl.opengl.GL11;

import com.wesserboy.overlays.helpers.ModRenderHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class SlimeChunkOverlay {
	
	public static SlimeChunkOverlay INSTANCE;
	
	public SlimeChunkOverlay(){
		INSTANCE = this;
	}
	
	private float rotation;
	
	private float yOff = 0;
	private float yOffDirection = 1;
	
	private boolean state = false;
	
	public void toggleState(){
		this.state = !this.state;
	}
	
	@SubscribeEvent
	public void render(RenderWorldLastEvent event){
		if(this.state){
			Minecraft mc = Minecraft.getMinecraft();
			Entity player = mc.getRenderViewEntity();
			
			int cChunkX = player.chunkCoordX;
			int cChunkZ = player.chunkCoordZ;
			
			int radius = Minecraft.getMinecraft().gameSettings.renderDistanceChunks;
			
			int minX = cChunkX - radius;
			int maxX = cChunkX + radius;
			int minZ = cChunkZ - radius;
			int maxZ = cChunkZ + radius;
			
			for(int x = minX; x <= maxX; x++){
				for(int z = minZ; z <= maxZ; z++){
					Chunk chunk = player.world.getChunkFromChunkCoords(x, z);
					
					// See EntitySlime.getCanSpawnHere
					if(chunk.getRandomWithSeed(987234911L).nextInt(10) == 0){
						
						EntitySlime fakeSlime = new EntitySlime(player.world);
						fakeSlime.prevRotationYawHead = fakeSlime.rotationYawHead = 0;
						setSlimeSize(fakeSlime, 3);
						
						GlStateManager.pushMatrix();
						
							GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
					        GlStateManager.disableTexture2D();
					        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
						
							ModRenderHelper.translateToWorldCoords(event.getPartialTicks());
							GlStateManager.translate(x * 16 + 8, 0, z * 16 + 8); // translate to the center of the chunk
							
							
							double pY = player.prevPosY + (player.posY - player.prevPosY) * event.getPartialTicks();
							GlStateManager.translate(0, pY + 2 + yOff, 0);
							GlStateManager.rotate(rotation, 0F, 1F, 0F);
							
							GlStateManager.color(1F, 1F, 1F, 0.15F);
							
							GlStateManager.enableBlend();
							mc.getRenderManager().doRenderEntity(fakeSlime, 0, 0, 0, 0F, event.getPartialTicks(), false);
							
							
						GlStateManager.popMatrix();
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event){
		if(event.phase == TickEvent.Phase.START && this.state){
			rotation += 2F;
			
			yOff += 0.01 * yOffDirection / Math.max(Math.abs(yOff), 0.6F);
			if(yOff < -1 || yOff > 1){
				yOffDirection = -yOffDirection;
			}
		}
	}
	
	private void setSlimeSize(EntitySlime fakeSlime, int size){
		NBTTagCompound fakeNbt = new NBTTagCompound();
		fakeNbt.setInteger("Size", size);
		fakeSlime.readEntityFromNBT(fakeNbt);
	}

}

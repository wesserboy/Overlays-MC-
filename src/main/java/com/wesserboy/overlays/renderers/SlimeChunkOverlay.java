package com.wesserboy.overlays.renderers;

import java.util.ArrayList;
import java.util.Comparator;

import com.mojang.blaze3d.platform.GlStateManager;
import com.wesserboy.overlays.helpers.ModRenderHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
			Minecraft mc = Minecraft.getInstance();
			Entity player = mc.getRenderViewEntity();
			
			if(mc.getIntegratedServer() != null){
				ArrayList<Vec3d> positions = calcPositions(mc, player);
						
				SlimeEntity fakeSlime = new SlimeEntity(EntityType.SLIME, player.world);
				fakeSlime.prevRotationYawHead = fakeSlime.rotationYawHead = 0;
				setSlimeSize(fakeSlime, 3);
				
				for(Vec3d pos : positions) {
					GlStateManager.pushMatrix();
					
						ModRenderHelper.translateToWorldCoords(event.getPartialTicks());
						GlStateManager.translated(pos.x, pos.y, pos.z);
						
						
						double pY = player.getEyePosition(event.getPartialTicks()).y;
						
						GlStateManager.translatef(0, (float) (pY - (fakeSlime.getHeight() / 2) + yOff), 0);
						GlStateManager.rotatef(rotation, 0F, 1F, 0F);
						
						GlStateManager.color4f(1F, 1F, 1F, 0.5F);
						
						GlStateManager.enableBlend();
						mc.getRenderManager().renderEntity(fakeSlime, 0, 0, 0, 0F, event.getPartialTicks(), false);
					    
					    mc.gameRenderer.disableLightmap();
						
						
					GlStateManager.popMatrix();
				}
			}else{
				if(player instanceof PlayerEntity){
					((PlayerEntity) player).sendStatusMessage(new TranslationTextComponent("message.slimechunk.multiplayer"), true);
					this.state = false;
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
	
	/***
	 * Calculates the positions of the slimes to be rendered, and sorts them according to their distance from the player
	 * @return a sorted list of the positions
	 */
	private ArrayList<Vec3d> calcPositions(Minecraft mc, Entity player){
		ArrayList<Vec3d> positions = new ArrayList<Vec3d>();
		
		int cChunkX = player.chunkCoordX;
		int cChunkZ = player.chunkCoordZ;
		
		int radius = mc.gameSettings.renderDistanceChunks;
		
		int minX = cChunkX - radius;
		int maxX = cChunkX + radius;
		int minZ = cChunkZ - radius;
		int maxZ = cChunkZ + radius;
		
		World world = mc.getIntegratedServer().getWorld(player.world.getDimension().getType());
		
		for(int x = minX; x <= maxX; x++){
			for(int z = minZ; z <= maxZ; z++){
				// See SlimeEntity#func_223366_c
				if(SharedSeedRandom.seedSlimeChunk(x, z, world.getSeed(), 987234911L).nextInt(10) == 0){
					positions.add(new Vec3d(x * 16 + 8, 0, z * 16 + 8)); // center of the chunk
				}
			}
		}
		
		// the list has to be sorted in order for the alpha blending to work properly, the objects farthest in the scene should be rendered first.
		positions.sort(new Comparator<Vec3d>() {

			@Override
			public int compare(Vec3d o1, Vec3d o2) {
				double dist1sq = player.getDistanceSq(o1);
				double dist2sq = player.getDistanceSq(o2);
				
				return (int) (dist2sq - dist1sq);
			}
			
		});
		
		return positions;
	}
	
	private void setSlimeSize(SlimeEntity fakeSlime, int size){
		CompoundNBT fakeNbt = new CompoundNBT();
		fakeNbt.putInt("Size", size);
		fakeSlime.read(fakeNbt);
	}

}

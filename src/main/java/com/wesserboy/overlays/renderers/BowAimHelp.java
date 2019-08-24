package com.wesserboy.overlays.renderers;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;
import com.wesserboy.overlays.config.ConfigHandler;
import com.wesserboy.overlays.entities.EntityDummyArrow;
import com.wesserboy.overlays.helpers.ModRenderHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPartEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.BowItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

public class BowAimHelp {
	
	private EntityDummyArrow theArrow;
	
	@SubscribeEvent
	public void render(RenderWorldLastEvent event){
		if(ConfigHandler.bowAssistMode > 0 && theArrow != null){
			RayTraceResult hit = theArrow.getHit();
			
			GL11.glPushMatrix();
			
			ModRenderHelper.translateToWorldCoords(event.getPartialTicks());
			
			GlStateManager.disableTexture();
			GlStateManager.lineWidth(1F);
			
			double xOff1 = 0D;
			double xOff2 = 0D;
			double yOff1 = 0D;
			double yOff2 = 0D;
			double zOff1 = 0D;
			double zOff2 = 0D;
			
			BlockRayTraceResult theActHit;
			if(hit.getType() == RayTraceResult.Type.BLOCK){
				theActHit = (BlockRayTraceResult) hit;
			}else{
				EntityRayTraceResult entityHit = (EntityRayTraceResult) hit;
				List<AxisAlignedBB> box = new ArrayList<AxisAlignedBB>();
				box.add(entityHit.getEntity().getBoundingBox());
				theActHit = AxisAlignedBB.rayTrace(box, theArrow.getPath()[theArrow.getPath().length - 1], theArrow.getPositionVec(), new BlockPos(0, 0, 0));
			};
			
			if(theActHit != null){ // This should not be possible, however the ender dragon sometimes manages to do this...
				switch(theActHit.getFace().getAxis()){
				case X:
					xOff1 = xOff2 = 0.01 * theActHit.getFace().getAxisDirection().getOffset();
					
					yOff1 = 0.1D;
					yOff2 = -0.1D;
					
					zOff1 = 0.1D;
					zOff2 = -0.1D;
					break;
				case Y:
					xOff1 = 0.1D;
					xOff2 = -0.1D;
					
					yOff1 = yOff2 = 0.01 * theActHit.getFace().getAxisDirection().getOffset();
					
					zOff1 = 0.1D;
					zOff2 = -0.1D;
					break;
				case Z:
					xOff1 = 0.1D;
					xOff2 = -0.1D;
					
					yOff1 = 0.1D;
					yOff2 = -0.1D;
					
					zOff1 = zOff2 = 0.01 * theActHit.getFace().getAxisDirection().getOffset();
					break;
				}
				
				GL11.glBegin(GL11.GL_LINES);
				
				GL11.glColor3f(1F, 0F, 0F);
				
				Vec3d end = theActHit.getHitVec();
				
				GL11.glVertex3d(end.x + xOff1, end.y + yOff1, end.z + zOff1);
				GL11.glVertex3d(end.x + xOff2, end.y + yOff2, end.z + zOff2);
				
				GL11.glVertex3d(end.x + xOff1, end.y + yOff2, end.z + zOff2);
				GL11.glVertex3d(end.x + xOff2, end.y + yOff1, end.z + zOff1);
				
				GL11.glVertex3d(end.x + xOff2, end.y + yOff2, end.z + zOff1);
				GL11.glVertex3d(end.x + xOff1, end.y + yOff1, end.z + zOff2);
				
				GL11.glEnd();
			}
			
			GlStateManager.enableTexture();
			
			GL11.glPopMatrix();
		}
	}
	
	@SubscribeEvent
	public void renderAdvanced(RenderGameOverlayEvent.Post event){
		if(ConfigHandler.bowAssistMode > 1 && theArrow != null){
			if(event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR){
				Minecraft mc = Minecraft.getInstance();
				Entity player = mc.getRenderViewEntity();
				
				RayTraceResult hit = theArrow.getHit();
				Entity target = null;
				if(hit instanceof EntityRayTraceResult) {
					target = ((EntityRayTraceResult) hit).getEntity();
				}
				
				if(target instanceof EnderDragonPartEntity){
					EnderDragonPartEntity part = (EnderDragonPartEntity) target;
					EnderDragonEntity dragon = part.dragon;
					if(dragon instanceof Entity){
						target = (Entity) dragon;
					}
				}
				
				if(target == null){
					target = new FallingBlockEntity(player.world, 0, 0, 0, player.world.getBlockState(((BlockRayTraceResult) theArrow.getHit()).getPos()));
					target.setPosition(0, 300, 0);
				}
				
				GlStateManager.pushMatrix();
				
				GlStateManager.enableColorMaterial();
				
				GlStateManager.translatef(30F, mc.mainWindow.getScaledHeight() - 15F, 50F);
				GlStateManager.scalef((float)(-30), (float)30, (float)30);
				
				GlStateManager.rotatef(-player.rotationPitch, 1, 0, 0);
				GlStateManager.rotatef(180 - player.getRotationYawHead(), 0, 1, 0);
				
				GlStateManager.rotatef(180, 0, 0, 1);
				
				mc.getRenderManager().setRenderShadow(false);
				RenderHelper.enableStandardItemLighting();
				
				
				
				ArrowEntity fakeArrow = new ArrowEntity(EntityType.ARROW, player.world);
				fakeArrow.rotationPitch = theArrow.rotationPitch;
				fakeArrow.rotationYaw = theArrow.rotationYaw;
				
				
				if(target != null){
					// Render the target
					GlStateManager.enableAlphaTest();
					GlStateManager.enableBlend();
					GlStateManager.enableNormalize();
					
					// Scale down entities that are too large (looking at you ender dragon >:( )
					AxisAlignedBB renderBox = target.getRenderBoundingBox();
					double scale = renderBox.getAverageEdgeLength() > 1.5D ? 1.5D / renderBox.getAverageEdgeLength() : 1D;
					GlStateManager.scaled(scale, scale, scale);
					
					if(!(target instanceof FallingBlockEntity)){
						mc.getRenderManager().renderEntity(target, 0F, 0F, 0F, 0F, 1F, false);
					}else{
						FallingBlockEntity blockEntity = (FallingBlockEntity) target;
						BlockState state = blockEntity.getBlockState();
						Block block = state.getBlock();
						
						if(block.hasTileEntity(state)){
							if(state.getRenderType() == BlockRenderType.MODEL){
								mc.getRenderManager().renderEntity(target, 0F, 0F, 0F, 0F, 1F, false);
							}
							TileEntity tile = player.world.getTileEntity(((BlockRayTraceResult) theArrow.getHit()).getPos());
							if(tile != null){
								if(TileEntityRendererDispatcher.instance.getRenderer(tile) != null){
									TileEntityRendererDispatcher.instance.render(tile, -0.5D, 0D, -0.5D, event.getPartialTicks());
									GlStateManager.disableFog();
								}
							}
						}else{
							mc.getRenderManager().renderEntity(target, 0F, 0F, 0F, 0F, 1F, false);
						}
					}
					
					if(!(target instanceof FallingBlockEntity)){
						// Get the position where the target was hit
						Vec3d[] path = theArrow.getPath();
						
						EntityRayTraceResult entityHit = (EntityRayTraceResult) hit;
						List<AxisAlignedBB> box = new ArrayList<AxisAlignedBB>();
						box.add(entityHit.getEntity().getBoundingBox());
						BlockRayTraceResult actHitCoords = AxisAlignedBB.rayTrace(box, path[path.length - 1], theArrow.getPositionVec(), new BlockPos(0, 0, 0));
						
						if(actHitCoords != null){ // This should not be possible, however the ender dragon sometimes manages to do this...
							// Draw the arrow at that location
							GlStateManager.translated(-(target.posX - actHitCoords.getHitVec().x), -(target.posY - actHitCoords.getHitVec().y), -(target.posZ - actHitCoords.getHitVec().z));
						}
					}else{
						BlockPos pos = ((BlockRayTraceResult) hit).getPos();
						GlStateManager.translatef(-0.5f, 0, -0.5f);
						GlStateManager.translated(-(pos.getX() - hit.getHitVec().x), -(pos.getY() - hit.getHitVec().y), -(pos.getZ() - hit.getHitVec().z));
					}
					mc.getRenderManager().renderEntity(fakeArrow, 0F, 0F, 0F, 0F, 1F, false);
				}
				
				
				GlStateManager.disableAlphaTest();
				GlStateManager.disableBlend();
				GlStateManager.disableNormalize();
				
				mc.getRenderManager().setRenderShadow(true);
				RenderHelper.disableStandardItemLighting();
				
		        mc.gameRenderer.disableLightmap();

				GlStateManager.popMatrix();
			}
		}
	}
	
	private boolean shouldTrack = false;
	
	@SubscribeEvent
	public void onStartUsingBow(ArrowNockEvent event){
		if(event.getWorld().isRemote){
			shouldTrack = true;
		}
	}
	
	@SubscribeEvent
	public void onStopUsingBow(ArrowLooseEvent event){
		if(event.getWorld().isRemote){
			shouldTrack = false;
			theArrow = null;
		}
	}
	
	@SubscribeEvent
	public void onPlayerTickEnd(PlayerTickEvent event){
		if(event.side == LogicalSide.CLIENT && event.phase == TickEvent.Phase.END){
			
			if(shouldTrack){
				PlayerEntity player = Minecraft.getInstance().player;
				World world = player.world;
				
				// Adapted from BowItem#onPlayerStoppedUsing
				int i = 72000 - player.getItemInUseCount();
				
				float f = BowItem.getArrowVelocity(i);
				
		        EntityDummyArrow arrow = new EntityDummyArrow(world, player);
		        arrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3.0F, 0.0F);
		        
		        if(arrow.getHit() != null){
		        	theArrow = arrow;
		        }else{
		        	theArrow = null;
		        }
		        
		        // Safety check for if the player changes to a different slot without releasing the mouse button
		        if(!(player.getActiveItemStack().getItem() instanceof BowItem)){
		        	shouldTrack = false;
					theArrow = null;
		        }
			}
		}
	}

}

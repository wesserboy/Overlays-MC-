package com.wesserboy.overlays.renderers;

import org.lwjgl.opengl.GL11;

import com.wesserboy.overlays.entities.EntityDummyArrow;
import com.wesserboy.overlays.helpers.ModRenderHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class BowAimHelp {
	
	private EntityDummyArrow theArrow;
	
	@SubscribeEvent
	public void render(RenderWorldLastEvent event){
		if(theArrow != null){
			RayTraceResult hit = theArrow.getHit();
			
			GL11.glPushMatrix();
			
				ModRenderHelper.translateToWorldCoords(event.getPartialTicks());
				
				GlStateManager.disableTexture2D();
				GlStateManager.glLineWidth(1F);
				
				GL11.glBegin(GL11.GL_LINES);
				
				GL11.glColor3f(1F, 0F, 0F);
				
				double xOff1 = 0D;
				double xOff2 = 0D;
				double yOff1 = 0D;
				double yOff2 = 0D;
				double zOff1 = 0D;
				double zOff2 = 0D;
				
				RayTraceResult theActHit;
				if(hit.typeOfHit == RayTraceResult.Type.BLOCK){
					theActHit = hit;
				}else{
					Entity target = hit.entityHit;
					// Get the position where the target was hit
					Vec3d[] path = theArrow.getPath();
					// Based on EntityArrow.findEntityOnPath
					theActHit = target.getEntityBoundingBox().grow(0.30000001192092896D).calculateIntercept(path[path.length - 1], theArrow.getPositionVector());
				};
				
				switch(theActHit.sideHit.getAxis()){
				case X:
					xOff1 = xOff2 = 0.01 * theActHit.sideHit.getAxisDirection().getOffset();
					
					yOff1 = 0.1D;
					yOff2 = -0.1D;
					
					zOff1 = 0.1D;
					zOff2 = -0.1D;
					break;
				case Y:
					xOff1 = 0.1D;
					xOff2 = -0.1D;
					
					yOff1 = yOff2 = 0.01 * theActHit.sideHit.getAxisDirection().getOffset();
					
					zOff1 = 0.1D;
					zOff2 = -0.1D;
					break;
				case Z:
					xOff1 = 0.1D;
					xOff2 = -0.1D;
					
					yOff1 = 0.1D;
					yOff2 = -0.1D;
					
					zOff1 = zOff2 = 0.01 * theActHit.sideHit.getAxisDirection().getOffset();
					break;
				}
				
				Vec3d end = theActHit.hitVec;
				
				GL11.glVertex3d(end.x + xOff1, end.y + yOff1, end.z + zOff1);
				GL11.glVertex3d(end.x + xOff2, end.y + yOff2, end.z + zOff2);
				
				GL11.glVertex3d(end.x + xOff1, end.y + yOff2, end.z + zOff2);
				GL11.glVertex3d(end.x + xOff2, end.y + yOff1, end.z + zOff1);
				
				GL11.glVertex3d(end.x + xOff2, end.y + yOff2, end.z + zOff1);
				GL11.glVertex3d(end.x + xOff1, end.y + yOff1, end.z + zOff2);
				
				GL11.glEnd();
				
				GlStateManager.enableTexture2D();
			
			GL11.glPopMatrix();
		}
	}
	
	@SubscribeEvent
	public void renderAdvanced(RenderGameOverlayEvent.Post event){
		if(theArrow != null){
			if(event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR){
				Minecraft mc = Minecraft.getMinecraft();
				Entity player = mc.getRenderViewEntity();
				
				RayTraceResult hit = theArrow.getHit();
				Entity target = hit.entityHit;
				
				if(target == null){
					target = new EntityFallingBlock(player.world, 0, 0, 0, player.world.getBlockState(theArrow.getHit().getBlockPos()));
				}
				
				GlStateManager.enableColorMaterial();
				GlStateManager.pushMatrix();
				
					GlStateManager.translate(25F, event.getResolution().getScaledHeight_double() - 10, 50F);
					GlStateManager.scale((float)(-30), (float)30, (float)30);
					
					GlStateManager.rotate(-player.rotationPitch, 1, 0, 0);
					GlStateManager.rotate(180 - player.getRotationYawHead(), 0, 1, 0);
					
					GlStateManager.rotate(180, 0, 0, 1);
					
					mc.getRenderManager().setRenderShadow(false);
					RenderHelper.enableStandardItemLighting();
					
					
					
					EntityArrow fakeArrow = new EntityTippedArrow(player.world);
					fakeArrow.rotationPitch = theArrow.rotationPitch;
					fakeArrow.rotationYaw = theArrow.rotationYaw;
					
					
					if(target != null){
						// Render the target
						mc.getRenderManager().doRenderEntity(target, 0F, 0F, 0F, 0F, 1F, false);
						
						if(!(target instanceof EntityFallingBlock)){
							// Get the position where the target was hit
							Vec3d[] path = theArrow.getPath();
							// Based on EntityArrow.findEntityOnPath
							RayTraceResult actHitCoords = target.getEntityBoundingBox().grow(0.30000001192092896D).calculateIntercept(path[path.length - 1], theArrow.getPositionVector());
							
							// Draw the arrow at that location
							GlStateManager.translate(-(target.posX - actHitCoords.hitVec.x), -(target.posY - actHitCoords.hitVec.y), -(target.posZ - actHitCoords.hitVec.z));
						}else{
							GlStateManager.enableAlpha();
							
							BlockPos pos = hit.getBlockPos();
							GlStateManager.translate(-0.5, 0, -0.5);
							GlStateManager.translate(-(pos.getX() - hit.hitVec.x), -(pos.getY() - hit.hitVec.y), -(pos.getZ() - hit.hitVec.z));
						}
						mc.getRenderManager().doRenderEntity(fakeArrow, 0F, 0F, 0F, 0F, 1F, false);
					}
					
					mc.getRenderManager().setRenderShadow(true);
					RenderHelper.disableStandardItemLighting();
					
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
		if(event.side == Side.CLIENT && event.phase == TickEvent.Phase.END){
			
			if(shouldTrack){
				EntityPlayer player = Minecraft.getMinecraft().player;
				World world = player.world;
				
				// Adapted from ItemBow.onPlayerStoppedUsing
				int i = 72000 - player.getItemInUseCount();
				
				float f = ItemBow.getArrowVelocity(i);
				
		        EntityDummyArrow arrow = new EntityDummyArrow(world, player);
		        arrow.setAim(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3.0F, 0.0F);
		        
		        theArrow = arrow;
		        
		        // Safety check for if the player changes to a different slot without releasing the mouse button
		        if(!(player.getActiveItemStack().getItem() instanceof ItemBow)){
		        	shouldTrack = false;
					theArrow = null;
		        }
			}
		}
	}

}

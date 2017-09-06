package com.wesserboy.overlays.entities;

import java.util.ArrayList;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityDummyArrow extends EntityArrow{

	public EntityDummyArrow(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
		
		this.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
	}

	@Override
	protected ItemStack getArrowStack() {
		return null;
	}
	
	private boolean hasHit = false;
	
	@Override
	protected void onHit(RayTraceResult result) {
		hasHit = true;
		this.hitResult = result;
	}
	
	private void calcPath(){
		ArrayList<Vec3d> path = new ArrayList<Vec3d>();
		
		while(!this.hasHit){
			Vec3d pos = new Vec3d(this.posX, this.posY, this.posZ);
			path.add(pos);
			this.onUpdate();
		}
		
		this.path =  path.toArray(new Vec3d[path.size()]);
	}
	
	private Vec3d[] path;
	private RayTraceResult hitResult;
	
	public Vec3d[] getPath(){
		if(this.path == null){
			calcPath();
		}
		
		return this.path;
	}
	
	public RayTraceResult getHit(){
		if(this.hitResult == null){
			this.calcPath();
		}
		
		return this.hitResult;
	}

}

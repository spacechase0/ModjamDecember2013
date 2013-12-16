package com.spacechase0.minecraft.endertech.world;

import com.spacechase0.minecraft.endertech.entity.VehicleEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IWorldAccess;

public class HelperWorldAccess implements IWorldAccess
{
	public HelperWorldAccess( VehicleEntity theEntity, FakeWorld theWorld )
	{
		entity = theEntity;
		world = theWorld;
	}
	
	@Override
	public void markBlockForUpdate( int x, int y, int z )
	{
		entity.sync( x, y, z );
	}

	@Override
	public void markBlockForRenderUpdate(int i, int j, int k) {
		// TODO Auto-generated method stub

	}

	@Override
	public void markBlockRangeForRenderUpdate(int i, int j, int k, int l,
			int i1, int j1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playSound(String s, double d0, double d1, double d2, float f,
			float f1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playSoundToNearExcept(EntityPlayer entityplayer, String s,
			double d0, double d1, double d2, float f, float f1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void spawnParticle(String s, double d0, double d1, double d2,
			double d3, double d4, double d5) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEntityCreate(Entity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEntityDestroy(Entity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playRecord(String s, int i, int j, int k) {
		// TODO Auto-generated method stub

	}

	@Override
	public void broadcastSound(int i, int j, int k, int l, int i1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playAuxSFX(EntityPlayer entityplayer, int i, int j, int k,
			int l, int i1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroyBlockPartially(int i, int j, int k, int l, int i1) {
		// TODO Auto-generated method stub

	}
	
	private VehicleEntity entity;
	private FakeWorld world;
}

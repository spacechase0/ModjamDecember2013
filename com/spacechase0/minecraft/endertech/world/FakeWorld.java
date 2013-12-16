package com.spacechase0.minecraft.endertech.world;

import com.spacechase0.minecraft.endertech.entity.VehicleEntity;

import net.minecraft.entity.Entity;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;

public class FakeWorld extends World
{
	public FakeWorld( VehicleEntity theEntity )
	{
		super( new FakeSaveHandler(), "Vehicle" + theEntity.entityId, new FakeWorldProvider(), new WorldSettings( 0, EnumGameType.NOT_SET, false, false, WorldType.FLAT ), new Profiler(), new FakeLogAgent() );
		entity = theEntity;
	}
	
	@Override
	protected IChunkProvider createChunkProvider()
	{
		if ( chunkProvider == null )
		{
			chunkProvider = new FakeChunkProvider( this, entity );
		}
		
		return chunkProvider;
	}

	@Override
	public Entity getEntityByID( int id )
	{
		return entity.worldObj.getEntityByID( id );
	}
	
	private VehicleEntity entity;
}

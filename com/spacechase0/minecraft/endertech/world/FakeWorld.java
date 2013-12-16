package com.spacechase0.minecraft.endertech.world;

import com.spacechase0.minecraft.endertech.entity.VehicleEntity;
import com.spacechase0.minecraft.endertech.tileentity.VehicleTileEntity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
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
	
	public void loadFrom( VehicleTileEntity vehicle )
	{
		Chunk chunk = createChunkProvider().provideChunk( 0, 0 );
		
		int size = vehicle.getSize();
		for ( int ix = 0; ix < size; ++ix )
		{
			for ( int iy = 0; iy < size; ++iy )
			{
				for ( int iz = 0; iz < size; ++iz )
				{
					int index = ix + ( iy * size ) + ( iz * size * size );
					
					short data = vehicle.getBlockData()[ index ];
					int id = vehicle.getBlockId( data );
					int meta = vehicle.getBlockMeta( data );
					TileEntity te = vehicle.getBlockTiles()[ index ];
					
					chunk.setBlockIDWithMetadata( ix, iy, iz, id, meta );
					if ( te != null )
					{
						// Just in case
						te.xCoord = ix;
						te.yCoord = iy;
						te.zCoord = iz;
						
						chunk.addTileEntity( te );
					}
				}
			}
		}
	}
	
	public void loadFrom( NBTTagCompound tag )
	{
		VehicleTileEntity te = new VehicleTileEntity();
		VehicleTileEntity.fromFakeWorld = false;
		te.readFromNBT( tag );
		VehicleTileEntity.fromFakeWorld = true;
		
		loadFrom( te );
	}
	
	public void saveTo( NBTTagCompound tag )
	{
		int size = tag.getInteger( "Size" );
		
		VehicleTileEntity te = new VehicleTileEntity();
		VehicleTileEntity.fromFakeWorld = false;
		te.setVehicle( 0, 0, 0, size, size, size );
		te.writeToNBT( tag );
		VehicleTileEntity.fromFakeWorld = true;
	}
	
	private VehicleEntity entity;
}

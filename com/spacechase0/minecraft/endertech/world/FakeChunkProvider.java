package com.spacechase0.minecraft.endertech.world;

import java.util.List;

import com.spacechase0.minecraft.endertech.entity.VehicleEntity;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

public class FakeChunkProvider implements IChunkProvider
{
	public FakeChunkProvider( FakeWorld theWorld, VehicleEntity theEntity )
	{
		world = theWorld;
		entity = theEntity;
		
		chunk = new Chunk( theWorld, 0, 0 );
	}
	
	@Override
	public boolean chunkExists( int x, int z )
	{
		return ( x == 0 && z == 0 );
	}

	@Override
	public Chunk provideChunk( int x, int z )
	{
		if ( chunkExists( x, z ) ) return chunk;
		return null;
	}

	@Override
	public Chunk loadChunk( int x, int z )
	{
		if ( chunkExists( x, z ) ) return chunk;
		return null;
	}

	@Override
	public void populate( IChunkProvider ichunkprovider, int x, int z )
	{
	}

	@Override
	public boolean saveChunks( boolean flag, IProgressUpdate iprogressupdate )
	{
		return false;
	}

	@Override
	public boolean unloadQueuedChunks()
	{
		return false;
	}

	@Override
	public boolean canSave()
	{
		return false;
	}

	@Override
	public String makeString()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getPossibleCreatures( EnumCreatureType enumcreaturetype, int i, int j, int k )
	{
		return null;
	}

	@Override
	public ChunkPosition findClosestStructure( World world, String s, int x, int y, int z )
	{
		return null;
	}

	@Override
	public int getLoadedChunkCount()
	{
		return 1;
	}

	@Override
	public void recreateStructures( int x, int z )
	{
	}

	@Override
	public void saveExtraData()
	{
	}
	
	private FakeWorld world;
	private VehicleEntity entity;
	private Chunk chunk;
}

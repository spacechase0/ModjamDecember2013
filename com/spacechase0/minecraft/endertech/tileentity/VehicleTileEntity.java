package com.spacechase0.minecraft.endertech.tileentity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.NextTickListEntry;

public class VehicleTileEntity extends TileEntity
{
	public VehicleTileEntity()
	{
	}
	
	@Override
	public void updateEntity()
	{
	}
	
	@Override
    public Packet getDescriptionPacket()
    {
		NBTTagCompound data = new NBTTagCompound();
		writeToNBT( data );
		
        return new Packet132TileEntityData( xCoord, yCoord, zCoord, 0, data );
    }
	
	@Override
    public void onDataPacket( INetworkManager net, Packet132TileEntityData packet )
    {
		readFromNBT( packet.data );
    }
	
	public void setVehicle( int minX, int minY, int minZ, int maxX, int maxY, int maxZ )
	{
		int size = maxX - minX;
		int arraySize = ( int ) Math.pow( size, 3 );
		blockData = new short[ arraySize ];
		blockTiles = new TileEntity[ arraySize ];
		blockTicks = new NextTickListEntry[ arraySize ];
		
		for ( int ix = minX; ix < maxX; ++ix )
		{
			for ( int iy = minY; iy < maxY; ++iy )
			{
				for ( int iz = minZ; iz < maxZ; ++iz )
				{
					int id = worldObj.getBlockId( ix, iy, iz );
					int meta = worldObj.getBlockMetadata( ix, iy, iz );
					short data = getBlockData( id, meta );
					
					int numX = ix - minX;
					int numY = iy - minY;
					int numZ = iz - minZ;
					
					TileEntity te = worldObj.getBlockTileEntity( ix, iy, iz );
					if ( te instanceof VehicleTileEntity/*te == this*/ )
					{
						myX = numX;
						myY = numY;
						myZ = numZ;
						te = new VehicleTileEntity();
					}
					
					if ( te != null )
					{
						te.xCoord = numX;
						te.yCoord = numY;
						te.zCoord = numZ;
					}
					
					int index = numX + ( numY * size ) + ( numZ * size * size );
					blockData[ index ] = data;
					blockTiles[ index ] = te;

					List list = worldObj.getPendingBlockUpdates( worldObj.getChunkFromBlockCoords( ix, iz ), false );
					if ( list != null )
					{
						for ( Object obj : list )
						{
							NextTickListEntry entry = ( NextTickListEntry ) obj;
							if ( entry != null && entry.xCoord == ix && entry.yCoord == iy && entry.zCoord == iz )
							{
								blockTicks[ index ] = new NextTickListEntry( numX, numY, numZ, entry.blockID );
								blockTicks[ index ].priority = entry.priority;
								blockTicks[ index ].scheduledTime = entry.scheduledTime;
							}
						}
					}
				}
			}
		}
		
		//System.out.println("my: "+myX+" "+myY+" "+myZ);
		if ( fromFakeWorld ) return;
		
		for ( int ix = minX; ix < maxX; ++ix )
		{
			for ( int iy = minY; iy < maxY; ++iy )
			{
				for ( int iz = minZ; iz < maxZ; ++iz )
				{
					if ( ix == minX + myX && iy == minY + myY && iz == minZ + myZ ) continue;
					
					worldObj.removeBlockTileEntity( ix, iy, iz );
					worldObj.setBlock( ix, iy, iz, 0, 0, 0x2 );
				}
			}
		}
	}
	
	@Override
    public void readFromNBT( NBTTagCompound tag )
    {
    	if ( !fromFakeWorld ) super.readFromNBT( tag );
		fromFakeWorld = false;
    	
    	if ( tag.hasKey( "EmbeddedX" ) )
    	{
    		myX = tag.getInteger( "EmbeddedX" );
    		myY = tag.getInteger( "EmbeddedY" );
    		myZ = tag.getInteger( "EmbeddedZ" );
    		
    		int[] data = tag.getIntArray( "BlockData" );
    		
    		int size = tag.getInteger( "Size" );
    		int arraySize = ( int ) Math.pow( size, 3 );
    		
    		blockData = new short[ arraySize ];
    		blockTiles = new TileEntity[ arraySize ];
    		blockTicks = new NextTickListEntry[ arraySize ];

    		for ( int i = 0; i < data.length; ++i )
    		{
    			int num = data[ i ];
    			blockData[ ( i * 2 ) + 0 ] = decompressA( num );
    			if ( ( i * 2 ) + 1 < blockData.length )
    			{
    				blockData[ ( i * 2 ) + 1 ] = decompressB( num );
    			}
    		}
    		
    		if ( tag.hasKey( "BlockTiles" ) )
    		{
    			NBTTagList list = ( NBTTagList ) tag.getTag( "BlockTiles" );
    			for ( int i = 0; i < list.tagCount(); ++i )
    			{
    				NBTTagCompound nbt = ( NBTTagCompound ) list.tagAt( i );
    				TileEntity te = TileEntity.createAndLoadEntity( nbt );
    				if ( te == null )
    				{
    					System.out.println( "SOMETHING WENT WRONG!!! " + nbt + " "+ this +" ");
    					Thread.dumpStack();
    					continue;
    				}
    				
					int index = te.xCoord + ( te.yCoord * size ) + ( te.zCoord * size * size );
					blockTiles[ index ] = te;
    			}
    		}
    		
    		if ( tag.hasKey( "BlockTicks" ) )
    		{
    			NBTTagList list = ( NBTTagList ) tag.getTag( "BlockTicks" );
    			for ( int i = 0; i < list.tagCount(); ++i )
    			{
    				NBTTagCompound nbt = ( NBTTagCompound ) list.tagAt( i );
    				
    				NextTickListEntry tick = new NextTickListEntry( nbt.getInteger( "X" ), nbt.getInteger( "Y" ), nbt.getInteger( "Z" ), nbt.getInteger( "BlockID" ) );
    				tick.priority = nbt.getInteger( "Priority" );
    				tick.scheduledTime = nbt.getLong( "Time" );

					int index = tick.xCoord + ( tick.yCoord * size ) + ( tick.zCoord * size * size );
					blockTicks[ index ] = tick;
    			}
    		}
    	}
    }

	@Override
    public void writeToNBT( NBTTagCompound tag )
    {
		//System.out.println("fw1:"+getEmbeddedIndex());
		if ( !fromFakeWorld ) super.writeToNBT( tag );
		fromFakeWorld = false;
		
		//System.out.println("fw2:"+getEmbeddedIndex());
		if ( myX == -1 ) { /*System.out.println("no x:"+myX+" "+myY+" "+myZ);*/return;}
		tag.setInteger( "EmbeddedX", myX );
		tag.setInteger( "EmbeddedY", myY );
		tag.setInteger( "EmbeddedZ", myZ );
		tag.setInteger( "Size", ( int ) Math.cbrt( blockData.length ) );
		
		int len = ( int ) Math.ceil( blockData.length / 2.f );
		int[] data = new int[ len ];
		for ( int i = 0; i < len; ++i )
		{
			int indexBase = i * 2;
			data[ i ] = compress( blockData[ indexBase + 0 ], ( blockData.length > indexBase + 1 ) ? blockData[ indexBase + 1 ] : 0 );
		}
		tag.setIntArray( "BlockData", data );
		
		NBTTagList tileEntities = new NBTTagList();
		for ( TileEntity te : blockTiles )
		{
			if ( te == null ) continue;
			
			NBTTagCompound nbt = new NBTTagCompound();
			//System.out.println("saving TE:"+te);
			te.writeToNBT( nbt );
			//System.out.println("wrote "+nbt);
			
			tileEntities.appendTag( nbt );
		}
		if ( tileEntities.tagCount() > 0 )
		{
			tag.setTag( "BlockTiles", tileEntities );
		}
		
		NBTTagList ticks = new NBTTagList();
		for ( NextTickListEntry tick : blockTicks )
		{
			if ( tick == null ) continue;
			
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger( "X", tick.xCoord );
			nbt.setInteger( "Y", tick.yCoord );
			nbt.setInteger( "Z", tick.zCoord );
			nbt.setInteger( "BlockID", tick.blockID );
			nbt.setInteger( "Priority", tick.priority );
			nbt.setLong( "Time", tick.scheduledTime );
			
			ticks.appendTag( nbt );
		}
		if ( ticks.tagCount() > 0 )
		{
			tag.setTag( "blockTicks", ticks );
		}
    }
	
	public int getEmbeddedX()
	{
		return myX;
	}
	
	public int getEmbeddedY()
	{
		return myY;
	}
	
	public int getEmbeddedZ()
	{
		return myZ;
	}
	
	public int getEmbeddedIndex()
	{
		int size = getSize();
		return myX + ( myY * size ) + ( myZ * size * size );
	}
	
	public int getSize()
	{
		return ( int ) Math.cbrt( blockData.length );
	}
	
	public short[] getBlockData()
	{
		return blockData;
	}
	
	public TileEntity[] getBlockTiles()
	{
		return blockTiles;
	}
	
	public NextTickListEntry[] getBlockTicks()
	{
		return blockTicks;
	}
	
	public int getBlockId( short data )
	{
		return ( data & 0x0FFF );
	}
	
	public int getBlockMeta( short data )
	{
		return ( ( data & 0xF000 ) >> 12 );
	}
	
	private short getBlockData( int id, int meta )
	{
		return ( short )( ( id & 0x0FFF ) | ( ( meta << 12 ) & 0xF000 ) );
	}
	
	private int compress( short a, short b )
	{
		return ( ( a & 0x0000FFFF ) | ( ( b << 16 ) & 0xFFFF0000 ) );
	}
	
	private short decompressA( int num )
	{
		return ( short )( num & 0x0000FFFF );
	}
	
	private short decompressB( int num )
	{
		return ( short )( ( num >> 16 ) & 0x0000FFFF );
	}
	
	private short[] blockData = new short[ 0 ];
	private TileEntity[] blockTiles = new TileEntity[ 0 ];
	private NextTickListEntry[] blockTicks = new NextTickListEntry[ 0 ];
	private int myX = -1, myY = -1, myZ = -1;
	
	public static boolean fromFakeWorld = false;
}

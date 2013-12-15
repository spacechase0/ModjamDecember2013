package com.spacechase0.minecraft.endertech.tileentity;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class VehicleTileEntity extends TileEntity
{
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
		
		for ( int ix = minX; ix < maxX; ++ix )
		{
			for ( int iy = minY; iy < maxY; ++iy )
			{
				for ( int iz = minZ; iz < maxZ; ++iz )
				{
					int id = worldObj.getBlockId( ix, iy, iz );
					int meta = worldObj.getBlockMetadata( ix, iy, iz );
					short data = getBlockData( id, meta );
					
					TileEntity te = worldObj.getBlockTileEntity( ix, iy, iz );
					te.xCoord = ix;
					te.yCoord = iy;
					te.zCoord = iz;
					
					if ( te == this )
					{
						myX = ix;
						myY = iy;
						myZ = iz;
						te = new VehicleTileEntity();
					}
					
					int index = ix + ( iy * size ) + ( iz * size * size );
					blockData[ index ] = data;
					blockTiles[ index ] = te;
				}
			}
		}
		
		for ( int ix = minX; ix < maxX; ++ix )
		{
			for ( int iy = minY; iy < maxY; ++iy )
			{
				for ( int iz = minZ; iz < maxZ; ++iz )
				{
					worldObj.removeBlockTileEntity( ix, iy, iz );
					worldObj.setBlock( ix, iy, iz, 0, 0, 0x2 );
				}
			}
		}
	}
	
	@Override
    public void readFromNBT( NBTTagCompound tag )
    {
    	super.readFromNBT( tag );
    	
    	if ( tag.hasKey( "EmbeddedX" ) )
    	{
    		myX = tag.getInteger( "EmbeddedX" );
    		myY = tag.getInteger( "EmbeddedY" );
    		myZ = tag.getInteger( "EmbeddedZ" );
    		
    		int[] data = tag.getIntArray( "BlockData" );
    		
    		int size = ( int ) Math.cbrt( data.length );
    		
    		blockData = new short[ data.length ];
    		blockTiles = new TileEntity[ data.length ];
    		
    		for ( int i = 0; i < data.length; ++i )
    		{
    			int num = data[ i ];
    			blockData[ ( i * 2 ) + 0 ] = decompressA( num );
    			blockData[ ( i * 2 ) + 1 ] = decompressB( num );
    		}
    		
    		if ( tag.hasKey( "BlockTiles" ) )
    		{
    			NBTTagList list = ( NBTTagList ) tag.getTag( "BlockTiles" );
    			for ( int i = 0; i < list.tagCount(); ++i )
    			{
    				NBTTagCompound nbt = ( NBTTagCompound ) list.tagAt( i );
    				TileEntity te = TileEntity.createAndLoadEntity( nbt );

					int index = te.xCoord + ( te.yCoord * size ) + ( te.zCoord * size * size );
					blockTiles[ index ] = te;
    			}
    		}
    	}
    }

	@Override
    public void writeToNBT( NBTTagCompound tag )
    {
		super.writeToNBT( tag );
		
		if ( myX == -1 ) return;
		tag.setInteger( "EmbeddedX", myX );
		tag.setInteger( "EmbeddedY", myY );
		tag.setInteger( "EmbeddedZ", myZ );
		
		int[] data = new int[ blockData.length / 2 ];
		for ( int i = 0; i < blockData.length / 2; ++i )
		{
			int indexBase = i * 2;
			data[ i ] = compress( blockData[ indexBase + 0 ], blockData[ indexBase + 1 ] );
		}
		tag.setIntArray( "BlockData", data );
		
		NBTTagList tileEntities = new NBTTagList();
		for ( TileEntity te : blockTiles )
		{
			if ( te == null ) continue;
			
			NBTTagCompound nbt = new NBTTagCompound();
			te.writeToNBT( nbt );
			
			tileEntities.appendTag( nbt );
		}
		if ( tileEntities.tagCount() > 0 )
		{
			tag.setTag( "BlockTiles", tileEntities );
		}
    }
	
	private int getBlockId( short data )
	{
		return ( data & 0x0FFF );
	}
	
	private int getBlockMeta( short data )
	{
		return ( ( data & 0xF000 ) >> 12 );
	}
	
	private short getBlockData( int id, int meta )
	{
		return ( short )( ( id & 0x0FFF ) | ( ( meta << 12 ) & 0xF000 ) );
	}
	
	private int compress( short a, short b )
	{
		return ( short )( a | ( b << 16 ) );
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
	private int myX = -1, myY = -1, myZ = -1;
}

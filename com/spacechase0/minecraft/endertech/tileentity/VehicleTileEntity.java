package com.spacechase0.minecraft.endertech.tileentity;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
					worldObj.removeBlockTileEntity( ix, iy, iz );
					
					int index = ix + ( iy * size ) + ( iz * size * size );
					blockData[ index ] = data;
					blockTiles[ index ] = te;
					worldObj.removeBlockTileEntity( ix, iy, iz );
					worldObj.setBlock( ix, iy, iz, 0, 0, 0x2 );
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
    }

	@Override
    public void writeToNBT( NBTTagCompound tag )
    {
		super.writeToNBT( tag );
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
	
	private short[] blockData = new short[ 0 ];
	private TileEntity[] blockTiles = new TileEntity[ 0 ];
}

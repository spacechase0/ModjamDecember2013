package com.spacechase0.minecraft.endertech.tileentity;

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
		System.out.println( maxX - minX );
		for ( int ix = minX; ix < maxX; ++ix )
		{
			for ( int iy = minY; iy < maxY; ++iy )
			{
				for ( int iz = minZ; iz < maxZ; ++iz )
				{
					
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
}

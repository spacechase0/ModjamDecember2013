package com.spacechase0.minecraft.endertech.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TubeTileEntity extends TileEntity
{
	public TubeTileEntity()
	{
		filters = new ItemStack[ 6 ][];
		for ( int i = 0; i < 6; ++i )
		{
			filters[ i ] = new ItemStack[ 9 ];
		}
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
	
	@Override
    public void readFromNBT( NBTTagCompound tag )
    {
    	super.readFromNBT( tag );
    	
    	input = expand( tag.getByte( "Input" ), 6 );
    	output = expand( tag.getByte( "Output" ), 6 );
    	upgrades = tag.getByteArray( "Upgrades" );
    	
    	NBTTagCompound filtersTag = ( NBTTagCompound ) tag.getTag( "Filters" );
    	if ( filtersTag != null )
    	{
    		for ( int i = 0; i < filters.length; ++i )
    		{
    			NBTTagCompound filter = ( NBTTagCompound ) filtersTag.getTag( "Side" + i );
    			if ( filter == null ) continue;
    			
    			for ( int is = 0; is < filters[ i ].length; ++is )
    			{
    				NBTTagCompound stackTag = ( NBTTagCompound ) filter.getTag( "Item" + is );
    				if ( stackTag == null ) continue;
    				
    				ItemStack stack = ItemStack.loadItemStackFromNBT( stackTag );
    				filters[ i ][ is ] = stack;
    			}
    		}
    	}
    	
    	NBTTagCompound bufferTag = ( NBTTagCompound ) tag.getTag( "Buffer" );
    	if ( bufferTag != null )
    	{
    		buffer = ItemStack.loadItemStackFromNBT( bufferTag );
    	}
    }

	@Override
    public void writeToNBT( NBTTagCompound tag )
    {
		super.writeToNBT( tag );

		tag.setByte( "Input", condense( input ) );
		tag.setByte( "Output", condense( output ) );
		tag.setByteArray( "Upgrades", upgrades );

		NBTTagCompound filtersTag = new NBTTagCompound();
		for ( int i = 0; i < filters.length; ++i )
		{
			NBTTagCompound filterTag = new NBTTagCompound();
			for ( int is = 0; is < filters[ i ].length; ++is )
			{
				ItemStack stack = filters[ i ][ is ];
				if ( stack == null ) continue;
				
				NBTTagCompound stackTag = new NBTTagCompound();
				stack.writeToNBT( stackTag );
				filterTag.setTag( "Item" + is, stackTag );
			}
			
			if ( !filterTag.hasNoTags() )
			{
				filtersTag.setTag( "Side" + i, filterTag );
			}
		}
		if ( !filtersTag.hasNoTags() )
		{
			tag.setTag( "Filters", filtersTag );
		}
		
		if ( buffer != null )
		{
			NBTTagCompound bufferTag = new NBTTagCompound();
			buffer.writeToNBT( bufferTag );
			tag.setTag( "Buffer", bufferTag );
		}
    }
	
	public boolean doesInput( ForgeDirection dir )
	{
		return input[ dir.ordinal() ];
	}
	
	public boolean doesOutput( ForgeDirection dir )
	{
		return output[ dir.ordinal() ];
	}
	
	public void toggleInput( ForgeDirection dir )
	{
		input[ dir.ordinal() ] = !input[ dir.ordinal() ];
		worldObj.markBlockForUpdate( xCoord, yCoord, zCoord );
	}
	
	public void toggleOutput( ForgeDirection dir )
	{
		output[ dir.ordinal() ] = !output[ dir.ordinal() ];
		worldObj.markBlockForUpdate( xCoord, yCoord, zCoord );
	}
	
	public byte getUpgrades( ForgeDirection dir )
	{
		return upgrades[ dir.ordinal() ];
	}
	
	public boolean hasUpgrade( ForgeDirection dir, byte upgrade )
	{
		return ( ( getUpgrades( dir ) & upgrade ) != 0 );
	}
	
	public void upgrade( ForgeDirection dir, byte upgrade )
	{
		upgrades[ dir.ordinal() ] |= upgrade;
		worldObj.markBlockForUpdate( xCoord, yCoord, zCoord );
	}
	
	private static byte condense( boolean[] bools )
	{
		if ( bools.length > 8 ) throw new IllegalArgumentException( "Length > 8" );
		
		byte b = 0;
		for ( int i = 0; i < bools.length; ++i )
		{
			b |= ( bools[ i ] ? 1 : 0 ) << i;
		}
		
		return b;
	}
	
	private static boolean[] expand( byte b, int len )
	{
		if ( len > 8 ) throw new IllegalArgumentException( "Length > 8" );
		
		boolean[] bools = new boolean[ len ];
		for ( int i = 0; i < len; ++i )
		{
			bools[ i ] = ( ( b >> i ) & 0x1 ) != 0;
		}
		
		return bools;
	}
	
	private boolean[] input = new boolean[ 6 ];
	private boolean[] output = new boolean[ 6 ];
	private byte[] upgrades = new byte[ 6 ];
	private ItemStack[][] filters;
	
	private ItemStack buffer;

	public static final byte UPGRADE_SPEED    = 1 << 0;
	public static final byte UPGRADE_FILTER   = 1 << 1;
	public static final byte UPGRADE_BLOCK    = 1 << 2;
	public static final byte UPGRADE_REDSTONE = 1 << 3;
	public static final byte[] UPGRADES = new byte[] { UPGRADE_SPEED, UPGRADE_FILTER, UPGRADE_BLOCK, UPGRADE_REDSTONE };
}

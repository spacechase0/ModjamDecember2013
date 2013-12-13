package com.spacechase0.minecraft.endertech.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TubeTileEntity extends TileEntity
{
	@Override
	public void updateEntity()
	{
		for(int i=0;i<6;++i)input[i]=true;
		for(int i=0;i<6;++i)output[i]=true;
		input[1]=true;
		output[3]=true;
	}
	
	@Override
    public void readFromNBT( NBTTagCompound tag )
    {
    	super.readFromNBT( tag );
    	
    	input = expand( tag.getByte( "Input" ), 6 );
    	output = expand( tag.getByte( "Output" ), 6 );
    	upgrades = tag.getByteArray( "Upgrades" );
    }

	@Override
    public void writeToNBT( NBTTagCompound tag )
    { 
		super.writeToNBT( tag );

		tag.setByte( "Input", condense( input ) );
		tag.setByte( "Output", condense( output ) );
		tag.setByteArray( "Upgrades", upgrades );
    }
	
	public boolean doesInput( ForgeDirection dir )
	{
		return input[ dir.ordinal() ];
	}
	
	public boolean doesOutput( ForgeDirection dir )
	{
		return output[ dir.ordinal() ];
	}
	
	public byte getUpgrades( ForgeDirection dir )
	{
		return upgrades[ dir.ordinal() ];
	}
	
	public boolean hasUpgrade( ForgeDirection dir, byte upgrade )
	{
		return ( ( getUpgrades( dir ) & upgrade ) != 0 );
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

	public static final byte UPGRADE_SPEED    = 1 << 0;
	public static final byte UPGRADE_FILTER   = 1 << 1;
	public static final byte UPGRADE_BLOCK    = 1 << 2;
	public static final byte UPGRADE_REDSTONE = 1 << 3;
	public static final byte[] UPGRADES = new byte[] { UPGRADE_SPEED, UPGRADE_FILTER, UPGRADE_BLOCK, UPGRADE_REDSTONE };
}

package com.spacechase0.minecraft.endertech.tileentity;

import java.util.List;

import com.spacechase0.minecraft.endertech.entity.TransportingEntity;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;

public class TubeTileEntity extends TileEntity
{
	public TubeTileEntity()
	{
		filters = new ItemStack[ 12 ][]; // 2 per side?
		for ( int i = 0; i < filters.length; ++i )
		{
			filters[ i ] = new ItemStack[ 9 ];
		}
	}
	
	@Override
	public void updateEntity()
	{
		if ( worldObj.isRemote ) return;
		
		if ( cooldown > 0 )
		{
			--cooldown;
			return;
		}
		cooldown = 5;
		
		if ( buffer != null )
		{
			trySend();
		}
		
		if ( buffer == null )
		{
			tryReceive();
			if ( buffer != null )
			{
				trySend();
			}
		}
		
		if ( buffer == null )
		{
			tryInput();
			if ( buffer != null )
			{
				trySend();
			}
		}
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
    	
    	cooldown = tag.getByte( "Cooldown" );
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
		
		tag.setByte( "Cooldown", cooldown );
    }
	
	public boolean doesInput( ForgeDirection dir )
	{
		if ( dir == ForgeDirection.UNKNOWN ) return false;
		return input[ dir.ordinal() ];
	}
	
	public boolean doesOutput( ForgeDirection dir )
	{
		if ( dir == ForgeDirection.UNKNOWN ) return false;
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
	
	private void tryInput()
	{
		if ( buffer != null ) return;
		for ( int i = 0; i < input.length; ++i )
		{
			if ( !input[ i ] ) continue;
			
			ForgeDirection dir = ForgeDirection.getOrientation( i );
			if ( dir == ForgeDirection.EAST || dir == ForgeDirection.WEST ) dir = dir.getOpposite();
			
			TileEntity te = worldObj.getBlockTileEntity( xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ );
			if ( te instanceof ISidedInventory )
			{
				ISidedInventory inv = ( ISidedInventory ) te;
				
				int[] slots = inv.getAccessibleSlotsFromSide( dir.getOpposite().ordinal() );
				for ( int slot : slots )
				{
					ItemStack inSlot = inv.getStackInSlot( slot );
					if ( inSlot == null || !inv.canExtractItem( slot, inSlot, dir.getOpposite().ordinal() ) )
					{
						continue;
					}
					
					inv.setInventorySlotContents( slot, null );
					inv.onInventoryChanged();
					
					buffer = inSlot;
					return;
				}
			}
			else if ( te instanceof IInventory )
			{
				IInventory inv = ( IInventory ) te;
				
				for ( int slot = 0; slot < inv.getSizeInventory(); ++slot )
				{
					ItemStack inSlot = inv.getStackInSlot( slot );
					if ( inSlot == null )
					{
						continue;
					}
					
					inv.setInventorySlotContents( slot, null );
					inv.onInventoryChanged();
					
					buffer = inSlot;
					return;
				}
			}
		}
	}
	
	private void tryReceive()
	{
		List inside = worldObj.getEntitiesWithinAABB( TransportingEntity.class, AxisAlignedBB.getBoundingBox( xCoord + 0.25, yCoord + 0.25, zCoord + 0.25, xCoord + 0.75, yCoord + 0.75, zCoord + 0.75 ) );
		if ( inside.size() <= 0 )
		{
			return;
		}
		
		for ( Object obj : inside )
		{
			if ( buffer != null ) break;

			TransportingEntity entity = ( TransportingEntity ) obj;
			
			if ( entity.getDistanceTraveled() < 1 || !canReceive( entity ) )
			{
				System.out.println("can't receive");
				continue;
			}
			
			buffer = entity.getItemStack();
			worldObj.removeEntity( entity );
			
			trySend();
		}
	}
	
	private void trySend()
	{
		if ( buffer == null ) return;
		
		/*
		List inside = worldObj.getEntitiesWithinAABB( TransportingEntity.class, AxisAlignedBB.getBoundingBox( xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1 ) );
		if ( inside.size() > 0 )
		{
			return;
		}
		*/
		
		for ( int i = 0; i < output.length; ++i )
		{
			ForgeDirection dir = ForgeDirection.getOrientation( i );
			if ( !checkOutputFilter( dir, buffer ) ) continue;
			
			if ( dir == ForgeDirection.EAST || dir == ForgeDirection.WEST ) dir = dir.getOpposite();
			
			TransportingEntity entity = new TransportingEntity( worldObj, buffer, dir, 1 );
			entity.posX = xCoord + 0.5;
			entity.posY = yCoord + 0.5 - 0.25;
			entity.posZ = zCoord + 0.5;
			worldObj.spawnEntityInWorld( entity );
			
			buffer = null;
		}
	}
	
	private boolean canReceive( TransportingEntity entity )
	{
		ForgeDirection inDir = entity.getDirection().getOpposite();
		if ( !checkInputFilter( inDir, entity ) )
		{
			return false;
		}
		
		for ( int id = 0; id < 6; ++id )
		{
			ForgeDirection outDir = ForgeDirection.getOrientation( id );
			if ( checkOutputFilter( outDir, entity.getItemStack() ) )
			{
				return true;
			}
		}
		
		return false;
	}
	
	private boolean checkInputFilter( ForgeDirection dir, TransportingEntity entity )
	{
		if ( !doesInput( dir ) )
		{
			return false;
		}
		
		return checkFilter( dir.ordinal() * 2, entity.getItemStack() );
	}
	
	private boolean checkOutputFilter( ForgeDirection dir, ItemStack stack )
	{
		if ( !doesOutput( dir ) )
		{
			return false;
		}
		
		return checkFilter( ( dir.ordinal() * 2 ) + 1, stack );
	}
	
	private boolean checkFilter( int index, ItemStack stack )
	{
		return true;
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
	private ItemStack[][] filters; // 0i1, 1o1, 2i2, 3o2, 4i3, 4o3, etc.
	                               // First num = index
	                               // Letter = input.output
	                               // Second num = side
	
	private ItemStack buffer;
	private byte cooldown;

	public static final byte UPGRADE_SPEED    = 1 << 0;
	public static final byte UPGRADE_FILTER   = 1 << 1;
	public static final byte UPGRADE_BLOCK    = 1 << 2;
	public static final byte UPGRADE_REDSTONE = 1 << 3;
	public static final byte[] UPGRADES = new byte[] { UPGRADE_SPEED, UPGRADE_FILTER, UPGRADE_BLOCK, UPGRADE_REDSTONE };
}

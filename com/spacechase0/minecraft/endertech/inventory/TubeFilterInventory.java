package com.spacechase0.minecraft.endertech.inventory;

import com.spacechase0.minecraft.endertech.tileentity.TubeTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class TubeFilterInventory implements IInventory
{
	public TubeFilterInventory( TubeTileEntity theTube, int theFilter )
	{
		tube = theTube;
		filter = theFilter;
	}
	
	@Override
	public int getSizeInventory()
	{
		return getStacks().length;
	}

	@Override
	public ItemStack getStackInSlot( int slot )
	{
		return getStacks()[ slot ];
	}

	@Override
	public ItemStack decrStackSize( int slot, int amount )
	{
		getStacks()[ slot ] = null;
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing( int slot )
	{
		return null;
	}

	@Override
	public void setInventorySlotContents( int slot, ItemStack stack )
	{
		if ( stack != null )
		{
			getStacks()[ slot ] = stack.copy();
			getStacks()[ slot ].stackSize = 1;
		}
		else
		{
			getStacks()[ slot ] = null;
		}
	}

	@Override
	public String getInvName()
	{
		return "meow"; // TODO
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit() 
	{
		return 1;
	}

	@Override
	public void onInventoryChanged()
	{
	}

	@Override
	public boolean isUseableByPlayer( EntityPlayer player )
	{
		return true;
	}

	@Override
	public void openChest() 
	{
	}

	@Override
	public void closeChest()
	{
	}

	@Override
	public boolean isItemValidForSlot( int slot, ItemStack stack )
	{
		return true;
	}
	
	private ItemStack[] getStacks()
	{
		return tube.getFilters( filter );
	}

	private final TubeTileEntity tube;
	private final int filter;
}

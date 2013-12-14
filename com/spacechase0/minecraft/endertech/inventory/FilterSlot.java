package com.spacechase0.minecraft.endertech.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class FilterSlot extends Slot
{
	public FilterSlot( IInventory inv, int id, int x, int y )
	{
		super( inv, id, x, y );
	}
	
	// Can't remember how to do this...
	
    public boolean canTakeStack( EntityPlayer player )
    {
        return true;
    }
}

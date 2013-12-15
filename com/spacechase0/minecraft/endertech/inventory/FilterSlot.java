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

	@Override
    public void putStack( ItemStack stack )
    {
		if ( stack == null ) { super.putStack( stack ); return; }
		
		stack = stack.copy();
		stack.stackSize = 1;
		super.putStack( stack );
    }

	@Override
    public boolean canTakeStack( EntityPlayer player )
    {
        return true;
    }
}

package com.spacechase0.minecraft.endertech.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class FilterContainer extends Container
{
	public FilterContainer( TubeFilterInventory filters, EntityPlayer player )
	{
		for ( int i = 0; i < filters.getSizeInventory(); ++i )
		{
			int ix = i % 3;
			int iy = i / 3;
			
			addSlotToContainer( new FilterSlot( filters, i, 62 + ix * 18, 17 + iy * 18 ) );
		}
		
		bindPlayerInventory( player.inventory );
	}
	
	@Override
	public boolean canInteractWith( EntityPlayer player )
	{
		return true;
	}

	private void bindPlayerInventory( IInventory par1IInventory)
	{
		int i, j;
		
        for (i = 0; i < 3; ++i )
        {
            for (j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(par1IInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(par1IInventory, i, 8 + i * 18, 142));
        }
	}
}

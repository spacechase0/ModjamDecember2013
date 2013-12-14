package com.spacechase0.minecraft.endertech;

import com.spacechase0.minecraft.endertech.tileentity.TubeTileEntity;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public class DropHandler
{
	@ForgeSubscribe
	public void interceptDrops( HarvestDropsEvent event )
	{
		// Called after tile entity is destroyed
		/*
		if ( event.block == EnderTech.blocks.tube )
		{
			TubeTileEntity tube = ( TubeTileEntity ) event.world.getBlockTileEntity( event.x, event.y, event.z );
			if ( !event.harvester.capabilities.isCreativeMode )
			{
				for ( ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS )
				{
					if ( tube.hasUpgrade( dir, TubeTileEntity.UPGRADE_SPEED ) )
					{
						event.drops.add( new ItemStack( Item.goldNugget ) );
					}
					if ( tube.hasUpgrade( dir, TubeTileEntity.UPGRADE_FILTER ) )
					{
						event.drops.add( new ItemStack( EnderTech.items.nugget, 1, 1 ) );
					}
					if ( tube.hasUpgrade( dir, TubeTileEntity.UPGRADE_BLOCK ) )
					{
						event.drops.add( new ItemStack( EnderTech.items.nugget, 1, 0 ) );
					}
					if ( tube.hasUpgrade( dir, TubeTileEntity.UPGRADE_REDSTONE ) )
					{
						event.drops.add( new ItemStack( Item.redstone ) );
					}
				}
			}
		}
		*/
	}
}

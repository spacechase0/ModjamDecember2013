package com.spacechase0.minecraft.endertech.client;

import com.spacechase0.minecraft.endertech.CommonProxy;
import com.spacechase0.minecraft.endertech.EnderTech;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;

public class TooltipHandler
{
	@ForgeSubscribe
	public void modifyTooltip( ItemTooltipEvent event )
	{
		/*
		if ( event.itemStack.getItem() == Item.goldNugget )
		{
			event.toolTip.add( StatCollector.translateToLocal( "endertech:tube.upgrade.gold" ) );
		}
		else if ( event.itemStack.getItem() == Item.redstoneDust )
		{
			event.toolTip.add( StatCollector.translateToLocal( "endertech:tube.upgrade.redstone" ) );
		}
		else if ( CommonProxy.isOre( "nuggetIron", event.itemStack ) )
		{
			event.toolTip.add( StatCollector.translateToLocal( "endertech:tube.upgrade.iron" ) );
		}
		else if ( CommonProxy.isOre( "nuggetDiamond", event.itemStack ) )
		{
			event.toolTip.add( StatCollector.translateToLocal( "endertech:tube.upgrade.iron" ) );
		}
		else */if ( event.itemStack.getItem() instanceof ItemBlock )
		{
			// No way to avoid using the block ID?
			ItemBlock block = ( ItemBlock ) event.itemStack.getItem();
			if ( block.getBlockID() == EnderTech.blocks.tube.blockID )
			{
				int amt = Integer.valueOf( StatCollector.translateToLocal( "tile.endertech:tube.tooltip.amount" ) );
				for ( int i = 1; i <= amt; ++i )
				{
					event.toolTip.add( StatCollector.translateToLocal( "tile.endertech:tube.tooltip." + i ) );
				}
			}
		}
	}
}

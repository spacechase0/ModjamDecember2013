package com.spacechase0.minecraft.endertech;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class CommonProxy
{
	public void init()
	{
	}
	
	public static boolean isOre( String type, ItemStack stack )
	{
		List< ItemStack > stacks = OreDictionary.getOres( type );
		for ( ItemStack ore : stacks )
		{
			if ( ore.itemID != stack.itemID ) continue;
			
			if ( ore.getItemDamage() == OreDictionary.WILDCARD_VALUE )
			{
				return true;
			}
			else if ( ore.getItemDamage() == stack.getItemDamage() )
			{
				// Do I need to check NBT?
				return true;
			}
		}
		
		return false;
	}
}

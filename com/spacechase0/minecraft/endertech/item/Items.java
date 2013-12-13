package com.spacechase0.minecraft.endertech.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.common.Configuration;

// I'm following a structure similar to what I use in SpaceCore, so I can remove the manual registering afterwards.
public class Items
{
	public void register( Configuration config )
	{
		register( config, 19430 );
	}
	
	// I can remove this entire function later :)
	private void register( Configuration config, int baseId )
	{
		nugget = new NuggetItem( config.getItem( "nugget", baseId ).getInt( baseId++ ), ( String[] ) nuggetParams[ 0 ] );
		GameRegistry.registerItem( nugget, "endertech:nugget" );
	}
	
	//@ModItem
	public NuggetItem nugget;
	public Object[] nuggetParams = new Object[] { new String[] { "iron", "diamond" } };
}

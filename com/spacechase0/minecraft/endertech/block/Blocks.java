package com.spacechase0.minecraft.endertech.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.common.Configuration;

// I'm following a structure similar to what I use in SpaceCore, so I can remove the manual registering afterwards.
public class Blocks
{
	public void register( Configuration config )
	{
		register( config, 1480 );
	}
	
	// I can remove this entire function later :)
	private void register( Configuration config, int baseId )
	{
		tube = new TubeBlock( config.getBlock( "tube", baseId ).getInt( baseId++ ) );
		GameRegistry.registerBlock( tube, "endertech:tube" );
	}
	
	//@ModBlock
	public TubeBlock tube;
}

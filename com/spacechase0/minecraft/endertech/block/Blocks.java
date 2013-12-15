package com.spacechase0.minecraft.endertech.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.Configuration;

// I'm following a structure similar to what I use in SpaceCore, so I can remove the manual registering afterwards.
public class Blocks
{
	public void register( Configuration config )
	{
		register( config, 1480 );
		
		ender.setHardness( 4 );
		ender.setResistance( 4 );
		ender.setCreativeTab( CreativeTabs.tabBlock );
		
		vehicleFrame.setHardness( 6 );
		vehicleFrame.setResistance( 6 );
		vehicleFrame.setCreativeTab( CreativeTabs.tabTransport );
		
		vehicleEngine.setHardness( 8 );
		vehicleEngine.setResistance( 8 );
		vehicleEngine.setCreativeTab( CreativeTabs.tabTransport );
	}
	
	// I can remove this entire function later :)
	private void register( Configuration config, int baseId )
	{
		tube = new TubeBlock( config.getBlock( "tube", baseId ).getInt( baseId++ ) );
		GameRegistry.registerBlock( tube, "endertech:tube" );
		
		ender = new SimpleBlock( config.getBlock( "ender", baseId ).getInt( baseId++ ), ( String ) enderParams[ 0 ], ( Material ) enderParams[ 1 ] );
		GameRegistry.registerBlock( ender, "endertech:ender" );
		
		vehicleFrame = new SimpleBlock( config.getBlock( "vehicleFrame", baseId ).getInt( baseId++ ), ( String ) vehicleFrameParams[ 0 ], ( Material ) vehicleFrameParams[ 1 ] );
		GameRegistry.registerBlock( vehicleFrame, "endertech:vehicleFrame" );
		
		vehicleEngine = new SimpleBlock( config.getBlock( "vehicleEngine", baseId ).getInt( baseId++ ), ( String ) vehicleEngineParams[ 0 ], ( Material ) vehicleEngineParams[ 1 ] );
		GameRegistry.registerBlock( vehicleEngine, "endertech:vehicleEngine" );
	}
	
	//@ModBlock
	public TubeBlock tube;
	
	//@ModBlock
	public SimpleBlock ender;
	public Object[] enderParams = new Object[] { "ender", Material.dragonEgg };
	
	//@ModBlock
	public SimpleBlock vehicleFrame;
	public Object[] vehicleFrameParams = new Object[] { "vehicleFrame", Material.dragonEgg };
	
	//@ModBlock
	public SimpleBlock vehicleEngine;
	public Object[] vehicleEngineParams = new Object[] { "vehicleEngine", Material.dragonEgg };
}

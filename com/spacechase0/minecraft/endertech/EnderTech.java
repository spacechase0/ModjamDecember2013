package com.spacechase0.minecraft.endertech;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@NetworkMod( clientSideRequired = true, serverSideRequired = false )
@Mod( modid = "SC0_EnderTech", useMetadata = true )
public class EnderTech
{
	@EventHandler
	public void preInit( FMLPreInitializationEvent event )
	{
		config = new Configuration( event.getSuggestedConfigurationFile() );
	}
	
	@EventHandler
	public void init( FMLInitializationEvent event )
	{
		
	}
	
	@EventHandler
	public void postInit( FMLPostInitializationEvent event )
	{
		config.save();
	}
	
	public Configuration config;
}

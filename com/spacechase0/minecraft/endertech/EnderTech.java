package com.spacechase0.minecraft.endertech;

import com.spacechase0.minecraft.endertech.block.Blocks;
import com.spacechase0.minecraft.endertech.item.Items;
import com.spacechase0.minecraft.endertech.tileentity.TubeTileEntity;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@NetworkMod( clientSideRequired = true, serverSideRequired = false )
@Mod( modid = "SC0_EnderTech", useMetadata = true )
public class EnderTech
{
	@Instance( value = "SC0_EnderTech" )
	public static EnderTech instance;
	
	@SidedProxy( serverSide = "com.spacechase0.minecraft.endertech.CommonProxy",
	             clientSide = "com.spacechase0.minecraft.endertech.client.ClientProxy" )
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit( FMLPreInitializationEvent event )
	{
		config = new Configuration( event.getSuggestedConfigurationFile() );
		
		blocks = new Blocks();
		blocks.register( config );
		
		items = new Items();
		items.register( config );
		
		registerTileEntities();
	}
	
	@EventHandler
	public void init( FMLInitializationEvent event )
	{
		LanguageRegistry.instance().loadLocalization( "/assets/endertech/lang/en_US.lang", "en_US", false );
		registerOreDictionary();
		registerRecipes();
		
		proxy.init();
	}
	
	@EventHandler
	public void postInit( FMLPostInitializationEvent event )
	{
		config.save();
	}
	
	private void registerTileEntities()
	{
		GameRegistry.registerTileEntity( TubeTileEntity.class, "EnderTube" );
	}
	
	private void registerOreDictionary()
	{
		OreDictionary.registerOre( "nuggetIron", new ItemStack( items.nugget, 1, 0 ) );
		OreDictionary.registerOre( "nuggetDiamond", new ItemStack( items.nugget, 1, 1 ) );
	}
	
	private void registerRecipes()
	{
		GameRegistry.addShapelessRecipe( new ItemStack( items.nugget, 9, 0 ), Item.ingotIron );
		GameRegistry.addShapelessRecipe( new ItemStack( items.nugget, 9, 1 ), Item.diamond );
		GameRegistry.addRecipe( new ShapedOreRecipe( new ItemStack( Item.ingotIron ),
		                                             "***",
		                                             "***",
		                                             "***",
		                                             '*', "nuggetIron" ) );
		GameRegistry.addRecipe( new ShapedOreRecipe( new ItemStack( Item.diamond ),
		                                             "***",
		                                             "***",
		                                             "***",
		                                             '*', "nuggetDiamond" ) );
		GameRegistry.addShapedRecipe( new ItemStack( blocks.tube ),
		                              "HH_",
		                              "HOH",
		                              "_HH",
		                              'H', Block.hopperBlock,
		                              '_', Block.pressurePlateGold,
		                              'O', Item.eyeOfEnder );
	}
	
	public Blocks blocks;
	public Items items;
	public Configuration config;
}

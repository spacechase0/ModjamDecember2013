package com.spacechase0.minecraft.endertech;

import com.spacechase0.minecraft.endertech.block.Blocks;
import com.spacechase0.minecraft.endertech.entity.TransportingEntity;
import com.spacechase0.minecraft.endertech.entity.VehicleEntity;
import com.spacechase0.minecraft.endertech.item.Items;
import com.spacechase0.minecraft.endertech.network.PacketHandler;
import com.spacechase0.minecraft.endertech.tileentity.TubeTileEntity;
import com.spacechase0.minecraft.endertech.tileentity.VehicleTileEntity;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
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
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
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
		registerEntities();
		
		NetworkRegistry.instance().registerGuiHandler( this, gui = new GuiHandler() );
		packets = new PacketHandler();
		MinecraftForge.EVENT_BUS.register( dropHandler = new DropHandler() );
		MinecraftForge.EVENT_BUS.register( containerHandler = new OpenContainerHandler() );
		
		proxy.init();
	}
	
	@EventHandler
	public void postInit( FMLPostInitializationEvent event )
	{
		MinecraftForge.setBlockHarvestLevel( blocks.tube, "pickaxe", 2 );
		MinecraftForge.setBlockHarvestLevel( blocks.ender, "pickaxe", 2 );/*
		MinecraftForge.setBlockHarvestLevel( blocks.vehicleFrame, "pickaxe", 2 );
		MinecraftForge.setBlockHarvestLevel( blocks.vehicleEngine, "pickaxe", 2 );
		MinecraftForge.setBlockHarvestLevel( blocks.vehicleController, "pickaxe", 2 );
		MinecraftForge.setBlockHarvestLevel( blocks.rail, "pickaxe", 2 );
		MinecraftForge.setBlockHarvestLevel( blocks.railEnd, "pickaxe", 2 );*/
		
		config.save();
	}
	
	private void registerTileEntities()
	{
		GameRegistry.registerTileEntity( TubeTileEntity.class, "EnderTube" );
		//GameRegistry.registerTileEntity( VehicleTileEntity.class, "EnderVehicle" );
	}
	
	private void registerOreDictionary()
	{
		OreDictionary.registerOre( "nuggetIron", new ItemStack( items.nugget, 1, 0 ) );
		OreDictionary.registerOre( "nuggetDiamond", new ItemStack( items.nugget, 1, 1 ) );
	}
	
	private void registerRecipes()
	{
		//GameRegistry.addShapelessRecipe( new ItemStack( items.nugget, 9, 0 ), Item.ingotIron );
		GameRegistry.addShapelessRecipe( new ItemStack( items.nugget, 9, 1 ), Item.diamond );
		/*GameRegistry.addRecipe( new ShapedOreRecipe( new ItemStack( Item.ingotIron ),
		                                             "***",
		                                             "***",
		                                             "***",
		                                             '*', "nuggetIron" ) );*/
		GameRegistry.addRecipe( new ShapedOreRecipe( new ItemStack( Item.diamond ),
		                                             "***",
		                                             "***",
		                                             "***",
		                                             '*', "nuggetDiamond" ) );
		GameRegistry.addShapedRecipe( new ItemStack( blocks.tube, 2 ),
		                              "HH_",
		                              "HOH",
		                              "_HH",
		                              'H', Block.hopperBlock,
		                              '_', Block.pressurePlateGold,
		                              'O', blocks.ender );
		
		GameRegistry.addShapelessRecipe( new ItemStack( Item.enderPearl, 9 ), blocks.ender );
		GameRegistry.addShapedRecipe( new ItemStack( blocks.ender ),
		                              "OOO",
		                              "OOO",
		                              "OOO",
		                              'O', Item.eyeOfEnder );/*
		GameRegistry.addShapedRecipe( new ItemStack( blocks.vehicleFrame, 8 ),
		                              "---",
		                              "-B-",
		                              "---",
		                              '-', Item.ingotIron,
		                              'B', blocks.ender );
		GameRegistry.addShapedRecipe( new ItemStack( blocks.rail, 8 ),
		                              "-B-",
		                              'B', blocks.ender,
		                              '-', Item.eyeOfEnder );
		GameRegistry.addShapedRecipe( new ItemStack( blocks.railEnd, 1 ),
		                              "-*-",
		                              "-B-",
		                              "---",
		                              '-', Item.ingotGold,
		                              '*', Item.diamond,
		                              'B', blocks.ender );
		GameRegistry.addShapedRecipe( new ItemStack( blocks.vehicleController, 1 ),
		                              "*E*",
		                              "*B*",
		                              "*R*",
		                              '*', Item.diamond,
		                              'E', Block.blockEmerald,
		                              'R', Block.blockRedstone,
		                              'B', blocks.ender );*/
	}
	
	private void registerEntities()
	{ 
		transportingId = EntityRegistry.findGlobalUniqueEntityId();
		EntityRegistry.registerGlobalEntityID( TransportingEntity.class, "endertech:transporting", transportingId );
		EntityRegistry.registerModEntity( TransportingEntity.class, "endertech:transporting", transportingId, this, 16, 50, true );
		/*
		vehicleId = EntityRegistry.findGlobalUniqueEntityId();
		EntityRegistry.registerGlobalEntityID( TransportingEntity.class, "endertech:vehicle", vehicleId );
		EntityRegistry.registerModEntity( VehicleEntity.class, "endertech:vehicle", vehicleId, this, 64, 20, true );*/
	}
	
	private int transportingId;
	private int vehicleId;
	
	public static Blocks blocks;
	public static Items items;
	public static Configuration config;
	
	private GuiHandler gui;
	private PacketHandler packets;
	private DropHandler dropHandler;
	private OpenContainerHandler containerHandler;
}

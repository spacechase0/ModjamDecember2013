package com.spacechase0.minecraft.endertech.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class VehicleBlock extends BlockContainer
{
	protected VehicleBlock( int id )
	{
		super( id, Material.dragonEgg );
		
		setHardness( 8 );
		setResistance( 8 );
		
		setUnlocalizedName( "endertech:vehicleController" );
		setCreativeTab( CreativeTabs.tabTransport );
	}
	
	@Override
	public void registerIcons( IconRegister register )
	{
		activeIcon = register.registerIcon( "endertech:vehicle_active" );
		inactiveIcon = register.registerIcon( "endertech:vehicle_inactive" );
		
		blockIcon = inactiveIcon;
	}

	@Override
	public TileEntity createNewTileEntity( World world )
	{
		// TODO
		return null;
	}

	public Icon activeIcon;
	public Icon inactiveIcon;
}
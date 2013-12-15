package com.spacechase0.minecraft.endertech.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class RailEndBlock extends SimpleBlock
{
	public RailEndBlock( int id )
	{
		super( id, "railEnd", Material.dragonEgg );
		
		setHardness( 8 );
		setResistance( 8 );
		
		setCreativeTab( CreativeTabs.tabTransport );
	}
	
	// Based off of BlockRotatedPillar
	@Override
    public int onBlockPlaced( World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta )
    {
		System.out.println( "meow? " + side );
		return ForgeDirection.getOrientation( side ).ordinal();
    }
	
	@Override
	public void registerIcons( IconRegister register )
	{
		blockIcon = register.registerIcon( "endertech:railEnd_side" );
		frontIcon = register.registerIcon( "endertech:railEnd_front" );
	}
	
	@Override
    public Icon getIcon( int side, int meta )
	{
		if ( side == meta ) return frontIcon;
		return blockIcon;
	}
	
	public Icon frontIcon;
}

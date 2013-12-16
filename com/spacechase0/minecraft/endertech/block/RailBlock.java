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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class RailBlock extends SimpleBlock
{
	public RailBlock( int id )
	{
		super( id, "rail", Material.dragonEgg );
		
		setHardness( 4 );
		setResistance( 4 );
		
		setCreativeTab( CreativeTabs.tabTransport );
	}

	// Based off of BlockRotatedPillar
	@Override
    public int onBlockPlaced( World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta )
    {
		     if ( side == 0 || side == 1 ) return ORIENT_UD;
		else if ( side == 2 || side == 3 ) return ORIENT_NS;
		else if ( side == 4 || side == 5 ) return ORIENT_EW;
		
		return 0;
    }
	
	public void setBlockBoundsBasedOnState( IBlockAccess world, int x, int y, int z )
	{
		float min = 0.5f - ( 1.f / 16 );
		float max = 0.5f + ( 1.f / 16 );
		
		int meta = world.getBlockMetadata( x, y, z );
		if ( ( meta & ORIENT ) == ORIENT_UD )
		{
			setBlockBounds( min, 0, min, max, 1, max );
		}
		else if ( ( meta & ORIENT ) == ORIENT_NS )
		{
			setBlockBounds( min, min, 0, max, max, 1 );
		}
		else
		{
			setBlockBounds( 0, min, min, 1, max, max );
		}
	}
	
	@Override
	public void setBlockBoundsForItemRender()
	{
		float min = 0.5f - ( 1.f / 16 );
		float max = 0.5f + ( 1.f / 16 );
		
		setBlockBounds( 0, min, min, 1, max, max );
	}
	
	@Override
	public void registerIcons( IconRegister register )
	{
		blockIcon = register.registerIcon( "endertech:rail_offline" );
		onlineIcon = register.registerIcon( "endertech:rail_online" );
	}
	
	@Override
    public Icon getIcon( int side, int meta )
    {
		return ( ( meta & 1 ) != 0 ) ? onlineIcon : blockIcon;
    }
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	public Icon onlineIcon;

	// Bit 1 = online
	// Bit 2/3 = dir
	
	public static final int ONLINE = 0x1;
	public static final int ORIENT = 0x6;
	public static final int ORIENT_UD = 0x0;
	public static final int ORIENT_NS = 0x2;
	public static final int ORIENT_EW = 0x4;
}

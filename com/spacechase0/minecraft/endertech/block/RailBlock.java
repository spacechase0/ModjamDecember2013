package com.spacechase0.minecraft.endertech.block;

import com.spacechase0.minecraft.endertech.EnderTech;

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
import net.minecraftforge.common.ForgeDirection;

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
	
	@Override
    public void onNeighborBlockChange( World world, int x, int y, int z, int neighbor )
	{
		int meta = world.getBlockMetadata( x, y, z );
		int orient = meta & ORIENT;
		
		ForgeDirection[] dirs = null;
		     if ( orient == ORIENT_UD ) dirs = new ForgeDirection[] { ForgeDirection.UP, ForgeDirection.DOWN };
		else if ( orient == ORIENT_NS ) dirs = new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.SOUTH };
		else if ( orient == ORIENT_EW ) dirs = new ForgeDirection[] { ForgeDirection.EAST, ForgeDirection.WEST };
		
		for ( ForgeDirection dir : dirs )
		{
			int ix = x + dir.offsetX;
			int iy = y + dir.offsetY;
			int iz = z + dir.offsetZ;
			
			Block block = Block.blocksList[ world.getBlockId( ix, iy, iz ) ];
			if ( block == EnderTech.blocks.railEnd )
			{
			}
			else if ( block == EnderTech.blocks.rail )
			{
				int metadata = world.getBlockMetadata( ix, iy, iz );
				if ( ( metadata & ONLINE ) == 0 )
				{
					world.setBlockMetadataWithNotify( x, y, z, meta & ~ONLINE, 0x3 );
					return; 
				}
			}
			else
			{
				world.setBlockMetadataWithNotify( x, y, z, meta & ~ONLINE, 0x3 );
				return;
			}
		}
	}
	
	@Override
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

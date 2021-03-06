package com.spacechase0.minecraft.endertech.block;

import java.util.ArrayList;
import java.util.List;

import com.spacechase0.minecraft.endertech.CommonProxy;
import com.spacechase0.minecraft.endertech.EnderTech;
import com.spacechase0.minecraft.endertech.tileentity.TubeTileEntity;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class TubeBlock extends BlockContainer
{
	public TubeBlock( int id )
	{
		super( id, Material.dragonEgg );

		float min = 0.25f, max = 0.75f;
		setBlockBounds( min, min, min, max, max, max );
		setResistance( 10.f );
		setHardness( 10.f );
		
		setUnlocalizedName( "endertech:tube" );
		setCreativeTab( CreativeTabs.tabRedstone );
	}
	
	@Override
	public TileEntity createNewTileEntity( World world )
	{
		// TODO Auto-generated method stub
		return new TubeTileEntity();
	}
	
	@Override
    public boolean onBlockActivated( World world, int x, int y, int z, EntityPlayer player, int sideNum, float offX, float offY, float offZ )
    {
		if ( world.isRemote ) return true;
		TubeTileEntity tube = ( TubeTileEntity ) world.getBlockTileEntity( x, y, z );
		
		ForgeDirection side = ForgeDirection.getOrientation( sideNum );
		
		ItemStack holding = player.getCurrentEquippedItem();
		if ( holding != null )
		{
			boolean didStuff = false;
			/*
			if ( holding.getItem() == EnderTech.items.nugget )
			{
				didStuff = true;
				if ( holding.getItemDamage() == 0 && !tube.hasUpgrade( side, TubeTileEntity.UPGRADE_BLOCK ) )
				{
					tube.upgrade( side, TubeTileEntity.UPGRADE_BLOCK );
					if ( !player.capabilities.isCreativeMode ) --holding.stackSize;
				}
				else if ( holding.getItemDamage() == 1 && !tube.hasUpgrade( side, TubeTileEntity.UPGRADE_FILTER ) )
				{
					tube.upgrade( side, TubeTileEntity.UPGRADE_FILTER );
					if ( !player.capabilities.isCreativeMode ) --holding.stackSize;
				}
			}
			else */
			if ( holding.getItem() == Item.goldNugget && !tube.hasUpgrade( side, TubeTileEntity.UPGRADE_SPEED ) )
			{
				didStuff = true;
				tube.upgrade( side, TubeTileEntity.UPGRADE_SPEED );
				if ( !player.capabilities.isCreativeMode ) --holding.stackSize;
			}
			else if ( holding.getItem() == Item.redstone && !tube.hasUpgrade( side, TubeTileEntity.UPGRADE_REDSTONE ) )
			{
				didStuff = true;
				tube.upgrade( side, TubeTileEntity.UPGRADE_REDSTONE );
				if ( !player.capabilities.isCreativeMode ) --holding.stackSize;
			}
			else if ( CommonProxy.isOre( "nuggetDiamond", holding ) && !tube.hasUpgrade( side,  TubeTileEntity.UPGRADE_FILTER ) )
			{
				didStuff = true;
				tube.upgrade( side, TubeTileEntity.UPGRADE_FILTER );
				if ( !player.capabilities.isCreativeMode ) --holding.stackSize;
			}
			
			if ( didStuff )
			{
				if ( holding.stackSize <= 0 )
				{
					player.setCurrentItemOrArmor( 0, null );
				}
				return true;
			}
		}
		
		float[] coords = new float[] { offX, offY, offZ };
		float[] check = new float[ 2 ];
		for ( int c = 0, k = 0; c < coords.length; ++c )
		{
			if ( coords[ c ] != 0.25 && coords[ c ] != 0.75 )
			{
				check[ k++ ] = coords[ c ];
			}
		}
		
		int mult = 1;
		if ( EnderTech.proxy.isMultiplayer() )
		{
			mult = 2;
		}
		
		boolean toggleOut = false;
		for ( float c : check )
		{
			final double IN_A  = 0.50 + 0.08 * mult;
			final double OUT_A = 0.50 + 0.14 * mult;
			final double IN_B  = 0.50 - 0.14 * mult;
			final double OUT_B = 0.50 - 0.08 * mult;
			if ( ( c >= IN_A && c <= OUT_A ) ||
			     ( c >= IN_B && c <= OUT_B ) )
			{
				toggleOut = true;
			}
		}
		if ( toggleOut )
		{
			if ( holding == null && tube.hasUpgrade( side, TubeTileEntity.UPGRADE_FILTER ) )
			{
				player.openGui( EnderTech.instance, sideNum | 0x8, world, x, y, z );
			}
			else
			{
				tube.toggleOutput( side );
			}
			return true;
		}
		
		final double LOW  = 0.50 - 0.06 * mult;
		final double HIGH = 0.50 + 0.06 * mult;
		if ( check[ 0 ] >= LOW && check[ 0 ] <= HIGH && check[ 1 ] >= LOW && check[ 1 ] <= HIGH )
		{
			if ( holding == null && tube.hasUpgrade( side, TubeTileEntity.UPGRADE_FILTER ) )
			{
				player.openGui( EnderTech.instance, sideNum, world, x, y, z );
			}
			else
			{
				tube.toggleInput( side );
			}
			return true;
		}
		
        return false;
    }
	
	@Override
    public void breakBlock( World world, int x, int y, int z, int oldId, int oldMeta )
	{
		TubeTileEntity tube = ( TubeTileEntity ) world.getBlockTileEntity( x, y, z );
		tube.dropBuffer();

		// TODO: Do properly and not make these drop if in creative mode
		List< ItemStack > drops = new ArrayList< ItemStack >();
		for ( ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS )
		{
			if ( tube.hasUpgrade( dir, TubeTileEntity.UPGRADE_SPEED ) )
			{
				drops.add( new ItemStack( Item.goldNugget ) );
			}
			if ( tube.hasUpgrade( dir, TubeTileEntity.UPGRADE_FILTER ) )
			{
				drops.add( new ItemStack( EnderTech.items.nugget, 1, 1 ) );
			}
			if ( tube.hasUpgrade( dir, TubeTileEntity.UPGRADE_BLOCK ) )
			{
				drops.add( new ItemStack( EnderTech.items.nugget, 1, 0 ) );
			}
			if ( tube.hasUpgrade( dir, TubeTileEntity.UPGRADE_REDSTONE ) )
			{
				drops.add( new ItemStack( Item.redstone ) );
			}
		}
		
		for ( ItemStack stack : drops )
		{
			EntityItem entity = new EntityItem( world, x + 0.5, y + 0.5, z + 0.5, stack );
			world.spawnEntityInWorld( entity );
		}
		
		super.breakBlock( world, x, y, z, oldId, oldMeta );
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
	
	@Override
	public void registerIcons( IconRegister register )
	{
		blockIcon = register.registerIcon( "endertech:tube/frame" );
	}
}

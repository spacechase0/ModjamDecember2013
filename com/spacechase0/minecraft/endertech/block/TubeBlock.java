package com.spacechase0.minecraft.endertech.block;

import com.spacechase0.minecraft.endertech.EnderTech;
import com.spacechase0.minecraft.endertech.tileentity.TubeTileEntity;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
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
		System.out.println(offX + " "+offY+" "+offZ);
		
		ForgeDirection side = ForgeDirection.getOrientation( sideNum );
		if ( side == ForgeDirection.EAST || side == ForgeDirection.WEST )
		{
			// Weirdness
			side = side.getOpposite();
		}
		
		ItemStack holding = player.getCurrentEquippedItem();
		if ( holding != null )
		{
			if ( holding.getItem() == EnderTech.items.nugget )
			{
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
			else if ( holding.getItem() == Item.goldNugget && !tube.hasUpgrade( side, TubeTileEntity.UPGRADE_SPEED ) )
			{
				tube.upgrade( side, TubeTileEntity.UPGRADE_SPEED );
				if ( !player.capabilities.isCreativeMode ) --holding.stackSize;
			}
			else if ( holding.getItem() == Item.redstone && !tube.hasUpgrade( side, TubeTileEntity.UPGRADE_REDSTONE ) )
			{
				tube.upgrade( side, TubeTileEntity.UPGRADE_REDSTONE );
				if ( !player.capabilities.isCreativeMode ) --holding.stackSize;
			}
			
			if ( holding.stackSize <= 0 )
			{
				player.setCurrentItemOrArmor( 0, null );
			}
			return true;
		}
		else if ( tube.hasUpgrade( side, TubeTileEntity.UPGRADE_FILTER ) )
		{
			// open gui
			return true;
		}
		
		System.out.println( side.offsetX +" "+side.offsetY+" "+side.offsetZ);
		
        return false;
    }
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public void registerIcons( IconRegister register )
	{
		blockIcon = register.registerIcon( "endertech:tube/frame" );
	}
}

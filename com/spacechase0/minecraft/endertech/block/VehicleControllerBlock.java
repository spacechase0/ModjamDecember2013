package com.spacechase0.minecraft.endertech.block;

import java.util.ArrayList;
import java.util.List;

import com.spacechase0.minecraft.endertech.tileentity.VehicleTileEntity;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class VehicleControllerBlock extends BlockContainer
{
	protected VehicleControllerBlock( int id )
	{
		super( id, Material.dragonEgg );
		
		setHardness( 8 );
		setResistance( 8 );
		
		setUnlocalizedName( "endertech:vehicleController" );
		setCreativeTab( CreativeTabs.tabTransport );
	}

	@Override
	public TileEntity createNewTileEntity( World world )
	{
		return new VehicleTileEntity();
	}
	
	@Override
    public ArrayList< ItemStack > getBlockDropped( World world, int x, int y, int z, int meta, int fortune )
    {
		ItemStack stack = new ItemStack( this );
		if ( meta == 1 )
		{
			stack.setItemDamage( 1 );
			
			NBTTagCompound tag = new NBTTagCompound();
			world.getBlockTileEntity( x, y, z ).writeToNBT( tag );
			tag.removeTag( "id" );
			tag.removeTag( "x" );
			tag.removeTag( "y" );
			tag.removeTag( "z" );
			
			stack.setTagCompound( tag );
		}
		
		ArrayList< ItemStack > stacks = new ArrayList< ItemStack >();
		stacks.add( stack );
		
		return stacks;
    }
	
	@Override
	public void registerIcons( IconRegister register )
	{
		activeIcon = register.registerIcon( "endertech:vehicle_active" );
		inactiveIcon = register.registerIcon( "endertech:vehicle_inactive" );
		disabledIcon = register.registerIcon( "endertech:vehicle_disabled" );
		
		blockIcon = disabledIcon;
	}
	
	@Override
    public Icon getBlockTexture( IBlockAccess access, int x, int y, int z, int side )
    {
		switch ( access.getBlockMetadata( x, y, z ) )
		{
			case 0: return disabledIcon;
			case 1: return inactiveIcon;
			case 2: return activeIcon;
		}
		
		return disabledIcon;
    }

	public Icon activeIcon;
	public Icon inactiveIcon;
	public Icon disabledIcon;
}

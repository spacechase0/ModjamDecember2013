package com.spacechase0.minecraft.endertech.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

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
		return null;
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

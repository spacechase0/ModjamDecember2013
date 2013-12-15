package com.spacechase0.minecraft.endertech.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;

public class SimpleBlock extends Block
{
	public SimpleBlock( int id, String theName, Material mat )
	{
		super( id, mat );
		name = theName;
		
		setUnlocalizedName( "endertech:" + name );
	}
	
	@Override
	public void registerIcons( IconRegister register )
	{
		blockIcon = register.registerIcon( "endertech:" + name );
	}
	
	private final String name;
}

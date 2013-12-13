package com.spacechase0.minecraft.endertech.client;

import com.spacechase0.minecraft.endertech.CommonProxy;
import com.spacechase0.minecraft.endertech.client.render.tileentity.TubeTileEntityRenderer;
import com.spacechase0.minecraft.endertech.tileentity.TubeTileEntity;

import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void init()
	{
		ClientRegistry.bindTileEntitySpecialRenderer( TubeTileEntity.class, new TubeTileEntityRenderer() );
	}
}

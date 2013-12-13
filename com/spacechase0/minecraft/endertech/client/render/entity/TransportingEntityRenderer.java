package com.spacechase0.minecraft.endertech.client.render.entity;

import com.spacechase0.minecraft.endertech.entity.TransportingEntity;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.ResourceLocation;

public class TransportingEntityRenderer extends Render
{
	@Override
	public void doRender( Entity entity_, double x, double y, double z, float f1, float f2 )
	{
		TransportingEntity entity = ( TransportingEntity ) entity_;

		if ( entity.getItemStack() == null || entity.getItemStack().itemID == Block.pistonMoving.blockID )
		{
			return;
		}
		
		EntityItem fake = new EntityItem( entity.worldObj, entity.posX, entity.posY, entity.posZ, entity.getItemStack() );
		render.doRenderItem( fake, x, y, z, f1, f2 );
	}

	@Override
	protected ResourceLocation getEntityTexture( Entity entity )
	{
		return null;
	}
	
	private RenderItem render = new RenderItem();
}

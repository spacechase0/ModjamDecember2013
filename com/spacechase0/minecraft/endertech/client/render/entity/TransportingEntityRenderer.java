package com.spacechase0.minecraft.endertech.client.render.entity;

import com.spacechase0.minecraft.endertech.entity.TransportingEntity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class TransportingEntityRenderer extends Render
{
	@Override
	public void doRender( Entity entity_, double x, double y, double d2z, float f, float f1 )
	{
		TransportingEntity entity = ( TransportingEntity ) entity_;
		// TODO
	}

	@Override
	protected ResourceLocation getEntityTexture( Entity entity )
	{
		return null;
	}
	
}

package com.spacechase0.minecraft.endertech.client;

import net.minecraftforge.common.MinecraftForge;

import com.spacechase0.minecraft.endertech.CommonProxy;
import com.spacechase0.minecraft.endertech.client.render.entity.TransportingEntityRenderer;
import com.spacechase0.minecraft.endertech.client.render.entity.VehicleEntityRenderer;
import com.spacechase0.minecraft.endertech.client.render.tileentity.TubeTileEntityRenderer;
import com.spacechase0.minecraft.endertech.entity.TransportingEntity;
import com.spacechase0.minecraft.endertech.entity.VehicleEntity;
import com.spacechase0.minecraft.endertech.tileentity.TubeTileEntity;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void init()
	{
		ClientRegistry.bindTileEntitySpecialRenderer( TubeTileEntity.class, new TubeTileEntityRenderer() );
		RenderingRegistry.registerEntityRenderingHandler( TransportingEntity.class, new TransportingEntityRenderer() );
		RenderingRegistry.registerEntityRenderingHandler( VehicleEntity.class, new VehicleEntityRenderer() );
		
		MinecraftForge.EVENT_BUS.register( tooltipHandler = new TooltipHandler() );
	}
	
	private TooltipHandler tooltipHandler;
}

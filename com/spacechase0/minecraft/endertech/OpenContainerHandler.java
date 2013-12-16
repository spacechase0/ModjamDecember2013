package com.spacechase0.minecraft.endertech;

import java.lang.reflect.Field;

import com.spacechase0.minecraft.endertech.world.FakeWorld;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;

public class OpenContainerHandler
{
	@ForgeSubscribe
	public void playerOpenedContainer( PlayerOpenContainerEvent event )
	{
		Container container = event.entityPlayer.openContainer;
		if ( container instanceof ContainerFurnace )
		{
			TileEntity te = get( container, 0 );
			if ( te != null && te.worldObj instanceof FakeWorld )
			{
				event.setResult( Result.ALLOW );
			}
		}
	}
	
	private TileEntity get( Container container, int num )
	{
		try
		{
			Field f = container.getClass().getDeclaredFields()[ num ];
			f.setAccessible( true );
			return ( TileEntity ) f.get( container );
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
			return null;
		}
	}
}

package com.spacechase0.minecraft.endertech;

import java.lang.reflect.Field;

import com.spacechase0.minecraft.endertech.world.FakeWorld;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;

public class OpenContainerHandler
{
	@ForgeSubscribe
	public void playerOpenedContainer( PlayerOpenContainerEvent event )
	{
		Container container = event.entityPlayer.openContainer;
		World world = null;
		if ( container instanceof ContainerFurnace || container instanceof ContainerBrewingStand || container instanceof ContainerDispenser )
		{
			TileEntity te = ( TileEntity ) get( container, 0 );
			if ( te != null )
			{
				world = te.worldObj;
			}
		}
		else if ( container instanceof ContainerChest )
		{
			IInventory inv = ( IInventory ) get( container, 0 );
			if ( inv instanceof TileEntityChest )
			{
				world = ( ( TileEntity ) inv ).worldObj;
			}
			else if ( inv instanceof InventoryLargeChest )
			{
				if ( true ) return; // BROKEN
				Object obj = get( container, 1 );
				world = ( ( TileEntity )( obj instanceof TileEntity ? obj : get( container, 2 ) ) ).worldObj;
			}
		}
		else if ( container instanceof ContainerWorkbench )
		{
			world = ( World ) get( container, 2 );
		}
		
		if ( world instanceof FakeWorld )
		{
			event.setResult( Result.ALLOW );
		}
	}
	
	private Object get( Container container, int num )
	{
		try
		{
			Field f = container.getClass().getDeclaredFields()[ num ];
			f.setAccessible( true );
			return f.get( container );
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
			return null;
		}
	}
}

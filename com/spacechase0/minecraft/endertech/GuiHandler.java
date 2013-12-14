package com.spacechase0.minecraft.endertech;

import com.spacechase0.minecraft.endertech.client.gui.FilterGui;
import com.spacechase0.minecraft.endertech.inventory.FilterContainer;
import com.spacechase0.minecraft.endertech.inventory.TubeFilterInventory;
import com.spacechase0.minecraft.endertech.tileentity.TubeTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	// Is it cheating if I use the the ID for other data? :P
	
	@Override
	public Object getServerGuiElement( int id, EntityPlayer player, World world, int x, int y, int z )
	{
		TileEntity te = world.getBlockTileEntity( x, y, z );
		if ( te instanceof TubeTileEntity )
		{
			TubeTileEntity tube = ( TubeTileEntity ) te;
			return new FilterContainer( getInventory( tube, id ), player );
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement( int id, EntityPlayer player, World world, int x, int y, int z )
	{
		TileEntity te = world.getBlockTileEntity( x, y, z );
		if ( te instanceof TubeTileEntity )
		{
			TubeTileEntity tube = ( TubeTileEntity ) te;
			return new FilterGui( new FilterContainer( getInventory( tube, id ), player ) );
		}
		
		return null;
	}
	
	private TubeFilterInventory getInventory( TubeTileEntity tube, int id )
	{
		ForgeDirection side = getSide( id );
		TubeFilterInventory inv = new TubeFilterInventory( tube, isInput( id ) ? tube.getInputFilterIndex( side ) : tube.getOutputFilterIndex( side ) );
		return inv;
	}
	
	private ForgeDirection getSide( int id )
	{
		return ForgeDirection.getOrientation( id & 0x7 );
	}
	
	private boolean isInput( int id )
	{
		return ( ( id & 0x8 ) == 0 );
	}
}

package com.spacechase0.minecraft.endertech.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class VehicleFrameBlock extends SimpleBlock
{
	public VehicleFrameBlock( int id )
	{
		super( id, "vehicleFrame", Material.dragonEgg );
		
		setHardness( 6 );
		setResistance( 6 );
		setCreativeTab( CreativeTabs.tabTransport );
	}
	
	@Override
    public boolean onBlockActivated( World world, int x, int y, int z, EntityPlayer player, int sideNum, float offX, float offY, float offZ )
    {
		if ( world.isRemote ) return true;
		
		boolean down  = ( world.getBlockId( x + 0, y - 1, z + 0 ) == blockID );
		boolean up    = ( world.getBlockId( x + 0, y + 1, z + 0 ) == blockID );
		boolean north = ( world.getBlockId( x + 0, y + 0, z - 1 ) == blockID );
		boolean south = ( world.getBlockId( x + 0, y + 0, z + 1 ) == blockID );
		boolean west  = ( world.getBlockId( x - 1, y + 0, z + 0 ) == blockID );
		boolean east  = ( world.getBlockId( x + 1, y + 0, z + 0 ) == blockID );
		
		int amt = 0;
		if ( down  ) ++amt;
		if ( up    ) ++amt;
		if ( north ) ++amt;
		if ( south ) ++amt;
		if ( west  ) ++amt;
		if ( east  ) ++amt;
		
		if ( ( up && down ) || ( north && south ) || ( east && west ) || amt != 3 )
		{
			player.sendChatToPlayer( ChatMessageComponent.createFromTranslationKey( "chat.endertech:vehicleFrame.notCorner" ) );
			return true;
		}
		
		int len = 0;
		boolean firstRun = true;
		Vec3[] corners = new Vec3[ 3 ];
		int currCorner = 0;
		for ( ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS )
		{
			int thisLen = 0;
			for ( int i = 0; i < 18; ++i )
			{
				int ix = x + ( dir.offsetX * i );
				int iy = y + ( dir.offsetY * i );
				int iz = z + ( dir.offsetZ * i );
				
				if ( world.getBlockId( ix, iy, iz ) == blockID )
				{
					++thisLen;
				}
				else
				{
					corners[ currCorner++ ] = Vec3.createVectorHelper( x, y, z );
					break;
				}
			}
			
			if ( thisLen <= 1 ) continue;
			
			if ( firstRun )
			{
				len = thisLen;
				firstRun = false;
			}
			else if ( thisLen != len )
			{
				if ( !partialCheck ) player.sendChatToPlayer( ChatMessageComponent.createFromTranslationKey( "chat.endertech:vehicleFrame.mustBeSquare" ) );
				return true;
			}
		}
		
		if ( len <= 5 )
		{
			if ( !partialCheck ) player.sendChatToPlayer( ChatMessageComponent.createFromTranslationKey( "chat.endertech:vehicleFrame.tooSmall" ) );
			return true;
		}
		
		if ( !partialCheck ) player.sendChatToPlayer( ChatMessageComponent.createFromText( "Good for now" ) );
		return true;
    }
	
	private static boolean partialCheck = false;
}

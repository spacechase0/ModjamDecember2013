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
			//if ( !fakeCheck ) player.sendChatToPlayer( ChatMessageComponent.createFromTranslationKey( "chat.endertech:vehicleFrame.notCorner" ) );
			return ( fakeCheck ? false : /*true*/false );
		}
		
		int len = fakeCheck ? fakeLen : 0;
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
					if ( thisLen > 1 ) corners[ currCorner++ ] = Vec3.createVectorHelper( ix - dir.offsetX, iy - dir.offsetY, iz - dir.offsetZ );
					break;
				}
			}
			
			if ( thisLen <= 1 ) continue;
			
			if ( firstRun && fakeLen == -1 )
			{
				len = thisLen;
				firstRun = false;
			}
			else if ( thisLen != len )
			{
				if ( !fakeCheck ) player.sendChatToPlayer( ChatMessageComponent.createFromTranslationKey( "chat.endertech:vehicleFrame.mustBeSquare" ) );
				return ( fakeCheck ? false : true );
			}
		}
		
		if ( len < 5 || currCorner < 3 )
		{
			if ( !fakeCheck ) player.sendChatToPlayer( ChatMessageComponent.createFromTranslationKey( "chat.endertech:vehicleFrame.tooSmall" ) );
			return ( fakeCheck ? false : true );
		}
		
		if ( fakeCheck ) return true;
		
		fakeCheck = true;
		fakeLen = len;
		boolean result = onBlockActivated( world, ( int ) corners[ 2 ].xCoord, ( int ) corners[ 0 ].yCoord, ( int ) corners[ 1 ].zCoord, player, sideNum, offX, offY, offZ );
		fakeCheck = false;
		fakeLen = -1;
		if ( !result )
		{
			player.sendChatToPlayer( ChatMessageComponent.createFromTranslationKey( "chat.endertech:vehicleFrame.oppositeSideInvalid" ) );
			return true;
		}
		
		if ( !fakeCheck ) player.sendChatToPlayer( ChatMessageComponent.createFromText( "Good for now" ) );
		return true;
    }
	
	private static boolean fakeCheck = false;
	private static int fakeLen = -1;
}

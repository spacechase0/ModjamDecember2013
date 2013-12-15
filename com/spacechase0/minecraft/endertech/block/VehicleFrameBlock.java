package com.spacechase0.minecraft.endertech.block;

import com.spacechase0.minecraft.endertech.EnderTech;
import com.spacechase0.minecraft.endertech.tileentity.VehicleTileEntity;

import net.minecraft.block.Block;
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
		
		int minX = Math.min( x + 1, ( int ) corners[ 2 ].xCoord + 1 );
		int minY = Math.min( y + 1, ( int ) corners[ 0 ].yCoord + 1 );
		int minZ = Math.min( z + 1, ( int ) corners[ 1 ].zCoord + 1 );
		int maxX = Math.max( x + 0, ( int ) corners[ 2 ].xCoord + 0 );
		int maxY = Math.max( y + 0, ( int ) corners[ 0 ].yCoord + 0 );
		int maxZ = Math.max( z + 0, ( int ) corners[ 1 ].zCoord + 0 );
		
		int contrX = 0, contrY = 0, contrZ = 0;
		int contrCount = 0;
		int engineCount = 0;
		for ( int ix = minX; ix < maxX; ++ix )
		{
			for ( int iy = minY; iy < maxY; ++iy )
			{
				for ( int iz = minZ; iz < maxZ; ++iz )
				{
					Block block = Block.blocksList[ world.getBlockId( ix, iy, iz ) ];
					if ( block == EnderTech.blocks.vehicleController )
					{
						if ( world.getBlockMetadata( ix, iy, iz ) != 0 ) continue;
						contrX = ix;
						contrY = iy;
						contrZ = iz;
						++contrCount;
					}
					else if ( block == EnderTech.blocks.vehicleEngine )
					{
						++engineCount;
					}
				}
			}
		}
		
		if ( contrCount != 1 )
		{
			player.sendChatToPlayer( ChatMessageComponent.createFromTranslationKey( "chat.endertech:vehicleFrame.singleController" ) );
			return true;
		}
		else if ( engineCount != 2 )
		{
			player.sendChatToPlayer( ChatMessageComponent.createFromTranslationKey( "chat.endertech:vehicleFrame.twoEngines" ) );
			return true;
		}
		
		boolean foundEnginePair = false;
		ForgeDirection[] toCheck = new ForgeDirection[] { ForgeDirection.UP, ForgeDirection.SOUTH, ForgeDirection.EAST };
		for ( ForgeDirection check : toCheck )
		{
			ForgeDirection oppos = check.getOpposite();

			Block blockA = Block.blocksList[ world.getBlockId( contrX + check.offsetX, contrY + check.offsetY, contrZ + check.offsetZ ) ];
			Block blockB = Block.blocksList[ world.getBlockId( contrX + oppos.offsetX, contrY + oppos.offsetY, contrZ + oppos.offsetZ ) ];
			
			if ( blockA == EnderTech.blocks.vehicleEngine && blockB == EnderTech.blocks.vehicleEngine )
			{
				foundEnginePair = true;
				break;
			}
		}
		
		if ( !foundEnginePair )
		{
			player.sendChatToPlayer( ChatMessageComponent.createFromTranslationKey( "chat.endertech:vehicleFrame.twoEngines" ) );
			return true;
		}
		
		VehicleTileEntity te = ( VehicleTileEntity ) world.getBlockTileEntity( contrX, contrY, contrZ );
		te.setVehicle( minX, minY, minZ, maxX, maxY, maxZ );
		world.setBlockMetadataWithNotify( contrX, contrY, contrZ, 1, 0x2 );
		
		return true;
    }
	
	private static boolean fakeCheck = false;
	private static int fakeLen = -1;
}

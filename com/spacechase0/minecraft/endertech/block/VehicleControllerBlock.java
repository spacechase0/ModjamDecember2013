package com.spacechase0.minecraft.endertech.block;

import java.util.ArrayList;
import java.util.List;

import com.spacechase0.minecraft.endertech.EnderTech;
import com.spacechase0.minecraft.endertech.tileentity.VehicleTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class VehicleControllerBlock extends BlockContainer
{
	protected VehicleControllerBlock( int id )
	{
		super( id, Material.dragonEgg );
		
		setHardness( 8 );
		setResistance( 8 );
		
		setUnlocalizedName( "endertech:vehicleController" );
		setCreativeTab( CreativeTabs.tabTransport );
	}

	@Override
	public TileEntity createNewTileEntity( World world )
	{
		return new VehicleTileEntity();
	}
	
	@Override
    public void breakBlock( World world, int x, int y, int z, int oldId, int oldMeta )
    {
		ItemStack stack = new ItemStack( this );
		if ( oldMeta == 1 )
		{
			stack.setItemDamage( 1 );
			
			NBTTagCompound tag = new NBTTagCompound();
			world.getBlockTileEntity( x, y, z ).writeToNBT( tag );
			tag.removeTag( "id" );
			tag.removeTag( "x" );
			tag.removeTag( "y" );
			tag.removeTag( "z" );
			
			stack.setTagCompound( tag );
			
			EntityItem entity = new EntityItem( world, x + 0.5, y + 0.5, z + 0.5, stack );
			world.spawnEntityInWorld( entity );
		}
		
		super.breakBlock( world, x, y, z, oldId, oldMeta );
    }
	
	@Override
    public void onBlockPlacedBy( World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack )
	{
		if ( stack.getItemDamage() == 1 )
		{
			VehicleTileEntity te = ( VehicleTileEntity ) world.getBlockTileEntity( x, y, z );
			
			NBTTagCompound tmp = new NBTTagCompound();
			te.writeToNBT( tmp );
			
			NBTTagCompound tag = ( NBTTagCompound ) stack.getTagCompound().copy();
			tag.setString( "id", tmp.getString( "id" ) );
			tag.setInteger( "x", tmp.getInteger( "x" ) );
			tag.setInteger( "y", tmp.getInteger( "y" ) );
			tag.setInteger( "z", tmp.getInteger( "z" ) );
			
			te.readFromNBT( tag );
		}
	}
	
	@Override
    public boolean onBlockActivated( World world, int x, int y, int z, EntityPlayer player, int side, float offX, float offY, float offZ )
    {
		int meta = world.getBlockMetadata( x, y, z );
		if ( meta == 0 )
		{
			return false;
		}
		
		if ( world.isRemote ) return true;
		
		ForgeDirection railDir = null;
		int railCount = 0;
		for ( ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS )
		{
			if ( isRail( getBlockIn( world, x, y, z, dir ) ) )
			{
				railDir = dir;
				++railCount;
			}
		}
		
		if ( railCount != 2 || !isRail( getBlockIn( world, x, y, z, railDir.getOpposite() ) ) )
		{
			player.sendChatToPlayer( ChatMessageComponent.createFromTranslationKey( "chat.endertech:vehicleController.oppositeRails" ) );
			return true;
		}
		
		VehicleTileEntity te = ( VehicleTileEntity ) world.getBlockTileEntity( x, y, z );
		
		int size = te.getSize();
		int offD = ( int ) -Math.pow( size, 2 );
		int offU = ( int ) +Math.pow( size, 2 );
		int offN = ( int ) -Math.pow( size, 3 );
		int offS = ( int ) +Math.pow( size, 3 );
		int offW = ( int ) -1;
		int offE = ( int ) +1;
		int[] off = new int[] { offD, offU, offN, offS, offW, offE };
		
		boolean[] good = new boolean[ off.length ];
		for ( int i = 0; i < off.length; ++i )
		{
			int arrayOffset = off[ i ];
			if ( te.getBlockId( te.getData()[ te.getEmbeddedIndex() ] ) == EnderTech.blocks.vehicleEngine.blockID )
			{
				good[ i ] = true;
			}
		}
		
		if ( !( good[ railDir.ordinal() ] && good[ railDir.getOpposite().ordinal() ] ) )
		{
			player.sendChatToPlayer( ChatMessageComponent.createFromTranslationKey( "chat.endertech:vehicleController.nonMatchingRails" ) );
			return true;
		}
		
        return true;
    }
	
	@Override
	public void registerIcons( IconRegister register )
	{
		activeIcon = register.registerIcon( "endertech:vehicle_active" );
		inactiveIcon = register.registerIcon( "endertech:vehicle_inactive" );
		disabledIcon = register.registerIcon( "endertech:vehicle_disabled" );
		
		blockIcon = disabledIcon;
	}
	
	@Override
    public Icon getIcon( int side, int meta )
    {
		switch ( meta )
		{
			case 0: return disabledIcon;
			case 1: return inactiveIcon;
			case 2: return activeIcon;
		}
		
		return disabledIcon;
    }
	
	private Block getBlockIn( World world, int x, int y, int z, ForgeDirection dir )
	{
		return Block.blocksList[ world.getBlockId( x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ ) ];
	}
	
	private boolean isRail( Block block )
	{
		return ( block == EnderTech.blocks.rail || block == EnderTech.blocks.railEnd );
	}

	public Icon activeIcon;
	public Icon inactiveIcon;
	public Icon disabledIcon;
}

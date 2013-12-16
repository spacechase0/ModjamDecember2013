package com.spacechase0.minecraft.endertech.entity;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.spacechase0.minecraft.endertech.EnderTech;
import com.spacechase0.minecraft.endertech.block.RailBlock;
import com.spacechase0.minecraft.endertech.tileentity.VehicleTileEntity;
import com.spacechase0.minecraft.endertech.world.FakeWorld;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class VehicleEntity extends Entity implements IEntityAdditionalSpawnData
{
	public VehicleEntity( World world )
	{
		super( world );
		setSize( 3, 3 );
		
		noClip = true;
	}
	
	public VehicleEntity( World world, VehicleTileEntity vehicle )
	{
		this( world );
		
		size = vehicle.getSize();
		contrX = vehicle.getEmbeddedX();
		contrY = vehicle.getEmbeddedY();
		contrZ = vehicle.getEmbeddedZ();
		fakeWorld = new FakeWorld( this );
		fakeWorld.loadFrom( vehicle );
		
		setSize( size, size );
	}

	@Override
	public void entityInit()
	{
	}
	
	@Override
	public void onUpdate()
	{
		//super.onUpdate();
		
		setPosition( posX + motionX, posY + motionY, posZ + motionZ );
		
		if ( !worldObj.isRemote )
		{
			float halfSize = size / 2.f;
			int bx = ( int )( posX - halfSize + contrX );
			int by = ( int )( posY + contrY );
			int bz = ( int )( posZ - halfSize + contrZ );
			
			Block b = Block.blocksList[ worldObj.getBlockId( bx, by, bz ) ];
			if ( b != EnderTech.blocks.rail || ( worldObj.getBlockMetadata( bx, by, bz ) & RailBlock.ONLINE ) == 0 )
			{
				// TODO: Place back in block
				worldObj.removeEntity( this );
			}
		}
		
		fakeWorld.isRemote = worldObj.isRemote;
		fakeWorld.tick();
	}
	
	@Override
    protected boolean pushOutOfBlocks( double x, double y, double z )
	{
		return true;
	}

	@Override
    public void setPositionAndRotation2( double par1, double par3, double par5, float par7, float par8, int par9 )
    {
        this.setPosition(par1, par3, par5);
        this.setRotation(par7, par8);
    }

	@Override
	public void readEntityFromNBT( NBTTagCompound tag )
	{
		size = tag.getInteger( "Size" );
		fakeWorld = new FakeWorld( this );
		fakeWorld.loadFrom( tag );

		motionZ = tag.getDouble( "VelocityX" );
		motionY = tag.getDouble( "VelocityY" );
		motionZ = tag.getDouble( "VelocityZ" );
		
		setSize( size, size );
	}

	@Override
	public void writeEntityToNBT( NBTTagCompound tag )
	{
		tag.setInteger( "Size", size );
		fakeWorld.saveTo( tag );

		tag.setDouble( "VelocityX", motionX );
		tag.setDouble( "VelocityY", motionY );
		tag.setDouble( "VelocityZ", motionZ );

		setSize( size, size );
	}
	
	@Override
	public void writeSpawnData( ByteArrayDataOutput data )
	{
		NBTTagCompound tag = new NBTTagCompound();
		writeEntityToNBT( tag );

		System.out.println(tag);
		try
		{
			NBTBase.writeNamedTag( tag, data );
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
		}
	}

	@Override
	public void readSpawnData( ByteArrayDataInput data )
	{
		try
		{
			NBTTagCompound tag = ( NBTTagCompound ) NBTBase.readNamedTag( data );
			System.out.println(tag);
			readEntityFromNBT( tag );
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
		}
	}
	
	public int getSize()
	{
		return size;
	}
	
	public int getEmbeddedX()
	{
		return contrX;
	}
	
	public int getEmbeddedY()
	{
		return contrY;
	}
	
	public int getEmbeddedZ()
	{
		return contrZ;
	}
	
	public World getFakeWorld()
	{
		return fakeWorld;
	}

	private int size = -1;
	private int contrX = -1, contrY = -1, contrZ = -1;
	private FakeWorld fakeWorld;
}

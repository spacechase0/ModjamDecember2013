package com.spacechase0.minecraft.endertech.entity;

import com.spacechase0.minecraft.endertech.tileentity.VehicleTileEntity;
import com.spacechase0.minecraft.endertech.world.FakeWorld;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class VehicleEntity extends Entity
{
	public VehicleEntity( World world )
	{
		super( world );
		setSize( 3, 3 );
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
		super.onUpdate();
		
		setPosition( posX + motionX, posY + motionY, posZ + motionZ );
	}

	@Override
	public void readEntityFromNBT( NBTTagCompound tag )
	{
		size = tag.getInteger( "Size" );
		fakeWorld.loadFrom( tag );
		
		System.out.println("read size of " +size+" "+worldObj.isRemote);
		setSize( size, size );
	}

	@Override
	public void writeEntityToNBT( NBTTagCompound tag )
	{
		tag.setInteger( "Size", size );
		fakeWorld.saveTo( tag );

		System.out.println("wrote size of " +size+" "+worldObj.isRemote);
		setSize( size, size );
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

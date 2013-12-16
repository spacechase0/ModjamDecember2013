package com.spacechase0.minecraft.endertech.entity;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
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

public class VehicleEntity extends Entity// implements IEntityAdditionalSpawnData
{
	public VehicleEntity( World world )
	{
		super( world );
		setSize( 3, 3 );
		
		System.out.println("created with " + world.isRemote);
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
		//System.out.println("update:"+worldObj.isRemote);
		setPosition( posX + motionX, posY + motionY, posZ + motionZ );
	}

	@Override
	public void readEntityFromNBT( NBTTagCompound tag )
	{/*
		size = tag.getInteger( "Size" );
		fakeWorld = new FakeWorld( this );
		fakeWorld.loadFrom( tag );
		
		//System.out.println("read size of " +size+" "+worldObj.isRemote);
		setSize( size, size );*/
	}

	@Override
	public void writeEntityToNBT( NBTTagCompound tag )
	{/*
		tag.setInteger( "Size", size );
		fakeWorld.saveTo( tag );

		//System.out.println("wrote size of " +size+" "+worldObj.isRemote);
		setSize( size, size );*/
	}
	
	//@Override
	public void writeSpawnData( ByteArrayDataOutput data )
	{
		//System.out.println("writing spawn data");
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

	//@Override
	public void readSpawnData( ByteArrayDataInput data )
	{
		//System.out.println("reading spawn data");
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

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
		fakeWorld = new FakeWorld( this );
		fakeWorld.loadFrom( vehicle );
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
	protected void readEntityFromNBT( NBTTagCompound tag )
	{
		size = tag.getInteger( "Size" );
		fakeWorld.loadFrom( tag );
	}

	@Override
	protected void writeEntityToNBT( NBTTagCompound tag )
	{
		tag.setInteger( "Size", size );
		fakeWorld.saveTo( tag );
	}

	private int size = -1;
	private FakeWorld fakeWorld;
}

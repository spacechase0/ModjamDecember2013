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
		fakeWorld = new FakeWorld( this );
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
	}

	@Override
	protected void writeEntityToNBT( NBTTagCompound tag )
	{
	}

	private World fakeWorld;
}

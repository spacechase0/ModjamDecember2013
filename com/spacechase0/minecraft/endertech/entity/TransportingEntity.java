package com.spacechase0.minecraft.endertech.entity;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class TransportingEntity extends Entity
{
	public TransportingEntity( World world )
	{
		super( world );
	}
	
	public TransportingEntity( World world, ItemStack theStack, ForgeDirection theDir, float speed )
	{
		this( world );
		
		stack = theStack;
		setDirection( theDir, speed );
	}

	@Override
	protected void entityInit()
	{
	}

	@Override
	protected void readEntityFromNBT( NBTTagCompound tag )
	{
		NBTTagCompound stackTag = new NBTTagCompound();
		stack.writeToNBT( stackTag );
		tag.setTag( "Item", stackTag );
		
		dir = ForgeDirection.getOrientation( tag.getInteger( "Direction" ) );
		distanceTraveled = tag.getInteger( "DistanceTraveled" );
	}

	@Override
	protected void writeEntityToNBT( NBTTagCompound tag )
	{
		NBTTagCompound stackTag = ( NBTTagCompound ) tag.getTag( "Item" );
		stack = ItemStack.loadItemStackFromNBT( stackTag );
		
		tag.setInteger( "Direction", dir.ordinal() );
		tag.setInteger( "DistanceTraveled", distanceTraveled );
	}
	
	public void setDirection( ForgeDirection dir, float speed )
	{
		motionX = dir.offsetX / 20.0 * speed;
		motionY = dir.offsetY / 20.0 * speed;
		motionZ = dir.offsetZ / 20.0 * speed;
	}
	
	private ItemStack stack;
	private ForgeDirection dir;
	private int distanceTraveled = 0;
}

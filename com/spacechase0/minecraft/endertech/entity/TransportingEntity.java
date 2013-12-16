package com.spacechase0.minecraft.endertech.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class TransportingEntity extends Entity
{
	public TransportingEntity( World world )
	{
		super( world );
		setSize( 0.5f, 0.5f );
		targetX = targetY = targetZ = 0;
		distanceToTravel = 0;
		
		noClip = true;
	}
	
	public TransportingEntity( World world, ItemStack theStack, ForgeDirection theDir, float speed )
	{
		this( world );
		
		stack = theStack;
		setDirection( theDir, speed );
	}

	@Override
	public void entityInit()
	{
		// I can't remember how to do this properly :P
		dataWatcher.addObject( DW_STACK, ( stack == null ) ? new ItemStack( Block.pistonMoving ) : stack );
		dataWatcher.addObject( DW_DIR, ( dir == null ) ? ForgeDirection.UNKNOWN.ordinal() : dir.ordinal() );
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		setPosition( posX + motionX, posY + motionY, posZ + motionZ );
		
		if ( worldObj.isRemote )
		{
			stack = dataWatcher.getWatchableObjectItemStack( DW_STACK );
			dir = ForgeDirection.getOrientation( dataWatcher.getWatchableObjectInt( DW_DIR ) );
		}
		else
		{
			/*
			if ( distanceTraveled > 16 )
			{
				EntityItem item = new EntityItem( worldObj, posX, posY, posZ, stack );
				worldObj.spawnEntityInWorld( item );
				
				worldObj.removeEntity( this );
			}
			*/
			
			if ( ( int ) prevPosX != ( int ) posX || ( int ) prevPosY != ( int ) posY || ( int ) prevPosZ != ( int ) posZ )
			{
				++distanceTraveled;
			}
			
			if ( ( int ) posX == targetX && ( int ) posY == targetY && ( int ) posZ == targetZ )
			{
				worldObj.removeEntity( this );
			}
			else if ( distanceTraveled >= distanceToTravel  )
			{
				worldObj.removeEntity( this );
			}
			
			if ( stack != null )
			{
				dataWatcher.updateObject( DW_STACK, stack );
			}
			dataWatcher.updateObject( DW_DIR, dir.ordinal() );
		}
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
	protected void readEntityFromNBT( NBTTagCompound tag )
	{
		NBTTagCompound stackTag = ( NBTTagCompound ) tag.getTag( "Item" );
		stack = ItemStack.loadItemStackFromNBT( stackTag );
		
		dir = ForgeDirection.getOrientation( tag.getInteger( "Direction" ) );
		distanceTraveled = tag.getInteger( "DistanceTraveled" );
		distanceToTravel = tag.getInteger( "DistanceToTravel" );

		targetX = tag.getInteger( "TargetX" );
		targetY = tag.getInteger( "TargetY" );
		targetZ = tag.getInteger( "TargetZ" );
	}

	@Override
	protected void writeEntityToNBT( NBTTagCompound tag )
	{
		NBTTagCompound stackTag = new NBTTagCompound();
		stack.writeToNBT( stackTag );
		tag.setTag( "Item", stackTag );
		
		tag.setInteger( "Direction", dir.ordinal() );
		tag.setInteger( "DistanceTraveled", distanceTraveled );
		tag.setInteger( "DistanceToTravel", distanceToTravel );

		tag.setInteger( "TargetX", targetX );
		tag.setInteger( "TargetY", targetY );
		tag.setInteger( "TargetZ", targetZ );
	}
	
	public ForgeDirection getDirection()
	{
		return dir;
	}
	
	public void setDirection( ForgeDirection theDir, float speed )
	{
		dir = theDir;
		motionX = dir.offsetX / 20.0 * speed;
		motionY = dir.offsetY / 20.0 * speed;
		motionZ = dir.offsetZ / 20.0 * speed;
	}
	
	public ItemStack getItemStack()
	{
		return stack;
	}
	
	public void setTarget( int x, int y, int z )
	{
		targetX = x;
		targetY = y;
		targetZ = z;
		
		distanceToTravel = -1;
		distanceToTravel += Math.abs( targetX - ( int ) posX );
		distanceToTravel += Math.abs( targetY - ( int ) posY );
		distanceToTravel += Math.abs( targetZ - ( int ) posZ );
	}
	
	/*
	public int getDistanceTraveled()
	{
		return distanceTraveled;
	}
	*/
	
	private ItemStack stack;
	private ForgeDirection dir = ForgeDirection.UNKNOWN;
	private int distanceTraveled;
	private int distanceToTravel;
	private int targetX, targetY, targetZ;
	
	private static final int DW_STACK = 10;
	private static final int DW_DIR = 11;
}

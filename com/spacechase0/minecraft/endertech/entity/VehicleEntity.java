package com.spacechase0.minecraft.endertech.entity;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.spacechase0.minecraft.endertech.EnderTech;
import com.spacechase0.minecraft.endertech.block.RailBlock;
import com.spacechase0.minecraft.endertech.network.SyncBlockPacket;
import com.spacechase0.minecraft.endertech.tileentity.VehicleTileEntity;
import com.spacechase0.minecraft.endertech.world.FakeWorld;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

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
		
		for ( int ix = 0; ix < size; ++ix )
		{
			for ( int iy = 0; iy < size; ++iy )
			{
				for ( int iz = 0; iz < size; ++iz )
				{
					Block block = Block.blocksList[ fakeWorld.getBlockId( ix, iy, iz ) ];
					if ( block == EnderTech.blocks.vehicleController )
					{
						fakeWorld.setBlockMetadataWithNotify( ix, iy, iz, 2, 0x2 );
					}
				}
			}
		}
		
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
    public boolean canBeCollidedWith()
    {
		// Needed for right clicking, hope it doesn't mess anything up.
        return true;
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
    public boolean interactFirst( EntityPlayer player )
    {
		World oldWorld = player.worldObj;
		double oldX = player.posX, oldY = player.posY, oldZ = player.posZ;
		try
		{
			player.worldObj = fakeWorld;
			player.posX += -posX + ( size / 2.f );
			player.posY += -posY;
			player.posZ += -posZ + ( size / 2.f );
			
			if ( !worldObj.isRemote )
			{
				player.posY += 1.62; // getEyeHeight() is incorrect?
			}
			
			boolean flag = true;
			
            double d0 = player.capabilities.isCreativeMode ? 5 : 4.5;
            MovingObjectPosition mop = rayTrace(player,d0, 1);
            if (mop != null && mop.typeOfHit == EnumMovingObjectType.TILE)
            {
                int j = mop.blockX;
                int k = mop.blockY;
                int l = mop.blockZ;
                int i1 = mop.sideHit;
                /*
                if (par1 == 0)
                {
                    this.playerController.clickBlock(j, k, l, mop.sideHit);
                }
                else*/
                {
                	ItemStack itemstack = player.getCurrentEquippedItem();
                    int j1 = itemstack != null ? itemstack.stackSize : 0;

                    boolean result = !ForgeEventFactory.onPlayerInteract(player, Action.RIGHT_CLICK_BLOCK, j, k, l, i1).isCanceled();
                    if (result && onPlayerRightClick(player, fakeWorld, itemstack, j, k, l, i1, mop.hitVec))
                    {
                        flag = false;
                        //player.swingItem();
                    }

                    if (itemstack == null)
                    {
                        return !flag;
                    }

                    if (itemstack.stackSize == 0)
                    {
                        player.inventory.mainInventory[player.inventory.currentItem] = null;
                    }
                    /*else if (itemstack.stackSize != j1 || this.playerController.isInCreativeMode())
                    {
                        //this.entityRenderer.itemRenderer.resetEquippedProgress();
                    }
                    */
                }
            }
            
            return !flag;
		}
		finally
		{
			player.worldObj = oldWorld;
			player.posX = oldX;
			player.posY = oldY;
			player.posZ = oldZ;
		}
    }

	@Override
	public void readEntityFromNBT( NBTTagCompound tag )
	{
		size = tag.getInteger( "Size" );
		fakeWorld = new FakeWorld( this );
		fakeWorld.loadFrom( tag );

		contrX = tag.getInteger( "EmbeddedX" );
		contrY = tag.getInteger( "EmbeddedY" );
		contrZ = tag.getInteger( "EmbeddedZ" );

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

		tag.setInteger( "EmbeddedX", contrX );
		tag.setInteger( "EmbeddedY", contrY );
		tag.setInteger( "EmbeddedZ", contrZ );

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
	
	public void sync( int x, int y, int z )
	{
		if ( worldObj.isRemote ) return;
		
		WorldServer world = ( WorldServer ) worldObj;
		world.getEntityTracker().sendPacketToAllAssociatedPlayers( this, new SyncBlockPacket( this, x, y, z ).toPacket() );
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
	
	private boolean onPlayerRightClick(EntityPlayer par1EntityPlayer, World par2World, ItemStack par3ItemStack, int par4, int par5, int par6, int par7, Vec3 par8Vec3)
	{
        float f = (float)par8Vec3.xCoord - (float)par4;
        float f1 = (float)par8Vec3.yCoord - (float)par5;
        float f2 = (float)par8Vec3.zCoord - (float)par6;
        boolean flag = false;
        int i1;
        if (par3ItemStack != null &&
            par3ItemStack.getItem() != null &&
            par3ItemStack.getItem().onItemUseFirst(par3ItemStack, par1EntityPlayer, par2World, par4, par5, par6, par7, f, f1, f2))
        {
                return true;
        }

        if (!par1EntityPlayer.isSneaking() || (par1EntityPlayer.getHeldItem() == null || par1EntityPlayer.getHeldItem().getItem().shouldPassSneakingClickToBlock(par2World, par4, par5, par6)))
        {
            i1 = par2World.getBlockId(par4, par5, par6);

            if (i1 > 0 && Block.blocksList[i1].onBlockActivated(par2World, par4, par5, par6, par1EntityPlayer, par7, f, f1, f2))
            {
                flag = true;
            }
        }

        /*
        if (!flag && par3ItemStack != null && par3ItemStack.getItem() instanceof ItemBlock)
        {
            ItemBlock itemblock = (ItemBlock)par3ItemStack.getItem();

            if (!itemblock.canPlaceItemBlockOnSide(par2World, par4, par5, par6, par7, par1EntityPlayer, par3ItemStack))
            {
                return false;
            }
        }*/
        
        return false;
	}

    private MovingObjectPosition rayTrace(EntityPlayer player,double par1, float par3)
    {
    	// TODO: FIX ON SMP
    	
        Vec3 vec3 = player.worldObj.getWorldVec3Pool().getVecFromPool(player.posX, player.posY, player.posZ);
        Vec3 vec31 = player.getLook(par3);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * par1, vec31.yCoord * par1, vec31.zCoord * par1);
        return this.worldObj.clip(vec3, vec32);
    }

	private int size = -1;
	private int contrX = -1, contrY = -1, contrZ = -1;
	private FakeWorld fakeWorld;
}

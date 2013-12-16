package com.spacechase0.minecraft.endertech.world;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.spacechase0.minecraft.endertech.entity.VehicleEntity;
import com.spacechase0.minecraft.endertech.tileentity.VehicleTileEntity;

import net.minecraft.block.Block;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class FakeWorld extends World
{
	public FakeWorld( VehicleEntity theEntity )
	{
		super( new FakeSaveHandler(), "Vehicle" + theEntity.entityId, new FakeWorldProvider(), new WorldSettings( 0, EnumGameType.NOT_SET, false, false, WorldType.FLAT ), new Profiler(), new FakeLogAgent() );
		entity = theEntity;
		
		provider.registerWorld( this );

        if (this.pendingTickListEntriesHashSet == null)
        {
            this.pendingTickListEntriesHashSet = new HashSet();
        }

        if (this.pendingTickListEntriesTreeSet == null)
        {
            this.pendingTickListEntriesTreeSet = new TreeSet();
        }
	}
	
	@Override
    public void tick()
    {
		super.tick();
		
		tickUpdates( false );
		tickBlocksAndAmbiance();
    }
	
	@Override
	public boolean tickUpdates( boolean par1 )
	{
        int i = this.pendingTickListEntriesTreeSet.size();

        if (i != this.pendingTickListEntriesHashSet.size())
        {
            throw new IllegalStateException("TickNextTick list out of synch");
        }
        else
        {
            if (i > 1000)
            {
                i = 1000;
            }

            this.theProfiler.startSection("cleaning");
            NextTickListEntry nextticklistentry;

            for (int j = 0; j < i; ++j)
            {
                nextticklistentry = (NextTickListEntry)this.pendingTickListEntriesTreeSet.first();

                if (!par1 && nextticklistentry.scheduledTime > this.worldInfo.getWorldTotalTime())
                {
                    break;
                }

                this.pendingTickListEntriesTreeSet.remove(nextticklistentry);
                this.pendingTickListEntriesHashSet.remove(nextticklistentry);
                this.pendingTickListEntriesThisTick.add(nextticklistentry);
            }

            this.theProfiler.endSection();
            this.theProfiler.startSection("ticking");
            Iterator iterator = this.pendingTickListEntriesThisTick.iterator();

            while (iterator.hasNext())
            {
                nextticklistentry = (NextTickListEntry)iterator.next();
                iterator.remove();
                //Keeping here as a note for future when it may be restored.
                //boolean isForced = getPersistentChunks().containsKey(new ChunkCoordIntPair(nextticklistentry.xCoord >> 4, nextticklistentry.zCoord >> 4));
                //byte b0 = isForced ? 0 : 8;
                byte b0 = 0;

                if (this.checkChunksExist(nextticklistentry.xCoord - b0, nextticklistentry.yCoord - b0, nextticklistentry.zCoord - b0, nextticklistentry.xCoord + b0, nextticklistentry.yCoord + b0, nextticklistentry.zCoord + b0))
                {
                    int k = this.getBlockId(nextticklistentry.xCoord, nextticklistentry.yCoord, nextticklistentry.zCoord);

                    if (k > 0 && Block.isAssociatedBlockID(k, nextticklistentry.blockID))
                    {
                        try
                        {
                            Block.blocksList[k].updateTick(this, nextticklistentry.xCoord, nextticklistentry.yCoord, nextticklistentry.zCoord, this.rand);
                        }
                        catch (Throwable throwable)
                        {
                            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while ticking a block");
                            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being ticked");
                            int l;

                            try
                            {
                                l = this.getBlockMetadata(nextticklistentry.xCoord, nextticklistentry.yCoord, nextticklistentry.zCoord);
                            }
                            catch (Throwable throwable1)
                            {
                                l = -1;
                            }

                            CrashReportCategory.addBlockCrashInfo(crashreportcategory, nextticklistentry.xCoord, nextticklistentry.yCoord, nextticklistentry.zCoord, k, l);
                            throw new ReportedException(crashreport);
                        }
                    }
                }
                else
                {
                    this.scheduleBlockUpdate(nextticklistentry.xCoord, nextticklistentry.yCoord, nextticklistentry.zCoord, nextticklistentry.blockID, 0);
                }
            }

            this.theProfiler.endSection();
            this.pendingTickListEntriesThisTick.clear();
            return !this.pendingTickListEntriesTreeSet.isEmpty();
        }
	}
	
	@Override
    protected void tickBlocksAndAmbiance()
    {
		super.tickBlocksAndAmbiance();
		if ( !isRemote )
		{
	        final long startTime = System.nanoTime();

	        //while (iterator.hasNext())
	        {
	        	int i=0,j=0;
	        	
	            ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair)new ChunkCoordIntPair(0,0);
	            int k = chunkcoordintpair.chunkXPos * 16;
	            int l = chunkcoordintpair.chunkZPos * 16;
	            this.theProfiler.startSection("getChunk");
	            Chunk chunk = this.getChunkFromChunkCoords(chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos);
	            this.moodSoundAndLightCheck(k, l, chunk);
	            this.theProfiler.endStartSection("tickChunk");
	            //Limits and evenly distributes the lighting update time
	            if (System.nanoTime() - startTime <= 4000000 && true)
	            {
	                chunk.updateSkylight();
	            }
	            int i1;
	            int j1;
	            int k1;
	            int l1;
	            int i2;
	            
	            this.theProfiler.endStartSection("tickTiles");
	            ExtendedBlockStorage[] aextendedblockstorage = chunk.getBlockStorageArray();
	            j1 = aextendedblockstorage.length;

	            for (k1 = 0; k1 < j1; ++k1)
	            {
	                ExtendedBlockStorage extendedblockstorage = aextendedblockstorage[k1];

	                if (extendedblockstorage != null && extendedblockstorage.getNeedsRandomTick())
	                {
	                    for (int j2 = 0; j2 < 3; ++j2)
	                    {
	                        this.updateLCG = this.updateLCG * 3 + 1013904223;
	                        i2 = this.updateLCG >> 2;
	                        int k2 = i2 & 15;
	                        int l2 = i2 >> 8 & 15;
	                        int i3 = i2 >> 16 & 15;
	                        int j3 = extendedblockstorage.getExtBlockID(k2, i3, l2);
	                        ++j;
	                        Block block = Block.blocksList[j3];

	                        if (block != null && block.getTickRandomly())
	                        {
	                            ++i;
	                            block.updateTick(this, k2 + k, i3 + extendedblockstorage.getYLocation(), l2 + l, this.rand);
	                        }
	                    }
	                }
	            }

	            this.theProfiler.endSection();
	        }
		}
    }
	
	@Override
	protected IChunkProvider createChunkProvider()
	{
		if ( chunkProvider == null )
		{
			chunkProvider = new FakeChunkProvider( this, entity );
		}
		
		return chunkProvider;
	}
	
	@Override
    public boolean spawnEntityInWorld( Entity toSpawn )
	{
		toSpawn.worldObj = entity.worldObj;
		toSpawn.posX += entity.boundingBox.minX;
		toSpawn.posY += entity.boundingBox.minY;
		toSpawn.posZ += entity.boundingBox.minZ;
		
		return entity.worldObj.spawnEntityInWorld( toSpawn );
	}
	
	@Override
    public List getPendingBlockUpdates(Chunk par1Chunk, boolean par2)
    {
        ArrayList arraylist = null;
        ChunkCoordIntPair chunkcoordintpair = par1Chunk.getChunkCoordIntPair();
        int i = (chunkcoordintpair.chunkXPos << 4) - 2;
        int j = i + 16 + 2;
        int k = (chunkcoordintpair.chunkZPos << 4) - 2;
        int l = k + 16 + 2;

        for (int i1 = 0; i1 < 2; ++i1)
        {
            Iterator iterator;

            if (i1 == 0)
            {
                iterator = this.pendingTickListEntriesTreeSet.iterator();
            }
            else
            {
                iterator = this.pendingTickListEntriesThisTick.iterator();

                if (!this.pendingTickListEntriesThisTick.isEmpty())
                {
                    System.out.println(this.pendingTickListEntriesThisTick.size());
                }
            }

            while (iterator.hasNext())
            {
                NextTickListEntry nextticklistentry = (NextTickListEntry)iterator.next();

                if (nextticklistentry.xCoord >= i && nextticklistentry.xCoord < j && nextticklistentry.zCoord >= k && nextticklistentry.zCoord < l)
                {
                    if (par2)
                    {
                        this.pendingTickListEntriesHashSet.remove(nextticklistentry);
                        iterator.remove();
                    }

                    if (arraylist == null)
                    {
                        arraylist = new ArrayList();
                    }

                    arraylist.add(nextticklistentry);
                }
            }
        }

        return arraylist;
    }
	
	@Override
    public void scheduleBlockUpdateWithPriority(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        NextTickListEntry nextticklistentry = new NextTickListEntry(par1, par2, par3, par4);
        //Keeping here as a note for future when it may be restored.
        //boolean isForced = getPersistentChunks().containsKey(new ChunkCoordIntPair(nextticklistentry.xCoord >> 4, nextticklistentry.zCoord >> 4));
        //byte b0 = isForced ? 0 : 8;
        byte b0 = 0;

        if (this.scheduledUpdatesAreImmediate && par4 > 0)
        {
            if (Block.blocksList[par4].func_82506_l())
            {
                b0 = 8;

                if (this.checkChunksExist(nextticklistentry.xCoord - b0, nextticklistentry.yCoord - b0, nextticklistentry.zCoord - b0, nextticklistentry.xCoord + b0, nextticklistentry.yCoord + b0, nextticklistentry.zCoord + b0))
                {
                    int k1 = this.getBlockId(nextticklistentry.xCoord, nextticklistentry.yCoord, nextticklistentry.zCoord);

                    if (k1 == nextticklistentry.blockID && k1 > 0)
                    {
                        Block.blocksList[k1].updateTick(this, nextticklistentry.xCoord, nextticklistentry.yCoord, nextticklistentry.zCoord, this.rand);
                    }
                }

                return;
            }

            par5 = 1;
        }

        if (this.checkChunksExist(par1 - b0, par2 - b0, par3 - b0, par1 + b0, par2 + b0, par3 + b0))
        {
            if (par4 > 0)
            {
                nextticklistentry.setScheduledTime((long)par5 + this.worldInfo.getWorldTotalTime());
                nextticklistentry.setPriority(par6);
            }

            if (!this.pendingTickListEntriesHashSet.contains(nextticklistentry))
            {
                this.pendingTickListEntriesHashSet.add(nextticklistentry);
                this.pendingTickListEntriesTreeSet.add(nextticklistentry);
            }
        }
    }
	
	@Override
    public void scheduleBlockUpdateFromLoad(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        NextTickListEntry nextticklistentry = new NextTickListEntry(par1, par2, par3, par4);
        nextticklistentry.setPriority(par6);

        if (par4 > 0)
        {
            nextticklistentry.setScheduledTime((long)par5 + this.worldInfo.getWorldTotalTime());
        }

        if (!this.pendingTickListEntriesHashSet.contains(nextticklistentry))
        {
            this.pendingTickListEntriesHashSet.add(nextticklistentry);
            this.pendingTickListEntriesTreeSet.add(nextticklistentry);
        }
    }

	@Override
    public boolean isBlockTickScheduledThisTick(int par1, int par2, int par3, int par4)
    {
        NextTickListEntry nextticklistentry = new NextTickListEntry(par1, par2, par3, par4);
        return this.pendingTickListEntriesThisTick.contains(nextticklistentry);
    }

	@Override
    public void scheduleBlockUpdate(int par1, int par2, int par3, int par4, int par5)
    {
        this.scheduleBlockUpdateWithPriority(par1, par2, par3, par4, par5, 0);
    }

	@Override
	public Entity getEntityByID( int id )
	{
		return entity.worldObj.getEntityByID( id );
	}
	
	@Override
    public int getBlockId(int par1, int par2, int par3)
    {
        if (par1 >= 0 && par3 >= 0 && par1 < entity.getSize() && par3 < entity.getSize())
        {
            if (par2 < 0)
            {
                return 0;
            }
            else if (par2 >= 256)
            {
                return 0;
            }
            else
            {
                Chunk chunk = null;

                try
                {
                    chunk = this.getChunkFromChunkCoords(par1 >> 4, par3 >> 4);
                    return chunk.getBlockID(par1 & 15, par2, par3 & 15);
                }
                catch (Throwable throwable)
                {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception getting block type in world");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Requested block coordinates");
                    crashreportcategory.addCrashSection("Found chunk", Boolean.valueOf(chunk == null));
                    crashreportcategory.addCrashSection("Location", CrashReportCategory.getLocationInfo(par1, par2, par3));
                    crashreportcategory.addCrashSection("Vehicle", entity);
                    throw new ReportedException(crashreport);
                }
            }
        }
        else
        {
            return 0;
        }
    }
	
	@Override
    public int getBlockMetadata(int par1, int par2, int par3)
    {
        if (par1 >= 0 && par3 >= 0 && par1 < entity.getSize() && par3 < entity.getSize())
        {
            if (par2 < 0)
            {
                return 0;
            }
            else if (par2 >= 256)
            {
                return 0;
            }
            else
            {
                Chunk chunk = this.getChunkFromChunkCoords(par1 >> 4, par3 >> 4);
                par1 &= 15;
                par3 &= 15;
                return chunk.getBlockMetadata(par1, par2, par3);
            }
        }
        else
        {
            return 0;
        }
    }
	
    @Override
    public BiomeGenBase getBiomeGenForCoordsBody( int x, int z )
    {
    	return entity.worldObj.getBiomeGenForCoords( ( int ) entity.posX, ( int ) entity.posZ );
    }
	
	@Override
    public int getFullBlockLightValue( int x, int y, int z )
    {
		return entity.worldObj.getFullBlockLightValue( ( int ) entity.boundingBox.minX + x, ( int ) entity.boundingBox.minY + y, ( int ) entity.boundingBox.minZ + z );
    }

    @Override
    public int getBlockLightValue_do(int x, int y, int z, boolean par4)
    {
		return entity.worldObj.getBlockLightValue_do( ( int ) entity.boundingBox.minX + x, ( int ) entity.boundingBox.minY + y, ( int ) entity.boundingBox.minZ + z, par4 );
    }
    
    @Override
    public int getLightBrightnessForSkyBlocks(int x, int y, int z, int par4)
    {
		return entity.worldObj.getLightBrightnessForSkyBlocks( ( int ) entity.boundingBox.minX + x, ( int ) entity.boundingBox.minY + y, ( int ) entity.boundingBox.minZ + z, par4 );
    }
	
	public void loadFrom( VehicleTileEntity vehicle )
	{
		Chunk chunk = createChunkProvider().provideChunk( 0, 0 );
		
		worldInfo.setWorldTime( 0 );
		
		int size = vehicle.getSize();
		for ( int ix = 0; ix < size; ++ix )
		{
			for ( int iy = 0; iy < size; ++iy )
			{
				for ( int iz = 0; iz < size; ++iz )
				{
					int index = ix + ( iy * size ) + ( iz * size * size );
					
					short data = vehicle.getBlockData()[ index ];
					int id = vehicle.getBlockId( data );
					int meta = vehicle.getBlockMeta( data );
					TileEntity te = vehicle.getBlockTiles()[ index ];
					NextTickListEntry tick = vehicle.getBlockTicks()[ index ];
					
					chunk.setBlockIDWithMetadata( ix, iy, iz, id, meta );
					if ( te != null )
					{
						// Just in case
						te.setWorldObj( this );
						te.xCoord = ix;
						te.yCoord = iy;
						te.zCoord = iz;
						
						if ( te != null )
						{
							chunk.addTileEntity( te );
						}
						
						if ( tick != null )
						{
							scheduleBlockUpdateWithPriority( tick.xCoord, tick.yCoord, tick.zCoord, id, ( int ) tick.scheduledTime, tick.priority );
						}
					}
				}
			}
		}
		
		worldInfo.setWorldTime( entity.worldObj.getWorldTime() );
	}
	
	public void loadFrom( NBTTagCompound tag )
	{
		VehicleTileEntity te = new VehicleTileEntity();
		VehicleTileEntity.fromFakeWorld = true;
		te.readFromNBT( tag );
		
		loadFrom( te );
	}
	
	public void saveTo( NBTTagCompound tag )
	{
		int size = tag.getInteger( "Size" );
		
		VehicleTileEntity te = new VehicleTileEntity();
		te.setWorldObj( this );
		
		VehicleTileEntity.fromFakeWorld = true;
		te.setVehicle( 0, 0, 0, size, size, size );
		te.writeToNBT( tag );
	}
	
	private VehicleEntity entity;
	
	private List pendingTickListEntriesThisTick = new ArrayList();
    private TreeSet pendingTickListEntriesTreeSet;
    private Set pendingTickListEntriesHashSet;
}

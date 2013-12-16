package com.spacechase0.minecraft.endertech.world;

import com.spacechase0.minecraft.endertech.entity.VehicleEntity;
import com.spacechase0.minecraft.endertech.tileentity.VehicleTileEntity;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ReportedException;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

public class FakeWorld extends World
{
	public FakeWorld( VehicleEntity theEntity )
	{
		super( new FakeSaveHandler(), "Vehicle" + theEntity.entityId, new FakeWorldProvider(), new WorldSettings( 0, EnumGameType.NOT_SET, false, false, WorldType.FLAT ), new Profiler(), new FakeLogAgent() );
		entity = theEntity;
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
	
	public void loadFrom( VehicleTileEntity vehicle )
	{
		Chunk chunk = createChunkProvider().provideChunk( 0, 0 );
		
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
					
					chunk.setBlockIDWithMetadata( ix, iy, iz, id, meta );
					if ( te != null )
					{
						// Just in case
						te.setWorldObj( this );
						te.xCoord = ix;
						te.yCoord = iy;
						te.zCoord = iz;
						
						chunk.addTileEntity( te );
					}
				}
			}
		}
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
}

package com.spacechase0.minecraft.endertech.world;

import java.io.File;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

public class FakeSaveHandler implements ISaveHandler
{
	@Override
	public WorldInfo loadWorldInfo()
	{
		return new WorldInfo( new WorldSettings( 0, EnumGameType.NOT_SET, false, false, WorldType.FLAT ), "" );
	}

	@Override
	public void checkSessionLock() throws MinecraftException
	{
	}

	@Override
	public IChunkLoader getChunkLoader( WorldProvider provider )
	{
		return null;
	}

	@Override
	public void saveWorldInfoWithPlayer( WorldInfo info, NBTTagCompound tag )
	{
	}

	@Override
	public void saveWorldInfo( WorldInfo info )
	{
	}

	@Override
	public IPlayerFileData getSaveHandler()
	{
		return null;
	}

	@Override
	public void flush()
	{
	}

	@Override
	public File getMapFileFromName( String str )
	{
		return null;
	}

	@Override
	public String getWorldDirectoryName()
	{
		return null;
	}

}

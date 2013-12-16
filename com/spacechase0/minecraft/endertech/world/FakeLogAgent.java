package com.spacechase0.minecraft.endertech.world;

import java.util.logging.Logger;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.logging.ILogAgent;

public class FakeLogAgent implements ILogAgent {

	@Override
	public void logInfo( String s )
	{
	}

	@Override
	@SideOnly( Side.SERVER )
	public Logger func_120013_a()
	{
		return null;
	}

	@Override
	public void logWarning( String s )
	{

	}

	@Override
	public void logWarningFormatted( String s, Object... var2 )
	{
	}

	@Override
	public void logWarningException( String s, Throwable throwable )
	{
	}

	@Override
	public void logSevere( String s )
	{
	}

	@Override
	public void logSevereException( String s, Throwable throwable )
	{
	}

	@Override
	@SideOnly( Side.CLIENT )
	public void logFine( String s )
	{
	}
}

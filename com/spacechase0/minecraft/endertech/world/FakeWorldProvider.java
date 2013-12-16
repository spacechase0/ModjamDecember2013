package com.spacechase0.minecraft.endertech.world;

import net.minecraft.world.WorldProvider;

public class FakeWorldProvider extends WorldProvider
{
	@Override
	public String getDimensionName()
	{
		return "Vehicle";
	}
}

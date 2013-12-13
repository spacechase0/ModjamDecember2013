package com.spacechase0.minecraft.endertech.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class NuggetItem extends Item
{
	public NuggetItem( int id, String[] theTypes )
	{
		super( id );
		types = theTypes;
		
		setUnlocalizedName( "endertech:nugget" );
		setCreativeTab( CreativeTabs.tabMaterials );
	}
	
	@Override
    public void getSubItems( int id, CreativeTabs tabs, List list )
    {
		for ( int i = 0; i < types.length; ++i )
		{
			list.add( new ItemStack( this, 1, i ) );
		}
    }

	@Override
    public String getUnlocalizedName( ItemStack stack )
    {
        return "item.endertech:" + types[ stack.getItemDamage() ] + "Nugget";
    }
	
	@Override
	public void registerIcons( IconRegister register )
	{
		icons = new Icon[ types.length ];
		for ( int i = 0; i < icons.length; ++i )
		{
			icons[ i ] = register.registerIcon( "endertech:" + types[ i ] + "Nugget" );
		}
	}
	
	@Override
    public Icon getIconFromDamage( int data )
    {
        return icons[ data ];
    }
	
	public final String[] types;
	public Icon[] icons;
}

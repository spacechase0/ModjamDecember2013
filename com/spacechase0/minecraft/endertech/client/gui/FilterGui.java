package com.spacechase0.minecraft.endertech.client.gui;

import org.lwjgl.opengl.GL11;

import com.spacechase0.minecraft.endertech.inventory.FilterContainer;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class FilterGui extends GuiContainer
{
	public FilterGui( FilterContainer container )
	{
		super( container );
	}
	
    protected void drawGuiContainerForegroundLayer( int mx, int my)
    {
        String s = StatCollector.translateToLocal( "container.endertech:filter" ); // TODO: input/output
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString( StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752 );
    }
    protected void drawGuiContainerBackgroundLayer( float par1, int mx, int my )
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture( GUI );
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
    
    private static final ResourceLocation GUI = new ResourceLocation( "endertech:/textures/gui/filter.png" );
}

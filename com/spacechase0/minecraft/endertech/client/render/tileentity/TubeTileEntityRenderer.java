package com.spacechase0.minecraft.endertech.client.render.tileentity;

import static org.lwjgl.opengl.GL11.*;

import com.spacechase0.minecraft.endertech.tileentity.TubeTileEntity;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeDirection;

public class TubeTileEntityRenderer extends TileEntitySpecialRenderer
{
	@Override
	public void renderTileEntityAt( TileEntity tile, double x, double y, double z, float f )
	{
		TubeTileEntity tube = ( TubeTileEntity ) tile;
		for ( int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i )
		{
			ForgeDirection dir = ForgeDirection.getOrientation( i );
			
			glPushMatrix();
			{
				glTranslated( x, y, z );
				if ( dir == ForgeDirection.EAST || dir == ForgeDirection.WEST )
				{
					if ( dir == ForgeDirection.WEST ) glTranslated( 0.5, 0, 0 );
					glRotatef( -90, 0, 1, 0 );
				}
				else if ( dir == ForgeDirection.NORTH || dir == ForgeDirection.SOUTH )
				{
					glTranslated( 1, 0, 0 );
					if ( dir == ForgeDirection.SOUTH ) glTranslated( 0, 0, 0.5 );
					glRotatef( 180, 0, 1, 0 );
				}
				else
				{
					if ( dir == ForgeDirection.UP ) glTranslated( 0, 0.5, 0 );
					glRotatef( 90, 1, 0, 0 );
				}
				
				double mult = 1;
				if ( dir == ForgeDirection.EAST || dir == ForgeDirection.NORTH || dir == ForgeDirection.DOWN )
				{
					mult = -1;
				}
	
				tileEntityRenderer.renderEngine.bindTexture( INNER_FLAT );
				if ( tube.doesInput( dir ) )
				{
					glPushMatrix();
					
					glTranslated( 0, 0, 0.125 * mult );
					draw();
					
					// Draw tube
					
					glPopMatrix();
				}
				else draw();
	
				tileEntityRenderer.renderEngine.bindTexture( OUTER_FLAT );
				if ( tube.doesOutput( dir ) )
				{
					glPushMatrix();
					
					glTranslated( 0, 0, -0.125 * mult );
					draw();
					
					// Draw tube
					
					glPopMatrix();
				}
				else draw();
				
				for ( int iu = 0; iu < TubeTileEntity.UPGRADES.length; ++iu )
				{
					if ( tube.hasUpgrade( dir, TubeTileEntity.UPGRADES[ iu ] ) )
					{
						tileEntityRenderer.renderEngine.bindTexture( UPGRADES[ iu ] );
						draw();
					}
				}
			}
			glPopMatrix();
		}
	}
	
	private void draw()
	{
		glDisable( GL_CULL_FACE );
		Tessellator tess = Tessellator.instance;
		glPushMatrix();
		tess.startDrawingQuads();
		{
			glTranslated( 0, 0, -0.25 );
			
			tess.addVertexWithUV( 0, 0, 0, 0, 0 );
			tess.addVertexWithUV( 1, 0, 0, 1, 0 );
			tess.addVertexWithUV( 1, 1, 0, 1, 1 );
			tess.addVertexWithUV( 0, 1, 0, 0, 1 );
		}
		tess.draw();
		glPopMatrix();
		glEnable( GL_CULL_FACE );
	}

	private static final ResourceLocation INNER_FLAT = new ResourceLocation( "endertech:/textures/blocks/tube/inner_flat.png" );
	private static final ResourceLocation OUTER_FLAT = new ResourceLocation( "endertech:/textures/blocks/tube/outer_flat.png" );
	private static final ResourceLocation[] UPGRADES = new ResourceLocation[]
	                                                   {
	                                                   	new ResourceLocation( "endertech:/textures/blocks/tube/upgrade_gold.png" ),
	                                                   	new ResourceLocation( "endertech:/textures/blocks/tube/upgrade_diamond.png" ),
	                                                   	new ResourceLocation( "endertech:/textures/blocks/tube/upgrade_iron.png" ),
	                                                   	new ResourceLocation( "endertech:/textures/blocks/tube/upgrade_redstone.png" ),
	                                                   };
}

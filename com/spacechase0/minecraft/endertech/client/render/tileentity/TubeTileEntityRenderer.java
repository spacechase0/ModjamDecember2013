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
				
				double multA = 1;
				double offB = 0;
				if ( dir == ForgeDirection.EAST || dir == ForgeDirection.NORTH || dir == ForgeDirection.DOWN )
				{
					multA = 0;
					offB = -0.125;
				}
	
				tileEntityRenderer.renderEngine.bindTexture( INNER_FLAT );
				if ( tube.doesInput( dir ) )
				{
					glPushMatrix();
					
					if ( multA != 1 )
					{
					}
					
					glTranslated( 0, 0, 0.125 * multA );
					draw( offB );
					
					tileEntityRenderer.renderEngine.bindTexture( TUBE );
					drawTube( 1 );
					
					glPopMatrix();
				}
				else draw( 0 );
				
				if ( dir == ForgeDirection.EAST || dir == ForgeDirection.NORTH || dir == ForgeDirection.DOWN )
				{
					offB = 0.125;
				}
				
				tileEntityRenderer.renderEngine.bindTexture( OUTER_FLAT );
				if ( tube.doesOutput( dir ) )
				{
					glPushMatrix();
					
					glTranslated( 0, 0, -0.125 * multA );
					draw( offB );

					tileEntityRenderer.renderEngine.bindTexture( TUBE );
					drawTube( 2 );
					
					glPopMatrix();
				}
				else draw( 0 );
				
				for ( int iu = 0; iu < TubeTileEntity.UPGRADES.length; ++iu )
				{
					if ( tube.hasUpgrade( dir, TubeTileEntity.UPGRADES[ iu ] ) )
					{
						tileEntityRenderer.renderEngine.bindTexture( UPGRADES[ iu ] );
						draw( 0 );
					}
				}
			}
			glPopMatrix();
		}
	}
	
	private void draw( double offset )
	{
		glDisable( GL_CULL_FACE );
		Tessellator tess = Tessellator.instance;
		glPushMatrix();
		tess.startDrawingQuads();
		tess.setColorOpaque_F( 255, 255, 255 );
		{
			glTranslated( 0, 0, -0.25 + offset );
			
			tess.addVertexWithUV( 0, 0, 0, 0, 0 );
			tess.addVertexWithUV( 1, 0, 0, 1, 0 );
			tess.addVertexWithUV( 1, 1, 0, 1, 1 );
			tess.addVertexWithUV( 0, 1, 0, 0, 1 );
		}
		tess.draw();
		glPopMatrix();
		glEnable( GL_CULL_FACE );
	}
	
	private void drawTube( double scale )
	{
		glDisable( GL_CULL_FACE );
		Tessellator tess = Tessellator.instance;
		glPushMatrix();
		{
			glTranslated( 0.375, 0.125, -0.25 );

			tess.startDrawingQuads();
			tess.setColorOpaque_F( 255, 255, 255 );
			{
				double w = 4.0 / 16;
				double h = 2.0 / 16;
				double i = 0.25;
				double o = 0.5;
				
				if ( scale == 1 )
				{
					glTranslated( 0.0625, 0, -0.125 );
					w /= 2;
					i = 0.25 + 0.0625;
					o = 0.5 - 0.0625;
				}
				
				tess.addVertexWithUV( 0, i, 0, 0, 0 );
				tess.addVertexWithUV( w, i, 0, 1, 0 );
				tess.addVertexWithUV( w, i, h, 1, 1 );
				tess.addVertexWithUV( 0, i, h, 0, 1 );
	
				tess.addVertexWithUV( 0, o, 0, 0, 0 );
				tess.addVertexWithUV( w, o, 0, 1, 0 );
				tess.addVertexWithUV( w, o, h, 1, 1 );
				tess.addVertexWithUV( 0, o, h, 0, 1 );
	
				if ( scale == 2 )
				{
					i = 0.25 + ( 1.0 / 16 / 2 );
					o = 0.5 - ( 1.0 / 16 / 2 );
					
					tess.addVertexWithUV( 0, i, 0, 0, 0 );
					tess.addVertexWithUV( w, i, 0, 1, 0 );
					tess.addVertexWithUV( w, i, h, 1, 1 );
					tess.addVertexWithUV( 0, i, h, 0, 1 );
					
					tess.addVertexWithUV( 0, o, 0, 0, 0 );
					tess.addVertexWithUV( w, o, 0, 1, 0 );
					tess.addVertexWithUV( w, o, h, 1, 1 );
					tess.addVertexWithUV( 0, o, h, 0, 1 );
				}
			}
			tess.draw();

			glTranslated( -0.25, 0.25, 0 );
			tess.startDrawingQuads();
			{
				double w = 4.0 / 16;
				double h = 2.0 / 16;
				double i = 0.25;
				double o = 0.5;
				
				if ( scale == 1 )
				{
					glTranslated( -0.0625, 1.0 / 16, 0 );
					w /= 2;
					i = 0.25 + 0.0625;
					o = 0.5 - 0.0625;
				}
				
				tess.addVertexWithUV( i, 0, 0, 0, 0 );
				tess.addVertexWithUV( i, w, 0, 1, 0 );
				tess.addVertexWithUV( i, w, h, 1, 1 );
				tess.addVertexWithUV( i, 0, h, 0, 1 );
	
				tess.addVertexWithUV( o, 0, 0, 0, 0 );
				tess.addVertexWithUV( o, w, 0, 1, 0 );
				tess.addVertexWithUV( o, w, h, 1, 1 );
				tess.addVertexWithUV( o, 0, h, 0, 1 );
	
				if ( scale == 2 )
				{
					i = 0.25 + ( 1.0 / 16 / 2 );
					o = 0.5 - ( 1.0 / 16 / 2 );
					
					tess.addVertexWithUV( i, 0, 0, 0, 0 );
					tess.addVertexWithUV( i, w, 0, 1, 0 );
					tess.addVertexWithUV( i, w, h, 1, 1 );
					tess.addVertexWithUV( i, 0, h, 0, 1 );
					
					tess.addVertexWithUV( o, 0, 0, 0, 0 );
					tess.addVertexWithUV( o, w, 0, 1, 0 );
					tess.addVertexWithUV( o, w, h, 1, 1 );
					tess.addVertexWithUV( o, 0, h, 0, 1 );
				}
			}
			tess.draw();
		}
		glPopMatrix();
		glEnable( GL_CULL_FACE );
	}

	private static final ResourceLocation INNER_FLAT = new ResourceLocation( "endertech:/textures/blocks/tube/inner_flat.png" );
	private static final ResourceLocation OUTER_FLAT = new ResourceLocation( "endertech:/textures/blocks/tube/outer_flat.png" );
	private static final ResourceLocation TUBE       = new ResourceLocation( "endertech:/textures/blocks/tube/tube.png" );
	private static final ResourceLocation[] UPGRADES = new ResourceLocation[]
	                                                   {
	                                                   	new ResourceLocation( "endertech:/textures/blocks/tube/upgrade_gold.png" ),
	                                                   	new ResourceLocation( "endertech:/textures/blocks/tube/upgrade_diamond.png" ),
	                                                   	new ResourceLocation( "endertech:/textures/blocks/tube/upgrade_iron.png" ),
	                                                   	new ResourceLocation( "endertech:/textures/blocks/tube/upgrade_redstone.png" ),
	                                                   };
}

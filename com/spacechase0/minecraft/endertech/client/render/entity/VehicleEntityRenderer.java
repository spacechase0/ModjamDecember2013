package com.spacechase0.minecraft.endertech.client.render.entity;

import static org.lwjgl.opengl.GL11.*;

import com.spacechase0.minecraft.endertech.entity.VehicleEntity;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class VehicleEntityRenderer extends Render
{
	@Override
	public void doRender( Entity entity, double x, double y, double z, float f, float f1 )
	{
		//if ( !( entity instanceof VehicleEntity ) ) return;
		VehicleEntity vehicle = ( VehicleEntity ) entity;
		
		glDisable( GL_LIGHTING );
		glPushMatrix();
		{
			glTranslated( x - vehicle.getSize() / 2.f, y, z - vehicle.getSize() / 2.f );
			
			World world = vehicle.getFakeWorld();
			blocks.blockAccess = world;
			for ( int ix = 0; ix < vehicle.getSize(); ++ix )
			{
				for ( int iy = 0; iy < vehicle.getSize(); ++iy )
				{
					for ( int iz = 0; iz < vehicle.getSize(); ++iz )
					{
						Block block = Block.blocksList[ world.getBlockId( ix, iy, iz ) ];
						if ( block == null ) continue;
						
						glColor3f( 1, 1, 1 );
						
						Tessellator tess = Tessellator.instance;
						tess.setColorOpaque( 255, 255, 255 );
						tess.startDrawingQuads();
						renderManager.renderEngine.bindTexture( TextureMap.locationBlocksTexture );
						blocks.renderBlockAllFaces( block, ix, iy, iz );
						tess.draw();
						
						TileEntity te = world.getBlockTileEntity( ix, iy, iz );
						if ( te != null )
						{
							te.setWorldObj( world );
							TileEntityRenderer.instance.renderTileEntityAt( te, ix, iy, iz, f1 );
						}
					}
				}
			}
		}
		glPopMatrix();
		glEnable( GL_LIGHTING );
	}

	@Override
	protected ResourceLocation getEntityTexture( Entity entity )
	{
		return null;
	}
	
	private RenderBlocks blocks = new RenderBlocks();
}

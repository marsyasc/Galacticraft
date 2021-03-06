package micdoodle8.mods.galacticraft.core.client.render.block;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockUnlitTorch;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreBlockRendererUnlitTorch implements ISimpleBlockRenderingHandler
{
    final int renderID;

    public GCCoreBlockRendererUnlitTorch(int var1)
    {
        this.renderID = var1;
    }

    @Override
	public boolean renderWorldBlock(IBlockAccess var1, int var2, int var3, int var4, Block var5, int var6, RenderBlocks var7)
    {
    	GCCoreBlockRendererUnlitTorch.renderGCUnlitTorch(var7, var5, var1, var2, var3, var4);
        return true;
    }

    @Override
	public boolean shouldRender3DInInventory()
    {
        return false;
    }

    @Override
	public int getRenderId()
    {
        return this.renderID;
    }

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) 
	{
        renderTorchAtAngle(renderer, block, 0, 0, 0, 0.0D, 0.0D);
	}

    public static void renderInvNormalBlock(RenderBlocks var0, Block var1, int var2)
    {
        final Tessellator var3 = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        var0.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        var3.startDrawingQuads();
        var3.setNormal(0.0F, -1.0F, 0.0F);
        var0.renderBottomFace(var1, 0.0D, 0.0D, 0.0D, var1.getBlockTextureFromSideAndMetadata(0, var2));
        var3.draw();
        var3.startDrawingQuads();
        var3.setNormal(0.0F, 1.0F, 0.0F);
        var0.renderTopFace(var1, 0.0D, 0.0D, 0.0D, var1.getBlockTextureFromSideAndMetadata(1, var2));
        var3.draw();
        var3.startDrawingQuads();
        var3.setNormal(0.0F, 0.0F, -1.0F);
        var0.renderEastFace(var1, 0.0D, 0.0D, 0.0D, var1.getBlockTextureFromSideAndMetadata(2, var2));
        var3.draw();
        var3.startDrawingQuads();
        var3.setNormal(0.0F, 0.0F, 1.0F);
        var0.renderWestFace(var1, 0.0D, 0.0D, 0.0D, var1.getBlockTextureFromSideAndMetadata(3, var2));
        var3.draw();
        var3.startDrawingQuads();
        var3.setNormal(-1.0F, 0.0F, 0.0F);
        var0.renderNorthFace(var1, 0.0D, 0.0D, 0.0D, var1.getBlockTextureFromSideAndMetadata(4, var2));
        var3.draw();
        var3.startDrawingQuads();
        var3.setNormal(1.0F, 0.0F, 0.0F);
        var0.renderSouthFace(var1, 0.0D, 0.0D, 0.0D, var1.getBlockTextureFromSideAndMetadata(5, var2));
        var3.draw();
    }
    
    public static void renderGCUnlitTorch(RenderBlocks renderBlocks, Block par1Block, IBlockAccess var1, int par2, int par3, int par4)
    {
    	final int var5 = var1.getBlockMetadata(par2, par3, par4);
        final Tessellator var6 = Tessellator.instance;
        var6.setBrightness(par1Block.getMixedBrightnessForBlock(var1, par2, par3, par4));
        var6.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        final double var7 = 0.4000000059604645D;
        final double var9 = 0.5D - var7;
        final double var11 = 0.20000000298023224D;

        if (var5 == 1)
        {
            renderTorchAtAngle(renderBlocks, par1Block, par2 - var9, par3 + var11, par4, -var7, 0.0D);
        }
        else if (var5 == 2)
        {
            renderTorchAtAngle(renderBlocks, par1Block, par2 + var9, par3 + var11, par4, var7, 0.0D);
        }
        else if (var5 == 3)
        {
            renderTorchAtAngle(renderBlocks, par1Block, par2, par3 + var11, par4 - var9, 0.0D, -var7);
        }
        else if (var5 == 4)
        {
            renderTorchAtAngle(renderBlocks, par1Block, par2, par3 + var11, par4 + var9, 0.0D, var7);
        }
        else
        {
            renderTorchAtAngle(renderBlocks, par1Block, par2, par3, par4, 0.0D, 0.0D);
        }
    }
    
    public static void renderTorchAtAngle(RenderBlocks renderBlocks, Block par1Block, double par2, double par4, double par6, double par8, double par10)
    {
    	if (par1Block instanceof GCCoreBlockUnlitTorch)
    	{
    		final GCCoreBlockUnlitTorch block = (GCCoreBlockUnlitTorch) par1Block;
    		
    		if (block.lit)
    		{
    			final Tessellator var12 = Tessellator.instance;
    	        int var13 = 11;

    	        if (renderBlocks.overrideBlockTexture >= 0)
    	        {
    	            var13 = renderBlocks.overrideBlockTexture;
    	        }

    	        final int var14 = (var13 & 15) << 4;
    	        final int var15 = var13 & 240;
    	        final float var16 = var14 / 256.0F;
    	        final float var17 = (var14 + 15.99F) / 256.0F;
    	        final float var18 = var15 / 256.0F;
    	        final float var19 = (var15 + 15.99F) / 256.0F;
    	        final double var20 = var16 + 0.02734375D;
    	        final double var22 = var18 + 0.0234375D;
    	        final double var24 = var16 + 0.03515625D;
    	        final double var26 = var18 + 0.03125D;
    	        par2 += 0.5D;
    	        par6 += 0.5D;
    	        final double var28 = par2 - 0.5D;
    	        final double var30 = par2 + 0.5D;
    	        final double var32 = par6 - 0.5D;
    	        final double var34 = par6 + 0.5D;
    	        final double var36 = 0.0625D;
    	        final double var38 = 0.625D;
    	        var12.addVertexWithUV(par2 + par8 * (1.0D - var38) - var36, par4 + var38, par6 + par10 * (1.0D - var38) - var36, var20, var22);
    	        var12.addVertexWithUV(par2 + par8 * (1.0D - var38) - var36, par4 + var38, par6 + par10 * (1.0D - var38) + var36, var20, var26);
    	        var12.addVertexWithUV(par2 + par8 * (1.0D - var38) + var36, par4 + var38, par6 + par10 * (1.0D - var38) + var36, var24, var26);
    	        var12.addVertexWithUV(par2 + par8 * (1.0D - var38) + var36, par4 + var38, par6 + par10 * (1.0D - var38) - var36, var24, var22);
    	        var12.addVertexWithUV(par2 - var36, par4 + 1.0D, var32, var16, var18);
    	        var12.addVertexWithUV(par2 - var36 + par8, par4 + 0.0D, var32 + par10, var16, var19);
    	        var12.addVertexWithUV(par2 - var36 + par8, par4 + 0.0D, var34 + par10, var17, var19);
    	        var12.addVertexWithUV(par2 - var36, par4 + 1.0D, var34, var17, var18);
    	        var12.addVertexWithUV(par2 + var36, par4 + 1.0D, var34, var16, var18);
    	        var12.addVertexWithUV(par2 + par8 + var36, par4 + 0.0D, var34 + par10, var16, var19);
    	        var12.addVertexWithUV(par2 + par8 + var36, par4 + 0.0D, var32 + par10, var17, var19);
    	        var12.addVertexWithUV(par2 + var36, par4 + 1.0D, var32, var17, var18);
    	        var12.addVertexWithUV(var28, par4 + 1.0D, par6 + var36, var16, var18);
    	        var12.addVertexWithUV(var28 + par8, par4 + 0.0D, par6 + var36 + par10, var16, var19);
    	        var12.addVertexWithUV(var30 + par8, par4 + 0.0D, par6 + var36 + par10, var17, var19);
    	        var12.addVertexWithUV(var30, par4 + 1.0D, par6 + var36, var17, var18);
    	        var12.addVertexWithUV(var30, par4 + 1.0D, par6 - var36, var16, var18);
    	        var12.addVertexWithUV(var30 + par8, par4 + 0.0D, par6 - var36 + par10, var16, var19);
    	        var12.addVertexWithUV(var28 + par8, par4 + 0.0D, par6 - var36 + par10, var17, var19);
    	        var12.addVertexWithUV(var28, par4 + 1.0D, par6 - var36, var17, var18);
    		}
    		else
    		{
    			final Tessellator var12 = Tessellator.instance;
    	        int var13 = 12;

    	        if (renderBlocks.overrideBlockTexture >= 0)
    	        {
    	            var13 = renderBlocks.overrideBlockTexture;
    	        }

    	        final int var14 = (var13 & 15) << 4;
    	        final int var15 = var13 & 240;
    	        final float var16 = var14 / 256.0F;
    	        final float var17 = (var14 + 15.99F) / 256.0F;
    	        final float var18 = var15 / 256.0F;
    	        final float var19 = (var15 + 15.99F) / 256.0F;
    	        final double var20 = var16 + 0.02734375D;
    	        final double var22 = var18 + 0.0234375D;
    	        final double var24 = var16 + 0.03515625D;
    	        final double var26 = var18 + 0.03125D;
    	        par2 += 0.5D;
    	        par6 += 0.5D;
    	        final double var28 = par2 - 0.5D;
    	        final double var30 = par2 + 0.5D;
    	        final double var32 = par6 - 0.5D;
    	        final double var34 = par6 + 0.5D;
    	        final double var36 = 0.0625D;
    	        final double var38 = 0.625D;
    	        var12.addVertexWithUV(par2 + par8 * (1.0D - var38) - var36, par4 + var38, par6 + par10 * (1.0D - var38) - var36, var20, var22);
    	        var12.addVertexWithUV(par2 + par8 * (1.0D - var38) - var36, par4 + var38, par6 + par10 * (1.0D - var38) + var36, var20, var26);
    	        var12.addVertexWithUV(par2 + par8 * (1.0D - var38) + var36, par4 + var38, par6 + par10 * (1.0D - var38) + var36, var24, var26);
    	        var12.addVertexWithUV(par2 + par8 * (1.0D - var38) + var36, par4 + var38, par6 + par10 * (1.0D - var38) - var36, var24, var22);
    	        var12.addVertexWithUV(par2 - var36, par4 + 1.0D, var32, var16, var18);
    	        var12.addVertexWithUV(par2 - var36 + par8, par4 + 0.0D, var32 + par10, var16, var19);
    	        var12.addVertexWithUV(par2 - var36 + par8, par4 + 0.0D, var34 + par10, var17, var19);
    	        var12.addVertexWithUV(par2 - var36, par4 + 1.0D, var34, var17, var18);
    	        var12.addVertexWithUV(par2 + var36, par4 + 1.0D, var34, var16, var18);
    	        var12.addVertexWithUV(par2 + par8 + var36, par4 + 0.0D, var34 + par10, var16, var19);
    	        var12.addVertexWithUV(par2 + par8 + var36, par4 + 0.0D, var32 + par10, var17, var19);
    	        var12.addVertexWithUV(par2 + var36, par4 + 1.0D, var32, var17, var18);
    	        var12.addVertexWithUV(var28, par4 + 1.0D, par6 + var36, var16, var18);
    	        var12.addVertexWithUV(var28 + par8, par4 + 0.0D, par6 + var36 + par10, var16, var19);
    	        var12.addVertexWithUV(var30 + par8, par4 + 0.0D, par6 + var36 + par10, var17, var19);
    	        var12.addVertexWithUV(var30, par4 + 1.0D, par6 + var36, var17, var18);
    	        var12.addVertexWithUV(var30, par4 + 1.0D, par6 - var36, var16, var18);
    	        var12.addVertexWithUV(var30 + par8, par4 + 0.0D, par6 - var36 + par10, var16, var19);
    	        var12.addVertexWithUV(var28 + par8, par4 + 0.0D, par6 - var36 + par10, var17, var19);
    	        var12.addVertexWithUV(var28, par4 + 1.0D, par6 - var36, var17, var18);
    		}
    	}
        
    }
}

//package micdoodle8.mods.galacticraft.API;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//import micdoodle8.mods.galacticraft.core.GCCoreChunk;
//import micdoodle8.mods.galacticraft.core.GCCoreEntityCreeper;
//import micdoodle8.mods.galacticraft.core.GCCoreEntitySkeleton;
//import micdoodle8.mods.galacticraft.core.GCCoreEntitySpider;
//import micdoodle8.mods.galacticraft.core.GCCoreEntityZombie;
//import net.minecraft.src.BiomeGenBase;
//import net.minecraft.src.Block;
//import net.minecraft.src.BlockSand;
//import net.minecraft.src.Chunk;
//import net.minecraft.src.ChunkProviderGenerate;
//import net.minecraft.src.EnumCreatureType;
//import net.minecraft.src.IChunkProvider;
//import net.minecraft.src.IProgressUpdate;
//import net.minecraft.src.MapGenMineshaft;
//import net.minecraft.src.MapGenVillage;
//import net.minecraft.src.MathHelper;
//import net.minecraft.src.NoiseGeneratorOctaves;
//import net.minecraft.src.SpawnListEntry;
//import net.minecraft.src.World;
//
///**
// * Copyright 2012-2013, micdoodle8
// * 
// *  All rights reserved.
// *
// */
//public class ChunkProviderBase extends ChunkProviderGenerate
//{
//	ChunkProviderFile chunkProviderFile;
//	
//	private Random rand;
//
//	private NoiseGeneratorOctaves noiseGen1;
//	private NoiseGeneratorOctaves noiseGen2;
//	private NoiseGeneratorOctaves noiseGen3;
//	private NoiseGeneratorOctaves noiseGen4;
//	public NoiseGeneratorOctaves noiseGen5;
//	public NoiseGeneratorOctaves noiseGen6;
//	public NoiseGeneratorOctaves mobSpawnerNoise;
//
////	public GCMarsBiomeDecorator biomedecoratorplanet = new GCMarsBiomeDecorator(GCMarsBiomeGenBase.marsFlat); // TODO
//
//	private World worldObj;
//
//	private final boolean mapFeaturesEnabled;
//
//	private double[] noiseArray;
//	private double[] stoneNoise = new double[256];
//
//	private MapGenVillage villageGenerator = new MapGenVillage();
//
//	private MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();
//
//	private BiomeGenBase[] biomesForGeneration = {GCMarsBiomeGenBase.marsFlat};
//
//	double[] noise1;
//	double[] noise2;
//	double[] noise3;
//	double[] noise5;
//	double[] noise6;
//	float[] field_35388_l;
//	int[][] field_914_i = new int[32][32];
//
//	public ChunkProviderBase(World par1World, long par2, boolean par4, ChunkProviderFile file)
//	{
//		super(par1World, par2, par4);
//		this.chunkProviderFile = file;
//		this.worldObj = par1World;
//		this.mapFeaturesEnabled = par4;
//		this.rand = new Random(par2);
//		this.noiseGen1 = new NoiseGeneratorOctaves(this.rand, 16);
//		this.noiseGen2 = new NoiseGeneratorOctaves(this.rand, 16);
//		this.noiseGen3 = new NoiseGeneratorOctaves(this.rand, 8);
//		this.noiseGen4 = new NoiseGeneratorOctaves(this.rand, 4);
//		this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
//		this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
//		this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
//	}
//
//	public void generateTerrain(int par1, int par2, int[] par3ArrayOfint)
//	{
//		int var4 = 4;
//		int var5 = 16;
//		int var6 = 63;
//		int var7 = var4 + 1;
//		int var8 = 17;
//		int var9 = var4 + 1;
//		this.biomesForGeneration = this.worldObj.getWorldChunkManager().getBiomesForGeneration(this.biomesForGeneration, par1 * 4 - 2, par2 * 4 - 2, var7 + 5, var9 + 5);
//		this.noiseArray = this.initializeNoiseField(this.noiseArray, par1 * var4, 0, par2 * var4, var7, var8, var9);
//
//		for (int var10 = 0; var10 < var4; ++var10) 
//		{
//			for (int var11 = 0; var11 < var4; ++var11)
//			{
//				for (int var12 = 0; var12 < var5; ++var12) 
//				{
//					double var13 = 0.125D;
//					double var15 = this.noiseArray[((var10 + 0) * var9 + var11 + 0) * var8 + var12 + 0];
//					double var17 = this.noiseArray[((var10 + 0) * var9 + var11 + 1) * var8 + var12 + 0];
//					double var19 = this.noiseArray[((var10 + 1) * var9 + var11 + 0) * var8 + var12 + 0];
//					double var21 = this.noiseArray[((var10 + 1) * var9 + var11 + 1) * var8 + var12 + 0];
//					double var23 = (this.noiseArray[((var10 + 0) * var9 + var11 + 0) * var8 + var12 + 1] - var15) * var13;
//					double var25 = (this.noiseArray[((var10 + 0) * var9 + var11 + 1) * var8 + var12 + 1] - var17) * var13;
//					double var27 = (this.noiseArray[((var10 + 1) * var9 + var11 + 0) * var8 + var12 + 1] - var19) * var13;
//					double var29 = (this.noiseArray[((var10 + 1) * var9 + var11 + 1) * var8 + var12 + 1] - var21) * var13;
//
//					for (int var31 = 0; var31 < 8; ++var31) 
//					{
//						double var32 = 0.25D;
//						double var34 = var15;
//						double var36 = var17;
//						double var38 = (var19 - var15) * var32;
//						double var40 = (var21 - var17) * var32;
//
//						for (int var42 = 0; var42 < 4; ++var42) 
//						{
//							int var43 = var42 + var10 * 4 << 11 | 0 + var11 * 4 << 7 | var12 * 8 + var31;
//							short var44 = 128;
//							var43 -= var44;
//							double var45 = 0.25D;
//							double var49 = (var36 - var34) * var45;
//							double var47 = var34 - var49;
//
//							for (int var51 = 0; var51 < 4; ++var51)
//							{
//								if ((var47 += var49) > 0.0D) 
//								{
//									par3ArrayOfint[var43 += var44] = (int) chunkProviderFile.getFillBlockID();
//								}
//								else if (var12 * 8 + var31 < var6) 
//								{
//									par3ArrayOfint[var43 += var44] = 0;
//								} 
//								else
//								{
//									par3ArrayOfint[var43 += var44] = 0;
//								}
//							}
//
//							var34 += var38;
//							var36 += var40;
//						}
//
//						var15 += var23;
//						var17 += var25;
//						var19 += var27;
//						var21 += var29;
//					}
//				}
//			}
//		}
//	}
//
//	public void replaceBlocksForBiome(int par1, int par2, int[] par3ArrayOfint, BiomeGenBase[] par4ArrayOfBiomeGenBase)
//	{
//		int var5 = 20;
//		double var6 = 0.03125D;
//		this.stoneNoise = this.noiseGen4.generateNoiseOctaves(this.stoneNoise, par1 * 16, par2 * 16, 0, 16, 16, 1, var6 * 2.0D, var6 * 2.0D, var6 * 2.0D);
//
//		for (int var8 = 0; var8 < 16; ++var8)
//		{
//			for (int var9 = 0; var9 < 16; ++var9) 
//			{
//				BiomeGenBase var10 = par4ArrayOfBiomeGenBase[var9 + var8 * 16];
//				float var11 = var10.getFloatTemperature();
//				int var12 = (int) (this.stoneNoise[var8 + var9 * 16] / 3.0D + 3.0D + this.rand.nextDouble() * 0.25D);
//				int var13 = -1;
//				int var14 = (int) chunkProviderFile.getTopBlockID();
//				int var15 = (int) chunkProviderFile.getMidBlockID();
//
//				for (int var16 = 127; var16 >= 0; --var16) 
//				{
//					int var17 = (var9 * 16 + var8) * 128 + var16;
//
//					if (var16 <= 0 + this.rand.nextInt(5)) 
//					{
//						par3ArrayOfint[var17] = (int) Block.bedrock.blockID;
//					} else 
//					{
//						int var18 = par3ArrayOfint[var17];
//
//						if (var18 == 0) 
//						{
//							var13 = -1;
//						} 
//						else if (var18 == (int) chunkProviderFile.getFillBlockID())
//						{
//							if (var13 == -1)
//							{
//								if (var12 <= 0)
//								{
//									var14 = 0;
//									var15 = (int) chunkProviderFile.getFillBlockID();
//								} 
//								else if (var16 >= var5 - -16 && var16 <= var5 + 1)
//								{
//									var14 = chunkProviderFile.getTopBlockID();
//									var15 = chunkProviderFile.getMidBlockID();
//								}
//
//								if (var16 < var5 && var14 == 0) 
//								{
//									if (var11 < 0.15F)
//									{
//										var14 = (int) Block.ice.blockID;
//									}
//									else 
//									{
//										var14 = (int) 0;
//									}
//								}
//
//								var13 = var12;
//
//								if (var16 >= var5 - 1) 
//								{
//									par3ArrayOfint[var17] = var14;
//								} else
//								{
//									par3ArrayOfint[var17] = var15;
//								}
//							} 
//							else if (var13 > 0)
//							{
//								--var13;
//								par3ArrayOfint[var17] = var15;
//
//								if (var13 == 0 && var15 == Block.sand.blockID)
//								{
//									var13 = this.rand.nextInt(4);
//									var15 = (int) Block.sandStone.blockID;
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//	}
//
//	@Override
//	public Chunk provideChunk(int par1, int par2)
//	{
//		this.rand.setSeed(par1 * 341873128712L + par2 * 132897987541L);
//		int[] var3 = new int[32768];
//		this.generateTerrain(par1, par2, var3);
//		this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, par1 * 16, par2 * 16, 16, 16);
//		this.replaceBlocksForBiome(par1, par2, var3, this.biomesForGeneration);
//
//		Chunk var4 = new GCCoreChunk(this.worldObj, var3, par1, par2);
//		byte[] var5 = var4.getBiomeArray();
//
//		for (int var6 = 0; var6 < var5.length; ++var6) 
//		{
//			var5[var6] = (byte) this.biomesForGeneration[var6].biomeID;
//		}
//		
//		var4.generateSkylightMap();
//		return var4;
//	}
//
//	private double[] initializeNoiseField(double[] par1ArrayOfDouble, int par2, int par3, int par4, int par5, int par6, int par7)
//	{
//		if (par1ArrayOfDouble == null)
//		{
//			par1ArrayOfDouble = new double[par5 * par6 * par7];
//		}
//
//		if (this.field_35388_l == null) 
//		{
//			this.field_35388_l = new float[25];
//
//			for (int var8 = -2; var8 <= 2; ++var8)
//			{
//				for (int var9 = -2; var9 <= 2; ++var9) 
//				{
//					float var10 = 10.0F / MathHelper.sqrt_float(var8 * var8 + var9 * var9 + 0.2F);
//					this.field_35388_l[var8 + 2 + (var9 + 2) * 5] = var10;
//				}
//			}
//		}
//
//		double var44 = 684.412D;
//		double var45 = 684.412D;
//		this.noise5 = this.noiseGen5.generateNoiseOctaves(this.noise5, par2, par4, par5, par7, 1.121D, 1.121D, 0.5D);
//		this.noise6 = this.noiseGen6.generateNoiseOctaves(this.noise6, par2, par4, par5, par7, 200.0D, 200.0D, 0.5D);
//		this.noise3 = this.noiseGen3.generateNoiseOctaves(this.noise3, par2, par3, par4, par5, par6, par7, var44 / 80.0D, var45 / 160.0D, var44 / 80.0D);
//		this.noise1 = this.noiseGen1.generateNoiseOctaves(this.noise1, par2, par3, par4, par5, par6, par7, var44, var45, var44);
//		this.noise2 = this.noiseGen2.generateNoiseOctaves(this.noise2, par2, par3, par4, par5, par6, par7, var44, var45, var44);
//		boolean var43 = false;
//		boolean var42 = false;
//		int var12 = 0;
//		int var13 = 0;
//
//		for (int var14 = 0; var14 < par5; ++var14) 
//		{
//			for (int var15 = 0; var15 < par7; ++var15) 
//			{
//				float var16 = 0.0F;
//				float var17 = 0.0F;
//				float var18 = 0.0F;
//				int var19 = 2;
//				BiomeGenBase var20 = this.biomesForGeneration[var14 + 2 + (var15 + 2) * (par5 + 5)];
//
//				for (int var21 = -var19; var21 <= var19; ++var21)
//				{
//					for (int var22 = -var19; var22 <= var19; ++var22)
//					{
//						BiomeGenBase var23 = this.biomesForGeneration[var14 + var21 + 2 + (var15 + var22 + 2) * (par5 + 5)];
//						float var24 = this.field_35388_l[var21 + 2 + (var22 + 2) * 5] / (var23.minHeight + 2.0F);
//
//						if (var23.minHeight > var20.minHeight) 
//						{
//							var24 /= 2.0F;
//						}
//
//						var16 += var23.maxHeight * var24 + 2;
//						var17 += var23.minHeight * var24 * 0.5 + 2;
//						var18 += var24;
//					}
//				}
//
//				var16 /= var18;
//				var17 /= var18;
//				var16 = var16 * 0.9F + 0.1F;
//				var17 = (var17 * 4.0F - 1.0F) / 8.0F;
//				double var47 = this.noise6[var13] / 8000.0D;
//
//				if (var47 < 0.0D)
//				{
//					var47 = -var47 * 0.3D;
//				}
//
//				var47 = var47 * 3.0D - 2.0D;
//
//				if (var47 < 0.0D) 
//				{
//					var47 /= 2.0D;
//
//					if (var47 < -1.0D)
//					{
//						var47 = -1.0D;
//					}
//
//					var47 /= 1.4D;
//					var47 /= 2.0D;
//				}
//				else 
//				{
//					if (var47 > 1.0D)
//					{
//						var47 = 1.0D;
//					}
//
//					var47 /= 8.0D;
//				}
//
//				++var13;
//
//				for (int var46 = 0; var46 < par6; ++var46)
//				{
//					double var48 = var17;
//					double var26 = var16;
//					var48 += var47 * 0.2D;
//					var48 = var48 * par6 / 16.0D;
//					double var28 = par6 / 2.0D + var48 * 4.0D;
//					double var30 = 0.0D;
//					double var32 = (var46 - var28) * 12.0D * 128.0D / 128.0D / var26;
//
//					if (var32 < 0.0D)
//					{
//						var32 *= 4.0D;
//					}
//
//					double var34 = this.noise1[var12] / 512.0D;
//					double var36 = this.noise2[var12] / 512.0D;
//					double var38 = (this.noise3[var12] / 10.0D + 1.0D) / 2.0D;
//
//					if (var38 < 0.0D) 
//					{
//						var30 = var34;
//					} 
//					else if (var38 > 1.0D)
//					{
//						var30 = var36;
//					} 
//					else 
//					{
//						var30 = var34 + (var36 - var34) * var38;
//					}
//
//					var30 -= var32;
//
//					if (var46 > par6 - 4)
//					{
//						double var40 = (var46 - (par6 - 4)) / 3.0F;
//						var30 = var30 * (1.0D - var40) + -10.0D * var40;
//					}
//
//					par1ArrayOfDouble[var12] = var30;
//					++var12;
//				}
//			}
//		}
//
//		return par1ArrayOfDouble;
//	}
//
//	@Override
//	public boolean chunkExists(int par1, int par2)
//	{
//		return true;
//	}
//
//	public void decoratePlanet(World par1World, Random par2Random, int par3, int par4)
//	{
////		this.biomedecoratorplanet.decorate(par1World, par2Random, par3, par4);
//	}
//
//	@Override
//	public void populate(IChunkProvider par1IChunkProvider, int par2, int par3)
//	{
//		BlockSand.fallInstantly = true;
//		int var4 = par2 * 16;
//		int var5 = par3 * 16;
//		BiomeGenBase var6 = this.worldObj.getBiomeGenForCoords(var4 + 16, var5 + 16);
//		this.rand.setSeed(this.worldObj.getSeed());
//		long var7 = this.rand.nextLong() / 2L * 2L + 1L;
//		long var9 = this.rand.nextLong() / 2L * 2L + 1L;
//		this.rand.setSeed(par2 * var7 + par3 * var9 ^ this.worldObj.getSeed());
//		boolean var11 = false;
//
//		int var12;
//		int var13;
//		int var14;
//
//		this.decoratePlanet(this.worldObj, this.rand, var4, var5);
//		var4 += 8;
//		var5 += 8;
//
//        if (!var11 && this.rand.nextInt(150) == 0)
//        {
//            var12 = var4 + this.rand.nextInt(16) + 8;
//            var13 = 10;
//            var14 = var5 + this.rand.nextInt(16) + 8;
//            (new GCMarsWorldGenCreeperNest()).generate(this.worldObj, this.rand, var12, var13, var14);
//        }
//
//		BlockSand.fallInstantly = false;
//	}
//
//	@Override
//	public boolean saveChunks(boolean par1, IProgressUpdate par2IProgressUpdate) 
//	{
//		return true;
//	}
//
//	@Override
//	public boolean unload100OldestChunks()
//	{
//		return false;
//	}
//
//	@Override
//	public boolean canSave()
//	{
//		return true;
//	}
//
//	@Override
//	public String makeString()
//	{
//		return "RandomLevelSource";
//	}
//
//	@Override
//	public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType,	int i, int j, int k) 
//	{
//		if (/*j < 39 && */par1EnumCreatureType == EnumCreatureType.monster)
//		{
//			List monsters = new ArrayList();
//			monsters.add(new SpawnListEntry(GCCoreEntityZombie.class, 6, 4, 4));
//			monsters.add(new SpawnListEntry(GCCoreEntitySpider.class, 6, 4, 4));
//			monsters.add(new SpawnListEntry(GCCoreEntitySkeleton.class, 6, 4, 4));
//			monsters.add(new SpawnListEntry(GCCoreEntityCreeper.class, 6, 4, 4));
//			return monsters;
//		}
//		else
//		{
//			return null;
//		}
//	}
//}

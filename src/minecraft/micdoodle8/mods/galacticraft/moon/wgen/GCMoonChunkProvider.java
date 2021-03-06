package micdoodle8.mods.galacticraft.moon.wgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityCreeper;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityFlag;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySkeleton;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpider;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityZombie;
import micdoodle8.mods.galacticraft.core.wgen.GCCoreChunk;
import micdoodle8.mods.galacticraft.moon.blocks.GCMoonBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenVillage;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMoonChunkProvider extends ChunkProviderGenerate
{
	
	private enum CraterSize
	{
		SMALL(8, 12, 14),
		MEDIUM(13, 17, 8),
		LARGE(18, 25, 2),
		EXTREME(26, 30, 1);
		
		public final int MIN_SIZE;
		public final int MAX_SIZE;
		private final int PROBABILITY;
		
		private static CraterSize[] sizeArray;
		
		private CraterSize(int min, int max, int prob)
		{
			MIN_SIZE = min;
			MAX_SIZE = max;
			PROBABILITY = prob;
		}
		
		static
		{
			int amount = 0;
			for(CraterSize c : CraterSize.values())
			{
				amount += c.PROBABILITY;
			}
			sizeArray = new CraterSize[amount];
			int pointer = 0;
			for(CraterSize c : CraterSize.values())
			{
				for(int i = 0; i < c.PROBABILITY; i++)
				{
					sizeArray[pointer] = c;
					pointer++;
				}
			}
		}
	}
	
	private final Random rand;

	private final NoiseGeneratorOctaves noiseGen1;
	private final NoiseGeneratorOctaves noiseGen2;
	private final NoiseGeneratorOctaves noiseGen3;
	private final NoiseGeneratorOctaves noiseGen4;
	public NoiseGeneratorOctaves noiseGen5;
	public NoiseGeneratorOctaves noiseGen6;
	public NoiseGeneratorOctaves mobSpawnerNoise;

	public GCMoonBiomeDecorator biomedecoratorplanet = new GCMoonBiomeDecorator(GCMoonBiomeGenBase.moonFlat);
	
//    private GCMarsMapGenCreeperNest creeperNest = new GCMarsMapGenCreeperNest();

	private final World worldObj;

	private final boolean mapFeaturesEnabled;

	private double[] noiseArray;
	private double[] stoneNoise = new double[256];
	
//	private GCMarsCaveGen caveGenerator = new GCMarsCaveGen();
//	private GCMarsCaveGen2 caveGenerator2 = new GCMarsCaveGen2();

	private final MapGenVillage villageGenerator = new MapGenVillage();

	private final MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();

	private BiomeGenBase[] biomesForGeneration = {GCMoonBiomeGenBase.moonFlat};

	double[] noise1;
	double[] noise2;
	double[] noise3;
	double[] noise4;
	double[] noise5;
	float[] field_35388_l;
	int[][] field_914_i = new int[32][32];
	
	private static final double TERRAIN_HEIGHT_MOD = 2.7;
	private static final double HILL_HEIGHT_MOD = 0.8;
	private static final int CRATER_PROB = 300;
	
	//DO NOT CHANGE
	private static final int MID_HEIGHT = 63;
	private static final int CHUNK_SIZE_X = 16;
	private static final int CHUNK_SIZE_Y = 128;
	private static final int CHUNK_SIZE_Z = 16;
	private static final double HILL_FILTER_MOD = 2.5;
	
	private int chunkX, chunkZ;

	public GCMoonChunkProvider(World par1World, long par2, boolean par4)
	{
		super(par1World, par2, par4);
		this.worldObj = par1World;
		this.mapFeaturesEnabled = par4;
		this.rand = new Random(par2);
		this.noiseGen1 = new NoiseGeneratorOctaves(this.rand, 4);
		this.noiseGen2 = new NoiseGeneratorOctaves(new Random(rand.nextLong()), 6);
		this.noiseGen3 = new NoiseGeneratorOctaves(new Random(rand.nextLong()), 1);
		this.noiseGen4 = new NoiseGeneratorOctaves(new Random(rand.nextLong()), 1);
	}

	public void generateTerrain(int chunkX, int chunkZ, int[] chunkArray)
	{
		noise1 = noiseGen1.generateNoiseOctaves(noise1, chunkX * CHUNK_SIZE_X, chunkZ * CHUNK_SIZE_Z, CHUNK_SIZE_X, CHUNK_SIZE_Z, 0.025D, 0.025D, 0.025D);
		noise2 = noiseGen2.generateNoiseOctaves(noise2, chunkX * CHUNK_SIZE_X, chunkZ * CHUNK_SIZE_Z, CHUNK_SIZE_X, CHUNK_SIZE_Z, 0.07D, 0.07D, 0.07D);
		noise3 = noiseGen3.generateNoiseOctaves(noise3, chunkX * CHUNK_SIZE_X, chunkZ * CHUNK_SIZE_Z, CHUNK_SIZE_X, CHUNK_SIZE_Z, 0.015D, 0.015D, 0.015D);
		
		for(int x = 0; x < CHUNK_SIZE_X; x++)
		{
			for(int z = 0; z < CHUNK_SIZE_Z; z++)
			{
				double d = noise1[z + x * 16] * TERRAIN_HEIGHT_MOD;
				double d2 = Math.abs(noise2[z + x * 16] * HILL_HEIGHT_MOD);
				double d3 = noise3[z + x * 16] * HILL_FILTER_MOD;
				
				double yDev = 0;
				
				if (d3 < 0.0D)
                {
                    yDev = d;
                }
                else if (d3 > 1.0D)
                {
                    yDev = d2;
                }
                else
                {
                    yDev = d + (d2 - d) * d3;
                }
				
				for(int y = 0; y < CHUNK_SIZE_Y; y++)
				{
					if(y < MID_HEIGHT + yDev)
					{
						chunkArray[getIndex(x, y, z)] = GCMoonBlocks.moonStone.blockID;
					}
				}
			}
		}
	}

	public void replaceBlocksForBiome(int par1, int par2, int[] par3ArrayOfint, BiomeGenBase[] par4ArrayOfBiomeGenBase)
	{
		final int var5 = 20;
		final double var6 = 0.03125D;
		this.stoneNoise = this.noiseGen4.generateNoiseOctaves(this.stoneNoise, par1 * 16, par2 * 16, 0, 16, 16, 1, var6 * 2.0D, var6 * 2.0D, var6 * 2.0D);

		for (int var8 = 0; var8 < 16; ++var8)
		{
			for (int var9 = 0; var9 < 16; ++var9) 
			{
				final BiomeGenBase var10 = par4ArrayOfBiomeGenBase[var9 + var8 * 16];
				final float var11 = var10.getFloatTemperature();
				final int var12 = (int) (this.stoneNoise[var8 + var9 * 16] / 3.0D + 3.0D + this.rand.nextDouble() * 0.25D);
				int var13 = -1;
				int var14 = GCMoonBlocks.moonGrass.blockID;
				int var15 = GCMoonBlocks.moonDirt.blockID;

				for (int var16 = 127; var16 >= 0; --var16) 
				{
					final int var17 = (var9 * 16 + var8) * 128 + var16;

					if (var16 <= 0 + this.rand.nextInt(5)) 
					{
						par3ArrayOfint[var17] = Block.bedrock.blockID;
					} 
					else 
					{
						final int var18 = par3ArrayOfint[var17];

						if (var18 == 0) 
						{
							var13 = -1;
						} 
						else if (var18 == GCMoonBlocks.moonStone.blockID)
						{
							if (var13 == -1)
							{
								if (var12 <= 0)
								{
									var14 = 0;
									var15 = GCMoonBlocks.moonStone.blockID;
								} 
								else if (var16 >= var5 - -16 && var16 <= var5 + 1)
								{
									var14 = GCMoonBlocks.moonGrass.blockID;
									var15 = GCMoonBlocks.moonDirt.blockID;
								}

								if (var16 < var5 && var14 == 0) 
								{
									if (var11 < 0.15F)
									{
										var14 = Block.ice.blockID;
									}
									else 
									{
										var14 = 0;
									}
								}

								var13 = var12;

								if (var16 >= var5 - 1) 
								{
									par3ArrayOfint[var17] = var14;
								} else if (var16 < var5 - 1 && var16 >= var5 - 2)
								{
									par3ArrayOfint[var17] = var15;
								}
							} 
							else if (var13 > 0)
							{
								--var13;
								par3ArrayOfint[var17] = var15;

								if (var13 == 0 && var15 == Block.sand.blockID)
								{
									var13 = this.rand.nextInt(4);
									var15 = Block.sandStone.blockID;
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public Chunk provideChunk(int par1, int par2)
	{
		this.rand.setSeed(par1 * 341873128712L + par2 * 132897987541L);
		final int[] var3 = new int[32768];
		this.generateTerrain(par1, par2, var3);
		this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, par1 * 16, par2 * 16, 16, 16);
		createCraters(par1, par2, var3);
		this.replaceBlocksForBiome(par1, par2, var3, this.biomesForGeneration);
//		this.caveGenerator.generate(this, this.worldObj, par1, par2, var3);
//		this.caveGenerator2.generate(this, this.worldObj, par1, par2, var3);

		final Chunk var4 = new GCCoreChunk(this.worldObj, var3, null, par1, par2);
		final byte[] var5 = var4.getBiomeArray();

		for (int var6 = 0; var6 < var5.length; ++var6) 
		{
			var5[var6] = (byte) this.biomesForGeneration[var6].biomeID;
		}
		
		final List list = this.worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(0 - 5, 0, 0 - 5, 5, 140, 5));
		boolean flag = false;
		
		for (int i = 0; i < list.size(); i++)
		{
			if (list.get(i) != null)
			{
				final Entity e = (Entity) list.get(i);
				
				if (e != null && e instanceof GCCoreEntityFlag)
				{
					flag = true;
				}
			}
		}
        
        if (!flag)
        {
        	final GCCoreEntityFlag entity = new GCCoreEntityFlag(this.worldObj, 0, 130, 0, 0F);
        	entity.setType(0);
        	entity.setIndestructable();
			this.worldObj.spawnEntityInWorld(entity);
        }
		
//		this.creeperNest.generate(this, this.worldObj, par1, par2, var3);
		var4.generateSkylightMap();
		return var4;
	}

	public void createCraters(int chunkX, int chunkZ, int[] chunkArray)
	{
		for(int cx = chunkX - 2; cx <= chunkX + 2; cx++)
		{
			for(int cz = chunkZ - 2; cz <= chunkZ + 2; cz++)
			{
				for(int x = 0; x < CHUNK_SIZE_X; x++)
				{
					for(int z = 0; z < CHUNK_SIZE_Z; z++)
					{
						noise4 = noiseGen4.generateNoiseOctaves(noise4, cx * CHUNK_SIZE_X + x, cz * CHUNK_SIZE_Z + z, 1, 1, 0.015D, 0.015D, 0.015D);
						if(Math.abs(randFromPoint(cx * 16 + x, (cz * 16 + z) * 1000)) < noise4[0] / CRATER_PROB)
						{
							Random random = new Random((cx * 16 + x) + (cz * 16 + z) * 5000);
							CraterSize cSize = CraterSize.sizeArray[random.nextInt(CraterSize.sizeArray.length)];
							int size = random.nextInt(cSize.MAX_SIZE - cSize.MIN_SIZE) + cSize.MIN_SIZE;
							makeCrater(cx * 16 + x, cz * 16 + z, chunkX * 16, chunkZ * 16, size, chunkArray);
						}
					}
				}
			}
		}
	}
	
	public void makeCrater(int craterX, int craterZ, int chunkX, int chunkZ, int size, int[] chunkArray)
	{
		for(int x = 0; x < CHUNK_SIZE_X; x++)
		{
			for(int z = 0; z < CHUNK_SIZE_Z; z++)
			{
				double xDev = craterX - (chunkX + x);
				double zDev = craterZ - (chunkZ + z);
				if(xDev * xDev + zDev * zDev < size * size)
				{
					xDev /= size;
					zDev /= size;
					double sqrtY = (xDev * xDev + zDev * zDev);
					double yDev = sqrtY * sqrtY * 6;
					yDev = 5 - yDev;
					System.out.println(yDev + ", " + xDev + ", " + zDev);
					int helper = 0;
					for(int y = 127; y > 0; y--)
					{
						if(chunkArray[getIndex(x, y, z)] != 0 && helper <= yDev)
						{
							chunkArray[getIndex(x, y, z)] = 0;
							helper++;
						}
						if(helper > yDev)
						{
							break;
						}
					}
				}
			}
		}
	}
	
	/*private double[] initializeNoiseField(double[] par1ArrayOfDouble, int par2, int par3, int par4, int par5, int par6, int par7)
	{
		if (par1ArrayOfDouble == null)
		{
			par1ArrayOfDouble = new double[par5 * par6 * par7];
		}

		if (this.field_35388_l == null) 
		{
			this.field_35388_l = new float[25];

			for (int var8 = -2; var8 <= 2; ++var8)
			{
				for (int var9 = -2; var9 <= 2; ++var9) 
				{
					final float var10 = 10.0F / MathHelper.sqrt_float(var8 * var8 + var9 * var9 + 0.2F);
					this.field_35388_l[var8 + 2 + (var9 + 2) * 5] = var10;
				}
			}
		}

		final double var44 = 684.412D;
		final double var45 = 684.412D;
		this.noise5 = this.noiseGen5.generateNoiseOctaves(this.noise5, par2, par4, par5, par7, 1.121D, 1.121D, 0.5D);
		this.noise6 = this.noiseGen6.generateNoiseOctaves(this.noise6, par2, par4, par5, par7, 200.0D, 200.0D, 0.5D);
		this.noise3 = this.noiseGen3.generateNoiseOctaves(this.noise3, par2, par3, par4, par5, par6, par7, var44 / 80.0D, var45 / 160.0D, var44 / 80.0D);
		this.noise1 = this.noiseGen1.generateNoiseOctaves(this.noise1, par2, par3, par4, par5, par6, par7, var44, var45, var44);
		this.noise2 = this.noiseGen2.generateNoiseOctaves(this.noise2, par2, par3, par4, par5, par6, par7, var44, var45, var44);
		final boolean var43 = false;
		final boolean var42 = false;
		int var12 = 0;
		int var13 = 0;

		for (int var14 = 0; var14 < par5; ++var14) 
		{
			for (int var15 = 0; var15 < par7; ++var15) 
			{
				float var16 = 0.0F;
				float var17 = 0.0F;
				float var18 = 0.0F;
				final int var19 = 2;
				final BiomeGenBase var20 = this.biomesForGeneration[var14 + 2 + (var15 + 2) * (par5 + 5)];

				for (int var21 = -var19; var21 <= var19; ++var21)
				{
					for (int var22 = -var19; var22 <= var19; ++var22)
					{
						final BiomeGenBase var23 = this.biomesForGeneration[var14 + var21 + 2 + (var15 + var22 + 2) * (par5 + 5)];
						float var24 = this.field_35388_l[var21 + 2 + (var22 + 2) * 5] / (var23.minHeight + 2.0F);

						if (var23.minHeight > var20.minHeight) 
						{
							var24 /= 2.0F;
						}

						var16 += var23.maxHeight * var24;
						var17 += var23.minHeight * var24 * 0.5 + 2;
						var18 += var24;
					}
				}

				var16 /= var18;
				var17 /= var18;
				var16 = var16 * 0.9F + 0.1F;
				var17 = (var17 * 4.0F - 1.0F) / 8.0F;
				double var47 = this.noise6[var13] / 8000.0D;

				if (var47 < 0.0D)
				{
					var47 = -var47 * 0.3D;
				}

				var47 = var47 * 3.0D - 2.0D;

				if (var47 < 0.0D) 
				{
					var47 /= 2.0D;

					if (var47 < -1.0D)
					{
						var47 = -1.0D;
					}

					var47 /= 1.4D;
					var47 /= 2.0D;
				}
				else 
				{
					if (var47 > 1.0D)
					{
						var47 = 1.0D;
					}

					var47 /= 8.0D;
				}

				++var13;

				for (int var46 = 0; var46 < par6; ++var46)
				{
					double var48 = var17;
					final double var26 = var16;
					var48 += var47 * 0.2D;
					var48 = var48 * par6 / 16.0D;
					final double var28 = par6 / 2.0D + var48 * 4.0D;
					double var30 = 0.0D;
					double var32 = (var46 - var28) * 12.0D * 128.0D / 128.0D / var26;

					if (var32 < 0.0D)
					{
						var32 *= 4.0D;
					}

					final double var34 = this.noise1[var12] / 512.0D;
					final double var36 = this.noise2[var12] / 512.0D;
					final double var38 = (this.noise3[var12] / 10.0D + 1.0D) / 2.0D;

					if (var38 < 0.0D) 
					{
						var30 = var34;
					} 
					else if (var38 > 1.0D)
					{
						var30 = var36;
					} 
					else 
					{
						var30 = var34 + (var36 - var34) * var38;
					}

					var30 -= var32;

					if (var46 > par6 - 4)
					{
						final double var40 = (var46 - (par6 - 4)) / 3.0F;
						var30 = var30 * (1.0D - var40) + -10.0D * var40;
					}

					par1ArrayOfDouble[var12] = var30;
					++var12;
				}
			}
		}

		return par1ArrayOfDouble;
	}*/

	@Override
	public boolean chunkExists(int par1, int par2)
	{
		return true;
	}
	
	/*private void computeCraters(int x, int z, int[] chunkArr) 
	{
		boolean evenX, evenZ;
		evenX = x % 2 == 0 ? true : false;
		evenZ = z % 2 == 0 ? true : false;
		this.chunkX = x * 16;
		this.chunkZ = z * 16;

		if (evenX && evenZ) 
		{
			this.createCrater(x, z, chunkArr);
		} 
		else if (evenX) 
		{
			this.createCrater(x, z + 1, chunkArr);
			this.createCrater(x, z - 1, chunkArr);
			this.createCrater(x + 2, z + 1, chunkArr);
			this.createCrater(x + 2, z - 1, chunkArr);
			this.createCrater(x - 2, z + 1, chunkArr);
			this.createCrater(x - 2, z - 1, chunkArr);
		} 
		else if (evenZ) 
		{
			this.createCrater(x + 1, z, chunkArr);
			this.createCrater(x - 1, z, chunkArr);
			this.createCrater(x + 1, z + 2, chunkArr);
			this.createCrater(x - 1, z + 2, chunkArr);
			this.createCrater(x + 1, z + 2, chunkArr);
			this.createCrater(x - 1, z + 2, chunkArr);
		} 
		else 
		{
			this.createCrater(x + 1, z + 1, chunkArr);
			this.createCrater(x - 1, z + 1, chunkArr);
			this.createCrater(x + 1, z - 1, chunkArr);
			this.createCrater(x - 1, z - 1, chunkArr);
		}
	}
	
	private void createCrater(int chnkX, int chnkZ, int[] chunkArr) 
	{
		final double centerRand = this.randFromPoint(chnkX, chnkZ);
		final int maxCenterDelta = 6;
		int centerX, centerZ, radius;

		centerX = chnkX * 16 + 8 + (int) (maxCenterDelta * centerRand);
		centerZ = chnkZ * 16 + 8 + (int) (maxCenterDelta * centerRand);
		radius = (int) ((centerRand + 1) * 8) + 8;

		int distance, sphereY = 0, index = 0;
		boolean inSphere;
		for (int z = 0; z < 16; z++) 
		{
			for (int x = 0; x < 16; x++) 
			{
				distance = -1 * centerX * centerX + 2 * (x + this.chunkX) * centerX - centerZ * centerZ + 2 * centerZ * (z + this.chunkZ) + radius * radius - (x + this.chunkX) * (x + this.chunkX) - (z + this.chunkZ) * (z + this.chunkZ);

				if (distance > 0) 
				{
					sphereY = (int) (Math.sqrt(distance) / 2.5);
				} 
				else 
				{
					continue;
				}

				inSphere = false;

				for (int y = 90; y > 0; y--) 
				{
					index = this.getIndex(x, y, z);

					if (sphereY == 0) 
					{
						break;
					}

					if (inSphere) 
					{
						chunkArr[index] = 0;
						sphereY--;
						continue;
					}

					if (chunkArr[index] == 0) 
					{
						continue;
					} 
					else 
					{
						y++;
						inSphere = true;
					}
				}

				if (this.worldObj.rand.nextDouble() < 0.8 && inSphere) 
				{
					chunkArr[index] = GCMoonBlocks.moonGrass.blockID;
				}
			}
		}
	}*/
	
	private int getIndex(int x, int y, int z) 
	{
		return (x * 16 + z) * 128 + y;
	}
	
	private double randFromPoint(int x, int z) 
	{
		int n;
		n = x + z * 57;
		n = n << 13 ^ n;
		return 1.0 - (n * (n * n * 15731 + 789221) + 1376312589 & 0x7fffffff) / 1073741824.0;
	}

	public void decoratePlanet(World par1World, Random par2Random, int par3, int par4)
	{
		this.biomedecoratorplanet.decorate(par1World, par2Random, par3, par4);
	}

	@Override
	public void populate(IChunkProvider par1IChunkProvider, int par2, int par3)
	{
		BlockSand.fallInstantly = true;
		int var4 = par2 * 16;
		int var5 = par3 * 16;
		final BiomeGenBase var6 = this.worldObj.getBiomeGenForCoords(var4 + 16, var5 + 16);
		this.rand.setSeed(this.worldObj.getSeed());
		final long var7 = this.rand.nextLong() / 2L * 2L + 1L;
		final long var9 = this.rand.nextLong() / 2L * 2L + 1L;
		this.rand.setSeed(par2 * var7 + par3 * var9 ^ this.worldObj.getSeed());
		final boolean var11 = false;

		final int var12;
		final int var13;
		final int var14;

		this.decoratePlanet(this.worldObj, this.rand, var4, var5); 
		var4 += 8;
		var5 += 8;

		BlockSand.fallInstantly = false;
	}

	@Override
	public boolean saveChunks(boolean par1, IProgressUpdate par2IProgressUpdate) 
	{
		return true;
	}

	@Override
	public boolean unload100OldestChunks()
	{
		return false;
	}

	@Override
	public boolean canSave()
	{
		return true;
	}

	@Override
	public String makeString()
	{
		return "RandomLevelSource";
	}

	@Override
	public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType,	int i, int j, int k) 
	{
		if (/*j < 39 && */par1EnumCreatureType == EnumCreatureType.monster)
		{
			final List monsters = new ArrayList();
			monsters.add(new SpawnListEntry(GCCoreEntityZombie.class, 1, 1, 3));
			monsters.add(new SpawnListEntry(GCCoreEntitySpider.class, 1, 1, 3));
			monsters.add(new SpawnListEntry(GCCoreEntitySkeleton.class, 1, 1, 3));
			monsters.add(new SpawnListEntry(GCCoreEntityCreeper.class, 1, 1, 3));
			return monsters;
		}
		else
		{
			return null;
		}
	}
}

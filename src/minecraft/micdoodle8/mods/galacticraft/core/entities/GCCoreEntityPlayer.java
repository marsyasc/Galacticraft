package micdoodle8.mods.galacticraft.core.entities;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import micdoodle8.mods.galacticraft.API.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreTeleporter;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.tile.GCCoreInventoryTankRefill;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet41EntityEffect;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.GameRegistry;

public class GCCoreEntityPlayer
{
	private final EntityPlayer currentPlayer;
	
	private int airRemaining;
	private int airRemaining2;
	
	public boolean hasTank;
	
	public ItemStack tankInSlot;
	
	public GCCoreInventoryTankRefill playerTankInventory = new GCCoreInventoryTankRefill();
	
	public boolean inPortal;
	
	public int timeUntilPortal;
	
	private int dimensionToSend = -2;
	
	private int damageCounter;

    public int astronomyPointsLevel;

    public int astronomyPointsTotal;

    public float astronomyPoints;
    
    public ItemStack[] rocketStacks;
    public int rocketType;
	
	public GCCoreEntityPlayer(EntityPlayer player) 
	{
		this.currentPlayer = player;
		GalacticraftCore.players.add(player);
		GalacticraftCore.gcPlayers.add(this);
		MinecraftForge.EVENT_BUS.register(this);
		player.getDataWatcher().addObject(23, new Integer(0));
	}
	
	public EntityPlayer getPlayer()
	{
		return this.currentPlayer;
	}

    public boolean isAABBInBreathableAirBlock()
    {
        final int var3 = MathHelper.floor_double(this.currentPlayer.boundingBox.minX);
        final int var4 = MathHelper.floor_double(this.currentPlayer.boundingBox.maxX + 1.0D);
        final int var5 = MathHelper.floor_double(this.currentPlayer.boundingBox.minY);
        final int var6 = MathHelper.floor_double(this.currentPlayer.boundingBox.maxY + 1.0D);
        final int var7 = MathHelper.floor_double(this.currentPlayer.boundingBox.minZ);
        final int var8 = MathHelper.floor_double(this.currentPlayer.boundingBox.maxZ + 1.0D);

        for (int var9 = var3; var9 < var4; ++var9)
        {
            for (int var10 = var5; var10 < var6; ++var10)
            {
                for (int var11 = var7; var11 < var8; ++var11)
                {
                    final Block var12 = Block.blocksList[this.currentPlayer.worldObj.getBlockId(var9, var10, var11)];

                    if (var12 != null && var12.blockID == GCCoreBlocks.breatheableAir.blockID)
                    {
                        final int var13 = this.currentPlayer.worldObj.getBlockMetadata(var9, var10, var11);
                        double var14 = var10 + 1;

                        if (var13 < 8)
                        {
                            var14 = var10 + 1 - var13 / 8.0D;
                        }

                        if (var14 >= this.currentPlayer.boundingBox.minY)
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
	
	@ForgeSubscribe
	public void onUpdate(LivingEvent event)
	{
		if (event.entityLiving instanceof EntityPlayer)
		{
			final EntityPlayer player = (EntityPlayer) event.entityLiving;
			
			if (player.worldObj.provider instanceof IGalacticraftWorldProvider && player.inventory.getCurrentItem() != null)
	        {
	        	final int var1 = player.inventory.getCurrentItem().stackSize;
	        	final int var2 = player.inventory.getCurrentItem().getItemDamage();
	        	
				if (player.inventory.getCurrentItem().getItem().itemID == Block.torchWood.blockID)
				{
		        	final ItemStack stack = new ItemStack(GCCoreBlocks.unlitTorch, var1, 0);
		            player.inventory.mainInventory[player.inventory.currentItem] = stack;
				}
				else if (player.inventory.getCurrentItem().getItem().itemID == Item.bow.itemID)
				{
		        	final Hashtable<Integer, Enchantment> enchants = new Hashtable<Integer, Enchantment>();
		        	
		        	final NBTTagList list = player.inventory.getCurrentItem().getEnchantmentTagList();

		        	if (list != null)
		            {
		                for (int var7 = 0; var7 < list.tagCount(); ++var7)
		                {
		                    final int enchID = Integer.valueOf(((NBTTagCompound)list.tagAt(var7)).getShort("id"));
		                    final int enchLvl = Integer.valueOf(((NBTTagCompound)list.tagAt(var7)).getShort("lvl"));
		                    
		                    final Enchantment e = Enchantment.enchantmentsList[enchID];
		                    
		                    enchants.put(enchLvl, e);
		                }
		            }
		        	
		        	final ItemStack stack = new ItemStack(GCCoreItems.gravityBow, var1, var2);
		        	
		        	final Iterator<Entry<Integer, Enchantment>> it = enchants.entrySet().iterator();
		        	
		        	while (it.hasNext())
		        	{
		        		final Entry<Integer, Enchantment> entry = it.next();
		        		
		        		if (entry.getKey() != null && entry.getValue() != null)
		        		{
			        		stack.addEnchantment(entry.getValue(), entry.getKey());
		        		}
		        	}
		        	
		            player.inventory.mainInventory[player.inventory.currentItem] = stack;
				}
				else if (player.inventory.getCurrentItem().getItem().itemID == Block.sapling.blockID)
				{
					// No jungle trees...
					if (var2 != 3)
					{
			        	final ItemStack stack = new ItemStack(GCCoreBlocks.sapling, var1, var2);
			            player.inventory.mainInventory[player.inventory.currentItem] = stack;
					}
				}
	        }
	        else if (!(player.worldObj.provider instanceof IGalacticraftWorldProvider) && player.inventory.getCurrentItem() != null)
	        {
	        	final int var1 = player.inventory.getCurrentItem().stackSize;
	        	final int var2 = player.inventory.getCurrentItem().getItemDamage();
	        	
	        	if (player.inventory.getCurrentItem().getItem().itemID == GCCoreBlocks.unlitTorch.blockID)
	        	{
	            	final ItemStack stack = new ItemStack(Block.torchWood, var1, 0);
	                player.inventory.mainInventory[player.inventory.currentItem] = stack;
	        	}
	        	else if (player.inventory.getCurrentItem().getItem().itemID == GCCoreBlocks.sapling.blockID)
	        	{
	            	final ItemStack stack = new ItemStack(Block.sapling, var1, var2);
	                player.inventory.mainInventory[player.inventory.currentItem] = stack;
	        	}
	        	else if (player.inventory.getCurrentItem().getItem().itemID == GCCoreItems.gravityBow.itemID)
	        	{
		        	final Hashtable<Integer, Enchantment> enchants = new Hashtable<Integer, Enchantment>();
		        	
		        	final NBTTagList list = player.inventory.getCurrentItem().getEnchantmentTagList();

		        	if (list != null)
		            {
		                for (int var7 = 0; var7 < list.tagCount(); ++var7)
		                {
		                    final int enchID = Integer.valueOf(((NBTTagCompound)list.tagAt(var7)).getShort("id"));
		                    final int enchLvl = Integer.valueOf(((NBTTagCompound)list.tagAt(var7)).getShort("lvl"));
		                    
		                    final Enchantment e = Enchantment.enchantmentsList[enchID];
		                    
		                    enchants.put(enchLvl, e);
		                }
		            }
		        	
	            	final ItemStack stack = new ItemStack(Item.bow, var1, var2);
		        	
		        	final Iterator<Entry<Integer, Enchantment>> it = enchants.entrySet().iterator();
		        	
		        	while (it.hasNext())
		        	{
		        		final Entry<Integer, Enchantment> entry = it.next();
		        		
		        		if (entry.getKey() != null && entry.getValue() != null)
		        		{
			        		stack.addEnchantment(entry.getValue(), entry.getKey());
		        		}
		        	}
		        	
	                player.inventory.mainInventory[player.inventory.currentItem] = stack;
	        	}
	        }
			
			if (this.damageCounter > 0)
			{
				this.damageCounter--;
			}
	        
			if (GalacticraftCore.tick % 10 == 0)
			{
				this.sendAirRemainingPacket();
			}
			
			if (player != null && player.worldObj.provider instanceof IGalacticraftWorldProvider)
			{
				player.fallDistance = 0.0F;
			}

			final ItemStack tankInSlot = this.playerTankInventory.getStackInSlot(2);
			final ItemStack tankInSlot2 = this.playerTankInventory.getStackInSlot(3);
			
			final int drainSpacing = GCCoreUtil.getDrainSpacing(tankInSlot);
			final int drainSpacing2 = GCCoreUtil.getDrainSpacing(tankInSlot2);
						
			if (player.worldObj.provider instanceof IGalacticraftWorldProvider && !player.capabilities.isCreativeMode)
	        {
				if (tankInSlot == null)
				{
					this.airRemaining = 0;
				}
				
				if (tankInSlot2 == null)
				{
					this.airRemaining2 = 0;
				}
				
				if (drainSpacing > 0)
				{
		    		this.airRemaining = 90 - tankInSlot.getItemDamage();
				}
				
				if (drainSpacing2 > 0)
				{
		    		this.airRemaining2 = 90 - tankInSlot2.getItemDamage();
				}
				
				if (drainSpacing > 0 && GalacticraftCore.tick % drainSpacing == 0 && !this.isAABBInBreathableAirBlock() && 90 - tankInSlot.getItemDamage() > 0) 
		    	{
		    		tankInSlot.damageItem(1, player);
		    	}
				
				if (drainSpacing2 > 0 && GalacticraftCore.tick % drainSpacing2 == 0 && !this.isAABBInBreathableAirBlock() && 90 - tankInSlot2.getItemDamage() > 0) 
		    	{
		    		tankInSlot2.damageItem(1, player);
		    	}
				
				if (drainSpacing == 0 && GalacticraftCore.tick % 20 == 0 && !this.isAABBInBreathableAirBlock() && this.airRemaining > 0)
				{
		    		this.airRemaining -= 1;
				}
				
				if (drainSpacing2 == 0 && GalacticraftCore.tick % 20 == 0 && !this.isAABBInBreathableAirBlock() && this.airRemaining2 > 0)
				{
		    		this.airRemaining2 -= 1;
				}
				
				if (this.airRemaining < 0)
				{
					this.airRemaining = 0;
				}
				
				if (this.airRemaining2 < 0)
				{
					this.airRemaining2 = 0;
				}
				
				if (GalacticraftCore.tick % 20 == 0 && this.isAABBInBreathableAirBlock() && this.airRemaining < 90 && tankInSlot != null)
				{
					this.airRemaining += 1;
				}
				
				if (GalacticraftCore.tick % 20 == 0 && this.isAABBInBreathableAirBlock() && this.airRemaining2 < 90 && tankInSlot2 != null)
				{
					this.airRemaining2 += 1;
				}
				
	        	if (this.damageCounter == 0) 
	        	{
	        		ItemStack helmetSlot = null;
	        		
	        		if (player.inventory.armorItemInSlot(3) != null)
	        		{
	        			helmetSlot = player.inventory.armorItemInSlot(3);
	        		}
	        		
	        		final boolean flag5 = (this.airRemaining <= 0 || this.airRemaining2 <= 0);
	        		final boolean invalid = !GCCoreUtil.hasValidOxygenSetup(player) || flag5;
	        		
	        		if (invalid && !this.isAABBInBreathableAirBlock()) 
					{
	        			if (!player.worldObj.isRemote && player.isEntityAlive())
	        			{
	        				if (this.damageCounter == 0) 
	        	        	{
		        				this.damageCounter = 100;
	        		            player.attackEntityFrom(DamageSource.inWall, 2);
	        	        	}
	        			}
					}
				}
	        }
			else if (GalacticraftCore.tick % 20 == 0 && !player.capabilities.isCreativeMode && this.airRemaining < 90)
			{
				this.airRemaining += 1;
				this.airRemaining2 += 1;
			}
			else if (player.capabilities.isCreativeMode)
			{
				this.airRemaining = 90;
				this.airRemaining2 = 90;
			}
		}
		
		if (this.getParachute())
		{
//			if (!this.getPlayer().isAirBorne || this.getPlayer().isDead)
//			{
//				this.usingParachute = false;
//			}
			
			this.getPlayer().motionY += 1;
		}
		
		if (this.timeUntilPortal > 0)
		{
			this.timeUntilPortal--;
		}
		
		if (this.inPortal && this.timeUntilPortal == 0)
        {
			if (this.currentPlayer instanceof EntityPlayerMP && !this.currentPlayer.worldObj.isRemote)
			{
				final EntityPlayerMP player = (EntityPlayerMP) this.currentPlayer;
				
	        	byte var5;
	        	
	            if (this.dimensionToSend != -2)
	            {
	            	var5 = (byte) this.dimensionToSend;
	            }
	            else
	            {
	            	var5 = 0;
	            }

	    		for (int i = 0; i < player.mcServer.worldServerForDimension(var5).customTeleporters.size(); i++)
	    		{
	    			if (player.mcServer.worldServerForDimension(var5).customTeleporters.get(i) instanceof GCCoreTeleporter)
	    			{
	        			this.transferPlayerToDimension(player, var5, player.mcServer.worldServerForDimension(var5).customTeleporters.get(i));
	    			}
	    		}
	    		
	            player.timeUntilPortal = 10;
	            
	            final Object[] toSend = {0.0F};
	            PacketDispatcher.sendPacketToPlayer(GCCoreUtil.createPacket("Galacticraft", 1, toSend), (Player)player);
	            
	            this.inPortal = false;
			}
        }
		
		if (this.currentPlayer.worldObj.provider instanceof IGalacticraftWorldProvider && FMLCommonHandler.instance().getEffectiveSide() != Side.CLIENT)
		{
			if (((IGalacticraftWorldProvider)this.currentPlayer.worldObj.provider).getMeteorFrequency() > 0)
			{
				final float f = ((IGalacticraftWorldProvider)this.currentPlayer.worldObj.provider).getMeteorFrequency();
				
				if (this.currentPlayer.worldObj.rand.nextInt(MathHelper.floor_float(f * 1000)) == 0)
				{
					int x, y, z;
					double motX, motZ;
					x = this.currentPlayer.worldObj.rand.nextInt(20) - 10;
					y = this.currentPlayer.worldObj.rand.nextInt(20) + 200;
					z = this.currentPlayer.worldObj.rand.nextInt(20) - 10;
					motX = this.currentPlayer.worldObj.rand.nextDouble() * 5;
					motZ = this.currentPlayer.worldObj.rand.nextDouble() * 5;
					
					final GCCoreEntityMeteor meteor = new GCCoreEntityMeteor(this.currentPlayer.worldObj, this.currentPlayer.posX + x, this.currentPlayer.posY + y, this.currentPlayer.posZ + z, motX - 2.5D, 0, motZ - 2.5D, 1);
					
					if (!this.currentPlayer.worldObj.isRemote)
					{
						this.currentPlayer.worldObj.spawnEntityInWorld(meteor);
					}
				}
				if (this.currentPlayer.worldObj.rand.nextInt(MathHelper.floor_float(f * 3000)) == 0)
				{
					int x, y, z;
					double motX, motZ;
					x = this.currentPlayer.worldObj.rand.nextInt(20) - 10;
					y = this.currentPlayer.worldObj.rand.nextInt(20) + 200;
					z = this.currentPlayer.worldObj.rand.nextInt(20) - 10;
					motX = this.currentPlayer.worldObj.rand.nextDouble() * 5;
					motZ = this.currentPlayer.worldObj.rand.nextDouble() * 5;
					
					final GCCoreEntityMeteor meteor = new GCCoreEntityMeteor(this.currentPlayer.worldObj, this.currentPlayer.posX + x, this.currentPlayer.posY + y, this.currentPlayer.posZ + z, motX - 2.5D, 0, motZ - 2.5D, 6);
					
					if (!this.currentPlayer.worldObj.isRemote)
					{
						this.currentPlayer.worldObj.spawnEntityInWorld(meteor);
					}
				}
			}
		}
	}

    public void travelToTheEnd(int par1)
    {
    	if (this.currentPlayer instanceof EntityPlayerMP)
    	{
    		final EntityPlayerMP player = (EntityPlayerMP) this.currentPlayer;
    		for (int i = 0; i < player.mcServer.worldServerForDimension(par1).customTeleporters.size(); i++)
    		{
    			if (player.mcServer.worldServerForDimension(par1).customTeleporters.get(i) instanceof GCCoreTeleporter)
    			{
        			this.transferPlayerToDimension(player, par1, player.mcServer.worldServerForDimension(par1).customTeleporters.get(i));
    			}
    		}
    	}
    }

    public void transferPlayerToDimension(EntityPlayerMP player, int par2, Teleporter teleporter)
    {
        int var3 = player.dimension;
        WorldServer var4 = player.mcServer.getConfigurationManager().getServerInstance().worldServerForDimension(player.dimension);
        player.dimension = par2;
        WorldServer var5 = player.mcServer.getConfigurationManager().getServerInstance().worldServerForDimension(player.dimension);

        player.playerNetServerHandler.sendPacketToPlayer(new Packet9Respawn(player.dimension, (byte)player.worldObj.difficultySetting, var5.getWorldInfo().getTerrainType(), var5.getHeight(), player.theItemInWorldManager.getGameType()));
        var4.removeEntity(player);
        player.isDead = false;
        this.transferEntityToWorld(player, var3, var4, var5, teleporter);
        player.mcServer.getConfigurationManager().func_72375_a(player, var4);
        player.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
        player.theItemInWorldManager.setWorld(var5);
        player.mcServer.getConfigurationManager().updateTimeAndWeatherForPlayer(player, var5);
        player.mcServer.getConfigurationManager().syncPlayerInventory(player);
        Iterator var6 = player.getActivePotionEffects().iterator();

        while (var6.hasNext())
        {
            PotionEffect var7 = (PotionEffect)var6.next();
            player.playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(player.entityId, var7));
        }

        GameRegistry.onPlayerChangedDimension(player);
    }

    public void transferEntityToWorld(Entity par1Entity, int par2, WorldServer par3WorldServer, WorldServer par4WorldServer, Teleporter teleporter)
    {
        WorldProvider pOld = par3WorldServer.provider;
        WorldProvider pNew = par4WorldServer.provider;
        double moveFactor = pOld.getMovementFactor() / pNew.getMovementFactor();
        double var5 = par1Entity.posX * moveFactor;
        double var7 = par1Entity.posZ * moveFactor;
        double var11 = par1Entity.posX;
        double var13 = par1Entity.posY;
        double var15 = par1Entity.posZ;
        float var17 = par1Entity.rotationYaw;
        par3WorldServer.theProfiler.startSection("moving");

        if (par1Entity.dimension == 1)
        {
            ChunkCoordinates var18;

            if (par2 == 1)
            {
                var18 = par4WorldServer.getSpawnPoint();
            }
            else
            {
                var18 = par4WorldServer.getEntrancePortalLocation();
            }

            var5 = var18.posX;
            par1Entity.posY = var18.posY;
            var7 = var18.posZ;
            par1Entity.setLocationAndAngles(var5, par1Entity.posY, var7, 90.0F, 0.0F);

            if (par1Entity.isEntityAlive())
            {
                par3WorldServer.updateEntityWithOptionalForce(par1Entity, false);
            }
        }

        par3WorldServer.theProfiler.endSection();

        if (par2 != 1)
        {
            par3WorldServer.theProfiler.startSection("placing");
            var5 = MathHelper.clamp_int((int)var5, -29999872, 29999872);
            var7 = MathHelper.clamp_int((int)var7, -29999872, 29999872);

            if (par1Entity.isEntityAlive())
            {
                par4WorldServer.spawnEntityInWorld(par1Entity);
                par1Entity.setLocationAndAngles(var5, par1Entity.posY, var7, par1Entity.rotationYaw, par1Entity.rotationPitch);
                par4WorldServer.updateEntityWithOptionalForce(par1Entity, false);
                
                if (teleporter instanceof GCCoreTeleporter)
                {
                    ((GCCoreTeleporter) teleporter).placeInPortal(par1Entity, var11, var13, var15, var17);
                }
            }

            par3WorldServer.theProfiler.endSection();
        }

        par1Entity.setWorld(par4WorldServer);
        
        final int var9b = MathHelper.floor_double(par1Entity.posX);
        final int var11b = MathHelper.floor_double(par1Entity.posZ);
        
        GCCoreEntityParaChest chest = new GCCoreEntityParaChest(par1Entity.worldObj, this.rocketStacks);

    	chest.setPosition(var9b, 260, var11b);
    	
        if (!par4WorldServer.isRemote)
        {
        	par4WorldServer.spawnEntityInWorld(chest);
        }
    }
    
    public void sendAirRemainingPacket()
    {
    	final Object[] toSend = {this.airRemaining, this.airRemaining2, this.currentPlayer.username};
    	
    	if (FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(this.currentPlayer.username) != null)
    	{
            FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(this.currentPlayer.username).playerNetServerHandler.sendPacketToPlayer(GCCoreUtil.createPacket("Galacticraft", 0, toSend));
    	}
    }
    
    public void setInPortal(int par1)
    {
        if (this.currentPlayer.timeUntilPortal > 0)
        {
            this.currentPlayer.timeUntilPortal = 10;
        }
        else
        {
        	this.dimensionToSend = par1;
            this.inPortal = true;
        }
    }

    public void addExperience(int par1)
    {
        int var2 = Integer.MAX_VALUE - this.astronomyPointsTotal;

        if (par1 > var2)
        {
            par1 = var2;
        }
        
        par1 /= 10;

        this.astronomyPoints += (float)par1 / (float)this.astrBarCap();

        for (this.astronomyPointsTotal += par1; this.astronomyPoints >= 1.0F; this.astronomyPoints /= this.astrBarCap())
        {
            this.astronomyPoints = (this.astronomyPoints - 1.0F) * this.astrBarCap();
            this.addAstronomyLevel(1);
        }
    }

    public void addAstronomyLevel(int par1)
    {
        this.astronomyPointsLevel += par1;

        if (this.astronomyPointsLevel < 0)
        {
            this.astronomyPointsLevel = 0;
            this.astronomyPoints = 0.0F;
            this.astronomyPointsTotal = 0;
        }
    }

    public int astrBarCap()
    {
        return this.astronomyPointsLevel >= 30 ? 62 + (this.astronomyPointsLevel - 30) * 7 : (this.astronomyPointsLevel >= 15 ? 17 + (this.astronomyPointsLevel - 15) * 3 : 17);
    }
	
    public void readEntityFromNBT()
    {
    	final NBTTagCompound par1NBTTagCompound = this.currentPlayer.getEntityData();
		this.airRemaining = par1NBTTagCompound.getInteger("playerAirRemaining");
		this.damageCounter = par1NBTTagCompound.getInteger("damageCounter");
        final NBTTagList var2 = par1NBTTagCompound.getTagList("InventoryTankRefill");
        this.playerTankInventory.readFromNBT2(var2);
        this.astronomyPoints = par1NBTTagCompound.getFloat("AstronomyPointsNum");
        this.astronomyPointsLevel = par1NBTTagCompound.getInteger("AstronomyPointsLevel");
        this.astronomyPointsTotal = par1NBTTagCompound.getInteger("AstronomyPointsTotal");
        this.setParachute(par1NBTTagCompound.getBoolean("usingParachute"));
    }

    public void writeEntityToNBT()
    {
    	final NBTTagCompound par1NBTTagCompound = this.currentPlayer.getEntityData();
    	par1NBTTagCompound.setInteger("playerAirRemaining", this.airRemaining);
    	par1NBTTagCompound.setInteger("damageCounter", this.damageCounter);
        par1NBTTagCompound.setTag("InventoryTankRefill", this.playerTankInventory.writeToNBT2(new NBTTagList()));
        par1NBTTagCompound.setFloat("AstronomyPointsNum", this.astronomyPoints);
        par1NBTTagCompound.setInteger("AstronomyPointsLevel", this.astronomyPointsLevel);
        par1NBTTagCompound.setInteger("AstronomyPointsTotal", this.astronomyPointsTotal);
        par1NBTTagCompound.setBoolean("usingParachute", this.getParachute());
    }
    
    public void setParachute(boolean tf)
    {
    	this.getPlayer().getDataWatcher().updateObject(23, tf ? 1 : 0);
    }
    
    public boolean getParachute()
    {
    	return this.getPlayer().getDataWatcher().getWatchableObjectInt(23) == 1;
    }
}

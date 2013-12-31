package com.dmillerw.remoteIO.item;

import java.util.List;

import net.minecraft.block.BlockHalfSlab;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import com.dmillerw.remoteIO.block.BlockHandler;
import com.dmillerw.remoteIO.core.CreativeTabRIO;
import com.dmillerw.remoteIO.core.helper.ChatHelper;
import com.dmillerw.remoteIO.item.ItemUpgrade.Upgrade;
import com.dmillerw.remoteIO.lib.ModInfo;

public class ItemTransmitter extends Item {

	public static boolean hasSelfRemote(EntityPlayer player) {
		for (ItemStack stack : player.inventory.mainInventory) {
			if (stack != null) {
				if (stack.getItem() == ItemHandler.itemTransmitter) {
					if (stack.hasTagCompound() && (stack.getTagCompound().hasKey("player") && stack.getTagCompound().getString("player").equals(player.username))) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private Icon icon;
	
	public ItemTransmitter(int id) {
		super(id);
		
		setMaxStackSize(1);
		setCreativeTab(CreativeTabRIO.tab);
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (world.isRemote) {
			return stack;
		}
		
		if (!player.isSneaking()) {
			return stack;
		}
		
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		
		NBTTagCompound nbt = stack.getTagCompound();
		nbt.setString("player", player.username);
		stack.setTagCompound(nbt);
		
		ChatHelper.info(player, "chat.transceiverLink");
		
		return stack;
	}
	
	@Override
	public boolean shouldPassSneakingClickToBlock(World world, int x, int y, int z) {
		return world.getBlockId(x, y, z) == BlockHandler.blockWirelessID;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean idk) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("player")) {
			list.add("Owner: " + stack.getTagCompound().getString("player"));
		}
	}
	
	@Override
	public Icon getIconFromDamage(int damage) {
		return this.icon;
	}
	
	@Override
	public void registerIcons(IconRegister register) {
		this.icon = register.registerIcon(ModInfo.RESOURCE_PREFIX + "itemTransmitter");
	}
	
}

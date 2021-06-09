package com.hamusuke.flycommod.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemFlyingStick extends Item {
	public ItemFlyingStick() {
		super(new Item.Settings().group(ItemGroup.TOOLS).maxCount(1));
	}

	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(new TranslatableText(this.getTranslationKey() + ".desc"));
		super.appendTooltip(stack, world, tooltip, context);
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity playerIn, Hand handIn) {
		ItemStack item = playerIn.getStackInHand(handIn);
		if (!playerIn.getAbilities().allowFlying) {
			playerIn.getAbilities().allowFlying = true;
			playerIn.sendAbilitiesUpdate();
		} else {
			playerIn.getAbilities().allowFlying = false;
			playerIn.getAbilities().flying = false;
			playerIn.sendAbilitiesUpdate();
			playerIn.fallDistance = -(float) (playerIn.getY() + 10.0D);
		}
		return TypedActionResult.success(item);
	}

	public Rarity getRarity(ItemStack stack) {
		return Rarity.EPIC;
	}

	public boolean hasGlint(ItemStack stack) {
		return true;
	}
}

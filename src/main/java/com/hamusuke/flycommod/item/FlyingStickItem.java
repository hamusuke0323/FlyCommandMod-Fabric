package com.hamusuke.flycommod.item;

import com.hamusuke.flycommod.invoker.LivingEntityInvoker;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FlyingStickItem extends Item {
	public FlyingStickItem() {
		super(new FabricItemSettings().recipeRemainder(Items.ELYTRA).equipmentSlot(stack -> EquipmentSlot.MAINHAND).fireproof().maxCount(1).rarity(Rarity.EPIC));
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.translatable(this.getTranslationKey() + ".desc"));
		super.appendTooltip(stack, world, tooltip, context);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity playerIn, Hand handIn) {
		ItemStack item = playerIn.getStackInHand(handIn);
		if (!playerIn.getAbilities().allowFlying) {
			playerIn.getAbilities().allowFlying = true;
			playerIn.sendAbilitiesUpdate();
		} else {
			playerIn.getAbilities().allowFlying = false;
			playerIn.getAbilities().flying = false;
			playerIn.sendAbilitiesUpdate();
			((LivingEntityInvoker) playerIn).markNoFallDamage(!playerIn.isOnGround());
		}
		return TypedActionResult.success(item);
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return true;
	}
}

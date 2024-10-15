package com.hamusuke.flycommod.item;

import com.hamusuke.flycommod.invoker.LivingEntityInvoker;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FlyingStickItem extends Item {
	public FlyingStickItem() {
		super(new Item.Settings().equipmentSlot((entity, stack) -> EquipmentSlot.MAINHAND).fireproof().rarity(Rarity.EPIC).recipeRemainder(Items.ELYTRA).maxCount(1));
	}


	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.translatable(this.getTranslationKey() + ".desc"));
		super.appendTooltip(stack, context, tooltip, type);
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

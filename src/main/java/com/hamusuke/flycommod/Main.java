package com.hamusuke.flycommod;

import com.hamusuke.flycommod.command.CommandEntityAbilities;
import com.hamusuke.flycommod.command.CommandFlying;
import com.hamusuke.flycommod.item.ItemFlyingStick;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Main implements ModInitializer {
	public static final String MOD_ID = "flycommand";

	public static final Item FLYING_STICK = new ItemFlyingStick();

	public void onInitialize() {
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "flying_stick"), FLYING_STICK);

		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			CommandFlying.register(dispatcher);
			CommandEntityAbilities.register(dispatcher);
		});
	}
}

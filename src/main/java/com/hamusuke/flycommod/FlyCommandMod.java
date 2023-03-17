package com.hamusuke.flycommod;

import com.hamusuke.flycommod.command.EntityAbilitiesCommand;
import com.hamusuke.flycommod.command.FlyCommand;
import com.hamusuke.flycommod.item.FlyingStickItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FlyCommandMod implements ModInitializer {
	public static final String MOD_ID = "flycommand";
	public static final Item FLYING_STICK = Registry.register(Registries.ITEM, new Identifier(MOD_ID, "flying_stick"), new FlyingStickItem());

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, registrationEnvironment) -> {
			FlyCommand.register(dispatcher);
			EntityAbilitiesCommand.register(dispatcher);
		});

		ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> player.sendAbilitiesUpdate());
	}
}

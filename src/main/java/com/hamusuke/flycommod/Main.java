package com.hamusuke.flycommod;

import com.hamusuke.flycommod.command.CommandEntityAbilities;
import com.hamusuke.flycommod.command.CommandFlying;
import com.hamusuke.flycommod.invoker.LivingEntityInvoker;
import com.hamusuke.flycommod.item.ItemFlyingStick;
import com.hamusuke.flycommod.network.NetworkManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
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

		ServerPlayNetworking.registerGlobalReceiver(NetworkManager.REQUEST_SYNC_NO_FALL_DAMAGE_MARKED_PACKET_ID, (server, player, handler, buf, responseSender) -> {
			((LivingEntityInvoker) player).sendMarkNoFallDamageS2CPacket();
		});
	}
}

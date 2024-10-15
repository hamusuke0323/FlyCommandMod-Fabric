package com.hamusuke.flycommod.client;

import com.hamusuke.flycommod.FlyCommandMod;
import com.hamusuke.flycommod.invoker.LivingEntityInvoker;
import com.hamusuke.flycommod.network.MarkNoFallDamagePacket;
import com.hamusuke.flycommod.network.NetworkManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemGroups;

@Environment(EnvType.CLIENT)
public class FlyCommandModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(FlyCommandMod.FLYING_STICK));
        PayloadTypeRegistry.playS2C().register(MarkNoFallDamagePacket.PACKET_ID, MarkNoFallDamagePacket.PACKET_CODEC);
        ClientPlayNetworking.registerGlobalReceiver(MarkNoFallDamagePacket.PACKET_ID, ((payload, context) -> {
            if (context.client().world != null) {
                MarkNoFallDamagePacket packet = new MarkNoFallDamagePacket(payload);
                Entity entity = context.client().world.getEntityById(packet.getEntityId());
                if (entity instanceof LivingEntityInvoker invoker) {
                    invoker.markNoFallDamage(packet.getMarkFlag());
                }
            }
        }));
    }
}

package com.hamusuke.flycommod.client;

import com.hamusuke.flycommod.invoker.LivingEntityInvoker;
import com.hamusuke.flycommod.network.NetworkManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;

@Environment(EnvType.CLIENT)
public class FlyCommandModClient implements ClientModInitializer {
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            handler.getConnection().send(new CustomPayloadC2SPacket(NetworkManager.REQUEST_SYNC_NO_FALL_DAMAGE_MARKED_PACKET_ID, PacketByteBufs.empty()));
        });

        ClientPlayNetworking.registerGlobalReceiver(NetworkManager.MARK_NO_FALL_DAMAGE_S2C_PACKET_ID, (client, handler, buf, responseSender) -> {
            if (client.player != null) {
                ((LivingEntityInvoker) client.player).markNoFallDamage(buf.readBoolean());
            }
        });
    }
}

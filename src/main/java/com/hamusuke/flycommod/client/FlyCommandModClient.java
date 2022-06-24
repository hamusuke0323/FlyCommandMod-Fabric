package com.hamusuke.flycommod.client;

import com.hamusuke.flycommod.invoker.LivingEntityInvoker;
import com.hamusuke.flycommod.network.MarkNoFallDamagePacket;
import com.hamusuke.flycommod.network.NetworkManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;

@Environment(EnvType.CLIENT)
public class FlyCommandModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(NetworkManager.NO_FALL_MARK_PACKET, (client, handler, buf, responseSender) -> {
            if (client.world != null) {
                MarkNoFallDamagePacket packet = new MarkNoFallDamagePacket(buf);
                Entity entity = client.world.getEntityById(packet.getEntityId());
                if (entity instanceof LivingEntityInvoker invoker) {
                    invoker.markNoFallDamage(packet.getMarkFlag());
                }
            }
        });
    }
}

package com.hamusuke.flycommod.client;

import com.hamusuke.flycommod.invoker.LivingEntityInvoker;
import com.hamusuke.flycommod.network.NetworkManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Environment(EnvType.CLIENT)
public class MainClient implements ClientModInitializer {
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(NetworkManager.MARK_NO_FALL_DAMAGE_S2C_PACKET_ID, (client, handler, buf, responseSender) -> {
            if (client.player != null) {
                ((LivingEntityInvoker) client.player).markNoFallDamage(buf.readBoolean());
            }
        });
    }
}

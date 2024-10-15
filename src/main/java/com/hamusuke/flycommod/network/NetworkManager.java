package com.hamusuke.flycommod.network;

import com.hamusuke.flycommod.FlyCommandMod;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;

import java.util.UUID;

public final class NetworkManager {
    public static final Identifier NO_FALL_MARK_PACKET = Identifier.of(FlyCommandMod.MOD_ID, "no_fall_mark_packet");

}

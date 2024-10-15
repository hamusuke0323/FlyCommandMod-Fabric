package com.hamusuke.flycommod.network;

import com.hamusuke.flycommod.FlyCommandMod;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class MarkNoFallDamagePacket implements CustomPayload {
    private final int entityId;
    private final boolean flag;

    public MarkNoFallDamagePacket(int entityId, boolean flag) {
        this.entityId = entityId;
        this.flag = flag;
    }

    public MarkNoFallDamagePacket(PacketByteBuf byteBuf) {
        this(byteBuf.readVarInt(), byteBuf.readBoolean());
    }

    public MarkNoFallDamagePacket(MarkNoFallDamagePacket payload) {
        this.entityId = payload.entityId;
        this.flag = payload.flag;
    }

    public PacketByteBuf write(PacketByteBuf byteBuf) {
        byteBuf.writeVarInt(this.entityId).writeBoolean(this.flag);
        return byteBuf;
    }

    // Static method, buffer-first
    public static void write2(PacketByteBuf byteBuf, MarkNoFallDamagePacket packet) {
        byteBuf.writeVarInt(packet.entityId).writeBoolean(packet.flag);
    }

    public int getEntityId() {
        return this.entityId;
    }

    public boolean getMarkFlag() {
        return this.flag;
    }

    public static final CustomPayload.Id<MarkNoFallDamagePacket> PACKET_ID = new CustomPayload.Id<>(Identifier.of(FlyCommandMod.MOD_ID, "no_fall_mark_packet"));
    public static final PacketCodec<PacketByteBuf, MarkNoFallDamagePacket> PACKET_CODEC = PacketCodec.ofStatic(MarkNoFallDamagePacket::write2, MarkNoFallDamagePacket::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}

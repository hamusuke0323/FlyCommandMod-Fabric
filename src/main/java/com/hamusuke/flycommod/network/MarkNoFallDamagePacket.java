package com.hamusuke.flycommod.network;

import net.minecraft.network.PacketByteBuf;

public class MarkNoFallDamagePacket {
    private final int entityId;
    private final boolean flag;

    public MarkNoFallDamagePacket(int entityId, boolean flag) {
        this.entityId = entityId;
        this.flag = flag;
    }

    public MarkNoFallDamagePacket(PacketByteBuf byteBuf) {
        this(byteBuf.readVarInt(), byteBuf.readBoolean());
    }

    public PacketByteBuf write(PacketByteBuf byteBuf) {
        byteBuf.writeVarInt(this.entityId).writeBoolean(this.flag);
        return byteBuf;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public boolean getMarkFlag() {
        return this.flag;
    }
}

package com.hamusuke.flycommod.network;

import com.hamusuke.flycommod.Main;
import net.minecraft.util.Identifier;

public class NetworkManager {
    public static final Identifier MARK_NO_FALL_DAMAGE_S2C_PACKET_ID = new Identifier(Main.MOD_ID, "mark_no_fall_damage_s2c_packet");
    public static final Identifier REQUEST_SYNC_NO_FALL_DAMAGE_MARKED_PACKET_ID = new Identifier(Main.MOD_ID, "request_sync_no_fall_damage_marked_packet");
}

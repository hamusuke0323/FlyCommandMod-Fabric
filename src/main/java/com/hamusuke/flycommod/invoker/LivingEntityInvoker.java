package com.hamusuke.flycommod.invoker;

public interface LivingEntityInvoker {
    void markNoFallDamage(boolean flag);

    void sendMarkNoFallDamageS2CPacket();

    boolean isNoFallDamageMarked();
}

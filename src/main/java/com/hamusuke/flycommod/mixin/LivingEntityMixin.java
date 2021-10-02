package com.hamusuke.flycommod.mixin;

import com.hamusuke.flycommod.invoker.LivingEntityInvoker;
import com.hamusuke.flycommod.network.NetworkManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements LivingEntityInvoker {
    protected boolean noFallDamageMarked;

    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    public void handleFallDamage(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Boolean> cir) {
        if (this.noFallDamageMarked) {
            this.markNoFallDamage(false);
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void writeNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("noFallDamageMarked", this.noFallDamageMarked);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readNbt(NbtCompound nbt, CallbackInfo ci) {
        this.markNoFallDamage(nbt.getBoolean("noFallDamageMarked"));
    }

    public void markNoFallDamage(boolean flag) {
        this.noFallDamageMarked = flag;
        this.sendMarkNoFallDamageS2CPacket();
    }

    public void sendMarkNoFallDamageS2CPacket() {
        if ((LivingEntity) (Object) this instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) (Object) this;
            if (serverPlayerEntity.networkHandler != null) {
                PacketByteBuf packetByteBuf = PacketByteBufs.create();
                packetByteBuf.writeBoolean(this.noFallDamageMarked);
                serverPlayerEntity.networkHandler.sendPacket(new CustomPayloadS2CPacket(NetworkManager.MARK_NO_FALL_DAMAGE_S2C_PACKET_ID, packetByteBuf));
            }
        }
    }

    public boolean isNoFallDamageMarked() {
        return this.noFallDamageMarked;
    }
}

package com.hamusuke.flycommod.mixin;

import com.hamusuke.flycommod.invoker.LivingEntityInvoker;
import com.hamusuke.flycommod.network.MarkNoFallDamagePacket;
import com.hamusuke.flycommod.network.NetworkManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityInvoker {
    private boolean isNoFallDamageMarked;

    LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    public void handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (this.isNoFallDamageMarked()) {
            this.markNoFallDamage(false);
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void writeNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("noFallDamageMarked", this.isNoFallDamageMarked());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readNbt(NbtCompound nbt, CallbackInfo ci) {
        this.markNoFallDamage(nbt.getBoolean("noFallDamageMarked"));
    }

    @Override
    public void markNoFallDamage(boolean flag) {
        if (!this.world.isClient) {
            PacketByteBuf byteBuf = new MarkNoFallDamagePacket(this.getId(), flag).write(PacketByteBufs.create());
            this.world.getServer().getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> serverPlayerEntity.networkHandler.sendPacket(new CustomPayloadS2CPacket(NetworkManager.NO_FALL_MARK_PACKET, byteBuf)));
        }

        this.isNoFallDamageMarked = flag;
    }

    @Override
    public boolean isNoFallDamageMarked() {
        return this.isNoFallDamageMarked;
    }
}

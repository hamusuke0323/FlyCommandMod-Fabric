package com.hamusuke.flycommod.mixin;

import com.hamusuke.flycommod.invoker.PlayerAbilitiesInvoker;
import net.minecraft.entity.player.PlayerAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerAbilities.class)
public class PlayerAbilitiesMixin implements PlayerAbilitiesInvoker {
    @Shadow
    private float flySpeed;

    @Shadow
    private float walkSpeed;

    public void setFlySpeed(float flySpeed) {
        this.flySpeed = flySpeed;
    }

    public void setWalkSpeed(float walkSpeed) {
        this.walkSpeed = walkSpeed;
    }
}

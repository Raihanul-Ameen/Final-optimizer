package com.example.mixin; // <-- KEEP WHATEVER YOUR TEMPLATE ALREADY HAS HERE!

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class ExampleMixin {

    @Shadow public ClientPlayerInteractionManager interactionManager;
    @Shadow private int itemUseCooldown;

    @Inject(at = @At("HEAD"), method = "tick")
    private void onTick(CallbackInfo ci) {
        MinecraftClient client = (MinecraftClient) (Object) this;
        
        // 1. PURE PLACEMENT OPTIMIZER (Triggers when YOU hold right-click)
        if (client.options.useKey.isPressed() && client.player != null) {
            // Check if you are holding a crystal in either hand
            if (client.player.getStackInHand(Hand.MAIN_HAND).isOf(Items.END_CRYSTAL) || 
                client.player.getStackInHand(Hand.OFF_HAND).isOf(Items.END_CRYSTAL)) {
                
                // Zero out the hardcoded 4-tick right-click delay instantly
                this.itemUseCooldown = 0;
            }
        }

        // 2. GHOST CRYSTAL REMOVER (Cleans client lag when YOU break a crystal)
        if (client.targetedEntity instanceof EndCrystalEntity crystal) {
            // If you have already broken it, clear the dead model immediately
            if (!crystal.isAlive() || crystal.getHealth() <= 0) {
                crystal.discard();
            }
        }
    }
}

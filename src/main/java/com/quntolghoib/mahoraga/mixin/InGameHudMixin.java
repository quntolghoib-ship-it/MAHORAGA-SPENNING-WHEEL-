
package com.quntolghoib.mahoraga.mixin;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow
    private int overlayRemaining;

    @Unique
    private long mahoraga$startTime = -1L;

    @Inject(
        method = "renderOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;F)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void mahoraga$animateWheel(
        DrawContext context,
        Identifier texture,
        float opacity,
        CallbackInfo ci
    ) {
        Identifier totemTexture = Identifier.of(
            "minecraft",
            "textures/misc/totem_of_undying.png"
        );

        if (!texture.equals(totemTexture)) {
            return;
        }

        if (mahoraga$startTime < 0L) {
            mahoraga$startTime = System.currentTimeMillis();
        }

        long elapsed = System.currentTimeMillis() - mahoraga$startTime;

        float progress = Math.min(elapsed / 1800.0F, 1.0F);

        int frame = (int) (
            21.0F * (1.0F - (float) Math.pow(1.0F - progress, 3))
        );

        frame = Math.min(frame, 21);

        int size = 128;

        int x = (context.getScaledWindowWidth() - size) / 2;
        int y = (context.getScaledWindowHeight() - size) / 2;

        int alpha = Math.max(
            0,
            Math.min(255, Math.round(opacity * 255.0F))
        );

        Identifier wheelTexture = Identifier.of(
            "mahoraga_wheel",
            "textures/gui/mahoraga_wheel.png"
        );

        context.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            wheelTexture,
            x,
            y,
            0.0F,
            frame * 128.0F,
            128,
            128,
            128,
            2816,
            (alpha << 24) | 0xFFFFFF
        );

        ci.cancel();
    }

    @Inject(method = "tick()V", at = @At("TAIL"))
    private void mahoraga$resetAnimation(CallbackInfo ci) {
        if (overlayRemaining <= 0) {
            mahoraga$startTime = -1L;
        }
    }
}

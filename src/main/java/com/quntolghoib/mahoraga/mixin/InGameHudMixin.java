package com.quntolghoib.mahoraga.mixin;

import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import net.minecraft.client.gui.hud.InGameHud;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @ModifyVariable(
        method = "renderOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;F)V",
        at = @At("HEAD"),
        argsOnly = true,
        ordinal = 0
    )
    private Identifier mahoraga$replaceTotemTexture(Identifier texture) {

        if (texture.getPath().equals("textures/misc/totem_of_undying.png")) {
            return Identifier.of(
                "mahoraga_wheel",
                "textures/gui/mahoraga_wheel.png"
            );
        }

        return texture;
    }
}

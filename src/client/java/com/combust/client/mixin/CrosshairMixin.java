package com.combust.client.mixin;

import com.combust.client.CombustClient;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class CrosshairMixin {
    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void renderCrosshair(GuiGraphics context, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (CombustClient.combatCrosshairEnabled) {
            ci.cancel(); // cancels the vanilla crosshair

            // draw your own crosshair here
            int x = context.guiWidth() / 2;
            int y = context.guiHeight() / 2;
            int color = 0xFFFFFFAA;
            int size = 4;

            Minecraft mc = Minecraft.getInstance();
            boolean enemyInReach = mc.crosshairPickEntity != null;

            if (enemyInReach) {
                for (int i = -size; i <= size; i++) {
                    // \ diagonal
                    context.fill(x + i, y + i, x + i + 1, y + i + 1, color);
                    // / diagonal
                    context.fill(x + i, y - i, x + i + 1, y - i + 1, color);
                }
            } else {
                context.fill(x - size, y, x + size + 1, y + 1, color); // horizontal
                context.fill(x, y - size, x + 1, y + size + 1, color); // vertical
            }
        }
    }
}
package com.combust.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class HudScreen extends Screen {
    public HudScreen(Component title) {
        super(title);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        CombustClient.panel.render(context, this.font, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean consumed) {
        if (!consumed && event.button() == 0) {
            CombustClient.panel.handleClick((int) event.x(), (int) event.y());
        }
        return super.mouseClicked(event, consumed);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
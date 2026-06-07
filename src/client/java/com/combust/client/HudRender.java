package com.combust.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class HudRender {
    private final List<CategoryButton> categories = new ArrayList<>();
    private static final int CAT_X = 10;
    private static final int CAT_Y = 10;
    private static final int CAT_W = 80;
    private static final int CAT_H = 20;
    private static final int CAT_SPACING = 25;
    private static final int PANEL_W = 110;
    private static final int PANEL_H = 20;
    private static final int PANEL_SPACING = 25;

    public HudRender() {
        CategoryButton combat = new CategoryButton("Combat", 0);
        combat.addButton("Combat Crosshair", () -> CombustClient.combatCrosshairEnabled, v -> CombustClient.combatCrosshairEnabled = v);
        combat.addButton("Fly", () -> CombustClient.speedEnabled, v -> CombustClient.speedEnabled = v);
        categories.add(combat);

        CategoryButton movement = new CategoryButton("Movement", 1);
        movement.addButton("Speed", () -> CombustClient.speedEnabled, v -> CombustClient.speedEnabled = v);
        categories.add(movement);
    }

    public void render(GuiGraphics context, Font font, int mouseX, int mouseY) {
        // first pass: figure out panel x positions based on order of open categories
        int nextPanelX = CAT_X + CAT_W + 5;
        for (CategoryButton cat : categories) {
            if (cat.open) {
                cat.panelX = nextPanelX;
                nextPanelX += PANEL_W + 5;
            }
        }

        // second pass: render
        for (int i = 0; i < categories.size(); i++) {
            int catY = CAT_Y + i * CAT_SPACING;
            categories.get(i).render(context, font, mouseX, mouseY, CAT_X, catY, CAT_W, CAT_H, PANEL_W, PANEL_H, PANEL_SPACING);
        }
    }

    public void handleClick(int mouseX, int mouseY) {
        for (int i = 0; i < categories.size(); i++) {
            int catY = CAT_Y + i * CAT_SPACING;
            categories.get(i).handleClick(mouseX, mouseY, CAT_X, catY, CAT_W, CAT_H, PANEL_W, PANEL_H, PANEL_SPACING);
        }
    }

    private static class CategoryButton {
        String label;
        int order;
        boolean open = false;
        int panelX = 0;
        List<Button> buttons = new ArrayList<>();

        CategoryButton(String label, int order) {
            this.label = label;
            this.order = order;
        }

        void addButton(String label, Supplier<Boolean> getter, Consumer<Boolean> setter) {
            buttons.add(new Button(label, getter, setter));
        }

        void render(GuiGraphics context, Font font, int mouseX, int mouseY,
                    int catX, int catY, int catW, int catH,
                    int panelW, int panelH, int panelSpacing) {
            boolean hovered = mouseX >= catX && mouseX <= catX + catW && mouseY >= catY && mouseY <= catY + catH;
            context.fill(catX, catY, catX + catW, catY + catH, hovered ? 0xFF555555 : 0xFF333333);
            context.drawString(font, (open ? "▼ " : "▶ ") + label, catX + 4, catY + (catH - 8) / 2, 0xFFFFFFFF, false);

            if (open) {
                for (int i = 0; i < buttons.size(); i++) {
                    int panelY = CAT_Y + i * panelSpacing;
                    buttons.get(i).render(context, font, mouseX, mouseY, panelX, panelY, panelW, panelH);
                }
            }
        }

        void handleClick(int mouseX, int mouseY,
                         int catX, int catY, int catW, int catH,
                         int panelW, int panelH, int panelSpacing) {
            if (mouseX >= catX && mouseX <= catX + catW && mouseY >= catY && mouseY <= catY + catH) {
                open = !open;
                return;
            }
            if (open) {
                for (int i = 0; i < buttons.size(); i++) {
                    int panelY = CAT_Y + i * panelSpacing;
                    buttons.get(i).handleClick(mouseX, mouseY, panelX, panelY, panelW, panelH);
                }
            }
        }
    }

    private static class Button {
        String label;
        Supplier<Boolean> getter;
        Consumer<Boolean> setter;

        Button(String label, Supplier<Boolean> getter, Consumer<Boolean> setter) {
            this.label = label;
            this.getter = getter;
            this.setter = setter;
        }

        void render(GuiGraphics context, Font font, int mouseX, int mouseY, int x, int y, int w, int h) {
            boolean active = getter.get();
            boolean hovered = mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
            context.fill(x, y, x + w, y + h, hovered ? 0xFF555555 : 0xFF333333);
            int textColor = active ? 0xFF00FF00 : 0xFFAAAAAA;
            context.drawString(font, label, x + 4, y + (h - 8) / 2, textColor, false);
        }

        void handleClick(int mouseX, int mouseY, int x, int y, int w, int h) {
            if (mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h) {
                setter.accept(!getter.get());
            }
        }
    }
}
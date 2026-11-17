package com.meis.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/** Simple settings menu (Right Shift) to switch each feature on or off. */
public class MeisScreen extends Screen {
    public MeisScreen() { super(Text.literal("meis client")); }

    private void toggle(int y, String name, BooleanSupplier get, Consumer<Boolean> set) {
        addDrawableChild(ButtonWidget.builder(label(name, get.getAsBoolean()), b -> {
            set.accept(!get.getAsBoolean());
            b.setMessage(label(name, get.getAsBoolean()));
        }).dimensions(width / 2 - 100, y, 200, 20).build());
    }

    private static Text label(String name, boolean on) {
        return Text.literal(name + ": " + (on ? "ON" : "OFF"));
    }

    @Override
    protected void init() {
        int y = height / 2 - 70;
        toggle(y, "HUD (FPS)", () -> MeisClient.hud, v -> MeisClient.hud = v); y += 24;
        toggle(y, "Coordinates", () -> MeisClient.coords, v -> MeisClient.coords = v); y += 24;
        toggle(y, "Toggle sprint", () -> MeisClient.sprint, v -> MeisClient.sprint = v); y += 24;
        toggle(y, "Fullbright (G)", () -> MeisClient.fullbright, v -> MeisClient.fullbright = v); y += 24;
        toggle(y, "Hitboxes (H)", () -> MeisClient.hitboxes, v -> MeisClient.hitboxes = v); y += 30;
        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), b -> close())
                .dimensions(width / 2 - 100, y, 200, 20).build());
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        ctx.drawCenteredTextWithShadow(textRenderer, "meis client  -  hold C to zoom", width / 2, height / 2 - 95, 0xFFFF7A3D);
    }
}

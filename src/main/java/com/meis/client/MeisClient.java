package com.meis.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.lwjgl.glfw.GLFW;

public class MeisClient implements ClientModInitializer {
    public static boolean hud = true, coords = true, sprint = true, fullbright = false, hitboxes = false;
    public static boolean zooming = false;
    private static boolean nvApplied = false;
    private static KeyBinding menuKey, zoomKey, fullbrightKey, hitboxKey;
    private static final String CAT = "category.meisclient";

    @Override
    public void onInitializeClient() {
        menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.meisclient.menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, CAT));
        zoomKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.meisclient.zoom", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C, CAT));
        fullbrightKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.meisclient.fullbright", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, CAT));
        hitboxKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.meisclient.hitboxes", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_H, CAT));

        ClientTickEvents.END_CLIENT_TICK.register(c -> {
            zooming = zoomKey.isPressed();
            while (menuKey.wasPressed()) c.setScreen(new MeisScreen());
            while (fullbrightKey.wasPressed()) fullbright = !fullbright;
            while (hitboxKey.wasPressed()) hitboxes = !hitboxes;
            if (c.getEntityRenderDispatcher() != null) c.getEntityRenderDispatcher().setRenderHitboxes(hitboxes);

            ClientPlayerEntity p = c.player;
            if (p == null) { nvApplied = false; return; }

            // Fullbright via a client-side night vision effect (no gamma hacks).
            if (fullbright) {
                p.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 400, 0, false, false));
                nvApplied = true;
            } else if (nvApplied) {
                p.removeStatusEffect(StatusEffects.NIGHT_VISION);
                nvApplied = false;
            }

            // Toggle sprint: keep sprinting while walking forward.
            if (sprint && c.options.forwardKey.isPressed() && !p.isSneaking()
                    && !p.horizontalCollision && p.getHungerManager().getFoodLevel() > 6) {
                p.setSprinting(true);
            }
        });

        HudRenderCallback.EVENT.register((ctx, tickCounter) -> {
            MinecraftClient c = MinecraftClient.getInstance();
            if (!hud || c.player == null || c.options.hudHidden || c.getDebugHud().shouldShowDebugHud()) return;
            TextRenderer tr = c.textRenderer;
            int y = 4, color = 0xFFFF7A3D;
            ctx.drawTextWithShadow(tr, "FPS: " + c.getCurrentFps(), 4, y, color); y += 10;
            if (coords) {
                ctx.drawTextWithShadow(tr, String.format("XYZ: %.0f %.0f %.0f", c.player.getX(), c.player.getY(), c.player.getZ()), 4, y, 0xFFFFFFFF); y += 10;
            }
            if (sprint) { ctx.drawTextWithShadow(tr, "[Sprint: on]", 4, y, 0xFFAAAAAA); y += 10; }
            if (fullbright) { ctx.drawTextWithShadow(tr, "[Fullbright]", 4, y, 0xFFAAAAAA); y += 10; }
            if (hitboxes) { ctx.drawTextWithShadow(tr, "[Hitboxes]", 4, y, 0xFFAAAAAA); }
        });
    }
}

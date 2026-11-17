package com.meis.client.mixin;

import com.meis.client.MeisClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    /** Zoom: divide the field of view while the zoom key is held. */
    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void meis$zoom(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Float> cir) {
        if (MeisClient.zooming) cir.setReturnValue(cir.getReturnValue() / 4.0f);
    }
}

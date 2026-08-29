package com.rasmus.clearsight;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import java.lang.reflect.Method;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.GameRenderState;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Util;
import org.joml.Vector4f;

/**
 * The last world frame, kept on the GPU and drawn over the server
 * reconfiguration screen so a proxy transfer looks like standing still in
 * the world instead of a menu background flash. Everything degrades to
 * vanilla: a failed capture just drops the frame, and the screen itself is
 * never replaced or closed, so its connection ticking and disconnect
 * button stay vanilla's problem.
 *
 * DynamicTexture does the allocation because its constructor picks the
 * color format internally: the format type was renamed and repackaged
 * between 26.1 and 26.2, and this is the one allocation path that never
 * names it. The two accessors that moved between those versions are
 * resolved by name once.
 */
public final class FrozenFrame {

    public static final Identifier ID = Identifier.fromNamespaceAndPath("clearsight", "frozen_frame");

    /**
     * After this long on the reconfiguration screen its vanilla text and
     * disconnect button render on top of the frozen frame, so a stuck
     * transfer is visible and escapable instead of a frozen world with no
     * explanation.
     */
    public static final long FOREGROUND_BUDGET_MS = 10_000L;

    private static DynamicTexture texture;
    private static int width;
    private static int height;
    private static volatile int backgroundColor = 0xFF000000;
    private static volatile long capturedAt;

    private static Method mainTarget;
    private static boolean mainTargetOnRenderer;
    private static Method renderState;

    private FrozenFrame() {
    }

    /** Render thread only: reads the main target and the render state. */
    public static void capture() {
        Minecraft minecraft = Minecraft.getInstance();
        try {
            RenderTarget target = resolveTarget(minecraft);
            GpuTexture source = target.getColorTexture();
            if (source == null) {
                return;
            }
            Vector4f fog = resolveRenderState(minecraft).levelRenderState.cameraRenderState.fogData.color;
            backgroundColor = ARGB.colorFromFloat(1.0F, fog.x, fog.y, fog.z);
            if (texture == null || texture.getTexture() == null || texture.getTexture().isClosed()
                    || width != target.width || height != target.height) {
                width = target.width;
                height = target.height;
                texture = new DynamicTexture("clearsight frozen frame", width, height, false);
                minecraft.getTextureManager().register(ID, texture);
            }
            RenderSystem.getDevice().createCommandEncoder()
                    .copyTextureToTexture(source, texture.getTexture(), 0, 0, 0, 0, 0, width, height);
            capturedAt = Util.getMillis();
        } catch (Exception e) {
            // A failure after register would otherwise orphan the texture
            // in the manager; full release is the only leak-free answer.
            release();
        }
    }

    /** A resource reload can close the registered texture under us. */
    public static boolean drawable() {
        return texture != null && texture.getTexture() != null && !texture.getTexture().isClosed();
    }

    public static boolean foregroundFresh() {
        return Util.getMillis() - capturedAt < FOREGROUND_BUDGET_MS;
    }

    public static int backgroundColor() {
        return backgroundColor;
    }

    /**
     * Stretch-fit; the negative source height flips the bottom-up
     * framebuffer. A window resize mid-transfer stretches for a moment,
     * which beats carrying gui-scale crop math.
     */
    public static void blit(GuiGraphicsExtractor graphics, int screenWidth, int screenHeight) {
        graphics.blit(RenderPipelines.GUI_OPAQUE_TEXTURED_BACKGROUND, ID,
                0, 0, 0.0F, (float) height, screenWidth, screenHeight, width, -height, width, height);
    }

    // Unconditional: vanilla's release null-guards an absent id, and the
    // local field can be null while the manager still holds a registration
    // from a capture that failed halfway.
    public static void release() {
        texture = null;
        Minecraft.getInstance().getTextureManager().release(ID);
    }

    private static RenderTarget resolveTarget(Minecraft minecraft) throws ReflectiveOperationException {
        if (mainTarget == null) {
            try {
                mainTarget = Minecraft.class.getMethod("getMainRenderTarget");
                mainTargetOnRenderer = false;
            } catch (NoSuchMethodException e) {
                mainTarget = GameRenderer.class.getMethod("mainRenderTarget");
                mainTargetOnRenderer = true;
            }
        }
        return (RenderTarget) mainTarget.invoke(mainTargetOnRenderer ? minecraft.gameRenderer : minecraft);
    }

    private static GameRenderState resolveRenderState(Minecraft minecraft) throws ReflectiveOperationException {
        if (renderState == null) {
            try {
                renderState = GameRenderer.class.getMethod("getGameRenderState");
            } catch (NoSuchMethodException e) {
                renderState = GameRenderer.class.getMethod("gameRenderState");
            }
        }
        return (GameRenderState) renderState.invoke(minecraft.gameRenderer);
    }
}

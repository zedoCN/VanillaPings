package com.vanillapings.features.ping;

import com.vanillapings.networking.PingNetworking;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.BlockPos;

@Environment(EnvType.CLIENT)
public class ClientPingManager {
    /**
     * Sends a packet to the server to ping in the direction the local player is facing in.
     * The server manages a cooldown for this action.
     */
    public static void pingInFrontOfPlayer() {
        var client = net.minecraft.client.Minecraft.getInstance();
        if (client.player == null || client.level == null) return;
        //? if >=26.2 {
        /*if (client.gui.screen() != null) return;*/
        //?} else {
        if (client.screen != null) return;
        //?}
        //? if >=1.20.5 {
        if (ClientPlayNetworking.canSend(PingNetworking.CameraPingPayload.ID)) {
            //? if >=26.2 {
            /*var camera = client.gameRenderer.mainCamera();
            ClientPlayNetworking.send(new PingNetworking.CameraPingPayload(camera.position(),
                    net.minecraft.world.phys.Vec3.directionFromRotation(camera.xRot(), camera.yRot())));*/
            //?} else {
            var camera = client.gameRenderer.getMainCamera();
            ClientPlayNetworking.send(new PingNetworking.CameraPingPayload(camera.getPosition(),
                    net.minecraft.world.phys.Vec3.directionFromRotation(camera.getXRot(), camera.getYRot())));
            //?}
            return;
        }
        // Upstream servers remain usable, but cannot know a detached camera's position.
        if (client.getCameraEntity() != client.player) {
            com.vanillapings.compat.Compat.sendActionBar(client.player, net.minecraft.network.chat.Component.translatable(
                    "vanillapings.ping.camera.server_required"));
            return;
        }
        if (!ClientPlayNetworking.canSend(PingNetworking.PingPayload.ID)) return;
        ClientPlayNetworking.send(new PingNetworking.PingPayload(BlockPos.ZERO));
        //?} else {
        /*ClientPlayNetworking.send(PingNetworking.ID_PING, net.fabricmc.fabric.api.networking.v1.PacketByteBufs.create());
        *///?}
    }
}

package com.vanillapings.networking;

import com.vanillapings.VanillaPings;
import com.vanillapings.compat.Compat;
import com.vanillapings.features.ping.PingManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
//? if >=1.20.5 {
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//?}

/**
 * Registration + payload for the ping trigger packet.
 *
 * <p>Three networking eras, all behind {@code //?}:
 * <ul>
 *   <li>{@code >=1.21}: CustomPacketPayload + {@code context.server()}</li>
 *   <li>{@code 1.20.5-1.20.6}: CustomPacketPayload + {@code context.player().getServer()}</li>
 *   <li>{@code <1.20.5}: legacy {@code RegistryFriendlyByteBuf} channel registration (no CustomPacketPayload)</li>
 * </ul>
 * The payload carries an (unused) {@link BlockPos}; the packet is just a "trigger a ping"
 * signal — the server raycasts from the sending player.
 */
public final class PingNetworking {
    private PingNetworking() {
    }

    //? if >=1.20.5 {
    public record PingPayload(BlockPos blockPos) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<PingPayload> ID =
                new CustomPacketPayload.Type<>(Compat.id(VanillaPings.MOD_ID, "ping"));
        public static final StreamCodec<RegistryFriendlyByteBuf, PingPayload> CODEC =
                StreamCodec.composite(BlockPos.STREAM_CODEC, PingPayload::blockPos, PingPayload::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }

    public static void register() {
        //? if >=26.1 {
        /*PayloadTypeRegistry.serverboundPlay().register(PingPayload.ID, PingPayload.CODEC);*/
        /*PayloadTypeRegistry.serverboundPlay().register(CameraPingPayload.ID, CameraPingPayload.CODEC);*/
        //?} else {
        PayloadTypeRegistry.playC2S().register(PingPayload.ID, PingPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(CameraPingPayload.ID, CameraPingPayload.CODEC);
        //?}
        //? if >=1.21 {
        ServerPlayNetworking.registerGlobalReceiver(PingPayload.ID, (payload, context) ->
                context.server().execute(() -> PingManager.pingWithCooldown(context.player())));
        ServerPlayNetworking.registerGlobalReceiver(CameraPingPayload.ID, (payload, context) ->
                context.server().execute(() -> PingManager.pingFromCameraWithCooldown(context.player(), payload.origin(), payload.direction())));
        //?} else {
        /*ServerPlayNetworking.registerGlobalReceiver(PingPayload.ID, (payload, context) ->
                context.player().getServer().execute(() -> PingManager.pingWithCooldown(context.player())));
        ServerPlayNetworking.registerGlobalReceiver(CameraPingPayload.ID, (payload, context) ->
                context.player().getServer().execute(() -> PingManager.pingFromCameraWithCooldown(context.player(), payload.origin(), payload.direction())));
        *///?}
    }
    /** Separate channel: never reinterpret the original upstream packet. */
    public record CameraPingPayload(net.minecraft.world.phys.Vec3 origin, net.minecraft.world.phys.Vec3 direction) implements CustomPacketPayload {
        public static final Type<CameraPingPayload> ID = new Type<>(Compat.id(VanillaPings.MOD_ID, "camera_ping_v1"));
        public static final StreamCodec<RegistryFriendlyByteBuf, CameraPingPayload> CODEC = StreamCodec.of(
                (buf, value) -> {
                    buf.writeDouble(value.origin.x); buf.writeDouble(value.origin.y); buf.writeDouble(value.origin.z);
                    buf.writeDouble(value.direction.x); buf.writeDouble(value.direction.y); buf.writeDouble(value.direction.z);
                },
                buf -> new CameraPingPayload(new net.minecraft.world.phys.Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()),
                        new net.minecraft.world.phys.Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble())));
        @Override public Type<? extends CustomPacketPayload> type() { return ID; }
    }
    //?} else {
    /*public static final net.minecraft.resources.ResourceLocation ID_PING = Compat.id(VanillaPings.MOD_ID, "ping");

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(ID_PING,
                (server, player, handler, buf, responseSender) -> server.execute(() -> PingManager.pingWithCooldown(player)));
    }
    *///?}
}

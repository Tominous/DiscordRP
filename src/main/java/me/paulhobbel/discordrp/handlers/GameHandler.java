package me.paulhobbel.discordrp.handlers;

import me.paulhobbel.discordrp.api.IDiscordRPDimension;
import me.paulhobbel.discordrp.api.impl.Registry;
import me.paulhobbel.discordrp.DiscordRP;
import me.paulhobbel.discordrp.utils.MinecraftRichPresence;
import me.paulhobbel.discordrp.manager.ManifestManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = DiscordRP.MODID, value = Side.CLIENT)
public class GameHandler {

    private static MinecraftRichPresence presence;

    @SubscribeEvent
    public static void onClientConnected(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        presence = new MinecraftRichPresence.Builder()
                .details("Connecting")
                .state(String.format("Playing %s", event.isLocal() ? "Singleplayer" : "Multiplayer"))
                .startTimestamp(System.currentTimeMillis() / 1000)
                .manifest(ManifestManager.getManifest())
                .build();

        presence.setPresence();
    }

    @SubscribeEvent
    public static void onJoinWorld(EntityJoinWorldEvent event) {
        if(event.getEntity().equals(Minecraft.getMinecraft().player)) {
            IDiscordRPDimension dimension = Registry.getInstance().getDimension(event.getWorld());

            presence = presence.buildUpon()
                    .dimension(dimension)
                    .build();

            presence.setPresence();
        }
    }

    @SubscribeEvent
    public static void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        presence = new MinecraftRichPresence.Builder()
                .details("In the Main Menu")
                .startTimestamp(System.currentTimeMillis() / 1000)
                .manifest(ManifestManager.getManifest())
                .build();

        presence.setPresence();
    }
}

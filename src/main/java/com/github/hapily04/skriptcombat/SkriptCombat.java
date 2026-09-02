package com.github.hapily04.skriptcombat;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.github.hapily04.skriptminestom.SkriptMinestom;
import io.github.togar2.pvp.MinestomPvP;
import io.github.togar2.pvp.player.CombatPlayerImpl;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class SkriptCombat extends JavaPlugin {

    private SkriptAddon addonInstance;

    @Override
    public void onEnable() {
        MinecraftServer.getGlobalEventHandler().addChild(MinestomPvP.events());

        MinestomPvP.init(false, true);

        MinecraftServer.getConnectionManager().setPlayerProvider(
            (playerConnection, gameProfile) -> {
                Player player = new CombatPlayerImpl(SkriptMinestom.getLuckPerms(), playerConnection, gameProfile);
                if (!SkriptMinestom.fireConnectEvent(player)) return null;
                return player;
            });

        addonInstance = Skript.registerAddon(this);
        try {
            addonInstance.loadClasses("com.github.hapily04.skriptcombat", "elements");
        } catch (IOException e) {
            getLogger().severe("An error occurred whilst loading skript-combat's elements: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public SkriptAddon getAddonInstance() {
        return addonInstance;
    }

}

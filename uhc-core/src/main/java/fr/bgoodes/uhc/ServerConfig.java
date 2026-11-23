package fr.bgoodes.uhc;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.set.SetCodec;

import java.util.HashSet;
import java.util.Set;

public class ServerConfig {

    public static final BuilderCodec<ServerConfig> CODEC = BuilderCodec.builder(ServerConfig.class, ServerConfig::new)
            .append(new KeyedCodec<>("Host", Host.CODEC),
                    (config, value) -> config.host = value,
                    (config) -> config.host).add()
            .append(new KeyedCodec<>("Spectator", Spectator.CODEC),
                    (config, value) -> config.spectator = value,
                    (config) -> config.spectator).add()
            .append(new KeyedCodec<>("SpawnMaxAttempts", Codec.INTEGER),
                    (config, value) -> config.spawnMaxAttempts = value,
                    (config) -> config.spawnMaxAttempts).add()
            .append(new KeyedCodec<>("PregenerateRadius", Codec.INTEGER),
                    (config, value) -> config.pregenerateRadius = value,
                    (config) -> config.pregenerateRadius).add()
            .build();

    private Host host = new Host();
    private Spectator spectator = new Spectator();
    private int spawnMaxAttempts = 20;
    private int pregenerateRadius = 0;

    public Host getHost() {
        return host;
    }

    public Spectator getSpectator() {
        return spectator;
    }

    public int getSpawnMaxAttempts() {
        return spawnMaxAttempts;
    }

    public int getPregenerateRadius() {
        return pregenerateRadius;
    }

    public static class Host {

        public static final BuilderCodec<Host> CODEC = BuilderCodec.builder(Host.class, Host::new)
                .append(new KeyedCodec<>("Permissions", new SetCodec<>(Codec.STRING, HashSet::new, true)),
                        (host, value) -> host.permissions = value,
                        (host) -> host.permissions).add()
                .build();

        private Set<String> permissions = Set.of(
                "uhc.host.*",
                "hytale.command.gamemode.*",
                "hytale.command.teleport.*",
                "hytale.teleport.command.tp",
                "hytale.command.give.*",
                "hytale.system.command.inventory.clear",
                "hytale.system.command.kick",
                "hytale.command.kill.*",
                "hytale.accesscontrolmodule.command.*",
                "hytale.universe.command.world.settings.pregenerate.*",
                "hytale.universe.command.world.perf.*",
                "hytale.system.command.stop"
        );

        public Set<String> getPermissions() {
            return permissions;
        }
    }

    public static class Spectator {

        public static final BuilderCodec<Spectator> CODEC = BuilderCodec.builder(Spectator.class, Spectator::new)
                .append(new KeyedCodec<>("Permissions", new SetCodec<>(Codec.STRING, HashSet::new, true)),
                        (spectator, value) -> spectator.permissions = value,
                        (spectator) -> spectator.permissions).add()
                .build();

        private Set<String> permissions = Set.of(
                "hytale.command.spectate.watch"
        );

        public Set<String> getPermissions() {
            return permissions;
        }
    }
}

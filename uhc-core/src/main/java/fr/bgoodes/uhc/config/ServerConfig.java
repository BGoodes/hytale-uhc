package fr.bgoodes.uhc.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

import java.util.List;

public class ServerConfig {

    public static final BuilderCodec<ServerConfig> CODEC = BuilderCodec.builder(ServerConfig.class, ServerConfig::new)
            .append(new KeyedCodec<>("Host", Host.CODEC),
                    (config, value) -> config.host = value,
                    (config) -> config.host).add()
            .build();

    private Host host = new Host();

    public Host getHost() {
        return host;
    }

    public static class Host {

        public static final BuilderCodec<Host> CODEC = BuilderCodec.builder(Host.class, Host::new)
                .append(new KeyedCodec<>("Permissions", Codec.STRING_ARRAY),
                        (host, value) -> host.permissions = value,
                        (host) -> host.permissions).add()
                .build();

        private String[] permissions = {
                "uhc.host.*",
                "hytale.command.gamemode.*",
                "hytale.command.teleport.*",
                "hytale.teleport.command.tp",
                "hytale.command.give.*",
                "hytale.system.command.inventory.clear",
                "hytale.command.kick.*",
                "hytale.command.kill.*",
                "hytale.accesscontrolmodule.command.*",
                "hytale.universe.command.world.settings.pregenerate.*",
                "hytale.universe.command.world.perf.*",
                "hytale.system.command.stop"
        };

        public List<String> getPermissions() {
            return List.of(permissions);
        }
    }
}

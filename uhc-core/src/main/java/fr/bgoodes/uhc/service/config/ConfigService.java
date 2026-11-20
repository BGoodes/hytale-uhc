package fr.bgoodes.uhc.service.config;

import com.hypixel.hytale.server.core.util.BsonUtil;
import fr.bgoodes.uhc.service.config.model.UHCConfig;
import org.bson.BsonDocument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigService {

    private static final String DEFAULT_CONFIG_NAME = "default_uhc.json";

    private final Path path;
    private final BsonDocument document;

    private final Map<String, UHCConfig> configs = new LinkedHashMap<>();

    public ConfigService(@NotNull Path dataDirectory) {
        this.path = dataDirectory.resolve(DEFAULT_CONFIG_NAME);

        BsonDocument loadedDocument = Files.exists(path) ? BsonUtil.readDocumentNow(path) : null;
        this.document = loadedDocument != null ? loadedDocument : new BsonDocument();
    }

    public synchronized void registerConfig(@NotNull String key, @NotNull UHCConfig config) {
        if (document.containsKey(key)) {
            config.decode(document.getDocument(key));
        }

        configs.put(key, config);
        document.put(key, config.encode());
        BsonUtil.writeDocument(path, document).join();
    }

    public synchronized @Nullable UHCConfig getConfig(@NotNull String key) {
        return configs.get(key);
    }

    public synchronized @NotNull Map<String, UHCConfig> getConfigs() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(configs)); // keep map order
    }
}

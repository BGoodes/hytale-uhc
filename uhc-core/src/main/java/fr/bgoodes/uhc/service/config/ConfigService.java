package fr.bgoodes.uhc.service.config;

import com.hypixel.hytale.server.core.util.BsonUtil;
import fr.bgoodes.uhc.service.config.model.IConfig;
import org.bson.BsonDocument;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigService {

    private static final String FILE_NAME = "default_uhc.json";

    private final Path path;
    private final BsonDocument document;

    public ConfigService(@NotNull Path dataDirectory) {
        this.path = dataDirectory.resolve(FILE_NAME);

        BsonDocument loadedDocument = Files.exists(path) ? BsonUtil.readDocumentNow(path) : null;
        this.document = loadedDocument != null ? loadedDocument : new BsonDocument();
    }

    public synchronized void registerConfig(@NotNull String id, @NotNull IConfig config) {
        if (document.containsKey(id)) {
            config.decode(document.getDocument(id));
            return;
        }

        document.put(id, config.encode());
        BsonUtil.writeDocument(path, document).join();
    }
}

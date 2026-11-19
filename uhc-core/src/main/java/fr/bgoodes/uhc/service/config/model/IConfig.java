package fr.bgoodes.uhc.service.config.model;

import org.bson.BsonDocument;
import org.jetbrains.annotations.NotNull;

public interface IConfig {

    @NotNull BsonDocument encode();

    void decode(@NotNull BsonDocument document);
}

package fr.bgoodes.uhc.service.config.model;

import org.bson.BsonDocument;

public interface IConfig {

    BsonDocument encode();

    void decode(BsonDocument document);
}

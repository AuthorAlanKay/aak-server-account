package org.aak.server.account.utils;

import cn.hutool.db.nosql.mongo.MongoFactory;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class MongoUtils {

    private static final MongoDatabase MONGO_DB = MongoFactory.getDS().getDb("database");

    public static final Map<String, MongoCollection<Document>> COLLECTION_MAP = new HashMap<>();

    static {
        COLLECTION_MAP.put("account", MONGO_DB.getCollection("account"));
        COLLECTION_MAP.put("book", MONGO_DB.getCollection("book"));
        COLLECTION_MAP.put("finance", MONGO_DB.getCollection("finance"));
        COLLECTION_MAP.put("keep", MONGO_DB.getCollection("keep"));
        COLLECTION_MAP.put("quickstart", MONGO_DB.getCollection("quickstart"));
        COLLECTION_MAP.put("translate", MONGO_DB.getCollection("translate"));
    }
}

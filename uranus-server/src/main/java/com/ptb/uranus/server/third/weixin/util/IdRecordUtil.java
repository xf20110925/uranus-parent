package com.ptb.uranus.server.third.weixin.util;

import com.alibaba.fastjson.JSON;
import com.ptb.uranus.server.third.weixin.entity.IdRecord;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Created by xuefeng on 2016/5/18.
 */
public class IdRecordUtil {
    public static final String SERIALIZEPATH = "idRecord.json";

    private static IdRecord idRecord;

    static Path path = Paths.get(SERIALIZEPATH);

    private static void serialize(IdRecord idRecord) {
        try (BufferedWriter writer = Files.newBufferedWriter(path);) {
            writer.write(idRecord.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static IdRecord deSerialize() {
        try (BufferedReader reader = Files.newBufferedReader(path);) {
            String json = reader.readLine();
            idRecord = JSON.parseObject(json, IdRecord.class);
        } catch (Exception e) {
            idRecord = new IdRecord();
            serialize(idRecord);
        }
        return idRecord;
    }

    public synchronized static Optional<IdRecord> getIdRecord() {
        try {
            if (idRecord != null) {
                return Optional.of(idRecord);
            }
            return Optional.of(deSerialize());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public synchronized static void syncMediaId(long mediaId) {
        idRecord.setMediaId(mediaId);
        serialize(idRecord);
    }

    public synchronized static void syncDynamicArticleId(long dynamicArticleId) {
        idRecord.setDynamicArticleId(dynamicArticleId);
        serialize(idRecord);
    }

    public synchronized static void syncStaticArticleId(long syncStaticArticleId) {
        idRecord.setStaticArticleId(syncStaticArticleId);
        serialize(idRecord);
    }

    public synchronized static void syncWbArticleId(long wbArticleId) {
        idRecord.setWbArticleId(wbArticleId);
        serialize(idRecord);
    }

    public synchronized static void syncWbMediaId(long wbMediaId) {
        idRecord.setWbMediaId(wbMediaId);
        serialize(idRecord);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Optional<IdRecord> idRecordOpt = getIdRecord();
        idRecordOpt.ifPresent(rc -> System.out.println(rc));
    }

}

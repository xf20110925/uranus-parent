package com.ptb.uranus.server.third.weixin.util;

import com.ptb.uranus.server.third.weixin.entity.IdRecord;
import java.io.*;
import java.util.Optional;

/**
 * Created by xuefeng on 2016/5/18.
 */
public class IdRecordUtil {
    public static final String SERIALIZEPATH = "idRecord.ser";

    private static IdRecord idRecord;

    private static void serialize(IdRecord idRecord) {
        try (FileOutputStream fs = new FileOutputStream(SERIALIZEPATH);
             ObjectOutputStream os = new ObjectOutputStream(fs);) {
            os.writeObject(idRecord);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     private static IdRecord deSerialize() {
        try (FileInputStream fs = new FileInputStream(SERIALIZEPATH);
             ObjectInputStream objStream = new ObjectInputStream(fs);) {
             idRecord = (IdRecord) objStream.readObject();
            return idRecord;
        } catch (FileNotFoundException e) {
            idRecord = new IdRecord(0, 0, 0);
            serialize(idRecord);
            return idRecord;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return idRecord;
    }
    public synchronized static Optional<IdRecord> getIdRecord(){
        try {
            if (idRecord != null){
                return Optional.of(idRecord);
            }
            return Optional.of(deSerialize());
        } catch (Exception e) {
           return Optional.empty();
        }
    }
    public synchronized static void syncMediaId(long mediaId){
        idRecord.setMediaId(mediaId);
        serialize(idRecord);
    }
    public synchronized static void syncDynamicArticleId(long dynamicArticleId){
        idRecord.setDynamicArticleId(dynamicArticleId);
        serialize(idRecord);
    }
    public synchronized static void syncStaticArticleId(long syncStaticArticleId){
        idRecord.setStaticArticleId(syncStaticArticleId);
        serialize(idRecord);
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        /*IdRecord idRecord = new IdRecord(10, 100, 100);
        FileOutputStream fs = new FileOutputStream("myFoo.ser");
        ObjectOutputStream os = new ObjectOutputStream(fs);
        os.writeObject(idRecord);*/
        FileInputStream fs = new FileInputStream(SERIALIZEPATH);
        ObjectInputStream objStream = new ObjectInputStream(fs);
        IdRecord idRecord = (IdRecord) objStream.readObject();
        System.out.println(idRecord);

    }

}

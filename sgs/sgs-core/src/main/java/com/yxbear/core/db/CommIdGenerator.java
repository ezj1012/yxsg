package com.yxbear.core.db;

import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yxbear.core.pk.PrimaryKeyGenerator;
import com.yxbear.core.pk.PrimaryKeyGeneratorProvider;

public class CommIdGenerator implements IdGenerator {

    @Override
    public Number id(TableInfo tableInfo) {
        // Long 用 雪花
        // if (Long.class == tableInfo.getIdColumn().getField().getType()) { return LongIDGen.createGenerator().getID(); }
        // int 用数据库中的
        PrimaryKeyGenerator pkGenerator = PrimaryKeyGeneratorProvider.getPKGenerator(tableInfo.getTableName());
        return pkGenerator.next().intValue(); // 这里写死int
    }

    public static class LongIDGen {

        protected Logger logger = LoggerFactory.getLogger(LongIDGen.class);

        private long baseId;

        private static LongIDGen idGenerator = null;

        public static LongIDGen createGenerator() {
            if (null == idGenerator) {
                idGenerator = new LongIDGen();
            }
            return idGenerator;
        }

        private LongIDGen() {
            long t = System.currentTimeMillis();
            // 53~45
            baseId = t;
            baseId &= 0x1FF0000000L;
            baseId <<= 16;
            // 30~17
            t &= 0xFFFC000L;
            t <<= 2;
            baseId |= t;
            // 44~31
            SecureRandom ng = new SecureRandom();
            t = ng.nextLong();
            t &= 0x3FFF0000000L;
            t <<= 2;
            baseId |= t;
            // 16~1
            baseId /= 50000;
            baseId *= 50000;
            baseId &= 0x1FFFFFFFFFFFFFL;
        }

        public synchronized long getID() {
            return baseId++;
        }

        public synchronized String getBatchID(long batch) {
            String idStartEnd = String.valueOf(baseId) + "-" + String.valueOf(baseId + batch - 1);
            baseId = baseId + batch;
            return idStartEnd;
        }

    }

}

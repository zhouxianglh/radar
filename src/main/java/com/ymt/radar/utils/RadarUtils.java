package com.ymt.radar.utils;

import java.io.Closeable;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RadarUtils {
    private static final Logger logger = LoggerFactory.getLogger(RadarUtils.class);

    /**
     * 按指定顺序关闭批定对象
     */
    public static void close(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            if (null != closeable) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
        }
    }
}

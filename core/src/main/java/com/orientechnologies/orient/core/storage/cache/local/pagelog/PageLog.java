package com.orientechnologies.orient.core.storage.cache.local.pagelog;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;

/**
 * Interface for the log which keeps data of pages before they will be finally fsync-ed to the data files. This log is used to
 * implement double write pattern.
 * <p>
 * At the first step we perform fsync of the data to the log using single sequential write calling {@link #write(ByteBuffer[])}
 * method.
 * <p>
 * As the second step we write pages to the data files.
 * <p>
 * At third step we fsync data files in background process and then truncate log calling {@link #truncate()} method.
 * Write to the file and truncation of data should be done in single lock to prevent situation when data are written to the log
 * but not to the pages and then truncated. That is typically not a problem because write cache uses single thread model.
 * <p>
 * If during the write of pages we reach log threshold {@link #write(ByteBuffer[])} method will return true. If that happens fsync
 * of pages should be called to truncate page log.
 */
public interface PageLog {
  boolean write(ByteBuffer[] buffers) throws IOException;

  void truncate() throws IOException;

  void close() throws IOException;

  void open(Path storagePath) throws IOException;
}
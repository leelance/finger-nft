package com.fingerchar.core.storage;

import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.stream.Stream;

public class StorageAdaptor implements Storage {

  @Override
  public void store(InputStream inputStream, long contentLength, String contentType, String keyName) {
    //  Auto-generated method stub

  }

  @Override
  public String store(InputStream inputStream, String fileName) {
    //  Auto-generated method stub
    return null;
  }

  @Override
  public Stream<Path> loadAll() {
    //  Auto-generated method stub
    return null;
  }

  @Override
  public Path load(String keyName) {
    //  Auto-generated method stub
    return null;
  }

  @Override
  public Resource loadAsResource(String keyName) {
    //  Auto-generated method stub
    return null;
  }

  @Override
  public void delete(String keyName) {
    //  Auto-generated method stub

  }

  @Override
  public String generateUrl(String keyName) {
    //  Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see com.fingerchar.core.storage.Storage#store(java.io.InputStream[], java.lang.String[])
   */
  @Override
  public String[] store(InputStream[] inputStreams, String[] fileNames, String dirPath) {
    //  Auto-generated method stub
    return null;
  }

}

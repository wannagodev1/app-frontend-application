/*
 * This file is part of the WannaGo distribution (https://github.com/wannago).
 * Copyright (c) [2019] - [2020].
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.wannagoframework.frontend.components.fileUpload;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Class containing file information for upload.
 */
public class FileData implements Serializable {

  private final String fileName, mimeType;
  private final ObjectOutputStream outputBuffer;

  /**
   * Create a FileData instance for a file.
   *
   * @param fileName the file name
   * @param mimeType the file MIME type
   * @param outputBuffer the output buffer where to write the file
   */
  public FileData(String fileName, String mimeType,
      ObjectOutputStream outputBuffer) {
    this.fileName = fileName;
    this.mimeType = mimeType;
    this.outputBuffer = outputBuffer;
  }

  /**
   * Return the mimeType of this file.
   *
   * @return mime types of the files
   */
  public String getMimeType() {
    return mimeType;
  }

  /**
   * Return the name of this file.
   *
   * @return file name
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * Return the output buffer for this file data.
   *
   * @return output buffer
   */
  public OutputStream getOutputBuffer() {
    return new ByteArrayOutputStream();
  }
}

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

import com.vaadin.flow.component.upload.Receiver;
import java.io.OutputStream;
import java.util.Arrays;
import lombok.Data;

/**
 * Basic in memory file receiver implementation.
 */
@Data
public class MemoryBuffer implements Receiver {

  private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
  protected byte[] buf = new byte[32];

  /**
   * The number of valid bytes in the buffer.
   */
  protected int count;
  private String fileName, mimeType;

  @Override
  public OutputStream receiveUpload(String fileName, String MIMEType) {
    this.fileName = fileName;
    this.mimeType = MIMEType;
    return new OutputStream() {
      public synchronized void write(int b) {
        ensureCapacity(count + 1);
        buf[count] = (byte) b;
        count += 1;
      }

      private void ensureCapacity(int minCapacity) {
        // overflow-conscious code
        if (minCapacity - buf.length > 0) {
          grow(minCapacity);
        }
      }

      private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = buf.length;
        int newCapacity = oldCapacity << 1;
        if (newCapacity - minCapacity < 0) {
          newCapacity = minCapacity;
        }
        if (newCapacity - MAX_ARRAY_SIZE > 0) {
          newCapacity = hugeCapacity(minCapacity);
        }
        buf = Arrays.copyOf(buf, newCapacity);
      }

      private int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
        {
          throw new OutOfMemoryError();
        }
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
      }
    };
  }
}

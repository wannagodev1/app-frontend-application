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


package io.rocketbase.vaadin.croppie;

import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import java.io.InputStream;
import java.net.URL;
import lombok.SneakyThrows;


public class CorsProxyResource extends StreamResource {

  public CorsProxyResource(String filename, String url) {
    super(filename,
        new InputStreamFactory() {
          @SneakyThrows
          @Override
          public InputStream createInputStream() {
            return new URL(url).openStream();
          }
        }

    );
  }
}

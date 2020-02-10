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


package io.rocketbase.vaadin.croppie.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CropPoints {

  private int topLeftX, topLeftY, bottomRightX, bottomRightY;

  public static CropPoints parseArray(String arrayString) {
    if (arrayString != null) {
      Pattern pattern = Pattern.compile(
          ".*\\[[ \"]*([0-9]+)[ \"]*,[ \"]*([0-9]+)[ \"]*,[ \"]*([0-9]+)[ \"]*,[ \"]*([0-9]+)[ \"]*\\].*");
      Matcher matcher = pattern.matcher(arrayString);
      if (matcher.matches()) {
        return new CropPoints(Integer.parseInt(matcher.group(1)),
            Integer.parseInt(matcher.group(2)),
            Integer.parseInt(matcher.group(3)),
            Integer.parseInt(matcher.group(4)));
      }
    }
    return null;
  }

  public String getJsonString() {
    return String.format("[%d, %d, %d, %d]", topLeftX, topLeftY, bottomRightX, bottomRightY);
  }
}
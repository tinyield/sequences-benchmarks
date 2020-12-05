/*
 * Copyright (c) 2017, jasync.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.tiniyield.sequences.benchmarks.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/**
 * @author Miguel Gamboa
 * created on 30-03-2017
 */
public class FileRequest implements Request {
    @Override
    public InputStream getBody(String path) {
        String[] parts = path.split("/");
        path = parts[parts.length - 1]
                .replaceAll("[,=?&]", "-")
                .substring(0, 68);
        try {
            return ClassLoader.getSystemResource(path).openStream();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

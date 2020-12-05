package com.github.tiniyield.sequences.benchmarks.common;

/**
 * @author Miguel Gamboa
 * created on 12-07-2017
 */

import com.github.tiniyield.sequences.benchmarks.common.util.FileRequest;
import com.github.tiniyield.sequences.benchmarks.common.util.IteratorFromInputStream;
import com.github.tiniyield.sequences.benchmarks.common.util.Request;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;

@State(Scope.Benchmark)
public class WeatherDataSource {
    static final String path = "past-weather.ashx-q-41.15--8.6167-date-2017-02-01-enddate-2017-04-30.csv";
    static final Request req = new FileRequest();

    public final String[] data;

    public WeatherDataSource() {
        try (InputStream in = req.getBody(path)) {
            ArrayList<String> lines = new ArrayList<>();
            new IteratorFromInputStream(req.getBody(path))
                    .forEachRemaining(lines::add);
            data = lines.toArray(new String[lines.size()]);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

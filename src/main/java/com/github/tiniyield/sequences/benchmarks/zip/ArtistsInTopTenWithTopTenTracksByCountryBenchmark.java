package com.github.tiniyield.sequences.benchmarks.zip;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.javatuples.Pair;
import org.jayield.Query;
import org.jooq.lambda.Seq;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import com.github.tiniyield.sequences.benchmarks.operations.GuavaOperations;
import com.github.tiniyield.sequences.benchmarks.operations.JoolOperations;
import com.github.tiniyield.sequences.benchmarks.operations.ProtonpackOperations;
import com.github.tiniyield.sequences.benchmarks.operations.QueryOperations;
import com.github.tiniyield.sequences.benchmarks.operations.StreamExOperations;
import com.github.tiniyield.sequences.benchmarks.operations.StreamOperations;
import com.github.tiniyield.sequences.benchmarks.operations.VavrOperations;
import com.github.tiniyield.sequences.benchmarks.operations.ZiplineOperations;
import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkJoolUtils;
import com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamUtils;

import one.util.streamex.StreamEx;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class ArtistsInTopTenWithTopTenTracksByCountryBenchmark extends AbstractZipBenchmark<Pair<Country, List<Artist>>> {

    @Setup
    public void init() {
        SequenceBenchmarkUtils.assertArtistsInTopTenWithTopTenTracksByCountryBenchmarkValidity(getGuava(),
                                                                                               getJool(),
                                                                                               getQuery(),
                                                                                               getStream(),
                                                                                               getProtonpack(),
                                                                                               getZipline(),
                                                                                               getStreamEx(),
                                                                                               getVavr());
    }

    protected io.vavr.collection.Stream<Pair<Country, List<Artist>>> getVavr() {
        return VavrOperations.artistsInTopTenWithTopTenTracksByCountry();
    }

    protected StreamEx<Pair<Country, List<Artist>>> getStreamEx() {
        return StreamExOperations.artistsInTopTenWithTopTenTracksByCountry();
    }

    protected Stream<Pair<Country, List<Artist>>> getZipline() {
        return ZiplineOperations.artistsInTopTenWithTopTenTracksByCountry();
    }

    protected Stream<Pair<Country, List<Artist>>> getProtonpack() {
        return ProtonpackOperations.artistsInTopTenWithTopTenTracksByCountry();
    }

    protected Stream<Pair<Country, List<Artist>>> getStream() {
        return StreamOperations.artistsInTopTenWithTopTenTracksByCountry();
    }

    protected Query<Pair<Country, List<Artist>>> getQuery() {
        return QueryOperations.artistsInTopTenWithTopTenTracksByCountry();
    }

    protected Stream<Pair<Country, List<Artist>>> getGuava() {
        return guava.artistsInTopTenWithTopTenTracksByCountry(
                SequenceBenchmarkStreamUtils.getArtists(),
                SequenceBenchmarkStreamUtils.getTracks()
        );
    }

    protected Seq<Pair<Country, List<Artist>>> getJool() {
        return jool.artistsInTopTenWithTopTenTracksByCountry(
                SequenceBenchmarkJoolUtils.getArtists(),
                SequenceBenchmarkJoolUtils.getTracks()
        );
    }

}

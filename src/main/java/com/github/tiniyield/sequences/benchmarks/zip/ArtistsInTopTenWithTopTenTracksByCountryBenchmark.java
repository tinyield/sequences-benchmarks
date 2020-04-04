package com.github.tiniyield.sequences.benchmarks.zip;

import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamUtils.getArtists;
import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamUtils.getTracks;

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

import com.github.tiniyield.sequences.benchmarks.operations.VavrOperations;
import com.github.tiniyield.sequences.benchmarks.operations.ZiplineOperations;
import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkJoolUtils;
import com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkQueryUtils;
import com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamExUtils;
import com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamUtils;
import com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkVavrUtils;

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
        return vavr.artistsInTopTenWithTopTenTracksByCountry(SequenceBenchmarkVavrUtils.getArtists(),
                                                                       SequenceBenchmarkVavrUtils.getTracks());
    }

    protected StreamEx<Pair<Country, List<Artist>>> getStreamEx() {
        return streamEx.artistsInTopTenWithTopTenTracksByCountry(SequenceBenchmarkStreamExUtils.getArtists(),
                                                                           SequenceBenchmarkStreamExUtils.getTracks());
    }

    protected Stream<Pair<Country, List<Artist>>> getZipline() {
        return ZiplineOperations.artistsInTopTenWithTopTenTracksByCountry();
    }

    protected Stream<Pair<Country, List<Artist>>> getProtonpack() {
        return protonpack.artistsInTopTenWithTopTenTracksByCountry(SequenceBenchmarkStreamUtils.getArtists(),
                                                                             SequenceBenchmarkStreamUtils.getTracks());
    }

    protected Stream<Pair<Country, List<Artist>>> getStream() {
        return stream.artistsInTopTenWithTopTenTracksByCountry(getArtists(), getTracks());
    }

    protected Query<Pair<Country, List<Artist>>> getQuery() {
        return query.artistsInTopTenWithTopTenTracksByCountry(SequenceBenchmarkQueryUtils.getArtists(),
                                                                        SequenceBenchmarkQueryUtils.getTracks());
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

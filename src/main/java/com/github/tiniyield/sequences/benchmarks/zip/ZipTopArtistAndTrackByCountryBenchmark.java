package com.github.tiniyield.sequences.benchmarks.zip;

import com.github.tiniyield.sequences.benchmarks.kt.operations.utils.SequenceBenchmarkKtSequenceUtils;
import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track;
import com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkJoolUtils;
import com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkQueryUtils;
import com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamExUtils;
import com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamUtils;
import com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkVavrUtils;
import kotlin.sequences.Sequence;
import one.util.streamex.StreamEx;
import org.javatuples.Triplet;
import org.jayield.Query;
import org.jooq.lambda.Seq;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamUtils.getArtists;
import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamUtils.getTracks;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class ZipTopArtistAndTrackByCountryBenchmark extends AbstractZipBenchmark<Triplet<Country, Artist, Track>> {

    @Override
    @Setup
    public void init() {
        SequenceBenchmarkUtils.assertZipTopArtistAndTrackByCountryBenchmarkValidity(
                getGuava(),
                getJool(),
                getQuery(),
                getStream(),
                getProtonpack(),
                getZipline(),
                getStreamEx(),
                getVavr(),
                getKotlin(),
                getJKotlin()
        );
    }

    @Override
    protected io.vavr.collection.Stream<Triplet<Country, Artist, Track>> getVavr() {
        return vavr.zipTopArtistAndTrackByCountry(SequenceBenchmarkVavrUtils.getArtists(),
                SequenceBenchmarkVavrUtils.getTracks());
    }

    @Override
    protected StreamEx<Triplet<Country, Artist, Track>> getStreamEx() {
        return streamEx.zipTopArtistAndTrackByCountry(SequenceBenchmarkStreamExUtils.getArtists(),
                SequenceBenchmarkStreamExUtils.getTracks());
    }

    @Override
    protected Stream<Triplet<Country, Artist, Track>> getZipline() {
        return zipline.zipTopArtistAndTrackByCountry(getArtists(), getTracks());
    }

    @Override
    protected Stream<Triplet<Country, Artist, Track>> getProtonpack() {
        return protonpack.zipTopArtistAndTrackByCountry(SequenceBenchmarkStreamUtils.getArtists(),
                SequenceBenchmarkStreamUtils.getTracks());
    }

    @Override
    protected Stream<Triplet<Country, Artist, Track>> getStream() {
        return stream.zipTopArtistAndTrackByCountry(getArtists(), getTracks());
    }

    @Override
    protected Query<Triplet<Country, Artist, Track>> getQuery() {
        return query.zipTopArtistAndTrackByCountry(SequenceBenchmarkQueryUtils.getArtists(),
                SequenceBenchmarkQueryUtils.getTracks());
    }

    @Override
    protected Stream<Triplet<Country, Artist, Track>> getGuava() {
        return guava.zipTopArtistAndTrackByCountry(
                SequenceBenchmarkStreamUtils.getArtists(),
                SequenceBenchmarkStreamUtils.getTracks()
        );
    }

    @Override
    protected Seq<Triplet<Country, Artist, Track>> getJool() {
        return jool.zipTopArtistAndTrackByCountry(
                SequenceBenchmarkJoolUtils.getArtists(),
                SequenceBenchmarkJoolUtils.getTracks()
        );
    }

    @Override
    protected Sequence<Triplet<Country, Artist, Track>> getKotlin() {
        return kotlin.zipTopArtistAndTrackByCountry(
                SequenceBenchmarkKtSequenceUtils.INSTANCE.getArtists(),
                SequenceBenchmarkKtSequenceUtils.INSTANCE.getTracks()
        );
    }

    @Override
    protected Sequence<Triplet<Country, Artist, Track>> getJKotlin() {
        return jkotlin.zipTopArtistAndTrackByCountry(
                SequenceBenchmarkKtSequenceUtils.INSTANCE.getArtists(),
                SequenceBenchmarkKtSequenceUtils.INSTANCE.getTracks()
        );
    }
}

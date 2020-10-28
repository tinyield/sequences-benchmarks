package com.github.tiniyield.sequences.benchmarks.zip;

import com.github.tiniyield.sequences.benchmarks.kt.operations.utils.SequenceBenchmarkKtSequenceUtils;
import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.operations.utils.*;
import kotlin.sequences.Sequence;
import one.util.streamex.StreamEx;
import org.javatuples.Pair;
import org.jayield.Query;
import org.jooq.lambda.Seq;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamUtils.getArtists;
import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamUtils.getTracks;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class ArtistsInTopTenWithTopTenTracksByCountryBenchmark extends AbstractZipBenchmark<Pair<Country, List<Artist>>> {

    @Override
    @Setup
    public void init() {
        SequenceBenchmarkUtils.assertArtistsInTopTenWithTopTenTracksByCountryBenchmarkValidity(
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
    protected io.vavr.collection.Stream<Pair<Country, List<Artist>>> getVavr() {
        return vavr.artistsInTopTenWithTopTenTracksByCountry(SequenceBenchmarkVavrUtils.getArtists(),
                SequenceBenchmarkVavrUtils.getTracks());
    }

    @Override
    protected StreamEx<Pair<Country, List<Artist>>> getStreamEx() {
        return streamEx.artistsInTopTenWithTopTenTracksByCountry(SequenceBenchmarkStreamExUtils.getArtists(),
                SequenceBenchmarkStreamExUtils.getTracks());
    }

    @Override
    protected Stream<Pair<Country, List<Artist>>> getZipline() {
        return zipline.artistsInTopTenWithTopTenTracksByCountry(getArtists(), getTracks());
    }

    @Override
    protected Stream<Pair<Country, List<Artist>>> getProtonpack() {
        return protonpack.artistsInTopTenWithTopTenTracksByCountry(SequenceBenchmarkStreamUtils.getArtists(),
                SequenceBenchmarkStreamUtils.getTracks());
    }

    @Override
    protected Stream<Pair<Country, List<Artist>>> getStream() {
        return stream.artistsInTopTenWithTopTenTracksByCountry(getArtists(), getTracks());
    }

    @Override
    protected Query<Pair<Country, List<Artist>>> getQuery() {
        return query.artistsInTopTenWithTopTenTracksByCountry(SequenceBenchmarkQueryUtils.getArtists(),
                SequenceBenchmarkQueryUtils.getTracks());
    }

    @Override
    protected Stream<Pair<Country, List<Artist>>> getGuava() {
        return guava.artistsInTopTenWithTopTenTracksByCountry(
                SequenceBenchmarkStreamUtils.getArtists(),
                SequenceBenchmarkStreamUtils.getTracks()
        );
    }

    @Override
    protected Seq<Pair<Country, List<Artist>>> getJool() {
        return jool.artistsInTopTenWithTopTenTracksByCountry(
                SequenceBenchmarkJoolUtils.getArtists(),
                SequenceBenchmarkJoolUtils.getTracks()
        );
    }

    @Override
    protected Sequence<Pair<Country, List<Artist>>> getKotlin() {
        return kotlin.artistsInTopTenWithTopTenTracksByCountry(
                SequenceBenchmarkKtSequenceUtils.INSTANCE.getArtists(),
                SequenceBenchmarkKtSequenceUtils.INSTANCE.getTracks()
        );
    }

    @Override
    protected Sequence<Pair<Country, List<Artist>>> getJKotlin() {
        return jkotlin.artistsInTopTenWithTopTenTracksByCountry(
                SequenceBenchmarkKtSequenceUtils.INSTANCE.getArtists(),
                SequenceBenchmarkKtSequenceUtils.INSTANCE.getTracks()
        );
    }

}

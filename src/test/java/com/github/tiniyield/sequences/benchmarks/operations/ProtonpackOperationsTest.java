package com.github.tiniyield.sequences.benchmarks.operations;

import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track;
import com.github.tiniyield.sequences.benchmarks.operations.model.wrapper.Value;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testng.AssertJUnit.assertEquals;

public class ProtonpackOperationsTest {

    private ProtonpackOperations instance;
    private Stream<Pair<Country, Stream<Artist>>> artists;
    private Stream<Pair<Country, Stream<Track>>> tracks;
    private MockData mockData;
    private Stream<Value> values;
    private Stream<Integer> numbers;
    private Stream<Integer> otherNumbers;

    @BeforeMethod
    public void setup() {
        mockData = new MockData();
        instance = new ProtonpackOperations();
        artists = mockData.getCountries().stream().map(c -> Pair.with(c, mockData.getArtists().stream()));
        tracks = mockData.getCountries().stream().map(c -> Pair.with(c, mockData.getTracks().stream()));
        values = mockData.getValues().stream();
        numbers = mockData.getNumbers().stream();
        otherNumbers = mockData.getNumbers().stream().sorted((t0, t1) -> t1 - t0);
    }

    @Test
    public void testZipTopArtistAndTrackByCountry() {
        List<Triplet<Country, Artist, Track>> expected = List.of(
                Triplet.with(
                        mockData.getCountries().get(0),
                        mockData.getArtists().get(0),
                        mockData.getTracks().get(0)
                )
        );

        List<Triplet<Country, Artist, Track>> actual = this.instance.zipTopArtistAndTrackByCountry(artists, tracks)
                                                                    .collect(Collectors.toList());


        assertEquals(expected.size(), actual.size());
        Triplet<Country, Artist, Track> actual0 = actual.get(0);
        Triplet<Country, Artist, Track> expected0 = expected.get(0);

        assertEquals(expected0.getValue0(), actual0.getValue0());
        assertEquals(expected0.getValue1(), actual0.getValue1());
        assertEquals(expected0.getValue2(), actual0.getValue2());
    }

    @Test
    public void testArtistsInTopTenWithTopTenTracksByCountry() {
        List<Pair<Country, List<Artist>>> expected = List.of(
                Pair.with(
                        mockData.getCountries().get(0),
                        mockData.getArtists().stream().skip(5).limit(5).collect(Collectors.toList())
                ),
                Pair.with(
                        mockData.getCountries().get(1),
                        mockData.getArtists().stream().skip(5).limit(5).collect(Collectors.toList())
                )
        );

        List<Pair<Country, List<Artist>>> actual = this.instance.artistsInTopTenWithTopTenTracksByCountry(artists,
                                                                                                          tracks)
                                                                .collect(Collectors.toList());


        assertEquals(expected.size(), actual.size());
        Pair<Country, List<Artist>> actual0 = actual.get(0);
        Pair<Country, List<Artist>> expected0 = expected.get(0);

        assertEquals(expected0.getValue0(), actual0.getValue0());
        assertEquals(expected0.getValue1().size(), actual0.getValue1().size());
        for (int i = 0; i < actual0.getValue1().size(); i++) {
            assertEquals(expected0.getValue1().get(i), actual0.getValue1().get(i));
        }

        Pair<Country, List<Artist>> actual1 = actual.get(0);
        Pair<Country, List<Artist>> expected1 = expected.get(0);

        assertEquals(expected1.getValue0(), actual1.getValue0());
        assertEquals(expected1.getValue1().size(), actual1.getValue1().size());
        for (int i = 0; i < actual1.getValue1().size(); i++) {
            assertEquals(expected1.getValue1().get(i), actual1.getValue1().get(i));
        }
    }

    @Test
    public void testZipPrimeWithValue() {
        List<Pair<Integer, Value>> expected = List.of(
                Pair.with(3, new Value(3)),
                Pair.with(5, new Value(4))
        );
        List<Pair<Integer, Value>> actual = instance.zipPrimeWithValue(numbers, values).collect(Collectors.toList());

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(i).getValue0(), actual.get(i).getValue0());
            assertEquals(expected.get(i).getValue1(), actual.get(i).getValue1());
        }
    }
}

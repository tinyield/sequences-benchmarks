package com.github.tiniyield.sequences.benchmarks.operations;

import com.github.tiniyield.sequences.benchmarks.common.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.common.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.common.model.track.Track;
import com.github.tiniyield.sequences.benchmarks.common.model.wrapper.Value;

import java.util.List;
import java.util.stream.Collectors;

public class MockData {

    public List<Country> getCountries() {
        return List.of(
                new Country().setName("0"),
                new Country().setName("1")
        );
    }

    public List<Track> getTracks() {
        List<Artist> artists = getArtists();
        return List.of(
                new Track().setName("0").setArtist(artists.get(artists.size() - 1)),
                new Track().setName("1").setArtist(artists.get(artists.size() - 2)),
                new Track().setName("2").setArtist(artists.get(artists.size() - 3)),
                new Track().setName("3").setArtist(artists.get(artists.size() - 4)),
                new Track().setName("4").setArtist(artists.get(artists.size() - 5)),
                new Track().setName("5").setArtist(artists.get(artists.size() - 6)),
                new Track().setName("6").setArtist(artists.get(artists.size() - 7)),
                new Track().setName("7").setArtist(artists.get(artists.size() - 8)),
                new Track().setName("8").setArtist(artists.get(artists.size() - 9)),
                new Track().setName("9").setArtist(artists.get(artists.size() - 10)),
                new Track().setName("10").setArtist(artists.get(artists.size() - 11)),
                new Track().setName("11").setArtist(artists.get(artists.size() - 12)),
                new Track().setName("12").setArtist(artists.get(artists.size() - 13)),
                new Track().setName("13").setArtist(artists.get(artists.size() - 14)),
                new Track().setName("14").setArtist(artists.get(artists.size() - 15))
        );
    }

    public List<Artist> getArtists() {
        return List.of(
                new Artist().setName("0"),
                new Artist().setName("1"),
                new Artist().setName("2"),
                new Artist().setName("3"),
                new Artist().setName("4"),
                new Artist().setName("5"),
                new Artist().setName("6"),
                new Artist().setName("7"),
                new Artist().setName("8"),
                new Artist().setName("9"),
                new Artist().setName("10"),
                new Artist().setName("11"),
                new Artist().setName("12"),
                new Artist().setName("13"),
                new Artist().setName("14")
        );
    }

    public List<Value> getValues() {
        return getNumbers().stream().map(Value::new).collect(Collectors.toList());
    }

    public List<Integer> getNumbers() {
        return List.of(3, 4, 5);
    }
}

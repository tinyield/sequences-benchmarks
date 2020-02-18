package benchmarks;

import static java.lang.String.format;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.javatuples.Triplet;

import model.artist.Artist;
import model.country.Country;
import model.track.Track;

public class SequenceBenchmarkUtils {

    static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    static void assertBenchmarkValidity() {
        System.out.println();
        System.out.println("Checking for test validity");
        List<Triplet<Country, Artist, Track>> jayieldTriplets = JayieldBenchmark.query().toList();
        List<Triplet<Country, Artist, Track>> streamTriplets = StreamBenchmark.query().collect(Collectors.toList());
        List<Triplet<Country, Artist, Track>> streamUtilsTriplets = StreamUtilsBenchmark.query().collect(Collectors.toList());
        List<Triplet<Country, Artist, Track>> guavaTriplets = GuavaBenchmark.query().collect(Collectors.toList());
        List<Triplet<Country, Artist, Track>> streamWithIteratorTriplets = StreamWithIteratorBenchmark.query().collect(Collectors.toList());
//        System.out.println("jayieldTriplets");
//        jayieldTriplets.forEach(System.out::println);
//        System.out.println("streamTriplets");
//        streamTriplets.forEach(System.out::println);
        int size = jayieldTriplets.size();
        if(size != streamTriplets.size() || size != streamUtilsTriplets.size() || size != guavaTriplets.size() || size != streamWithIteratorTriplets.size()) {
            throw new RuntimeException("query results are not the same");
        }

        jayieldTriplets.forEach(triplet -> {
            if(!streamTriplets.contains(triplet)) {
                throw new RuntimeException(format("query results are not the same, could not find triplet %s in streams results", triplet));
            }
            if(!streamUtilsTriplets.contains(triplet)) {
                throw new RuntimeException(format("query results are not the same, could not find triplet %s in StreamUtils results", triplet));
            }
            if(!guavaTriplets.contains(triplet)) {
                throw new RuntimeException(format("query results are not the same, could not find triplet %s in guava results", triplet));
            }
            if(!streamWithIteratorTriplets.contains(triplet)) {
                throw new RuntimeException(format("query results are not the same, could not find triplet %s in stream with iterator results", triplet));
            }
        });
        System.out.println("Test is valid");
    }
}

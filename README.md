# Glossary
1. [Overview](#overview)
2. [Usage](#usage)
3. [Benchmarks Overview](#benchmarks-overview)
    1. [All Match](#all-match)
    2. [Every](#every)
    3. [Find](#find)
    4. [First](#find-first)
    5. [Zip](#zip)
    6. [Distinct Top Artist and Top Track by Country Benchmark](#distinct-top-artist-and-top-track-by-country-benchmark)
    7. [Artists who are in a Country's top ten who also have Tracks in the same Country's top ten Benchmark](#artists-who-are-in-a-countrys-top-ten-who-also-have-tracks-in-the-same-countrys-top-ten-benchmark)
4. [Performance Comparison](#performance-comparison)
5. [Article](#article)

# Overview
In this document you'll find information on how to use the project 
in terms of how to run it in your local machine, a brief description
of each benchmark's pipeline, and a performance comparison in speedup
relative to Java's Stream API.

# Usage
To run these benchmarks on you local machine just run:
```
mvn clean install
```
And then:
```
java -jar target/benchmarks.jar
```
# Benchmarks Overview
### All Match - `allMatch(sequence, predicate)`
Benchmarks the `allMatch()` operation in the different sequence types.

Collection Sizes: [10, 1000, 100 000]

Pipeline:
```ignorelang
sequenceOfEvenIntegers -> allMatch(isEven)
```
### Every - `every(sequence1, sequence2, predicate)`
Every is an operation that, based on a user defined predicate, tests if all the
elements of a sequence match between corresponding positions. So for instance, if we have:
```java
Stream<String> seq1 = Stream.of("505", "Amsterdam", "Mural");
Stream<String> seq2 = Stream.of("505", "Amsterdam", "Mural");
Stream<String> seq3 = Stream.of("Mural", "Amsterdam", "505");
BiPredicate<String, String> pred = (s1, s2) -> s1.equals(s2);
```
Then:
```java
every(seq1, seq2, pred); // returns true
every(seq1, seq3, pred); // returns false
```
For `every` to return true, every element of each sequence must match in the same index.
The `every` feature can be achieved through a pipeline of `zip`-`allMatch` operations,
such as:
```java
seq1.zip(seq2, pred::test).allMatch(Boolean.TRUE::equals);
```

Benchmarked for: [Class, Integer, String, Randomly generated String]  
Collection Sizes: [10, 1000, 100 000]

Pipeline:
```ignorelang
sourceSequence + copyOfSourceSequence -> zip(Object::Equals) -> allMatch(Boolean.TRUE::equals)
```
### Find - `find(sequence1, sequence2, predicate)`
The `find` between two sequences is an operation that, based on a user defined
predicate, finds two elements that match, returning one of them in the process.
So if we have:
```java
Stream<String> seq1 = Stream.of("505", "Amsterdam", "Mural");
Stream<String> seq2 = Stream.of("400", "Amsterdam", "Stricken");
Stream<String> seq3 = Stream.of("Amsterdam", "505", "Stricken");
BiPredicate<String, String> pred = (s1, s2) -> s1.equals(s2);
```
Then:
```java
find(seq1, seq2, pred); // returns "Amsterdam"
find(seq1, seq3, pred); // returns null
find(seq2, seq3, pred); // returns "Stricken"
```
For `find` to return an element, two elements of each sequence must match in the
same index. Here's what an implementation of `find` would look like with the support 
of a `zip`:
```java
zip(seq1, seq2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
    .filter(Objects::nonNull)
    .findFirst()
    .orElse(null);
}
```

Benchmarked for: [Class, Integer, String]  
Collection Sizes: [10, 1000, 100 000]  
This pipeline has two ways of determining which element will be the matching element:
1. For String sequences, the matching element will be on a fixed index, using the 
following criteria:
    * For collections with more than 100 elements, the matching index will correspond
     to COLLECTION_SIZE / 100.  
      (e.g: for a COLLECTION_SIZE of 100K the pipeline will match with the 1000th element)
      
    * Otherwise, the matching index will correspond to COLLECTION_SIZE / 10.  
    (e.g: for a COLLECTION_SIZE of 100 elements the pipeline will match with the 10th element)
2. For all sequences (including String), the match index increments with each iteration
to make sure there are matches in every index of the sequence.  
(e.g: On the 1st iteration the match index will be 0, on the 2nd it will be 1, etc... )

Pipeline:
```ignorelang
sourceSequence1 + sourceSequence2 -> 
-> zip((e1, e2) -> Object.equals(e1, e2) ? t1 : null) -> 
-> filter(Objects::nonNull) -> 
-> findFirst()
```
### First - `first(sequence, predicate)`
Benchmarks the usage of the `findFirst()` operator. This benchmark was run 
with three types of Sequences:
1. One where the match would be found in the first element.
1. One where the match would be found in the middle.
1. One where the match would be found in the last element.

Collection Sizes: [10, 1000, 100 000]

Pipeline:
```ignorelang
sequenceOfAllButOneEvenIntegers -> filter(isOdd) -> findFirst()
```
### Zip Primes with Values - `zip(primes, values)`
Benchmarks _zipping_ a sequence of prime Integers with instances of the class Value.

Collection Sizes: [10, 1000, 100 000]

Pipeline:
```ignorelang
(sequenceOfIntegers -> filter(isPrime)) + sequenceOfValues -> zip(Pair::with) -> forEach(bh::consume)
```
### Distinct Top Artist and Top Track by Country Benchmark - `zip(artists, tracks)`
Benchmarks creating two different sequences, one consisting of the top 50 Artists 
(provided by [Last.fm](https://www.last.fm/api/)) of each non english speaking 
country (provided by [REST Countries](https://restcountries.eu/)) and the other
the exact same thing but for the top 50 Tracks.
Then _zipping_ both sequences into a Trio of Country, First Artist and First Track and
retrieving the distinct elements by Artist.

Pipelines:
* Sequence of Artists:
```ignorelang
sequenceOfCountries -> filter(isNonEnglishSpeaking) -> filter(hasArtists) -> map(Pair.of(country, artists));
```

* Sequence of Tracks:
```ignorelang
sequenceOfCountries -> filter(isNonEnglishSpeaking) -> filter(hasTracks) -> map(Pair.of(country, tracks));
```
* Pipeline
```ignorelang
sequenceOfArtists + sequenceOfTracks -> 
-> zip(Trio.of(country, topArtist, topTrack)) -> 
-> distinctBy(artist) -> 
-> forEach(bh::consume)
```
### Artists who are in a Country's top ten who also have Tracks in the same Country's top ten Benchmark - `zip(artists, tracks)`
Benchmarks creating two different sequences, one consisting of the top 50 Artists 
(provided by [Last.fm](https://www.last.fm/api/)) of each non english speaking 
country (provided by [REST Countries](https://restcountries.eu/)) and the other
the exact same thing but for the top 50 Tracks.
Then, for each Country, we take the top ten Artists and top ten Track artist's 
names and zip them into a Trio. After, the top ten artists are filtered by their 
presence in the top ten Track artist's name, returning a Pair with the Country 
and the resulting Sequence.

Pipelines:
* Sequence of Artists:
```ignorelang
sequenceOfCountries -> filter(isNonEnglishSpeaking) -> filter(hasArtists) -> map(Pair.of(country, artists));
```

* Sequence of Tracks:
```ignorelang
sequenceOfCountries -> filter(isNonEnglishSpeaking) -> filter(hasTracks) -> map(Pair.of(country, tracks));
```
* Pipeline
```ignorelang
sequenceOfArtists + sequenceOfTracks -> 
-> zip(Trio.of(country, artists, tracks)) -> 
-> map(Pair.of(country, artists)) -> 
-> forEach(bh::consume)
```

# Performance Comparison
The results presented here were based on the results attained from Github Actions,
they are presented in relation to Java's Stream performance. For the actual results
check the [Github Actions Section](https://github.com/tinyield/sequences-benchmarks/actions).

**Notes:**
* Java's Stream performance is equivalent to 1, all results are presented in relation to it
* For Pipelines where collection sizes vary, only 1k results and 100k results will be
displayed, separated in a pipe format, like so:
  * 1K results | 100K results

### Benchmarks with one Sequence
<table>
    <thead>
        <tr>
            <th>Benchmark</th>
            <th>Time Complexity</th>
            <comment></comment>
            <th>Kotlin Sequence</th>
            <comment></comment>
            <th>Jayield</th>
            <th>StreamEx</th>
            <th>jOOλ</th>
            <th>Vavr</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td class="Benchmark">All Match</td>
            <td class="TimeComplexity">Linear</td>
            <comment></comment>
            <td class="Kotlin Sequence">1.7 | 2.8</td>
            <comment></comment>
            <td class="Jayield">3.7 | 5.6</td>
            <td class="StreamEx">0.7 | 0.9</td>
            <td class="jOOλ">0.6 | 1.0</td>
            <td class="Vavr">0.1 | 0.1</td>
        </tr>
        <tr>
            <td class="Benchmark">First in the Beginning</td>
            <td class="TimeComplexity">Constant</td>
			<comment></comment>
			<td class="Kotlin Sequence">11.6 | 11.4</td>
			<comment></comment>
			<td class="Jayield">0.4 | 0.4</td>
			<td class="StreamEx">0.9 | 0.9</td>
			<td class="jOOλ">0.5 | 0.5</td>
			<td class="Vavr">0.9 | 0.9</td>
        </tr>
        <tr>
            <td class="Benchmark">First in the Middle</td>
            <td class="TimeComplexity">Linear</td>
			<comment></comment>
			<td class="Kotlin Sequence">3.8 | 7.1</td>
			<comment></comment>
			<td class="Jayield">0.7 | 1.3</td>
			<td class="StreamEx">0.5 | 1.0</td>
			<td class="jOOλ">1.0 | 1.0</td>
			<td class="Vavr">0.1 | 0.1</td>
        </tr>
        <tr>
            <td class="Benchmark">First in the End</td>
            <td class="TimeComplexity">Linear</td>
			<comment></comment>
			<td class="Kotlin Sequence">4.4 | 8.9</td>
			<comment></comment>
			<td class="Jayield">0.8 | 1.2</td>
			<td class="StreamEx">0.6 | 1.0</td>
			<td class="jOOλ">0.8 | 1.0</td>
			<td class="Vavr">0.1 | 0.1</td>
        </tr>
    </tbody>
</table>

### Benchmarks with two Sequences
<table>
    <thead>
        <tr>
            <th>Benchmark</th>
            <th>Time Complexity</th>
            <comment></comment>
            <th>Zipline</th>
            <comment></comment>
            <th>Kotlin Sequence</th>
            <comment></comment>
            <th>Jayield</th>
            <th>StreamEx</th>
            <th>jOOλ</th>
            <th>Vavr</th>
            <comment></comment>
            <th>Guava over Stream</th>
            <th>Protonpack over Stream</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td class="Benchmark">Every String</td>
            <td class="TimeComplexity">Linear</td>
            <comment></comment>
			<td class="Zipline">1.2 | 1.2</td>
			<comment></comment>
			<td class="Kotlin Sequence">3.7 | 3.9</td>
			<comment></comment>
			<td class="Jayield">8.4 | 4.0</td>
			<td class="StreamEx">1.3 | 1.4</td>
			<td class="jOOλ">0.7 | 0.8</td>
			<td class="Vavr">0.1 | 0.1</td>
			<comment></comment>
			<td class="Guava">1.1 | 1.1</td>
			<td class="Protonpack">1.1 | 1.1</td>
        </tr>
        <tr>
            <td class="Benchmark">Every Integer</td>
            <td class="TimeComplexity">Linear</td>
            <comment></comment>
			<td class="Zipline">1.5 | 1.7</td>
			<comment></comment>
			<td class="Kotlin Sequence">5.7 | 6.0</td>
			<comment></comment>
			<td class="Jayield">1.0 | 5.6</td>
			<td class="StreamEx">1.4 | 1.3</td>
			<td class="jOOλ">0.8 | 0.8</td>
			<td class="Vavr">0.1 | 0.1</td>
			<comment></comment>
			<td class="Guava">1.0 | 1.0</td>
			<td class="Protonpack">1.0 | 0.8</td>
        </tr>
        <tr>
            <td class="Benchmark">Every Random String</td>
            <td class="TimeComplexity">Linear</td>
            <comment></comment>
			<td class="Zipline">1.3 | 1.5</td>
			<comment></comment>
			<td class="Kotlin Sequence">4.4 | 3.5</td>
			<comment></comment>
			<td class="Jayield">7.9 | 7.0</td>
			<td class="StreamEx">1.3 | 1.7</td>
			<td class="jOOλ">0.8 | 0.9</td>
			<td class="Vavr">0.1 | 0.1</td>
			<comment></comment>
			<td class="Guava">1.2 | 1.0</td>
			<td class="Protonpack">1.2 | 2.3</td>
        </tr>
        <tr>
            <td class="Benchmark">Find String</td>
            <td class="TimeComplexity">Linear</td>
            <comment></comment>
			<td class="Zipline">1.1 | 1.0</td>
			<comment></comment>
			<td class="Kotlin Sequence">2.0 | 1.4</td>
			<comment></comment>
			<td class="Jayield">2.2 | 1.7</td>
			<td class="StreamEx">1.1 | 1.1</td>
			<td class="jOOλ">0.8 | 0.9</td>
			<td class="Vavr">0.2 | 0.1</td>
			<comment></comment>
			<td class="Guava">1.1 | 0.9</td>
			<td class="Protonpack">1.1 | 1.5</td>
        </tr>
        <tr>
            <td class="Benchmark">Find Integer</td>
            <td class="TimeComplexity">Linear</td>
            <comment></comment>
			<td class="Zipline">1.2 | 1.1</td>
			<comment></comment>
			<td class="Kotlin Sequence">3.3 | 3.7</td>
			<comment></comment>
			<td class="Jayield">5.1 | 7.4</td>
			<td class="StreamEx">1.3 | 1.2</td>
			<td class="jOOλ">0.8 | 0.8</td>
			<td class="Vavr">0.1 | 0.1</td>
			<comment></comment>
			<td class="Guava">1.1 | 1.0</td>
			<td class="Protonpack">1.2 | 1.0</td>
        </tr>
        <tr>
            <td class="Benchmark">Find Fixed Index</td>
            <td class="TimeComplexity">Constant</td>
            <comment></comment>
			<td class="Zipline">1.1 | 0.9</td>
			<comment></comment>
			<td class="Kotlin Sequence">1.9 | 2.7</td>
			<comment></comment>
			<td class="Jayield">1.4 | 5.3</td>
			<td class="StreamEx">1.2 | 1.3</td>
			<td class="jOOλ">0.7 | 0.8</td>
			<td class="Vavr">0.2 | 0.1</td>
			<comment></comment>
			<td class="Guava">1.0 | 0.9</td>
			<td class="Protonpack">1.2 | 1.0</td>
        </tr>
        <tr>
            <td class="Benchmark">Zip Primes with Values</td>
            <td class="TimeComplexity">Linear</td>
            <comment></comment>
            <td class="Zipline">1.2 | 1.2</td>
            <comment></comment>
            <td class="Kotlin Sequence">1.6 | 1.3</td>
            <comment></comment>
            <td class="Jayield">1.6 | 1.2</td>
            <td class="StreamEx">0.9 | 1.1</td>
            <td class="jOOλ">0.9 | 1.1</td>
            <td class="Vavr">0.3 | 0.7</td>
            <comment></comment>
            <td class="Guava">0.9 | 1.1</td>
            <td class="Protonpack">1.0 | 1.0</td>
        </tr>
        <tr>
            <td class="Benchmark">Zip Top Artist & Track(1)</td>
            <td class="TimeComplexity">Linear</td>
            <comment></comment>
            <td class="Zipline">1.1</td>
            <comment></comment>
            <td class="Kotlin Sequence">2.0</td>
            <comment></comment>
            <td class="Jayield">1.1</td>
            <td class="StreamEx">0.9</td>
            <td class="jOOλ">0.8</td>
            <td class="Vavr">0.6</td>
            <comment></comment>
            <td class="Guava">1.0</td>
            <td class="Protonpack">1.0</td>
        </tr>
        <tr>
            <td class="Benchmark">Zip Artists in Top10(2)</td>
            <td class="TimeComplexity">Linear</td>
            <comment></comment>
            <td class="Zipline">1.1</td>
            <comment></comment>
            <td class="Kotlin Sequence">1.5</td>
            <comment></comment>
            <td class="Jayield">1.3</td>
            <td class="StreamEx">0.9</td>
            <td class="jOOλ">0.8</td>
            <td class="Vavr">0.4</td>
            <comment></comment>
            <td class="Guava">1.1</td>
            <td class="Protonpack">1.0</td>
        </tr>
    </tbody>
</table>

(1) Corresponds to "Distinct Top Artist and Top Track by Country Benchmark"  <br>
(2) Corresponds to Artists who are in a Country's top ten who also have Tracks" 
in the same Country's top ten Benchmark

# Article
If you want to read a more in depth analysis on some of these benchmarks, check out our 
[article](https://dzone.com/articles/bridge-the-gap-of-zip-operation) over at [DZONE](https://dzone.com/).


# Glossary
1. [Overview](#overview)
2. [Usage](#usage)
3. [Benchmarks Overview](#benchmarks-overview)
    1. [All Match](#all-match)
    2. [Every](#every)
    3. [Find](#find)
    4. [First](#first)
    5. [FlatMap and Reduce](#flatmap)
    6. [Zip Primes with Values](#primes)
    7. [Distinct Top Artist and Top Track by Country Benchmark](#distinct)
    8. [Artists who are in a Country's top ten who also have Tracks in the same Country's top ten Benchmark](#filter)
    9. [World Weather API Pipelines](#world-weather-api)
        1. [Query number of temperature transitions](#collapse)
        1. [Query max temperature](#max)
        1. [Query number of distinct temperatures](#distinct-temperatures)
4. [Performance Comparison](#performance)
   1. [Benchmarks with one Sequence](#one-sequence)
   1. [Benchmarks with two Sequences](#two-sequences)
5. [Article](#article)

# Overview<a name="overview"></a>
In this document you'll find information on how to use the project 
in terms of how to run it in your local machine, a brief description
of each benchmark's pipeline, and a performance comparison in speedup
relative to Java's Stream API.

# Usage<a name="usage"></a>
To run these benchmarks on you local machine just run:
```
mvn clean install
```
And then:
```
java -jar target/benchmarks.jar
```
# Benchmarks Overview<a name="benchmarks-overview"></a>
### All Match - `allMatch(sequence, predicate)` <a name="all-match"></a>
Benchmarks the `allMatch()` operation in the different sequence types.

Collection Sizes: [10, 1000, 100 000]

Pipeline:
```ignorelang
sequenceOfEvenIntegers -> allMatch(isEven)
```
### Every - `every(sequence1, sequence2, predicate)` <a name="every"></a>
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
### Find - `find(sequence1, sequence2, predicate)` <a name="find"></a>
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
### First - `first(sequence, predicate)` <a name="first"></a>
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

### FlatMap and Reduce <a name="flatmap"></a>
Benchmarks the usage of `flatMap` in conjunction with a `reduce`.

Pipeline:
```ignorelang
nestedSequenceOfIntegers -> flatMap() -> reduce(sum)
```
### [Zip Primes] - Zip Primes with Values - `zip(primes, values)` <a name="primes"></a>
Benchmarks _zipping_ a sequence of prime Integers with instances of the class Value.

Collection Sizes: [10, 1000, 100 000]

Pipeline:
```ignorelang
(sequenceOfIntegers -> filter(isPrime)) + sequenceOfValues -> zip(Pair::with) -> forEach(bh::consume)
```
### [Distinct] - Distinct Top Artist and Top Track by Country Benchmark - `zip(artists, tracks)` <a name="distinct"></a>
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
### [Filter] - Artists who are in a Country's top ten who also have Tracks in the same Country's top ten Benchmark - `zip(artists, tracks)` <a name="filter"></a>
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

### World Weather API Pipelines <a name="world-weather-api"></a>

For these benchmarks we created two custom operations `oddLines` and `collapse`. Both these operations are quite simple
`oddLines` simply lets through elements in odd indexes of the sequence it is applied to, while `collapse` collapses
repeating elements into one, the following snippet exemplifies:

```java
Sequence<String> days = Sequence.of(
  "2020-05-08",
  "2020-05-09",
  "2020-05-10",
  "2020-05-11"
);
Sequence<String> temperatures = Sequence.of( "22ºC", "23ºC", "23ºC", "24ºC", "22ºC", "22ºC", "21ºC" );

days.oddLines().forEach(System.out::println);
// Output
// "2020-05-09"
// "2020-05-11"

temperatures.collapse().forEach(System.out::print);
// Output
// "22ºC", "23ºC", "24ºC", "22ºC", "21ºC"
```

We then queried [WorldWeatherOnline](https://www.worldweatheronline.com/) for the weather of Lisbon, Portugal between the dates of 2020-05-08 and
2020-11-08, which left us with a `.csv` file, that we manipulated with the operations above in three different queries:

1. Query number of temperature transitions
2. Query max temperature
3. Query number of distinct temperatures

In these benchmarks, like the ones from [REST Countries](https://restcountries.eu/) and 
[Last.fm](https://www.last.fm/api/), a part of the pipeline is common between all three. This is due to the fact that 
they all manipulate the same set of data. The next snippet shows the common to part, we first `filter` all the comments 
from the `.csv` file, then `skip` one line that has "Not available" written in it, then we use oddLines to let through 
only the hourly info, and finally map to the temperature on that line.

```java
Sequence.of(weatherData)
    .filter(s -> s.charAt(0) != '#')                    // Filter comments
    .skip(1)                                            // Skip line: Not available
    .oddLines()                                         // Filter hourly info
    .mapToInt(line -> parseInt(line.substring(14, 16))) // Map to Temperature data
```

From here on out these three queries are quite easy to explain.

#### [Collapse] - Query number of temperature transitions <a name="collapse"></a>

Benchmarks querying the number of temperature transitions we simply take the common part of the pipeline and add the `collapse`
operation, leaving us with all the transitions, followed by a `count`.

```java
Sequence.of(weatherData)
    .filter(s -> s.charAt(0) != '#')                    // Filter comments
    .skip(1)                                            // Skip line: Not available
    .oddLines()                                         // Filter hourly info
    .mapToInt(line -> parseInt(line.substring(14, 16))) // Map to Temperature data
    .collapse()
    .count();
```

#### [Max] - Query max temperature <a name="max"></a>

Benchmarks retrieving the highest temperature in the data set by adding the operation `max` to the common part of the pipeline.

```java
Sequence.of(weatherData)
    .filter(s -> s.charAt(0) != '#')                    // Filter comments
    .skip(1)                                            // Skip line: Not available
    .oddLines()                                         // Filter hourly info
    .mapToInt(line -> parseInt(line.substring(14, 16))) // Map to Temperature data
    .max()
```

#### [Distinct Temperatures] - Query number of distinct temperatures <a name="distinct-temperatures"></a>

Benchmark querying the distinct number of temperatures by applying `distinct` to common part of the pipeline, followed by a `count`.

```java
Sequence.of(weatherData)
    .filter(s -> s.charAt(0) != '#')                    // Filter comments
    .skip(1)                                            // Skip line: Not available
    .oddLines()                                         // Filter hourly info
    .mapToInt(line -> parseInt(line.substring(14, 16))) // Map to Temperature data
    .distinct()
    .count()
```


# Performance Comparison <a name="performance"></a>
The results presented here were based on the results attained from Github Actions,
they are presented in relation to Java's Stream performance. For the actual results
check the [Github Actions Section](https://github.com/tinyield/sequences-benchmarks/actions).

**Notes:**
* Java's Stream performance is equivalent to 1, all results are presented in relation to it
* For Pipelines where collection sizes vary, only 1k results and 100k results will be
displayed, separated in a pipe format, like so:
  * 1K results&nbsp;|&nbsp;100K results

### Benchmarks with one Sequence <a name="one-sequence"></a>
<table>
    <thead>
    <tr>
        <th>Benchmark</th>
        <th>Time&nbsp;Complexity</th>
        <th>Jayield</th>
        <th>Sek</th>
        <th>Kotlin Sequence</th>
        <th>Eclipse Collections</th>
        <th>jOOλ</th>
        <th>StreamEx</th>
        <th>Vavr</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>All Match</td>
        <td>Linear</td>
        <td>2.4 | 2.4</td>
        <td>1.7 | 1.6</td>
        <td>2.8 | 2.4</td>
        <td>1.6 | 1.9</td>
        <td>1.1 | 1.0</td>
        <td>1.0 | 0.9</td>
        <td>0.0 | 0.0</td>
    </tr>
    <tr>
        <td>Collapse</td>
        <td>Linear</td>
        <td>3.5</td>
        <td>1.5</td>
        <td>2.2</td>
        <td>1.2</td>
        <td>1.0</td>
        <td>0.4</td>
        <td>0.4</td>
    </tr>
    <tr>
        <td>First In the Beginning</td>
        <td>Constant</td>
        <td>1.3 | 1.3</td>
        <td>2.7 | 2.8</td>
        <td>11.2 | 11.8</td>
        <td>0.1 | 0.0</td>
        <td>0.5 | 0.5</td>
        <td>1.1 | 1.0</td>
        <td>1.9 | 1.9</td>
    </tr>
    <tr>
        <td>First In the End</td>
        <td>Linear</td>
        <td>0.5 | 0.6</td>
        <td>4.3 | 4.0</td>
        <td>5.7 | 7.1</td>
        <td>3.0 | 3.0</td>
        <td>1.0 | 1.0</td>
        <td>0.9 | 1.1</td>
        <td>0.1 | 0.1</td>
    </tr>
    <tr>
        <td>First In the Middle</td>
        <td>Linear</td>
        <td>0.6 | 0.6</td>
        <td>4.0 | 5.1</td>
        <td>6.1 | 7.1</td>
        <td>2.7 | 2.0</td>
        <td>1.0 | 1.0</td>
        <td>0.9 | 1.0</td>
        <td>0.1 | 0.1</td>
    </tr>
    <tr>
        <td>FlatMap and Reduce</td>
        <td>Linear</td>
        <td>1.1 | 1.1</td>
        <td>1.1 | 1.3</td>
        <td>1.2 | 1.4</td>
        <td>0.6 | 0.6</td>
        <td>0.4 | 0.5</td>
        <td>1.1 | 1.3</td>
        <td>0.1 | 0.1</td>
    </tr>
    <tr>
        <td>Max</td>
        <td>Linear</td>
        <td>1.3</td>
        <td>0.5</td>
        <td>0.7</td>
        <td>0.5</td>
        <td>0.5</td>
        <td>0.2</td>
        <td>0.1</td>
    </tr>
    <tr>
        <td>Distinct Temperatures</td>
        <td>Linear</td>
        <td>1.2</td>
        <td>0.6</td>
        <td>0.8</td>
        <td>0.6</td>
        <td>0.9</td>
        <td>0.2</td>
        <td>0.2</td>
    </tr>
    </tbody>
</table>

### Benchmarks with two Sequences <a name="two-sequences"></a>
<table>
    <thead>
    <tr>
        <th>Benchmark</th>
        <th>Time Complexity</th>
        <th>Zipline</th>
        <th>Jayield</th>
        <th>Sek</th>
        <th>Kotlin Sequence</th>
        <th>Eclipse Collections</th>
        <th>jOOλ</th>
        <th>StreamEx</th>
        <th>Vavr</th>
        <th>Guava over Stream</th>
        <th>Protonpack over Stream</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>Every Class</td>
        <td>Linear</td>
        <td>1.4 | 1.2</td>
        <td>6.7 | 1.2</td>
        <td>4.6 | 2.2</td>
        <td>5.0 | 2.5</td>
        <td>4.6 | 3.9</td>
        <td>0.7 | 1.0</td>
        <td>1.4 | 1.1</td>
        <td>0.1 | 0.2</td>
        <td>1.0 | 1.0</td>
        <td>2.0 | 1.4</td>
    </tr>
    <tr>
        <td>Every Integer</td>
        <td>Linear</td>
        <td>1.4 | 1.2</td>
        <td>6.5 | 1.2</td>
        <td>5.2 | 4.1</td>
        <td>5.1 | 4.2</td>
        <td>4.1 | 4.0</td>
        <td>0.8 | 0.7</td>
        <td>1.4 | 1.4</td>
        <td>0.1 | 0.1</td>
        <td>1.0 | 1.1</td>
        <td>2.3 | 2.2</td>
    </tr>
    <tr>
        <td>Every Random String</td>
        <td>Linear</td>
        <td>1.3 | 1.3</td>
        <td>5.3 | 1.2</td>
        <td>4.9 | 3.9</td>
        <td>5.1 | 3.0</td>
        <td>4.3 | 2.6</td>
        <td>0.8 | 0.6</td>
        <td>1.4 | 1.3</td>
        <td>0.1 | 0.1</td>
        <td>1.1 | 1.1</td>
        <td>1.3 | 1.1</td>
    </tr>
    <tr>
        <td>Every String</td>
        <td>Linear</td>
        <td>1.2 | 1.3</td>
        <td>6.2 | 1.2</td>
        <td>4.6 | 4.1</td>
        <td>4.7 | 2.7</td>
        <td>4.0 | 2.6</td>
        <td>0.7 | 0.8</td>
        <td>1.3 | 1.5</td>
        <td>0.1 | 0.1</td>
        <td>1.0 | 1.1</td>
        <td>1.2 | 1.1</td>
    </tr>
    <tr>
        <td>Find Class</td>
        <td>Linear</td>
        <td>1.2 | 1.0</td>
        <td>0.6 | 1.2</td>
        <td>3.3 | 1.3</td>
        <td>3.1 | 1.3</td>
        <td>1.3 | 1.4</td>
        <td>0.8 | 0.8</td>
        <td>1.3 | 1.0</td>
        <td>0.1 | 0.5</td>
        <td>1.1 | 1.0</td>
        <td>1.1 | 1.4</td>
    </tr>
    <tr>
        <td>Find Fixed Index</td>
        <td>Constant</td>
        <td>1.1 | 1.1</td>
        <td>0.7 | 0.7</td>
        <td>2.9 | 4.0</td>
        <td>3.4 | 3.1</td>
        <td>0.3 | 0.2</td>
        <td>0.6 | 0.8</td>
        <td>1.4 | 1.2</td>
        <td>0.2 | 0.1</td>
        <td>1.0 | 1.0</td>
        <td>1.4 | 1.1</td>
    </tr>
    <tr>
        <td>Find Integer</td>
        <td>Linear</td>
        <td>1.1 | 1.1</td>
        <td>0.6 | 0.8</td>
        <td>4.1 | 2.1</td>
        <td>4.6 | 2.2</td>
        <td>1.4 | 1.3</td>
        <td>0.7 | 0.9</td>
        <td>1.4 | 1.2</td>
        <td>0.1 | 0.4</td>
        <td>1.0 | 1.0</td>
        <td>2.1 | 1.3</td>
    </tr>
    <tr>
        <td>Find String</td>
        <td>Linear</td>
        <td>1.0 | 1.0</td>
        <td>0.5 | 1.2</td>
        <td>2.9 | 1.2</td>
        <td>2.7 | 1.4</td>
        <td>1.0 | 1.6</td>
        <td>0.8 | 0.9</td>
        <td>1.3 | 1.0</td>
        <td>0.1 | 0.5</td>
        <td>1.0 | 1.0</td>
        <td>0.8 | 1.5</td>
    </tr>
    <tr>
        <td>Filter</td>
        <td>Linear</td>
        <td>1.0</td>
        <td>1.0</td>
        <td>1.3</td>
        <td>1.2</td>
        <td>1.3</td>
        <td>0.8</td>
        <td>0.9</td>
        <td>0.4</td>
        <td>1.0</td>
        <td>1.0</td>
    </tr>
    <tr>
        <td>Zip Primes</td>
        <td>Linear</td>
        <td>1.4 | 1.0</td>
        <td>1.4 | 1.1</td>
        <td>1.5 | 1.2</td>
        <td>1.3 | 1.1</td>
        <td>1.7 | 1.1</td>
        <td>1.0 | 1.0</td>
        <td>1.1 | 1.1</td>
        <td>0.3 | 0.7</td>
        <td>1.0 | 1.1</td>
        <td>1.1 | 1.0</td>
    </tr>
    <tr>
        <td>Distinct</td>
        <td>Linear</td>
        <td>1.1</td>
        <td>1.3</td>
        <td>1.9</td>
        <td>1.9</td>
        <td>1.2</td>
        <td>0.8</td>
        <td>0.9</td>
        <td>0.7</td>
        <td>1.0</td>
        <td>1.0</td>
    </tr>
    </tbody>
</table>

(1) Corresponds to "Distinct Top Artist and Top Track by Country Benchmark"  <br>
(2) Corresponds to Artists who are in a Country's top ten who also have Tracks" 
in the same Country's top ten Benchmark

# Article <a name="article"></a>
If you want to read a more in depth analysis on some of these benchmarks, check out our 
[article](https://dzone.com/articles/bridge-the-gap-of-zip-operation) over at [DZONE](https://dzone.com/).


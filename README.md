# Glossary
1. [Overview](#overview)
2. [Usage](#usage)
3. [Benchmarks Overview](#benchmarks-overview)
    1. [All Match](#all-match)
    2. [Every](#every)
    3. [Find](#find)
    4. [Find First](#find-first)
    5. [Zip](#zip)
    6. [Distinct Top Artist and Top Track by Country Benchmark](#distinct-top-artist-and-top-track-by-country-benchmark)
    7. [Artists who are in a Country's top ten who also have Tracks in the same Country's top ten Benchmark](#artists-who-are-in-a-countrys-top-ten-who-also-have-tracks-in-the-same-countrys-top-ten-benchmark)
4. [Performance Comparison](#performance-comparison)

# Overview
In this document you'll find information on how to use the project 
in terms of how to run it in your local machine, A brief description
of each Benchmark's pipeline, and a performance comparison in terms of
speedup comparing to Java's Stream API.

# Usage
To use run these benchmarks on you local machine just run:
```
mvn clean install
```
And then:
```
java -jar target/benchmarks.jar
```
# Benchmarks Overview
##### All Match
Benchmarks the `allMatch()` operation in the different sequence types.

Collection Sizes: [10, 1000, 100 000]

Pipeline:
```ignorelang
sourceSequence -> allMatch(...)
```
##### Every
Benchmarks _zipping_ two sequences into Boolean elements and then calling `allMatch()` over the resulting sequence.

Benchmarked for: [Class, Integer, String, Randomly generated String]

Collection Sizes: [10, 1000, 100 000]

Pipeline:
```ignorelang
sourceSequence1 + sourceSequence2 -> zip(...) -> allMatch(...)
```
##### Find
Benchmarks _zipping_ two sequences into Boolean elements and then calling `findFirst()` over the resulting sequence.

Benchmarked for: [Class, Integer, String, String matching in a fixed index]

Collection Sizes: [10, 1000, 100 000]

Pipeline:
```ignorelang
sourceSequence1 + sourceSequence2 -> zip(...) -> filter(...) -> findFirst()
```
##### Find First
Benchmarks the usage of the `findFirst()` operator. This benchmark was run 
with three types of Sequences:
1. One where the match would be found in the first element.
1. One where the match would be found in the middle.
1. One where the match would be found in the last element.

Collection Sizes: [10, 1000, 100 000]

Pipeline:
```ignorelang
sourceSequence -> filter(...) -> findFirst()
```
##### Zip
Benchmarks _zipping_ a sequence of prime Integers with instances of the class Value.

Collection Sizes: [10, 1000, 100 000]

Pipeline:
```ignorelang
sourceSequence1 -> filter(...) -> zip(sourceSequence2) -> forEach(...)
```
##### Distinct Top Artist and Top Track by Country Benchmark
Benchmarks creating two different sequences, one consisting of the top 50 Artists 
(provided by [Last.fm](https://www.last.fm/api/)) of each non english speaking 
country (provided by [REST Countries](https://restcountries.eu/)) and the other
the exact same thing but for the top 50 Tracks.
Then _zipping_ both sequences into a Trio of Country, First Artist and First Track and
retrieving the distinct elements by Artist.

Pipeline:
```ignorelang
sourceSequence1 = data -> filter(...) -> filter(...) -> map(...);
sourceSequence2 = data -> filter(...) -> filter(...) -> map(...);

sourceSequence1 + sourceSequence2 -> zip(...) -> filter(...) -> forEach(...)
```
##### Artists who are in a Country's top ten who also have Tracks in the same Country's top ten Benchmark
Benchmarks creating two different sequences, one consisting of the top 50 Artists 
(provided by [Last.fm](https://www.last.fm/api/)) of each non english speaking 
country (provided by [REST Countries](https://restcountries.eu/)) and the other
the exact same thing but for the top 50 Tracks.
Then, for each Country, we take the top ten Artists and top ten Track artist's 
names and zip them into a Trio. After, the top ten artists are filtered by their 
presence in the top ten Track artist's name, returning a Pair with the Country 
and the resulting Sequence.

Pipeline:
```ignorelang
sourceSequence1 = data -> filter(...) -> filter(...) -> map(...);
sourceSequence2 = data -> filter(...) -> filter(...) -> map(...);

sourceSequence1 + sourceSequence2 -> zip(...) -> map(...) -> forEach(...)
```

# Performance Comparison
The results presented here were retrieved from Github Actions.

[UNDER CONSTRUCTION...]

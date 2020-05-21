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
5. [Article](#article)

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
The results presented here were based on the results attained from Github Actions,
they are presented in relation to Java's Stream performance. For the actual results
check the [Github Actions Section](https://github.com/tinyield/sequences-benchmarks/actions).

<table>
    <thead>
        <tr>
            <th>Benchmark</th>
            <th>Collection Size</th>
            <comment></comment>
            <th>Java Stream</th>
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
            <th>Guava</th>
            <th>Protonpack</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td class="Benchmark">All Match</td>
            <td class="Collection Size">10</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
            <td class="Zipline">-</td>
            <comment></comment>
            <td class="Kotlin Sequence">3.5</td>
            <comment></comment>
            <td class="Jayield">3.3</td>
            <td class="StreamEx">0.8</td>
            <td class="jOOλ">0.6</td>
            <td class="Vavr">0.1</td>
            <comment></comment>
            <td class="Guava">-</td>
            <td class="Protonpack">-</td>
        </tr>
        <tr>
            <td class="Benchmark">All Match</td>
            <td class="Collection Size">1K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
            <td class="Zipline">-</td>
            <comment></comment>
            <td class="Kotlin Sequence">1.7</td>
            <comment></comment>
            <td class="Jayield">3.7</td>
            <td class="StreamEx">0.7</td>
            <td class="jOOλ">0.6</td>
            <td class="Vavr">0.1</td>
            <comment></comment>
            <td class="Guava">-</td>
            <td class="Protonpack">-</td>
        </tr>
        <tr>
            <td class="Benchmark">All Match</td>
            <td class="Collection Size">100K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
            <td class="Zipline">-</td>
            <comment></comment>
            <td class="Kotlin Sequence">2.8</td>
            <comment></comment>
            <td class="Jayield">5.6</td>
            <td class="StreamEx">0.9</td>
            <td class="jOOλ">1</td>
            <td class="Vavr">0.1</td>
            <comment></comment>
            <td class="Guava">-</td>
            <td class="Protonpack">-</td>
        </tr>
        <tr class="Separator"><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
        <tr>
            <td class="Benchmark">Every String</td>
            <td class="Collection Size">10</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">1.3</td>
			<comment></comment>
			<td class="Kotlin Sequence">2.5</td>
			<comment></comment>
			<td class="Jayield">3.7</td>
			<td class="StreamEx">1.2</td>
			<td class="jOOλ">0.6</td>
			<td class="Vavr">0.1</td>
			<comment></comment>
			<td class="Guava">1</td>
			<td class="Protonpack">1.1</td>
        </tr>
        <tr>
            <td class="Benchmark">Every String</td>
            <td class="Collection Size">1K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">1.2</td>
			<comment></comment>
			<td class="Kotlin Sequence">3.7</td>
			<comment></comment>
			<td class="Jayield">8.4</td>
			<td class="StreamEx">1.3</td>
			<td class="jOOλ">0.7</td>
			<td class="Vavr">0.1</td>
			<comment></comment>
			<td class="Guava">1.1</td>
			<td class="Protonpack">1.1</td>
        </tr>
        <tr>
            <td class="Benchmark">Every String</td>
            <td class="Collection Size">100K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">1.2</td>
			<comment></comment>
			<td class="Kotlin Sequence">3.9</td>
			<comment></comment>
			<td class="Jayield">4</td>
			<td class="StreamEx">1.4</td>
			<td class="jOOλ">0.8</td>
			<td class="Vavr">0.1</td>
			<comment></comment>
			<td class="Guava">1.1</td>
			<td class="Protonpack">1.1</td>
        </tr>
        <tr class="Separator"><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
        <tr>
            <td class="Benchmark">Every Integer</td>
            <td class="Collection Size">10</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">1.4</td>
			<comment></comment>
			<td class="Kotlin Sequence">2.5</td>
			<comment></comment>
			<td class="Jayield">1.1</td>
			<td class="StreamEx">1.6</td>
			<td class="jOOλ">0.7</td>
			<td class="Vavr">0.1</td>
			<comment></comment>
			<td class="Guava">1</td>
			<td class="Protonpack">1</td>
        </tr>
        <tr>
            <td class="Benchmark">Every Integer</td>
            <td class="Collection Size">1K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">1.5</td>
			<comment></comment>
			<td class="Kotlin Sequence">5.7</td>
			<comment></comment>
			<td class="Jayield">0.9</td>
			<td class="StreamEx">1.4</td>
			<td class="jOOλ">0.8</td>
			<td class="Vavr">0.1</td>
			<comment></comment>
			<td class="Guava">1</td>
			<td class="Protonpack">1</td>
        </tr>
        <tr>
            <td class="Benchmark">Every Integer</td>
            <td class="Collection Size">100K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">1.7</td>
			<comment></comment>
			<td class="Kotlin Sequence">6</td>
			<comment></comment>
			<td class="Jayield">5.6</td>
			<td class="StreamEx">1.3</td>
			<td class="jOOλ">0.8</td>
			<td class="Vavr">0.1</td>
			<comment></comment>
			<td class="Guava">1</td>
			<td class="Protonpack">0.8</td>
        </tr>
        <tr class="Separator"><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
        <tr>
            <td class="Benchmark">Every Class</td>
            <td class="Collection Size">10</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">1.5</td>
			<comment></comment>
			<td class="Kotlin Sequence">2.4</td>
			<comment></comment>
			<td class="Jayield">1.1</td>
			<td class="StreamEx">1.3</td>
			<td class="jOOλ">0.6</td>
			<td class="Vavr">0.1</td>
			<comment></comment>
			<td class="Guava">1</td>
			<td class="Protonpack">1.4</td>
        </tr>
        <tr>
            <td class="Benchmark">Every Class</td>
            <td class="Collection Size">1K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">1.5</td>
			<comment></comment>
			<td class="Kotlin Sequence">6</td>
			<comment></comment>
			<td class="Jayield">1</td>
			<td class="StreamEx">1.4</td>
			<td class="jOOλ">0.7</td>
			<td class="Vavr">0.1</td>
			<comment></comment>
			<td class="Guava">1</td>
			<td class="Protonpack">1.2</td>
        </tr>
        <tr>
            <td class="Benchmark">Every Class</td>
            <td class="Collection Size">100K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">1.7</td>
			<comment></comment>
			<td class="Kotlin Sequence">4.1</td>
			<comment></comment>
			<td class="Jayield">4.9</td>
			<td class="StreamEx">1.3</td>
			<td class="jOOλ">0.8</td>
			<td class="Vavr">0.1</td>
			<comment></comment>
			<td class="Guava">1</td>
			<td class="Protonpack">1.1</td>
        </tr>
        <tr class="Separator"><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
        <tr>
            <td class="Benchmark">Every Random String</td>
            <td class="Collection Size">10</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">1.3</td>
			<comment></comment>
			<td class="Kotlin Sequence">2.5</td>
			<comment></comment>
			<td class="Jayield">3.6</td>
			<td class="StreamEx">1.1</td>
			<td class="jOOλ">0.6</td>
			<td class="Vavr">0.1</td>
			<comment></comment>
			<td class="Guava">0.9</td>
			<td class="Protonpack">1.1</td>
        </tr>
        <tr>
            <td class="Benchmark">Every Random String</td>
            <td class="Collection Size">1K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">1.3</td>
			<comment></comment>
			<td class="Kotlin Sequence">4.4</td>
			<comment></comment>
			<td class="Jayield">7.9</td>
			<td class="StreamEx">1.3</td>
			<td class="jOOλ">0.8</td>
			<td class="Vavr">0.1</td>
			<comment></comment>
			<td class="Guava">1.2</td>
			<td class="Protonpack">1.2</td>
        </tr>
        <tr>
            <td class="Benchmark">Every Random String</td>
            <td class="Collection Size">100K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">1.5</td>
			<comment></comment>
			<td class="Kotlin Sequence">3.5</td>
			<comment></comment>
			<td class="Jayield">7</td>
			<td class="StreamEx">1.7</td>
			<td class="jOOλ">0.9</td>
			<td class="Vavr">0.1</td>
			<comment></comment>
			<td class="Guava">1</td>
			<td class="Protonpack">2.3</td>
        </tr>
        <tr class="Separator"><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
        <tr>
            <td class="Benchmark">Find String</td>
            <td class="Collection Size">10</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">1</td>
			<comment></comment>
			<td class="Kotlin Sequence">1.4</td>
			<comment></comment>
			<td class="Jayield">1</td>
			<td class="StreamEx">1.1</td>
			<td class="jOOλ">0.6</td>
			<td class="Vavr">0.2</td>
			<comment></comment>
			<td class="Guava">1</td>
			<td class="Protonpack">1.1</td>
        </tr>
        <tr>
            <td class="Benchmark">Find String</td>
            <td class="Collection Size">1K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">1.1</td>
			<comment></comment>
			<td class="Kotlin Sequence">2</td>
			<comment></comment>
			<td class="Jayield">2.2</td>
			<td class="StreamEx">1.1</td>
			<td class="jOOλ">0.8</td>
			<td class="Vavr">0.2</td>
			<comment></comment>
			<td class="Guava">1.1</td>
			<td class="Protonpack">1.1</td>
        </tr>
        <tr>
            <td class="Benchmark">Find String</td>
            <td class="Collection Size">100K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
            <td class="Zipline"></td>
            <comment></comment>
            <td class="Kotlin Sequence"></td>
            <comment></comment>
            <td class="Jayield"></td>
            <td class="StreamEx"></td>
            <td class="jOOλ"></td>
            <td class="Vavr"></td>
            <comment></comment>
            <td class="Guava"></td>
            <td class="Protonpack"></td>
        </tr>
        <tr class="Separator"><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
        <tr>
            <td class="Benchmark">Find Integer</td>
            <td class="Collection Size">10</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">1.1</td>
			<comment></comment>
			<td class="Kotlin Sequence">1.6</td>
			<comment></comment>
			<td class="Jayield">1</td>
			<td class="StreamEx">1.1</td>
			<td class="jOOλ">0.6</td>
			<td class="Vavr">0.2</td>
			<comment></comment>
			<td class="Guava">1</td>
			<td class="Protonpack">1.1</td>
        </tr>
        <tr>
            <td class="Benchmark">Find Integer</td>
            <td class="Collection Size">1K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">1.2</td>
			<comment></comment>
			<td class="Kotlin Sequence">3.3</td>
			<comment></comment>
			<td class="Jayield">5.1</td>
			<td class="StreamEx">1.3</td>
			<td class="jOOλ">0.8</td>
			<td class="Vavr">0.1</td>
			<comment></comment>
			<td class="Guava">1.1</td>
			<td class="Protonpack">1.2</td>
        </tr>
        <tr>
            <td class="Benchmark">Find Integer</td>
            <td class="Collection Size">100K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
            <td class="Zipline"></td>
            <comment></comment>
            <td class="Kotlin Sequence"></td>
            <comment></comment>
            <td class="Jayield"></td>
            <td class="StreamEx"></td>
            <td class="jOOλ"></td>
            <td class="Vavr"></td>
            <comment></comment>
            <td class="Guava"></td>
            <td class="Protonpack"></td>
        </tr>
        <tr class="Separator"><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
        <tr>
            <td class="Benchmark">Find Class</td>
            <td class="Collection Size">10</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">1</td>
			<comment></comment>
			<td class="Kotlin Sequence">1.5</td>
			<comment></comment>
			<td class="Jayield">1</td>
			<td class="StreamEx">1.1</td>
			<td class="jOOλ">0.6</td>
			<td class="Vavr">0.2</td>
			<comment></comment>
			<td class="Guava">1</td>
			<td class="Protonpack">1</td>
        </tr>
        <tr>
            <td class="Benchmark">Find Class</td>
            <td class="Collection Size">1K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">1.1</td>
			<comment></comment>
			<td class="Kotlin Sequence">1.7</td>
			<comment></comment>
			<td class="Jayield">1.8</td>
			<td class="StreamEx">1.2</td>
			<td class="jOOλ">0.8</td>
			<td class="Vavr">0.2</td>
			<comment></comment>
			<td class="Guava">1.1</td>
			<td class="Protonpack">1.1</td>
        </tr>
        <tr>
            <td class="Benchmark">Find Class</td>
            <td class="Collection Size">100K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
            <td class="Zipline"></td>
            <comment></comment>
            <td class="Kotlin Sequence"></td>
            <comment></comment>
            <td class="Jayield"></td>
            <td class="StreamEx"></td>
            <td class="jOOλ"></td>
            <td class="Vavr"></td>
            <comment></comment>
            <td class="Guava"></td>
            <td class="Protonpack"></td>
        </tr>
        <tr class="Separator"><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
        <tr>
            <td class="Benchmark">Find Fixed Index</td>
            <td class="Collection Size">10</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">1.1</td>
			<comment></comment>
			<td class="Kotlin Sequence">1.2</td>
			<comment></comment>
			<td class="Jayield">0.5</td>
			<td class="StreamEx">1</td>
			<td class="jOOλ">0.5</td>
			<td class="Vavr">0.4</td>
			<comment></comment>
			<td class="Guava">0.9</td>
			<td class="Protonpack">1.1</td>
        </tr>
        <tr>
            <td class="Benchmark">Find Fixed Index</td>
            <td class="Collection Size">1K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">1.1</td>
			<comment></comment>
			<td class="Kotlin Sequence">1.9</td>
			<comment></comment>
			<td class="Jayield">1.4</td>
			<td class="StreamEx">1.2</td>
			<td class="jOOλ">0.7</td>
			<td class="Vavr">0.2</td>
			<comment></comment>
			<td class="Guava">1</td>
			<td class="Protonpack">1.2</td>
        </tr>
        <tr>
            <td class="Benchmark">Find Fixed Index</td>
            <td class="Collection Size">100K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">0.9</td>
			<comment></comment>
			<td class="Kotlin Sequence">2.7</td>
			<comment></comment>
			<td class="Jayield">5.3</td>
			<td class="StreamEx">1.3</td>
			<td class="jOOλ">0.8</td>
			<td class="Vavr">0.1</td>
			<comment></comment>
			<td class="Guava">0.9</td>
			<td class="Protonpack">1</td>
        </tr>
        <tr class="Separator"><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
        <tr>
            <td class="Benchmark">Find First in the Beginning</td>
            <td class="Collection Size">10</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">-</td>
			<comment></comment>
			<td class="Kotlin Sequence">11.1</td>
			<comment></comment>
			<td class="Jayield">0.4</td>
			<td class="StreamEx">0.9</td>
			<td class="jOOλ">0.4</td>
			<td class="Vavr">0.8</td>
			<comment></comment>
			<td class="Guava">-</td>
			<td class="Protonpack">-</td>
        </tr>
        <tr>
            <td class="Benchmark">Find First in the Beginning</td>
            <td class="Collection Size">1K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">-</td>
			<comment></comment>
			<td class="Kotlin Sequence">11.6</td>
			<comment></comment>
			<td class="Jayield">0.4</td>
			<td class="StreamEx">0.9</td>
			<td class="jOOλ">0.5</td>
			<td class="Vavr">0.9</td>
			<comment></comment>
			<td class="Guava">-</td>
			<td class="Protonpack">-</td>
        </tr>
        <tr>
            <td class="Benchmark">Find First in the Beginning</td>
            <td class="Collection Size">100K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">-</td>
			<comment></comment>
			<td class="Kotlin Sequence">11.4</td>
			<comment></comment>
			<td class="Jayield">0.4</td>
			<td class="StreamEx">0.9</td>
			<td class="jOOλ">0.5</td>
			<td class="Vavr">0.9</td>
			<comment></comment>
			<td class="Guava">-</td>
			<td class="Protonpack">-</td>
        </tr>
        <tr class="Separator"><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
        <tr>
            <td class="Benchmark">Find First in the Middle</td>
            <td class="Collection Size">10</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">-</td>
			<comment></comment>
			<td class="Kotlin Sequence">7.2</td>
			<comment></comment>
			<td class="Jayield">0.4</td>
			<td class="StreamEx">0.9</td>
			<td class="jOOλ">0.5</td>
			<td class="Vavr">0.2</td>
			<comment></comment>
			<td class="Guava">-</td>
			<td class="Protonpack">-</td>
        </tr>
        <tr>
            <td class="Benchmark">Find First in the Middle</td>
            <td class="Collection Size">1K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">-</td>
			<comment></comment>
			<td class="Kotlin Sequence">3.8</td>
			<comment></comment>
			<td class="Jayield">0.7</td>
			<td class="StreamEx">0.5</td>
			<td class="jOOλ">1</td>
			<td class="Vavr">0.1</td>
			<comment></comment>
			<td class="Guava">-</td>
			<td class="Protonpack">-</td>
        </tr>
        <tr>
            <td class="Benchmark">Find First in the Middle</td>
            <td class="Collection Size">100K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">-</td>
			<comment></comment>
			<td class="Kotlin Sequence">7.1</td>
			<comment></comment>
			<td class="Jayield">1.3</td>
			<td class="StreamEx">1</td>
			<td class="jOOλ">1</td>
			<td class="Vavr">0.1</td>
			<comment></comment>
			<td class="Guava">-</td>
			<td class="Protonpack">-</td>
        </tr>
        <tr class="Separator"><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
        <tr>
            <td class="Benchmark">Find First in the End</td>
            <td class="Collection Size">10</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">-</td>
			<comment></comment>
			<td class="Kotlin Sequence">5.6</td>
			<comment></comment>
			<td class="Jayield">0.4</td>
			<td class="StreamEx">0.9</td>
			<td class="jOOλ">0.5</td>
			<td class="Vavr">0.1</td>
			<comment></comment>
			<td class="Guava">-</td>
			<td class="Protonpack">-</td>
        </tr>
        <tr>
            <td class="Benchmark">Find First in the End</td>
            <td class="Collection Size">1K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
			<td class="Zipline">-</td>
			<comment></comment>
			<td class="Kotlin Sequence">4.4</td>
			<comment></comment>
			<td class="Jayield">0.8</td>
			<td class="StreamEx">0.6</td>
			<td class="jOOλ">0.8</td>
			<td class="Vavr">0.1</td>
			<comment></comment>
			<td class="Guava">-</td>
			<td class="Protonpack">-</td>
        </tr>
        <tr>
            <td class="Benchmark">Find First in the End</td>
            <td class="Collection Size">100K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
            <td class="Zipline">-</td>
            <comment></comment>
            <td class="Kotlin Sequence">8.9</td>
            <comment></comment>
            <td class="Jayield">1.2</td>
            <td class="StreamEx">1</td>
            <td class="jOOλ">1</td>
            <td class="Vavr">0.1</td>
            <comment></comment>
            <td class="Guava">-</td>
            <td class="Protonpack">-</td>
        </tr>
        <tr class="Separator"><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
        <tr>
            <td class="Benchmark">Zip</td>
            <td class="Collection Size">10</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
            <td class="Zipline">1.8</td>
            <comment></comment>
            <td class="Kotlin Sequence">2.3</td>
            <comment></comment>
            <td class="Jayield">2.5</td>
            <td class="StreamEx">1</td>
            <td class="jOOλ">0.7</td>
            <td class="Vavr">0.3</td>
            <comment></comment>
            <td class="Guava">1</td>
            <td class="Protonpack">1</td>
        </tr>
        <tr>
            <td class="Benchmark">Zip</td>
            <td class="Collection Size">1K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
            <td class="Zipline">1.2</td>
            <comment></comment>
            <td class="Kotlin Sequence">1.6</td>
            <comment></comment>
            <td class="Jayield">1.6</td>
            <td class="StreamEx">0.9</td>
            <td class="jOOλ">0.9</td>
            <td class="Vavr">0.3</td>
            <comment></comment>
            <td class="Guava">0.9</td>
            <td class="Protonpack">1</td>
        </tr> 
        <tr>
            <td class="Benchmark">Zip</td>
            <td class="Collection Size">100K</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
            <td class="Zipline">1.2</td>
            <comment></comment>
            <td class="Kotlin Sequence">1.3</td>
            <comment></comment>
            <td class="Jayield">1.2</td>
            <td class="StreamEx">1.1</td>
            <td class="jOOλ">1.1</td>
            <td class="Vavr">0.7</td>
            <comment></comment>
            <td class="Guava">1.1</td>
            <td class="Protonpack">1</td>
        </tr>
        <tr class="Separator"><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
        <tr>
            <td class="Benchmark">Artists who are in a Country's top ten who also have Tracks in the same Country's top ten Benchmark</td>
            <td class="Collection Size">-</td>
            <comment></comment>
            <td class="Java Stream">1</td>
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
            <td class="Protonpack">1</td>
        </tr>
        <tr>
            <td class="Benchmark">Distinct Top Artist and Top Track by Country Benchmark</td>
            <td class="Collection Size">-</td>
            <comment></comment>
            <td class="Java Stream">1</td>
            <comment></comment>
            <td class="Zipline">1.1</td>
            <comment></comment>
            <td class="Kotlin Sequence">2</td>
            <comment></comment>
            <td class="Jayield">1.1</td>
            <td class="StreamEx">0.9</td>
            <td class="jOOλ">0.8</td>
            <td class="Vavr">0.6</td>
            <comment></comment>
            <td class="Guava">1</td>
            <td class="Protonpack">1</td>
        </tr>
    </tbody>
</table>

# Article
If you want to read a more in depth analysis on some of these benchmarks, check out our 
[article](https://dzone.com/articles/bridge-the-gap-of-zip-operation) over at [DZONE](https://dzone.com/).


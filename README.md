# benchtools 

[![Clojars Project](https://img.shields.io/clojars/v/palisades-lakes/bench-tools.svg)](https://clojars.org/palisades-lakes/benchtools)

Shared utilities used by other projects that benchmark
[Clojure](https://clojure.org/) and Java code.

Currently very unstable, not appropriate for use as a library.
The code itself may have examples that are useful starting 
points.


## Building

Builds with [Apache Maven](https://maven.apache.org/), 
specifically the 
[clojure-maven-plugin](https://github.com/talios/clojure-maven-plugin),
eg:

```
mvn clean install
```
Javadoc:
```
mvn javadoc:javadoc
```
Codox:

```
clj.sh src/scripts/clojure/palisades/lakes/bench/doc/codox.clj
```

```
clj.bat src\scripts\clojure\palisades\lakes\bench\doc\codox.clj
```

## Acknowledgments

### ![Yourkit](https://www.yourkit.com/images/yklogo.png)

YourKit is kindly supporting open source projects with its full-featured Java
Profiler.

YourKit, LLC is the creator of innovative and intelligent tools for profiling
Java and .NET applications. Take a look at YourKit's leading software products:

* <a href="http://www.yourkit.com/java/profiler/index.jsp">YourKit Java Profiler</a> and
* <a href="http://www.yourkit.com/.net/profiler/index.jsp">YourKit .NET Profiler</a>.

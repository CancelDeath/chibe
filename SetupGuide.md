If you only want to run ChiBE, then please use the install files at the main page. This page describes how to run ChiBE from the latest sources.

Before starting, please make sure that you have [JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html), [Mercurial](http://mercurial.selenic.com/), and [Maven 3](http://maven.apache.org/download.html) in your system.

## Download sources ##

For anonymous pull:

```
hg clone https://code.google.com/p/chibe/
```

If you plan to contribute:

```
hg clone https://[username]@code.google.com/p/chibe/
```

where `[username`] is your Google username, without brackets.
This will create a directory named `chibe`, containing all the sources.

## Compile ##

Go into the project directory, and tell maven to compile the project.

```
cd chibe
mvn clean compile
```

## Prepare Jar ##

```
mvn assembly:single
```

This will create `ChiBE.jar` under the `target` folder in the project directory.

## Run ChiBE ##

Run ChiBE with the following command.

If using Mac (it should be version 10.6 or higher):

```
java -d32 -XstartOnFirstThread -jar target/ChiBE.jar
```

For other platforms:

```
java -jar target/ChiBE.jar
```

If you plan to load very large graphs, or load expression datasets, then increase the maximum memory as needed.

```
java -Xmx4G -jar target/ChiBE.jar
```

## Contributing ##

You can commit your changes, while in the project directory (`chibe`), with:

```
hg commit
```

This will let you write a commit message, and will commit your changes to your local repository.

If you are a committer to the project, you can push your committed changes to Google repository with:

```
hg push
```
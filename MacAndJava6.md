If you are running the latest 64-bit MacOSX and your Java version is higher than 6, the operating system won't let you run Java in 32-bit mode, which unfortunately is a requirement for running ChiBE.

The solution is to not use the latest Java, but to use Java 6. Most Mac users can do this by modifying the command in the ChiBE.sh file, which is in the installation directory.

Current command in this file is

```
java -Xmx1024M -XstartOnFirstThread -d32 -da -jar ChiBE_macosx.jar
```

The required modification is adding the Java 6 specific location to the "java" command. With computers we tested, the following command worked.

```
export JAVA_HOME=/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home
$JAVA_HOME/bin/java -Xmx1024M -XstartOnFirstThread -d32 -da -jar ChiBE_macosx.jar
```

Users may need to modify the first line if their Java 6 is in a different directory. If Java 6 is not installed by default (we don't know if this ever happens), then you can download from [this page](http://support.apple.com/kb/DL1572).
Tray
====

Tray is a small Java application targeted Windows Systems, that places an icon in the Windows System Tray to show the
current date. Further more it tracks start- and stop times and can show a status based on timestamps.

<p align="center">
  <img src="doc/images/tray.png"/><br>
  <i>Shows icon in the tray showing the date and the popup-menu activaed</i><br><br>
  <img src="doc/images/tracker.png"/><br>
  <i>Shows the time tracker window that calculates times and remaining work-estimates</i><br>
</p>

Features
--------

+ Java application
+ Shows current Date in an icon in the System Tray (Windows Systems)
+ Stamps simple textfiles upon launch and exit (to use for time tracking)
+ Counts hours from the timestamps and show status for the week
+ Advanced wallpaper generation with small calendars and the monthly image from audi.dk calendar

Dependencies
------------

Tray depends on a couple of external components to achieve its functionality.

+ Java Native Access for changing the desktop wallpaper
+ Launch4J (For wrapping the Java Application as an .exe file).


Java Native Access
------------------

Used for calling Win32 API to change the Desktop wallpaper on Windows.

https://github.com/java-native-access/jna

Development
-----------

IntelliJ Project files are not committed to Git. In order to get IntelliJ working with Tray all you should do is create a new plain Java-project in the Tray directory.

+ Change src-folders to include `src/main/java` and `src/main/resources`
+ Change test-folders to include `src/test/java`
+ Change Project compiler output to `build` (F4 > Project)
+ Add `lib\jna-3.2.7.jar` to dependencies (F4 > Modules > Dependencies)
+ Go to a Test, e.g. `src/test/ModelTest` and press Alt+Enter at one of the failing @Test directives and select "Add 'JUnit4' to classpath" (Just use the distribution with IntelliJ)
+ Add build.xml under the Ant tab
+ In the Ant-tab add the `lib/launch4j/Launch4j.jar` as additional classpath to Ant (Alt+Enter, then Additional Classpath Tab)
+ Build the resources.jar (ant target `pack(jar)`)
+ Add the newly build `build\dist\lib\resources.jar` to dependencies (F4 > Modules > Dependencies)
+ Add a Run Configuration for e.g. `TestMain.java` that has a valid directory as working directory. It is valid if it has the a data-directory with the proper structure / content. A good way to do this is to just make a copy of production directory.

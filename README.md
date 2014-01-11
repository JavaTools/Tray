Tray
====

Tray is a small Java application targeted Windows Systems, that places an icon in the Windows System Tray to show the
current date. Further more it tracks start- and stop times and can show a status based on timestamps.

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

+ JDIC (Java Desktop Integration Components). These are for accessing various Desktop
  elements from Java. The System Tray and the possibility to change wallpaper
+ Launch4J (For wrapping the Java Application as an .exe file).


JDIC Integration
----------------

java.library.path should point to a location with the JDIC dll's.

Example VM parameter:

```
-Djava.library.path=D:\Projects\Shell\Modules\Tray\build\dist\lib
```

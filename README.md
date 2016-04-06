Transportr
==========

The public transport companion that respects your privacy and your freedom.
Transportr is a non-profit app developed by people around the world to make using public transport as easy as possible wherever you are. 

[![Transportr Logo](/res/drawable-xhdpi/ic_launcher.png)](http://transportr.grobox.de)
[![Build Status](https://travis-ci.org/grote/Transportr.svg?branch=master)](https://travis-ci.org/grote/Transportr)

Please **[visit the website](http://transportr.grobox.de)** for more information!

If you find any issues with this app, please report them at [the issue tracker](https://github.com/grote/Transportr/issues). Contributions are both encouraged and appreciated. If you like to contribute please [check the website](http://transportr.grobox.de/#contribute) for more information.

The upstream repository is at: https://github.com/grote/Transportr

[![Follow @TransportrApp](art/twitter.png)](https://twitter.com/TransportrApp)
[![Flattr me](art/flattr.png)](https://flattr.com/thing/1921242)

Get Transportr
--------------

[![Available on F-Droid](/art/f-droid.png)](https://f-droid.org/repository/browse/?fdid=de.grobox.liberario)
[![Available on Google Play](/art/google-play.png)](https://play.google.com/store/apps/details?id=de.grobox.liberario)

Pre-releases and beta versions for advanced users are available via [a special F-Droid repository](http://grobox.de/fdroid/).

Screenshots
-----------
[<img src="art/screenshots/drawer.png" width="200">](art/screenshots/drawer.png)
[<img src="art/screenshots/directions.png" width="200">](art/screenshots/directions.png)
[<img src="art/screenshots/trips.png" width="200">](art/screenshots/trips.png)
[<img src="art/screenshots/trip_details.png" width="200">](art/screenshots/trip_details.png)
[<img src="art/screenshots/favorite_trips.png" width="200">](art/screenshots/favorite_trips.png)
[<img src="art/screenshots/departures.png" width="200">](art/screenshots/departures.png)
[<img src="art/screenshots/nearby_stations.png" width="200">](art/screenshots/nearby_stations.png)
[<img src="art/screenshots/nearby_stations_map.png" width="200">](art/screenshots/nearby_stations_map.png)
[<img src="art/screenshots/about.png" width="200">](art/screenshots/about.png)


Building From Source
--------------------

If you want to start working on Transportr and if you haven't done already, you should [familiarize yourself with Android development](https://developer.android.com/training/basics/firstapp/index.html) and [set up a development environment](https://developer.android.com/sdk/index.html).

The next step is to clone the source code repository. Please make sure to also clone the git submodules that contain external libraries.

    $ git submodule init
    $ git submodule update

Then, you also need to run the following command to build the public transport enabler library.

    $ ./pre-build.sh

If you don't want to use an IDE like Android Studio, you can build Transportr on the command line as follows.

    $ gradle assembleRelease

The resulting unsigned apk file will be found at `build/outputs/apk/transportr-release-unsigned.apk`.

License
-------

![GNU GPLv3 Image](https://www.gnu.org/graphics/gplv3-127x51.png)

This program is Free Software: You can use, study share and improve it at your
will. Specifically you can redistribute and/or modify it under the terms of the
[GNU General Public License](https://www.gnu.org/licenses/gpl.html) as
published by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

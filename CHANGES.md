# DateTimeTemplate for Android #

## CHANGELOG ##

1.3.1 (2018-03-11)
==================
* Fixed bug in `format()` not replacing placeholders

1.3.0 (2018-03-11) (broken!)
==================
* Added support for `%Aa%` placeholder

1.2.4 (2018-02-23)
==================
* Added date/time pickers to demo app
* [#10] `%k%` and `%kk%` produced 0 instead of 12 for noon and midnight

v1.2.3 (2017-07-05)
===================
* Updated dependencies

v1.2.2 (2017-05-26)
===================
* Disabled proguard for the library

v1.2.1 (2017-05-25)
===================
* [#6] Fixed proguard config file

v1.2.0 (2017-05-24)
===================
* [#2] Fixed `%mm%` producing wrong results
* [#4] Calendar's TZ was not used for localized placeholders (i.e. day names)

v1.1.0 (2017-03-09)
===================
* You can now pass `Locale` of your choice to `format()` when needed

v1.0.1 (2017-03-08)
===================
* Lowered `minSdk` to `ICE_CREAM_SANDWICH` (`14`)

v1.0.0 (2017-03-08)
===================
* First public release

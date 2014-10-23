# fumbbl-mapper

Ever wondered which countries you have played agains on FUMBBL? Well, here's a little script I wrote to generate a Google map highlighting your opposing countries!

![fumbbl-mapper screenshot](https://raw.githubusercontent.com/roante/fumbbl-mapper/master/res/screenshot.png)

## How to get it?
Simply download the latest [zip here](https://github.com/roante/fumbbl-mapper/tree/master/dist).

## How to run?
Unpack and there's a `run.bat` file. Edit the coachId in the file:

	java -jar fumbbl-mapper-1.0.0-RELEASE.jar -coachId=YOUR_COACH_ID_HERE

And that's all!

Note that you need *Java 8* to run the script.

## How to get your coachId?

Check the following with your coach name:

	https://fumbbl.com/xml:teams?coach=roante

There the first line should tell it to you:

	<teams coach="YOUR_COACH_ID_HERE">
		...
	</teams>


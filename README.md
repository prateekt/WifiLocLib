WifiLocLib
==========

A Wi-Fi Localizer you can use in 5 minutes

See blog post: https://tandonp.wordpress.com/2014/02/13/get-wi-fi-localization-working-in-your-android-app-in-5-minutes/

Making indoor localization based on Wi-Fi as easy as outdoor localization for Android...

Just call:

public void locateMe()

And use a callback to receive the location:

@Override
public void onLocationChanged(String location) {
 
    //display location
    Toast t = Toast.makeText(this.getApplicationContext(), "Location: " + location,Toast.LENGTH_SHORT);
    t.setGravity(Gravity.CENTER, 0, 0);
    t.show();
    Log.d("LOCATION", "LOCATION: " + location);
}

productlinemessages
===================

An example how a product line may be realized with Tapestry5

Start it like this:
```bash
$ mvn jetty:run
```
Then open your browser and go to [the context root](http://localhost:8080/productlinemessages). It will (should) show a generic greeting (i.e. "Welcome") and a "Goodbye". Adding `/de` to the URL will change the greeting to german (i.e. "Willkommen"), but the goodbye is still in english. Adding `/productOne` then says "Willkommen zum ersten Produkt". Removing the `/de` portion or changing it to `/en` will result in "Welcome to the first product". Changing  `/productOne` to `/productTwo` then leads to "Welcome to the second product". With product two you can exchange the language, too. Exciting.

If the default language of your browser is already german, then of course it will show the german messages without a language component in the path. Via adding `/en` you will get greetings in english.

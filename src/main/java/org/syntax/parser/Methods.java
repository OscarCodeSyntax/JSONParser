package org.syntax.parser;

public class Methods {

    public Integer isValidJson (String json) {

        json = json.replaceAll("\\s+","");

        if (json.isEmpty()) {
           return 0;
        }

        if(!validStartAndFinishOfJson(json)) {
            return 2;
        };

        //these are all value to allow for checking.
        Integer quoteNumber = 0;
        boolean previousWasComma = false;
        boolean previousWasColon = false;


        //start and end of json are valid, start from 1 and length-1
        for (int i = 1; i < json.length(); i++) {

            //valid atomic checker

            char c = json.charAt(i);

            //This is the initial check for what is expected at the end (and by proxy, the start)
            // of a valid object. It should only have ',' or '{' after quoteNumber 4 is found.
            if (quoteNumber == 4) {

                boolean isValidCharOnEndOfObject = false;
                if (c == ',') {
                    isValidCharOnEndOfObject = true;

                    //passes to method further down to handle checking the start of valid objects.
                    previousWasComma = true;
                } else if (c == '{') {
                    isValidCharOnEndOfObject = true;
                } else if (c == '}') {
                    isValidCharOnEndOfObject = true;
                }

                if (!isValidCharOnEndOfObject) {
                    //incorrect format - missing , or { at the end.
                    return 5;
                } else {
                    quoteNumber = 0;
                }
            }

            //Handles the expected ':' in the middle.
            //This will have to handle the boolean and integers.
            if (quoteNumber == 2) {

                //this looks to check that the middle ':' is set.
                if(c != ':' && !previousWasColon){
                    return 4;
                }

                //this looks to make sure the value after the middle ':' starts correctly
                //We expect ".
                if(previousWasColon && c != '"'){
                    return 8;
                }
            }

            //This is to cover finding a new object after ',' is found.
            //eg. { "value" : "test" , "value2" : test2" }
            if (previousWasComma) {
                previousWasComma = false;
                if (c != '"') {
                    return 7;
                }
            }

            //adding and variable control to count speechmarks.
            //This, along with current char, allows us to work out what is expected
            //We add to the quote number if its not at 4
            if (c == '"' && quoteNumber != 4) {
                quoteNumber++;
            } else if (c == '"' && quoteNumber == 4) {
                quoteNumber = 0;
            }

            if (c == ':' && !previousWasColon) {
                //missing colon error
                previousWasColon = true;
            } else {
                previousWasColon = false;
            }

        }

        //may note be needed - caught by 7
        if(quoteNumber != 0) {
            //incomplete number of ""
            return 6;
        }

        return 1;


    }

    private boolean validStartAndFinishOfJson (String json) {

            //check the start and end of the object
            if(json.startsWith("{") && json.endsWith("}")) {
                return true;
            }

        return false;
    }
}

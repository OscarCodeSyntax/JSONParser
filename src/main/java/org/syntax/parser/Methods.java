package org.syntax.parser;

class StateTracker {
    int quoteNumber;
    boolean previousWasComma;
    boolean previousWasColon;
    boolean previousWasNumber;
    boolean hasBeenReset;
    int position;
    char currentChar;
    String json;
}

public class Methods {

    public Integer isValidJson(String json) {

        StateTracker stateTracker = new StateTracker();

        stateTracker.quoteNumber = 0;
        stateTracker.previousWasComma = false;
        stateTracker.previousWasColon = false;
        stateTracker.previousWasNumber = false;
        stateTracker.position = 0;
        stateTracker.hasBeenReset = false;

        stateTracker.json = json.replaceAll("\\s+", "");

        if (stateTracker.json.isEmpty()) {
            return 0;
        }

        if (!validStartAndFinishOfJson(stateTracker.json)) {
            return 2;
        }
        ;

        //these are all value to allow for checking.


        //start and end of json are valid, start from 1 and length-1
        for (stateTracker.position = 1; stateTracker.position < stateTracker.json.length(); stateTracker.position++) {

            stateTracker.currentChar = stateTracker.json.charAt(stateTracker.position);

            //new loop means this char wont have been reset anymore.
            stateTracker.hasBeenReset = false;


            //This is to cover finding a new object after ',' is found.
            //eg. { "value" : "test" , "value2" : test2" }
            //Check for this first as this is changed/set later on.
            if (stateTracker.previousWasComma) {
                stateTracker.previousWasComma = false;
                if (stateTracker.currentChar != '"') {
                    return 7;
                }
            }

            //This is the initial check for what is expected at the end (and by proxy, the start)
            // of a valid object. It should only have ',' or '{' after quoteNumber 4 is found.
            if (stateTracker.quoteNumber == 4) {

                boolean isValidCharOnEndOfObject = false;
                if (stateTracker.currentChar == ',') {
                    isValidCharOnEndOfObject = true;

                    //passes to method further down to handle checking the start of valid objects.
                    //do this make sense?
                    stateTracker.previousWasComma = true;
                } else if (stateTracker.currentChar == '{') {
                    isValidCharOnEndOfObject = true;
                } else if (stateTracker.currentChar == '}') {
                    isValidCharOnEndOfObject = true;
                }

                if (!isValidCharOnEndOfObject) {
                    //incorrect format - missing , or { at the end.
                    return 5;
                } else {
                    stateTracker.quoteNumber = 0;
                }
            }

            //Handles the expected ':' in the middle.
            //This will have to handle the boolean and integers.
            if (stateTracker.quoteNumber == 2) {

                //this looks to check that the middle ':' is set.
                if (stateTracker.currentChar != ':' && !stateTracker.previousWasColon && !stateTracker.previousWasNumber) {
                    return 4;
                }

                //this looks to make sure the value after the middle ':' starts correctl

                if (stateTracker.previousWasColon) {
                    boolean isValidValue = true;

                    //got to have to check numbers 1-9 - has to be run on each iteration along an INT
                    //One for true, false and null;
                    //case statement to check that the first value post : is valid.
                    //These are static values
                    switch (stateTracker.currentChar) {
                        case 't':
                            isValidValue = validTrueBooleanValue(stateTracker.json, stateTracker.position);

                            if (!isValidValue) {
                                return 9;
                            }
                            //set stateTracker.currentChar to the char after 'true' and reset position state;
                            resetTrackingValues(stateTracker, 4);
                            break;
                        case 'f':
                            isValidValue = validFalseBooleanValue(stateTracker.json, stateTracker.position);
                            if (!isValidValue) {
                                return 10;
                            }
                            //set stateTracker.currentChar to the char after 'false' and reset position state;
                            //note these can be refactored into a reset method
                            resetTrackingValues(stateTracker, 5);
                            break;

                        case 'n':
                            isValidValue = validNullValue(stateTracker.json, stateTracker.position);
                            if (!isValidValue) {
                                return 11;
                            }
                            //set stateTracker.currentChar to the char after 'null' and reset position state;
                            resetTrackingValues(stateTracker, 4);
                            break;
                        default:
                            //dont think i need anything here
                    }


                    //number values are dynamic
                    if (checkIfValidNumber(stateTracker.currentChar)) {
                        isValidValue = true;
                        stateTracker.previousWasNumber = true;
                        //now that we are in a valid number post ':' 2 - check that this is always the case
                        //set to zero as it will finish on quote number 2 even if valid number
                        stateTracker.quoteNumber = 0;
                    }

                    if (!isValidValue) {
                        return 11;
                    }

                    //loop through following numbers --here

                }
                //We expect " for everything that isnt an edgecase valid object
                //need to add number handling - this is getting triggered on a number
                if (stateTracker.previousWasColon && stateTracker.currentChar != '"' && !checkIfValidNumber(stateTracker.currentChar)) {
                    return 8;
                }
            }

            //this is to handle number values outside of the inital post ':' check.
            if (stateTracker.previousWasNumber) {

                if (!checkIfValidNumber(stateTracker.currentChar)) {
                    //valid end to value pair.
                    if (stateTracker.currentChar == '}' || stateTracker.currentChar == ',') {
                        stateTracker.previousWasNumber = false;
                    } else {
                        return 12;
                    }
                }
            }


            //adding and variable control to count speechmarks.
            //This, along with current char, allows us to work out what is expected
            //We add to the quote number if its not at 4
            if (stateTracker.currentChar == '"' && stateTracker.quoteNumber != 4) {
                stateTracker.quoteNumber++;
            } else if (stateTracker.currentChar == '"' && stateTracker.quoteNumber == 4) {
                stateTracker.quoteNumber = 0;
            }

            if (stateTracker.currentChar == ':' && !stateTracker.previousWasColon) {
                //missing colon error
                stateTracker.previousWasColon = true;
            } else {
                stateTracker.previousWasColon = false;
            }

            //catch the end of a uniue string value eg. 'nulll' or 'truee'
            if (stateTracker.hasBeenReset) {
                if(!checkIfValidValueAfterResetTrackingValues(stateTracker.currentChar )){
                    return 13;
                }
            }

        }

        //may note be needed - caught by 7
        if (stateTracker.quoteNumber != 0) {
            //incomplete number of ""
            return 6;
        }



        return 1;


    }

    private boolean validStartAndFinishOfJson(String json) {

        //check the start and end of the object
        if (json.startsWith("{") && json.endsWith("}")) {
            return true;
        }

        return false;
    }

    private boolean validTrueBooleanValue(String json, int i) {

        //for out of bounds loop control - enough space for there to be 'true'
        if (json.length() - i - 4 > 0) {

            String trueString = "";
            int countControl = i;

            for (int j = 0; j < 4; j++) {
                trueString = trueString.concat(String.valueOf(json.charAt(countControl + j)));
            }

            if (!trueString.equals("true")) {
                return false;
            }
        }
        return true;
    }

    private boolean validFalseBooleanValue(String json, int i) {

        //for out of bounds loop control - enough space for there to be 'false'
        if (json.length() - i - 5 > 0) {

            String trueString = "";

            for (int j = 0; j < 5; j++) {
                trueString = trueString.concat(String.valueOf(json.charAt(i + j)));
            }

            if (!trueString.equals("false")) {
                return false;
            }

        }
        return true;
    }

    private boolean validNullValue(String json, int i) {

        //for out of bounds loop control - enough space for there to be 'false'
        if (json.length() - i - 4 > 0) {

            String trueString = "";

            for (int j = 0; j < 4; j++) {
                trueString = trueString.concat(String.valueOf(json.charAt(i + j)));
            }

            if (!trueString.equals("null")) {
                return false;
            }

        }
        return true;
    }

    private void resetTrackingValues(StateTracker stateTracker, int positionForwardNumber) {
        stateTracker.previousWasColon = false;
        stateTracker.quoteNumber = 0;
        stateTracker.previousWasComma = false;
        stateTracker.position = stateTracker.position + positionForwardNumber;
        stateTracker.currentChar = stateTracker.json.charAt(stateTracker.position);
        stateTracker.hasBeenReset = true;

    }

    private boolean checkIfValidNumber(char c) {

        switch (c) {
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9':
                return true;
            default:
                return false;
        }
    }

    private boolean checkIfValidValueAfterResetTrackingValues(char c) {

        return switch (c) {
            case '"' -> true;
            case ',' -> true;
            case '}' -> true;
            default -> false;
        };
    }
}

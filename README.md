# JSONParser
This challenge is to build your own JSON parser.

No spliting, just character looping. 

O(n) efficiency as we loop through once, checking each char once.

https://codingchallenges.fyi/challenges/challenge-json-parser/



Behaviour:

- Basic key-value string pairs:
  - Start with {
  - End with } or ,
  - have a total of four quotes
  - One colon 

- Numbers:
  - Come in as chars
  - They only appear after the : the key value pair.
  - We check them with the side value processing of the key value pair.
  - They will only have two quoteCount even if valid.

- Specific Strings (false, true, null)
  - They only appear after the : the key value pair.
  - We check them with the side value processing of the key value pair.
  - They only appear after the : the key value pair.
  - We check them with the side value processing of the key value pair.
  - Set length that can be jumped forward in loop control.

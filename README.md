# Compilation

This implementation has a few core elements.
For the MySMTPServer and Connection classes to compile, you must import in the following libraries:
- all java.net library files must be imported
- all java.io library files must be imported

I reason the io library file needs to be imported is for the following files:
- all java.io.BufferedWriter, java.io.File and java.io.FileWriter files must be imported for the socket io streams to work in tune with Java's println and writeLine functions.
- the java.io.IOException file must also be imported to try and catch any code and allow for the code to be robust.

To compile other parts of my code, you must import in the following libraries:
- all java.util.regex library files must be imported for the regular expressions to be functional

# Test Cases
I have created 2 test files.
- One is a basic file, with a minimal needed input.
- The second is a more complex and in ways an unorthodox test as it is a one long test. I have added all the types of errors I believe are associated with an command, checking all the codes outputed. These are followed by correct implementations of commands to again check code and function outputs.

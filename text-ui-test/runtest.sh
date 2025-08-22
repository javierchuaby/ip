#!/usr/bin/env bash

# create bin directory if it doesn't exist
if [ ! -d "../bin" ]; then
  mkdir ../bin
fi

# delete output from previous run
[ -e "./ACTUAL.TXT" ] && rm ACTUAL.TXT

# compile the code into the bin folder, terminate if error occurred
if ! javac -cp ../src/main/java -Xlint:none -d ../bin ../src/main/java/*.java; then
  echo "********** BUILD FAILURE **********"
  exit 1
fi

# run the program, feed commands from input.txt file and redirect the output to ACTUAL.TXT
# (update 'MrMoon' if your main class is in a package, e.g., duke.MrMoon)
java -classpath ../bin MrMoon < input.txt > ACTUAL.TXT

# --- Normalise to Unix with dos2unix (write into new files) ---
dos2unix -n ACTUAL.TXT   ACTUAL-UNIX.TXT
dos2unix -n EXPECTED.TXT EXPECTED-UNIX.TXT

# --- Ensure both end with exactly one trailing newline ---
# If the last byte isn't \n, append one.
if [ -s ACTUAL-UNIX.TXT ] && [ "$(tail -c1 ACTUAL-UNIX.TXT 2>/dev/null)" != $'\n' ]; then
  printf '\n' >> ACTUAL-UNIX.TXT
fi
if [ -s EXPECTED-UNIX.TXT ] && [ "$(tail -c1 EXPECTED-UNIX.TXT 2>/dev/null)" != $'\n' ]; then
  printf '\n' >> EXPECTED-UNIX.TXT
fi

# Copy to -NORM (optional, keeps naming from your earlier script)
cp ACTUAL-UNIX.TXT   ACTUAL-NORM.TXT
cp EXPECTED-UNIX.TXT EXPECTED-NORM.TXT

# --- Compare ---
diff ACTUAL-NORM.TXT EXPECTED-NORM.TXT
if [ $? -eq 0 ]; then
  echo "Test result: PASSED"
  exit 0
else
  echo "Test result: FAILED"
  exit 1
fi

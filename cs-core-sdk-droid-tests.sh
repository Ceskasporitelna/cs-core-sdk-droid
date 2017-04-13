#!/bin/bash Start the emulator

if "$1"; then
    echo no | android create avd --force -n test -t android-21 --abi armeabi-v7a
    emulator -avd test -no-window -wipe-data & EMULATOR_PID=$!
else
    emulator -avd Nexus_6P_API_21 -no-window -wipe-data & EMULATOR_PID=$!
fi

# Wait for Android to finish booting
WAIT_CMD="adb wait-for-device shell getprop init.svc.bootanim"
until $WAIT_CMD | grep -m 1 stopped; do
  echo "Waiting..."
  sleep 1
done

echo "Emulator is ready"

# Unlock the Lock Screen
adb shell input keyevent 82

# Run the tests
OUTPUT="$(./gradlew connectedAndroidTest test -i --stacktrace )"
if echo $OUTPUT | grep -q 'BUILD SUCCESSFUL'; then
	echo "ANDROID TESTS SUCCESSFUL"
else
	echo "$OUTPUT"
fi

# Stop the background processes
kill -9 $EMULATOR_PID
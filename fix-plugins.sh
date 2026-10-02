#!/bin/bash
# fix-plugins.sh — run from the root of the Flixie repo

set -e

echo "=== 1. Revert package names in source files ==="
find . -type f \( -name "*.kt" -o -name "*.kts" -o -name "*.xml" -o -name "*.toml" \) \
  -not -path "./.git/*" \
  -print0 | xargs -0 sed -i \
  -e 's/com\.dolus\.flixie4/com.lagradost.cloudstream4/g' \
  -e 's/com\.dolus\.flixie/com.lagradost.cloudstream3/g' \
  -e 's/com\.dolus\.api/com.lagradost.api/g'

echo "=== 2. Move directory trees to match packages ==="
# App sources
if [ -d app/src/main/java/com/dolus/flixie ]; then
  mkdir -p app/src/main/java/com/lagradost
  mv app/src/main/java/com/dolus/flixie app/src/main/java/com/lagradost/cloudstream3
fi

# Library sources
find library/src -type d -name "flixie" 2>/dev/null | while read -r dir; do
  parent=$(dirname "$dir")
  mkdir -p "$parent/lagradost"
  mv "$dir" "$parent/lagradost/cloudstream3" 2>/dev/null || true
done

find library/src -type d -name "api" 2>/dev/null | while read -r dir; do
  parent=$(dirname "$dir")
  mkdir -p "$parent/lagradost"
  mv "$dir" "$parent/lagradost/api" 2>/dev/null || true
done

# Shared module
if [ -d shared ]; then
  find shared -type d -name "flixie4" 2>/dev/null | while read -r dir; do
    mv "\( dir" " \){dir/flixie4/cloudstream4}" 2>/dev/null || true
  done
fi

echo "=== 3. Fix the critical build.gradle.kts files ==="
sed -i \
  -e 's/namespace = "com.dolus.api"/namespace = "com.lagradost.api"/' \
  -e 's/optIn("com.dolus.flixie.InternalAPI")/optIn("com.lagradost.cloudstream3.InternalAPI")/' \
  -e 's/optIn("com.dolus.flixie.Prerelease")/optIn("com.lagradost.cloudstream3.Prerelease")/' \
  -e 's/annotatedWith.add("com.dolus.flixie./annotatedWith.add("com.lagradost.cloudstream3./g' \
  -e 's/packageName = "com.dolus.api"/packageName = "com.lagradost.api"/' \
  -e 's/groupId = "com.dolus.api"/groupId = "com.lagradost.api"/' \
  library/build.gradle.kts

sed -i \
  -e 's/namespace = "com.dolus.flixie"/namespace = "com.lagradost.cloudstream3"/' \
  -e 's/"com.dolus.flixie.InternalAPI"/"com.lagradost.cloudstream3.InternalAPI"/' \
  -e 's/"com.dolus.flixie.Prerelease"/"com.lagradost.cloudstream3.Prerelease"/' \
  app/build.gradle.kts

if [ -f shared/build.gradle.kts ]; then
  sed -i \
    -e 's/namespace = "com.dolus.flixie4"/namespace = "com.lagradost.cloudstream4"/' \
    -e 's/com.dolus.flixie/com.lagradost.cloudstream3/g' \
    shared/build.gradle.kts
fi

echo "=== 4. Clean leftover empty dirs ==="
find . -type d -empty -delete 2>/dev/null || true

echo "=== Done ==="
echo "Now run: ./gradlew clean assembleStableRelease"
echo "Then test with megarepo or cspr."

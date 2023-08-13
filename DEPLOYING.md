# Deploying

ClojureScript optimized build, Dockerfile, Uberjar, Github actions CD to fly.io

```
HYPERFIDDLE_ELECTRIC_APP_VERSION=`git describe --tags --long --always --dirty`
clojure -X:build uberjar :jar-name "app.jar" :version '"'$HYPERFIDDLE_ELECTRIC_APP_VERSION'"'
java -DHYPERFIDDLE_ELECTRIC_SERVER_VERSION=$HYPERFIDDLE_ELECTRIC_APP_VERSION -jar app.jar
```

```
docker build --progress=plain --build-arg VERSION="$HYPERFIDDLE_ELECTRIC_APP_VERSION" -t electric-starter-app .
docker run --rm -p 7070:8080 electric-starter-app
```


# electric-starter-app

```
$ clj -A:dev -X user/main

Starting Electric compiler and server...
shadow-cljs - server version: 2.20.1 running at http://localhost:9630
shadow-cljs - nREPL server started on port 9001
[:app] Configuring build.
[:app] Compiling ...
[:app] Build completed. (224 files, 0 compiled, 0 warnings, 1.93s)

👉 App server available at http://0.0.0.0:8080
```

# Error reporting

Reproduce this now and confirm error handling works so you trust it:

![screenshot of electric error reporting](readme-electric-error-reporting-proof.png)

Electric is a reactive (async) language. Like React.js, we reconstruct synthetic async stack traces. If you aren't seeing them, something is wrong!

## See also

See also [LOGGING.md] and [DEPLOYING.md].

[LOGGING.md]: ./LOGGING.md
[DEPLOYING.md]: ./DEPLOYING.md

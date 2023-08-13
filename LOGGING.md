# Logging

The Electric server logs. The default logger config is slightly verbose by default to force you to see it working:

```
DEBUG hyperfiddle.electric.impl.env: reloading app.todo-list
DEBUG hyperfiddle.electric-jetty-adapter: Client disconnected for an unknown reason (browser default close code) {:status 1005, :reason nil}
DEBUG hyperfiddle.electric-jetty-adapter: Websocket handler completed gracefully.
DEBUG hyperfiddle.electric-jetty-adapter: WS connect ...
DEBUG hyperfiddle.electric.impl.env: reloading app.todo-list
DEBUG hyperfiddle.electric-jetty-adapter: Client disconnected for an unknown reason (browser default close code) {:status 1005, :reason nil}
```

**Silence the Electric debug logs by live editing logback.xml** and setting `name="hyperfiddle"` to `level="INFO"`, it will hot code reload so no restart is needed. Please **do NOT disable logs entirely**; the Electric server logs one important warning at the `INFO` level we call **unserializable reference transfer**, here is an example:

```
(e/defn TodoCreate []
  (e/client
    (InputSubmit. (e/fn [v]
                    (e/server
                      (d/transact! !conn [{:task/description v
                                           :task/status :active}])
                      nil))))) ;     <-- here
```

Note the intentional `nil` in the final line. If you remove the nil — try it right now — Electric will attempt to serialize whatever `d/transact!` returns — a reference — and stream it to the client. Since that reference cannot be serialized, Electric will send `nil` instead, and log at the `INFO` level:

```
INFO  hyperfiddle.electric.impl.io: Unserializable reference transfer: datascript.lru$cache$reify__35945 datascript.lru$cache$reify__35945@48ea0f24
INFO  hyperfiddle.electric.impl.io: Unserializable reference transfer: datascript.db.Datom #datascript/Datom [1 :task/description "asdf" 536870913 true]
...
```

We decided not to throw an exception here because it is almost always unintentional when this happens. **Do not disable this warning, it will save you one day!** If you want to target this exact message, use this:
`<logger name="hyperfiddle.electric.impl.io" level="DEBUG" additivity="false"><appender-ref ref="STDOUT" /></logger>`

[Note: Perhaps we should revisit this decision in the future now that our exception handling is more mature.]

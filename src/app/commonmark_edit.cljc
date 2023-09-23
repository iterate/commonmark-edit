(ns app.commonmark-edit
  (:require [hyperfiddle.electric :as e]
            [hyperfiddle.electric-dom2 :as dom]
            #?(:clj [app.pandoc :as pandoc])
            ))

#?(:clj (defonce !global-state (atom {})))
(e/def global-state (e/server (e/watch !global-state)))

(e/defn doc-from-url []
  (e/client (.get (js/URLSearchParams. (-> js/window .-location .-search))
                  "doc")))

(e/defn Edit-doc [doc-id]
  (e/server swap! !global-state update doc-id
            (fn [oldval] (if oldval oldval "")))
  (e/client (dom/p (dom/text "Now visiting doc: " doc-id)
                   (dom/text ". ")
                   (dom/a (dom/props {:href "?"}) (dom/text "Go back"))
                   (dom/text "."))))

(e/defn Welcome []
  (e/client
   (dom/p (dom/a (dom/props {:href (str "?doc=" (random-uuid))}) (dom/text "Create new document")))

   ))

(e/defn Editor []
  (e/client
   (dom/link (dom/props {:rel :stylesheet :href "/todo-list.css"}))
   (dom/link (dom/props {:rel :stylesheet :href "/pandoc-preview.css"}))
   (dom/h1 (dom/text "Commonmark editor"))
   (dom/p (dom/em (dom/text "powered by Pandoc and Electric Clojure")))

   (dom/p (dom/text "DEBUG"))
   (dom/pre (dom/text (prn-str (e/client (.get (js/URLSearchParams. (-> js/window .-location .-search))
                                               "doc")))))

   (dom/p (dom/text "STOP_DEBUG"))

   (if-let [doc-id (doc-from-url)]
     (Edit-doc. doc-id)
     (Welcome.))

   (dom/h2 (dom/text "Debug"))
   (dom/pre (dom/text (prn-str global-state)))

   ))

(comment
  @!global-state

  (swap! !global-state assoc "abc123" "trolo")
  (swap! !global-state dissoc "abc123")

  )

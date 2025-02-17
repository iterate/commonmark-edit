(ns app.todo-list
  (:require [hyperfiddle.electric :as e]
            [hyperfiddle.electric-dom2 :as dom]
            #?(:clj [app.pandoc :as pandoc])))

#?(:clj (defonce !state (atom {:text "# Your document

- major point 1
- major point 2

_for glory!_"})))

(e/def state (e/server (e/watch !state)))
(e/def text (:text state))

(comment
  (reset! !state "INSANE STUFF 4"))

#?(:cljs
   (defn doc-from-url []
     (.get (js/URLSearchParams. (-> js/window .-location .-search))
           "doc")))

(e/defn Markdown-editor []
  (e/client
   (dom/link (dom/props {:rel :stylesheet :href "/todo-list.css"}))
   (dom/link (dom/props {:rel :stylesheet :href "/pandoc-preview.css"}))
   (dom/h1 (dom/text "Commonmark editor"))
   (dom/p (dom/em (dom/text "powered by Pandoc and Electric Clojure")))

   (dom/textarea (dom/props {:class "input-textfield"
                             :placeholder text})
                 (dom/on "input" (e/fn [e]
                                   (when-some [v (.. e -target -value)]
                                     (e/server (reset! !state v))))))
   (let [text-html (e/server (-> text pandoc/from-markdown pandoc/to-html))]
     (e/client (-> js/document (.getElementById "vwnm4o") (.-innerHTML) (set! text-html)))
     (dom/div
      (dom/div (dom/props {:id "vwnm4o"}))
      (dom/pre (dom/text (e/server text-html)))))
   (dom/section
    (dom/h2 (dom/text "Debug info"))
    (dom/pre (dom/text (e/client (doc-from-url))))
    )))

(ns app.todo-list
  (:require [hyperfiddle.electric :as e]
            [hyperfiddle.electric-dom2 :as dom]
            #?(:clj [app.pandoc :as pandoc])))

;; plan
;;
;; 1. Read document from URL (how?)
;; 2. Work on that document.
;;    If it exists, cool, continue using it.
;;    Otherwise, cool. It's an empty string.

#_{:clj-kondo/ignore [:unresolved-namespace]}
(defn markdown->html [md-str]
  (e/server (-> md-str pandoc/from-markdown pandoc/to-html)))

#?(:clj (defonce !text (atom "# Your document

- major point 1
- major point 2

_for glory!_")))
(e/def text (e/server (e/watch !text)))

(comment
  (reset! !text "INSANE STUFF 4"))

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
                                     (e/server (reset! !text v))))))
   (let [text-html (markdown->html text)]
     #_{:clj-kondo/ignore [:unresolved-namespace]}
     (e/client (-> js/document (.getElementById "vwnm4o") (.-innerHTML) (set! text-html)))
     (dom/div
      (dom/div (dom/props {:id "vwnm4o"}))
      (dom/pre (dom/text (e/server text-html)))))))

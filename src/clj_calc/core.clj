(ns clj-calc.core
  (:require [instaparse.core :as insta])
  (:use seesaw.core)
)

(def parse-expr
  (insta/parser
    "S = EXPR
     EXPR = TERM | ADD | SUB
     ADD = EXPR '+' TERM
     SUB = EXPR '-' TERM
     TERM = FACTOR | MUL | DIV
     MUL = TERM '*' FACTOR
     DIV = TERM '/' FACTOR
     FACTOR = NUMBER | NEG | PAREN
     NEG = '-' FACTOR
     PAREN = '(' EXPR ')'
     NUMBER = #'[0-9]+(\\.[0-9]+)?'"))

(defn calc-tree
  [tree]
  (cond
    (= :S (tree 0)) (calc-tree (tree 1))
    (= :EXPR (tree 0)) (calc-tree (tree 1))
    (= :TERM (tree 0)) (calc-tree (tree 1))
    (= :FACTOR (tree 0)) (calc-tree (tree 1))
    (= :ADD (tree 0)) (+ (calc-tree (tree 1)) (calc-tree (tree 3)))
    (= :SUB (tree 0)) (- (calc-tree (tree 1)) (calc-tree (tree 3)))
    (= :MUL (tree 0)) (* (calc-tree (tree 1)) (calc-tree (tree 3)))
    (= :DIV (tree 0)) (/ (calc-tree (tree 1)) (calc-tree (tree 3)))
    (= :NEG (tree 0)) (- 0 (calc-tree (tree 2)))
    (= :PAREN (tree 0)) (calc-tree (tree 2))
    (= :NUMBER (tree 0)) (Double/parseDouble (tree 1)))
)

(def panel (form-panel :items [
  [(text :id :expr :columns 20 :listen [
    :key-released
    (fn [e]
      (let [expr (clojure.string/replace (:expr (value panel)) " " "") ; removes whitespace
            tree (parse-expr expr)]
      (value! panel
        { :result (str (if (insta/failure? tree)
                           "Invalid expression"
                           (calc-tree tree))) })))
  ]) :grid :next :weightx 1.0]
  [(label :id :result :text "0" :halign :right)]
]))

(defn -main []
  (invoke-later
    (-> (frame :title "Clojure Calculator",
      :content panel
      :on-close :exit)
    pack!
    show!)))

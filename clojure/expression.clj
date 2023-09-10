(def constant constantly)
(def one (constant 1))
(defn variable [name] (fn [vars] (vars name)))
(def variables #{'x 'y 'z})
(defn op-fabric [op]
  (fn [& exprs]
    (fn [vars]
      (apply op (mapv #(% vars) exprs))
      ))
  )
(def add (op-fabric +))
(def subtract (op-fabric -))
(def multiply (op-fabric *))
(defn divide-op
  ([a] (/ 1.0 a))
  ([a & xs] (/ a (double (apply * xs)))))
(def divide (op-fabric divide-op))
(def negate (op-fabric -))
(defn sumexp-op [& x]
  (apply + (mapv #(Math/exp %) x)))
(def sumexp (op-fabric sumexp-op))
(defn logsumexp [& x]
  (Math/log (apply sumexp-op x)))
(def lse (op-fabric logsumexp))
(def operations {'+      add
                 '-      subtract
                 '*      multiply
                 '/      divide
                 'negate negate
                 'sumexp sumexp
                 'lse    lse})
(defn parseFactory [to-var to-const ops]
  (letfn [(parse [expr] (cond
                          (number? expr) (to-const expr)
                          (contains? variables expr) (to-var (name expr))
                          :else (apply (ops (first expr)) (mapv parse (rest expr)))
                          ))]
    (comp parse read-string)))
(def parseFunction (parseFactory variable constant operations))



(def evaluate #(.evaluate %1 %2))
(def diff #(.diff %1 %2))
(def toString str)

(definterface Expression
  (^Number evaluate [vars])
  (^user.Expression diff [x]))

(deftype Primary [data eval to-string differ]
  Object
  (toString [this] (to-string this))
  Expression
  (evaluate [this vars] (eval this vars))
  (diff [this x] (differ this x)))
(deftype Unary [data op op-str differ]
  Object
  (toString [this] (str "(" op-str " " (str data) ")"))
  Expression
  (evaluate [this vars] (op (evaluate data vars)))
  (diff [this x] (differ data (diff data x))))

(deftype Binary [data op op-str differ]
  Object
  (toString [this] (str "(" op-str " " (clojure.string/join " " (mapv str data)) ")"))
  Expression
  (evaluate [this vars] (apply op (mapv #(evaluate % vars) data)))
  (diff [this x] (differ data (mapv #(diff % x) data))))


(declare Constant)
(declare ZERO)
(defn data-str [this] (str (.-data this)))
(defn cnst-evaluate [this vars] (.-data this))
(defn cnst-diff [this x] ZERO)
(defn Constant [c] (Primary.
                     c
                     cnst-evaluate
                     data-str
                     cnst-diff))
(def ZERO (Constant 0))
(def ONE (Constant 1))
(def TWO (Constant 2))

(defn var-evaluate [this vars] (vars (.-data this)))
(defn var-diff [this namex] (if (= (.-data this) namex) ONE ZERO))
(defn Variable [x] (Primary.
                     x
                     var-evaluate
                     data-str
                     var-diff))
(declare Add)
(declare Multiply)
(declare Divide)
(declare Subtract)
(declare RMS)
(declare Meansq)
(defn unary-factory [op op-str differ]
  (fn [expr]
    (Unary. expr
            op
            op-str
            differ)))
(defn binary-factory
  ([op op-str differ]
   (fn [& exprs]
     (Binary.
       exprs
       op
       op-str
       differ))))

(def Negate (unary-factory -
                           "negate"
                           (fn [F F']
                             (Negate F'))))
(defn lin-diff [constr]
  (fn [F F']
    (apply (force constr) F')))
(def Add (binary-factory + "+" (lin-diff (delay Add))))
(def Subtract (binary-factory - "-" (lin-diff (delay Subtract))))
(defn mul-diff [F F']
  (let [F_F' (map list F F')
        ab' #(Multiply (first %1) (second %2))
        diffy (fn [pp' ff'] (list (Multiply (first pp') (first ff'))
                                  (Add (ab' pp' ff')
                                       (ab' ff' pp'))))]
    (second (reduce diffy (list ONE ZERO) F_F'))))
(def Multiply (binary-factory * "*" mul-diff))
(defn div-diff [F F']
  (let [FF' (map list F F')
        diffy (fn diffy
                ([a] (diffy (list ONE ZERO) a))
                ([a & bs] (let [u (first a)
                                u' (second a)
                                F (map first bs)
                                F' (map second bs)
                                v (apply Multiply F)
                                v' (mul-diff F F')]
                            (Divide (Subtract (Multiply u' v) (Multiply u v'))
                                    (Multiply v v)))))
        ]
    (apply diffy FF')))
(def Divide (binary-factory divide-op "/" div-diff))
(defn meansq-diff [F F']
  (Divide (apply Add (mapv #(Multiply TWO %1 %2) F F')) (Constant (count F))))
(defn meansq-op [& args]
  (/ (apply + (mapv #(* % %) args)) (count args)))
(def Meansq (binary-factory meansq-op
                            "meansq"
                            meansq-diff))
(def rms-op #(Math/sqrt (apply meansq-op %&)))
(defn rms-diff [F F']
  (Divide (meansq-diff F F') (apply RMS F) TWO))
(def RMS (binary-factory rms-op
                         "rms"
                         rms-diff))

(def Operations {'+      Add
                 '-      Subtract
                 '*      Multiply
                 '/      Divide
                 'negate Negate
                 'meansq Meansq
                 'rms    RMS})

(def parseObject (parseFactory Variable Constant Operations))
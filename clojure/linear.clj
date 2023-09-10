(def same-size? #(apply == (map count %)))
(def num-vec? #(and (vector? %) (every? number? %)))
(def same-size-vecs? #(and (every? num-vec? %) (same-size? %)) )
(def matr? #(and (vector? %) (seq %) (every? num-vec? %) (same-size? %)))
(defn op-maker [op checker]
  (fn [& xs]
    {:pre [(checker xs)]}
    (apply mapv op xs)
    )
  )
(defn vec-op [op]
  (op-maker op same-size-vecs?))
(def v+ (vec-op +))
(def v* (vec-op *))
(def vd (vec-op /))
(def v- (vec-op -))
(defn scalar [& vs]
  {:pre [same-size-vecs? vs]}
  (reduce + (reduce v* vs)))
(defn vect [& vs]
  {:pre [(and (same-size-vecs? vs) (== 3 (count (first vs))))]}
  (letfn [(vect2 [x y]
            (vector (- (* (nth x 1) (nth y 2)) (* (nth x 2) (nth y 1)))
                    (- (* (nth x 2) (nth y 0)) (* (nth x 0) (nth y 2)))
                    (- (* (nth x 0) (nth y 1)) (* (nth x 1) (nth y 0)))))]
    (reduce vect2 vs))
  )
(defn *s [op checker]
  (fn [x & ss]
    {:pre [(checker x) (every? number? ss)]}
    (let [s (reduce * ss)]
      (mapv #(op %1 s) x))
    ))
(def v*s (*s * num-vec?))

(defn matr-op [op]
  (op-maker (vec-op op) #(and (every? matr? %) (same-size? %)))
  )
(def m+ (matr-op +))
(def m* (matr-op *))
(def m- (matr-op -))
(def md (matr-op /))
(def m*s (*s v*s matr?))
(defn m*v [m & vs]
  {:pre [(matr? m) (same-size-vecs? vs)]}
  (mapv #(reduce + (reduce v* % vs)) m)
  )
(defn transpose [m]
  {:pre [(matr? m)]}
  (apply mapv vector m))
(defn m*m [& ms]
  (letfn [(m*m2 [a b]
            (transpose (mapv #(m*v a %) (transpose b))))]
    (reduce m*m2 ms))
  )
  
(def cur-dim #(cond
                (number? %) 0
                (vector? %) (count %)
                :else nil))
(defn same-dims? [& ts]
  (and (apply == (map cur-dim ts))
       (or
         (every? number? ts)
         (apply same-dims? (map first ts)))
       ))
(def tensor? #(or (number? %) (and (vector? %) (not-empty %) (every? tensor? %) (apply same-dims? %))))

(defn tensor-op [op]
  (letfn [(t-op [& args]
            (cond
              (every? number? args) (apply op args)
              :else (apply mapv t-op args)
              )
            )
          ]
    (fn [& args]
      {:pre [(every? tensor? args) (apply same-dims? args)]}
      (apply t-op args)
      )))

(def t+ (tensor-op +))
(def t* (tensor-op *))
(def t- (tensor-op -))
(def td (tensor-op /))
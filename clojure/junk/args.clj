(ns org.gnuton.showargs)
(doseq [arg *command-line-args*]
  (printf "arg='%s'\n" arg))

(if (= "2" (second *command-line-args*))
  (println "\n\nSecond arg is string 2, not number 2."))

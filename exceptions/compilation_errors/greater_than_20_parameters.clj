(ns compilation_errors.greater_than_20_parameters)

(#(+ %22222 1) 2 3)

;(expect #"A function may not take more than more than 20 parameters."
;        (get-text-no-location (run-and-catch-pretty-no-stacktrace (load-file compilation_errors.greater_than_20_parameters))))

;(load-file compilation_errors.greater_than_20_parameters)

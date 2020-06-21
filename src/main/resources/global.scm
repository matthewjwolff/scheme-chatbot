;;; Initialization script.
;;; Runs when a new scheme interpreter is created
(define (send-to message target) 
    (invoke _bot 'sendMessage message target)
)

(define (user-has-perm? user perm)
    (invoke _bot 'userHasPermission user perm )
)
;;; Initialization script.
;;; Runs when a new scheme interpreter is created
(define (send-to message target) 
    (.sendMessage _bot message target)
)

(define (user-has-perm? user perm)
    (.userHasPermission _bot user perm )
)

;; onMessage eventlistener implementation

;; list of event listeners
(define event-listeners ())
;; to register an event listener, construct a list with the new listener and the rest of the listeners
(define (register-message-listener listener) 
    ;; TODO: validate signature of event listener
    (set! event-listeners (cons listener event-listeners))
)

;; calls every procedure in the event listener list
(define (on-message  sender message )
    ;; 1st arg: lambda that calls the argument, 2nd arg: list of procedures
    (for-each (lambda (proc) (proc message sender)) event-listeners)
)

(define (is-url? url) (io.wolff.chatbot.Utils.isUrl url))
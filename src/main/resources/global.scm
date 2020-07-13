;;; Initialization script.
;;; Runs when a new scheme interpreter is created
(define (send-to message target) 
    (.sendMessage _bot message target)
)

(define (user-has-perm? user perm)
    (.userHasPermission _bot user perm )
)

(define (register-on-message message) 
    (.registerOnMessage _bot message)
)

(define (is-url? url) (io.wolff.chatbot.Utils.isUrl url))
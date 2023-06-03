.PHONY: install repl

install:
	brew install clojure
	bash -c "$$(curl -s https://raw.githubusercontent.com/babashka/babashka/master/install)"

repl:
	rlwrap bb --repl


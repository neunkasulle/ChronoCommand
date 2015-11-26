#!/usr/bin/make -f

all:
    git clone https://github.com/adobe-fonts/source-serif-pro.git
	xelatex -output-directory=out pflichtenheft.tex

clean:
	rm out/*

.PHONY: all clean

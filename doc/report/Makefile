# Generate all outputs for the project.
NAME=report

default : compile

clean : 
	$(RM) $(NAME).aux
	$(RM) $(NAME).bbl
	$(RM) $(NAME).blg
	$(RM) $(NAME).dvi
	$(RM) $(NAME).log
	$(RM) $(NAME).out
	$(RM) $(NAME).pdf
	$(RM) $(NAME).ps
	$(RM) $(NAME).tpt

compile :
	pdflatex		$(NAME).tex	# Make aux that bibtex need.
	pdflatex		$(NAME).tex	# Make aux that bibtex need.

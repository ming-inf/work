adc(){asciidoc $1adoc} # asciidoc compile
ado(){open $1html} # asciidoc open
adco(){adc $1; ado $1}

notes() {
  if [ ! -z "$1" ]; then
    # Using the "$@" here will take all parameters passed into
    # this function so we can place everything into our file.
    echo "$@" >> "$HOME/commandlineNotes.adoc"
  else
    # If no arguments were passed we will take stdout and place
    # it into our notes instead.
    cat - >> "$HOME/commandlineNotes.adoc"
  fi
}

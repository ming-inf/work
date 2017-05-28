# The following lines were added by compinstall
zstyle :compinstall filename '~/.zshrc'

autoload -Uz compinit
compinit

HISTFILE=~/.histfile
HISTSIZE=10000
SAVEHIST=10000

setopt append_history # Allow multiple terminal sessions to all append to one zsh command history
setopt extended_history # save timestamp of command and duration
setopt inc_append_history # Add comamnds as they are typed, do not wait until shell exit
setopt hist_expire_dups_first # when trimming history, lose oldest duplicates first
setopt hist_ignore_dups # Do not write events to history that are duplicates of previous events
setopt hist_ignore_space # remove command line from history list when first character on the line is a space
setopt hist_find_no_dups # When searching history do not display results already cycled through twice
setopt hist_reduce_blanks # Remove extra blanks from each command line being added to history
setopt autocd # automatically change directory
setopt notify # Report the status of background jobs immediately, rather than waiting until just before printing a prompt

unsetopt beep
bindkey -v
# End of lines added by compinstall

bindkey ' ' magic-space # remap space to perform history expansion

[[ -f ~/.alias ]] && source ~/.alias
[[ -f ~/.alias.zsh ]] && source ~/.alias.zsh
[[ -f ~/.functions.zsh ]] && source ~/.functions.zsh

# os specific settings
if [ -f ~/.zshrc.os ]; then
	source ~/.zshrc.os
fi

#function nested_processes() {
#	typeset -a p
#	parentpid=$PPID
#	while (($parentpid != 1)) do
#		p=($(ps -O pid -p "$parentpid" | awk '$1 == PP {print $8}' PP="$parentpid" | xargs basename) $p)
#		parentpid=$(ps -O ppid -p "$parentpid" | awk '$1 == PP {print $2}' PP="$parentpid")
#	done
#	echo ${(j: > :)p}' ' # join strings with ' > '
#}

setopt PROMPT_SUBST
PROMPT='%# '
if type nested_processes > /dev/null; then
	PROMPT='$(nested_processes)'$PROMPT
fi
if type status_line > /dev/null; then
	PROMPT='$(status_line)'$PROMPT
fi
if type exit_status > /dev/null; then
	PROMPT='$(exit_status $?)'$PROMPT
fi
#PS1="%m%# " # default
PS2="$_> "
PS3="?# "
PS4="+"

if [[ -a ~/.zshrc.local ]]; then
	source ~/.zshrc.local # local override
fi

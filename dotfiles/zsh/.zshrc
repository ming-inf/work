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

source ~/.alias
source ~/.alias.zsh

bindkey ' ' magic-space # remap space to perform history expansion

# git prompt
source ~/.git-prompt.zsh

function exit_status() {
	blank_line=$'\n'
	exit_status=$?
	if [ $exit_status -eq 0 ]; then
		ok='[  OK  ]'
		statusline="%F{green}${(l:$COLUMNS:: :)ok}%f"
	else
		error='[ERRORS]'
		statusline="%F{red}${(l:$COLUMNS:: :)error}%f"
	fi
	echo $blank_line$statusline
}

function status_line() {
	blank_line=$'\n'
	left='%D{%F %T} %F{245}%! %F{cyan}%n%f@%F{red}%m%f:%F{cyan}%~%f'
	if type parse_git_state > /dev/null; then
		right=$(parse_git_state)
	else
		right=''
	fi

	left_size=${#${(S%%)left//(\%([KF1]|)\{*\}|\%[Bbkf])}} # filter non-printable characters (colour codes)
	right_padding_size=$(($COLUMNS-$left_size))

	echo $blank_line$left${(l:$right_padding_size:: :)right}
}

function nested_processes() {
	typeset -a p
	parentpid=$PPID
	if [ cygwin = $OSTYPE ]; then
		while (($parentpid != 1)) do
			if type procps > /dev/null; then
				p=($(procps -o cmd= $parentpid | awk '{sub(/^-/, "", $1);print $1}' | xargs basename) $p) # awk sub() replaces first dash (login shell), $1 returns command (not options or arguments), xargs turns stream into arguments, basename returns last name from pathname
				parentpid=$(procps -o ppid:1= $parentpid)
			else
				p=($(ps -p "$parentpid" | awk '$1 == PP {print $8}' PP="$parentpid" | xargs basename) $p)
				parentpid=$(ps -p "$parentpid" | awk '$1 == PP {print $2}' PP="$parentpid")
			fi
		done
	else
		while (($parentpid != 1)) do
			p=($(ps -O pid -p "$parentpid" | awk '$1 == PP {print $8}' PP="$parentpid" | xargs basename) $p)
			parentpid=$(ps -O ppid -p "$parentpid" | awk '$1 == PP {print $2}' PP="$parentpid")
		done
	fi
	echo ${(j: > :)p}' ' # join strings with ' > '
}

setopt PROMPT_SUBST
PROMPT='%# '
if type nested_processes > /dev/null; then
	PROMPT='$(nested_processes)'$PROMPT
fi
if type status_line > /dev/null; then
	PROMPT='$(status_line)'$PROMPT
fi
if type exit_status > /dev/null; then
	PROMPT='$(exit_status)'$PROMPT
fi
#PS1="%m%# " # default
PS2="$_> "
PS3="?# "
PS4="+"

if [[ -a ~/.zshrc.local ]]; then
	source ~/.zshrc.local # local override
fi

# os specific settings
if [ -f ~/.zshrc.os ]; then
	source ~/.zshrc.os
fi

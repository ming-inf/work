# Lines configured by zsh-newuser-install
HISTFILE=~/.histfile
HISTSIZE=10000
SAVEHIST=10000
setopt appendhistory autocd notify
unsetopt beep
bindkey -v
# End of lines configured by zsh-newuser-install
# The following lines were added by compinstall
zstyle :compinstall filename '~/.zshrc'

autoload -Uz compinit
compinit
# End of lines added by compinstall

. ~/.alias
. ~/.alias.zsh

bindkey ' ' magic-space # remap space to perform history expansion

# git prompt
source ~/.git-prompt.zsh

precmd() {
	blank_line=$'\n'
	left='%D{%F %H:%M} %F{245}%! %F{cyan}%n%f@%F{red}%m%f:%F{cyan}%~%f '$?
	right=$(parse_git_state)

	left_size=${#${(S%%)left//(\%([KF1]|)\{*\}|\%[Bbkf])}}
	padding=$(($COLUMNS-$left_size))

	print -P $blank_line$left${(l:$padding:: :)right}
}

function nested_processes() {
	typeset -a p
	parentpid=$PPID
	if [[ cygwin = $os ]]; then
		while (($parentpid != 1)) do
			p=($(ps -p "$parentpid" | awk '$1 == PP {print $8}' PP="$parentpid" | xargs basename) $p)
			parentpid=$(ps -p "$parentpid" | awk '$1 == PP {print $2}' PP="$parentpid")
		done
	else
		while (($parentpid != 1)) do
			p=($(ps -O pid -p "$parentpid" | awk '$1 == PP {print $8}' PP="$parentpid" | xargs basename) $p)
			parentpid=$(ps -O ppid -p "$parentpid" | awk '$1 == PP {print $2}' PP="$parentpid")
		done
	fi
	echo ${(j: > :)p} # prepend ' > '
}

setopt PROMPT_SUBST
PROMPT='$(nested_processes) %# '
#PS1="%m%# " # default
PS2="$_> "
PS3="?# "
PS4="+"

[[ -a ~/.zshrc.local ]] && . ~/.zshrc.local # local override

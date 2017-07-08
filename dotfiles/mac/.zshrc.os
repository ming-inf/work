# bsd ls
alias l='ls -lG'
alias ll='ls -alG'

# case insensitive tab completion
zstyle ':completion:*' matcher-list 'm:{a-zA-Z}={A-Za-z}'

function nested_processes() {
	typeset -a p
	parentpid=$PPID
	while [ $parentpid -ne 1 ]; do
		p=($(ps -o comm= $parentpid | awk '{sub(/^-/, "", $1);print $1}' | xargs basename) $p) # remove leading dash if exists
		parentpid=$(ps -o ppid= $parentpid | awk '{$1=$1};1') # trim space at beginning of line
	done
	echo ${(j: > :)p}' ' # join strings with ' > '
}

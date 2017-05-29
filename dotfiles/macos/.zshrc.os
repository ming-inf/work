alias l='ls -lG'
alias ll='ls -alG'

function nested_processes() {
	typeset -a p
	parentpid=$PPID
	while [ $parentpid -ne 1 ]; do
		p=($(ps -o comm= $parentpid | awk '{sub(/^-/, "", $1);print $1}' | xargs basename) $p)
		parentpid=$(ps -o ppid= $parentpid)
	done
	echo ${(j: > :)p}' ' # join strings with ' > '
}

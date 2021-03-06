export CYGWIN=winsymlinks:native
alias open='cygstart'

# gnu ls
alias l='ls -l --color=auto'
alias ll='ls -al --color=auto'
alias ld='ls -adl */ .*/ --color=auto'

function nested_processes() {
	typeset -a p
	parentpid=$PPID
	while (($parentpid != 1)) do
		if type procps > /dev/null; then
			p=($(procps -o cmd= $parentpid | awk '{sub(/^-/, "", $1);print $1}' | xargs basename) $p) # awk sub() replaces first dash (login shell), $1 returns command (not options or arguments), xargs turns stream into arguments, basename returns last name from pathname
			parentpid=$(procps -o ppid:1= $parentpid)
		else
			p=($(ps -p "$parentpid" | awk '$1 == PP {print $8}' PP="$parentpid" | xargs basename) $p)
			parentpid=$(ps -p "$parentpid" | awk '$1 == PP {print $2}' PP="$parentpid")
		fi
	done
	echo ${(j: > :)p}' ' # join strings with ' > '
}

function chpwd() {
	settitle $(cygpath -m `pwd`)
}

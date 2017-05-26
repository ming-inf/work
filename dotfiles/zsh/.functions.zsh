function parse_git_state() {
	if ! git rev-parse --git-dir >/dev/null 2>&1; then return; fi

	local git_status=$(git status -s --porcelain)

	local staged=$(echo $git_status | awk '/^[ACDMR]/ {count++} END {print count}')
	local unstaged=$(echo $git_status | awk '/^.[DM?]/ {count++} END {print count}')

	local ahead=$(git rev-list @{u}.. 2> /dev/null | awk '{count++} END {print count}')
	local behind=$(git rev-list ..@{u} 2> /dev/null | awk '{count++} END {print count}')

	local git_branch=$(git name-rev --name-only --no-undefined --always HEAD)
	local git_hash=$(git rev-parse --short HEAD 2> /dev/null)

	local git_state="± $git_hash"
	(( $staged )) || (( $unstaged )) && git_state+=" ${unstaged}i$staged"
	(( $ahead )) || (( $behind )) && git_state+=" ${ahead}b$behind"
	[[ -n $git_branch ]] && git_state+=" [$git_branch]"
	echo $git_state
}

function exit_status() {
	blank_line=$'\n'
	exit_status=$1
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

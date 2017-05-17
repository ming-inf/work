function parse_git_state() {
	if ! git rev-parse --git-dir >/dev/null 2>&1; then return; fi

	local git_status=$(git status -s --porcelain)

	local git_index_state staged unstaged
	local staged=$(echo $git_status | awk '/^[ACDMR]/ {count++} END {print count}')
	local unstaged=$(echo $git_status | awk '/^.[ADM?]/ {count++} END {print count}')

	local git_branch_state ahead behind
	local ahead=$(git rev-list @{u}.. 2> /dev/null | awk '{count++} END {print count}')
	local behind=$(git rev-list ..@{u} 2> /dev/null | awk '{count++} END {print count}')

	local git_branch=${$(git symbolic-ref -q HEAD 2> /dev/null || git name-rev --name-only --no-undefined --always HEAD)#refs\/heads\/}
	local git_hash=$(git rev-parse --short HEAD 2> /dev/null)

	local git_state="± $git_hash"
	(( $staged )) || (( $unstaged )) && git_state+=" ${unstaged}i$staged"
	(( $ahead )) || (( $behind )) && git_state+=" ${ahead}b$behind"
	[[ -n $git_branch ]] && git_state+=" [$git_branch]"
	echo $git_state
}

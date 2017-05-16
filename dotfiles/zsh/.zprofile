# os specific settings
setopt no_case_match
if [[ $(uname -s) =~ cygwin* ]]; then
	export CYGWIN=winsymlinks:nativestrict
	. ~/.alias.cygwin
fi

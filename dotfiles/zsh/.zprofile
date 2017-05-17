# os specific settings
setopt no_case_match
if [[ $(uname -s) =~ cygwin* ]]; then
	export os=cygwin
	export CYGWIN=winsymlinks:nativestrict
	. ~/.alias.cygwin
fi

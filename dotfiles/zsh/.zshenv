# set vim as default editor
export VISUAL=vim
export EDITOR=$VISUAL

# load local override
[[ -f ~/.zshenv.local ]] && source ~/.zshenv.local

# override aliases with tmux aware aliases
alias gg='git log --graph --date=format-local:"%F %T" --format=format:"%h %C(dim white)%ad%C(reset) %<(20,trunc)%s %C(dim white)%ae%C(reset)%C(auto)%d" -$(($(tmux display-message -pt $TMUX_PANE "#{pane_height}") / 3))'
alias ggg='git log --graph --date=format-local:"%F %T" --format=format:"%h %C(dim white)%ad%C(reset) %<(20,trunc)%s %C(dim white)%ae%C(reset)%C(auto)%d" -$(($(tmux display-message -pt $TMUX_PANE "#{pane_height}") * 2/3)) --all'

:imap jk <esc>
noremap l h
noremap ; l
noremap h :

set splitbelow splitright
set number
set tabstop=1
set showcmd
set cursorline
set showmatch
set incsearch
set hlsearch

let &t_ti.="\e[1 q"
let &t_SI.="\e[5 q"
let &t_EI.="\e[1 q"
let &t_te.="\e[0 q"

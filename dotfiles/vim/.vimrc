:imap jk <esc>
noremap l h
noremap ; l
noremap h :

set splitbelow splitright

" https://dougblack.io/words/a-good-vimrc.html
set number
set tabstop=1
set showcmd
set cursorline
set showmatch
set incsearch
set hlsearch

" https://github.com/mintty/mintty/wiki/Tips#mode-dependent-cursor-in-vim
let &t_ti.="\e[1 q"
let &t_SI.="\e[5 q"
let &t_EI.="\e[1 q"
let &t_te.="\e[0 q"

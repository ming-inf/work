:imap jk <esc>
noremap l h
noremap ; l
noremap h :

noremap <C-w>l <C-w>h
noremap <C-w>; <C-w>l

set splitbelow splitright

" https://dougblack.io/words/a-good-vimrc.html
set number relativenumber
set tabstop=1
set showcmd
set cursorline cursorcolumn
set incsearch hlsearch showmatch

" https://github.com/mintty/mintty/wiki/Tips#mode-dependent-cursor-in-vim
let &t_ti.="\e[1 q"
let &t_SI.="\e[5 q"
let &t_EI.="\e[1 q"
let &t_te.="\e[0 q"

:highlight cursorline ctermbg=0 cterm=none
:highlight cursorcolumn ctermbg=0

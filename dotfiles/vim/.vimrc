" reload with :so %
" turn off highlighting until next search :noh
" | separates commands
imap jk <esc>| " set jk to escape insert mode
noremap j gj| " move down in between wrapped lines
noremap k gk| " move up in between wrapped lines
noremap l <left>| " moves left
noremap ; <right>| " moves right
noremap h :| " enters commandline mode

noremap <c-w>l <c-w>h| " moves left pane
noremap <c-w>; <c-w>l| " moves right pane

highlight cursorline ctermbg=0 cterm=none| " highlight current line
highlight cursorcolumn ctermbg=0| " highlight current column

set splitbelow splitright " open split panes below and to the right of the current pane
set list listchars=tab:»·,trail:·,nbsp:· " display whitespace
set colorcolumn=120 " show columns
set timeoutlen=1000 ttimeoutlen=0 " esc key delay
set nobackup nowritebackup " no backup made
set noswapfile " disable swap file
set ignorecase " ignore case when searching
set smartcase " ignore case when pattern is all lowercase
set smarttab " insert tabs according to shiftwidgth not tabstop
set autoindent " copy indent from current line when starting new line
set copyindent " copy previous indentation on autoindenting
set visualbell " don't beep
set noerrorbells " don't beep
set backspace=start,indent,eol " make backspace delete over line breaks
set title " set terminal title
set laststatus=2 " always display status line
set background=dark " set colour scheme

filetype on| " autodetect file type
syntax enable| " enable syntax highlighting

" https://dougblack.io/words/a-good-vimrc.html
set number " precede each line with line number
set relativenumber " show line numbers relative to line with cursor
set tabstop=1 " set tab to 1 space
set shiftwidth=0 " disable shiftwidth, use tabstop
set ruler " show cursor position
set showcmd " display incomplete commands
set cursorline cursorcolumn " highlight line and column of the cursor
set incsearch " perform incremental search
set hlsearch " highlight matches
set showmatch " when bracket is inserted, briefly jump to matching one
set wildmenu " visual autocomplete for command menu
set lazyredraw " redraw only when necessary

" https://github.com/mintty/mintty/wiki/Tips#mode-dependent-cursor-in-vim
" block cursor in normal mode, line cursor in insert mode
let &t_ti.="\e[1 q"
let &t_SI.="\e[5 q"
let &t_EI.="\e[1 q"
let &t_te.="\e[0 q"

" local override
if filereadable($HOME . "/.vimrc.local")
	source ~/.vimrc.local
endif

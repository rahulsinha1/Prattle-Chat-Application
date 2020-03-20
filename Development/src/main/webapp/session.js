if(localStorage.getItem('username') === null){
    window.location.href = 'login.html';
} else {
    window.location.href = 'index.html';
}

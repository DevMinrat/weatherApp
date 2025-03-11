document.addEventListener("DOMContentLoaded", () => {

    const regForm = document.querySelector("#registration-form");

    if (regForm != null) {
        const password = document.querySelector("#password");
        const repeatedPassword = document.querySelector("#repeat-password");
        const confirmPassword = document.querySelector("#confirm-password");

        regForm.addEventListener("submit", (e) => {
            if (password != null && repeatedPassword != null) {
                if (password.value !== repeatedPassword.value) {
                    e.preventDefault();
                    confirmPassword.style.display = "block";
                }
            }
        })
    }

});
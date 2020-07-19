/**
 * Login management
 */

function showPasswordAlert(msg) {
    document.getElementById("id_signup_error_message").style.display = "block";
    document.getElementById("id_signup_error_message").textContent = msg;
}

function pwMismatch() {

    let pw = document.getElementById("id_signup_password_input").value;
    let pw2 = document.getElementById("id_signup_password_2_input").value;

    // If password not entered
    if (pw == '')
        showPasswordAlert("Please enter Password");

    // If confirm password not entered
    else if (pw2 == '')
        showPasswordAlert("Please enter confirm password");

    // If Not same return False.
    else if (pw != pw2) {
        showPasswordAlert("Password did not match: Please try again...")
        return false;
    } else if (pw.length < 6) {
        showPasswordAlert("Password is too short, at least 6 characters.")
        return false;
    }

    // If same return True.
    else {
        return true;
    }
}


function showLogin() {
    document.getElementById("id_login_section").style.display="block";
    document.getElementById("id_signup_section").style.display="none";
}
function showSignUp() {
    document.getElementById("id_signup_section").style.display="block";
    document.getElementById("id_login_section").style.display="none";
}


const urlParams = new URLSearchParams(window.location.search);
if (urlParams.has('errorMessage')) {
    const errorParams = urlParams.get('errorMessage');
    let errorAlert = document.getElementById("id_main_error_message");
    errorAlert.textContent = errorParams;
    errorAlert.style.display="block";

    document.getElementById("id_main_success_message").style.display="none";
} else if (urlParams.has('successMessage')) {
    const successParams = urlParams.get('successMessage');
    let successAlert = document.getElementById("id_main_success_message");
    successAlert.textContent = successParams;
    successAlert.style.display="block";

    document.getElementById("id_main_error_message").style.display="none";
}

document.getElementById('id_signup_password_2_input').onchange = pwMismatchRealtime;
document.getElementById('id_signup_password_input').onchange = pwMismatchRealtime;

function pwMismatchRealtime(e) {
    var pw2 = e.target.value;
    let pw = document.getElementById("id_signup_password_input").value;
    if (pw != pw2) {
        document.getElementById("id_signup_error_message").style.display = "block";
        document.getElementById("id_signup_error_message").textContent = "passwords don't match"

    } else if (pw.length < 6) {
        document.getElementById("id_signup_error_message").style.display = "block";
        document.getElementById("id_signup_error_message").textContent = "Password is too short, at least 6 characters.";
    } else {
        document.getElementById("id_signup_error_message").style.display = "none";
        document.getElementById("id_signup_error_message").textContent = "";
    }
}

(function () { // avoid variables ending up in the global scope


    document.getElementById("id_login_button").addEventListener('click', (e) => {

        var form = e.target.closest("form");
        if (form.checkValidity()) {
            makeCall("POST", 'CheckLogin', e.target.closest("form"),
                function (req) {
                    if (req.readyState == XMLHttpRequest.DONE) {
                        var message = req.responseText;
                        switch (req.status) {
                            case 200:
                                sessionStorage.setItem('username', message);
                                window.location.href = "./WebApp";
                                break;
                            case 400: // bad request
                                document.getElementById("id_error_message").textContent = message;
                                document.getElementById("id_error_message").style.display = "block";
                                break;
                            case 401: // unauthorized
                                document.getElementById("id_error_message").textContent = message;
                                document.getElementById("id_error_message").style.display = "block";
                                break;
                            case 500: // server error
                                document.getElementById("id_error_message").textContent = message;
                                document.getElementById("id_error_message").style.display = "block";
                                break;
                        }
                    }
                }
            );
        } else {
            form.reportValidity();
        }

    });

    document.getElementById("id_signup_login_button").addEventListener('click', (e) => {
        if (pwMismatch()) {
            var form = e.target.closest("form");
            if (form.checkValidity()) {
                makeCall("POST", 'SignUp', e.target.closest("form"),
                    function (req) {
                        if (req.readyState == XMLHttpRequest.DONE) {
                            var message = req.responseText;
                            switch (req.status) {
                                case 200:
                                    sessionStorage.setItem('username', message);
                                    window.location.href = "./WebApp";
                                    break;
                                case 400: // bad request
                                    document.getElementById("id_signup_error_message").textContent = message;
                                    document.getElementById("id_signup_error_message").style.display = "block";
                                    break;
                                case 401: // unauthorized
                                    document.getElementById("id_signup_error_message").textContent = message;
                                    document.getElementById("id_signup_error_message").style.display = "block";
                                    break;
                                case 500: // server error
                                    document.getElementById("id_signup_error_message").textContent = req.responseText;
                                    document.getElementById("id_signup_error_message").style.display = "block";
                                    break;
                            }
                        }
                    }
                );
            } else {
                form.reportValidity();
            }
        }
    })
})();
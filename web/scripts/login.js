/**
 * Login management
 */

function pwMismatch() {

    let pw = document.getElementById("id_signup_password_input").value;
    let pw2 = document.getElementById("id_signup_password_2_input").value;

    // If password not entered
    if (pw == '')
        alert ("Please enter Password");

    // If confirm password not entered
    else if (pw2 == '')
        alert ("Please enter confirm password");

    // If Not same return False.
    else if (pw != pw2) {
        alert ("\nPassword did not match: Please try again...")
        return false;
    }

    // If same return True.
    else{
        alert("Password Match: Welcome to GeeksforGeeks!")
        return true;
    }

}

document.getElementById("id_signup_password_2_input").onchange = pwMismatchRealtime;

function pwMismatchRealtime(e) {
    var pw2 = e.target.value;
    let pw = document.getElementById("id_signup_password_input").value;
    if (pw != pw2) {
        document.getElementById("id_signup_error_message").style.display = "block";
        document.getElementById("id_signup_error_message").textContent = "passwords don't match"

    }
    else {
        document.getElementById("id_signup_error_message").style.display = "none";
        document.getElementById("id_signup_error_message").textContent = "";
    }
}

(function() { // avoid variables ending up in the global scope

    document.getElementById("id_login_button").addEventListener('click', (e) => {
        var form = e.target.closest("form");
        if (form.checkValidity()) {
            makeCall("POST", 'CheckLogin', e.target.closest("form"),
                function(req) {
                    if (req.readyState == XMLHttpRequest.DONE) {
                        var message = req.responseText;
                        switch (req.status) {
                            case 200:
                                sessionStorage.setItem('username', message);
                                window.location.href = "./WebApp";
                                break;
                            case 400: // bad request
                                document.getElementById("id_creation_alert").textContent = message;
                                break;
                            case 401: // unauthorized
                                document.getElementById("id_creation_alert").textContent = message;
                                break;
                            case 500: // server error
                                document.getElementById("id_creation_alert").textContent = message;
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
        var form = e.target.closest("form");
        if (form.checkValidity()) {
            makeCall("POST", 'SignUp', e.target.closest("form"),
                function(req) {
                    if (req.readyState == XMLHttpRequest.DONE) {
                        var message = req.responseText;
                        switch (req.status) {
                            case 200:
                                sessionStorage.setItem('username', message);
                                window.location.href = "./WebApp";
                                break;
                            case 400: // bad request
                                document.getElementById("id_creation_alert").textContent = message;
                                break;
                            case 401: // unauthorized
                                document.getElementById("id_creation_alert").textContent = message;
                                break;
                            case 500: // server error
                                document.getElementById("id_creation_alert").textContent = message;
                                break;
                        }
                    }
                }
            );
        } else {
            form.reportValidity();
        }
    })
})();
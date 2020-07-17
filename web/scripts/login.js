/**
 * Login management
 */

(function() { // avoid variables ending up in the global scope

    document.getElementById("loginbutton").addEventListener('click', (e) => {
        var form = e.target.closest("form");
        if (form.checkValidity()) {
            makeCall("POST", '../CheckMeetingParameters', e.target.closest("form"),
                function(req) {
                    if (req.readyState == XMLHttpRequest.DONE) {
                        var message = req.responseText;
                        switch (req.status) {
                            case 200:
                                sessionStorage.setItem('username', message);
                                window.location.href = "../WebApp";
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

})();
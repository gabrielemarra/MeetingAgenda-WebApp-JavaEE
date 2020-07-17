/**
 * Login management
 */

(function() { // avoid variables ending up in the global scope

    document.getElementById("creationMeetingSubmit").addEventListener('click', (e) => {
        var form = e.target.closest("form");
        alert("funzziona");
        if (form.checkValidity()) {
            makeCall("POST", 'CheckLogin', e.target.closest("form"),
                function(req) {
                    if (req.readyState == XMLHttpRequest.DONE) {
                        var message = req.responseText;
                        switch (req.status) {
                            case 200:
                                //open modal
                                break;
                            case 400: // bad request
                                document.getElementById("errormessage").textContent = message;
                                break;
                            case 401: // unauthorized
                                document.getElementById("errormessage").textContent = message;
                                break;
                            case 500: // server error
                                document.getElementById("errormessage").textContent = message;
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

document.getElementById("title-input").onchange = validateTitle;
function validateTitle(e){
    var title = document.getElementById("title-input");
    if (title.value==="" || title.value.length >= 48 || title.value.length < 3) {
        const titleAlert = document.getElementById("id_title_alert");
        titleAlert.textContent = "invalid title";
        titleAlert.style.display = "block";
    } else {
        const titleAlert = document.getElementById("id_title_alert");
        titleAlert.textContent = "";
        titleAlert.style.display = "none";
    }
}

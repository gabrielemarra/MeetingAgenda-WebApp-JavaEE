(function() { // avoid variables ending up in the global scope

    document.getElementById("creationMeetingSubmit").addEventListener('click', (e) => {
        var target=e.target;
        var form = target.closest("form");
        if (form.checkValidity()) {
            makeCall("POST", '../CheckMeetingParameters', form,
                function(req) {
                    if (req.readyState == XMLHttpRequest.DONE) {
                        var message = req.responseText;
                        switch (req.status) {
                            case 200:
                                //open modal
                                alert("APRI MODAL")
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

    document.getElementById("title-input").onchange = realtimeValidateTitle;
    function realtimeValidateTitle(e){
        var title = e.target.value;
        if (title==="" || title.length >= 48 || title.length < 3) {
            e.target.className= "form-control is-invalid"
            const titleAlert = document.getElementById("id_title_alert");
            titleAlert.textContent = "Invalid title";
            titleAlert.style.display = "block";
        } else {
            e.target.className= "form-control is-valid"
            const titleAlert = document.getElementById("id_title_alert");
            titleAlert.textContent = "";
            titleAlert.style.display = "none";
        }
    }

    document.getElementById("duration-input").onchange = realtimeValidateDuration;
    function realtimeValidateDuration(e){
        var duration = e.target.value;
        if (duration==="" || parseInt(duration) <= 0) {
            e.target.className= "form-control is-invalid"
            const titleAlert = document.getElementById("id_duration_alert");
            titleAlert.textContent = "Invalid duration";
            titleAlert.style.display = "block";
        } else {
            e.target.className= "form-control is-valid"
            const titleAlert = document.getElementById("id_duration_alert");
            titleAlert.textContent = "";
            titleAlert.style.display = "none";
        }
    }

    document.getElementById("max-participants-input").onchange = realtimeValidateParticipants;
    function realtimeValidateParticipants(e){
        var duration = e.target.value;
        if (duration==="" || parseInt(duration) <= 0) {
            e.target.className= "form-control is-invalid"
            const titleAlert = document.getElementById("id_participants_alert");
            titleAlert.textContent = "Invalid max participants";
            titleAlert.style.display = "block";
        } else {
            e.target.className= "form-control is-valid"
            const titleAlert = document.getElementById("id_participants_alert");
            titleAlert.textContent = "";
            titleAlert.style.display = "none";
        }
    }

})();

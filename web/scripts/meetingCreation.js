


(function() { // avoid variables ending up in the global scope

    function openModal() {
        $("#invitationModal").modal("show");
    }

    function InvitationList(_alert, _listcontainer) {

        this.alert = _alert;
        this.listcontainer = _listcontainer;

        this.reset = function () {
            this.listcontainer.style.visibility = "hidden";
        };

        this.show = function () {
            let self = this;
            makeCall("GET", "../GetInvitationList", null,
                function (req) {
                    if (req.readyState == 4) {
                        var message = req.responseText;
                        if (req.status == 200) {
                            var users = JSON.parse(req.responseText);
                            if (users.length == 0) {
                                self.alert.textContent += "no user available";
                                return;
                            }
                            self.update(users); // self visible by closure

                        }
                    } else {
                        self.alert.textContent = message;
                    }
                }
            );

            this.update = function (invitationList) {

                var row, cell;
                this.listcontainer.innerHTML = "";
                // build updated list
                var self = this;

                var template = document.querySelector("#id_invite_user");

                //print a table row for each meeting
                invitationList.forEach(function (user) { // self visible here, not this
                    var row = template.content.cloneNode(true);
                    var label = row.querySelector("label");
                    label.textContent = user.displayedName;
                    label.setAttribute("for", user.userID);

                    self.listcontainer.appendChild(row);
                });
                this.listcontainer.style.visibility = "visible";
            }
        };

    }

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
                                openModal();
                                invitationList = new InvitationList(
                                    document.getElementById("id_modal_alert"),
                                    document.getElementById("id_invitation_list"));
                                invitationList.reset();
                                invitationList.show();
                                break;
                            case 400: // bad request
                               // document.getElementById("errormessage").textContent = message;
                                break;
                            case 401: // unauthorized
                               // document.getElementById("errormessage").textContent = message;
                                break;
                            case 500: // server error
                                //document.getElementById("errormessage").textContent = message;
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

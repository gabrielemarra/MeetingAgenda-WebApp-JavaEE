(function () { // avoid variables ending up in the global scope
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

                var alreadySelectedUsers = false;
                if (sessionStorage.getItem("alreadySelectedUsers")) {
                    //todo
                    // alreadySelectedUsers = parseInt(sessionStorage.getItem("alreadySelectedUsers"));
                }

                //print a table row for each meeting
                invitationList.forEach(function (user) { // self visible here, not this
                    var row = template.content.cloneNode(true);
                    var label = row.querySelector("label");
                    label.textContent = user.displayedName;
                    label.setAttribute("for", user.id);

                    var checkBox = row.querySelector("input");
                    checkBox.setAttribute("field", user.id);
                    checkBox.setAttribute("name", user.id);
                    checkBox.setAttribute("value", user.id);

                    if (alreadySelectedUsers && alreadySelectedUsers === user.id) {
                        //todo check
                        checkBox.setAttribute("checked", "checked");
                    }

                    self.listcontainer.appendChild(row);
                });
                this.listcontainer.style.visibility = "visible";
            }
        };

    }

    //when the user tries to submit a meeting from the home page
    document.getElementById("creationMeetingSubmit").addEventListener('click', (e) => {
        var target = e.target;
        var form = target.closest("form");
        if (form.checkValidity()) {
            makeCall("POST", '../CheckMeetingParameters', form,
                function (req) {
                    if (req.readyState == XMLHttpRequest.DONE) {
                        var message = req.responseText;
                        switch (req.status) {
                            case 200:
                                saveMeetingInfo(req.responseText);
                                // setMeetingInfoIntoInvitationForm();
                                resetInvitationAttempts();
                                //open modal
                                prepareAndShowModal();
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

    function realtimeValidateTitle(e) {
        var title = e.target.value;
        if (title === "" || title.length >= 48 || title.length < 3) {
            e.target.className = "form-control is-invalid"
            const titleAlert = document.getElementById("id_title_alert");
            titleAlert.textContent = "Invalid title";
            titleAlert.style.display = "block";
        } else {
            e.target.className = "form-control is-valid"
            const titleAlert = document.getElementById("id_title_alert");
            titleAlert.textContent = "";
            titleAlert.style.display = "none";
        }
    }

    document.getElementById("duration-input").onchange = realtimeValidateDuration;

    function realtimeValidateDuration(e) {
        var duration = e.target.value;
        if (duration === "" || parseInt(duration) <= 0) {
            e.target.className = "form-control is-invalid"
            const titleAlert = document.getElementById("id_duration_alert");
            titleAlert.textContent = "Invalid duration";
            titleAlert.style.display = "block";
        } else {
            e.target.className = "form-control is-valid"
            const titleAlert = document.getElementById("id_duration_alert");
            titleAlert.textContent = "";
            titleAlert.style.display = "none";
        }
    }

    document.getElementById("max-participants-input").onchange = realtimeValidateParticipants;

    function realtimeValidateParticipants(e) {
        var duration = e.target.value;
        if (duration === "" || parseInt(duration) <= 0) {
            e.target.className = "form-control is-invalid"
            const titleAlert = document.getElementById("id_participants_alert");
            titleAlert.textContent = "Invalid max participants";
            titleAlert.style.display = "block";
        } else {
            e.target.className = "form-control is-valid"
            const titleAlert = document.getElementById("id_participants_alert");
            titleAlert.textContent = "";
            titleAlert.style.display = "none";
        }
    }

    function openModal() {
        $("#invitationModal").modal("show");
    }

    function prepareAndShowModal() {
        openModal();
        let invitationList = new InvitationList(
            document.getElementById("id_modal_alert"),
            document.getElementById("id_invitation_list"));
        invitationList.reset();
        invitationList.show();
    }

    function saveMeetingInfo(jsonMeetingInfo) {
        sessionStorage.setItem("meetingInfo", jsonMeetingInfo);
    }

    function getMeetingInfo() {
        const jsonString = sessionStorage.getItem("meetingInfo");
        return JSON.parse(jsonString);
    }

    function resetInvitationAttempts() {
        sessionStorage.setItem("invitationAttempts", "0");
    }

    function getInvitationAttempts() {
        return parseInt(sessionStorage.getItem("invitationAttempts"))
    }

    function increaseInvitationAttempts() {
        let oldAttempts = getInvitationAttempts();

        //send ajax call to servlet

        let invitationAttempts = oldAttempts + 1;
        sessionStorage.setItem("invitationAttempts", invitationAttempts.toString());
    }

    // function setMeetingInfoIntoInvitationForm() {
    //     let meetingInfo = getMeetingInfo();
    //     let localDate = meetingInfo.localDate;
    //     //month decrease by one because of Date.prototype
    //     let meetingDate = new Date(localDate.year, localDate.month-1, localDate.day, meetingInfo.localTime.hour, meetingInfo.localTime.minute, meetingInfo.localTime.second);
    //
    //     alert("Test");
    //
    //     let meetingDateString = meetingDate.toISOString().slice(0, 19).replace(/T/, " ");
    //
    //     document.getElementById("id_meetingTitle").setAttribute("value", meetingInfo.title);
    //     document.getElementById("id_meetingDateTime").setAttribute("value", meetingDateString);
    // }


    //the user submits a meeting with the invited users list
    document.getElementById("id_modal_submit_button").addEventListener('click', (e) => {
        var test = getInvitationAttempts();

        var form = document.getElementById("id_invitation_form");
        var userSelected = getCheckedUserIDFromForm();
        if (userSelected.length == 0) {
            return;
        } else if (userSelected.length > getMeetingInfo().maxParticipants - 1) {
            return;
        }
        //todo chiamare solo se il controllo locale è andato bene
        if (form.checkValidity()) {
            makeCall("POST", '../CheckInvitations', getInvitationDataForm(),
                function (req) {
                    if (req.readyState == XMLHttpRequest.DONE) {
                        var message = req.responseText;
                        switch (req.status) {
                            case 200:
                                //todo
                                //close modal and refresh tables
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

    function getInvitationDataForm() {
        let meetingInfo = getMeetingInfo();
        let invitationIDs = getCheckedUserIDFromForm();

        let formToSend = new FormData();
        formToSend.append("meetingTitle", meetingInfo.title);
        formToSend.append("meetingDateTime", getDateString(meetingInfo.localDate, meetingInfo.localTime));

        invitationIDs.forEach(element => formToSend.append(element, element));

        return formToSend;
    }

    function getDateString(localDate, localTime) {
        let result;

        result = localDate.year + "-";

        if (localDate.month < 10) {
            result = result + "0";
        }
        result = result + localDate.month + "-";

        if (localDate.day < 10) {
            result = result + "0";
        }
        result = result + localDate.day + " ";

        if (localTime.hour < 10) {
            result = result + "0";
        }
        result = result + localTime.hour + ":";

        if (localTime.minute < 10) {
            result = result + "0";
        }
        result = result + localTime.minute + ":";

        if (localTime.second < 10) {
            result = result + "0";
        }
        result = result + localTime.second;

        return result;
    }

    function getCheckedUserIDFromForm() {
        let checkboxes = document.getElementById("id_invitation_list").getElementsByClassName("form-check-input");
        let checkedID = [];

        for (let i = 0; i < checkboxes.length; i++) {
            if (checkboxes[i].checked) {
                let id = checkboxes[i].getAttribute("value")
                checkedID.push(id);
            }
        }
        // Return the array if it is non-empty, or null
        return checkedID;
    }


})();

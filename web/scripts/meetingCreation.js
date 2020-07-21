function resetInviteError() {
    let titleAlert = document.getElementById("id_modal_alert");
    titleAlert.textContent = "";
    titleAlert.style.display = "none";
}

(function () { // avoid variables ending up in the global scope

    function backToWebApp(msg) {
        alert(msg);
        closeModalAndRefreshTables();
        window.location.href = "../WebApp";
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
                                backToWebApp("error: no users available");
                                return;
                            }
                            self.update(users); // self visible by closure
                            sessionStorage.setItem("availableUsers", req.responseText);

                        }
                        switch (req.status) {
                            case 400: // bad request
                                backToWebApp("invalid request.");
                                break;
                            case 401: // unauthorized
                                forceLocalLogout();
                                break;
                            case 500: // server error
                                backToWebApp("internal server error.");
                                break;
                        }
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
        if (form.checkValidity() && checkAllMeetingInfo()) {
            setGenericCreationAlert();
            makeCall("POST", '../CheckMeetingParameters', form,
                function (req) {
                    if (req.readyState == XMLHttpRequest.DONE) {
                        var message = req.responseText;
                        switch (req.status) {
                            case 200:
                                saveMeetingInfo(req.responseText);
                                resetInvitationAttempts();
                                resetCreatingInfoForm();
                                prepareAndShowModal();
                                break;
                            case 400: // bad request
                                backToWebApp("invalid request.");
                                break;
                            case 401: // unauthorized
                                forceLocalLogout();
                                break;
                            case 500: // server error
                                backToWebApp("internal server error.");
                                break;
                        }
                    }
                }
            );
        } else {
            form.reportValidity(); //todo valutare ritorno?
            setGenericCreationAlert();
        }
    });

    function setGenericCreationAlert() {
        const formGenericAlert = document.getElementById("id_creation_alert");
        if (!checkAllMeetingInfo()) {
            formGenericAlert.className = "alert alert-danger";
            formGenericAlert.textContent = "Please verify all the information.";
            formGenericAlert.style.display = "block";
        } else {
            formGenericAlert.className = "alert alert-danger";
            formGenericAlert.textContent = "";
            formGenericAlert.style.display = "none";
        }
    }

    function setCreationSuccessAlert() {
        const formGenericAlert = document.getElementById("id_creation_alert");
        formGenericAlert.className = "alert alert-success";
        formGenericAlert.textContent = "The meeting has been created successfully!";
        formGenericAlert.style.display = "block";
    }

    function checkAllMeetingInfo() {
        let title = document.getElementById("title-input").value;
        let duration = document.getElementById("duration-input").value;
        let maxParticipants = document.getElementById("max-participants-input").value;
        let dateRead = document.getElementById("date-input").value;
        let timeRead = document.getElementById("time-input").value;

        let now = new Date();
        now.setTime(now.getTime() + 2 * 3600 * 1000);
        let dateNow = now.toISOString().split("T")[0];
        let timeNow = now.toISOString().split("T")[1].substring(0, 5);

        return !(titleIsInvalid(title) || durationIsInvalid(duration) || maxParticipantsAreInvalid(maxParticipants)
            || dateIsInvalid(dateRead, dateNow) || timeIsInvalid(dateRead, dateNow, timeRead, timeNow));
    }

    document.getElementById("title-input").onchange = realtimeValidateTitle;

    function realtimeValidateTitle(e) {
        var title = e.target.value;
        if (titleIsInvalid(title)) {
            e.target.className = "form-control is-invalid";
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

    function resetTitleStatus() {
        let title = document.getElementById("title-input");
        const titleAlert = document.getElementById("id_title_alert");
        titleAlert.textContent = "";
        titleAlert.style.display = "none";
        title.className = "form-control";
    }

    function titleIsInvalid(title) {
        return title === "" || title.length >= 60 || title.length < 3;
    }

    document.getElementById("duration-input").onchange = realtimeValidateDuration;

    function realtimeValidateDuration(e) {
        let duration = e.target.value;
        const durationAlert = document.getElementById("id_duration_alert");
        if (durationIsInvalid(duration)) {
            e.target.className = "form-control is-invalid";
            durationAlert.textContent = "Meeting duration is too short, at least 5 minutes";
            durationAlert.style.display = "block";
        } else {
            e.target.className = "form-control is-valid"
            durationAlert.textContent = "";
            durationAlert.style.display = "none";
        }
    }

    function resetDurationStatus() {
        let duration = document.getElementById("title-input");
        const durationAlert = document.getElementById("id_duration_alert");
        // duration.value = duration.defaultValue;
        durationAlert.textContent = "";
        durationAlert.style.display = "none";
        duration.className = "form-control";
    }

    function durationIsInvalid(duration) {
        return duration === "" || parseInt(duration) < 5;
    }

    document.getElementById("max-participants-input").onchange = realtimeValidateParticipants;

    function realtimeValidateParticipants(e) {
        let maxParticipants = e.target.value;
        const maxParticipantsAlert = document.getElementById("id_participants_alert");
        if (maxParticipantsAreInvalid(maxParticipants)) {
            e.target.className = "form-control is-invalid";
            maxParticipantsAlert.textContent = "Invalid max participants, at least 2";
            maxParticipantsAlert.style.display = "block";
        } else {
            e.target.className = "form-control is-valid"
            maxParticipantsAlert.textContent = "";
            maxParticipantsAlert.style.display = "none";
        }
    }

    function resetMaxParticipantsStatus() {
        let maxParticipants = document.getElementById("title-input");
        const maxParticipantsAlert = document.getElementById("id_participants_alert");
        // maxParticipants.value = maxParticipants.defaultValue;
        maxParticipantsAlert.textContent = "";
        maxParticipantsAlert.style.display = "none";
        maxParticipants.className = "form-control";
    }

    function maxParticipantsAreInvalid(maxParticipants) {
        return maxParticipants === "" || parseInt(maxParticipants) < 2;
    }

    document.getElementById("date-input").onchange = realtimeValidateDate;

    function realtimeValidateDate(e) {
        let now = new Date();
        now.setTime(now.getTime() + 2 * 3600 * 1000);
        let dateNow = now.toISOString().split("T")[0];
        let timeNow = now.toISOString().split("T")[1].substring(0, 5);

        let timeRead = document.getElementById("time-input").value;
        let dateRead = e.target.value;

        const dateAlert = document.getElementById("id_date_alert");
        if (dateIsInvalid(dateRead, dateNow)) {
            e.target.className = "form-control is-invalid";
            dateAlert.textContent = "Invalid date, please select a future date";
            dateAlert.style.display = "block";
        } else if (dateRead === dateNow) {
            e.target.className = "form-control is-valid"
            dateAlert.textContent = "";
            dateAlert.style.display = "none";

            if (timeRead < timeNow) {
                document.getElementById("time-input").className = "form-control is-invalid"
                const titleAlert = document.getElementById("id_time_alert");
                titleAlert.textContent = "Invalid time, please select a future time";
                titleAlert.style.display = "block";
            }

        } else {
            e.target.className = "form-control is-valid";
            dateAlert.textContent = "";
            dateAlert.style.display = "none";
        }
    }

    function resetDateStatus() {
        let date = document.getElementById("title-input");
        const dateAlert = document.getElementById("id_date_alert");
        // date.value = date.defaultValue;
        dateAlert.textContent = "";
        dateAlert.style.display = "none";
        date.className = "form-control";
    }

    function dateIsInvalid(dateRead, dateNow) {
        return dateRead < dateNow;
    }

    document.getElementById("time-input").onchange = realtimeValidateTime;

    function realtimeValidateTime(e) {
        let now = new Date();
        now.setTime(now.getTime() + 2 * 3600 * 1000);
        let dateNow = now.toISOString().split("T")[0];
        let timeNow = now.toISOString().split("T")[1].substring(0, 5);

        let dateRead = document.getElementById("date-input").value;
        let timeRead = e.target.value;

        const timeAlert = document.getElementById("id_time_alert");
        if (timeIsInvalid(dateRead, dateNow, timeRead, timeNow)) {
            e.target.className = "form-control is-invalid";
            timeAlert.textContent = "Invalid time, please select a future time";
            timeAlert.style.display = "block";
        } else {
            e.target.className = "form-control is-valid"
            timeAlert.textContent = "";
            timeAlert.style.display = "none";
        }
    }

    function resetTimeStatus() {
        let time = document.getElementById("title-input");
        const timeAlert = document.getElementById("id_time_alert");
        // time.value = time.defaultValue;
        timeAlert.textContent = "";
        timeAlert.style.display = "none";
        time.className = "form-control";
    }

    function timeIsInvalid(dateRead, dateNow, timeRead, timeNow) {
        return dateRead === dateNow && timeRead <= timeNow;
    }

    document.getElementById("creationMeetingCancel").addEventListener('click', (e) => {
        resetCreatingInfoForm();
    });


    function resetCreatingInfoForm() {
        let duration = document.getElementById("duration-input");
        duration.value = duration.defaultValue;
        duration.className = "form-control";
        let maxParticipants = document.getElementById("max-participants-input");
        maxParticipants.value = maxParticipants.defaultValue;
        maxParticipants.className = "form-control";
        let dateRead = document.getElementById("date-input");
        dateRead.value = dateRead.defaultValue;
        dateRead.className = "form-control";
        let timeRead = document.getElementById("time-input");
        timeRead.value = timeRead.defaultValue;
        timeRead.className = "form-control";

        resetTitleStatus();
        resetDurationStatus();
        resetMaxParticipantsStatus();
        resetDateStatus();
        resetTimeStatus();
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
        document.getElementById("id_modal_alert").style.display = "none";
    }

    function saveMeetingInfo(jsonMeetingInfo) {
        sessionStorage.setItem("meetingInfo", jsonMeetingInfo);
    }

    function getMeetingInfo() {
        const jsonString = sessionStorage.getItem("meetingInfo");
        return JSON.parse(jsonString);
    }

    function resetMeetingInfo() {
        sessionStorage.removeItem("meetingInfo");
    }

    function resetInvitationAttempts() {
        sessionStorage.setItem("invitationAttempts", "0");
    }

    function getInvitationAttempts() {
        return parseInt(sessionStorage.getItem("invitationAttempts"))
    }

    function increaseInvitationAttempts() {
        let oldAttempts = getInvitationAttempts();

        oldAttempts++;

        sessionStorage.setItem("invitationAttempts", oldAttempts.toString());

        makeCall("POST", '../IncreaseAttempts', getMeetingInfoForm(), function (req) {
            if (req.readyState == XMLHttpRequest.DONE) {
                switch (req.status) {
                    case 400: // bad request
                        backToWebApp("invalid request.");
                        break;
                    case 401: // unauthorized
                        forceLocalLogout();
                        break;
                    case 500: // server error
                        backToWebApp("internal server error.");
                        break;
                }
            }
        });

    }

    document.getElementById("id_modal_submit_button").addEventListener('click', (e) => {
        var form = document.getElementById("id_invitation_form");
        var userSelected = getCheckedUserIDFromForm();
        if (userSelected.length <= 0) {
            let titleAlert = document.getElementById("id_modal_alert");
            titleAlert.textContent = "Please select at least one user";
            titleAlert.style.display = "block";
            //todo + reminder appena seleziona qualcuno l'errore scompare
            return;
        } else if (userSelected.length > getAvailableUsers()) {
            let titleAlert = document.getElementById("id_modal_alert");
            titleAlert.textContent = "Local form error";
            titleAlert.style.display = "block";
            //homepage con errore del text content
            return;
        }  else if (userSelected.length > getMeetingInfo().maxParticipants - 1) {
            increaseInvitationAttempts();
            let titleAlert = document.getElementById("id_modal_alert");
            let numberDesect = userSelected.length - (getMeetingInfo().maxParticipants - 1);
            titleAlert.textContent = "Too many user selected. Deselect at least " + numberDesect + ((numberDesect > 1) ? " users." : " user.");
            titleAlert.style.display = "block";

        } else {
            if (form.checkValidity()) {
                makeCall("POST", '../CheckInvitations', getInvitationDataForm(),
                    function (req) {
                        if (req.readyState == XMLHttpRequest.DONE) {
                            switch (req.status) {
                                case 200:
                                    setCreationSuccessAlert();
                                    closeModalAndRefreshTables();
                                    break;
                                case 400: // bad request
                                    backToWebApp("bad request, unable to create meeting");
                                    break;
                                case 401: // unauthorized
                                    backToWebApp("error: unauthorized");
                                    forceLocalLogout();
                                    break;
                                case 500: // server error
                                    backToWebApp("internal server error, try again later");
                                    break;
                            }
                        }
                    }
                );
            } else {
                form.reportValidity();
            }
            return;
        }

        if (getInvitationAttempts() >= 3) {
            closeModalAndRefreshTables();
            openCancellationModal();
        }
    });

    document.getElementById("id_modal_cancel_button").addEventListener('click', (e) => {
        makeCall("POST", '../DeleteTemporaryMeeting', getMeetingInfoForm(), function (req) {
            if (req.readyState == XMLHttpRequest.DONE) {
                switch (req.status) {
                    case 200:
                        resetMeetingInfo();
                        break;
                    case 400: // bad request
                        backToWebApp("invalid request.");
                        break;
                    case 401: // unauthorized
                        forceLocalLogout();
                        break;
                    case 500: // server error
                        backToWebApp("internal server error.");
                        break;
                }
            }
        });
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

    function getMeetingInfoForm() {
        let meetingInfo = getMeetingInfo();

        let formToSend = new FormData();
        formToSend.append("meetingTitle", meetingInfo.title);
        formToSend.append("meetingDateTime", getDateString(meetingInfo.localDate, meetingInfo.localTime));

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

    function closeModal() {
        $('#invitationModal').modal('hide')
    }

    function closeModalAndRefreshTables() {
        closeModal();
        cleanModalBody();

        let invitedList = new InvitedAtList(
            document.getElementById("id_alert"),
            document.getElementById("id_invitedAtList"));
        let createdMeetingsList = new CreatedList(
            document.getElementById("id_c_alert"),
            document.getElementById("id_CreatedList"));
        invitedList.reset();
        createdMeetingsList.reset();
        invitedList.show();
        createdMeetingsList.show();
    }

    function cleanModalBody() {
        document.getElementById("id_invitation_list").innerHTML = "";
    }

    function getAvailableUsers() {
        return JSON.parse(sessionStorage.getItem("availableUsers"));
    }

    function openCancellationModal() {
        $("#cancellationModal").modal("show");
    }

})();

window.addEventListener("load", () => {
    let invitedList;
    let createdMeetingsList;
    if (sessionStorage.getItem("username") == null) {
        window.location.href = "../index.html";
    } else {
        invitedList = new InvitedAtList(
            document.getElementById("id_alert"),
            document.getElementById("id_invitedAtList"));
        createdMeetingsList = new CreatedList(
            document.getElementById("id_c_alert"),
            document.getElementById("id_CreatedList"));
        invitedList.reset();
        createdMeetingsList.reset();
        invitedList.show();
        createdMeetingsList.show();

    }

}, false);

function InvitedAtList(_alert, _listcontainer) {

    this.alert = _alert;
    this.listcontainer = _listcontainer;

    this.reset = function () {
        this.listcontainer.style.visibility = "hidden";
    };

    this.show = function () {
        let self = this;
        makeCall("GET", "../GetMeetingsData?my=false", null,
            function (req) {
                if (req.readyState == 4) {
                    var message = req.responseText;
                    if (req.status == 200) {
                        var invitedAtMeetings = JSON.parse(req.responseText);
                        if (invitedAtMeetings.length == 0) {
                            self.alert.textContent += "You've not been invited to any meeting yet";
                            return;
                        }
                        self.update(invitedAtMeetings); // self visible by closure

                    }
                    switch (req.status) {
                        case 400: // bad request
                            forceLocalLogoutMessage("bad request");
                            break;
                        case 401: // unauthorized
                            self.alert.textContent = "error: unauthorized";
                            forceLocalLogout();
                            break;

                        case 404:
                            self.alert.textContent = "error: bad request url";
                            forceLocalLogoutMessage("invalid request path");
                            break;
                        case 500: // server error
                            self.alert.textContent = "internal server error, unable to fetch data";
                            forceLocalLogoutMessage("internal server error, please try again later");
                            break;
                    }
                }
            }
        );
    };

    this.update = function (arrayMeetings) {

        var row, cell;
        this.listcontainer.innerHTML = "<thead>\n" +
            "                                <tr>\n" +
            "                                    <th>Title</th>\n" +
            "                                    <th>Invited by</th>\n" +
            "                                    <th>Date</th>\n" +
            "                                    <th>Start</th>\n" +
            "                                    <th>End</th> <!-- start + duration -->\n" +
            "                                </tr>\n" +
            "                                </thead>"; // empty the table body
        // build updated list
        var self = this;

        //print a table row for each meeting
        arrayMeetings.forEach(function (meeting) { // self visible here, not this

            row = document.createElement("tr");

            cell = document.createElement("td");
            cell.textContent = meeting.title;
            row.appendChild(cell);

            var creatorCell = document.createElement("td");
            creatorCell.textContent = meeting.creatorName;
            row.appendChild(creatorCell);

            var dateCell = document.createElement("td");
            dateCell.textContent = getDateString(meeting);
            row.appendChild(dateCell);

            var startCell = document.createElement("td");
            startCell.textContent = getTimeFormatted(meeting.time);
            row.appendChild(startCell);

            var endCell = document.createElement("td");
            endCell.textContent = getTimeFormatted(meeting.endTime);
            row.appendChild(endCell);

            self.listcontainer.appendChild(row);
        });
        this.listcontainer.style.visibility = "visible";
    }
}

function CreatedList(_alert, _listcontainer) {

    this.alert = _alert;
    this.listcontainer = _listcontainer;

    this.reset = function () {
        this.listcontainer.style.visibility = "hidden";
    };

    this.show = function () {
        let self = this;
        makeCall("GET", "../GetMeetingsData?my=true", null,
            function (req) {
                if (req.readyState == 4) {
                    var message = req.responseText;
                    if (req.status == 200) {
                        var invitedAtMeetings = JSON.parse(req.responseText);
                        if (invitedAtMeetings.length == 0) {
                            self.alert.textContent += "You have not yet created a meeting.";

                            return;
                        }
                        self.update(invitedAtMeetings); // self visible by closure

                    }
                    switch (req.status) {
                        case 400: // bad request
                            forceLocalLogoutMessage("bad request");
                            break;
                        case 401: // unauthorized
                            forceLocalLogout();
                            break;
                        case 500: // server error
                            forceLocalLogoutMessage("internal server error");
                            break;
                    }
                }
            }
        );
    };

    this.update = function (arrayMeetings) {

        var row, cell;
        this.listcontainer.innerHTML = "                                <thead>\n" +
            "                                <tr>\n" +
            "                                    <th>Title</th>\n" +
            "                                    <th>Date</th>\n" +
            "                                    <th>Start</th>\n" +
            "                                    <th>End</th> <!-- start + duration -->\n" +
            "                                    <th>Max. # Participants</th>\n" +
            "                                </tr>\n" +
            "                                </thead>"; // empty the table body
        // build updated list
        var self = this;

        //print a table row for each meeting
        arrayMeetings.forEach(function (meeting) { // self visible here, not this

            row = document.createElement("tr");

            cell = document.createElement("td");
            cell.textContent = meeting.title;
            row.appendChild(cell);

            var dateCell = document.createElement("td");
            dateCell.textContent = getDateString(meeting);
            row.appendChild(dateCell);

            var startCell = document.createElement("td");
            startCell.textContent = getTimeFormatted(meeting.time);
            row.appendChild(startCell);

            var endCell = document.createElement("td");
            endCell.textContent = getTimeFormatted(meeting.endTime);
            row.appendChild(endCell);

            var maxParticipants = document.createElement("td");
            maxParticipants.textContent = meeting.maxParticipants + " participants";
            row.appendChild(maxParticipants);

            self.listcontainer.appendChild(row);
        });
        this.listcontainer.style.visibility = "visible";
    }
}

function logOut() {
    makeCall("GET", '../Logout', null, function (req) {
        if (req.readyState == XMLHttpRequest.DONE) {
            switch (req.status) {
                case 200:
                    localStorage.removeItem("username");
                    window.location.href = "../index.html";
                    break;
                case 400: // bad request
                    logOut();
                    break;
                case 401: // unauthorized
                    forceLocalLogout();
                    break;
                case 500: // server error
                    logOut();
                    break;
            }
        }
    });

}

function getDateString(meeting) {
    return meeting.date.year + "-" + meeting.date.month + "-" + getDayFormatted(meeting.date.day);
}

function getDayFormatted(day) {
    if (day < 10) {
        return "0" + day;
    } else return day;
}

function getTimeFormatted(time) {
    return time.hour + ":" + time.minute + ":" + time.second;
}

function getEndTimeFormatted(startTime, duration) {
    var h = startTime.hour;
    var m = startTime.minute;
    var s = startTime.second;
}

function logOut() {
    makeCall("GET", '../Logout', null, function (req) {
        if (req.readyState == XMLHttpRequest.DONE) {
            switch (req.status) {
                case 200:
                    localStorage.removeItem("username");
                    window.location.href = "../index.html?successMessage=You have successfully logged out!";
                    break;
                case 400: // bad request
                    forceLocalLogoutMessage("bad request");
                    break;
                case 401: // unauthorized
                    forceLocalLogout();
                    break;
                case 500: // server error
                    forceLocalLogoutMessage("internal server error, please try again later");
                    break;
            }
        }
    });
}

function forceLocalLogout() {
    localStorage.removeItem("username");
    window.location.href = "../index.html?errorMessage=Please log in to continue";
}
function forceLocalLogoutMessage(msg) {
    localStorage.removeItem("username");
    window.location.href = "../index.html?errorMessage=" + msg;
}
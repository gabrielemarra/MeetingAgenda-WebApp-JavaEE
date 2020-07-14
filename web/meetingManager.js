pageOrchestrator = new PageOrchestrator(); // main controller

window.addEventListener("load", () => {
    let missionsList;
    if (sessionStorage.getItem("username") == null) {
        window.location.href = "../index.html";
    } else {
        // pageOrchestrator.start(); // initialize the components
        //pageOrchestrator.refresh();
        missionsList = new InvitedAtList(
            document.getElementById("id_alert"),
            document.getElementById("id_invitedAtList"));
        missionsList.show()
    } // display initial content
}, false);

function InvitedAtList(_alert, _listcontainer) {

    this.alert = _alert;
    this.listcontainer = _listcontainer;

    this.reset = function() {
        this.listcontainer.style.visibility = "hidden";
    }

    this.show = function(next) {
        let self = this;
        makeCall("GET", "GetMeetingsData", null,
            function(req) {
                if (req.readyState == 4) {
                    var message = req.responseText;
                    if (req.status == 200) {
                        var invitedAtMeetings = JSON.parse(req.responseText);
                        if (missionsToShow.length == 0) {
                            self.alert.textContent = "You've not been invited to any meeting yet";
                            return;
                        }
                        self.update(invitedAtMeetings); // self visible by closure
                        if (next) next(); // show the default element of the list if present
                    }
                } else {
                    self.alert.textContent = message;
                }
            }
        );
    };

    this.update = function(arrayMeetins) {
        var elem, i, row, destcell, datecell, linkcell, anchor;
        this.listcontainerbody.innerHTML = ""; // empty the table body
        // build updated list
        var self = this;
        arrayMissions.forEach(function(mission) { // self visible here, not this
            row = document.createElement("tr");
            destcell = document.createElement("td");
            destcell.textContent = mission.destination;
            row.appendChild(destcell);
            datecell = document.createElement("td");
            datecell.textContent = mission.startDate;
            row.appendChild(datecell);
            linkcell = document.createElement("td");
            anchor = document.createElement("a");
            linkcell.appendChild(anchor);
            linkText = document.createTextNode("Show");
            anchor.appendChild(linkText);
            //anchor.missionid = mission.id; // make list item clickable
            anchor.setAttribute('missionid', mission.id); // set a custom HTML attribute
            anchor.addEventListener("click", (e) => {
                // dependency via module parameter
                missionDetails.show(e.target.getAttribute("missionid")); // the list must know the details container
            }, false);
            anchor.href = "#";
            row.appendChild(linkcell);
            self.listcontainerbody.appendChild(row);
        });
        this.listcontainer.style.visibility = "visible";

    }


}


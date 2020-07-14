// pageOrchestrator = new PageOrchestrator(); // main controller

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

(function() {

    function Wizard(wizardId, alert) {
        // minimum date the user can choose, in this case now and in the future
        var now = new Date(),
            formattedDate = now.toISOString().substring(0, 10);
        this.wizard = wizardId;
        this.alert = alert;

        this.wizard.querySelector('input[type="date"]').setAttribute("min", formattedDate);

        this.registerEvents = function () {

            // Manage submit button
            this.wizard.querySelector("button[type='button'].submit").addEventListener('click', (e) => {
                var target = e.target;

                var eventFieldSet = target.closest("fieldset"),
                    valid = true;
                for (i = 0; i < eventFieldSet.elements.length; i++) {
                    // if (!eventFieldSet.elements[i].checkValidity()) {
                    //     eventFieldSet.elements[i].reportValidity();
                    //     valid = false;
                    //     break;
                    // }
                }

                // if (valid) {
                //     var self = this;
                //     makeCall("POST", 'CreateMission', e.target.closest("form"),
                //         function(req) {
                //             if (req.readyState == XMLHttpRequest.DONE) {
                //                 var message = req.responseText; // error message or mission id
                //                 if (req.status == 200) {
                //                     orchestrator.refresh(message); // id of the new mission passed
                //                 } else {
                //                     self.alert.textContent = message;
                //                     self.reset();
                //                 }
                //             }
                //         }
                //     );
                // }
            });
            // Manage cancel button
            this.wizard.querySelector("input[type='button'].cancel").addEventListener('click', (e) => {
                var target = e.target;
                target.closest('form').reset();
                this.reset();
            });
        };

        this.reset = function () {
            // var fieldsets = document.querySelectorAll("#" + this.wizard.id + " fieldset");
            // fieldsets[0].hidden = false;
            // fieldsets[1].hidden = true;
            // fieldsets[2].hidden = true;

        }
    }

    this.start = function () {
        wizard = new Wizard(document.getElementById("creationMeetingForm"));
        wizard.registerEvents(this);
    }();

})();
